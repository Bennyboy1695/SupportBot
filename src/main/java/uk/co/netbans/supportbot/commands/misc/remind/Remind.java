package uk.co.netbans.supportbot.commands.misc.remind;

import me.bhop.bjdautilities.command.CommandHandler;
import me.bhop.bjdautilities.command.annotation.Command;
import me.bhop.bjdautilities.command.annotation.Execute;
import me.bhop.bjdautilities.command.result.CommandResult;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import uk.co.netbans.supportbot.SupportBot;

@Command(label = {"remind", "reminder"}, children = {Add.class,List.class,Remove.class})
public class Remind {

    @Execute
    public CommandResult onRemind(Member member, TextChannel channel, Message message, String label, java.util.List<String> args, SupportBot bot, CommandHandler handler) {
        if (!args.get(0).toLowerCase().equals("list") || !args.get(0).toLowerCase().equals("add") || !args.get(0).toLowerCase().equals("remove")) {
            final CommandResult[] result = {CommandResult.success()};
            handler.getCommand(Add.class).ifPresent(cmd -> {
               result[0] = cmd.execute(member, channel, message, label, args);
            });
            if (result[0].equals(CommandResult.invalidArguments())) {
                //
            }
            return result[0];
        }
        return CommandResult.success();
    }

}

