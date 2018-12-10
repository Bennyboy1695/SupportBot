package uk.co.netbans.supportbot.Support.Command;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import uk.co.netbans.supportbot.Message.Messenger;
import uk.co.netbans.supportbot.NetBansBot;
import uk.co.netbans.supportbot.Music.Command.*;
import uk.co.netbans.supportbot.Support.Command.Admin.ConfigReload;
import uk.co.netbans.supportbot.Support.Command.Admin.Perm;
import uk.co.netbans.supportbot.Support.Command.Support.Help;
import uk.co.netbans.supportbot.Support.Command.Support.Ticket;

public class CommandListener extends ListenerAdapter {
    private CommandRouter router;
    private NetBansBot bot; //

    public CommandListener(NetBansBot bot) {
        this.bot = bot;
        this.router = new CommandRouter(bot, null, null);
        router.addCommand(
                new Play(),
                new Search(),
                new Queue(),
                new Skip(),
                new Current(),
                new Shuffle(),
                new Reset(),
                new Volume(),
                new Proximity(),
                new Ticket(),
                new Perm(),
                new ConfigReload(),
                new Help()
        );
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        if (!e.getMessage().getContentRaw().startsWith(bot.getCommandPrefix()) || e.getAuthor().isBot() || e.getMessage().getContentRaw().length() <= 1)
            return;

        String[] args = e.getMessage().getContentRaw().split(" ");
        args[0] = args[0].substring(1);
        if (router.onCommand(e.getMember(), e.getTextChannel(), args).equals(CommandResult.NOPERMS)) {
            bot.getMessenger().sendEmbed(e.getTextChannel(), Messenger.NO_PERMS, 10);
        }
        bot.getMessenger().delMessage(e.getTextChannel(), e.getMessageIdLong(), 5);
    }

    public CommandRouter getRouter() {
        return router;
    }
}