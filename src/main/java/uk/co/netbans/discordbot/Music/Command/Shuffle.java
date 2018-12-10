package uk.co.netbans.discordbot.Music.Command;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import uk.co.netbans.discordbot.Message.Messenger;
import uk.co.netbans.discordbot.Support.Command.Command;
import uk.co.netbans.discordbot.Support.Command.CommandResult;
import uk.co.netbans.discordbot.Music.MusicManager;
import uk.co.netbans.discordbot.NetBansBot;
import uk.co.netbans.discordbot.Util;

public class Shuffle implements Command {
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

        if (music.isIdle(channel.getGuild()))
            return CommandResult.SUCCESS;

        music.getTrackManager(channel.getGuild()).shuffleQueue();
        bot.getMessenger().sendMessage(channel, "\u2705 Shuffled the queue!", 10);
        return CommandResult.SUCCESS;
    }

    @Override
    public String name() {
        return "shuffle";
    }

    @Override
    public String desc() {
        return "Shuffle the current play queue.";
    }

    @Override
    public String usage() {
        return "shuffle";
    }

    @Override
    public String[] aliases() {
        return new String[0];
    }
}
