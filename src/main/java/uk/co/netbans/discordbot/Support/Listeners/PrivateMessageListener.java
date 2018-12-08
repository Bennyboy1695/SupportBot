package uk.co.netbans.discordbot.Support.Listeners;

import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class PrivateMessageListener extends ListenerAdapter {

    @Override
    public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
        System.out.println("Call me maybe!");
        event.getMessage().addReaction("\u274C").complete();
    }
}
