package uk.co.netbans.supportbot.Commands.Misc.Remind;

import me.bhop.bjdautilities.command.CommandHandler;
import me.bhop.bjdautilities.command.annotation.Command;
import me.bhop.bjdautilities.command.annotation.Execute;
import me.bhop.bjdautilities.command.result.CommandResult;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import uk.co.netbans.supportbot.NetBansBot;

@Command(label = {"remind", "reminder"}, children = {Add.class,List.class,Remove.class})
public class Remind {

    @Execute
    public CommandResult onRemind(Member member, TextChannel channel, Message message, String label, java.util.List<String> args, NetBansBot bot, CommandHandler handler) {
        return CommandResult.success();
    }

}

