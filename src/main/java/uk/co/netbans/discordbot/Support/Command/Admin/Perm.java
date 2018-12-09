package uk.co.netbans.discordbot.Support.Command.Admin;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import uk.co.netbans.discordbot.Support.Command.Command;
import uk.co.netbans.discordbot.Support.Command.CommandResult;
import uk.co.netbans.discordbot.NetBansBot;

public class Perm implements Command {

    @Override
    public CommandResult onExecute(NetBansBot bot, Member sender, TextChannel channel, String label, String[] args) {
        bot.getMessenger().sendMessage(channel, "Allowed users: <@" + bot.getPerms().get(0) + "> <@" + bot.getPerms().get(1) + ">");
        return null;
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
        return "!perm";
    }

    @Override
    public String[] aliases() {
        return new String[0];
    }
}
