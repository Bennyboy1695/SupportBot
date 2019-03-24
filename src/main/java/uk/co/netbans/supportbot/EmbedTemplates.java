package uk.co.netbans.supportbot;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;

import java.awt.*;
import java.time.Instant;

public enum EmbedTemplates {
    BASE(new EmbedBuilder().setTimestamp(Instant.now())),
    ERROR(BASE.getEmbed().setColor(Color.RED)),
    SUCCESS(BASE.getEmbed().setColor(Color.GREEN)),
    PRETTY_SUCCESSFULL(BASE.getEmbed().setColor(Color.CYAN)),

    // Command
    INVALID_COMMAND(ERROR.getEmbed().setDescription("This is not a valid command!")),

    // Support
    CHANNEL_LOCKED(SUCCESS.getEmbed().setDescription("\uD83D\uDD12 Channel has been locked to just Project Leaders and the Ticket Creator!")),
    CHANNEL_UNLOCKED(SUCCESS.getEmbed().setDescription("\uD83D\uDD13 Channel has been unlocked, it is now back to default ticket perms!")),
    CHANNEL_UNLOCKED_FULL(SUCCESS.getEmbed().setDescription("\uD83D\uDD13 Channel has been unlocked, it is now open to all!")),

    // Moderation
    AMOUNT_TOO_HIGH(ERROR.getEmbed().setDescription("The max number allowed is {0}!")),
    NO_MESSAGES_FOUND(ERROR.getEmbed().setDescription("Unable to find any messages matching your query!")),
    DELETED_X_MESSAGES(SUCCESS.getEmbed().setTitle("Success!").setDescription("Deleted {0} messages!")),
    DELETED_X_MESSAGES_FROM(SUCCESS.getEmbed().setTitle("Success!").setDescription("Deleted {0} messages from {1}!")),

    // Music
    //todo there are a bunch more of these which are required.
    ;
    private final EmbedBuilder embed;
    EmbedTemplates(EmbedBuilder embed) {
        this.embed = embed;
    }
    public EmbedBuilder getEmbed() {
        return new EmbedBuilder(embed);
    }
    public EmbedBuilder getEmbed(Object... replacements) {
        EmbedBuilder embed = getEmbed();
        String description = embed.getDescriptionBuilder().toString();
        for (int i = 0; i < replacements.length; i++)
            description = description.replace("{" + i + "}", replacements[i].toString());
        return embed.setDescription(description);
    }
    public MessageEmbed getBuilt() {
        return getEmbed().build();
    }
}
