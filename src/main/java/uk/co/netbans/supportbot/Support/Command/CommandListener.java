package uk.co.netbans.supportbot.Support.Command;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import uk.co.netbans.supportbot.Message.Messenger;
import uk.co.netbans.supportbot.NetBansBot;
import uk.co.netbans.supportbot.Music.Command.*;
import uk.co.netbans.supportbot.Support.Command.Admin.*;
import uk.co.netbans.supportbot.Support.Command.Support.Help;
import uk.co.netbans.supportbot.Support.Command.Support.Ticket;

import java.util.ArrayList;

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
                new Help(),
                new Tips(),
                new Embedify(),
                new Faq()
        );
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        if (!e.getMessage().getContentRaw().startsWith(bot.getCommandPrefix()) || e.getAuthor().isBot() || e.getMessage().getContentRaw().length() <= 1)
            return;

        String[] args = quotesOrSpaceSplits(e.getMessage().getContentRaw());
        args[0] = args[0].substring(1);

        CommandResult result = router.onCommand(e.getMember(), e.getTextChannel(), args);
        if (result.equals(CommandResult.INVALIDCOMMAND)) {
            bot.getMessenger().sendEmbed(e.getTextChannel(), Messenger.INVALID_COMMAND, 10);
        }
        if (result.equals(CommandResult.NOPERMS)) {
            bot.getMessenger().sendEmbed(e.getTextChannel(), Messenger.NO_PERMS, 10);
        }
        bot.getMessenger().delMessage(e.getTextChannel(), e.getMessageIdLong(), 5);
    }

    public CommandRouter getRouter() {
        return router;
    }

    private String[] quotesOrSpaceSplits(String str) {
        str += " ";
        ArrayList<String> strings = new ArrayList<String>();
        boolean inQuote = false;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == '"' || c == ' ' && !inQuote) {
                if (c == '"')
                    inQuote = !inQuote;
                if (!inQuote && sb.length() > 0) {
                    strings.add(sb.toString());
                    sb.delete(0, sb.length());
                }
            } else
                sb.append(c);
        }
        return strings.toArray(new String[strings.size()]);
    }
}