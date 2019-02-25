package uk.co.netbans.supportbot.Commands;

import uk.co.netbans.supportbot.CommandFramework.Command;
import uk.co.netbans.supportbot.CommandFramework.CommandArgs;
import uk.co.netbans.supportbot.CommandFramework.CommandResult;
import uk.co.netbans.supportbot.Message.NewMessenger;

public class btest {
    private final NewMessenger messenger = new NewMessenger();

    @Command(name = "bhop", displayName = "bhop", aliases = "", usage = "shut up and die")
    public CommandResult onExecute(CommandArgs args) {



        return CommandResult.SUCCESS;
    }
}
