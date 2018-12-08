package uk.co.netbans.discordbot.Music.Command;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import uk.co.netbans.discordbot.Support.Command.Command;
import uk.co.netbans.discordbot.Support.Command.CommandResult;
import uk.co.netbans.discordbot.NetBansBot;

public class Search implements Command {
    @Override
    public CommandResult onExecute(NetBansBot bot, Member sender, TextChannel channel, String label, String[] args) {
        if (args.length == 0)
            return CommandResult.INVALIDARGS;
        String input = "ytsearch: " + String.join(" ", args);
        bot.getMusicManager().loadTrack(input, sender, channel);
        return CommandResult.SUCCESS;
    }

    @Override
    public String name() {
        return "search";
    }

    @Override
    public String desc() {
        return "Search for a song on YouTube!";
    }

    @Override
    public String usage() {
        return "!search We Are Number One";
    }

    @Override
    public String[] aliases() {
        return new String[]{"s", "ytplay"};
    }
}
