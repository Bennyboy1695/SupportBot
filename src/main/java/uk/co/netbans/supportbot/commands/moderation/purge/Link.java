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
import uk.co.netbans.supportbot.utils.Util;

import java.util.List;

@Command(value = "link", permission = Permission.MESSAGE_MANAGE)
public class Link {

    @Execute
    public CommandResult onPurgeUser(Member member, TextChannel channel, Message message, String label, List<String> args, SupportBot bot) {
        String[] arguments = args.toArray(new String[0]);
        int amount = Integer.parseInt(arguments[1]);
        int count = 0;
        Member member2 = null;
        if (amount > 100) {
            bot.getMessenger().sendEmbed(channel, EmbedTemplates.AMOUNT_TOO_HIGH.getEmbed(100).build(), 10);
            return CommandResult.invalidArguments();
        }

        if (arguments.length == 3) {
            member2 = bot.getJDA().getGuildById(bot.getGuildID()).getMemberById(arguments[2].replaceAll("<", "").replaceAll("@", "").replaceAll("!", "").replaceAll(">", ""));
        }
        for (Message msg : channel.getHistory().retrievePast(100).complete()) {
            if (member2 != null && Util.containsLink(msg.getContentRaw()) && count < amount) {
                msg.delete().reason("purge requested by " + member.getEffectiveName()).complete();
                count++;
            } else if (Util.containsLink(msg.getContentRaw()) && count < amount) {
                msg.delete().reason("purge requested by " + member.getEffectiveName()).complete();
                count++;
            }
        }
        if (count > 0){
            if (member2 != null) {
                bot.getMessenger().sendEmbed(channel, EmbedTemplates.DELETED_X_MESSAGES_FROM.getEmbed(count, member2.getEffectiveName()).build(), 10);
            } else {
                bot.getMessenger().sendEmbed(channel, EmbedTemplates.DELETED_X_MESSAGES.getEmbed(count).build(), 10);
            }
        } else {
            bot.getMessenger().sendEmbed(channel, EmbedTemplates.NO_MESSAGES_FOUND.getBuilt(), 10);
        }
        return CommandResult.success();
    }
}
