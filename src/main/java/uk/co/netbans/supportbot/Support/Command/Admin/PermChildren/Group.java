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

public class Group implements Command {

    @Override
    public CommandResult onExecute(NetBansBot bot, Member sender, TextChannel channel, String label, String[] args) {
        for (Role roles : bot.getJDA().getGuildById(Long.valueOf((String) bot.getConf().get("guildID"))).getRoles()) {
            if (roles.getName().toLowerCase().equals(args[3].toLowerCase())) {
                bot.getMessenger().sendEmbed(channel, Messenger.INCOMPATIBLE_ARG, 10);
            }
        }
        switch (args[2].toLowerCase()) {
            case "set":
                bot.getMessenger().sendEmbed(channel, Messenger.INCOMPATIBLE_ARG, 10);
                break;
            case "add":
                if (!bot.getSqlManager().permAlreadyExists(args[1], args[3])) {
                    if (bot.getSqlManager().addNewGroupPerm(args[1], args[3])) {
                        bot.getMessenger().sendEmbed(channel, new EmbedBuilder().setTitle("Successful")
                                .setColor(Color.GREEN)
                                .setDescription("Successfully added `" + args[3] + "` to group: " + args[1]).build(), 15);
                        return CommandResult.SUCCESS;
                    }
                } else {
                    bot.getMessenger().sendEmbed(channel, Messenger.PERM_ALREADY_EXIST, 10);
                }
                break;
            case "remove":
                if (bot.getSqlManager().permAlreadyExists(args[1], args[3])) {
                    if (bot.getSqlManager().addNewGroupPerm(args[1], args[3])) {
                        bot.getMessenger().sendEmbed(channel, new EmbedBuilder().setTitle("Successful")
                                .setColor(Color.GREEN)
                                .setDescription("Successfully removed `" + args[3] + "` from group: " + args[1]).build(), 15);
                        return CommandResult.SUCCESS;
                    }
                } else {
                    bot.getMessenger().sendEmbed(channel, Messenger.PERM_ALREADY_EXIST, 10);
                }
                break;
            case "adddiscordrole":
                if (bot.getSqlManager().groupAlreadyExist(args[1])) {
                    for (Role roles2 : bot.getJDA().getGuildById(Long.valueOf((String) bot.getConf().get("guildID"))).getRoles()) {
                        if (roles2.getName().toLowerCase().equals(args[3].toLowerCase())) {
                            if (bot.getSqlManager().addRoleToGroup(args[1], roles2.getIdLong())) {
                                bot.getMessenger().sendEmbed(channel, new EmbedBuilder().setTitle("Successful")
                                        .setColor(Color.GREEN)
                                        .setDescription("Successfully added `" + roles2.getName() + "` to group: " + args[1]).build(), 15);
                                return CommandResult.SUCCESS;
                            }
                        } else {
                            bot.getMessenger().sendEmbed(channel, Messenger.DISCORD_ROLE_DOESNT_EXIST, 10);
                        }
                    }
                }
                break;
            default:
                return CommandResult.INVALIDARGS;
        }
        return CommandResult.SUCCESS;
    }

    @Override
    public String name() {
        return "group";
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
