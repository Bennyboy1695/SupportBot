package uk.co.netbans.supportbot.Support.Listeners;

import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import uk.co.netbans.supportbot.NetBansBot;

public class SuggestionListener extends ListenerAdapter {

    private NetBansBot bot;

    public SuggestionListener(NetBansBot bot) {
        this.bot = bot;
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

    }
}
