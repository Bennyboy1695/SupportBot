package uk.co.netbans.supportbot.commands.misc.remind;

import me.bhop.bjdautilities.command.CommandResult;
import me.bhop.bjdautilities.command.annotation.Command;
import me.bhop.bjdautilities.command.annotation.Execute;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import uk.co.netbans.supportbot.NetBansBot;

import java.util.List;

@Command("remove")
public class Remove {

    @Execute
    public CommandResult onRemove(Member member, TextChannel channel, Message message, String label, List<String> args, NetBansBot bot) {
        return CommandResult.SUCCESS;
    }
}
