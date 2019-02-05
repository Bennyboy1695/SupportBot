package uk.co.netbans.supportbot.Message;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.*;

import java.awt.*;
import java.time.Instant;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class Messenger {
    private final ScheduledExecutorService executor;
    private static EmbedBuilder commonEmbed;

    public Messenger() {
        this.executor = Executors.newScheduledThreadPool(2);
    }

    public static EmbedBuilder getCommonEmbed() {
        commonEmbed = new EmbedBuilder().setTimestamp(Instant.now());
        return commonEmbed;
    }

    public Message sendMessage(TextChannel channel, String message) {
        return this.sendMessage(channel, new MessageBuilder().append(message).build(), 0);
    }

    public Message sendMessage(TextChannel channel, String message, int lifetime) {
        return this.sendMessage(channel, new MessageBuilder().append(message).build(), lifetime);
    }

    public Message sendEmbed(TextChannel channel, MessageEmbed embed) {
        return this.sendMessage(channel, new MessageBuilder().setEmbed(embed).build(), 0);
    }

    public Message sendEmbed(TextChannel channel, MessageEmbed embed, int lifetime) {
       return this.sendMessage(channel, new MessageBuilder().setEmbed(embed).build(), lifetime);
    }

    public Message sendMessage(TextChannel channel, Message message, int lifetime) {

        Message sentMessage = channel.sendMessage(message).complete();
        if (lifetime != 0)
            this.delMessage(channel, sentMessage.getIdLong(), lifetime);

        return sentMessage;
    }

    public void delMessage(TextChannel channel, long id, int lifetime) {
        AtomicLong messageID = new AtomicLong();
        messageID.set(id);
        this.executor.schedule(() -> channel.getMessageById(messageID.get()).queue(m -> m.delete().queue()), lifetime, TimeUnit.SECONDS);
    }

    public Message sendPrivateMessage(User user, MessageEmbed embed) {
        PrivateChannel channel = user.openPrivateChannel().complete();
        return channel.sendMessage(new MessageBuilder().setEmbed(embed).build()).complete();
    }

    public Message sendPrivateMessage(User user, Message message) {
        PrivateChannel channel = user.openPrivateChannel().complete();
        return channel.sendMessage(message).complete();
    }

    public Message sendPrivateMessage(User user, String message) {
        PrivateChannel channel = user.openPrivateChannel().complete();
        return channel.sendMessage(new MessageBuilder().append(message).build()).complete();
    }

    public static MessageEmbed NOT_SAME_VOICE = new EmbedBuilder().setColor(Color.RED).setDescription("You are not in the same voice channel as the bot!").build();
    public static MessageEmbed NOT_VOICE = new EmbedBuilder().setColor(Color.RED).setDescription("You are not in a voice channel!").build();
    public static MessageEmbed INVALID_COMMAND = new EmbedBuilder().setColor(Color.RED).setDescription("This is not a valid command!").build();
    public static MessageEmbed GROUP_DOESNT_EXIST = new EmbedBuilder().setColor(Color.RED).setDescription("This group doesn't exist!").build();
    public static MessageEmbed GROUP_ALREADY_EXIST = new EmbedBuilder().setColor(Color.RED).setDescription("This group already exists!").build();
    public static MessageEmbed DISCORD_ROLE_DOESNT_EXIST = new EmbedBuilder().setColor(Color.RED).setDescription("This Discord role doesn't exist!").build();
    public static MessageEmbed GROUP_HAS_PERM = new EmbedBuilder().setColor(Color.RED).setDescription("This group already has this perm!").build();
    public static MessageEmbed USER_HAS_PERM = new EmbedBuilder().setColor(Color.RED).setDescription("This user already has this perm!").build();
    public static MessageEmbed INCOMPATIBLE_ARG = new EmbedBuilder().setColor(Color.RED).setDescription("This arg is incompatible with your current set of args!").build();
    public static MessageEmbed AMOUNT_TOO_HIGH = new EmbedBuilder().setColor(Color.RED).setDescription("The max amount allowed for this is 100!").build();
    public static MessageEmbed NO_MESSAGES_FOUND = new EmbedBuilder().setColor(Color.RED).setDescription("No messages matching your query was found!").build();
    public static MessageEmbed VOLUME_TOO_HIGH = new EmbedBuilder().setColor(Color.RED).setDescription("The max volume allowed is 100!").build();
    public static MessageEmbed VOLUME_TOO_LOW = new EmbedBuilder().setColor(Color.RED).setDescription("The min volume allowed is 1!").build();
    public static MessageEmbed HASTEBIN = new EmbedBuilder().setTitle("Exported List").setDescription("Successfully exported list to Json").setColor(Color.GREEN).build();
    public static MessageEmbed CHANNEL_LOCKED = getCommonEmbed().setDescription("\uD83D\uDD12 Channel has been locked to just Project Leaders and the Ticket Creator!").setColor(Color.GREEN).build();

    public static MessageEmbed INVALID_ARGS(String usage) {
        return new EmbedBuilder().setTitle("Invalid Args!").setDescription("Correct Usage: \n " + usage).setColor(Color.RED).build();
    }

    public static MessageEmbed NO_PERMS(String perm) {
        return new EmbedBuilder().setTitle("No Perms!").setDescription("You do not have the required permission: `" + perm + "` to run this command!").setColor(Color.RED).build();
    }

    public static MessageEmbed DELETED_AMOUNT_OF_MESSAGES(int amount, String username) {
        return new EmbedBuilder().setTitle("Success!").setDescription("Deleted " + amount + " messages from " + username + "!").setColor(Color.GREEN).build();
    }

    public static MessageEmbed DELETED_AMOUNT_OF_MESSAGES(int amount) {
        return new EmbedBuilder().setTitle("Success!").setDescription("Deleted " + amount + " messages!").setColor(Color.GREEN).build();
    }

    public static MessageEmbed VOLUME_NOW_AT(int volume) {
        return new EmbedBuilder().setDescription("Volume is now " + volume).setColor(Color.GREEN).build();
    }

    public static MessageEmbed LEAVING_VOICE(String channel) {
        return new EmbedBuilder().setTitle("Leaving Channel!").setDescription("Leaving " + channel + "!").setColor(Color.GREEN).build();
    }

    public static MessageEmbed JOINING_VOICE(String channel) {
        return new EmbedBuilder().setTitle("Joining Channel!").setDescription("Joining " + channel + "!").setColor(Color.GREEN).build();
    }

    public static MessageEmbed MOVING_VOICE(String oldChannel, String channel) {
        return new EmbedBuilder().setTitle("Moving Channel!").setDescription("Moving from " + oldChannel + " to " + channel + "!").setColor(Color.GREEN).build();
    }

}