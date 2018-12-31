package uk.co.netbans.supportbot;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.hooks.InterfacedEventManager;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import uk.co.netbans.supportbot.BenCMDFramework.CommandFramework;
import uk.co.netbans.supportbot.Message.Messenger;
import uk.co.netbans.supportbot.Moderation.Purge.Link;
import uk.co.netbans.supportbot.Moderation.Purge.Purge;
import uk.co.netbans.supportbot.Storage.SQLManager;
import uk.co.netbans.supportbot.Support.Command.Admin.*;
import uk.co.netbans.supportbot.Support.Command.Admin.PermChildren.CreateGroup;
import uk.co.netbans.supportbot.Support.Command.Admin.PermChildren.Group;
import uk.co.netbans.supportbot.Support.Command.Admin.PermChildren.User;
import uk.co.netbans.supportbot.Support.Command.CommandListener;
import uk.co.netbans.supportbot.Music.MusicManager;
import uk.co.netbans.supportbot.Support.Command.CommandRouter;
import uk.co.netbans.supportbot.Support.Command.Support.Help;
import uk.co.netbans.supportbot.Support.Command.Support.Ticket;
import uk.co.netbans.supportbot.Support.Listeners.SuggestionListener;
import uk.co.netbans.supportbot.Support.Listeners.SupportCategoryListener;
import uk.co.netbans.supportbot.Support.Listeners.PrivateMessageListener;
import uk.co.netbans.supportbot.Support.Listeners.TicketChannelsReactionListener;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NetBansBot {
    private JDA jda;
    private Messenger messenger;
    private MusicManager music;

    private Path directory;
    private Path logDirectory;
    private JSONObject conf;
    private EnumMap<PermType, List<Long>> perms;
    private SQLManager sqlManager;
    private CommandListener listener;
    private CommandFramework framework;

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

        System.out.println("Initializing Perms!");
        try {
            initPerms();
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize perms!", e);
        }

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
        this.jda.addEventListener(listener = new CommandListener(this));
        registerCommands();

        this.jda.addEventListener(new PrivateMessageListener(this));
        this.jda.addEventListener(new SupportCategoryListener(this));
        this.jda.addEventListener(new TicketChannelsReactionListener(this));
        this.jda.addEventListener(new SuggestionListener(this));

        System.out.println("Loading Music Manager...");
        this.music = new MusicManager();

        System.out.println("Finished Loading | Now accepting input.");

    }

    public void shutdown() {
        System.out.println("Initiating Shutdown...");
        // shutdown code here.
        getJDA().shutdown();

        try {
            this.writePerms();
        } catch (Exception e) {
            e.printStackTrace();
        }

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

    public JDA getJDA() {
        return this.jda;
    }

    public Messenger getMessenger() {
        return this.messenger;
    }

    public MusicManager getMusicManager() {
        return this.music;
    }

    public CommandRouter getCommandRouter() {
        return getListener().getRouter();
    }

    public CommandListener getListener() {
        return listener;
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

    @SuppressWarnings("unchecked")
    private void initPerms() throws Exception {
        Path perms = directory.resolve("perms.json");
        if (!Files.exists(perms)) {
            Files.createFile(perms);

            JSONObject jo = new JSONObject();
            JSONArray array = new JSONArray();
            array.add("97995963137802240");
            array.add("138051041529692161");
            jo.put("admin", array);
            JSONArray modsArray = new JSONArray();
            jo.put("mod", modsArray);

            try (BufferedWriter writer = Files.newBufferedWriter(perms)) {
                writer.write(jo.toJSONString());
                writer.flush();
            }
        }

        try (BufferedReader reader = Files.newBufferedReader(perms)) {
            JSONParser parser = new JSONParser();
            JSONObject file = (JSONObject) parser.parse(reader);

            this.perms = new EnumMap<>(PermType.class);

            List<Long> admins = new ArrayList<>();
            for (Object obj : (JSONArray) file.get("admin"))
                admins.add(Long.valueOf(obj.toString()));
            this.perms.put(PermType.ADMIN, admins);
            List<Long> mods = new ArrayList<>();
            for (Object obj : (JSONArray) file.get("mod"))
                mods.add(Long.valueOf(obj.toString()));
            this.perms.put(PermType.MOD, mods);
            // friends perm group?
        }
    }

    @SuppressWarnings("unchecked")
    public void writePerms() throws Exception {
        Path perms = this.directory.resolve("perms.json");

        JSONObject jo = new JSONObject();
        JSONArray admin = new JSONArray();
        admin.addAll(this.perms.get(PermType.ADMIN));
        jo.put("admin", admin);

        JSONArray mod = new JSONArray();
        mod.addAll(this.perms.get(PermType.MOD));
        jo.put("mod", mod);

        try (BufferedWriter writer = Files.newBufferedWriter(perms)) {
            writer.write(jo.toJSONString());
            writer.flush();
        }
    }

    public void reloadPerms() {
        try {
            this.writePerms();
            this.initPerms();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reloadConfig() {
        try {
            this.initConfig();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public EnumMap<PermType, List<Long>> getPerms() {
        return perms;
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

    public PermType getPermForPlayer(long user) {
        if (this.perms.get(PermType.ADMIN).contains(user))
            return PermType.ADMIN;
        if (this.perms.get(PermType.MOD).contains(user))
            return PermType.MOD;
        return PermType.DEFAULT;
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
        framework.registerCommands(new uk.co.netbans.supportbot.Support.Command.Admin.PermChildren.List());
        framework.registerCommands(new User());
        framework.registerCommands(new Group());
        framework.registerCommands(new CreateGroup());

        //Other Admin Commands
        framework.registerCommands(new Tips());
        framework.registerCommands(new ManualChannel());
        framework.registerCommands(new Faq());
        framework.registerCommands(new ConfigReload());
        framework.registerCommands(new Embedify());

        //Moderation Commands
        framework.registerCommands(new uk.co.netbans.supportbot.Moderation.Purge.User());
        framework.registerCommands(new Link());

        //Normal Commands (No Perms)
        framework.registerCommands(new Help());
        framework.registerCommands(new Ticket());
    }
}
