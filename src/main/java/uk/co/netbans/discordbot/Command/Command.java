package uk.co.netbans.discordbot.Command;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import uk.co.netbans.discordbot.NetBansBot;

public interface Command<T, M extends ListenerAdapter> {
    CommandResult onExecute(NetBansBot bot, Member sender, TextChannel channel, String label, String[] args);

    String name();
    String desc();
    String usage();
    String[] aliases();
}
