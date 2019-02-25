package uk.co.netbans.supportbot.OldMusic;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.VoiceChannel;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

public class TrackManager extends AudioEventAdapter {
    private final AudioPlayer player;
    private final Queue<TrackInfo> queue;
    private boolean shuffle = false;
    private boolean loop = false;

    public TrackManager(AudioPlayer player) {
        this.player = player;
        this.queue = new LinkedBlockingQueue<>();
    }

    public void queue(AudioTrack track, Member author) {
        TrackInfo info = new TrackInfo(track, author);
        queue.add(info);

        if (player.getPlayingTrack() == null) {
            player.playTrack(track);
        }
    }

    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {
        TrackInfo info = queue.element();
        VoiceChannel vChan = info.getAuthor().getVoiceState().getChannel();
        if (vChan == null) { // User has left all voice channels
            player.stopTrack();
        } else {
            info.getAuthor().getGuild().getAudioManager().openAudioConnection(vChan);
        }
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        Guild g = queue.poll().getAuthor().getGuild();
        if (queue.isEmpty()) {
            g.getAudioManager().closeAudioConnection();
        } else {
            TrackInfo info = queue.element();
            if (info.getAuthor().getVoiceState().getChannel().getMembers().size() == 1) {
                g.getAudioManager().closeAudioConnection();
            }
            if (shuffle) {
                player.playTrack(shuffle().getTrack());
            } else if (loop) {
                player.playTrack(track);
            } else {
                player.playTrack(queue.element().getTrack());
            }
        }
    }

    public TrackInfo shuffle() {
        ArrayList<TrackInfo> tracks = new ArrayList<>(getQueuedTracks());
        int random = ThreadLocalRandom.current().nextInt(getQueuedTracks().size());
        return tracks.get(random);
    }

    public Set<TrackInfo> getQueuedTracks() {
        return new LinkedHashSet<>(queue);
    }

    public void purgeQueue() {
        queue.clear();
    }

    public void remove(TrackInfo entry) {
        queue.remove(entry);
    }

    public TrackInfo getTrackInfo(AudioTrack track) {
        return queue.stream().filter(trackInfo -> trackInfo.getTrack().equals(track)).findFirst().orElse(null);
    }

    public void setShuffle(boolean shuffle) {
        this.shuffle = shuffle;
    }

    public boolean isLoop() {
        return loop;
    }

    public boolean isShuffle() {
        return shuffle;
    }

    public void setLoop(boolean loop) {
        this.loop = loop;
    }
}
