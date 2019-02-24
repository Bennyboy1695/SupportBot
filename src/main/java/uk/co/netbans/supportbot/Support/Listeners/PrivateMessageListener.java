package uk.co.netbans.supportbot.Support.Listeners;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import uk.co.netbans.supportbot.NetBansBot;

import java.awt.*;
import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

public class PrivateMessageListener extends ListenerAdapter {

    private NetBansBot bot;
    private int userCount;

    public PrivateMessageListener(NetBansBot bot) {
        this.bot = bot;
    }

    @Override
    public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/YY");
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");
        if (event.getAuthor().isBot()) return;

        String userMessage = event.getMessage().getContentRaw();

        Member member = bot.getJDA().getGuildById(bot.getGuildID()).getMember(event.getAuthor());

        if (member.getRoles().contains(bot.getJDA().getGuildById(bot.getGuildID()).getRoleById(bot.getConfig().getConfigValue("noHelpRoleID").getAsString()))) {
            member.getUser().openPrivateChannel().complete().sendMessage("No channel has been created because you have the anti-support role!").complete();
            return;
        }
        for (Guild.Ban bans : bot.getJDA().getGuildById(bot.getGuildID()).getBanList().complete()) {
            if (bans.getUser().getIdLong() == member.getUser().getIdLong())
                return;
        }

        for (TextChannel channel : bot.getJDA().getCategoryById(Long.valueOf(bot.getConfig().getConfigValue("category").getAsString())).getTextChannels()) {
            if (channel.getName().startsWith(event.getAuthor().getName())) {
                userCount++;
                if (userCount >= 3) {
                    member.getUser().openPrivateChannel().complete().sendMessage("No channel has been created because you multiple channels open already. Please complete these issue first!").complete();
                    return;
                }
            }
        }

        TextChannel supportChannel = (TextChannel) bot.getJDA().getCategoryById(Long.valueOf(bot.getConfig().getConfigValue("category").getAsString()))
                .createTextChannel(event.getAuthor().getName() + "-" + ThreadLocalRandom.current().nextInt(99999)).complete();

        supportChannel.createPermissionOverride(member).setAllow(101440).complete();
        supportChannel.getManager().setTopic("Creation date: "+ supportChannel.getCreationTime().format(dateFormat) + " Creation Time: " + supportChannel.getCreationTime().format(timeFormat) + "GMT").complete();
        Message message = new MessageBuilder()
                .append("**Author:** " + member.getAsMention())
                .append("\n")
                .append("**Message:** " + userMessage)
                .append("\n")
                .append("\n")
                .append("_To close this ticket please react with a \u2705 to this message!_")
                .build();
        Message supportMessage = bot.getMessenger().sendMessage(supportChannel, message, 0);
        for (Message.Attachment attachment : event.getMessage().getAttachments()) {
                try {
                    if (!new File(bot.getLogDirectory().toFile(), "tmp").exists()) {
                        new File(bot.getLogDirectory().toFile(), "tmp").mkdir();
                    }
                    attachment.download(new File(bot.getLogDirectory().toFile() + "/tmp/", supportChannel.getName() + ".log"));
                    supportChannel.sendFile(new File(bot.getLogDirectory().toFile() + "/tmp/", supportChannel.getName() + ".log")).complete();
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
        supportMessage.pin().complete();
        supportChannel.getHistory().retrievePast(1).queue(l -> l.forEach(m -> m.delete().queue()));
        supportMessage.addReaction("\u2705").complete();
        supportMessage.addReaction("\uD83D\uDD12").complete();
        supportMessage.addReaction("\uD83D\uDD13").complete();
        event.getAuthor().openPrivateChannel().complete().sendMessage(new EmbedBuilder()
                .setTitle("Support Channel")
                .setDescription("https://discordapp.com/channels/" + bot.getGuildID()  + "/" + supportChannel.getIdLong())
                .setColor(new Color(127, 255, 212))
                .build()).complete();



    }
}
