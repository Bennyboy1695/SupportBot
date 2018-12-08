package uk.co.netbans.discordbot.Support.Command;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import uk.co.netbans.discordbot.NetBansBot;
import uk.co.netbans.discordbot.Music.Command.*;
import uk.co.netbans.discordbot.Support.Command.Support.Ticket;

public class CommandListener extends ListenerAdapter {
    private CommandRouter main;

    public CommandListener(NetBansBot bot) {
        this.main = new CommandRouter(bot, null, null);
        main.addCommand(
                new Play(),
                new Search(),
                new Queue(),
                new Skip(),
                new Current(),
                new Shuffle(),
                new Reset(),
                new Volume(),
                new Proximity(),
                new Ticket()
        );
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        if (!e.getMessage().getContentRaw().startsWith("!") || e.getAuthor().isBot() || e.getMessage().getContentRaw().length() <= 1)
            return;

        String[] args = e.getMessage().getContentRaw().split(" ");
        args[0] = args[0].substring(1);
        main.onCommand(e.getMember(), e.getTextChannel(), args);
    }
}
