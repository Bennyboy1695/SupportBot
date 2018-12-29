package uk.co.netbans.supportbot.Support.Command.Support;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import uk.co.netbans.supportbot.BenCMDFramework.Command;
import uk.co.netbans.supportbot.BenCMDFramework.CommandArgs;
import uk.co.netbans.supportbot.NetBansBot;
import uk.co.netbans.supportbot.BenCMDFramework.CommandResult;


public class Help {

    @Command(name = "help")
    public CommandResult onExecute(CommandArgs commandArgs) {
        NetBansBot bot = commandArgs.getBot();
        String[] args = commandArgs.getArgs();
        Member sender = commandArgs.getMember();
        TextChannel channel = (TextChannel) commandArgs.getChannel();
        /* TODO: Needs redesigning for the new CommandFramework and the new perm system!
        Collections.sort(bot.getCommandRouter().getCommands());
        if (bot.getPerms().get(PermType.ADMIN).contains(sender.getUser().getIdLong())) {
            StringBuilder adminCommands = new StringBuilder();
            StringBuilder modCommands = new StringBuilder();
            StringBuilder commands = new StringBuilder();
            List<String> temp = new ArrayList<String>();
            for (Command command : bot.getFramework().getCommandMap().get()) {
                if (command.permission() == PermType.ADMIN) {
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
        */
        return CommandResult.SUCCESS;

    }
}
