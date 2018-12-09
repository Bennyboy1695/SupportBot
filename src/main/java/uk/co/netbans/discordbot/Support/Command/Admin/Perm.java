package uk.co.netbans.discordbot.Support.Command.Admin;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import uk.co.netbans.discordbot.Message.Messenger;
import uk.co.netbans.discordbot.PermType;
import uk.co.netbans.discordbot.Support.Command.Command;
import uk.co.netbans.discordbot.Support.Command.CommandResult;
import uk.co.netbans.discordbot.NetBansBot;

import java.util.List;

public class Perm implements Command {

    @SuppressWarnings("unchecked")
    @Override
    public CommandResult onExecute(NetBansBot bot, Member sender, TextChannel channel, String label, String[] args) {
        List<Long> admin = bot.getPerms().get(PermType.ADMIN);
        List<Long> mod = bot.getPerms().get(PermType.MOD);
        //JSONArray admin = (JSONArray) bot.getPerms().get("admin");
        //JSONArray mod = (JSONArray) bot.getPerms().get("mod");
        if (admin.contains(sender.getUser().getIdLong())) {
            if (args.length == 1) {
                if (args[0].toLowerCase().equals("reload")) {
                    bot.reloadPerms();
                } else if (args[0].toLowerCase().equals("list")) {
                    StringBuilder adminIDs = new StringBuilder();
                    //StringBuilder modIDs = new StringBuilder();
                    for (long id : admin) {
                        adminIDs.append("<@" + id + "> \n");
                    }
                    //for (long id : mod) {
                    //    modIDs.append("<@" + id + "> \n");
                    //}
                    bot.getMessenger().sendEmbed(channel, bot.getMessenger().getCommonEmbed().setTitle("Perm's List").addField("Admin", adminIDs.toString(), false)
                            //.addField("Mod", modIDs.toString(), false)
                            .build());
                } else {
                    return CommandResult.INVALIDARGS;
                }
            } else if (args.length < 3) {
                return CommandResult.INVALIDARGS;
            } else {
                Long user = bot.getJDA().getUserById(Long.valueOf(args[1].replace("<@!", "").replace(">", ""))).getIdLong();
                switch (args[0].toLowerCase()) {
                    case "add":
                        switch (args[2].toLowerCase()) {
                            case "admin":
                                admin.add(user);//call directly on the one in the list
                                System.out.println("Added " + user + " to admins!");
                                //bot.reloadPerms();
                                break;
                            case "mod":
                                //mod.add(user);
                                bot.reloadPerms();
                                break;
                        }
                        break;
                    case "remove":
                        switch (args[2].toLowerCase()) {
                            case "admin":
                                admin.remove(user); // same as above
                                bot.reloadPerms();
                                break;
                            case "mod":
                                //mod.remove(user);
                                bot.reloadPerms();
                                break;
                        }
                            break;
                }
            }
            } else{
                bot.getMessenger().sendEmbed(channel, Messenger.NO_PERMS);
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
        return "!perm <add|remove|reload|list> [user <admin|mod>]";
    }

    @Override
    public String[] aliases() {
        return new String[]{"perms"};
    }
}
