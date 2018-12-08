package uk.co.netbans.discordbot.Command.MusicCommands;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import uk.co.netbans.discordbot.Command.Command;
import uk.co.netbans.discordbot.Command.CommandResult;
import uk.co.netbans.discordbot.Music.MusicManager;
import uk.co.netbans.discordbot.NetBansBot;

public class Shuffle implements Command {
    @Override
    public CommandResult onExecute(NetBansBot bot, Member sender, TextChannel channel, String label, String[] args) {
        MusicManager music = bot.getMusicManager();

        if (music.isIdle(channel.getGuild()))
            return CommandResult.SUCCESS;

        music.getTrackManager(channel.getGuild()).shuffleQueue();
        bot.getMessenger().sendMessage(channel, "\u2705 Shuffled the queue!", 10);
        return CommandResult.SUCCESS;
    }

    @Override
    public String name() {
        return "shuffle";
    }

    @Override
    public String desc() {
        return "Shuffle the current play queue.";
    }

    @Override
    public String usage() {
        return "!shuffle";
    }

    @Override
    public String[] aliases() {
        return new String[0];
    }
}
