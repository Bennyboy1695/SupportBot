package uk.co.netbans.supportbot.commands.admin;

import me.bhop.bjdautilities.command.annotation.Command;
import me.bhop.bjdautilities.command.annotation.Execute;
import me.bhop.bjdautilities.command.result.CommandResult;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import uk.co.netbans.supportbot.SupportBot;

import java.awt.*;
import java.util.List;

@Command(label = {"embed", "embedify"}, permission = Permission.ADMINISTRATOR)
public class Embedify {

    @Execute
    public CommandResult onEmbedify(Member member, TextChannel channel, Message message, String label, List<String> args, SupportBot bot) {
        if (args.size() > 2) {
            EmbedBuilder embed = new EmbedBuilder();
            embed.setColor(new Color(247, 207, 13));
            embed.setThumbnail("https://i.imgur.com/sBIoRP4.png");

            for (String str : args) {
                System.out.println(str);
            }

            if (!args.get(1).startsWith("`http")) {
                embed.setTitle(args.get(0).replaceAll("\"", ""));
            } else {
                embed.setTitle(args.get(0).replaceAll("\"", ""), args.get(1).replaceAll("`", ""));
            }
            if (args.size() == 4) {
                embed.addField("**Username:**", args.get(2).replaceAll("`", ""), true);
                embed.addField("**Password:**", args.get(3).replaceAll("`", ""), true);
            } else {
                if (!args.get(1).contains("Token") || !args.get(1).contains("token")) {
                    embed.addField("**Username:**", args.get(1).replaceAll("`", ""), true);
                    embed.addField("**Password:**", args.get(2).replaceAll("`", ""), true);
                } else {
                    embed.addField("**" + args.get(1).replaceAll("`", "") + "**:", args.get(2).replaceAll("`", ""), false);
                }
            }
            channel.sendMessage(embed.build()).complete();
        }
        return CommandResult.invalidArguments();
    }
}
