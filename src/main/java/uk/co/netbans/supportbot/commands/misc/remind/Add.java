package uk.co.netbans.supportbot.commands.misc.remind;

import me.bhop.bjdautilities.ReactionMenu;
import me.bhop.bjdautilities.command.result.CommandResult;
import me.bhop.bjdautilities.command.annotation.Command;
import me.bhop.bjdautilities.command.annotation.Execute;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import uk.co.netbans.supportbot.EmbedTemplates;
import uk.co.netbans.supportbot.NetBansBot;
import uk.co.netbans.supportbot.utils.Util;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Command(label = {"add", "create"}, minArgs = 2, usage = "remind add 1d1h1m1s How to use this")
public class Add {

    @Execute
    public CommandResult onAdd(Member member, TextChannel channel, Message message, String label, List<String> args, NetBansBot bot) {
        String length = args.get(0);
        if (length.toLowerCase().matches("([0-9]+w)?([0-9]+d)?([0-9]+h)?([0-9]+m)?([0-9]+s)?")) {
            List<String> mine = args;
            mine.remove(0);
            String msg = String.join(",", mine).replaceAll(",", " ");
            long expiry = Util.stringToMillisConverter(length);
            long finalExpiry = Instant.now().plusMillis(expiry).toEpochMilli();

            Reminder reminder = new Reminder(member.getUser().getIdLong(), channel.getIdLong(), Instant.now().toEpochMilli(), finalExpiry, msg);
            try {
                bot.getSqlManager().addNewRemind(reminder);
                bot.getMessenger().sendEmbed(channel, EmbedTemplates.SUCCESS.getEmbed().setTitle("Success").setDescription("Successfully added a reminder that will appear in " + length).addField("Message: ", msg, true).build(), 10);
            } catch (Exception e) {
                e.printStackTrace();
            }
            long duration = TimeUnit.MILLISECONDS.toSeconds(expiry);
            Executors.newSingleThreadScheduledExecutor().schedule(() -> {
                final Message tagMessage = channel.sendMessage(member.getAsMention() + " here is the reminder you asked for!").complete();
                new ReactionMenu.Builder(bot.getJDA())
                        .setEmbed(EmbedTemplates.PRETTY_SUCCESSFULL.getEmbed().setTitle("Reminder").setDescription(msg).setFooter("To delete this message react with a \u274C!", bot.getJDA().getSelfUser().getAvatarUrl()).build())
                        .setRemoveReactions(true)
                        .onClick("\u274C", (reactionMenu, reactor) -> {
                            if (reactor.equals(member)) {
                                reactionMenu.destroy();
                            } else if (reactor.hasPermission(Permission.MESSAGE_MANAGE)) {
                                reactionMenu.destroy();
                            }
                        })
                        .onDelete(delete -> tagMessage.delete().complete())
                        .buildAndDisplay(bot.getJDA().getTextChannelById(reminder.getChannelID()));
                bot.getSqlManager().removeRemind(reminder);
            }, duration, TimeUnit.SECONDS);
            return CommandResult.success();
        } else {
            return CommandResult.invalidArguments();
        }
    }
}
