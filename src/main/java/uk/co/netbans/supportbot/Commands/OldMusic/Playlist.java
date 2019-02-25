package uk.co.netbans.supportbot.Commands.OldMusic;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import uk.co.netbans.supportbot.CommandFramework.Command;
import uk.co.netbans.supportbot.CommandFramework.CommandArgs;
import uk.co.netbans.supportbot.CommandFramework.CommandCategory;
import uk.co.netbans.supportbot.CommandFramework.CommandResult;
import uk.co.netbans.supportbot.OldMusic.MusicManager;
import uk.co.netbans.supportbot.NetBansBot;

public class Playlist {

    @Command(name = "playlist", displayName = "playlist", permission = "supportbot.command.music.playlist", category = CommandCategory.MUSIC, usage = "playlist ncs|proximity|monstercat")
    public CommandResult onPlaylist(CommandArgs args) {
        if (args.getArgs().length <= 1) {
            NetBansBot bot = args.getBot();
            Member member = args.getMember();
            TextChannel channel = (TextChannel) args.getChannel();
            MusicManager musicPlayer = bot.getMusicManager();
            if (args.getArgs().length == 1) {
                String playlistName = args.getArgs(0);

                if (musicPlayer.canBotPlayMusic(member, channel, bot)) {
                    switch (playlistName.toLowerCase()) {
                        case "proximity":
                            musicPlayer.loadTrack("https://www.youtube.com/playlist?list=PL3osQJLUr9gL42vxYKssd62eng5MV_1WM", member, channel, true);
                            return CommandResult.SUCCESS;
                        case "ncs":
                            musicPlayer.loadTrack("https://www.youtube.com/playlist?list=PLRBp0Fe2GpgnIh0AiYKh7o7HnYAej-5ph", member, channel, true);
                            return CommandResult.SUCCESS;
                        case "monstercat":
                            musicPlayer.loadTrack("https://www.youtube.com/playlist?list=PLe8jmEHFkvsaDOOWcREvkgFoj6MD0pQ67", member, channel, true);
                            return CommandResult.SUCCESS;
                    }
                }
            } else {
                return CommandResult.INVALIDARGS;
            }
        }
        return CommandResult.DEFAULT;
    }
}
