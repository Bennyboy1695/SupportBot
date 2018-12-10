package uk.co.netbans.discordbot.Music.Command;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import uk.co.netbans.discordbot.Message.Messenger;
import uk.co.netbans.discordbot.Support.Command.Command;
import uk.co.netbans.discordbot.Support.Command.CommandResult;
import uk.co.netbans.discordbot.Music.MusicManager;
import uk.co.netbans.discordbot.NetBansBot;
import uk.co.netbans.discordbot.Util;

public class Current implements Command {
    private NetBansBot bot;

    @Override
    public CommandResult onExecute(NetBansBot bot, Member sender, TextChannel channel, String label, String[] args) {
        this.bot = bot;
        MusicManager music = bot.getMusicManager();

        if (sender.getVoiceState().getChannel() == null) {
            bot.getMessenger().sendEmbed(channel, Messenger.NOT_VOICE, 10);
            return CommandResult.TARGETNOTFOUND;
        }
        Member botMember = bot.getJDA().getGuildById(Long.valueOf((String)bot.getConf().get("guildID"))).getMemberById(bot.getJDA().asBot().getApplicationInfo().complete().getIdLong());
        if (botMember.getVoiceState().inVoiceChannel() && !sender.getVoiceState().getChannel().getMembers().contains(bot.getJDA().getGuildById(Long.valueOf((String)bot.getConf().get("guildID"))).getMemberById(bot.getJDA().asBot().getApplicationInfo().complete().getIdLong()))) {
            bot.getMessenger().sendEmbed(channel, Messenger.NOT_SAME_VOICE, 10);
            return CommandResult.TARGETNOTFOUND;
        }

        if (!music.hasPlayer(channel.getGuild()) || music.getPlayer(channel.getGuild()).getPlayingTrack() == null) { // No song is playing
            bot.getMessenger().sendMessage(channel, "No song is being played at the moment! *It's your time to shine..*", 10);
        } else {
            AudioTrack track = music.getPlayer(channel.getGuild()).getPlayingTrack();
            music.sendEmbed(channel, "Track Info", String.format(music.QUEUE_DESCRIPTION, music.CD, music.getOrNull(track.getInfo().title),
                    "\n\u23F1 **|>** `[ " + music.getTimestamp(track.getPosition()) + " / " + music.getTimestamp(track.getInfo().length) + " ]`",
                    "\n" + music.MIC, music.getOrNull(track.getInfo().author),
                    "\n\uD83C\uDFA7 **|>**  " + Util.userDiscrimSet(music.getTrackManager(channel.getGuild()).getTrackInfo(track).getAuthor().getUser())));
        }
        return CommandResult.SUCCESS;
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
        return "current";
    }

    @Override
    public String[] aliases() {
        return new String[]{"curr", "nowplaying", "info"};
    }
}
