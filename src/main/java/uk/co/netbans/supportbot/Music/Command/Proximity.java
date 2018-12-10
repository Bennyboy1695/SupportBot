package uk.co.netbans.supportbot.Music.Command;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import uk.co.netbans.supportbot.Message.Messenger;
import uk.co.netbans.supportbot.Support.Command.Command;
import uk.co.netbans.supportbot.Support.Command.CommandResult;
import uk.co.netbans.supportbot.Music.MusicManager;
import uk.co.netbans.supportbot.NetBansBot;

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
            music.loadTrack("https://www.youtube.com/playlist?list=PL3osQJLUr9gL42vxYKssd62eng5MV_1WM", sender, channel);
            TimeUnit.SECONDS.sleep(10);
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
        return ("proximity");
    }

    @Override
    public String[] aliases() {
        return new String[]{"pp"};
    }
}
