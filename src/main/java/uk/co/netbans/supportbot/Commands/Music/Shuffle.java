package uk.co.netbans.supportbot.Commands.Music;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import uk.co.netbans.supportbot.CommandFramework.Command;
import uk.co.netbans.supportbot.CommandFramework.CommandArgs;
import uk.co.netbans.supportbot.CommandFramework.CommandCategory;
import uk.co.netbans.supportbot.CommandFramework.CommandResult;
import uk.co.netbans.supportbot.Music.MusicManager;
import uk.co.netbans.supportbot.NetBansBot;

public class Shuffle {

    @Command(name = "shuffle", displayName = "shuffle", permission = "supportbot.command.music.shuffle", category = CommandCategory.MUSIC)
    public CommandResult onShuffle(CommandArgs commandArgs) {
        NetBansBot bot = commandArgs.getBot();
        Member member = commandArgs.getMember();
        TextChannel channel = (TextChannel) commandArgs.getChannel();
        MusicManager musicPlayer = bot.getMusicManager();
        String[] args = commandArgs.getArgs();

        if (musicPlayer.isIdle(channel.getGuild()))
            return CommandResult.SUCCESS;
        if (musicPlayer.canBotPlayMusic(member, channel, bot)) {
            musicPlayer.getTrackManager(channel.getGuild()).shuffleQueue();
            bot.getMessenger().sendMessage(channel, "\u2705 Shuffled the queue!", 10);
        }
        return CommandResult.SUCCESS;
    }

}
