package uk.co.netbans.supportbot.Moderation.Purge;

import uk.co.netbans.supportbot.BenCMDFramework.Command;
import uk.co.netbans.supportbot.BenCMDFramework.CommandArgs;
import uk.co.netbans.supportbot.BenCMDFramework.CommandResult;

import java.util.Arrays;

public class Purge {

    @Command(name = "purge", displayName = "purge", aliases = "purged", permission = "supportbot.command.moderation.purge")
    public CommandResult onPurge(CommandArgs args) {
        return CommandResult.SUCCESS;
    }

}
