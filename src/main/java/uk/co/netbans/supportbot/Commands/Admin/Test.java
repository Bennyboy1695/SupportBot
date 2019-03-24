package uk.co.netbans.supportbot.Commands.Admin;

import me.bhop.bjdautilities.command.annotation.Command;
import me.bhop.bjdautilities.command.annotation.Execute;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import uk.co.netbans.supportbot.NetBansBot;
import uk.co.netbans.supportbot.CommandFramework.CommandResult;

import java.util.List;
import java.util.Optional;

@Command(label = "test")
public class Test {

    @Execute
    public CommandResult onTest(Member member, TextChannel channel, Message message, String label, List<String> args, NetBansBot bot) {
        channel.sendMessage("test").complete();
        return CommandResult.SUCCESS;
    }
}
