package uk.co.netbans.supportbot.Commands.OldMusic;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import uk.co.netbans.supportbot.CommandFramework.Command;
import uk.co.netbans.supportbot.CommandFramework.CommandArgs;
import uk.co.netbans.supportbot.CommandFramework.CommandCategory;
import uk.co.netbans.supportbot.OldMusic.MusicManager;
import uk.co.netbans.supportbot.CommandFramework.CommandResult;
import uk.co.netbans.supportbot.NetBansBot;

public class Play {

    @Command(name = "play", displayName = "play", permission = "supportbot.command.music.play", category = CommandCategory.MUSIC)
    public CommandResult onPlay(CommandArgs commandArgs) {
        NetBansBot bot = commandArgs.getBot();
        Member member = commandArgs.getMember();
        TextChannel channel = (TextChannel) commandArgs.getChannel();
        MusicManager musicPlayer = bot.getMusicManager();
        String[] args = commandArgs.getArgs();
        if (args.length == 0)
            return CommandResult.INVALIDARGS;

        if (musicPlayer.canBotPlayMusic(member, channel, bot)) {
            String input = args[0];
            bot.getMusicManager().loadTrack(input, member, channel, false);
            return CommandResult.SUCCESS;
        }
        return CommandResult.SUCCESS;
    }
}
