package uk.co.netbans.supportbot.commands.misc.remind;

import me.bhop.bjdautilities.ReactionMenu;
import me.bhop.bjdautilities.command.annotation.Command;
import me.bhop.bjdautilities.command.annotation.Execute;
import me.bhop.bjdautilities.command.result.CommandResult;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import uk.co.netbans.supportbot.EmbedTemplates;
import uk.co.netbans.supportbot.SupportBot;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

@Command(value = "list")
public class List {

    @Execute
    public CommandResult onList(Member member, TextChannel channel, Message message, String label, java.util.List<String> args, SupportBot bot) {
        EmbedBuilder embed = EmbedTemplates.PRETTY_SUCCESSFULL.getEmbed();
        embed.setTitle("Reminders for: " + member.getEffectiveName());
        for (Reminder reminder : bot.getSqlManager().getRemindersForUser(member.getUser().getIdLong())) {
            if (Instant.ofEpochMilli(reminder.getExpiry()).isAfter(Instant.now())) {
                long duration = Instant.ofEpochMilli(reminder.getExpiry()).minusMillis(Instant.now().toEpochMilli()).toEpochMilli();
                long secs = TimeUnit.MILLISECONDS.toSeconds(duration);
                embed.addField("Message shown in " + secs + "s :", reminder.getMessage(), true);
            }
        }
        new ReactionMenu.Builder(bot.getJDA())
                .setStartingReactions("\u274C")
                .setEmbed(embed.build())
                .onClick("\u274C", reactionMenu -> reactionMenu.destroy())
                .onDestroy(delete -> {
                    System.out.println("Deleted");
                })
                .buildAndDisplay(channel).destroyIn(30);
        return CommandResult.success();
    }
}
