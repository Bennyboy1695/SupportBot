package uk.co.netbans.supportbot;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import me.bhop.bjdautilities.Messenger;
import me.bhop.bjdautilities.command.CommandHandler;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.hooks.InterfacedEventManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.netbans.supportbot.commands.misc.Emote;
import uk.co.netbans.supportbot.commands.misc.bDoc;
import uk.co.netbans.supportbot.commands.misc.remind.Add;
import uk.co.netbans.supportbot.commands.misc.remind.Remind;
import uk.co.netbans.supportbot.commands.misc.Timezones;
import uk.co.netbans.supportbot.commands.moderation.purge.Link;
import uk.co.netbans.supportbot.commands.moderation.purge.Mention;
import uk.co.netbans.supportbot.commands.moderation.purge.Purge;
import uk.co.netbans.supportbot.commands.moderation.purge.User;
import uk.co.netbans.supportbot.commands.music.Play;
import uk.co.netbans.supportbot.commands.support.Ticket;
import uk.co.netbans.supportbot.music.AudioHandler;
import uk.co.netbans.supportbot.oldmusic.MusicManager;
import uk.co.netbans.supportbot.storage.SQLManager;
import uk.co.netbans.supportbot.support.listeners.*;
import uk.co.netbans.supportbot.task.ExpiryCheckTask;
import uk.co.netbans.supportbot.utils.Util;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NetBansBot {
    private JDA jda;
    private Messenger messenger;
    private MusicManager music;

    private Path directory;
    private Path logDirectory;
    private Path musicDirectory;
    private Config config;
    private SQLManager sqlManager;
    private CommandHandler commandHandler;
    private NetBansBot bot = this;
    private Path configDirectory;
    private Logger logger;

    // Music New
    private AudioHandler audioHandler = new AudioHandler(this);

    public void init(Path directory, Path configDirectory) throws Exception {
        this.directory = directory;
        this.configDirectory = configDirectory;

        logger = LoggerFactory.getLogger("SupportBot");

        logger.info("Initializing Config!");
        try {
            initConfig(configDirectory);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize config!", e);
        }

        logger.info("Loading SQL!");
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

        if (config.getConfigValue("token").getAsString().equals("add_me")) {
            logger.error("Found unedited config. Please add the token.");
            System.exit(1);
        }

        this.jda = new JDABuilder(AccountType.BOT)
                .setToken(config.getConfigValue("token").getAsString())
                .setBulkDeleteSplittingEnabled(false)
                .setEventManager(new ThreadedEventManager())
                .build();
        jda.awaitReady();

        logger.info("Loading Messenger...");
        this.messenger = new Messenger();

        logger.info("Registering Commands...");
        // old
        commandHandler = new CommandHandler.Builder(jda).addCustomParameter(bot).setPrefix(getCommandPrefix()).setDeleteCommandTime(10).setGenerateHelp(true).setSendTyping(true).setEntriesPerHelpPage(6).build();

        // Admin
        commandHandler.register(new uk.co.netbans.supportbot.commands.admin.ConfigReload());
        commandHandler.register(new uk.co.netbans.supportbot.commands.admin.Embedify());
        commandHandler.register(new uk.co.netbans.supportbot.commands.admin.Faq());
        commandHandler.register(new uk.co.netbans.supportbot.commands.admin.ManualChannel());
        commandHandler.register(new uk.co.netbans.supportbot.commands.admin.Say());
        commandHandler.register(new uk.co.netbans.supportbot.commands.admin.Tips());

        // Misc
        commandHandler.register(new Emote());
        commandHandler.register(new Timezones());
        commandHandler.register(new Remind());
        commandHandler.register(new Add());
        commandHandler.register(new uk.co.netbans.supportbot.commands.misc.remind.List());

        commandHandler.register(new bDoc());

        // Moderation/Purge
        commandHandler.register(new Purge());
        commandHandler.register(new Mention());
        commandHandler.register(new Link());
        commandHandler.register(new User());

        // Music
        commandHandler.register(new Play());

        // Support
        commandHandler.register(new Ticket());

        commandHandler.getCommand(Remind.class).ifPresent(cmd -> cmd.addCustomParam(commandHandler));

        //registerCommands();

        this.jda.addEventListener(new PrivateMessageListener(this));
        this.jda.addEventListener(new SupportCategoryListener(this));
        this.jda.addEventListener(new TicketChannelsReactionListener(this));
        this.jda.addEventListener(new SuggestionListener(this));
        //this.jda.addEventListener(new HelpMessageReactionListener(this));
        this.jda.addEventListener(new TagListener(this));
        this.jda.addEventListener(new EmoteRemoverListener(this));

        Executors.newSingleThreadExecutor().submit(() -> {
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
        });

        Executors.newSingleThreadExecutor().submit((Runnable) () -> {
            while (true) {
                Util.getSupportChannels(bot);
                try {
                    Thread.sleep(Duration.ofMinutes(10).toMillis());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        TimerTask timerTask = new ExpiryCheckTask(this);
        Timer timer = new Timer(true);
        timer.schedule(timerTask, Duration.ofMinutes(0).toMillis(), Duration.ofMinutes(15).toMillis());

        logger.info("Loading Music Manager...");
        this.music = new MusicManager(this);

        // Music New


        logger.info("Finished Loading | Now accepting input.");

    }

    public void shutdown() {
        logger.info("Initiating Shutdown...");
        getJDA().shutdown();
        logger.info("Shutdown Complete.");
    }

    public List<String[]> getTips(){
        JsonArray tips = config.getConfigValue("tips");
        List<String[]> tipArray = new ArrayList<>();
        for (Object obj : tips) {
            JsonObject jsonObject = (JsonObject) obj;
            String word = jsonObject.get("word").getAsString();
            String suggestion = jsonObject.get("suggestion").getAsString();
            String[] put = new String[]{word, suggestion};
            tipArray.add(put);
        }
        return tipArray;
    }

    public List<String> getReplies() {
        List<String> replyArray = new ArrayList<>();
            JsonArray replies = config.getConfigValue("replies");
            for (Object obj : replies) {
                JsonObject jsonObject = (JsonObject) obj;
                String word = jsonObject.get("reply").getAsString();
                replyArray.add(word);
            }
        return replyArray;
    }

    public JDA getJDA() {
        return this.jda;
    }

    public Messenger getMessenger() {
        return this.messenger;
    }

    public Path getDirectory() {
        return directory;
    }

    public Path getConfigDirectory() {
        return configDirectory;
    }

    public MusicManager getMusicManager() {
        return this.music;
    }

    public SQLManager getSqlManager() {
        return sqlManager;
    }

    public CommandHandler getCommandHandler() {
        return commandHandler;
    }

    private void initConfig(Path configDirectory){
        try {
            config = new Config(this, configDirectory);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Config getConfig() {
        return config;
    }

    public Logger getLogger() {
        return logger;
    }

    public long getGuildID() {
        return Long.valueOf(config.getConfigValue("guildID").getAsString());
    }

    public long getSupportCategoryID() {
        return Long.valueOf(config.getConfigValue("category").getAsString());
    }

    public String getCommandPrefix() {
        return config.getConfigValue("commandPrefix").getAsString();
    }

    public void reloadConfig() {
        try {
            this.initConfig(getConfigDirectory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Path getLogDirectory() {
        return logDirectory;
    }

    public Path getMusicDirectory() {
        return musicDirectory;
    }

    public AudioHandler getAudioHandler() {
        return audioHandler;
    }

    // potentially un necessary.
    private final class ThreadedEventManager extends InterfacedEventManager {
        private final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()+1);

        @Override
        public void handle(Event e) {
            executor.submit(() -> super.handle(e));
        }
    }

    //private void registerCommands() {
    //    Set<Class<?>> methods = new HashSet<>();
    //    try {
    //        Reflections reflections = new Reflections("uk.co.netbans.supportbot.Commands", new TypeAnnotationsScanner());
    //        methods = reflections.getTypesAnnotatedWith(Command.class, true);
    //    } catch (Exception e) {
    //        e.printStackTrace();
    //    }
//
    //    StringBuilder builder = new StringBuilder();
    //    builder.append("Registered Commands: ");
    //    for (Class<?> method : methods) {
    //        try {
    //            commandHandler.register(method.newInstance());
    //            builder.append(method.getSimpleName() + ", ");
    //        } catch (InstantiationException | IllegalAccessException e) {
    //            e.printStackTrace();
    //        }
    //    }
    //    logger.info(builder.toString().substring(0, builder.length() - 2));
    //}
}
