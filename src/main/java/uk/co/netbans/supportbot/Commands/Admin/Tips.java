package uk.co.netbans.supportbot.Commands.Admin;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import uk.co.netbans.supportbot.CommandFramework.Command;
import uk.co.netbans.supportbot.CommandFramework.CommandArgs;
import uk.co.netbans.supportbot.CommandFramework.CommandCategory;
import uk.co.netbans.supportbot.NetBansBot;
import uk.co.netbans.supportbot.CommandFramework.CommandResult;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Tips {


    @Command(name = "tips", displayName = "tips", aliases = "suggestions", permission = "supportbot.command.admin.tips", usage = "tips", category = CommandCategory.ADMIN)
    public CommandResult onTips(CommandArgs args) {
        Member sender = args.getMember();
        NetBansBot bot = args.getBot();
        TextChannel channel = (TextChannel) args.getChannel();
        List<String[]> tips = new ArrayList<>();
        try {
            tips = bot.getTips();
        } catch (Exception e) {
            e.printStackTrace();
        }
        EmbedBuilder builder = new EmbedBuilder().setColor(Color.CYAN).setTitle("Tips").setDescription("This is the trigger words and what the bot will send upon receiving the trigger word!");
        for (String[] strings : tips) {
            builder.addField("**" + strings[0] + "**: ", strings[1]
                    .replaceAll("<tag>", sender.getAsMention())
                    .replaceAll("<prefix>", bot.getCommandPrefix())
                    .replaceAll("<githubissues>", "https://github.com/NetBans/NetBans/issues/new")
                    .replaceAll("<github>", "https://github.com/NetBans")
                    .replaceAll("<download>", "TODO") //TODO: download link!
                    .replaceAll("<sponge>", "TODO")
                    .replaceAll("<spigot>", "TODO")
                    .replaceAll("<velocity>", "TODO")
                    .replaceAll("<bungee>", "TODO")
                    , false);
        }

        bot.getMessenger().sendEmbed(channel, builder.build(), 30);
        return CommandResult.SUCCESS;
    }
}
