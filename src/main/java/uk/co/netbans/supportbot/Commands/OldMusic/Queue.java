package uk.co.netbans.supportbot.Commands.OldMusic;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import org.json.JSONObject;
import uk.co.netbans.supportbot.CommandFramework.Command;
import uk.co.netbans.supportbot.CommandFramework.CommandArgs;
import uk.co.netbans.supportbot.CommandFramework.CommandCategory;
import uk.co.netbans.supportbot.CommandFramework.CommandResult;
import uk.co.netbans.supportbot.OldMusic.TrackInfo;
import uk.co.netbans.supportbot.OldMusic.MusicManager;
import uk.co.netbans.supportbot.NetBansBot;

import java.util.Set;

public class Queue {

    @Command(name = "queue", displayName = "queue", permission = "supportbot.command.music.queue", category = CommandCategory.MUSIC)
    public CommandResult onQueue(CommandArgs commandArgs) {
        NetBansBot bot = commandArgs.getBot();
        Member member = commandArgs.getMember();
        TextChannel channel = (TextChannel) commandArgs.getChannel();
        MusicManager musicPlayer = bot.getMusicManager();
        String[] args = commandArgs.getArgs();

        if (!musicPlayer.hasPlayer(channel.getGuild()) || musicPlayer.getTrackManager(channel.getGuild()).getQueuedTracks().isEmpty()) {
            bot.getMessenger().sendMessage(channel, "The music queue is empty!", 10);
            return CommandResult.SUCCESS;
        }
        StringBuilder sb = new StringBuilder();
        Set<TrackInfo> queue = musicPlayer.getTrackManager(channel.getGuild()).getQueuedTracks();
        queue.forEach(trackInfo -> sb.append(musicPlayer.buildQueueMessage(trackInfo)));
        String embedTitle = String.format(musicPlayer.QUEUE_INFO, queue.size());

        if (sb.length() <= 1960) {
            musicPlayer.sendEmbed(channel, embedTitle, "**>** " + sb.toString());
        } else /* if (sb.length() <= 20000) */ {
            try {
                sb.setLength(sb.length() - 1);
                HttpResponse response = Unirest.post("https://hastebin.com/documents").body(sb.toString()).asString();
                musicPlayer.sendEmbed(channel, embedTitle, "[Click here for a detailed list](https://hastebin.com/"
                        + new JSONObject(response.getBody().toString()).getString("key") + ")");
            } catch (UnirestException ex) {
                ex.printStackTrace();
            }
        }
        return CommandResult.SUCCESS;
    }
}
