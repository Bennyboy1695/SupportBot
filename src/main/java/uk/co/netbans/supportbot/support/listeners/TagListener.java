package uk.co.netbans.supportbot.support.listeners;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.bhop.bjdautilities.ReactionMenu;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.bson.Document;
import uk.co.netbans.supportbot.EmbedTemplates;
import uk.co.netbans.supportbot.SupportBot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class TagListener extends ListenerAdapter {

    private SupportBot bot;

    public TagListener(SupportBot bot) {
        this.bot = bot;
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        if ((Boolean) ((Document) bot.getMongoRequestRegistry().getGuildModules(event.getGuild().getIdLong()).get("roleTags")).get("enabled")) {

            if (!event.getMessage().getMentionedRoles().isEmpty()) {
                for (Long role : getRoleMentions()) {
                    Role role1 = bot.getJDA().getRoleById(role);
                    if (event.getMessage().getMentionedRoles().contains(role1)) {
                        TextChannel textChannel = bot.getJDA().getTextChannelById(bot.getMainConfig().getConfigValue("mention_channel").getAsLong());
                        new ReactionMenu.Builder(bot.getJDA())
                                .onClick("\u274C", ReactionMenu::destroy)
                                .setEmbed(EmbedTemplates.PRETTY_SUCCESSFULL.getEmbed().setTitle(role1.getName() + " was mentioned in " + event.getMessage().getTextChannel().getName() + "!").setDescription(event.getMessage().getJumpUrl()).addField("Message: ", event.getMessage().getContentRaw(), true).setFooter("Mentioned by " + event.getMember().getEffectiveName(), event.getAuthor().getAvatarUrl()).build())
                                .addStartingReaction("\u274C")
                                .buildAndDisplay(textChannel);
                    }
                }
            }
            if (!event.getMessage().getMentionedUsers().isEmpty()) {
                for (Long user : getUserMentions()) {
                    User user1 = bot.getJDA().getUserById(user);
                    if (event.getMessage().getMentionedUsers().contains(user1)) {
                        TextChannel textChannel = bot.getJDA().getTextChannelById(bot.getMainConfig().getConfigValue("mention_channel").getAsLong());
                        new ReactionMenu.Builder(bot.getJDA())
                                .onClick("\u274C", ReactionMenu::destroy)
                                .setEmbed(EmbedTemplates.PRETTY_SUCCESSFULL.getEmbed().setTitle(user1.getName() + " was mentioned in " + event.getMessage().getTextChannel().getName() + "!").setDescription(event.getMessage().getJumpUrl()).addField("Message: ", event.getMessage().getContentRaw(), false).setFooter("Mentioned by " + event.getMember().getEffectiveName(), event.getAuthor().getAvatarUrl()).build())
                                .addStartingReaction("\u274C")
                                .buildAndDisplay(textChannel);
                    }
                }
            }
        }
    }

    private List<Long> getRoleMentions() {
        List<Long> longs = new ArrayList<>();
        JsonObject mentions = bot.getMainConfig().getConfigValue("mentions");
        JsonArray roles = mentions.get("roles").getAsJsonArray();
        for (Object object : roles) {
            JsonElement entry = (JsonElement) object;
            longs.add(entry.getAsLong());
        }
        return longs;
    }

    private List<Long> getUserMentions() {
        List<Long> longs = new ArrayList<>();
        JsonObject mentions = bot.getMainConfig().getConfigValue("mentions");
        JsonArray users = mentions.get("users").getAsJsonArray();
        for (Object object : users) {
            JsonElement entry = (JsonElement) object;
            longs.add(entry.getAsLong());
        }
        return longs;
    }
}
