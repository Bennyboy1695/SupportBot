package uk.co.netbans.supportbot.Support.Command.Admin;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import org.json.simple.parser.ParseException;
import uk.co.netbans.supportbot.NetBansBot;
import uk.co.netbans.supportbot.PermType;
import uk.co.netbans.supportbot.Support.Command.Command;
import uk.co.netbans.supportbot.Support.Command.CommandResult;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Tips implements Command {


    @Override
    public CommandResult onExecute(NetBansBot bot, Member sender, TextChannel channel, String label, String[] args) {
        List<String[]> tips = new ArrayList<>();
        try {
            tips = bot.getTips();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
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

    @Override
    public String name() {
        return "tips";
    }

    @Override
    public String desc() {
        return "Gives you a list of all the words that can trigger tips!";
    }

    @Override
    public String usage() {
        return "tips";
    }

    @Override
    public String[] aliases() {
        return new String[]{"suggestions"};
    }

    @Override
    public PermType getPermission() {
        return PermType.ADMIN;
    }
}
