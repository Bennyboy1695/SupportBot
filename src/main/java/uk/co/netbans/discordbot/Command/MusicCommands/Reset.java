package uk.co.netbans.discordbot.Command.MusicCommands;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import uk.co.netbans.discordbot.Command.Command;
import uk.co.netbans.discordbot.Command.CommandCode;
import uk.co.netbans.discordbot.NetBansBot;

public class Reset implements Command {
    @Override
    public CommandCode onExecute(NetBansBot bot, Member sender, TextChannel channel, String label, String[] args) {
        bot.getMusicManager().reset(channel.getGuild());
        bot.getMessenger().sendMessage(channel, "\uD83D\uDD04 Resetting the music player..", 10);
        return CommandCode.OK;
    }

    @Override
    public String name() {
        return "reset";
    }

    @Override
    public String desc() {
        return "Reset the music player.";
    }

    @Override
    public String usage() {
        return "!reset";
    }

    @Override
    public String[] aliases() {
        return new String[0];
    }
}
