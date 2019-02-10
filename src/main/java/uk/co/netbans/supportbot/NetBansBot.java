package uk.co.netbans.supportbot;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.RichPresence;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.hooks.InterfacedEventManager;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import uk.co.netbans.supportbot.CommandFramework.CommandFramework;
import uk.co.netbans.supportbot.Commands.Misc.Timezones;
import uk.co.netbans.supportbot.Commands.Moderation.Purge.Mention;
import uk.co.netbans.supportbot.Commands.Music.*;
import uk.co.netbans.supportbot.Message.Messenger;
import uk.co.netbans.supportbot.Commands.Moderation.Purge.Link;
import uk.co.netbans.supportbot.Commands.Moderation.Purge.Purge;
import uk.co.netbans.supportbot.Storage.SQLManager;
import uk.co.netbans.supportbot.Commands.Admin.*;
import uk.co.netbans.supportbot.Commands.Admin.PermChildren.CreateGroup;
import uk.co.netbans.supportbot.Commands.Admin.PermChildren.Group;
import uk.co.netbans.supportbot.Commands.Admin.PermChildren.User;
import uk.co.netbans.supportbot.Music.MusicManager;
import uk.co.netbans.supportbot.Commands.Support.Help;
import uk.co.netbans.supportbot.Commands.Support.Ticket;
import uk.co.netbans.supportbot.Support.Listeners.*;
import uk.co.netbans.supportbot.Task.ExpiryCheckTask;
import uk.co.netbans.supportbot.Utils.Util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NetBansBot {
    private JDA jda;
    private Messenger messenger;
    private MusicManager music;

    private Path directory;
    private Path logDirectory;
    private Path musicDirectory;
    private JSONObject conf;
    private SQLManager sqlManager;
    private CommandFramework framework;
    private NetBansBot bot = this;

    public void init(Path directory) throws Exception {
        this.directory = directory;

        System.out.println("Initializing Config!");
        try {
            initConfig();
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize config!", e);
        }

        System.out.println("Loading SQL!");
        sqlManager = new SQLManager(directory.toFile());

        try {
            Path path = Paths.get(directory + "/logs");
            if (!path.toFile().exists()) {
                path.toFile().mkdir();
                logDirectory = path;
            }
            logDirectory = path;
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Path path = Paths.get(directory + "/music");
            if (!path.toFile().exists()) {
                path.toFile().mkdir();
                musicDirectory = path;
            }
            musicDirectory = path;
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (conf.get("token") == "add_me") {
            System.out.println("Found unedited config. Please add the token.");
            System.exit(1);
        }

        this.jda = new JDABuilder(AccountType.BOT)
                .setToken((String) conf.get("token"))
                .setBulkDeleteSplittingEnabled(false)
                .setEventManager(new ThreadedEventManager())
                .build();
        jda.awaitReady();

        System.out.println("Loading Messenger...");
        this.messenger = new Messenger();

        System.out.println("Registering Commands...");
        // old
        this.jda.addEventListener(framework = new CommandFramework(this));
        registerCommands();

        this.jda.addEventListener(new PrivateMessageListener(this));
        this.jda.addEventListener(new SupportCategoryListener(this));
        this.jda.addEventListener(new TicketChannelsReactionListener(this));
        this.jda.addEventListener(new SuggestionListener(this));
        this.jda.addEventListener(new HelpMessageReactionListener(this));
        this.jda.addEventListener(new TagListener(this));

        Executors.newSingleThreadExecutor().submit(new Runnable() {
            @Override
            public void run() {
                    Member member = Util.randomMember(bot);
                    if (member.getGame() != null) {
                        if (member.getGame().asRichPresence().getDetails().toLowerCase().contains("netbans"))
                            jda.getPresence().setPresence(OnlineStatus.ONLINE, Game.watching(member.getUser().getName() + " work on me!"));
                    } else {
                        jda.getPresence().setPresence(OnlineStatus.ONLINE, Game.watching(member.getUser().getName() +  (member.getGame() != null ? " play " + member.getGame().getName() : "") + "!"));
                    }
                    try {
                        Thread.sleep(Duration.ofMinutes(5).toMillis());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
            }
        });

        Executors.newSingleThreadExecutor().submit(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    Util.getSupportChannels(bot);
                    try {
                        Thread.sleep(Duration.ofMinutes(10).toMillis());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        TimerTask timerTask = new ExpiryCheckTask(this);
        Timer timer = new Timer(true);
        timer.schedule(timerTask, Duration.ofMinutes(0).toMillis(), Duration.ofMinutes(15).toMillis());

        System.out.println("Loading Music Manager...");
        this.music = new MusicManager();

        System.out.println("Finished Loading | Now accepting input.");

    }

    public void shutdown() {
        System.out.println("Initiating Shutdown...");
        getJDA().shutdown();
        System.out.println("Shutdown Complete.");
    }

    public List<String[]> getTips() throws IOException, ParseException {
        BufferedReader reader = Files.newBufferedReader(directory.resolve("config.json"));
        JSONParser jsonParser = new JSONParser();
        JSONObject result = (JSONObject) jsonParser.parse(reader);
        JSONArray tips = (JSONArray) result.get("tips");
        List<String[]> tipArray = new ArrayList<>();
        for (Object obj : tips) {
            JSONObject jsonObject = (JSONObject) obj;
            String word = (String) jsonObject.get("word");
            String suggestion = (String) jsonObject.get("suggestion");
            String[] put = new String[]{word, suggestion};
            tipArray.add(put);
        }
        return tipArray;
    }

    public List<String> getReplies() {
        List<String> replyArray = new ArrayList<>();
        try {
            BufferedReader reader = Files.newBufferedReader(directory.resolve("config.json"));
            JSONParser jsonParser = new JSONParser();
            JSONObject result = (JSONObject) jsonParser.parse(reader);
            JSONArray tips = (JSONArray) result.get("replies");
            for (Object obj : tips) {
                JSONObject jsonObject = (JSONObject) obj;
                String word = (String) jsonObject.get("reply");
                replyArray.add(word);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return replyArray;
    }

    public JDA getJDA() {
        return this.jda;
    }

    public Messenger getMessenger() {
        return this.messenger;
    }

    public long getGuildID() {
        return Long.valueOf((String)bot.getConf().get("guildID"));
    }

    public long getSupportCategoryID() {
        return Long.valueOf((String)bot.getConf().get("category"));
    }

    public MusicManager getMusicManager() {
        return this.music;
    }

    public SQLManager getSqlManager() {
        return sqlManager;
    }

    public CommandFramework getFramework() {
        return framework;
    }

    @SuppressWarnings("unchecked")
    private void initConfig() throws Exception {
        Path config = directory.resolve("config.json");
        if (!Files.exists(config)) {
            Files.createFile(config);

            JSONObject jo = new JSONObject();
            jo.put("token", "add_me");
            jo.put("category","add_me");
            jo.put("logChannelID", "add_me");
            jo.put("guildID", "add_me");
            jo.put("commandPrefix", "!");
            jo.put("timezones", new JSONArray());

            try (BufferedWriter writer = Files.newBufferedWriter(config)) {
                writer.write(jo.toJSONString());
                writer.flush();
            }
        }

        try (BufferedReader reader = Files.newBufferedReader(config)) {
            JSONParser parser = new JSONParser();
            this.conf = (JSONObject) parser.parse(reader);
        }
    }

    public void reloadConfig() {
        try {
            this.initConfig();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JSONObject getConf() {
        return conf;
    }

    public String getCommandPrefix() {
        return (String) getConf().get("commandPrefix");
    }

    public Path getLogDirectory() {
        return logDirectory;
    }

    public Path getMusicDirectory() {
        return musicDirectory;
    }

    // potentially un necessary.
    private final class ThreadedEventManager extends InterfacedEventManager {
        private final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()+1);

        @Override
        public void handle(Event e) {
            executor.submit(() -> super.handle(e));
        }
    }

    private void registerCommands() {
        framework.registerCommands(new Test());
        framework.registerCommands(new Purge());

        //Perm Commands And Children
        framework.registerCommands(new Perm());
        framework.registerCommands(new uk.co.netbans.supportbot.Commands.Admin.PermChildren.List());
        framework.registerCommands(new User());
        framework.registerCommands(new Group());
        framework.registerCommands(new CreateGroup());

        //Other Admin Commands
        framework.registerCommands(new Tips());
        framework.registerCommands(new ManualChannel());
        framework.registerCommands(new Faq());
        framework.registerCommands(new ConfigReload());
        framework.registerCommands(new Embedify());
        framework.registerCommands(new Say());

        //Moderation Commands
        framework.registerCommands(new uk.co.netbans.supportbot.Commands.Moderation.Purge.User());
        framework.registerCommands(new Link());
        framework.registerCommands(new Mention());

        //Normal Commands (No Perms)
        framework.registerCommands(new Help());
        framework.registerCommands(new Ticket());
        framework.registerCommands(new Timezones());

        //Music
        framework.registerCommands(new Play());
        framework.registerCommands(new Current());
        framework.registerCommands(new Export());
        framework.registerCommands(new Queue());
        framework.registerCommands(new Reset());
        framework.registerCommands(new Search());
        framework.registerCommands(new Shuffle());
        framework.registerCommands(new Skip());
        framework.registerCommands(new Volume());
        framework.registerCommands(new Playlist());
        framework.registerCommands(new Repeat());
    }

}
