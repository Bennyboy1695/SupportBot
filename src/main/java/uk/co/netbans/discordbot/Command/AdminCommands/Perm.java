package uk.co.netbans.discordbot.Command.AdminCommands;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import uk.co.netbans.discordbot.Command.Command;
import uk.co.netbans.discordbot.Command.CommandResult;
import uk.co.netbans.discordbot.NetBansBot;

public class Perm implements Command {

    @Override
    public CommandResult onExecute(NetBansBot bot, Member sender, TextChannel channel, String label, String[] args) {
        return null;
    }

    @Override
    public String name() {
        return null;
    }

    @Override
    public String desc() {
        return null;
    }

    @Override
    public String usage() {
        return null;
    }

    @Override
    public String[] aliases() {
        return new String[0];
    }
}
