package uk.co.netbans.supportbot.Commands.Admin.PermChildren;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import uk.co.netbans.supportbot.CommandFramework.Command;
import uk.co.netbans.supportbot.CommandFramework.CommandArgs;
import uk.co.netbans.supportbot.CommandFramework.CommandCategory;
import uk.co.netbans.supportbot.Message.Messenger;
import uk.co.netbans.supportbot.NetBansBot;
import uk.co.netbans.supportbot.CommandFramework.CommandResult;

import java.awt.*;

public class User{

    @Command(name = "perm~user", displayName = "user", aliases = "permission~user,perms~user", permission = "supportbot.command.admin.perm.user", usage = "perm user <user> <add|set|remove> <perm|group>", category = CommandCategory.ADMIN)
    public CommandResult onPermUser(CommandArgs commandArgs) {
        String[] args = commandArgs.getArgs();
        NetBansBot bot = commandArgs.getBot();
        TextChannel channel = (TextChannel) commandArgs.getChannel();
        if (args.length <= 3)
            return CommandResult.INVALIDARGS;
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
        return CommandResult.SUCCESS;
    }
}
