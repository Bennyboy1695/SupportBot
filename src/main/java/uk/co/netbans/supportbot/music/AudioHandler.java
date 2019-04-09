package uk.co.netbans.supportbot.music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import me.bhop.bjdautilities.ReactionMenu;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import uk.co.netbans.supportbot.EmbedTemplates;
import uk.co.netbans.supportbot.SupportBot;
import uk.co.netbans.supportbot.utils.Pair;
import uk.co.netbans.supportbot.utils.Utils;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class AudioHandler extends ListenerAdapter {
    private final SupportBot bot;
    private final AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
    private final Map<Long, Pair<AudioPlayer, TrackHandler>> players = new HashMap<>();

    private ReactionMenu nowPlaying;

    public AudioHandler(SupportBot bot) {
        this.bot = bot;
        AudioSourceManagers.registerRemoteSources(playerManager);
    }

    @Override
    public void onGuildVoiceLeave(GuildVoiceLeaveEvent event) {
//        Pair<AudioPlayer, TrackHandler> playerHandler = players.get(event.getGuild().getIdLong());
//        if (playerHandler == null) return;
//        AudioPlayer player = playerHandler.getKey();
//        TrackHandler handler = playerHandler.getValue();
//
//        handler.getQueuedTracks().stream()
//                .filter(track ->
//                        !track.getTrack().equals(player.getPlayingTrack()) &&
//                        track.getOwner().getUser().equals(event.getMember().getUser()))
//                .forEach(handler::removeTrack);
        // todo the above removes tracks added by a user when they disconnect. That is probably unwanted behaviour
    }

    @Override
    public void onGuildLeave(GuildLeaveEvent event) {
        stop(event.getGuild());
    }

    public boolean hasPlayer(Guild guild) {
        return players.containsKey(guild.getIdLong());
    }
    public AudioPlayer getPlayer(Guild guild) {
        return hasPlayer(guild) ? players.get(guild.getIdLong()).getKey() : createPlayer(guild);
    }
    public AudioPlayer createPlayer(Guild guild) {
        AudioPlayer newPlayer = playerManager.createPlayer();
        TrackHandler newHandler = new TrackHandler(newPlayer);
        newPlayer.addListener(newHandler);
        guild.getAudioManager().setSendingHandler(new AudioPlayerSendHandler(newPlayer));
        players.put(guild.getIdLong(), new Pair<>(newPlayer, newHandler));
        return newPlayer;
    }

    public TrackHandler getTrackHandler(Guild guild) {
        return players.get(guild.getIdLong()).getValue();
    }

    public void stop(Guild guild) {
        players.remove(guild.getIdLong());
        getPlayer(guild).destroy();
        getTrackHandler(guild).clearQueue();
        guild.getAudioManager().setAutoReconnect(false);
        guild.getAudioManager().closeAudioConnection();
    }

    // Track Management Methods

    public void loadTrack(String id, Member author, TextChannel channel, boolean shuffle) {
        if (author.getVoiceState().getChannel() == null) {
            bot.getMessenger().sendEmbed(channel,
                    EmbedTemplates.ERROR.getEmbed().setDescription("You must be in a voice channel to play music!").build(),
                    10);
            return;
        }
        Guild guild = author.getGuild();
        getPlayer(guild);

        channel.sendTyping().queue();
        playerManager.loadItemOrdered(guild, id, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                bot.getMessenger().sendEmbed(channel,
                        EmbedTemplates.SUCCESS.getEmbed().setDescription("Successfully queued a new song... i need a better message haha").build(),
                        10);
                getTrackHandler(guild).queue(track, author);
                getTrackHandler(guild).setShuffle(shuffle);
//                createNowPlaying(channel);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                if (playlist.getSelectedTrack() != null) {
                    trackLoaded(playlist.getSelectedTrack());
                } else if (playlist.isSearchResult()) {
                    trackLoaded(playlist.getTracks().get(0));
                } else {
                    bot.getMessenger().sendEmbed(channel,
                            EmbedTemplates.SUCCESS.getEmbed().setDescription("Successfully queued a new playlist... i need a better message haha").build(),
                            10);
                    for (int i = 0; i < Math.min(playlist.getTracks().size(), 1000); i++) {
                        getTrackHandler(guild).queue(playlist.getTracks().get(i), author);
                        getTrackHandler(guild).setShuffle(shuffle);
                    }
                }
                createNowPlaying(channel);
            }

            @Override
            public void noMatches() {
                bot.getMessenger().sendEmbed(channel,
                        EmbedTemplates.ERROR.getEmbed().setDescription("No playable songs were found!").build(),
                        10);
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                bot.getMessenger().sendEmbed(channel,
                        EmbedTemplates.ERROR.getEmbed().setDescription("Failed to load song due to exception:\n" + exception.getMessage()).build(),
                        10);
            }
        });
    }

    public void skipTrack(Guild guild) {
        getPlayer(guild).stopTrack();
    }

    public void setVolume(Guild guild, int volume) {
        getPlayer(guild).setVolume(volume);
    }

    public boolean isIdle(Guild guild) {
        return !hasPlayer(guild) || getPlayer(guild).getPlayingTrack() == null;
    }

    private void createNowPlaying(TextChannel channel) {
        if (nowPlaying != null) {
            nowPlaying.destroy();
            nowPlaying = null;
        }
        nowPlaying = new ReactionMenu.Builder(channel.getJDA())
                .setEmbed(buildNowPlayingEmbed(channel))
                .onClick("\u23EC", menu -> createNowPlaying(channel))
                .onClick("\u23F9", menu -> {
                    menu.destroy();
                    stop(channel.getGuild());
                })
                .onClick("\u23E9", menu -> skipTrack(channel.getGuild()))
                .buildAndDisplay(channel);
        nowPlaying.getMessage().setEmbedRepeat(() -> buildNowPlayingEmbed(channel), 5);
    }

    private MessageEmbed buildNowPlayingEmbed(Channel channel) {
        EmbedBuilder embed = new EmbedBuilder();

        TrackHandler th = getTrackHandler(channel.getGuild());
        Track current = th.getCurrentTrack();
        if (current != null) {
            AudioTrackInfo info = current.getTrack().getInfo();
            embed.setAuthor("Now Playing \uD83C\uDFB5", null, channel.getJDA().getSelfUser().getAvatarUrl());

            embed.setTitle(info.title, info.uri);

            StringBuilder desc = new StringBuilder();
            desc.append("by ").append(info.author).append("\n");

            String bar = "\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC";
            int loc = (int) Math.round(bar.length() * ((double) current.getTrack().getPosition()/current.getTrack().getDuration()));
            desc.append(bar, 0, loc).append("\uD83D\uDD35").append(bar.substring(loc)).append("\n");

            desc.append("``").append(Utils.msToTimeString(current.getTrack().getPosition())).append(" / ").append(Utils.msToTimeString(current.getTrack().getDuration())).append("``\n");

            embed.setDescription(desc.toString().trim());
            embed.setTimestamp(Instant.now());
            embed.setFooter("Queued by " + current.getOwner().getEffectiveName(), current.getOwner().getUser().getAvatarUrl());
        } else
            embed.setDescription("No song is currently playing...");
        return embed.build();
    }
}
