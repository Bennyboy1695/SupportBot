package uk.co.netbans.supportbot.Commands.Music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import uk.co.netbans.supportbot.CommandFramework.Command;
import uk.co.netbans.supportbot.CommandFramework.CommandArgs;
import uk.co.netbans.supportbot.CommandFramework.CommandCategory;
import uk.co.netbans.supportbot.CommandFramework.CommandResult;
import uk.co.netbans.supportbot.Music.MusicManager;
import uk.co.netbans.supportbot.NetBansBot;
import uk.co.netbans.supportbot.Utils.Util;

public class Current {

    @Command(name = "playing", displayName = "playing", permission = "supportbot.command.music.playing", category = CommandCategory.MUSIC)
    public CommandResult onNowPlaying(CommandArgs commandArgs) {
        NetBansBot bot = commandArgs.getBot();
        Member member = commandArgs.getMember();
        TextChannel channel = (TextChannel) commandArgs.getChannel();
        MusicManager musicPlayer = bot.getMusicManager();
        String[] args = commandArgs.getArgs();

        if (!musicPlayer.hasPlayer(channel.getGuild()) || musicPlayer.getPlayer(channel.getGuild()).getPlayingTrack() == null) { // No song is playing
            bot.getMessenger().sendMessage(channel, "No song is being played at the moment! *It's your time to shine..*", 10);
        } else {
            AudioTrack track = musicPlayer.getPlayer(channel.getGuild()).getPlayingTrack();
            musicPlayer.sendEmbed(channel, "Track Info", String.format(musicPlayer.QUEUE_DESCRIPTION, musicPlayer.CD, musicPlayer.getOrNull(track.getInfo().title),
                    "\n\u23F1 **|>** `[ " + musicPlayer.getTimestamp(track.getPosition()) + " / " + musicPlayer.getTimestamp(track.getInfo().length) + " ]`",
                    "\n" + musicPlayer.MIC, musicPlayer.getOrNull(track.getInfo().author),
                    "\n\uD83C\uDFA7 **|>**  " + Util.userDiscrimSet(musicPlayer.getTrackManager(channel.getGuild()).getTrackInfo(track).getAuthor().getUser())));
        }
        return CommandResult.SUCCESS;
    }
}
