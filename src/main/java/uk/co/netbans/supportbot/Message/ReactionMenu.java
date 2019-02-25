package uk.co.netbans.supportbot.Message;

import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.core.events.message.guild.react.GuildMessageReactionRemoveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class ReactionMenu extends ListenerAdapter {
    private final EditableMessage message;
    private final NewMessenger messenger;

    public ReactionMenu(EditableMessage message, NewMessenger messenger) {
        this.message = message;
        this.messenger = messenger;
    }


    @Override
    public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event) {
        if (message.getId() != event.getMessageIdLong())
            return;


    }

    @Override
    public void onGuildMessageReactionRemove(GuildMessageReactionRemoveEvent event) {
        super.onGuildMessageReactionRemove(event);
    }

    public static class Builder {
        private final MessageBuilder message = new MessageBuilder();

        public Builder setMessage(String message) {
            this.message.append(message);
            return this;
        }

        public Builder setEmbed(MessageEmbed embed) {
            message.setEmbed(embed);
            return this;
        }
    }
}
