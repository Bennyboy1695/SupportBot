package uk.co.netbans.discordbot.Command.MusicCommands;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import org.json.JSONObject;
import uk.co.netbans.discordbot.Command.Command;
import uk.co.netbans.discordbot.Command.CommandCode;
import uk.co.netbans.discordbot.Music.TrackInfo;
import uk.co.netbans.discordbot.Music.MusicManager;
import uk.co.netbans.discordbot.NetBansBot;

import java.util.Set;

public class Queue implements Command {
    @Override
    public CommandCode onExecute(NetBansBot bot, Member sender, TextChannel channel, String label, String[] args) {
        MusicManager music = bot.getMusicManager();
        if (!music.hasPlayer(channel.getGuild()) || music.getTrackManager(channel.getGuild()).getQueuedTracks().isEmpty()) {
            bot.getMessenger().sendMessage(channel, "The music queue is empty!", 10);
            return CommandCode.OK;
        }
        StringBuilder sb = new StringBuilder();
        Set<TrackInfo> queue = music.getTrackManager(channel.getGuild()).getQueuedTracks();
        queue.forEach(trackInfo -> sb.append(music.buildQueueMessage(trackInfo)));
        String embedTitle = String.format(music.QUEUE_INFO, queue.size());

        if (sb.length() <= 1960) {
            music.sendEmbed(channel, embedTitle, "**>** " + sb.toString());
        } else /* if (sb.length() <= 20000) */ {
            try {
                sb.setLength(sb.length() - 1);
                HttpResponse response = Unirest.post("https://hastebin.com/documents").body(sb.toString()).asString();
                music.sendEmbed(channel, embedTitle, "[Click here for a detailed list](https://hastebin.com/"
                        + new JSONObject(response.getBody().toString()).getString("key") + ")");
            } catch (UnirestException ex) {
                ex.printStackTrace();
            }
        }
        return CommandCode.OK;
    }

    @Override
    public String name() {
        return "queue";
    }

    @Override
    public String desc() {
        return "View the current play queue.";
    }

    @Override
    public String usage() {
        return "!queue";
    }

    @Override
    public String[] aliases() {
        return new String[]{"q"};
    }
}
