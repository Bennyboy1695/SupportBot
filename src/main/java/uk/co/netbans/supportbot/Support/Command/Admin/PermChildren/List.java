package uk.co.netbans.supportbot.Support.Command.Admin.PermChildren;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import uk.co.netbans.supportbot.NetBansBot;
import uk.co.netbans.supportbot.Support.Command.Command;
import uk.co.netbans.supportbot.Support.Command.CommandResult;

import java.awt.*;

public class List implements Command {

    @Override
    public CommandResult onExecute(NetBansBot bot, Member sender, TextChannel channel, String label, String[] args) {
        System.out.println("Im Being called!");
            if (args.length >= 1) {
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
            return CommandResult.SUCCESS;
    }

    @Override
    public String name() {
        return "list";
    }

    @Override
    public String desc() {
        return "?";
    }

    @Override
    public String usage() {
        return "???";
    }

    @Override
    public String[] aliases() {
        return new String[0];
    }
}
