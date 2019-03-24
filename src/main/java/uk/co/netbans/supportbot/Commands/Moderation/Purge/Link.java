package uk.co.netbans.supportbot.Commands.Moderation.Purge;

import me.bhop.bjdautilities.command.annotation.Command;
import me.bhop.bjdautilities.command.annotation.Execute;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import uk.co.netbans.supportbot.CommandFramework.CommandResult;
//import uk.co.netbans.supportbot.Message.Messenger;
import uk.co.netbans.supportbot.NetBansBot;
import uk.co.netbans.supportbot.Utils.Util;

import java.util.List;

@Command(value = "link", permission = Permission.MESSAGE_MANAGE)
public class Link {

    @Execute
    public CommandResult onPurgeUser(Member member, TextChannel channel, Message message, String label, List<String> args, NetBansBot bot) {
        String[] arguments = args.toArray(new String[0]);
        int amount = Integer.parseInt(arguments[1]);
        int count = 0;
        Member member2 = null;
        if (amount > 100) {
            //bot.getMessenger().sendEmbed(channel, Messenger.AMOUNT_TOO_HIGH, 10);
            return CommandResult.INVALIDARGS;
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
                //bot.getMessenger().sendEmbed(channel, Messenger.DELETED_AMOUNT_OF_MESSAGES(count, member2.getEffectiveName()), 10);
            } else {
                //bot.getMessenger().sendEmbed(channel, Messenger.DELETED_AMOUNT_OF_MESSAGES(count), 10);
            }
            return CommandResult.SUCCESS;
        } else {
            //bot.getMessenger().sendEmbed(channel, Messenger.NO_MESSAGES_FOUND, 10);
        }
        return CommandResult.TARGETNOTFOUND;
    }
}
