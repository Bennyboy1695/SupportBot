package uk.co.netbans.discordbot.Music.Command;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import uk.co.netbans.discordbot.Support.Command.Command;
import uk.co.netbans.discordbot.Support.Command.CommandResult;
import uk.co.netbans.discordbot.NetBansBot;

public class Reset implements Command {
    private NetBansBot bot;

    @Override
    public CommandResult onExecute(NetBansBot bot, Member sender, TextChannel channel, String label, String[] args) {
        this.bot = bot;
        bot.getMusicManager().reset(channel.getGuild());
        bot.getMessenger().sendMessage(channel, "\uD83D\uDD04 Resetting the music player..", 10);
        return CommandResult.SUCCESS;
    }

    @Override
    public String name() {
        return "reset";
    }

    @Override
    public String desc() {
        return "Reset the music player.";
    }

    @Override
    public String usage() {
        return bot.getCommandPrefix() + "reset";
    }

    @Override
    public String[] aliases() {
        return new String[]{"leave"};
    }
}
