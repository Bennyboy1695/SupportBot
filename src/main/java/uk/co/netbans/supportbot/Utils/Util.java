package uk.co.netbans.supportbot.Utils;

import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.*;
import uk.co.netbans.supportbot.Music.MusicManager;
import uk.co.netbans.supportbot.NetBansBot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public static boolean containsLink(String message) {
        String URL_REGEX = "^((https?|ftp)://|(www|ftp)\\.)?[a-z0-9-]+(\\.[a-z0-9-]+)+([/?].*)?$";

        Pattern p = Pattern.compile(URL_REGEX);
        Matcher m = p.matcher(message);
        return m.find();
    }

    public static String randomUser(NetBansBot bot) {
        List<String> names = new ArrayList<>();
        for (Role role : bot.getJDA().getGuildById(bot.getGuildID()).getRoles()) {
            if (role.getName().contains("Leader") || role.getName().contains("Cont") || role.getName().contains("Dev")) {
                for (Member user : bot.getJDA().getGuildById(bot.getGuildID()).getMembers()) {
                    if (user.getOnlineStatus().equals(OnlineStatus.ONLINE) || user.getOnlineStatus().equals(OnlineStatus.IDLE)) {
                        if (user.getRoles().contains(role)) {
                            names.add(user.getEffectiveName());
                        }
                    }
                }
            }
        }
        return names.get(ThreadLocalRandom.current().nextInt(names.size()));
    }

    public static String[] quotesOrSpaceSplits(String str) {
        str += " ";
        ArrayList<String> strings = new ArrayList<String>();
        boolean inQuote = false;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == '"' || c == ' ' && !inQuote) {
                if (c == '"')
                    inQuote = !inQuote;
                if (!inQuote && sb.length() > 0) {
                    strings.add(sb.toString());
                    sb.delete(0, sb.length());
                }
            } else
                sb.append(c);
        }
        return strings.toArray(new String[strings.size()]);
    }

    public static String formatProgressBar(long progress, long full) {
        double percentage = (double) progress / full;
        StringBuilder progressBar = new StringBuilder();
        for (int i = 0; i < 20; i++) {
            if ((int) (percentage * 20) == i)
                progressBar.append("\uD83D\uDD18");
            else
                progressBar.append("▬");
        }
        return progressBar.toString();
    }
}
