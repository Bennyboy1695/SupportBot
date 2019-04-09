package uk.co.netbans.supportbot;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import me.bhop.bjdautilities.EditableMessage;
import me.bhop.bjdautilities.Messenger;
import me.bhop.bjdautilities.ReactionMenu;
import me.bhop.bjdautilities.command.CommandHandler;
import me.bhop.bjdautilities.command.handler.GuildDependentCommandHandler;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.hooks.InterfacedEventManager;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.netbans.supportbot.commands.admin.*;
import uk.co.netbans.supportbot.commands.misc.Doc;
import uk.co.netbans.supportbot.commands.misc.Emote;
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
import uk.co.netbans.supportbot.storage.MongoController;
import uk.co.netbans.supportbot.storage.MongoRequestRegistry;
import uk.co.netbans.supportbot.storage.SQLManager;
import uk.co.netbans.supportbot.support.listeners.*;
import uk.co.netbans.supportbot.task.ExpiryCheckTask;
import uk.co.netbans.supportbot.utils.Hastebin;
import uk.co.netbans.supportbot.utils.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class SupportBot {
    private JDA jda;
    private Messenger messenger;
    private MusicManager music;

    private Path directory;
    private Path logDirectory;
    private Path musicDirectory;
    private Config mainConfig;
    private JsonObject mainConf;
    private SQLManager sqlManager;
    private MongoController mongoController;
    private MongoRequestRegistry mongoRequestRegistry;
    private GuildDependentCommandHandler commandHandler;
    private SupportBot bot = this;
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

        mongoController = new MongoController(mainConfig.getConfigValue("mongo","hostname").getAsString(),
                mainConfig.getConfigValue("mongo","port").getAsInt(),
                mainConfig.getConfigValue("mongo","database").getAsString(),
                mainConfig.getConfigValue("mongo","username").getAsString(),
                mainConfig.getConfigValue("mongo", "password").getAsString(),
                mainConfig.getConfigValue("mongo", "authDb").getAsString());

        mongoRequestRegistry = new MongoRequestRegistry(this);

        logger.info("Loading SQL!");
        sqlManager = new SQLManager(directory.toFile());

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

        this.jda = new JDABuilder(AccountType.BOT)
                .setToken(getMongoRequestRegistry().getGlobalSettings().get("token").toString())
                .setBulkDeleteSplittingEnabled(false)
                .setEventManager(new ThreadedEventManager())
                .build();
        jda.awaitReady();


        commandHandler = new CommandHandler.Builder(jda).addCustomParameter(bot).setUsePermissionsInHelp(true).setGenerateHelp(true).setEntriesPerHelpPage(6).guildDependent().build();

        this.jda.getGuilds().forEach(guild -> {
            if (mongoRequestRegistry.getGuildConfig(guild.getIdLong()).get("prefix").toString() != null)
                commandHandler.getPrefixes().put(guild.getIdLong(), mongoRequestRegistry.getGuildConfig(guild.getIdLong()).get("prefix").toString());
            if (!getBotAsMember(guild).getEffectiveName().equalsIgnoreCase(mongoRequestRegistry.getGuildConfig(guild.getIdLong()).getString("botName"))) {
                guild.getController().setNickname(getBotAsMember(guild), mongoRequestRegistry.getGuildConfig(guild.getIdLong()).getString("botName")).complete();
            }
        });

        logger.info("Loading Messenger...");
        this.messenger = new Messenger();

        logger.info("Registering Commands...");
        // old

        // Admin
        commandHandler.register(new ConfigReload());
        commandHandler.register(new Embedify());
        commandHandler.register(new Faq());
        commandHandler.register(new ManualChannel());
        commandHandler.register(new Say());
        commandHandler.register(new Tips());

        // Misc
        commandHandler.register(new Emote());
        commandHandler.register(new Timezones());
        commandHandler.register(new Remind());
        commandHandler.register(new Add());
        commandHandler.register(new uk.co.netbans.supportbot.commands.misc.remind.List());
        commandHandler.register(new Doc());

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

        this.jda.addEventListener(new JoinListener(this));
        this.jda.addEventListener(new PrivateMessageListener(this));
        this.jda.addEventListener(new SupportCategoryListener(this));

        makeMessages();
        //this.jda.addEventListener(new SuggestionListener(this));
        //this.jda.addEventListener(new HelpMessageReactionListener(this));
        //this.jda.addEventListener(new TagListener(this));
        //this.jda.addEventListener(new EmoteRemoverListener(this));

        Executors.newSingleThreadExecutor().submit(() -> {
                Member member = Utils.randomMember(bot);
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
                Utils.TicketUtils.getSupportChannels(bot);
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

    public void makeMessages () {
        for (Guild guild : jda.getGuilds()) {
            if ((Boolean) ((Document) getMongoRequestRegistry().getGuildModules(guild.getIdLong()).get("tickets")).get("enabled")) {
                if (bot.getMongoRequestRegistry().getGuildTicketSettings(guild.getIdLong()).get("categoryId") != "add_me") {
                    System.out.println(bot.getMongoRequestRegistry().getGuildTicketSettings(guild.getIdLong()).get("categoryId"));
                    Category category = guild.getCategoryById((String) bot.getMongoRequestRegistry().getGuildTicketSettings(guild.getIdLong()).get("categoryId"));
                    if (!category.getChannels().isEmpty() && !category.getTextChannels().isEmpty()) {
                        for (TextChannel textChannel : category.getTextChannels()) {
                            TextChannel supportChannel = textChannel;
                            if (!textChannel.getPinnedMessages().complete().isEmpty()) {
                                final Message[] message = {null};
                                textChannel.getPinnedMessages().queueAfter(1, TimeUnit.SECONDS, (msg) -> message[0] = msg.get(0));
                                ReactionMenu menu = new ReactionMenu.Import(message[0])
                                        .onClick("\u2705", (finished, ticketMember) -> Utils.TicketUtils.closeTicket(bot, guild.getMember(ticketMember), guild, supportChannel))
                                        .onClick("\uD83D\uDD12", (lock, ticketMember) -> Utils.TicketUtils.lockChannel(bot, guild.getMember(ticketMember), guild, supportChannel))
                                        .onClick("\uD83D\uDD13", (unlock, ticketMember) -> Utils.TicketUtils.unlockChannel(bot, guild.getMember(ticketMember), guild, supportChannel))
                                        .onRemove("\uD83D\uDD12", (unlock, ticketMember) -> Utils.TicketUtils.resetLockOnChannel(bot, guild.getMember(ticketMember), guild, supportChannel))
                                        .onRemove("\uD83D\uDD13", (lock, ticketMember) -> Utils.TicketUtils.resetLockOnChannel(bot, guild.getMember(ticketMember), guild, supportChannel))
                                        .setRemoveReactions(false)
                                        .build();
                            }
                        }
                    }
                }
            }
        }
    }

    public Member getBotAsMember(Guild guild) {
        return guild.getMemberById(bot.getJDA().asBot().getApplicationInfo().complete().getIdLong());
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

    public MongoController getMongoController() {
        return mongoController;
    }

    public MongoRequestRegistry getMongoRequestRegistry() {
        return mongoRequestRegistry;
    }

    public CommandHandler getCommandHandler() {
        return commandHandler;
    }

    private void initConfig(Path configDirectory){
        try {
            mainConfig = new Config(this, configDirectory);
            mainConf = mainConfig.newConfig("config", Config.mainConfigDefaults());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Config getMainConfig() {
        return mainConfig;
    }

    public Logger getLogger() {
        return logger;
    }

    public long getGuildID() {
        return Long.valueOf(mainConfig.getConfigValue("required","guildID").getAsString());
    }

    public long getSupportCategoryID() {
        return Long.valueOf(mainConfig.getConfigValue("required","category").getAsString());
    }

    public String getCommandPrefix() {
        return mainConfig.getConfigValue("required","commandPrefix").getAsString();
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

}
