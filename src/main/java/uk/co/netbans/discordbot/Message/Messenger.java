package uk.co.netbans.discordbot.Message;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.*;

import java.awt.*;
import java.time.Instant;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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

    public Message sendMessage(final TextChannel channel, Message message, int lifetime) {
        final long messageID = channel.sendMessage(message).complete().getIdLong();

        if (lifetime != 0)
            this.executor.schedule(() -> channel.getMessageById(messageID).queue(m -> m.delete().reason("netbans_auto_deletion").queue()), lifetime, TimeUnit.SECONDS);

        return channel.getMessageById(messageID).complete();
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

    public static MessageEmbed NO_PERMS = new EmbedBuilder().setColor(Color.RED).setDescription("You do not have permission to run this command!").build();

}
