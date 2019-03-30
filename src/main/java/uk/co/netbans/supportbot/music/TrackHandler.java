package uk.co.netbans.supportbot.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.VoiceChannel;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

public class TrackHandler extends AudioEventAdapter {
    private final AudioPlayer player;
    private final Queue<Track> queue;
    private boolean shuffle = false;
    private boolean loop = false;

    private VoiceChannel liveChannel = null;
    private Track current = null;

    public TrackHandler(AudioPlayer player) {
        this.player = player;
        this.queue = new LinkedBlockingQueue<>();
    }

    public void queue(AudioTrack track, Member owner) {
        Track added = new Track(track, owner);
        queue.add(added);
        if (player.getPlayingTrack() == null)
            player.playTrack(track);
    }

    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {
        Track t = queue.element();
        current = t;
        VoiceChannel channel = t.getOwner().getVoiceState().getChannel();
        if (liveChannel == null && channel != null) {
            t.getOwner().getGuild().getAudioManager().openAudioConnection(channel);
            liveChannel = channel;
        } else if (liveChannel != null && liveChannel.getMembers().size() > 1) {
            t.getOwner().getGuild().getAudioManager().openAudioConnection(liveChannel);
        } else {
            player.stopTrack();
            if (liveChannel != null)
                liveChannel = null;
        }
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        Guild guild = queue.poll().getOwner().getGuild(); //todo this line is sus as fuck
        if (queue.isEmpty())
            guild.getAudioManager().closeAudioConnection();
        else {
            if (liveChannel == null || liveChannel.getMembers().size() == 1) {
                guild.getAudioManager().closeAudioConnection();
                current = null;
            } if (shuffle) {
                Track t = getRandomTrack();
//                current = t;
                player.playTrack(t.getTrack());
            } else if (loop)
                player.playTrack(track);
            else {
                Track t = queue.element();
//                current = t;
                player.playTrack(t.getTrack());
            }
        }
    }

    private Track getRandomTrack() {
        return new ArrayList<>(getQueuedTracks()).get(ThreadLocalRandom.current().nextInt(getQueuedTracks().size()));
    }

    public void clearQueue() {
        queue.clear();
    }

    public void removeTrack(Track track) {
        queue.remove(track);
    }

    public Track getCurrentTrack() {
        return current;
    }

    public Set<Track> getQueuedTracks() {
        return new LinkedHashSet<>(queue);
    }

    public void setShuffle(boolean shuffle) {
        this.shuffle = shuffle;
    }
    public boolean isShuffle() {
        return shuffle;
    }

    public void setLoop(boolean loop) {
        this.loop = loop;
    }
    public boolean isLoop() {
        return loop;
    }
}
