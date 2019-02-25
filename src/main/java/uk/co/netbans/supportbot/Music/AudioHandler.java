package uk.co.netbans.supportbot.Music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.audio.AudioSendHandler;
import net.dv8tion.jda.core.entities.Guild;

import java.util.LinkedList;
import java.util.List;

public class AudioHandler extends AudioEventAdapter implements AudioSendHandler {
    private final AudioPlayerHandler handler;
    private final Guild guild;
    private final AudioPlayer player;

    private final LinkedList<AudioTrack> queue = new LinkedList<>();
    private AudioFrame lastFrame;

    AudioHandler(AudioPlayerHandler handler, Guild guild, AudioPlayer player) {
        this.handler = handler;
        this.guild = guild;
        this.player = player;
    }

    public int addTrackFirst(AudioTrack track) {
        if (player.getPlayingTrack() == null) {
            player.playTrack(track);
            return -1;
        } else {
            queue.addFirst(track);
            return 0;
        }
    }

    public int addTrack(AudioTrack track) {
        if (player.getPlayingTrack() == null) {
            player.playTrack(track);
            return -1;
        } else {
            queue.add(track);
            return queue.size() - 1;
        }
    }

    public LinkedList<AudioTrack> getQueue() {
        return queue;
    }

    public void stop() {
        queue.clear();
        player.stopTrack();
    }

    public boolean isPlaying() {
        return guild.getSelfMember().getVoiceState().inVoiceChannel() && player.getPlayingTrack() != null;
    }

    public AudioPlayer getPlayer() {
        return player;
    }

    public long getRequester() {
        if (player.getPlayingTrack() == null || player.getPlayingTrack().getUserData(Long.class) == null)
            return 0;
        return player.getPlayingTrack().getUserData(Long.class);
    }

    // Handler Methods
    @Override
    public boolean canProvide() {
        if (lastFrame == null)
            lastFrame = player.provide();
        return lastFrame != null;
    }

    @Override
    public byte[] provide20MsAudio() {
        if (lastFrame == null)
            lastFrame = player.provide();
        byte[] data = lastFrame != null ? lastFrame.getData() : null;
        lastFrame = null;
        return data;
    }

    @Override
    public boolean isOpus() {
        return true;
    }

    // Events
    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (queue.isEmpty()) {
            System.out.println("I NEED TO LEAVE THE CHANNEL HERE BUT I DONT REMEMBER HOW TO DO THAT"); //todo leave channel
        } else {
            player.playTrack(queue.getFirst());
            queue.removeFirst();
        }
    }

    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {
        System.out.println("UPDATE NOW PLAYING THING!");
    }

    // todo now playing stuffs
}
