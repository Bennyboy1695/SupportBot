package uk.co.netbans.supportbot.Commands.Admin;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import uk.co.netbans.supportbot.CommandFramework.Command;
import uk.co.netbans.supportbot.CommandFramework.CommandArgs;
import uk.co.netbans.supportbot.CommandFramework.CommandCategory;
import uk.co.netbans.supportbot.NetBansBot;
import uk.co.netbans.supportbot.CommandFramework.CommandResult;
import uk.co.netbans.supportbot.Commands.Support.Help;

public class Test {

    @Command(name = "test", displayName = "test", aliases = "tester", usage = "test", permission = "command.test.new", category = CommandCategory.ADMIN)
    public CommandResult onTest(CommandArgs args) {
        NetBansBot bot = args.getBot();

        Message message = bot.getMessenger().sendEmbed((TextChannel) args.getChannel(), Help.helpPages(bot, 1), 30);

        message.addReaction("\u25C0").complete();
        message.addReaction("\u25B6").complete();

        return CommandResult.SUCCESS;
    }
}
