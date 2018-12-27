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
            System.out.println("Empty Args (Both)");
            bot.getSqlManager().addNewGroup("test");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            System.out.println("Empty Args (Role)");
            bot.getSqlManager().addNewGroup("testwithchild", "test");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            System.out.println("Full Args");
            bot.getSqlManager().addNewGroup("testwithall", "testwithchild", String.valueOf(123456789L));
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
