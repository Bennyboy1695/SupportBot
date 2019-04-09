package uk.co.netbans.supportbot.support.listeners;

import com.google.common.primitives.Chars;
import com.vdurmont.emoji.Emoji;
import com.vdurmont.emoji.EmojiManager;
import com.vdurmont.emoji.EmojiParser;
import com.vdurmont.emoji.EmojiTrie;
import me.bhop.bjdautilities.ReactionMenu;
import net.dv8tion.jda.client.managers.EmoteManager;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.bson.Document;
import uk.co.netbans.supportbot.EmbedTemplates;
import uk.co.netbans.supportbot.SupportBot;
import uk.co.netbans.supportbot.utils.Hastebin;
import uk.co.netbans.supportbot.utils.Utils;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class PrivateMessageListener extends ListenerAdapter {

    private SupportBot bot;
    private int userCount;

    public PrivateMessageListener(SupportBot bot) {
        this.bot = bot;
    }

    @Override
    public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        if (bot.getJDA().getMutualGuilds(event.getAuthor()).size() == 1) {
            Guild guild = bot.getJDA().getMutualGuilds(event.getAuthor()).get(0);
            if ((Boolean) ((Document) bot.getMongoRequestRegistry().getGuildModules(guild.getIdLong()).get("tickets")).get("enabled")) {

                Member member = guild.getMember(event.getAuthor());

                //if (member.getRoles().contains(bot.getJDA().getGuildById(bot.getGuildID()).getRoleById(bot.getMainConfig().getConfigValue("noHelpRoleID").getAsString()))) {
                //    member.getUser().openPrivateChannel().complete().sendMessage("No channel has been created because you have the anti-support role!").complete();
                //    return;
                //}

                for (Guild.Ban bans : guild.getBanList().complete()) {
                    if (bans.getUser().getIdLong() == member.getUser().getIdLong())
                        return;
                }

                for (TextChannel channel : guild.getCategoryById((String) bot.getMongoRequestRegistry().getGuildTicketSettings(guild.getIdLong()).get("categoryId")).getTextChannels()) {
                    if (channel.getName().startsWith(member.getUser().getName().toLowerCase())) {
                        userCount++;
                        if (userCount >= 3) {
                            member.getUser().openPrivateChannel().complete().sendMessage("No channel has been created because you have multiple channels already open in the selected server. Please complete these issue first!").complete();
                            return;
                        }
                    }
                }
                Utils.TicketUtils.createTicket(bot, member, guild, event.getMessage());
            }
        } else {
            EmbedBuilder builder = EmbedTemplates.PRETTY_SUCCESSFULL.getEmbed().setDescription("We see that you share multiple servers with this bot. Please select which server you want the ticket to be in by reacting with the emote that matches the key below!");
            char endCharacter = 'a';
            String alias = "regional_indicator_symbol_";
            ReactionMenu.Builder reactionBuilder = new ReactionMenu.Builder(bot.getJDA());
            reactionBuilder.setRemoveReactions(false);
            boolean haveClicks = false;
            guilds:
            for (Guild guild : bot.getJDA().getMutualGuilds(event.getAuthor())) {
                userCount = 0;
                for (Guild.Ban bans : guild.getBanList().complete()) {
                    if (bans.getUser().getIdLong() == event.getAuthor().getIdLong())
                        continue guilds;
                }

                for (TextChannel channel : guild.getCategoryById((String) bot.getMongoRequestRegistry().getGuildTicketSettings(guild.getIdLong()).get("categoryId")).getTextChannels()) {
                    if (channel.getName().startsWith(event.getAuthor().getName().toLowerCase())) {
                        userCount++;
                        if (userCount >= 3) {
                            //event.getAuthor().openPrivateChannel().complete().sendMessage("No channel has been created because you have multiple channels already open in the selected server. Please complete these issue first!").complete();
                            continue guilds;
                        }
                    }
                }

                haveClicks = true;
                Emoji emoji = EmojiManager.getForAlias(alias + endCharacter);
                builder.addField(guild.getName(), emoji.getUnicode(), true);
                reactionBuilder.onClick(emoji.getUnicode(), (menu2,user) -> {
                    menu2.getMessage().setContent(EmbedTemplates.SUCCESS.getEmbed().setDescription("You have selected **" + guild.getName() + "** as the server you would like your ticket to go to!").build());
                    Utils.TicketUtils.createTicket(bot, guild.getMember(user), guild, event.getMessage());
                    bot.getMongoRequestRegistry().createNewTicket(guild, event.getMessage(), event.getAuthor(), Instant.now().toEpochMilli(), 0);
                    menu2.destroy();
                });
                endCharacter++;
            }
            if (haveClicks) {
                reactionBuilder.setEmbed(builder.build());
                reactionBuilder.buildAndDisplayForPrivateMessage(event.getChannel());
            } else {
                reactionBuilder.setEmbed(EmbedTemplates.ERROR.getEmbed().setDescription("You have too many tickets open in all the servers you share with the bot! Please complete these first before creating another ticket.").build());
                reactionBuilder.buildAndDisplayForPrivateMessage(event.getChannel());
            }
        }
    }
}
