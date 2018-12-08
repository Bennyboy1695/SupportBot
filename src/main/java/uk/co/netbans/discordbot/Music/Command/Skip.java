package uk.co.netbans.discordbot.Music.Command;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import uk.co.netbans.discordbot.Support.Command.Command;
import uk.co.netbans.discordbot.Support.Command.CommandResult;
import uk.co.netbans.discordbot.NetBansBot;

public class Skip implements Command {
    @Override
    public CommandResult onExecute(NetBansBot bot, Member sender, TextChannel channel, String label, String[] args) {
        bot.getMusicManager().skipTrack(sender.getGuild(), channel);
        bot.getMessenger().sendMessage(channel, "\u23E9 Skipping current track.", 10);
        return CommandResult.SUCCESS;
    }

    @Override
    public String name() {
        return "skip";
    }

    @Override
    public String desc() {
        return "Skip the current song.";
    }

    @Override
    public String usage() {
        return "!skip";
    }

    @Override
    public String[] aliases() {
        return new String[0];
    }
}
