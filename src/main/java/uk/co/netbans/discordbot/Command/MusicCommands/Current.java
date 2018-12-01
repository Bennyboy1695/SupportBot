package uk.co.netbans.discordbot.Command.MusicCommands;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import uk.co.netbans.discordbot.Command.Command;
import uk.co.netbans.discordbot.Command.CommandCode;
import uk.co.netbans.discordbot.Music.MusicManager;
import uk.co.netbans.discordbot.NetBansBot;
import uk.co.netbans.discordbot.Util;

public class Current implements Command {
    @Override
    public CommandCode onExecute(NetBansBot bot, Member sender, TextChannel channel, String label, String[] args) {
        MusicManager music = bot.getMusicManager();

        if (!music.hasPlayer(channel.getGuild()) || music.getPlayer(channel.getGuild()).getPlayingTrack() == null) { // No song is playing
            bot.getMessenger().sendMessage(channel, "No song is being played at the moment! *It's your time to shine..*", 10);
        } else {
            AudioTrack track = music.getPlayer(channel.getGuild()).getPlayingTrack();
            music.sendEmbed(channel, "Track Info", String.format(music.QUEUE_DESCRIPTION, music.CD, music.getOrNull(track.getInfo().title),
                    "\n\u23F1 **|>** `[ " + music.getTimestamp(track.getPosition()) + " / " + music.getTimestamp(track.getInfo().length) + " ]`",
                    "\n" + music.MIC, music.getOrNull(track.getInfo().author),
                    "\n\uD83C\uDFA7 **|>**  " + Util.userDiscrimSet(music.getTrackManager(channel.getGuild()).getTrackInfo(track).getAuthor().getUser())));
        }
        return CommandCode.OK;
    }

    @Override
    public String name() {
        return "current";
    }

    @Override
    public String desc() {
        return "View info about the current song.";
    }

    @Override
    public String usage() {
        return "!current";
    }

    @Override
    public String[] aliases() {
        return new String[]{"curr", "nowplaying", "info"};
    }
}
