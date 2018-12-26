package uk.co.netbans.supportbot.Support.Listeners;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import uk.co.netbans.supportbot.NetBansBot;
import uk.co.netbans.supportbot.PermType;


public class TicketChannelsReactionListener extends ListenerAdapter {

    private NetBansBot bot;

    public TicketChannelsReactionListener(NetBansBot bot) {
        this.bot = bot;
    }

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        if (event.isFromType(ChannelType.TEXT)) {
            TextChannel channel = (TextChannel) event.getChannel();
            if (channel.getParent().getIdLong() == Long.valueOf((String) bot.getConf().get("category")) || channel.getName().contains("manual")) {
                for (Message message : channel.getPinnedMessages().complete()) {
                    if (message.getAuthor().isBot() && event.getReactionEmote().getName().equals("\u2705")) {
                        for (Member member : message.getMentionedMembers()) {
                            if (event.getMember() == member || bot.getPerms().get(PermType.ADMIN).contains(event.getMember().getUser().getIdLong())) {
                                Message historyMessage = channel.getHistory().retrievePast(1).complete().get(0);
                                String reason = "";
                                String memberMention = "";
                                if (member.getUser().getIdLong() == event.getMember().getUser().getIdLong()) {
                                    memberMention = "you";
                                } else {
                                    memberMention = event.getMember().getAsMention();
                                }
                                if (historyMessage.getContentRaw().startsWith("~")){
                                    reason = event.getChannel().getName() + " was deleted by " + memberMention + " for reason: " + historyMessage.getContentRaw().replace("~", "");
                                } else {
                                    reason = memberMention + " deleted `" + channel.getName() + "` because the issue was marked complete!";
                                }
                                member.getUser().openPrivateChannel().complete().sendMessage(new EmbedBuilder()
                                        .setTitle("Issue Completed")
                                        .setDescription(reason.replaceAll("you", "You") + " \nBecause of this we have sent you a log file containing the history, so that you may look at it incase you encounter the issue again!")
                                        .addField("Next Step: ", "If the issue still persists, please create a github issue using the link provided below!\n" +
                                                "https://github.com/NetBans/NetBans/issues/new", false)
                                        .build()).complete();
                                member.getUser().openPrivateChannel().complete()
                                        .sendFile(bot.getLogDirectory().resolve(channel.getName()+ ".log").toFile())
                                        .complete();
                                bot.getJDA().getGuildById(Long.valueOf((String)bot.getConf().get("guildID"))).getTextChannelById(Long.valueOf((String)bot.getConf().get("logChannelID")))
                                        .sendFile(bot.getLogDirectory().resolve(channel.getName()+ ".log").toFile(), new MessageBuilder()
                                                .append(reason.replaceAll("you", event.getMember().getAsMention()))
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
