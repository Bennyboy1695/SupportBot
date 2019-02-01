package uk.co.netbans.supportbot.Commands.Support;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.TextChannel;
import uk.co.netbans.supportbot.CommandFramework.Command;
import uk.co.netbans.supportbot.CommandFramework.CommandArgs;
import uk.co.netbans.supportbot.CommandFramework.CommandCategory;
import uk.co.netbans.supportbot.NetBansBot;
import uk.co.netbans.supportbot.CommandFramework.CommandResult;

import java.awt.*;
import java.lang.reflect.Method;
import java.util.*;
import java.util.List;


public class Help {

    @Command(name = "help", displayName = "help", usage = "help")
    public CommandResult onExecute(CommandArgs commandArgs) {
        NetBansBot bot = commandArgs.getBot();
        String[] args = commandArgs.getArgs();
        Member sender = commandArgs.getMember();
        TextChannel channel = (TextChannel) commandArgs.getChannel();

        return CommandResult.SUCCESS;
    }

    public static MessageEmbed helpPages(NetBansBot bot, int page) {
        EmbedBuilder embed = new EmbedBuilder().setTitle("Command Help!").setColor(Color.CYAN);
        List<Command> commandsList = new ArrayList<Command>();
        bot.getFramework().getCommandMap().forEach((x, z) -> {
            Method method = bot.getFramework().getCommandMap().get(x).getKey();
            Command command = method.getAnnotation(Command.class);
            if (x.equals(Arrays.toString(command.aliases()).replace("[", "").replace("]", "")))
                return; //Dirty way to only do each command once as some have only one alias!
            if (x.contains("~")) return;
            commandsList.add(command);
        });
        commandsList.sort(Comparator.comparing(Command::name));
        switch (page) {
            case 1:
                embed.setDescription("Help Page 1");
                for (Command cmd : commandsList) {
                    if (cmd.category() == CommandCategory.DEFAULT || cmd.category() == CommandCategory.MUSIC) {
                        StringBuilder commands = new StringBuilder();
                        String output = cmd.name().substring(0, 1).toUpperCase() + cmd.name().substring(1);
                        commands.append("**" + output + ":**" + "\n" +
                                "\u00A0\u00A0**Aliases:** " + Arrays.toString(cmd.aliases()).replace("[", "").replace("]", "") + "\n" +
                                "\u00A0\u00A0**Usage:** " + bot.getCommandPrefix() + cmd.usage() + "\n" +
                                "\u00A0\u00A0**Category:** " + cmd.category() + "\n" +
                                "\u00A0\u00A0**Perm:** `" + cmd.permission().toLowerCase() + "`\n" + "\n");
                        embed.addField("", commands.toString(), false);
                    }
                }
                return embed.build();
            case 2:
                embed.setDescription("Help Page 2");
                for (Command cmd : commandsList) {
                    if (cmd.category() == CommandCategory.MODERATION) {
                        StringBuilder commands = new StringBuilder();
                        String output = cmd.name().substring(0, 1).toUpperCase() + cmd.name().substring(1);
                        commands.append("**" + output + ":**" + "\n" +
                                "\u00A0\u00A0**Aliases:** " + Arrays.toString(cmd.aliases()).replace("[", "").replace("]", "") + "\n" +
                                "\u00A0\u00A0**Usage:** " + bot.getCommandPrefix() + cmd.usage() + "\n" +
                                "\u00A0\u00A0**Category:** " + cmd.category() + "\n" +
                                "\u00A0\u00A0**Perm:** `" + cmd.permission().toLowerCase() + "`\n" + "\n");
                        embed.addField("", commands.toString(), false);
                    }
                }
                return embed.build();
            case 3:
                embed.setDescription("Help Page 3");
                for (Command cmd : commandsList) {
                    if (cmd.category() == CommandCategory.ADMIN) {
                        StringBuilder commands = new StringBuilder();
                        String output = cmd.name().substring(0, 1).toUpperCase() + cmd.name().substring(1);
                        commands.append("**" + output + ":**" + "\n" +
                                "\u00A0\u00A0**Aliases:** " + Arrays.toString(cmd.aliases()).replace("[", "").replace("]", "") + "\n" +
                                "\u00A0\u00A0**Usage:** " + bot.getCommandPrefix() + cmd.usage() + "\n" +
                                "\u00A0\u00A0**Category:** " + cmd.category() + "\n" +
                                "\u00A0\u00A0**Perm:** `" + cmd.permission().toLowerCase() + "`\n" + "\n");
                        embed.addField("", commands.toString(), false);
                    }
                }
                return embed.build();
        }
        return null;
    }
}
