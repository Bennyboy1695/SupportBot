package uk.co.netbans.discordbot.Command.SupportCommands;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import uk.co.netbans.discordbot.Command.Command;
import uk.co.netbans.discordbot.Command.CommandResult;
import uk.co.netbans.discordbot.NetBansBot;

public class Ticket implements Command {

    @Override
    public CommandResult onExecute(NetBansBot bot, Member sender, TextChannel channel, String label, String[] args) {
        return CommandResult.SUCCESS;
    }

    @Override
    public String name() {
        return "ticket";
    }

    @Override
    public String desc() {
        return "Provides you with information on how to create a ticket!";
    }

    @Override
    public String usage() {
        return "!ticket";
    }

    @Override
    public String[] aliases() {
        return new String[0];
    }
}
