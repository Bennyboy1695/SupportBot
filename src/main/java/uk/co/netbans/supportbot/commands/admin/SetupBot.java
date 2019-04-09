package uk.co.netbans.supportbot.commands.admin;

import me.bhop.bjdautilities.command.annotation.Command;
import me.bhop.bjdautilities.command.annotation.Execute;
import me.bhop.bjdautilities.command.result.CommandResult;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import uk.co.netbans.supportbot.SupportBot;

import java.util.List;

@Command(value = "setup", permission = Permission.ADMINISTRATOR)
public class SetupBot {

    @Execute
    public CommandResult onSetup(Member member, TextChannel channel, Message message, String label, List<String> args, SupportBot bot) {

        return CommandResult.success();
    }
}
