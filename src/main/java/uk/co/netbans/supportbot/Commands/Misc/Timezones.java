package uk.co.netbans.supportbot.Commands.Misc;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import me.bhop.bjdautilities.command.annotation.Command;
import me.bhop.bjdautilities.command.annotation.Execute;
import me.bhop.bjdautilities.command.result.CommandResult;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import uk.co.netbans.supportbot.EmbedTemplates;
import uk.co.netbans.supportbot.NetBansBot;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.zone.ZoneRulesException;
import java.util.List;

@Command(label = {"times", "time", "timezone", "timezones"})
public class Timezones {

    @Execute
    public CommandResult onExecute(Member member, TextChannel channel, Message message, String label, List<String> args, NetBansBot bot) {
        JsonArray arr = bot.getConfig().getConfigValue("timezones");
        String[] arguments = args.toArray(new String[0]);

        EmbedBuilder embed = EmbedTemplates.PRETTY_SUCCESSFULL.getEmbed();


        if (arguments.length == 0) {
            StringBuilder sb = new StringBuilder();
            for (JsonElement o : arr) {
                ZoneId zone;
                try {
                    zone = ZoneId.of(o.getAsString());
                } catch (ZoneRulesException ignored) {
                    bot.getLogger().error("Failed to parse zone '" + o.getAsString() + "'. It does not exist.");
                    continue;
                }
                ZonedDateTime time = ZonedDateTime.ofInstant(Instant.now(), zone);
                sb.append(o.getAsString().replaceAll("/.*", "")).append(": ").append(time.format(DateTimeFormatter.ofPattern("kk:mmzzz , EEE dd MMM")).replace(",", "on")).append("\n");
            }
            embed.setDescription(sb.toString());
        } else if (arguments.length == 2) {
            switch (arguments[0]) {
                case "add":

                    if (!arr.contains(new JsonPrimitive(arguments[1]))) {
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
                    if (arr.contains(new JsonPrimitive(arguments[1]))) {
                        arr.remove(new JsonPrimitive(arguments[1]));
                        embed.setDescription("Removed timezone!");
                    } else
                        embed.setDescription("The target was not enabled previously.");
                    break;
            }
            bot.getConfig().saveConfig();
            bot.reloadConfig();
        }

        bot.getMessenger().sendEmbed(channel, embed.build());
        return CommandResult.success();
    }
}
