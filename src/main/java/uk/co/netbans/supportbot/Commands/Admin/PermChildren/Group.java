package uk.co.netbans.supportbot.Commands.Admin.PermChildren;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import uk.co.netbans.supportbot.CommandFramework.Command;
import uk.co.netbans.supportbot.CommandFramework.CommandArgs;
import uk.co.netbans.supportbot.CommandFramework.CommandCategory;
import uk.co.netbans.supportbot.Message.Messenger;
import uk.co.netbans.supportbot.NetBansBot;
import uk.co.netbans.supportbot.CommandFramework.CommandResult;

import java.awt.*;

public class Group{

    @Command(name = "perm~group", displayName = "group", permission = "supportbot.command.admin.perm.group", usage = "perm group <groupname> <add|remove|adddiscordrole> <perm|role>", category = CommandCategory.ADMIN)
    public CommandResult onPermGroup(CommandArgs commandArgs) {
        NetBansBot bot = commandArgs.getBot();
        TextChannel channel = (TextChannel) commandArgs.getChannel();
        String[] args = commandArgs.getArgs();
        if (args.length <= 4)
            return CommandResult.INVALIDARGS;
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
                    bot.getMessenger().sendEmbed(channel, Messenger.GROUP_HAS_PERM, 10);
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
                    bot.getMessenger().sendEmbed(channel, Messenger.GROUP_HAS_PERM, 10);
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
}
