package uk.co.netbans.supportbot.Commands.Music;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import uk.co.netbans.supportbot.CommandFramework.Command;
import uk.co.netbans.supportbot.CommandFramework.CommandArgs;
import uk.co.netbans.supportbot.CommandFramework.CommandCategory;
import uk.co.netbans.supportbot.Music.MusicManager;
import uk.co.netbans.supportbot.CommandFramework.CommandResult;
import uk.co.netbans.supportbot.NetBansBot;

public class Skip {

    @Command(name = "skip", displayName = "skip", permission = "supportbot.command.music.skip", category = CommandCategory.MUSIC)
    public CommandResult onSkip(CommandArgs commandArgs) {
        NetBansBot bot = commandArgs.getBot();
        Member member = commandArgs.getMember();
        TextChannel channel = (TextChannel) commandArgs.getChannel();
        MusicManager musicPlayer = bot.getMusicManager();
        String[] args = commandArgs.getArgs();
        if (musicPlayer.canBotPlayMusic(member, channel, bot)) {
            bot.getMusicManager().skipTrack(member.getGuild(), channel);
            bot.getMessenger().sendMessage(channel, "\u23E9 Skipping current track.", 10);
        }
        return CommandResult.SUCCESS;
    }

}
