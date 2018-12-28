package uk.co.netbans.supportbot.Support.Command.Admin.PermChildren;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import uk.co.netbans.supportbot.Message.Messenger;
import uk.co.netbans.supportbot.NetBansBot;
import uk.co.netbans.supportbot.Support.Command.Command;
import uk.co.netbans.supportbot.Support.Command.CommandResult;

import java.awt.*;

public class CreateGroup implements Command {

    @Override
    public CommandResult onExecute(NetBansBot bot, Member sender, TextChannel channel, String label, String[] args) {
        if (args.length >= 1) {
            if (!bot.getSqlManager().groupAlreadyExist(args[1])) {
                if (args.length == 2) {
                    if (bot.getSqlManager().addNewGroup(args[1])) {
                        bot.getMessenger().sendEmbed(channel, new EmbedBuilder().setTitle("Successful")
                                .setColor(Color.GREEN)
                                .setDescription("Successfully created group: " + args[1]).build(), 15);
                        return CommandResult.SUCCESS;
                    }
                } else if (args.length == 3) {
                    if (bot.getSqlManager().groupAlreadyExist(args[2])) {
                        if (bot.getSqlManager().addNewGroup(args[1], args[2])) {
                            bot.getMessenger().sendEmbed(channel, new EmbedBuilder().setTitle("Successful")
                                    .setColor(Color.GREEN)
                                    .setDescription("Successfully created group: " + args[1] + " with child group: " + args[2]).build(), 15);
                            return CommandResult.SUCCESS;
                        }
                    } else {
                        for (Role roles : bot.getJDA().getGuildById(Long.valueOf((String) bot.getConf().get("guildID"))).getRoles()) {
                            if (roles.getName().toLowerCase().equals(args[2].toLowerCase())) {
                                if (bot.getSqlManager().addNewGroup(args[1], roles.getIdLong())) {
                                    bot.getMessenger().sendEmbed(channel, new EmbedBuilder().setTitle("Successful")
                                            .setColor(Color.GREEN)
                                            .setDescription("Successfully created group: " + args[1] + " with linked Discord role: " + roles.getName()).build(), 15);
                                    return CommandResult.SUCCESS;
                                }
                            } else {
                                bot.getMessenger().sendEmbed(channel, Messenger.INCOMPATIBLE_ARG, 10);
                            }
                        }
                    }
                } else if (args.length == 4) {
                    for (Role roles : bot.getJDA().getGuildById(Long.valueOf((String) bot.getConf().get("guildID"))).getRoles()) {
                        if (roles.getName().toLowerCase().equals(args[3].toLowerCase())) {
                            if (bot.getSqlManager().addNewGroup(args[1], args[2], roles.getIdLong())) {
                                bot.getMessenger().sendEmbed(channel, new EmbedBuilder().setTitle("Successful")
                                        .setColor(Color.GREEN)
                                        .setDescription("Successfully created group: " + args[1] + " with child group: " + args[2] + " and linked Discord role: " + roles.getName()).build(), 15);
                                return CommandResult.SUCCESS;
                            }
                        }
                    }
                }
            } else {
                bot.getMessenger().sendEmbed(channel, Messenger.GROUP_ALREADY_EXIST, 10);
            }
        }
        return CommandResult.SUCCESS;
    }

    @Override
    public String name() {
        return "creategroup";
    }

    @Override
    public String desc() {
        return "";
    }

    @Override
    public String usage() {
        return "";
    }

    @Override
    public String[] aliases() {
        return new String[0];
    }
}
