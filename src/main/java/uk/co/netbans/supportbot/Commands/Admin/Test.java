package uk.co.netbans.supportbot.Commands.Admin;

import uk.co.netbans.supportbot.CommandFramework.Command;
import uk.co.netbans.supportbot.CommandFramework.CommandArgs;
import uk.co.netbans.supportbot.CommandFramework.CommandCategory;
import uk.co.netbans.supportbot.NetBansBot;
import uk.co.netbans.supportbot.CommandFramework.CommandResult;

public class Test {

    @Command(name = "test", displayName = "test", aliases = "tester", usage = "test", permission = "command.test.new", category = CommandCategory.ADMIN)
    public CommandResult onTest(CommandArgs args) {
        NetBansBot bot = args.getBot();
        return CommandResult.SUCCESS;
    }
}
