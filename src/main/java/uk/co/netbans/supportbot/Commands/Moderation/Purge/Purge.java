package uk.co.netbans.supportbot.Commands.Moderation.Purge;

import uk.co.netbans.supportbot.CommandFramework.Command;
import uk.co.netbans.supportbot.CommandFramework.CommandArgs;
import uk.co.netbans.supportbot.CommandFramework.CommandCategory;
import uk.co.netbans.supportbot.CommandFramework.CommandResult;

public class Purge {

    @Command(name = "purge", displayName = "purge", aliases = "purged", permission = "supportbot.command.moderation.purge", category = CommandCategory.MODERATION)
    public CommandResult onPurge(CommandArgs args) {
        return CommandResult.SUCCESS;
    }

}
