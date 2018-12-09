package uk.co.netbans.discordbot.Music.Command;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import uk.co.netbans.discordbot.Support.Command.Command;
import uk.co.netbans.discordbot.Support.Command.CommandResult;
import uk.co.netbans.discordbot.NetBansBot;

public class Volume implements Command {
    private NetBansBot bot;

    @Override
    public CommandResult onExecute(NetBansBot bot, Member sender, TextChannel channel, String label, String[] args) {
        this.bot = bot;
        if (args.length == 0)
            return CommandResult.INVALIDARGS;
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
        return bot.getCommandPrefix() + "volume 1";
    }

    @Override
    public String[] aliases() {
        return new String[]{"vol"};
    }
}
