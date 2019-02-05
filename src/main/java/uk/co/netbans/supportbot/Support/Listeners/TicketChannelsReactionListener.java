package uk.co.netbans.supportbot.Support.Listeners;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import uk.co.netbans.supportbot.Message.Messenger;
import uk.co.netbans.supportbot.NetBansBot;


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
                            if (event.getMember() == member || bot.getSqlManager().userAlreadyHasPerm(event.getUser().getIdLong(), "supportbot.admin.ticket.close")) {
                                Message historyMessage = channel.getHistory().retrievePast(1).complete().get(0);
                                String memberMention = "";
                                if (member.getUser().getIdLong() == event.getMember().getUser().getIdLong()) {
                                    memberMention = "you";
                                } else {
                                    memberMention = event.getMember().getAsMention();
                                }
                                String reason = memberMention + " deleted `" + channel.getName() + "` because the issue was marked complete!";
                                if (historyMessage.getContentRaw().startsWith("~")){
                                    reason = event.getChannel().getName() + " was deleted by " + memberMention + " for reason: " + historyMessage.getContentRaw().replace("~", "");
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
                                bot.getJDA().getGuildById(bot.getGuildID()).getTextChannelById(Long.valueOf((String)bot.getConf().get("logChannelID")))
                                        .sendFile(bot.getLogDirectory().resolve(channel.getName()+ ".log").toFile(), new MessageBuilder()
                                                .append(reason.replaceFirst("you", event.getMember().getAsMention()))
                                                .build())
                                        .complete();
                                channel.delete().reason("Issue completed!").complete();
                            }
                        }
                    } else if (message.getAuthor().isBot() && event.getReactionEmote().getName().equals("\uD83D\uDD12")) {
                        if (bot.getSqlManager().userAlreadyHasPerm(event.getUser().getIdLong(), "supportbot.admin.channel.lock")) {
                            channel.sendTyping().queue();
                            for (Role role : bot.getJDA().getGuildById(bot.getGuildID()).getRoles()) {
                                channel.getManager().putPermissionOverride(role, 0L, 76800L).complete();
                            }
                            channel.getManager().putPermissionOverride(channel.getPinnedMessages().complete().get(0).getMentionedMembers().get(0), 101440L, 0L).complete();
                            bot.getMessenger().sendEmbed(channel, Messenger.CHANNEL_LOCKED);
                            System.out.println("Locked channel: " + channel.getName());
                        }
                    }
                }
            }
        }
    }
}
