package uk.co.netbans.supportbot.oldmusic;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import uk.co.netbans.supportbot.music.AudioPlayerSendHandler;
import uk.co.netbans.supportbot.NetBansBot;
import uk.co.netbans.supportbot.utils.Util;

import java.util.*;

public class MusicManager extends ListenerAdapter {
    public final String CD = "\uD83D\uDCBF";
    public final String DVD = "\uD83D\uDCC0";
    public final String MIC = "\uD83C\uDFA4 **|>** ";

    public final String QUEUE_TITLE = "__%s has added %d new track%s to the Queue:__";
    public final String QUEUE_DESCRIPTION = "%s **|>**  %s\n%s\n%s %s\n%s";
    public final String QUEUE_INFO = "Info about the Queue: (Size - %d)";
    public final String ERROR = "Error while loading \"%s\"";

    private final AudioPlayerManager myManager = new DefaultAudioPlayerManager();
    private final Map<String, Map.Entry<AudioPlayer, TrackManager>> players = new HashMap<>();
    private NetBansBot bot;

    public MusicManager(NetBansBot bot) {
        this.bot = bot;
        AudioSourceManagers.registerRemoteSources(myManager);
    }

    @Override
    public void onGuildVoiceLeave(GuildVoiceLeaveEvent event) {
        if (!players.containsKey(event.getGuild().getId()))
            return; //Guild doesn't have a music player

        TrackManager manager = getTrackManager(event.getGuild());
        manager.getQueuedTracks().stream()
                .filter(info -> !info.getTrack().equals(getPlayer(event.getGuild()).getPlayingTrack())
                        && info.getAuthor().getUser().equals(event.getMember().getUser()))
                .forEach(manager::remove);
    }

    @Override
    public void onGuildLeave(GuildLeaveEvent event) {
        stop(event.getGuild());
    }

    public boolean hasPlayer(Guild guild) {
        return players.containsKey(guild.getId());
    }

    public AudioPlayer getPlayer(Guild guild) {
        AudioPlayer p;
        if (hasPlayer(guild)) {
            p = players.get(guild.getId()).getKey();
        } else {
            p = createPlayer(guild);
        }
        return p;
    }

    public TrackManager getTrackManager(Guild guild) {
        return players.get(guild.getId()).getValue();
    }

    public AudioPlayer createPlayer(Guild guild) {
        AudioPlayer nPlayer = myManager.createPlayer();
        TrackManager manager = new TrackManager(nPlayer);
        nPlayer.addListener(manager);
        guild.getAudioManager().setSendingHandler(new AudioPlayerSendHandler(nPlayer));
        players.put(guild.getId(), new AbstractMap.SimpleEntry<>(nPlayer, manager));
        return nPlayer;
    }

    public boolean canBotPlayMusic(Member sender, TextChannel channel, NetBansBot bot) {
        if (sender.getVoiceState().getChannel() == null) {
            //bot.getMessenger().sendEmbed(channel, Messenger.NOT_VOICE, 10);
            return false;
        }
        Member botMember = bot.getJDA().getGuildById(bot.getGuildID()).getMemberById(bot.getJDA().asBot().getApplicationInfo().complete().getIdLong());
        if (botMember.getVoiceState().inVoiceChannel() && !sender.getVoiceState().getChannel().getMembers().contains(bot.getJDA().getGuildById(bot.getGuildID()).getMemberById(bot.getJDA().asBot().getApplicationInfo().complete().getIdLong()))) {
            //bot.getMessenger().sendEmbed(channel, Messenger.NOT_SAME_VOICE, 10);
            return false;
        }
        return true;
    }

    public void stop(Guild guild) {
        players.remove(guild.getId());
        getPlayer(guild).destroy();
        getTrackManager(guild).purgeQueue();
        guild.getAudioManager().setAutoReconnect(false);
        guild.getAudioManager().closeAudioConnection();
    }

    public void loadTrack(String identifier, Member author, TextChannel channel, boolean shuffle) {
        if (author.getVoiceState().getChannel() == null) {
            bot.getMessenger().sendMessage(channel, "You must be in a voice channel to summon me :D");
            return;
        }

        Guild guild = author.getGuild();
        getPlayer(guild); // Make sure this guild has a player.

        channel.sendTyping().queue(); //todo this is interesting
        myManager.loadItemOrdered(guild, identifier, new AudioLoadResultHandler() {

            @Override
            public void trackLoaded(AudioTrack track) {
                sendEmbed(channel, String.format(QUEUE_TITLE, Util.userDiscrimSet(author.getUser()), 1, ""),
                        String.format(QUEUE_DESCRIPTION, CD, getOrNull(track.getInfo().title), "", MIC, getOrNull(track.getInfo().author), ""));
                getTrackManager(guild).queue(track, author);
                getTrackManager(guild).setShuffle(shuffle);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                if (playlist.getSelectedTrack() != null) {
                    trackLoaded(playlist.getSelectedTrack());
                } else if (playlist.isSearchResult()) {
                    trackLoaded(playlist.getTracks().get(0));
                } else {
                    sendEmbed(channel, String.format(QUEUE_TITLE, Util.userDiscrimSet(author.getUser()), Math.min(playlist.getTracks().size(), 1000), "s"),
                            String.format(QUEUE_DESCRIPTION, DVD, getOrNull(playlist.getName()), "", "", "", ""));
                    for (int i = 0; i < Math.min(playlist.getTracks().size(), 1000); i++) {
                        getTrackManager(guild).queue(playlist.getTracks().get(i), author);
                        getTrackManager(guild).setShuffle(shuffle);
                    }
                }
            }

            @Override
            public void noMatches() {
                sendEmbed(channel, String.format(ERROR, identifier), "\u26A0 No playable tracks were found.");
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                sendEmbed(channel, String.format(ERROR, identifier), "\u26D4 " + exception.getLocalizedMessage());
            }
        });
    }

    public boolean isIdle(Guild guild) {
        return !hasPlayer(guild) || getPlayer(guild).getPlayingTrack() == null;
    }

    public void skipTrack(Guild guild, TextChannel channel) {
        getPlayer(guild).stopTrack();
    }

    public void setVolume(Guild guild, TextChannel channel, int volume) {
        getPlayer(guild).setVolume(volume);
        bot.getMessenger().sendMessage(channel, "Set volume to " + volume + "!");
    }

    public String buildQueueMessage(TrackInfo info) {
        AudioTrackInfo trackInfo = info.getTrack().getInfo();
        String title = trackInfo.title;
        long length = trackInfo.length;
        return "`[ " + getTimestamp(length) + " ]` " + title + "\n";
    }

    public String getTimestamp(long milis) {
        long seconds = milis / 1000;
        long hours = Math.floorDiv(seconds, 3600);
        seconds = seconds - (hours * 3600);
        long mins = Math.floorDiv(seconds, 60);
        seconds = seconds - (mins * 60);
        return (hours == 0 ? "" : hours + ":") + String.format("%02d", mins) + ":" + String.format("%02d", seconds);
    }

    public String getOrNull(String s) {
        return s.isEmpty() ? "N/A" : s;
    }

    public void sendEmbed(MessageChannel channel, String title, String description) {
        bot.getMessenger().sendEmbed((TextChannel) channel, new EmbedBuilder().setTitle(title, null).setDescription(description).build());
    }
}
