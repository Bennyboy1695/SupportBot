package uk.co.netbans.supportbot.Commands.Admin;

import net.dv8tion.jda.core.entities.TextChannel;
import uk.co.netbans.supportbot.CommandFramework.Command;
import uk.co.netbans.supportbot.CommandFramework.CommandArgs;
import uk.co.netbans.supportbot.CommandFramework.CommandCategory;
import uk.co.netbans.supportbot.CommandFramework.CommandResult;

import java.util.Arrays;

public class Say {

    @Command(name = "say", displayName = "say", permission = "command.admin.say", category = CommandCategory.ADMIN)
    public CommandResult onSay(CommandArgs args) {
        args.getBot().getMessenger().sendMessage((TextChannel) args.getChannel(), String.join(" ", args.getArgs()), 30);
        return CommandResult.SUCCESS;
    }
}
