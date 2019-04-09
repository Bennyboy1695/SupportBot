package uk.co.netbans.supportbot.commands.moderation.purge;


import me.bhop.bjdautilities.command.annotation.Command;
import me.bhop.bjdautilities.command.annotation.Execute;
import me.bhop.bjdautilities.command.result.CommandResult;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import uk.co.netbans.supportbot.EmbedTemplates;
import uk.co.netbans.supportbot.SupportBot;

import java.util.List;

@Command(value = "user", permission = Permission.MESSAGE_MANAGE)
public class User {

    @Execute
    public CommandResult onPurgeUser(Member member, TextChannel channel, Message message, String label, List<String> args, SupportBot bot) {
        System.out.println("Purge user");
        String[] arguments = args.toArray(new String[0]);
        int amount = Integer.parseInt(arguments[1]);
        int count = 0;
        if (amount > 100) {
            bot.getMessenger().sendEmbed(channel, EmbedTemplates.AMOUNT_TOO_HIGH.getEmbed(100).build(), 10);
            return CommandResult.invalidArguments();
        }

        Member member2 = bot.getJDA().getGuildById(bot.getGuildID()).getMemberById(arguments[0].replaceAll("<", "").replaceAll("@", "").replaceAll("!", "").replaceAll(">", ""));
        for (Message msg : channel.getHistory().retrievePast(amount).complete()) {
                if (msg.getAuthor() == member2.getUser()) {
                    msg.delete().reason("purge requested by " + member.getEffectiveName()).complete();
                    count++;
                }
        }
        if (count > 0)
            bot.getMessenger().sendEmbed(channel, EmbedTemplates.DELETED_X_MESSAGES_FROM.getEmbed(count, member2.getEffectiveName()).build(), 10);
        else
            bot.getMessenger().sendEmbed(channel, EmbedTemplates.UNKNOWN_TARGET.getBuilt(), 10);
        return CommandResult.success();
    }
}
