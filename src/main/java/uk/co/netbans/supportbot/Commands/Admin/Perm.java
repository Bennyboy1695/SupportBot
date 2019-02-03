package uk.co.netbans.supportbot.Commands.Admin;

import uk.co.netbans.supportbot.CommandFramework.Command;
import uk.co.netbans.supportbot.CommandFramework.CommandArgs;
import uk.co.netbans.supportbot.CommandFramework.CommandCategory;
import uk.co.netbans.supportbot.CommandFramework.CommandResult;


public class Perm {

    @Command(name = "perm", displayName = "perm", aliases = "perms,permission", permission = "supportbot.command.admin.perm", usage = "perm <user|group|list|creategroup> <group|groups|user> [add|set|remove> <perm>]", category = CommandCategory.ADMIN)
    public CommandResult onPerm(CommandArgs args) {
        return CommandResult.SUCCESS;
    }
}
