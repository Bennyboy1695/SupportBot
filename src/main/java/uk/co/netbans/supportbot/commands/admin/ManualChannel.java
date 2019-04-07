package uk.co.netbans.supportbot.commands.admin;


import me.bhop.bjdautilities.command.annotation.Command;
import me.bhop.bjdautilities.command.annotation.Execute;
import me.bhop.bjdautilities.command.result.CommandResult;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Category;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import uk.co.netbans.supportbot.SupportBot;

import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Command(label = "channel", permission = Permission.MANAGE_CHANNEL)
public class ManualChannel {

    @Execute
    public CommandResult onManualChannel(Member member, TextChannel channel, Message message, String label, List<String> args, SupportBot bot) {
        if (args.size() >= 2) {
            if (args.get(0).equals("create")) {
                DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/YY");
                DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");
                TextChannel supportChannel;

                Member member2 = bot.getJDA().getGuildById(bot.getGuildID()).getMemberById(args.get(1).replaceAll("<", "").replaceAll("@", "").replaceAll("!", "").replaceAll(">", ""));
                if (args.size() != 3) {
                    supportChannel = (TextChannel) bot.getJDA().getCategoryById(Long.valueOf(bot.getMainConfig().getConfigValue("category").getAsString()))
                            .createTextChannel(member2.getEffectiveName() + "-" + "manual").complete();
                } else {
                    supportChannel = (TextChannel) bot.getJDA().getTextChannelById(args.get(2).replaceAll("<", "").replaceAll("#", "").replaceAll(">", "")).getParent().createTextChannel(member2.getEffectiveName() + "-" + "manual").complete();
                }

                supportChannel.createPermissionOverride(member2).setAllow(101440).complete();
                supportChannel.getManager().setTopic("Creation date: " + supportChannel.getCreationTime().format(dateFormat) + " Creation Time: " + supportChannel.getCreationTime().format(timeFormat) + "GMT").complete();
                Message message2 = new MessageBuilder()
                        .append("**User:** " + member2.getAsMention())
                        .append("\n")
                        .append("**Message:** " + "This channel was created manually, so please see below messages for the reason!")
                        .append("\n")
                        .append("\n")
                        .append("_To close this ticket please react with a \u2705 to this message2!_")
                        .build();
                //Message supportMessage = bot.getMessenger().sendMessage(supportChannel, message2, 0);
                //supportMessage.pin().complete();
                //supportChannel.getHistory().retrievePast(1).queue(l -> l.forEach(m -> m.delete().queue()));
                //supportMessage.addReaction("\u2705").complete();
                member2.getUser().openPrivateChannel().complete().sendMessage(new EmbedBuilder()
                        .setTitle("Support Channel")
                        .setDescription("https://discordapp.com/channels/" + bot.getGuildID() + "/" + supportChannel.getIdLong())
                        .setColor(new Color(127, 255, 212))
                        .build()).complete();
                return CommandResult.success();
            } else if (args.get(0).equals("move")) {
                Category original = channel.getParent();
                TextChannel channelArg = bot.getJDA().getTextChannelById(args.get(1).replaceAll("<", "").replaceAll("#", "").replaceAll(">", ""));
                channel.getManager().setParent(channelArg.getParent()).complete();
                channel.getManager().setName(channel.getName().replaceAll("-[0-9]*", "-manual")).complete();
                channel.sendMessage("**Channel was moved from Category: " + original.getName() + " to Category: " + channelArg.getParent().getName() + "!**").complete();
                return CommandResult.success();
            } else {
                return CommandResult.invalidArguments();
            }
        }
        return CommandResult.invalidArguments();
    }
}
