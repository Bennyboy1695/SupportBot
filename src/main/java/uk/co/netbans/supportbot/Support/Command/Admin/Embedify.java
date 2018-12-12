package uk.co.netbans.supportbot.Support.Command.Admin;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import uk.co.netbans.supportbot.NetBansBot;
import uk.co.netbans.supportbot.PermType;
import uk.co.netbans.supportbot.Support.Command.Command;
import uk.co.netbans.supportbot.Support.Command.CommandResult;

import java.awt.*;

public class Embedify implements Command {


    @Override
    public CommandResult onExecute(NetBansBot bot, Member sender, TextChannel channel, String label, String[] args) {
        if (args.length > 2) {
            EmbedBuilder embed = new EmbedBuilder();
            embed.setColor(new Color(247, 207, 13));
            embed.setThumbnail("https://i.imgur.com/sBIoRP4.png");

            for (String str : args) {
                System.out.println(str);
            }

            if (!args[1].startsWith("`http")) {
                embed.setTitle(args[0].replaceAll("\"", ""));
            } else {
                embed.setTitle(args[0].replaceAll("\"", ""), args[1].replaceAll("`", ""));
            }
            if (args.length == 4) {
                embed.addField("**Username:**", args[2].replaceAll("`", ""), true);
                embed.addField("**Password:**", args[3].replaceAll("`", ""), true);
            } else {
                if (!args[1].contains("Token")) {
                    embed.addField("**Username:**", args[1].replaceAll("`", ""), true);
                    embed.addField("**Password:**", args[2].replaceAll("`", ""), true);
                } else {
                    embed.addField("**" + args[1].replaceAll("`", "") + "**:", args[2].replaceAll("`", ""), false);
                }
            }
            channel.sendMessage(embed.build()).complete();
        }
        return CommandResult.SUCCESS;
    }

    @Override
    public String name() {
        return "embedify";
    }

    @Override
    public String desc() {
        return "Creates a nice embed! Put args inside ` to have them multi word!";
    }

    @Override
    public String usage() {
        return "embed <args>";
    }

    @Override
    public String[] aliases() {
        return new String[]{"embed"};
    }

    @Override
    public PermType getPermission() {
        return PermType.ADMIN;
    }
}
