package uk.co.netbans.discordbot.Command.MusicCommands;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import uk.co.netbans.discordbot.Command.Command;
import uk.co.netbans.discordbot.Command.CommandResult;
import uk.co.netbans.discordbot.NetBansBot;

public class Play implements Command {
    @Override
    public CommandResult onExecute(NetBansBot bot, Member sender, TextChannel channel, String label, String[] args) {
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
        return "!play <url>";
    }

    @Override
    public String[] aliases() {
        return new String[0];
    }
}
