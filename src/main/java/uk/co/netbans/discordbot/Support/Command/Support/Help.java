package uk.co.netbans.discordbot.Support.Command.Support;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import uk.co.netbans.discordbot.NetBansBot;
import uk.co.netbans.discordbot.PermType;
import uk.co.netbans.discordbot.Support.Command.Command;
import uk.co.netbans.discordbot.Support.Command.CommandResult;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Help implements Command {

    private NetBansBot bot;

    @Override
    public CommandResult onExecute(NetBansBot bot, Member sender, TextChannel channel, String label, String[] args) {
        this.bot = bot;
        if (bot.getPerms().get(PermType.ADMIN).contains(sender.getUser().getIdLong())) {
            StringBuilder adminCommands = new StringBuilder();
            StringBuilder modCommands = new StringBuilder();
            StringBuilder defaultCommands = new StringBuilder();
            for (Command command : bot.getCommandRouter().getCommands()) {
                if (command.getPermission() == PermType.ADMIN) {
                    String output = command.name().substring(0, 1).toUpperCase() + command.name().substring(1);
                    adminCommands.append("**" + output + ":**" + "\n" +
                            "**Description:** " + command.desc() + "\n" +
                            "**Usage:** " + bot.getCommandPrefix() + command.usage() + "\n"+ "\n");
                }
                if (command.getPermission() == PermType.MOD) {
                    String output = command.name().substring(0, 1).toUpperCase() + command.name().substring(1);
                    modCommands.append("**" + output + ":**" + "\n" +
                            "**Description:** " + command.desc() + "\n" +
                            "**Usage:** " + bot.getCommandPrefix() + command.usage() + "\n"+ "\n");
                }
                if (command.getPermission() != PermType.ADMIN || command.getPermission() != PermType.MOD) {
                    String output = command.name().substring(0, 1).toUpperCase() + command.name().substring(1);
                    defaultCommands.append("**" + output + ":**" + "\n" +
                            "**Description:** " + command.desc() + "\n" +
                            "**Usage:** " + bot.getCommandPrefix() + command.usage() + "\n"+ "\n");
                }
            }
            bot.getMessenger().sendEmbed(channel, new EmbedBuilder()
                    .setTitle("Command Help")
                    .addField("Admin Commands:", adminCommands.toString(), false)
                    .addField("Mod Commands:", modCommands.toString(), false)
                    .addField("Default Commands:", defaultCommands.toString(), false)
                    .setColor(Color.ORANGE).build());
            return CommandResult.SUCCESS;
        } else if (bot.getPerms().get(PermType.MOD).contains(sender.getUser().getIdLong())) {
            StringBuilder modCommands = new StringBuilder();
            StringBuilder defaultCommands = new StringBuilder();
            for (Command command : bot.getCommandRouter().getCommands()) {
            if (command.getPermission() == PermType.MOD) {
                String output = command.name().substring(0, 1).toUpperCase() + command.name().substring(1);
                modCommands.append("**" + output + ":**" + "\n" +
                        "**Description:** " + command.desc() + "\n" +
                        "**Perm Needed:**" + command.getPermission().name().toLowerCase() + "\n" +
                        "**Usage:** " + bot.getCommandPrefix() + command.usage() + "\n"+ "\n");
            }
            if (command.getPermission() != PermType.ADMIN || command.getPermission() != PermType.MOD) {
                String output = command.name().substring(0, 1).toUpperCase() + command.name().substring(1);
                defaultCommands.append("**" + output + ":**" + "\n" +
                        "**Description:** " + command.desc() + "\n" +
                        "**Usage:** " + bot.getCommandPrefix() + command.usage() + "\n"+ "\n");
            }
            }
            bot.getMessenger().sendEmbed(channel, new EmbedBuilder()
                    .setTitle("Command Help")
                    .addField("Mod Commands:", modCommands.toString(), false)
                    .addField("Default Commands:", defaultCommands.toString(), false)
                    .setColor(Color.ORANGE).build());
            return CommandResult.SUCCESS;
        } else {
            final Integer limit = 800;
            List<String> temp = new ArrayList<String>();
            StringBuilder commands = new StringBuilder();
            for (Command command : bot.getCommandRouter().getCommands()) {
                if (command.getPermission() != PermType.ADMIN || command.getPermission() != PermType.MOD) {
                    String output = command.name().substring(0, 1).toUpperCase() + command.name().substring(1);
                    commands.append("**" + output + ":**" + "\n" +
                            "**Description:** " + command.desc() + "\n" +
                                    "**Usage:** " + bot.getCommandPrefix() + command.usage() + "\n");
                    if (commands.length() > limit) {
                        temp.add(commands.toString());
                        commands = new StringBuilder();
                    }
                }
                for (String str : temp) {
                    System.out.println(str);
                    //bot.getMessenger().sendEmbed(channel, new EmbedBuilder().setTitle("Help").setColor(Color.ORANGE).addField("Commands:", str, false).build(), 20);
                }
            }

        return CommandResult.SUCCESS;
        }

    }

    @Override
    public String name() {
        return "help";
    }

    @Override
    public String desc() {
        return "Sends a list of commands available to you!";
    }

    @Override
    public String usage() {
        return "help [page]";
    }

    @Override
    public String[] aliases() {
        return new String[0];
    }
}
