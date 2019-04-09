package uk.co.netbans.supportbot.support.listeners;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.json.JSONObject;
import uk.co.netbans.supportbot.SupportBot;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class SupportCategoryListener extends ListenerAdapter {

    private SupportBot bot;

    public SupportCategoryListener(SupportBot bot) {
        this.bot = bot;
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("[dd/MM/YY HH:mm]");
        Guild guild = event.getGuild();
        if (String.valueOf(event.getChannel().getParent().getIdLong()) == bot.getMongoRequestRegistry().getGuildTicketSettings(guild.getIdLong()).get("categoryId")) {
            bot.getMongoRequestRegistry().addMessageToMembersMessages(event.getMember(), event.getMessage());
            /*Path logDirectory = null;
                try {
                    logDirectory = bot.getDirectory().resolve("logs").resolve(String.valueOf(guild.getIdLong()));
                    if (!Files.exists(bot.getDirectory().resolve("logs").resolve(String.valueOf(guild.getIdLong())).resolve(event.getChannel().getName() + ".log"))) {
                        Files.createFile(logDirectory.resolve(event.getChannel().getName() + ".log"));
                        logDirectory = bot.getDirectory().resolve("logs").resolve(String.valueOf(guild.getIdLong()));
                    }
                    StringBuilder content = new StringBuilder();
                    content.append("[").append(OffsetDateTime.now().format(format)).append("] ");
                    if (!event.getMessage().getEmbeds().isEmpty()) {
                        for (MessageEmbed embed : event.getMessage().getEmbeds()) {
                            content.append(event.getMember().getEffectiveName()).append(": ").append("Embed").append(embed.toJSONObject());
                        }
                    } else {
                        if (!event.getMessage().getMentionedMembers().isEmpty()) {
                            String message = event.getMessage().getContentRaw();
                            for (Member mention : event.getMessage().getMentionedMembers())
                                message = message.replace(mention.getAsMention(), mention.getEffectiveName());
                            content.append(event.getMember().getEffectiveName()).append(": ").append(message);
                        } else {
                            content.append(event.getMember().getEffectiveName()).append(": ").append(event.getMessage().getContentRaw());
                        }
                    }
                    Files.write(logDirectory.resolve(event.getChannel().getName() + ".log"), (content.toString() + "\n").getBytes(), StandardOpenOption.APPEND);
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
        }
    }
}
