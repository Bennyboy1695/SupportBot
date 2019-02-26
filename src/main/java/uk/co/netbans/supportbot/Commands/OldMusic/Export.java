package uk.co.netbans.supportbot.Commands.OldMusic;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import uk.co.netbans.supportbot.CommandFramework.Command;
import uk.co.netbans.supportbot.CommandFramework.CommandArgs;
import uk.co.netbans.supportbot.CommandFramework.CommandCategory;
import uk.co.netbans.supportbot.CommandFramework.CommandResult;
import uk.co.netbans.supportbot.Message.Messenger;
import uk.co.netbans.supportbot.OldMusic.MusicManager;
import uk.co.netbans.supportbot.OldMusic.TrackInfo;
import uk.co.netbans.supportbot.NetBansBot;

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;

public class Export {

    @Command(name = "export", displayName = "export", category = CommandCategory.MUSIC)
    public CommandResult onExport(CommandArgs commandArgs) {
        NetBansBot bot = commandArgs.getBot();
        Member member = commandArgs.getMember();
        TextChannel channel = (TextChannel) commandArgs.getChannel();
        MusicManager musicPlayer = bot.getMusicManager();
        String[] args = commandArgs.getArgs();
        String hastebin = "https://hastebin.com/";
        String hasteLink = "";
        String youtubeStart = "https://www.youtube.com/watch?v=";
        JsonArray jsonArray = new JsonArray();
        int count = 0;

        if (!musicPlayer.getTrackManager(member.getGuild()).getQueuedTracks().isEmpty()) {
            for (TrackInfo tracks : musicPlayer.getTrackManager(member.getGuild()).getQueuedTracks()) {
                jsonArray.add(youtubeStart + tracks.getTrack().getIdentifier());
                count++;
            }
            channel.sendTyping().queue();
            if (count == musicPlayer.getTrackManager(member.getGuild()).getQueuedTracks().size()) {
                try {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.add("links", jsonArray);
                    if (!Files.exists(bot.getMusicDirectory().resolve(member.getEffectiveName() + "-export.json"))) {
                        Files.createFile(bot.getMusicDirectory().resolve(member.getEffectiveName() + "-export.json"));
                    }
                    Path export = bot.getMusicDirectory().resolve(member.getEffectiveName() + "-export.json");
                    try (BufferedWriter writer = Files.newBufferedWriter(export)) {
                        writer.write(jsonObject.toString());
                        writer.flush();
                    }
                    //Hastebin.paste(jsonObject.toJSONString());
                    bot.getMessenger().sendEmbed(channel, Messenger.HASTEBIN, 10);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
        return CommandResult.SUCCESS;
    }
}
