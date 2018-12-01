package uk.co.netbans.discordbot;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.hooks.InterfacedEventManager;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import uk.co.netbans.discordbot.Message.Messenger;
import uk.co.netbans.discordbot.Command.CommandListener;
import uk.co.netbans.discordbot.Music.MusicManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NetBansBot {
    private final JDA jda;
    private final Messenger messenger;
    private final MusicManager music;

    private Path directory;
    private JSONObject conf;

    public NetBansBot(Path directory) throws Exception {
        this.directory = directory;

        System.out.println("Initializing Config!");
        try {
            initConfig();
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize config!", e);
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
        this.jda.addEventListener(new CommandListener(this));

        System.out.println("Loading Music Manager...");
        this.music = new MusicManager();

        System.out.println("Finished Loading.");
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

    @SuppressWarnings("unchecked")
    private void initConfig() throws Exception {
        Path config = directory.resolve("config.json");
        if (!Files.exists(config)) {
            Files.createFile(config);

            JSONObject jo = new JSONObject();
            jo.put("token", "add_me");

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

    // potentially un necessary.
    private final class ThreadedEventManager extends InterfacedEventManager {
        private final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()+1);

        @Override
        public void handle(Event e) {
            executor.submit(() -> super.handle(e));
        }
    }
}
