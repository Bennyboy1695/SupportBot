package uk.co.netbans.supportbot.Commands.Admin;


import me.bhop.bjdautilities.command.annotation.Command;
import me.bhop.bjdautilities.command.annotation.Execute;
import me.bhop.bjdautilities.command.result.CommandResult;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import uk.co.netbans.supportbot.NetBansBot;

import java.util.Arrays;
import java.util.List;

@Command(label = "say", permission = Permission.MESSAGE_MANAGE)
public class Say {

    @Execute
    public CommandResult onSay(Member member, TextChannel channel, Message message, String label, List<String> args, NetBansBot bot) {
        bot.getMessenger().sendMessage(channel, String.join(" ", args), 30);
        return CommandResult.success();
    }
}
