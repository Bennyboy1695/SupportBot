package uk.co.netbans.supportbot.Commands.Admin;

import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import uk.co.netbans.supportbot.CommandFramework.Command;
import uk.co.netbans.supportbot.CommandFramework.CommandArgs;
import uk.co.netbans.supportbot.CommandFramework.CommandCategory;
import uk.co.netbans.supportbot.NetBansBot;
import uk.co.netbans.supportbot.CommandFramework.CommandResult;

import java.util.Optional;

public class Test {

    @Command(name = "test", displayName = "test", aliases = "tester", usage = "test", permission = "command.test.new", category = CommandCategory.ADMIN)
    public CommandResult onTest(CommandArgs args) {
        NetBansBot bot = args.getBot();
        return CommandResult.SUCCESS;
    }
}
