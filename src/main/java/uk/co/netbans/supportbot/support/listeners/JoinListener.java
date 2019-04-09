package uk.co.netbans.supportbot.support.listeners;

import net.dv8tion.jda.core.entities.Category;
import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.bson.Document;
import uk.co.netbans.supportbot.SupportBot;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class JoinListener extends ListenerAdapter {

    private SupportBot bot;

    public JoinListener(SupportBot bot) {
        this.bot = bot;
    }

    @Override
    public void onGuildJoin(GuildJoinEvent event) {
        if (!bot.getMongoRequestRegistry().doesConfigExistAlready(event.getGuild().getIdLong())) {
            bot.getMongoRequestRegistry().createNewGuildConfig(event.getGuild().getIdLong());
        }

        try {
            if (!event.getGuild().getCategories().stream().map(Channel::getName).anyMatch(s -> s.equalsIgnoreCase("Support"))) {
                Category category = event.getGuild().getController().createCategory("Support").complete().getParent();
                try {
                    Document modules = (Document) bot.getMongoController().getCollection("config").find(new Document("_id", event.getGuild().getIdLong())).limit(1).first().get("modules");
                    Document settings = (Document) ((Document) modules.get("tickets")).get("settings");
                    settings.remove("categoryId");
                    settings.put("categoryId", String.valueOf(category.getIdLong()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Path path = bot.getDirectory().resolve("logs").resolve(String.valueOf(event.getGuild().getIdLong()));
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
