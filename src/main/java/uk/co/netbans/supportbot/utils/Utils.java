package uk.co.netbans.supportbot.utils;

import me.bhop.bjdautilities.ReactionMenu;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;
import uk.co.netbans.supportbot.EmbedTemplates;
import uk.co.netbans.supportbot.SupportBot;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Utils {

    public static long timeToMillis(long year, long month, long week, long day, long hour, long min, long sec) {
        return (year*31536000000L) + (month*2628000000L) + TimeUnit.DAYS.toMillis(week*7) + TimeUnit.DAYS.toMillis(day) + TimeUnit.HOURS.toMillis(hour) + TimeUnit.MINUTES.toMillis(min) + TimeUnit.SECONDS.toMillis(sec);
    }

    public static long stringToMillisConverter(CharSequence text){
        Pattern pattern = Pattern.compile("([0-9]+w)?([0-9]+d)?([0-9]+h)?([0-9]+m)?([0-9]+s)?", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);
        if (matcher.matches()){
            return timeToMillis(0L,0L,
                    (matcher.group(1) != null ? Long.valueOf(matcher.group(1).replace("w", "")) : 0L),
                    (matcher.group(2) != null ? Long.valueOf(matcher.group(2).replace("d", "")) : 0L),
                    (matcher.group(3) != null ? Long.valueOf(matcher.group(3).replace("h", "")) : 0L),
                    (matcher.group(4) != null ? Long.valueOf(matcher.group(4).replace("m", "")) : 0L),
                    (matcher.group(5) != null ? Long.valueOf(matcher.group(5).replace("s", "")) : 0L));
        }
        return 0L;
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

    public static String msToTimeString(long millis) {
        int sec = (int) TimeUnit.MILLISECONDS.toSeconds(millis);
        String minutes = String.valueOf(sec / 60);
        String seconds = String.valueOf(sec % 60);
        return String.format("%s:%s",
                minutes.length() < 2 ? "0" + minutes : minutes,
                seconds.length() < 2 ? "0" + seconds : seconds);
    }

    public static Member randomMember(SupportBot bot) {
        List<Member> members = new ArrayList<>();
        for (Role role : bot.getJDA().getGuildById(bot.getGuildID()).getRoles()) {
            if (role.getName().toLowerCase().contains("leader") || role.getName().toLowerCase().contains("cont") || role.getName().toLowerCase().contains("dev") || role.getName().toLowerCase().contains("admin")) {
                for (Member member : bot.getJDA().getGuildById(bot.getGuildID()).getMembers()) {
                    if (member.getOnlineStatus().equals(OnlineStatus.ONLINE) || member.getOnlineStatus().equals(OnlineStatus.IDLE)) {
                        if (member.getRoles().contains(role)) {
                            if (!member.getUser().isBot())
                                members.add(member);
                        }
                    }
                }
            }
        }
        return members.get(ThreadLocalRandom.current().nextInt(members.size()));
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

    public static class TicketUtils {

        public static boolean lockChannel(SupportBot bot, Member member, Guild guild, TextChannel channel) {
                if (member.hasPermission(Permission.MANAGE_CHANNEL)) {
                    channel.getManager().putPermissionOverride(channel.getPinnedMessages().complete().get(0).getMentionedMembers().get(0), 101440L, 0L).complete();
                    for (Role role : bot.getJDA().getGuildById(guild.getIdLong()).getRoles()) {
                        channel.getManager().putPermissionOverride(role, 0L, 76800L).complete();
                    }
                    bot.getMessenger().sendEmbed(channel, EmbedTemplates.CHANNEL_LOCKED.getEmbed().build());
                    return true;
                } else {
                    bot.getMessenger().sendEmbed(channel, EmbedTemplates.INVALID_PERMISSION.getEmbed(Permission.MANAGE_CHANNEL, "lock this channel!").build(), 10);
                    return false;
                }
        }

        public static boolean resetLockOnChannel(SupportBot bot, Member member, Guild guild, TextChannel channel) {
            if (member.hasPermission(Permission.MANAGE_CHANNEL)) {
                channel.getManager().sync().complete();
                channel.getManager().putPermissionOverride(channel.getPinnedMessages().complete().get(0).getMentionedMembers().get(0), 101440L, 0L).complete();
                bot.getMessenger().sendEmbed(channel, EmbedTemplates.CHANNEL_LOCK_RESET.getEmbed().build());
                return true;
            } else {
                bot.getMessenger().sendEmbed(channel, EmbedTemplates.INVALID_PERMISSION.getEmbed(Permission.MANAGE_CHANNEL, "reset the lock on this channel!").build(), 10);
                return false;
            }
        }

        public static boolean unlockChannel(SupportBot bot, Member member, Guild guild, TextChannel channel) {
            if (member.hasPermission(Permission.MANAGE_CHANNEL)) {
                channel.getManager().putPermissionOverride(guild.getPublicRole(), 117824L, 0L).complete();
                bot.getMessenger().sendEmbed(channel, EmbedTemplates.CHANNEL_UNLOCKED_FULL.getEmbed().build());
                return true;
            } else {
                bot.getMessenger().sendEmbed(channel, EmbedTemplates.INVALID_PERMISSION.getEmbed(Permission.MANAGE_CHANNEL, "lock this channel!").build(), 10);
                return false;
            }
        }

        public static boolean createTicket(SupportBot bot, Member member, Guild guild, Message message) {
            try {
                DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/YY");
                DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");
                TextChannel supportChannel = (TextChannel) bot.getJDA().getCategoryById(Long.valueOf(bot.getMongoRequestRegistry().getGuildTicketSettings(guild.getIdLong()).get("categoryId").toString()))
                        .createTextChannel(member.getUser().getName() + "-" + ThreadLocalRandom.current().nextInt(99999)).complete();

                supportChannel.createPermissionOverride(member).setAllow(101440).complete();
                supportChannel.getManager().setTopic("Creation date: " + supportChannel.getCreationTime().format(dateFormat) + " Creation Time: " + supportChannel.getCreationTime().format(timeFormat) + "GMT").complete();
                Message message2 = new MessageBuilder()
                        .append("**Author:** " + member.getAsMention())
                        .append("\n")
                        .append("**Message:** " + message.getContentRaw())
                        .append("\n")
                        .append("\n")
                        .append("_To close this ticket please react with a \u2705 to this message!_")
                        .build();
                Message supportMessage = new ReactionMenu.Builder(bot.getJDA())
                        .setMessage(message2.getContentRaw())
                        .onClick("\u2705", (finished, ticketMember) -> Utils.TicketUtils.closeTicket(bot, guild.getMember(ticketMember), guild, supportChannel))
                        .onClick("\uD83D\uDD12", (lock, ticketMember) -> Utils.TicketUtils.lockChannel(bot, guild.getMember(ticketMember), guild, supportChannel))
                        .onClick("\uD83D\uDD13", (unlock, ticketMember) -> Utils.TicketUtils.unlockChannel(bot, guild.getMember(ticketMember), guild, supportChannel))
                        .onRemove("\uD83D\uDD12", (unlock, ticketMember) -> Utils.TicketUtils.resetLockOnChannel(bot, guild.getMember(ticketMember), guild, supportChannel))
                        .onRemove("\uD83D\uDD13", (lock, ticketMember) -> Utils.TicketUtils.resetLockOnChannel(bot, guild.getMember(ticketMember), guild, supportChannel))
                        .setRemoveReactions(false)
                        .buildAndDisplay(supportChannel).getMessage();
                for (Message.Attachment attachment : message2.getAttachments()) {
                    try {
                        if (!new File(bot.getLogDirectory().toFile(), "tmp").exists()) {
                            new File(bot.getLogDirectory().toFile(), "tmp").mkdir();
                        }
                        attachment.download(new File(bot.getLogDirectory().toFile() + "/tmp/", supportChannel.getName() + ".log"));
                        supportChannel.sendFile(new File(bot.getLogDirectory().toFile() + "/tmp/", supportChannel.getName() + ".log")).complete();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                supportMessage.pin().complete();
                supportChannel.getHistory().retrievePast(1).queue(l -> l.forEach(m -> m.delete().queue()));
                member.getUser().openPrivateChannel().complete().sendMessage(new EmbedBuilder()
                        .setTitle("Support Channel")
                        .setDescription("https://discordapp.com/channels/" + guild.getIdLong() + "/" + supportChannel.getIdLong())
                        .setColor(new Color(127, 255, 212))
                        .build()).complete();
                return true;
            } catch (Exception e) {
                return false;
            }
        }

        public static boolean closeTicket(SupportBot bot, Member member, Guild guild, TextChannel channel) {
            if (member.hasPermission(Permission.MANAGE_CHANNEL) || channel.getPinnedMessages().complete().get(0).getMentionedUsers().get(0).equals(member)) {
                Path logDirectory = bot.getDirectory().resolve("logs").resolve(String.valueOf(guild.getIdLong()));
                String link = "";
                if (logDirectory.toFile().exists()) {
                    try (BufferedReader reader = Files.newBufferedReader(logDirectory.resolve(channel.getName() + ".log"))) {
                        link = Hastebin.post(reader.lines().collect(Collectors.joining("\n")));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        Files.deleteIfExists(logDirectory.resolve(channel.getName() + ".log"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                String finalLink = link;
                member.getUser().openPrivateChannel().queue(pm -> {
                    Consumer<Message> ticket_completed = msg -> channel.delete().reason("Ticket completed").queue();
                    if (!finalLink.equals("")) {
                        pm.sendMessage(EmbedTemplates.TICKET_COMPLETED.getEmbed().setTitle("Ticket completed!", finalLink).build()).queue(ticket_completed);
                    } else {
                        pm.sendMessage(EmbedTemplates.TICKET_COMPLETED.getEmbed().setTitle("Ticket completed!").build()).queue(ticket_completed);
                    }
                });
                    return true;
            }
            return false;
        }

         public static ArrayList<TextChannel> getSupportChannels(SupportBot bot) {
            ArrayList<TextChannel> channels = new ArrayList<>();
            for (TextChannel channel : bot.getJDA().getGuildById(bot.getGuildID()).getCategoryById(bot.getSupportCategoryID()).getTextChannels()) {
                channels.add(channel);
                if (channel.getIdLong() == 526622624655343626L || channel.getIdLong() == 521478045924589568L) {
                    channels.remove(channel);
                }
            }
            return channels;
        }

        public static void doExpiryCheck(SupportBot bot) {
            int expiryCount = 0;
            for (TextChannel channel : getSupportChannels(bot)) {
                for (Message message : channel.getHistory().retrievePast(5).complete()) {
                    if (!channel.getMessageById(channel.getLatestMessageId()).complete().getAuthor().isBot()) {
                        if (message.getAuthor().equals(channel.getPinnedMessages().complete().get(0).getMentionedUsers().get(0))) {
                            if (message.getCreationTime().isBefore(OffsetDateTime.now().minusWeeks(1))) {
                                expiryCount++;
                            }
                        }
                    }
                }
                if (expiryCount > 0) {
                    expireChannel(bot, channel);
                }
            }
        }

        public static void expireChannel(SupportBot bot, TextChannel channel) {
            for (Message message : channel.getPinnedMessages().complete()) {
                if (message.getAuthor().isBot()) {
                    for (Member member : message.getMentionedMembers()) {
                        String reason = "This issue has not been replied to in over a week by the Ticket Creator!";
                        member.getUser().openPrivateChannel().complete().sendMessage(new EmbedBuilder()
                                .setTitle("Issue Expired!")
                                .setDescription("This issue has not been replied to in over a week by the Ticket Creator!" + " \nBecause of this we have sent you a log file containing the history, so that you may look at it incase you encounter the issue again!")
                                .addField("Next Step: ", "If the issue still persists, please create a github issue using the link provided below!\n" +
                                        "https://github.com/NetBans/NetBans/issues/new", false)
                                .build()).complete();
                        member.getUser().openPrivateChannel().complete()
                                .sendFile(bot.getLogDirectory().resolve(channel.getName() + ".log").toFile())
                                .complete();
                        bot.getJDA().getGuildById(bot.getGuildID()).getTextChannelById(Long.valueOf(bot.getMainConfig().getConfigValue("logChannelID").getAsString()))
                                .sendFile(bot.getLogDirectory().resolve(channel.getName() + ".log").toFile(), new MessageBuilder()
                                        .append(reason)
                                        .build())
                                .complete();
                        channel.delete().reason("Issue Expired!").complete();
                    }
                }
            }
        }
    }
}
