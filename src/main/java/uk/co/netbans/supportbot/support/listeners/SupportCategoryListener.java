package uk.co.netbans.supportbot.support.listeners;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import uk.co.netbans.supportbot.NetBansBot;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class SupportCategoryListener extends ListenerAdapter {

    private NetBansBot bot;

    public SupportCategoryListener(NetBansBot bot) {
        this.bot = bot;
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("[dd/MM/YY HH:mm]");
        if (event.getChannel().getParent().getIdLong() == Long.valueOf((String) bot.getConfig().getConfigValue("category").getAsString()) || event.getChannel().getName().contains("manual")) {
            if (event.getChannel().getIdLong() != Long.valueOf((String) bot.getConfig().getConfigValue("logChannelID").getAsString())) {
                try {
                    if (!Files.exists(bot.getLogDirectory().resolve(event.getChannel().getName() + ".log"))) {
                        Files.createFile(bot.getLogDirectory().resolve(event.getChannel().getName() + ".log"));
                    }
                    String content = "";
                    if (!event.getMessage().getMentionedMembers().isEmpty()) {
                        String message = event.getMessage().getContentRaw();
                        for (Member mention : event.getMessage().getMentionedMembers())
                            message = message.replace(mention.getAsMention(), mention.getEffectiveName());
                        content = ("[" + OffsetDateTime.now().format(format) + "] " + event.getMember().getEffectiveName() + ": " +  message);
                    } else {
                        content = ("[" + OffsetDateTime.now().format(format) + "] " + event.getMember().getEffectiveName() + ": " + event.getMessage().getContentRaw());
                    }
                    Files.write(bot.getLogDirectory().resolve(event.getChannel().getName() + ".log"), (content + "\n").getBytes(), StandardOpenOption.APPEND);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
