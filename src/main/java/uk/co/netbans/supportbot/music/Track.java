package uk.co.netbans.supportbot.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.entities.Member;

public class Track {
    private final AudioTrack track;
    private final Member owner;

    public Track(AudioTrack track, Member owner) {
        this.track = track;
        this.owner = owner;
    }

    public AudioTrack getTrack() {
        return track;
    }

    public Member getOwner() {
        return owner;
    }
}
