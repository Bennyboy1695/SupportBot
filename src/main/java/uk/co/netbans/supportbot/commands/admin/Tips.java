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
import java.util.ArrayList;
import java.util.List;

@Command(label = {"tips","suggestions"}, permission = Permission.MANAGE_SERVER)
public class Tips {


    @Execute
    public CommandResult onTips(Member member, TextChannel channel, Message message, String label, List<String> args, SupportBot bot) {
        List<String[]> tips = new ArrayList<>();
        try {
            //tips = bot.getTips();
        } catch (Exception e) {
            e.printStackTrace();
        }
        EmbedBuilder builder = new EmbedBuilder().setColor(Color.CYAN).setTitle("Tips").setDescription("This is the trigger words and what the bot will send upon receiving the trigger word!");
        for (String[] strings : tips) {
            builder.addField("**" + strings[0] + "**: ", strings[1]
                    .replaceAll("<tag>", member.getAsMention())
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
        return CommandResult.success();
    }
}
