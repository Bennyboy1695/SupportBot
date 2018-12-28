package uk.co.netbans.supportbot.Support.Command.Admin;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import uk.co.netbans.supportbot.PermType;
import uk.co.netbans.supportbot.Support.Command.Command;
import uk.co.netbans.supportbot.Support.Command.CommandResult;
import uk.co.netbans.supportbot.NetBansBot;


public class Perm implements Command {

    @Override
    public CommandResult onExecute(NetBansBot bot, Member sender, TextChannel channel, String label, String[] args) {
        System.out.println("Perm being called!");
        return CommandResult.SUCCESS;
    }

    @Override
    public String name() {
        return "perm";
    }

    @Override
    public String desc() {
        return "perm_desc";
    }

    @Override
    public String usage() {
        return "perm <user|group|list|creategroup> [<user|groupname|groups> <set|add|remove|adddiscordrole|childgroupname> <permission|groupname|discordrole>";
    }

    @Override
    public String[] aliases() {
        return new String[]{"perms"};
    }

    @Override
    public PermType getPermission() {
        return PermType.ADMIN;
    }
}
