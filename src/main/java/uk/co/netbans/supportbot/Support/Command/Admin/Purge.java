package uk.co.netbans.supportbot.Support.Command.Admin;

import uk.co.netbans.supportbot.BenCMDFramework.Command;
import uk.co.netbans.supportbot.BenCMDFramework.CommandArgs;
import uk.co.netbans.supportbot.BenCMDFramework.CommandResult;

import java.util.Arrays;

public class Purge {

    @Command(name = "test.purge", displayName = "purge", aliases = "test.purged", permission = "command.test.purge")
    public CommandResult onPurge(CommandArgs args) {
        System.out.println("Purge!!");
        System.out.println(Arrays.toString(args.getArgs()));

        return CommandResult.SUCCESS;
    }

}
