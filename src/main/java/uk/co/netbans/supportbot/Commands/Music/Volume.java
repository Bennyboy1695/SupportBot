package uk.co.netbans.supportbot.Commands.Music;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import uk.co.netbans.supportbot.CommandFramework.Command;
import uk.co.netbans.supportbot.CommandFramework.CommandArgs;
import uk.co.netbans.supportbot.CommandFramework.CommandCategory;
import uk.co.netbans.supportbot.Message.Messenger;
import uk.co.netbans.supportbot.Music.MusicManager;
import uk.co.netbans.supportbot.CommandFramework.CommandResult;
import uk.co.netbans.supportbot.NetBansBot;

public class Volume {

    @Command(name = "volume", displayName = "volume", permission = "supportbot.command.music.volume", category = CommandCategory.MUSIC)
    public CommandResult onVolume(CommandArgs commandArgs) {
        NetBansBot bot = commandArgs.getBot();
        Member member = commandArgs.getMember();
        TextChannel channel = (TextChannel) commandArgs.getChannel();
        MusicManager musicPlayer = bot.getMusicManager();
        String[] args = commandArgs.getArgs();
        if (args.length == 0)
            return CommandResult.INVALIDARGS;

        if (musicPlayer.canBotPlayMusic(member, channel, bot)) {
            try {
                int vol = Integer.valueOf(args[0]);
                if (vol > 100) {
                    bot.getMessenger().sendEmbed(channel, Messenger.VOLUME_TOO_HIGH, 10);
                } else if (vol > 1) {
                    bot.getMusicManager().setVolume(channel.getGuild(), channel, vol);
                    bot.getMessenger().sendEmbed(channel, Messenger.VOLUME_NOW_AT(vol), 10);
                } else {
                    bot.getMessenger().sendEmbed(channel, Messenger.VOLUME_TOO_LOW, 10);
                }
                return CommandResult.SUCCESS;
            } catch (NumberFormatException e) {
                return CommandResult.INVALIDARGS;
            }
        }
        return CommandResult.SUCCESS;
    }

}
