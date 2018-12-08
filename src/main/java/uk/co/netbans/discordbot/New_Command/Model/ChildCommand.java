package uk.co.netbans.discordbot.New_Command.Model;

import net.dv8tion.jda.core.entities.TextChannel;
import uk.co.netbans.discordbot.Message.Messenger;

import java.util.List;

public abstract class ChildCommand<T> extends Command<T, Void> {
    public ChildCommand(List<String> labels, String description, String usage) {
        super(labels, description, usage);
    }

    public ChildCommand(List<String> labels, String description, String usage, List<Long> channels, boolean whitelist) {
        super(labels, description, usage, channels, whitelist);
    }

    @Override
    public void sendUsage(Messenger messenger, TextChannel channel) {
        messenger.sendMessage(channel, "Child Command Usage for " + getLabels(), 10);
    }
}
