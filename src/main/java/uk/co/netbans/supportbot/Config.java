package uk.co.netbans.supportbot;

import com.google.gson.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;

public class Config {

    private NetBansBot bot;
    private Path configDirectory;
    private JsonElement conf;
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    Config(NetBansBot bot, Path configDirectory) {
        this.bot = bot;
        this.configDirectory = configDirectory;
        Path config = configDirectory.resolve("config.json");
        try {
            if (!Files.exists(config)) {
                Files.createFile(config);

                JsonObject object = writeDefaults().getAsJsonObject();

                try (BufferedWriter writer = Files.newBufferedWriter(config)) {
                    writer.write(gson.toJson(object));
                    writer.flush();
                }
            }

            try (BufferedReader reader = Files.newBufferedReader(config)) {
                JsonParser parser = new JsonParser();
                this.conf = parser.parse(reader);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveConfig() {
        try (BufferedWriter writer = Files.newBufferedWriter(configDirectory.resolve("config.json"))) {
            writer.write(gson.toJson(conf));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JsonObject writeDefaults() {
        JsonObject jo = new JsonObject();
        jo.addProperty("token", "add_me");
        jo.addProperty("category","add_me");
        jo.addProperty("logChannelID", "add_me");
        jo.addProperty("guildID", "add_me");
        jo.addProperty("commandPrefix", "!");
        jo.add("timezones", new JsonArray());
        jo.add("tips", new JsonArray());
        jo.add("replies", new JsonArray());
        return jo;
    }

    public <T extends JsonElement> T getConfigValue(String key) {
        JsonObject object = (JsonObject) conf;
        if (object.get(key).isJsonObject()) {
            return (T) object.get(key).getAsJsonObject();
        }
        if (object.get(key).isJsonArray()) {
            return (T) object.get(key).getAsJsonArray();
        }
        return (T) object.get(key);
    }
}
