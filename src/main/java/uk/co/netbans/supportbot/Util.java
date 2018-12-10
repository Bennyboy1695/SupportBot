package uk.co.netbans.supportbot;

import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.User;
import uk.co.netbans.supportbot.Music.MusicManager;

public class Util {
    private static final MusicManager musicManager = new MusicManager();

    public static MusicManager getMusicManager() {
        return musicManager;
    }

    private static void sendMessage(MessageChannel channel, Message message) {
        channel.sendMessage(message).queue(null, null);
    }

    public static void sendMessage(MessageChannel channel, MessageEmbed embed) {
        sendMessage(channel, new MessageBuilder().setEmbed(embed).build());
    }

    public static void sendMessage(MessageChannel channel, String message) {
        sendMessage(channel, new MessageBuilder().append(message).build());
    }

    public static String filter(String msgContent) {
        return msgContent.length() > 2000
                ? "*The output message is over 2000 characters!*"
                : msgContent.replace("@everyone", "@\u180Eeveryone").replace("@here", "@\u180Ehere");
    }

    public static String userDiscrimSet(User u) {
        return stripFormatting(u.getName()) + "#" + u.getDiscriminator();
    }

    public static String stripFormatting(String s) {
        return s.replace("*", "\\*")
                .replace("`", "\\`")
                .replace("_", "\\_")
                .replace("~~", "\\~\\~")
                .replace(">", "\u180E>");
    }
}
