package uk.co.netbans.discordbot;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import uk.co.netbans.discordbot.Support.Command.Command;
import uk.co.netbans.discordbot.Support.Command.CommandResult;

public class Default implements Command {
    @Override
    public CommandResult onExecute(NetBansBot bot, Member sender, TextChannel channel, String label, String[] args) {
        bot.getMessenger().sendMessage(channel, "running default command");
        return CommandResult.SUCCESS;
    }

    @Override
    public String name() {
        return "~";
    }

    @Override
    public String desc() {
        return "desc";
    }

    @Override
    public String usage() {
        return "usage";
    }

    @Override
    public String[] aliases() {
        return new String[]{""};
    }
}
