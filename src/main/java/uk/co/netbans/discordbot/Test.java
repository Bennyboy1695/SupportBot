package uk.co.netbans.discordbot;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import uk.co.netbans.discordbot.Support.Command.Command;
import uk.co.netbans.discordbot.Support.Command.CommandResult;

public class Test implements Command {
    @Override
    public CommandResult onExecute(NetBansBot bot, Member sender, TextChannel channel, String label, String[] args) {
        System.out.println("I am a subcommand!");

        return CommandResult.SUCCESS;
    }

    @Override
    public String name() {
        return "test";
    }

    @Override
    public String desc() {
        return "A test subcommand";
    }

    @Override
    public String usage() {
        return "/parent test";
    }

    @Override
    public String[] aliases() {
        return new String[]{"testsub"};
    }
}
