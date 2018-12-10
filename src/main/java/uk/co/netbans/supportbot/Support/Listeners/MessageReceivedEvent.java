package uk.co.netbans.supportbot.Support.Listeners;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import uk.co.netbans.supportbot.NetBansBot;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class MessageReceivedEvent extends ListenerAdapter {

    private NetBansBot bot;

    public MessageReceivedEvent(NetBansBot bot) {
        this.bot = bot;
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("[dd/MM/YY HH:mm]");
        if (event.getChannel().getParent().getIdLong() == Long.valueOf((String) bot.getConf().get("category"))) {
            if (event.getChannel().getIdLong() != Long.valueOf((String) bot.getConf().get("logChannelID"))) {
                try {
                    if (!Files.exists(bot.getLogDirectory().resolve(event.getChannel().getName() + ".log"))) {
                        Files.createFile(bot.getLogDirectory().resolve(event.getChannel().getName() + ".log"));
                    }
                    String content = "";
                    if (!event.getMessage().getMentionedMembers().isEmpty()) {
                        String message = event.getMessage().getContentRaw();
                        for (Member mention : event.getMessage().getMentionedMembers())
                            message = message.replace(mention.getAsMention(), mention.getEffectiveName());
                        content = ("[" + OffsetDateTime.now().format(format) + "] " + message);
                    } else {
                        content = ("[" + OffsetDateTime.now().format(format) + "] " + event.getMessage().getContentRaw());
                    }
                    Files.write(bot.getLogDirectory().resolve(event.getChannel().getName() + ".log"), (content + "\n").getBytes(), StandardOpenOption.APPEND);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
