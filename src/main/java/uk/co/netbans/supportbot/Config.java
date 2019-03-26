package uk.co.netbans.supportbot;

import com.google.gson.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

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
                write(config, writeDefaults());
            }
            if (hasAllEntries(read(config).getAsJsonObject())) {
                this.conf = read(config);
            } else {
                write(config, writeNotFoundDefaults(read(config).getAsJsonObject()));
                this.conf = read(config);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JsonElement read(Path config) {
        JsonElement element = null;
        try (BufferedReader reader = Files.newBufferedReader(config)) {
            JsonParser parser = new JsonParser();
            element = parser.parse(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return element;
    }

    private void write(Path config, JsonObject object) {
        try (BufferedWriter writer = Files.newBufferedWriter(config)) {
            writer.write(gson.toJson(object));
            writer.flush();
        } catch (IOException e) {
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
        JsonObject mentions = new JsonObject();
        mentions.add("roles", new JsonArray());
        mentions.add("users", new JsonArray());
        jo.add("mentions", mentions);
        jo.addProperty("mention_channel", "add_me");
        jo.add("emotes", new JsonArray());
        return jo;
    }

    public JsonObject writeNotFoundDefaults(JsonObject config) {
        JsonObject finished = new JsonObject();
        for (Map.Entry<String, JsonElement> entrySet : writeDefaults().entrySet()) {
            if (!config.has(entrySet.getKey())) {
                finished.add(entrySet.getKey(), entrySet.getValue());
            } else {
                finished.add(entrySet.getKey(), config.get(entrySet.getKey()));
            }
        }
        return finished;
    }

    public boolean hasAllEntries(JsonObject config) {
        int count = 0;
        for (Map.Entry<String, JsonElement> entrySet : writeDefaults().entrySet()) {
            if (config.has(entrySet.getKey())) {
                count++;
            }
        }
        return (count == writeDefaults().size());
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
