package uk.co.netbans.supportbot.Commands.Music;

import net.dv8tion.jda.core.entities.TextChannel;
import uk.co.netbans.supportbot.CommandFramework.Command;
import uk.co.netbans.supportbot.CommandFramework.CommandArgs;
import uk.co.netbans.supportbot.CommandFramework.CommandCategory;
import uk.co.netbans.supportbot.CommandFramework.CommandResult;
import uk.co.netbans.supportbot.Message.EmbedTemplates;
import uk.co.netbans.supportbot.Message.ReactionMenu;

// proximity 549805463416340481
public class Play {


    @Command(name = "newplay", displayName = "newplay", category = CommandCategory.MUSIC, usage = "newplay <url>")
    public CommandResult onPlaylist(CommandArgs args) {
        if (args.getArgs().length == 0) {
            new ReactionMenu.Builder(args.getBot())
                    .setEmbed(EmbedTemplates.ERROR.getEmbed().setDescription("You must either specify a url to load or you can choose a playlist from below.").build())
                    .setStartingReactions("proximity", "monstercat", "ncs", "panic")
                    .onClick("proximity", menu -> {
                        menu.getMessage().setContent(EmbedTemplates.SUCCESS.getEmbed().setDescription("Loading Proximity Playlist...").build());
                        menu.clearClickListeners();
                        args.getBot().getAudioHandler().loadTrack(
                                "https://www.youtube.com/playlist?list=PL3osQJLUr9gL42vxYKssd62eng5MV_1WM",
                                args.getMember(),
                                (TextChannel) args.getChannel(),
                                false
                        );
                        menu.destroyIn(2);
                    })
                    .onClick("monstercat", menu -> {
                        menu.getMessage().setContent(EmbedTemplates.SUCCESS.getEmbed().setDescription("Loading Monstercat Playlist...").build());
                        menu.clearClickListeners();
                        args.getBot().getAudioHandler().loadTrack(
                                "https://www.youtube.com/playlist?list=PLe8jmEHFkvsaDOOWcREvkgFoj6MD0pQ67",
                                args.getMember(),
                                (TextChannel) args.getChannel(),
                                false
                        );
                        menu.destroyIn(2);
                    })
                    .onClick("ncs", menu -> {
                        menu.getMessage().setContent(EmbedTemplates.SUCCESS.getEmbed().setDescription("Loading NoCopyrightSounds Playlist...").build());
                        menu.clearClickListeners();
                        args.getBot().getAudioHandler().loadTrack(
                                "https://www.youtube.com/playlist?list=PLRBp0Fe2GpgnIh0AiYKh7o7HnYAej-5ph",
                                args.getMember(),
                                (TextChannel) args.getChannel(),
                                false
                        );
                        menu.destroyIn(2);
                    })
                    .onClick("panic", menu -> {
                        menu.getMessage().setContent(EmbedTemplates.SUCCESS.getEmbed().setDescription("Loading Panic at the Disco Playlist...").build());
                        menu.clearClickListeners();
                        args.getBot().getAudioHandler().loadTrack(
                                "https://www.youtube.com/playlist?list=PL0eheHgLSuZDVZ6ac_NkGomG0YXezgUHf",
                                args.getMember(),
                                (TextChannel) args.getChannel(),
                                false
                        );
                        menu.destroyIn(2);
                    })
                    .buildAndDisplay((TextChannel) args.getChannel());
            return CommandResult.SUCCESS;
        }
        return CommandResult.SUCCESS;
    }
}
