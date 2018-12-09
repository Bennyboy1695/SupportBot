package uk.co.netbans.discordbot.Music.Command;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import uk.co.netbans.discordbot.Support.Command.Command;
import uk.co.netbans.discordbot.Support.Command.CommandResult;
import uk.co.netbans.discordbot.NetBansBot;

public class Play implements Command {
    private NetBansBot bot;

    @Override
    public CommandResult onExecute(NetBansBot bot, Member sender, TextChannel channel, String label, String[] args) {
        this.bot = bot;
        if (args.length == 0)
            return CommandResult.INVALIDARGS;
        String input = args[0];
        bot.getMusicManager().loadTrack(input, sender, channel);
        return CommandResult.SUCCESS;
    }

    @Override
    public String name() {
        return "play";
    }

    @Override
    public String desc() {
        return "Play a song or playlist!";
    }

    @Override
    public String usage() {
        return bot.getCommandPrefix() + "play <url>";
    }

    @Override
    public String[] aliases() {
        return new String[0];
    }
}
