package uk.co.netbans.supportbot.commands.moderation.purge;


import me.bhop.bjdautilities.command.annotation.Command;
import me.bhop.bjdautilities.command.annotation.Execute;
import me.bhop.bjdautilities.command.result.CommandResult;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import uk.co.netbans.supportbot.NetBansBot;

import java.util.List;

@Command(label = "purge", permission = Permission.MESSAGE_MANAGE, children = {Link.class, Mention.class, User.class})
public class Purge {

    @Execute
    public CommandResult onPurge(Member member, TextChannel channel, Message message, String label, List<String> args, NetBansBot bot) {
        System.out.println("Purge parent");
        return CommandResult.success();
    }

}
