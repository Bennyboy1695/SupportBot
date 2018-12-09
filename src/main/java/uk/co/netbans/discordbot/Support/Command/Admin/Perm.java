package uk.co.netbans.discordbot.Support.Command.Admin;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import uk.co.netbans.discordbot.PermType;
import uk.co.netbans.discordbot.Support.Command.Command;
import uk.co.netbans.discordbot.Support.Command.CommandResult;
import uk.co.netbans.discordbot.NetBansBot;

import java.awt.*;
import java.util.List;

public class Perm implements Command {

    private NetBansBot bot;

    @Override
    public CommandResult onExecute(NetBansBot bot, Member sender, TextChannel channel, String label, String[] args) {
        this.bot = bot;
        List<Long> admin = bot.getPerms().get(PermType.ADMIN);
        List<Long> mod = bot.getPerms().get(PermType.MOD);
            if (args.length == 1) {
                if (args[0].toLowerCase().equals("reload")) {
                    bot.reloadPerms();
                    bot.getMessenger().sendEmbed(channel, new EmbedBuilder().setDescription("Reloaded Perms!").setColor(Color.GREEN).build(),10);
                } else if (args[0].toLowerCase().equals("list")) {
                    StringBuilder adminIDs = new StringBuilder();
                    StringBuilder modIDs = new StringBuilder();
                    for (long id : admin) {
                        adminIDs.append("<@" + id + "> \n");
                    }
                    for (long id : mod) {
                        modIDs.append("<@" + id + "> \n");
                    }
                    bot.getMessenger().sendEmbed(channel, bot.getMessenger().getCommonEmbed().setTitle("Perm's List").addField("Admin", adminIDs.toString(), false)
                            .addField("Mod", modIDs.toString(), false)
                            .build(), 10);
                } else {
                    return CommandResult.INVALIDARGS;
                }
            } else if (args.length < 3) {
                return CommandResult.INVALIDARGS;
            } else {
                Long user = bot.getJDA().getUserById(Long.valueOf(args[1].replace("<@", "").replace(">", ""))).getIdLong();
                switch (args[0].toLowerCase()) {
                    case "add":
                        switch (args[2].toLowerCase()) {
                            case "admin":
                                admin.add(user);//call directly on the one in the list
                                System.out.println("Added " + user + " to admins!");
                                bot.getMessenger().sendEmbed(channel, new EmbedBuilder().setDescription("Added user to Admin!").setColor(Color.GREEN).build(), 10);
                                break;
                            case "mod":
                                mod.add(user);
                                bot.getMessenger().sendEmbed(channel, new EmbedBuilder().setDescription("Added user to Mod!").setColor(Color.GREEN).build(),10);
                                break;
                        }
                        break;
                    case "remove":
                        switch (args[2].toLowerCase()) {
                            case "admin":
                                admin.remove(user); // same as above
                                bot.getMessenger().sendEmbed(channel, new EmbedBuilder().setDescription("Removed user from Admin!").setColor(Color.GREEN).build(),10);
                                break;
                            case "mod":
                                mod.remove(user);
                                bot.getMessenger().sendEmbed(channel, new EmbedBuilder().setDescription("Removed user from Mod!").setColor(Color.GREEN).build(),10);
                                break;
                        }
                            break;
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
        return bot.getCommandPrefix() +  "perm <add|remove|reload|list> [user <admin|mod>]";
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
