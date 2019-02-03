package uk.co.netbans.supportbot.Commands.Music;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import uk.co.netbans.supportbot.CommandFramework.Command;
import uk.co.netbans.supportbot.CommandFramework.CommandArgs;
import uk.co.netbans.supportbot.CommandFramework.CommandCategory;
import uk.co.netbans.supportbot.CommandFramework.CommandResult;
import uk.co.netbans.supportbot.Music.MusicManager;
import uk.co.netbans.supportbot.NetBansBot;

public class Panic {

    @Command(name = "panic", displayName = "panic!", permission = "supportbot.command.music.panic", category = CommandCategory.MUSIC, aliases = "panic!")
    public CommandResult onPanic(CommandArgs args) {
        NetBansBot bot = args.getBot();
        Member member = args.getMember();
        TextChannel channel = (TextChannel) args.getChannel();
        MusicManager musicPlayer = bot.getMusicManager();
        if (musicPlayer.canBotPlayMusic(member, channel, bot)) {
            musicPlayer.loadTrack("https://www.youtube.com/playlist?list=PL0eheHgLSuZDVZ6ac_NkGomG0YXezgUHf", member, channel, true);
        }
        return CommandResult.SUCCESS;
    }
}
