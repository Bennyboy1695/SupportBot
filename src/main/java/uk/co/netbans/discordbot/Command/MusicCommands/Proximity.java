package uk.co.netbans.discordbot.Command.MusicCommands;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import uk.co.netbans.discordbot.Command.Command;
import uk.co.netbans.discordbot.Command.CommandResult;
import uk.co.netbans.discordbot.Music.MusicManager;
import uk.co.netbans.discordbot.NetBansBot;

public class Proximity implements Command {
    @Override
    public CommandResult onExecute(NetBansBot bot, Member sender, TextChannel channel, String label, String[] args) {
        MusicManager music = bot.getMusicManager();
        music.loadTrack("https://www.youtube.com/playlist?list=PLZb6kNIh9TrHokGyJZrvJEhMpO78UMiQM", sender, channel);
        music.getTrackManager(channel.getGuild()).shuffleQueue();
        return CommandResult.SUCCESS;
    }

    @Override
    public String name() {
        return "proximity";
    }

    @Override
    public String desc() {
        return "Add the proximity playlist and shuffle the queue.";
    }

    @Override
    public String usage() {
        return "!proximity";
    }

    @Override
    public String[] aliases() {
        return new String[]{"pp"};
    }
}
