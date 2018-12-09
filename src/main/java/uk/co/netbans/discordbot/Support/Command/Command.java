package uk.co.netbans.discordbot.Support.Command;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import uk.co.netbans.discordbot.NetBansBot;
import uk.co.netbans.discordbot.PermType;

public interface Command {
    CommandResult onExecute(NetBansBot bot, Member sender, TextChannel channel, String label, String[] args);

    String name();
    String desc();
    String usage();
    String[] aliases();

    default PermType getPermission() {
        return PermType.DEFAULT;
    }
}
