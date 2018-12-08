package uk.co.netbans.discordbot.New_Command.Model;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import uk.co.netbans.discordbot.Message.Messenger;
import uk.co.netbans.discordbot.NetBansBot;
import uk.co.netbans.discordbot.New_Command.CommandResult;

import java.util.List;

public abstract class IsolatedCommand extends Command<Void, Void> {
    public IsolatedCommand(List<String> labels, String description, String usage) {
        super(labels, description, usage);
    }

    public IsolatedCommand(List<String> labels, String description, String usage, List<Long> channels, boolean whitelist) {
        super(labels, description, usage, channels, whitelist);
    }

    @Override
    public CommandResult execute(NetBansBot bot, Member sender, TextChannel channel, String label, Void aVoid, List<String> args) {
        return this.execute(bot, sender, channel, label, args);
    }

    protected abstract CommandResult execute(NetBansBot bot, Member sender, TextChannel channel, String label, List<String> args);

    @Override
    public void sendUsage(Messenger messenger, TextChannel channel) {
        messenger.sendMessage(channel, "Isolated Command Usage for " + getLabels(), 10);
    }
}
