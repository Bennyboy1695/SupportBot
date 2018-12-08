package uk.co.netbans.discordbot.New_Command.Model;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import uk.co.netbans.discordbot.Message.Messenger;
import uk.co.netbans.discordbot.NetBansBot;
import uk.co.netbans.discordbot.New_Command.CommandResult;

import java.util.ArrayList;
import java.util.List;

public abstract class SingletonCommand<T, I> extends Command<Void, T> {
    public SingletonCommand(List<String> labels, String description, String usage) {
        super(labels, description, usage);
    }

    public SingletonCommand(List<String> labels, String description, String usage, List<Long> channels, boolean whitelist) {
        super(labels, description, usage, channels, whitelist);
    }

    @Override
    public CommandResult execute(NetBansBot bot, Member sender, TextChannel channel, String label, Void aVoid, List<String> args) {
        if (args.size() < this.getMinArgs())
            return CommandResult.INVALID_ARGUMENTS;

        if (!super.canSend(channel))
            return CommandResult.INVALID_CHANNEL;

        List<String> arguments = new ArrayList<>();
        if (args.size() > getMinArgs())
            arguments.addAll(args.subList(getMinArgs(), args.size()));

        if (arguments.size() < getMinArgs())
            return CommandResult.INVALID_ARGUMENTS;

        I tID = parseTarget(bot, args.get(0), sender);
        if (tID == null)
            return CommandResult.ERROR;

        T target = getTarget(bot, tID, sender);
        if (target != null)
            return this.execute(bot, sender, channel, target, arguments);
        return CommandResult.ERROR;
    }

    protected abstract CommandResult execute(NetBansBot bot, Member source, TextChannel channel, T t, List<String> args);

    protected abstract I parseTarget(NetBansBot bot, String target, Member sender);
    protected abstract T getTarget(NetBansBot bot, I target, Member sender);

    @Override
    public void sendUsage(Messenger messenger, TextChannel channel) {
        messenger.sendMessage(channel, "Singleton Command Usage for " + getLabels(), 10);
    }
}
