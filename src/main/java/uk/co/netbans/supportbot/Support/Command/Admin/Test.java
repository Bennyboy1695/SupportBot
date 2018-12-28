package uk.co.netbans.supportbot.Support.Command.Admin;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import uk.co.netbans.supportbot.NetBansBot;
import uk.co.netbans.supportbot.PermType;
import uk.co.netbans.supportbot.Support.Command.Command;
import uk.co.netbans.supportbot.Support.Command.CommandResult;

import java.util.Optional;

public class Test implements Command {

    @Override
    public CommandResult onExecute(NetBansBot bot, Member sender, TextChannel channel, String label, String[] args) {
        try {
            if (bot.getSqlManager().addNewGroup("test")) System.out.println("Empty Args (Both)");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (bot.getSqlManager().addNewGroup("testwithchild", "test")) System.out.println("Empty Args (Role)");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (bot.getSqlManager().addNewGroup("testwithall", "testwithchild", 123456789L)) System.out.println("Full Args");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (bot.getSqlManager().addNewGroupPerm("test", "command.test")) System.out.println("Added 1 Group Perm");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (bot.getSqlManager().addNewGroupPerm("test2", "command.test.2")) System.out.println("Added 2 Group Perm");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (bot.getSqlManager().removeGroupPerm("test2", "command.test.2")) System.out.println("Removed 2nd Group Perm");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return CommandResult.SUCCESS;
    }

    @Override
    public String name() {
        return "test";
    }

    @Override
    public String desc() {
        return "A test command!";
    }

    @Override
    public String usage() {
        return "test";
    }

    @Override
    public String[] aliases() {
        return new String[0];
    }

    @Override
    public PermType getPermission() {
        return PermType.ADMIN;
    }
}
