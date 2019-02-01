package uk.co.netbans.supportbot.Support.Listeners;

import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import uk.co.netbans.supportbot.NetBansBot;
import uk.co.netbans.supportbot.Commands.Support.Help;

public class HelpMessageReactionListener extends ListenerAdapter {

    private NetBansBot bot;

    public HelpMessageReactionListener(NetBansBot bot) {
        this.bot = bot;
    }

    //\u25B6 >
    //\u25C0 <

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        if (event.isFromType(ChannelType.TEXT)) {
            TextChannel channel = (TextChannel) event.getChannel();
            if (!channel.getMessageById(event.getMessageIdLong()).complete().getEmbeds().isEmpty()) {
                Message message = channel.getMessageById(event.getMessageIdLong()).complete();
                if (message.getAuthor().isBot()) {
                    if (!event.getMember().getUser().isBot()) {
                        if (event.getReactionEmote().getName().equals("\u25B6")) {
                            for (MessageEmbed embed : message.getEmbeds()) {
                                event.getReaction().removeReaction(event.getUser()).complete();
                                if (embed.getTitle().equals("Command Help!")) {
                                    if (embed.getDescription().contains("Page 1")) {
                                        message.editMessage(Help.helpPages(bot, 2)).complete();
                                    }
                                    if (embed.getDescription().contains("Page 2")) {
                                        message.editMessage(Help.helpPages(bot, 3)).complete();
                                    }
                                }
                            }
                        } else if (event.getReactionEmote().getName().equals("\u25C0")) {
                            for (MessageEmbed embed : message.getEmbeds()) {
                                event.getReaction().removeReaction(event.getUser()).complete();
                                if (embed.getTitle().equals("Command Help!")) {
                                    if (embed.getDescription().contains("Page 2")) {
                                        message.editMessage(Help.helpPages(bot, 1)).complete();
                                    }
                                    if (embed.getDescription().contains("Page 3")) {
                                        message.editMessage(Help.helpPages(bot, 2)).complete();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
