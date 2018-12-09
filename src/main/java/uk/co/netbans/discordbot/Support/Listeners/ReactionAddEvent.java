package uk.co.netbans.discordbot.Support.Listeners;

import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import uk.co.netbans.discordbot.NetBansBot;
import uk.co.netbans.discordbot.PermType;


public class ReactionAddEvent extends ListenerAdapter {

    private NetBansBot bot;

    public ReactionAddEvent(NetBansBot bot) {
        this.bot = bot;
    }

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        if (event.isFromType(ChannelType.TEXT)) {
            TextChannel channel = (TextChannel) event.getChannel();
            if (channel.getParent().getIdLong() == Long.valueOf((String) bot.getConf().get("category"))) {
                for (Message message : channel.getPinnedMessages().complete()) {
                    if (message.getAuthor().isBot() && event.getReactionEmote().getName().equals("\u2705")) {
                        for (Member member : message.getMentionedMembers()) {
                            if (event.getMember() == member || bot.getPerms().get(PermType.ADMIN).contains(event.getMember().getUser().getIdLong())) {
                                member.getUser().openPrivateChannel().complete()
                                        .sendFile(bot.getLogDirectory().resolve(channel.getName()+ ".log").toFile() , new MessageBuilder()
                                                .append(event.getMember().getAsMention() + " marked your issue as closed! Because of this we have sent you a log file containing the history, so that you may look at it incase you encounter the issue again!")
                                                .build())
                                        .complete();
                                channel.delete().reason("Issue completed!").complete();
                            }
                        }
                    }
                }
            }
        }
    }
}
