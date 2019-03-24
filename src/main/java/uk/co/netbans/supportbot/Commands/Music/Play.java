package uk.co.netbans.supportbot.Commands.Music;

import me.bhop.bjdautilities.ReactionMenu;
import me.bhop.bjdautilities.command.annotation.Command;
import me.bhop.bjdautilities.command.annotation.Execute;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import uk.co.netbans.supportbot.CommandFramework.CommandResult;
import uk.co.netbans.supportbot.EmbedTemplates;
import uk.co.netbans.supportbot.NetBansBot;

import java.util.List;

@Command(label = {"play"}, usage = "play <url>")
public class Play {
    @Execute
    public CommandResult onPlay(Member member, TextChannel channel, Message message, String label, List<String> args, NetBansBot bot) {
        if (args.size() == 0) {
            new ReactionMenu.Builder(bot.getJDA())
                    .setEmbed(EmbedTemplates.ERROR.getEmbed().setDescription("You must either specify a url to load or you can choose a playlist from below.").build())
                    .setStartingReactions("proximity", "monstercat", "ncs", "panic")
                    .onClick("proximity", menu -> {
                        menu.getMessage().setContent(EmbedTemplates.SUCCESS.getEmbed().setDescription("Loading Proximity Playlist...").build());
                        menu.clearClickListeners();
                        bot.getAudioHandler().loadTrack(
                                "https://www.youtube.com/playlist?list=PL3osQJLUr9gL42vxYKssd62eng5MV_1WM",
                                member,
                                channel,
                                false
                        );
                        menu.destroyIn(2);
                    })
                    .onClick("monstercat", menu -> {
                        menu.getMessage().setContent(EmbedTemplates.SUCCESS.getEmbed().setDescription("Loading Monstercat Playlist...").build());
                        menu.clearClickListeners();
                        bot.getAudioHandler().loadTrack(
                                "https://www.youtube.com/playlist?list=PLe8jmEHFkvsaDOOWcREvkgFoj6MD0pQ67",
                                member,
                                channel,
                                false
                        );
                        menu.destroyIn(2);
                    })
                    .onClick("ncs", menu -> {
                        menu.getMessage().setContent(EmbedTemplates.SUCCESS.getEmbed().setDescription("Loading NoCopyrightSounds Playlist...").build());
                        menu.clearClickListeners();
                        bot.getAudioHandler().loadTrack(
                                "https://www.youtube.com/playlist?list=PLRBp0Fe2GpgnIh0AiYKh7o7HnYAej-5ph",
                                member,
                                channel,
                                false
                        );
                        menu.destroyIn(2);
                    })
                    .onClick("panic", menu -> {
                        menu.getMessage().setContent(EmbedTemplates.SUCCESS.getEmbed().setDescription("Loading Panic at the Disco Playlist...").build());
                        menu.clearClickListeners();
                        bot.getAudioHandler().loadTrack(
                                "https://www.youtube.com/playlist?list=PL0eheHgLSuZDVZ6ac_NkGomG0YXezgUHf",
                                member,
                                channel,
                                false
                        );
                        menu.destroyIn(2);
                    })
                    .buildAndDisplay(channel);
            return CommandResult.SUCCESS;
        }
        if (args.size() == 1) {

            System.out.println("Play music");
            return CommandResult.SUCCESS;
        } else
            return CommandResult.INVALIDARGS;
    }
}
