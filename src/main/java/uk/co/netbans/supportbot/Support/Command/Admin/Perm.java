package uk.co.netbans.supportbot.Support.Command.Admin;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import uk.co.netbans.supportbot.Message.Messenger;
import uk.co.netbans.supportbot.PermType;
import uk.co.netbans.supportbot.Support.Command.Command;
import uk.co.netbans.supportbot.Support.Command.CommandResult;
import uk.co.netbans.supportbot.NetBansBot;

import java.awt.*;


public class Perm implements Command {

    @Override
    public CommandResult onExecute(NetBansBot bot, Member sender, TextChannel channel, String label, String[] args) {
        if (args.length >= 1) {
            if (args[0].toLowerCase().equals("list")) {
                if (args.length == 2) {
                    if (bot.getSqlManager().groupAlreadyExist(args[1])) {
                        EmbedBuilder builder = new EmbedBuilder().setColor(Color.CYAN).setTitle(args[1] + "'s Perm List");
                        StringBuilder perms = new StringBuilder();
                        for (String str : bot.getSqlManager().getGroupsPerms(args[1])) {
                            perms.append("`" + str + "`\n");
                        }
                        builder.addField("", perms.toString(), false);
                        bot.getMessenger().sendEmbed(channel, builder.build(), 30);
                        return CommandResult.SUCCESS;
                    } else if (args[1].toLowerCase().equals("groups")) {
                        EmbedBuilder builder = new EmbedBuilder().setColor(Color.CYAN).setTitle("Groups List");
                        StringBuilder groupsList = new StringBuilder();
                        for (String str : bot.getSqlManager().getGroups()) {
                            groupsList.append("`" + str + "`\n");
                        }
                        bot.getMessenger().sendEmbed(channel, builder.addField("", groupsList.toString(), false).build(), 30);
                        return CommandResult.SUCCESS;
                    } else if (args[1].contains(args[1].replaceAll("<", "").replaceAll("@", "").replaceAll("!", "").replaceAll(">", ""))) {
                        Member member = bot.getJDA().getGuildById(Long.valueOf((String) bot.getConf().get("guildID"))).getMemberById(args[1].replaceAll("<", "").replaceAll("@", "").replaceAll("!", "").replaceAll(">", ""));
                        EmbedBuilder builder = new EmbedBuilder().setColor(Color.CYAN).setTitle(member.getEffectiveName() + "'s Perm List");
                        StringBuilder perms = new StringBuilder();
                        for (String str : bot.getSqlManager().getUsersGroups(member.getUser().getIdLong())) {
                            perms.append("`" + str + "`\n");
                        }
                        builder.addField("", perms.toString(), false);
                        bot.getMessenger().sendEmbed(channel, builder.build(), 30);
                        return CommandResult.SUCCESS;
                    }
                }
            } else if (args[0].toLowerCase().equals("creategroup")) {
                if (args.length >= 2) {
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

                } else {
                    return CommandResult.INVALIDARGS;
                }
            } else if (args[0].toLowerCase().equals("user") || args[0].toLowerCase().equals("group")) {
                if (args.length == 4) {
                    switch (args[0].toLowerCase()) {
                        case "group":
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
                        case "user":
                            for (Role roles1 : bot.getJDA().getGuildById(Long.valueOf((String) bot.getConf().get("guildID"))).getRoles()) {
                                if (roles1.getName().toLowerCase().equals(args[3].toLowerCase())) {
                                    bot.getMessenger().sendEmbed(channel, Messenger.INCOMPATIBLE_ARG, 10);
                                }
                            }
                            Member member = bot.getJDA().getGuildById(Long.valueOf((String) bot.getConf().get("guildID"))).getMemberById(args[1].replaceAll("<", "").replaceAll("@", "").replaceAll("!", "").replaceAll(">", ""));
                            switch (args[2].toLowerCase()) {
                                case "set":
                                        if (bot.getSqlManager().groupAlreadyExist(args[3])) {
                                            StringBuilder groupStrings = new StringBuilder();
                                            for (String group : bot.getSqlManager().getUsersGroups(member.getUser().getIdLong())) {
                                                bot.getSqlManager().removeGroupFromUser(member.getUser().getIdLong(), group);
                                                groupStrings.append(group);
                                            }
                                            if (bot.getSqlManager().addGroupToUser(member.getUser().getIdLong(), args[3])) {
                                                bot.getMessenger().sendEmbed(channel, new EmbedBuilder().setTitle("Successful")
                                                        .setColor(Color.GREEN)
                                                        .addField("**Removed:**", groupStrings.toString(), true)
                                                        .addField("**Added:**", args[3], true).build(), 15);
                                                return CommandResult.SUCCESS;
                                            }
                                        } else {
                                            bot.getMessenger().sendEmbed(channel, Messenger.GROUP_DOESNT_EXIST, 10);
                                        }
                                        break;
                                    case "add":
                                        if (bot.getSqlManager().addGroupToUser(member.getUser().getIdLong(), args[3])) {
                                            bot.getMessenger().sendEmbed(channel, new EmbedBuilder().setTitle("Successful")
                                                    .setColor(Color.GREEN)
                                                    .addField("**Added:**", args[3], true).build(), 15);
                                            return CommandResult.SUCCESS;
                                        }
                                        break;
                                    case "remove":
                                        if (bot.getSqlManager().removeGroupFromUser(member.getUser().getIdLong(), args[3])) {
                                            bot.getMessenger().sendEmbed(channel, new EmbedBuilder().setTitle("Successful")
                                                    .setColor(Color.GREEN)
                                                    .addField("**Removed:**", args[3], true).build(), 15);
                                            return CommandResult.SUCCESS;
                                        }
                                        break;
                                    case "adddiscordrole":
                                        bot.getMessenger().sendEmbed(channel, Messenger.INCOMPATIBLE_ARG, 10);
                                        break;
                                    default:
                                        bot.getMessenger().sendEmbed(channel, Messenger.INCOMPATIBLE_ARG, 10);
                                        break;
                                }
                                break;
                                default:
                                    return CommandResult.INVALIDARGS;
                            }
                } else {
                    return CommandResult.INVALIDARGS;
                }
            } else {
                return CommandResult.INVALIDARGS;
            }
        }

        return CommandResult.SUCCESS;
    }

    @Override
    public String name() {
        return "perm";
    }

    @Override
    public String desc() {
        return "perm_desc";
    }

    @Override
    public String usage() {
        return "perm <user|group|list|creategroup> [<user|groupname|groups> <set|add|remove|adddiscordrole|childgroupname> <permission|groupname|discordrole>";
    }

    @Override
    public String[] aliases() {
        return new String[]{"perms"};
    }

    @Override
    public PermType getPermission() {
        return PermType.ADMIN;
    }
}
