package uk.co.netbans.supportbot.Commands.Admin.PermChildren;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import uk.co.netbans.supportbot.CommandFramework.Command;
import uk.co.netbans.supportbot.CommandFramework.CommandArgs;
import uk.co.netbans.supportbot.CommandFramework.CommandCategory;
import uk.co.netbans.supportbot.NetBansBot;
import uk.co.netbans.supportbot.CommandFramework.CommandResult;

import java.awt.*;

public class List {

    @Command(name = "perm~list", displayName = "list", aliases = "permission~list,perms~list", permission = "supportbot.command.admin.perm.list", usage = "perm list <groups|user|group>", category = CommandCategory.ADMIN)
    public CommandResult onPermList(CommandArgs commandArgs) {
        String[] args = commandArgs.getArgs();
        NetBansBot bot = commandArgs.getBot();
        TextChannel channel = (TextChannel) commandArgs.getChannel();
            if (args.length >= 1) {
                if (bot.getSqlManager().groupAlreadyExist(args[0])) {
                    EmbedBuilder builder = new EmbedBuilder().setColor(Color.CYAN).setTitle(args[0] + "'s Perm List");
                    StringBuilder perms = new StringBuilder();
                    for (String str : bot.getSqlManager().getGroupsPerms(args[0])) {
                        perms.append("`" + str + "`\n");
                    }
                    builder.addField("", perms.toString(), false);
                    bot.getMessenger().sendEmbed(channel, builder.build(), 30);
                    return CommandResult.SUCCESS;
                } else if (args[0].toLowerCase().equals("groups")) {
                    EmbedBuilder builder = new EmbedBuilder().setColor(Color.CYAN).setTitle("Groups List");
                    StringBuilder groupsList = new StringBuilder();
                    for (String str : bot.getSqlManager().getGroups()) {
                        groupsList.append("`" + str + "`\n");
                    }
                    bot.getMessenger().sendEmbed(channel, builder.addField("", groupsList.toString(), false).build(), 30);
                    return CommandResult.SUCCESS;
                } else if (args[0].contains(args[0].replaceAll("<", "").replaceAll("@", "").replaceAll("!", "").replaceAll(">", ""))) {
                    Member member = bot.getJDA().getGuildById(Long.valueOf((String) bot.getConf().get("guildID"))).getMemberById(args[0].replaceAll("<", "").replaceAll("@", "").replaceAll("!", "").replaceAll(">", ""));
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

}
