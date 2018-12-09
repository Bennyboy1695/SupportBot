package uk.co.netbans.discordbot.Music.Command;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import uk.co.netbans.discordbot.Message.Messenger;
import uk.co.netbans.discordbot.Support.Command.Command;
import uk.co.netbans.discordbot.Support.Command.CommandResult;
import uk.co.netbans.discordbot.Music.MusicManager;
import uk.co.netbans.discordbot.NetBansBot;
import uk.co.netbans.discordbot.Util;

import java.util.concurrent.TimeUnit;

public class Proximity implements Command {
    private NetBansBot bot;

    @Override
    public CommandResult onExecute(NetBansBot bot, Member sender, TextChannel channel, String label, String[] args) {
        this.bot = bot;
        if (sender.getVoiceState().getChannel() == null) {
            bot.getMessenger().sendEmbed(channel, Messenger.NOT_VOICE, 10);
            return CommandResult.TARGETNOTFOUND;
        }
        Member botMember = bot.getJDA().getGuildById(Long.valueOf((String)bot.getConf().get("guildID"))).getMemberById(bot.getJDA().asBot().getApplicationInfo().complete().getIdLong());
        if (botMember.getVoiceState().inVoiceChannel() && !sender.getVoiceState().getChannel().getMembers().contains(bot.getJDA().getGuildById(Long.valueOf((String)bot.getConf().get("guildID"))).getMemberById(bot.getJDA().asBot().getApplicationInfo().complete().getIdLong()))) {
            bot.getMessenger().sendEmbed(channel, Messenger.NOT_SAME_VOICE, 10);
            return CommandResult.TARGETNOTFOUND;
        }

        MusicManager music = bot.getMusicManager();

        try {
            music.loadTrack("https://www.youtube.com/playlist?list=PLZb6kNIh9TrHokGyJZrvJEhMpO78UMiQM", sender, channel);
            TimeUnit.SECONDS.sleep(5);
            music.getTrackManager(channel.getGuild()).shuffleQueue();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return CommandResult.SUCCESS;
    }

    @Override
    public String name() {
        return "proximity";
    }

    @Override
    public String desc() {
        return "Add the proximity playlist and shuffle the queue.";
    }

    @Override
    public String usage() {
        return bot.getCommandPrefix() + "proximity";
    }

    @Override
    public String[] aliases() {
        return new String[]{"pp"};
    }
}
