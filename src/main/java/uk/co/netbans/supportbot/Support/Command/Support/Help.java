package uk.co.netbans.supportbot.Support.Command.Support;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import uk.co.netbans.supportbot.NetBansBot;
import uk.co.netbans.supportbot.PermType;
import uk.co.netbans.supportbot.Support.Command.Command;
import uk.co.netbans.supportbot.Support.Command.CommandResult;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Help implements Command {

    private NetBansBot bot;

    @Override
    public CommandResult onExecute(NetBansBot bot, Member sender, TextChannel channel, String label, String[] args) {
        this.bot = bot;
        Collections.sort(bot.getCommandRouter().getCommands());
        if (bot.getPerms().get(PermType.ADMIN).contains(sender.getUser().getIdLong())) {
            StringBuilder adminCommands = new StringBuilder();
            StringBuilder modCommands = new StringBuilder();
            StringBuilder commands = new StringBuilder();
            List<String> temp = new ArrayList<String>();
            for (Command command : bot.getCommandRouter().getCommands()) {
                if (command.getPermission() == PermType.ADMIN) {
                    String output = command.name().substring(0, 1).toUpperCase() + command.name().substring(1);
                    adminCommands.append("**" + output + ":**" + "\n" +
                            "**Description:** " + command.desc() + "\n" +
                            "**Usage:** " + bot.getCommandPrefix() + command.usage() + "\n"+
                            "\u00A0\u00A0\u00A0\u00A0\u00A0**Perm:** " + command.getPermission().toString().toLowerCase() + "\n" + "\n");
                }
                if (command.getPermission() == PermType.MOD) {
                    String output = command.name().substring(0, 1).toUpperCase() + command.name().substring(1);
                    modCommands.append("**" + output + ":**" + "\n" +
                            "**Description:** " + command.desc() + "\n" +
                            "**Usage:** " + bot.getCommandPrefix() + command.usage() + "\n"+
                            "\u00A0\u00A0\u00A0\u00A0\u00A0**Perm:** " + command.getPermission().toString().toLowerCase() + "\n" + "\n");
                }
                if (command.getPermission() != PermType.ADMIN || command.getPermission() != PermType.MOD) {
                    System.out.println("Running ?");
                    final Integer limit = 800;
                    String output = command.name().substring(0, 1).toUpperCase() + command.name().substring(1);
                    commands.append("**" + output + ":**" + "\n" +
                            "\u00A0\u00A0\u00A0\u00A0\u00A0**Description:** " + command.desc() + "\n" +
                            "\u00A0\u00A0\u00A0\u00A0\u00A0**Usage:** " + bot.getCommandPrefix() + command.usage() + "\n" +
                            "\u00A0\u00A0\u00A0\u00A0\u00A0**Perm:** " + command.getPermission().toString().toLowerCase() + "\n" + "\n");
                    if (commands.length() > limit) {
                        temp.add(commands.toString());
                        commands.setLength(0);
                    }
                }
            }
            if (commands.length() != 0) {
                temp.add(commands.toString());
                commands.setLength(0);
            }
            for (String str : temp) {
                bot.getMessenger().sendEmbed(channel, new EmbedBuilder().setTitle("Help").setColor(Color.ORANGE).addField("Commands:", str + "\n", false).build(), 30);
            }
            return CommandResult.SUCCESS;
        } else if (bot.getPerms().get(PermType.MOD).contains(sender.getUser().getIdLong())) {
            StringBuilder modCommands = new StringBuilder();
            List<String> temp = new ArrayList<String>();
            StringBuilder commands = new StringBuilder();
            for (Command command : bot.getCommandRouter().getCommands()) {
                if (command.getPermission() == PermType.MOD) {
                    String output = command.name().substring(0, 1).toUpperCase() + command.name().substring(1);
                    modCommands.append("**" + output + ":**" + "\n" +
                            "**Description:** " + command.desc() + "\n" +
                            "**Perm Needed:**" + command.getPermission().name().toLowerCase() + "\n" +
                            "**Usage:** " + bot.getCommandPrefix() + command.usage() + "\n" +
                            "\u00A0\u00A0\u00A0\u00A0\u00A0**Perm:** " + command.getPermission().toString().toLowerCase() + "\n" + "\n");
                }
                if (command.getPermission() != PermType.ADMIN || command.getPermission() != PermType.MOD) {
                    final Integer limit = 800;
                    String output = command.name().substring(0, 1).toUpperCase() + command.name().substring(1);
                    commands.append("**" + output + ":**" + "\n" +
                            "\u00A0\u00A0\u00A0\u00A0\u00A0**Description:** " + command.desc() + "\n" +
                            "\u00A0\u00A0\u00A0\u00A0\u00A0**Usage:** " + bot.getCommandPrefix() + command.usage() + "\n" +
                            "\u00A0\u00A0\u00A0\u00A0\u00A0**Perm:** " + command.getPermission().toString().toLowerCase() + "\n" + "\n");
                        if (commands.length() > limit) {
                            temp.add(commands.toString());
                            commands.setLength(0);
                        }
                    }
            }
            if (commands.length() != 0) {
                temp.add(commands.toString());
                commands.setLength(0);
            }
            for (String str : temp) {
                bot.getMessenger().sendEmbed(channel, new EmbedBuilder().setTitle("Help").setColor(Color.ORANGE).addField("Commands:", str + "\n", false).build(), 30);
            }
        } else {
            final Integer limit = 800;
            List<String> temp = new ArrayList<String>();
            StringBuilder commands = new StringBuilder();
            for (Command command : bot.getCommandRouter().getCommands()) {
                if (command.getPermission() != PermType.ADMIN || command.getPermission() != PermType.MOD) {
                    String output = command.name().substring(0, 1).toUpperCase() + command.name().substring(1);
                    commands.append("**" + output + ":**" + "\n" +
                            "\u00A0\u00A0\u00A0\u00A0\u00A0**Description:** " + command.desc() + "\n" +
                            "\u00A0\u00A0\u00A0\u00A0\u00A0**Usage:** " + bot.getCommandPrefix() + command.usage() + "\n" + "\n");
                }
                if (commands.length() > limit) {
                    temp.add(commands.toString());
                    commands.setLength(0);
                }
            }
            for (String str : temp) {
                bot.getMessenger().sendEmbed(channel, new EmbedBuilder().setTitle("Help").setColor(Color.ORANGE).addField("Commands:", str + "\n", false).build(), 30);
            }
        }
        return CommandResult.SUCCESS;

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
        return "help";
    }

    @Override
    public String[] aliases() {
        return new String[0];
    }
}
