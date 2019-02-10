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

    @Command(name = "time", displayName = "time", aliases = "times,timezones", usage = "time")
    public CommandResult onExecute(CommandArgs args) {
        NetBansBot bot = args.getBot();
        JSONArray arr = (JSONArray) bot.getConf().get("timezones");
        String[] arguments = args.getArgs();

        EmbedBuilder embed = Messenger.getCommonEmbed();
        embed.setColor(new Color(1, 175, 68));


        if (arguments.length == 0) {
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
                sb.append(o.toString().replaceAll("/.*", "")).append(": ").append(time.format(DateTimeFormatter.ofPattern("kk:mmzzz , EEE dd MMM")).replace(",", "on")).append("\n");
            }
            embed.setDescription(sb.toString());
        } else if (arguments.length == 2) {
            switch (arguments[0]) {
                case "add":
                    if (!arr.contains(arguments[1])) {
                        try {
                            ZoneId.of(arguments[1]);
                            arr.add(arguments[1]);
                            embed.setDescription("Added timezone field: " + arguments[1]);
                        } catch (Exception ignored) {
                            embed.setDescription("Invalid timezone field!");
                        }
                    } else
                        embed.setDescription("Timezone already added!");
                    break;
                case "remove":
                    if (arr.contains(arguments[1])) {
                        arr.remove(arguments[1]);
                        embed.setDescription("Removed timezone!");
                    } else
                        embed.setDescription("The target was not enabled previously.");
                    break;
            }
            bot.saveConfig();
            bot.reloadConfig();
        }

        bot.getMessenger().sendEmbed((TextChannel) args.getChannel(), embed.build());
        return CommandResult.SUCCESS;
    }
}
