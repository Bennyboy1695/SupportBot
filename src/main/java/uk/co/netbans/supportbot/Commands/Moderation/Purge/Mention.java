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

import java.util.List;

@Command(value = "mention", permission = Permission.MESSAGE_MANAGE)
public class Mention {

    @Execute
    public CommandResult onMentionUser(Member member, TextChannel channel, Message message, String label, List<String> args, NetBansBot bot) {
        String[] arguments = args.toArray(new String[0]);
        int amount = Integer.parseInt(arguments[1]);
        int count = 0;;
        if (amount > 100) {
            //bot.getMessenger().sendEmbed(channel, Messenger.AMOUNT_TOO_HIGH, 10);
            return CommandResult.INVALIDARGS;
        }
        for (Message msg : channel.getHistory().retrievePast(amount).complete()) {
            if (!msg.getMentionedMembers().isEmpty()) {
                member = msg.getMember();
                msg.delete().reason("purge requested by " + member.getEffectiveName()).complete();
                count++;
            }
        }
        if (count > 0){
            //bot.getMessenger().sendEmbed(channel, Messenger.DELETED_AMOUNT_OF_MESSAGES(count, member.getEffectiveName()), 10);
            return CommandResult.SUCCESS;
        }
        return CommandResult.TARGETNOTFOUND;
    }
}
