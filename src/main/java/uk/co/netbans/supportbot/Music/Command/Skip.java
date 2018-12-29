package uk.co.netbans.supportbot.Music.Command;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import uk.co.netbans.supportbot.Message.Messenger;
import uk.co.netbans.supportbot.Support.Command.Command;
import uk.co.netbans.supportbot.BenCMDFramework.CommandResult;
import uk.co.netbans.supportbot.NetBansBot;

public class Skip implements Command {
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

        bot.getMusicManager().skipTrack(sender.getGuild(), channel);
        bot.getMessenger().sendMessage(channel, "\u23E9 Skipping current track.", 10);
        return CommandResult.SUCCESS;
    }

    @Override
    public String name() {
        return "skip";
    }

    @Override
    public String desc() {
        return "Skip the current song.";
    }

    @Override
    public String usage() {
        return "skip";
    }

    @Override
    public String[] aliases() {
        return new String[0];
    }
}
