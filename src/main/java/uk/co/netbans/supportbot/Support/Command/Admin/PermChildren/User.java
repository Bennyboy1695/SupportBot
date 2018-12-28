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

public class User implements Command {

    @Override
    public CommandResult onExecute(NetBansBot bot, Member sender, TextChannel channel, String label, String[] args) {
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

    @Override
    public String name() {
        return "user";
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
