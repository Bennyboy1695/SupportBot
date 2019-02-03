package uk.co.netbans.supportbot.Commands.Music;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import uk.co.netbans.supportbot.CommandFramework.Command;
import uk.co.netbans.supportbot.CommandFramework.CommandArgs;
import uk.co.netbans.supportbot.CommandFramework.CommandCategory;
import uk.co.netbans.supportbot.Music.MusicManager;
import uk.co.netbans.supportbot.CommandFramework.CommandResult;
import uk.co.netbans.supportbot.NetBansBot;

public class Reset {

    @Command(name = "stop", displayName = "stop", permission = "supportbot.command.music.stop", category = CommandCategory.MUSIC)
    public CommandResult onStop(CommandArgs commandArgs) {
        NetBansBot bot = commandArgs.getBot();
        Member member = commandArgs.getMember();
        TextChannel channel = (TextChannel) commandArgs.getChannel();
        MusicManager musicPlayer = bot.getMusicManager();
        String[] args = commandArgs.getArgs();
        if (musicPlayer.canBotPlayMusic(member, channel, bot)) {
            bot.getMusicManager().stop(channel.getGuild());
            bot.getMessenger().sendMessage(channel, "\uD83D\uDD04 Resetting the music player..", 10);
        }
        return CommandResult.SUCCESS;
    }

}
