package uk.co.netbans.discordbot.Command;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import uk.co.netbans.discordbot.NetBansBot;

public interface Command {
    CommandCode onExecute(NetBansBot bot, Member sender, TextChannel channel, String label, String[] args);

    String name();
    String desc();
    String usage();
    String[] aliases();
}
