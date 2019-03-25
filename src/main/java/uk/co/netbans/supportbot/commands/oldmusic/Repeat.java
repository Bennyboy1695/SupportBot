package uk.co.netbans.supportbot.commands.oldmusic;



public class Repeat {

//    @Command(name = "repeat", displayName = "repeat", category = CommandCategory.MUSIC)
//    public CommandResult onRepeat(CommandArgs commandArgs) {
//        NetBansBot bot = commandArgs.getBot();
//        Member member = commandArgs.getMember();
//        TextChannel channel = (TextChannel) commandArgs.getChannel();
//        MusicManager musicPlayer = bot.getMusicManager();
//
//        if (musicPlayer.isIdle(channel.getGuild()))
//            return CommandResult.SUCCESS;
//        if (musicPlayer.canBotPlayMusic(member, channel, bot)) {
//            if (musicPlayer.getTrackManager(channel.getGuild()).isLoop()) {
//                musicPlayer.getTrackManager(channel.getGuild()).setLoop(false);
//                bot.getMessenger().sendMessage(channel, "\uD83D\uDD01 Stopped the repeat!", 10);
//            } else {
//                musicPlayer.getTrackManager(channel.getGuild()).setLoop(true);
//                bot.getMessenger().sendMessage(channel, "\uD83D\uDD01 Started the repeat!", 10);
//            }
//        }
//        return CommandResult.SUCCESS;
//    }
}
