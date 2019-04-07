package uk.co.netbans.supportbot.commands.oldmusic;



public class Queue {

//    @Command(name = "queue", displayName = "queue", category = CommandCategory.MUSIC)
//    public CommandResult onQueue(CommandArgs commandArgs) {
//        SupportBot bot = commandArgs.getBot();
//        Member member = commandArgs.getMember();
//        TextChannel channel = (TextChannel) commandArgs.getChannel();
//        MusicManager musicPlayer = bot.getMusicManager();
//        String[] args = commandArgs.getArgs();
//
//        if (!musicPlayer.hasPlayer(channel.getGuild()) || musicPlayer.getTrackManager(channel.getGuild()).getQueuedTracks().isEmpty()) {
//            bot.getMessenger().sendMessage(channel, "The music queue is empty!", 10);
//            return CommandResult.SUCCESS;
//        }
//        StringBuilder sb = new StringBuilder();
//        Set<TrackInfo> queue = musicPlayer.getTrackManager(channel.getGuild()).getQueuedTracks();
//        queue.forEach(trackInfo -> sb.append(musicPlayer.buildQueueMessage(trackInfo)));
//        String embedTitle = String.format(musicPlayer.QUEUE_INFO, queue.size());
//
//        if (sb.length() <= 1960) {
//            musicPlayer.sendEmbed(channel, embedTitle, "**>** " + sb.toString());
//        } else /* if (sb.length() <= 20000) */ {
//            try {
//                sb.setLength(sb.length() - 1);
//                HttpResponse response = Unirest.post("https://hastebin.com/documents").body(sb.toString()).asString();
//                musicPlayer.sendEmbed(channel, embedTitle, "[Click here for a detailed list](https://hastebin.com/"
//                        + new JSONObject(response.getBody().toString()).getString("key") + ")");
//            } catch (UnirestException ex) {
//                ex.printStackTrace();
//            }
//        }
//        return CommandResult.SUCCESS;
//    }
}
