package uk.co.netbans.supportbot.Support.Command.Admin;

import uk.co.netbans.supportbot.BenCMDFramework.Command;
import uk.co.netbans.supportbot.BenCMDFramework.CommandArgs;
import uk.co.netbans.supportbot.NetBansBot;
import uk.co.netbans.supportbot.BenCMDFramework.CommandResult;

import java.util.Arrays;

public class Test {

    @Command(name = "test", displayName = "test", aliases = "tester", usage = "test", permission = "command.test.new")
    public CommandResult onTest(CommandArgs args) {
        NetBansBot bot = args.getBot();

        System.out.println(Arrays.toString(args.getArgs()));
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
}
