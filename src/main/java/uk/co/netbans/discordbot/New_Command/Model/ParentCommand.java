package uk.co.netbans.discordbot.New_Command.Model;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import uk.co.netbans.discordbot.Message.Messenger;
import uk.co.netbans.discordbot.NetBansBot;
import uk.co.netbans.discordbot.New_Command.CommandResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("OptionalGetWithoutIsPresent")
public abstract class ParentCommand<T, I> extends Command<Void, T> {
    public ParentCommand(List<String> labels, String description, String usage, int minArgs, List<Command<T, ?>> children) {
        super(labels, description, usage, minArgs, children);
    }

    public ParentCommand(List<String> labels, String description, String usage, int minArgs, List<Long> channels, boolean whitelist, List<Command<T, ?>> children) {
        super(labels, description, usage, minArgs, channels, whitelist, children);
    }

    @Override
    public CommandResult execute(NetBansBot bot, Member sender, TextChannel channel, String label, Void aVoid, List<String> args) {
        if (args.size() < this.getMinArgs())
            return CommandResult.INVALID_ARGUMENTS;

        Optional<Command<T, ?>> opt = getChildren().get().stream()
                .filter(cmd -> cmd.getLabels().contains(args.get(getMinArgs()-1)))
                .findFirst();

        if (!opt.isPresent())
            return CommandResult.INVALID_CHILD_COMMAND;

        final Command<T, ?> cmd = opt.get();
        if (!cmd.canSend(channel))
            return CommandResult.INVALID_CHANNEL;

        List<String> arguments = new ArrayList<>();
        if (args.size() > getMinArgs())
            arguments.addAll(args.subList(getMinArgs(), args.size()));

        if (arguments.size() < cmd.getMinArgs())
            return CommandResult.INVALID_ARGUMENTS;

        final String name = args.get(0);
        I tID = parseTarget(bot, name, sender);
        if (tID == null)
            return CommandResult.ERROR;

        T target = getTarget(bot, tID, sender);
        if (target != null)
            return cmd.execute(bot, sender, channel, label, target, arguments);
        return CommandResult.ERROR;
    }

    protected abstract I parseTarget(NetBansBot bot, String target, Member sender);
    protected abstract T getTarget(NetBansBot bot, I target, Member sender);

    @Override
    public void sendUsage(Messenger messenger, TextChannel channel) {
        messenger.sendMessage(channel, "Parent Command Usage for " + getLabels(), 10);
    }

    @Override
    public boolean canSend(TextChannel channel) {
        return getChildren().get().stream().anyMatch(cmd -> cmd.canSend(channel));
    }
}
