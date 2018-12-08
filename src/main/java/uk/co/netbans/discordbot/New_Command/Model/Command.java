package uk.co.netbans.discordbot.New_Command.Model;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import uk.co.netbans.discordbot.Message.Messenger;
import uk.co.netbans.discordbot.NetBansBot;
import uk.co.netbans.discordbot.New_Command.CommandResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class Command<T, S> {
    private final List<String> labels;
    private final String description;
    private final String usage;
    private final int minArgs;
    private final List<Long> channels;
    private final boolean whitelist;
    private final List<Command<S, ?>> children;

    public Command(List<String> labels, String description, String usage) {
        this(labels, description, usage, 0, new ArrayList<>(), true, null);
    }

    public Command(List<String> labels, String description, String usage, List<Long> channels, boolean whitelist) {
        this(labels, description, usage, 0, channels, whitelist, null);
    }

    public Command(List<String> labels, String description, String usage, int minArgs, List<Command<S, ?>> children) {
        this(labels, description, usage, minArgs, new ArrayList<>(), true, children);
    }

    public Command(List<String> labels, String description, String usage, int minArgs, List<Long> channels, boolean whitelist, List<Command<S, ?>> children) {
        this.labels = labels;
        this.description = description;
        this.usage = usage;
        this.minArgs = minArgs;
        this.channels = channels;
        this.whitelist = whitelist;
        this.children = children;
    }

    public List<String> getLabels() {
        return labels;
    }
    public String getDescription() {
        return description;
    }
    public String getUsage() {
        return usage;
    }
    public int getMinArgs() {
        return minArgs;
    }
    public List<Long> getChannels() {
        return channels;
    }
    public Optional<List<Command<S, ?>>> getChildren() {
        return Optional.ofNullable(children);
    }

    public abstract CommandResult execute(NetBansBot bot, Member sender, TextChannel channel, String label, T t, List<String> args);

    public abstract void sendUsage(Messenger messenger, TextChannel channel);

    public boolean canSend(TextChannel channel) {
        if (channels.isEmpty())
            return true; // no whitelist/blacklist
        if (whitelist)
            return channels.stream().anyMatch(l -> l.equals(channel.getIdLong()));
        return channels.stream().noneMatch(l -> l.equals(channel.getIdLong()));
    }
}
