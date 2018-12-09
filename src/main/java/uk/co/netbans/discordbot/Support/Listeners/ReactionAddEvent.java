package uk.co.netbans.discordbot.Support.Listeners;

import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import uk.co.netbans.discordbot.NetBansBot;


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
                            if (event.getMember() == member) {
                                StringBuilder history = new StringBuilder();
                                for (Message message1 : channel.getHistory().retrievePast(10).complete()) { //TODO: doesn't work how i want it to as for one its backwards
                                    history.append(message1.getContentRaw() +  "\n");
                                }
                                member.getUser().openPrivateChannel().complete().sendMessage(history).complete();
                                channel.delete().reason("Issue completed!").complete();
                            }
                        }
                    }
                }
            }
        }
    }
}
