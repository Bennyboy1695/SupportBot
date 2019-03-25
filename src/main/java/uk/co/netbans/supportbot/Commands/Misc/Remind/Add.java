package uk.co.netbans.supportbot.Commands.Misc.Remind;

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
import uk.co.netbans.supportbot.Utils.Util;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

@Command(label = {"add", "create"}, minArgs = 2, usage = "remind add 1d1h1m1s How to use this")
public class Add {

    @Execute
    public CommandResult onAdd(Member member, TextChannel channel, Message message, String label, List<String> args, NetBansBot bot) {
        String length = args.get(0);
        List<String> mine = args;
        mine.remove(0);
        String msg = String.join(",", mine).replaceAll(",", " ");
        long expiry = Util.stringToMillisConverter(length);
        long finalExpiry = Instant.now().plusMillis(expiry).toEpochMilli();

        Reminder reminder = new Reminder(member.getUser().getIdLong(), Instant.now().toEpochMilli(), finalExpiry, msg);
        try {
            bot.getSqlManager().addNewRemind(reminder);
            bot.getMessenger().sendEmbed(channel, EmbedTemplates.SUCCESS.getEmbed().setTitle("Success").setDescription("Successfully added a reminder that will appear in " + length).addField("Message: ", msg, true).build(), 10);
        } catch (Exception e) {
            e.printStackTrace();
        }
        long duration = TimeUnit.MILLISECONDS.toSeconds(expiry);
        Executors.newSingleThreadScheduledExecutor().schedule(() -> {
            AtomicReference<Message> tagMsg = new AtomicReference<>();
            ReactionMenu menu = new ReactionMenu.Builder(bot.getJDA())
                    .setEmbed(EmbedTemplates.PRETTY_SUCCESSFULL.getEmbed().setTitle("Reminder").addField("Message: " , msg, true).setFooter("To delete this message react with a \u274C!", bot.getJDA().getSelfUser().getAvatarUrl()).build())
                    .setRemoveReactions(true)
                    .onClick("\u274C", (reactionMenu, reactor) -> {
                        if (reactor.equals(member)) {
                            reactionMenu.destroy();
                        } else if (reactor.hasPermission(Permission.MESSAGE_MANAGE)) {
                            reactionMenu.destroy();
                        }
                    })
                    .onDisplay(show -> {
                        tagMsg.set(channel.sendMessage(member.getAsMention() + " here is the reminder you asked for!").complete());
                    })
                    .onDelete(delete -> {
                        tagMsg.get().delete().complete();
                    }).buildAndDisplay(channel);
            bot.getSqlManager().removeRemind(reminder);
        }, duration , TimeUnit.SECONDS);
        return CommandResult.success();
    }
}
