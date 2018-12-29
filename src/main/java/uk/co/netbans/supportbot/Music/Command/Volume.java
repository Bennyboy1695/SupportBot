package uk.co.netbans.supportbot.Music.Command;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import uk.co.netbans.supportbot.Message.Messenger;
import uk.co.netbans.supportbot.Support.Command.Command;
import uk.co.netbans.supportbot.BenCMDFramework.CommandResult;
import uk.co.netbans.supportbot.NetBansBot;

public class Volume implements Command {
    private NetBansBot bot;

    @Override
    public CommandResult onExecute(NetBansBot bot, Member sender, TextChannel channel, String label, String[] args) {
        this.bot = bot;
        if (args.length == 0)
            return CommandResult.INVALIDARGS;
        if (sender.getVoiceState().getChannel() == null) {
            bot.getMessenger().sendEmbed(channel, Messenger.NOT_VOICE, 10);
            return CommandResult.TARGETNOTFOUND;
        }
        Member botMember = bot.getJDA().getGuildById(Long.valueOf((String)bot.getConf().get("guildID"))).getMemberById(bot.getJDA().asBot().getApplicationInfo().complete().getIdLong());
        if (botMember.getVoiceState().inVoiceChannel() && !sender.getVoiceState().getChannel().getMembers().contains(bot.getJDA().getGuildById(Long.valueOf((String)bot.getConf().get("guildID"))).getMemberById(bot.getJDA().asBot().getApplicationInfo().complete().getIdLong()))) {
            bot.getMessenger().sendEmbed(channel, Messenger.NOT_SAME_VOICE, 10);
            return CommandResult.TARGETNOTFOUND;
        }

        try {
            int vol = Integer.valueOf(args[0]);
            if (vol > 100) {
            channel.sendMessage("\uD83D\uDEAB" + " Max Volume is 100!").complete();
            } else {
                bot.getMusicManager().setVolume(channel.getGuild(), channel, vol);
            }
            return CommandResult.SUCCESS;
        } catch (NumberFormatException e) {
            return CommandResult.INVALIDARGS;
        }
    }

    @Override
    public String name() {
        return "volume";
    }

    @Override
    public String desc() {
        return "Change the volume of the audio player!";
    }

    @Override
    public String usage() {
        return "volume 1";
    }

    @Override
    public String[] aliases() {
        return new String[]{"vol"};
    }
}
