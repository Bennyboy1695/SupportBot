package uk.co.netbans.supportbot.Support.Command.Admin;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import uk.co.netbans.supportbot.NetBansBot;
import uk.co.netbans.supportbot.PermType;
import uk.co.netbans.supportbot.Support.Command.Command;
import uk.co.netbans.supportbot.Support.Command.CommandResult;

import java.awt.*;
import java.time.format.DateTimeFormatter;

public class ManualChannel implements Command {

    @Override
    public CommandResult onExecute(NetBansBot bot, Member sender, TextChannel channel, String label, String[] args) {
        if (args.length >= 1) {
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/YY");
            DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");
            TextChannel supportChannel;

            Member member = bot.getJDA().getGuildById(Long.valueOf((String) bot.getConf().get("guildID"))).getMemberById(args[0].replaceAll("<", "").replaceAll("@", "").replaceAll("!", "").replaceAll(">", ""));
            if (args.length != 2) {
                supportChannel = (TextChannel) bot.getJDA().getCategoryById(Long.valueOf((String) bot.getConf().get("category")))
                        .createTextChannel(member.getEffectiveName() + "-" + "manual").complete();
            } else {
                supportChannel = (TextChannel) bot.getJDA().getTextChannelById(args[1].replaceAll("<", "").replaceAll("#", "").replaceAll(">", "")).getParent().createTextChannel(member.getEffectiveName() + "-" + "manual").complete();
            }

            supportChannel.createPermissionOverride(member).setAllow(101440).complete();
            supportChannel.getManager().setTopic("Creation date: " + supportChannel.getCreationTime().format(dateFormat) + " Creation Time: " + supportChannel.getCreationTime().format(timeFormat) + "GMT").complete();
            Message message = new MessageBuilder()
                    .append("**User:** " + member.getAsMention())
                    .append("\n")
                    .append("**Message:** " + "This channel was created manually, so please see below messages for the reason!")
                    .append("\n")
                    .append("\n")
                    .append("_To close this ticket please react with a \u2705 to this message!_")
                    .build();
            Message supportMessage = bot.getMessenger().sendMessage(supportChannel, message, 0);
            supportMessage.pin().complete();
            supportChannel.getHistory().retrievePast(1).queue(l -> l.forEach(m -> m.delete().queue()));
            supportMessage.addReaction("\u2705").complete();
            member.getUser().openPrivateChannel().complete().sendMessage(new EmbedBuilder()
                    .setTitle("Support Channel")
                    .setDescription("https://discordapp.com/channels/" + bot.getConf().get("guildID") + "/" + supportChannel.getIdLong())
                    .setColor(new Color(127, 255, 212))
                    .build()).complete();
            return CommandResult.SUCCESS;
        }
        return CommandResult.INVALIDARGS;
    }

    @Override
    public String name() {
        return "channel";
    }

    @Override
    public String desc() {
        return "Manually creates a support channel with a user! If supplied with a channel the channel will be in that category!";
    }

    @Override
    public String usage() {
        return "channel <user> [channel]";
    }

    @Override
    public String[] aliases() {
        return new String[0];
    }

    @Override
    public PermType getPermission() {
        return PermType.ADMIN;
    }
}
