package uk.co.netbans.discordbot.Support.Command;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import uk.co.netbans.discordbot.Message.Messenger;
import uk.co.netbans.discordbot.NetBansBot;
import uk.co.netbans.discordbot.Music.Command.*;
import uk.co.netbans.discordbot.Support.Command.Admin.Perm;
import uk.co.netbans.discordbot.Support.Command.Support.Ticket;

public class CommandListener extends ListenerAdapter {
    private CommandRouter main;
    private NetBansBot bot; //

    public CommandListener(NetBansBot bot) {
        this.bot = bot;
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
                new Ticket(),
                new Perm()
        );
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        if (!e.getMessage().getContentRaw().startsWith("!") || e.getAuthor().isBot() || e.getMessage().getContentRaw().length() <= 1)
            return;

        String[] args = e.getMessage().getContentRaw().split(" ");
        args[0] = args[0].substring(1);
        switch (main.onCommand(e.getMember(), e.getTextChannel(), args)) {
            case NOPERMS:
                bot.getMessenger().sendEmbed(e.getTextChannel(), Messenger.NO_PERMS, 10);
                break;
            case INVALIDARGS:
                bot.getMessenger().sendEmbed(e.getTextChannel(), Messenger.INVALID_ARGS, 10);
                break;
        }

        bot.getMessenger().delMessage(e.getTextChannel(), e.getMessageIdLong(), 2);
    }
}
