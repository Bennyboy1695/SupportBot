package uk.co.netbans.supportbot.Commands.Misc;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.requests.RestAction;
import org.json.simple.JSONArray;
import uk.co.netbans.supportbot.CommandFramework.Command;
import uk.co.netbans.supportbot.CommandFramework.CommandArgs;
import uk.co.netbans.supportbot.CommandFramework.CommandResult;
import uk.co.netbans.supportbot.Message.Messenger;
import uk.co.netbans.supportbot.NetBansBot;

import java.awt.*;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.zone.ZoneRulesException;

public class Timezones {

    @Command(name = "timezones", displayName = "timezones", aliases = "times", usage = "times")
    public CommandResult onExecute(CommandArgs args) {
        NetBansBot bot = args.getBot();
        JSONArray arr = (JSONArray) bot.getConf().get("timezones");


        EmbedBuilder embed = Messenger.getCommonEmbed();
        embed.setColor(new Color(1, 175, 68));

        StringBuilder sb = new StringBuilder();
        for (Object o : arr) {
            ZoneId zone;
            try {
                zone = ZoneId.of(o.toString());
            } catch (ZoneRulesException ignored) {
                System.out.println("Failed to parse zone '" + o.toString() + "'. It does not exist.");
                continue;
            }
            ZonedDateTime time = ZonedDateTime.ofInstant(Instant.now(), zone);
            sb.append("In ").append(o.toString()).append(" it is ").append(time.format(DateTimeFormatter.ofPattern("hh:mm:ss , MMM dd")).replace(",", "on")).append("\n");
        }
        embed.setDescription(sb.toString());

        bot.getMessenger().sendEmbed((TextChannel) args.getChannel(), embed.build());
        return CommandResult.SUCCESS;
    }
}
