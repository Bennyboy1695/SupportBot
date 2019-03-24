package uk.co.netbans.supportbot.Commands.Moderation.Purge;

import me.bhop.bjdautilities.command.annotation.Command;
import me.bhop.bjdautilities.command.annotation.Execute;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import uk.co.netbans.supportbot.CommandFramework.CommandResult;
import uk.co.netbans.supportbot.NetBansBot;

import java.util.List;

@Command(label = "purge", permission = Permission.MESSAGE_MANAGE, children = {Link.class, Mention.class, User.class})
public class Purge {

    @Execute
    public CommandResult onPurge(Member member, TextChannel channel, Message message, String label, List<String> args, NetBansBot bot) {
        return CommandResult.SUCCESS;
    }

}
