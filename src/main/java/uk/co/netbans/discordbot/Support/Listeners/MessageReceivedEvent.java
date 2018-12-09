package uk.co.netbans.discordbot.Support.Listeners;

import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import uk.co.netbans.discordbot.NetBansBot;

public class MessageReceivedEvent extends ListenerAdapter {

    private NetBansBot bot;

    public MessageReceivedEvent(NetBansBot bot) {
        this.bot = bot;
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        //if (event.getAuthor().isBot()) {
        //
        //}
    }
}
