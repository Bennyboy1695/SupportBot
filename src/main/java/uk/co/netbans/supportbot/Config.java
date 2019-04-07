package uk.co.netbans.supportbot;

import com.google.gson.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class Config {

    private SupportBot bot;
    private Path configDirectory;
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private JsonObject conf;

    Config(SupportBot bot, Path configDirectory) {
        this.bot = bot;
        this.configDirectory = configDirectory;
    }

    JsonObject newConfig(String configName, JsonObject configOptions) {
        JsonObject conf = null;
        Path config = configDirectory.resolve(configName + ".json");
        try {
            if (!Files.exists(config)) {
                Files.createFile(config);
                write(config, configOptions);
            }
            if (hasAllEntries(read(config).getAsJsonObject(), configOptions)) {
                conf = read(config).getAsJsonObject();
            } else {
                write(config, writeNotFoundDefaults(read(config).getAsJsonObject(), configOptions));
                conf = read(config).getAsJsonObject();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.conf = conf;
        return conf;
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

    public void saveConfig(String configName, JsonElement conf) {
        try (BufferedWriter writer = Files.newBufferedWriter(configDirectory.resolve(configName + ".json"))) {
            writer.write(gson.toJson(conf));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static JsonObject mainConfigDefaults() {
        JsonObject main = new JsonObject();
        JsonObject required = new JsonObject();
        required.addProperty("token", "add_me");
        required.addProperty("category","add_me");
        required.addProperty("logChannelID", "add_me");
        required.addProperty("guildID", "add_me");
        required.addProperty("commandPrefix", "!");
        required.addProperty("mention_channel", "add_me");
        main.add("required", required);
        main.add("timezones", new JsonArray());
        main.add("tips", new JsonArray());
        main.add("replies", new JsonArray());
        JsonObject mentions = new JsonObject();
        mentions.add("roles", new JsonArray());
        mentions.add("users", new JsonArray());
        main.add("mentions", mentions);
        main.add("emotes", new JsonArray());
        return main;
    }

    public JsonObject writeNotFoundDefaults(JsonObject config, JsonObject configOptions) {
        JsonObject finished = new JsonObject();
        for (Map.Entry<String, JsonElement> entrySet : configOptions.entrySet()) {
            if (!config.has(entrySet.getKey())) {
                finished.add(entrySet.getKey(), entrySet.getValue());
            } else {
                finished.add(entrySet.getKey(), config.get(entrySet.getKey()));
            }
        }
        return finished;
    }

    public boolean hasAllEntries(JsonObject config, JsonObject configOptions) {
        int count = 0;
        for (Map.Entry<String, JsonElement> entrySet : configOptions.entrySet()) {
            if (config.has(entrySet.getKey())) {
                count++;
            }
        }
        return (count == configOptions.size());
    }

    public <T extends JsonElement> T getConfigValue(String... keys) {
        JsonObject parent = (JsonObject) conf;
        JsonElement temp = parent.get(keys[0]);
        if (temp.isJsonArray())
            return (T) temp.getAsJsonArray();
        JsonObject object = temp.getAsJsonObject();
        try {
            for (int i = 1; i < keys.length; i++) {
                temp = object.get(keys[i]);
                if (temp.isJsonArray())
                    return (T) temp.getAsJsonArray();
                if (temp.isJsonPrimitive())
                    return (T) temp.getAsJsonPrimitive();
                object = temp.getAsJsonObject();
            }
        } catch (NullPointerException e) {
            return (T) object;
        }
        return (T) object;
    }
}
