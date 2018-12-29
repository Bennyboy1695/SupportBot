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
    private EmbedBuilder commonEmbed;

    public Messenger() {
        this.executor = Executors.newScheduledThreadPool(2);
    }

    public EmbedBuilder getCommonEmbed() {
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
    public static MessageEmbed PERM_ALREADY_EXIST = new EmbedBuilder().setColor(Color.RED).setDescription("This group already has this perm!").build();
    public static MessageEmbed INCOMPATIBLE_ARG = new EmbedBuilder().setColor(Color.RED).setDescription("This arg is incompatible with your current set of args!").build();

    public static MessageEmbed INVALID_ARGS(String usage) {
        return new EmbedBuilder().setTitle("Invalid Args!").setDescription("Correct Usage: \n " + usage).setColor(Color.RED).build();
    }

    public static MessageEmbed NO_PERMS(String perm) {
        return new EmbedBuilder().setTitle("No Perms!").setDescription("You do not have the required permission: `" + perm + "` to run this command!").setColor(Color.RED).build();
    }


}
