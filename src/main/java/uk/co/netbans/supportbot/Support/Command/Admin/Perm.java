package uk.co.netbans.supportbot.Support.Command.Admin;

import uk.co.netbans.supportbot.BenCMDFramework.Command;
import uk.co.netbans.supportbot.BenCMDFramework.CommandArgs;
import uk.co.netbans.supportbot.BenCMDFramework.CommandResult;


public class Perm {

    @Command(name = "perm", aliases = "perms,permission", permission = "supportbot.command.admin.perm")
    public CommandResult onPerm(CommandArgs args) {
        System.out.println("Perm being called!");
        return CommandResult.INVALIDARGS;
    }
}
