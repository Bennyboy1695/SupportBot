package uk.co.netbans.supportbot.Music.Command;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import org.json.JSONObject;
import uk.co.netbans.supportbot.Message.Messenger;
import uk.co.netbans.supportbot.Support.Command.Command;
import uk.co.netbans.supportbot.BenCMDFramework.CommandResult;
import uk.co.netbans.supportbot.Music.TrackInfo;
import uk.co.netbans.supportbot.Music.MusicManager;
import uk.co.netbans.supportbot.NetBansBot;

import java.util.Set;

public class Queue implements Command {
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

        if (!music.hasPlayer(channel.getGuild()) || music.getTrackManager(channel.getGuild()).getQueuedTracks().isEmpty()) {
            bot.getMessenger().sendMessage(channel, "The music queue is empty!", 10);
            return CommandResult.SUCCESS;
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
        return CommandResult.SUCCESS;
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
        return "queue";
    }

    @Override
    public String[] aliases() {
        return new String[]{"q"};
    }
}
