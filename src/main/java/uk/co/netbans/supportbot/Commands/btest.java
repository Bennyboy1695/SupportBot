package uk.co.netbans.supportbot.Commands;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import uk.co.netbans.supportbot.CommandFramework.Command;
import uk.co.netbans.supportbot.CommandFramework.CommandArgs;
import uk.co.netbans.supportbot.CommandFramework.CommandResult;
import uk.co.netbans.supportbot.Message.NewMessenger;

public class btest {
    private final NewMessenger messenger = new NewMessenger();

    @Command(name = "bhop", displayName = "bhop", aliases = "", usage = "shut up and die")
    public CommandResult onExecute(CommandArgs args) {

        args.getBot().getAudioHandler().loadTrack("https://www.youtube.com/watch?v=Cp4wj80ux64", args.getMember(), (TextChannel) args.getChannel(), false);

//        new ReactionMenu.Builder(args.getBot())
//                .setMessage("I am a message")
//                .setStartingReactions("\u2705", "\uD83D\uDED1")
//                .onClick("\u2705", menu -> {
//                    menu.data.put("clicked", true);
////                    menu.getMessage().setContent("I have changed since the check was done!!!\nthe current data is: " + menu.data.toString());
//                    menu.removeReaction("\u2705");
//                })
//                .onClick("\uD83D\uDED1", ReactionMenu::destroy)
//                .buildAndDisplay((TextChannel) args.getChannel());

        return CommandResult.SUCCESS;
    }
}
