package uk.co.netbans.discordbot.Support.Command.Admin;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import org.json.simple.JSONArray;
import uk.co.netbans.discordbot.Message.Messenger;
import uk.co.netbans.discordbot.PermType;
import uk.co.netbans.discordbot.Support.Command.Command;
import uk.co.netbans.discordbot.Support.Command.CommandResult;
import uk.co.netbans.discordbot.NetBansBot;

import java.util.List;

public class Perm implements Command {

    @Override
    public CommandResult onExecute(NetBansBot bot, Member sender, TextChannel channel, String label, String[] args) {
        List<Long> admin = bot.getPerms().get(PermType.ADMIN);
        //JSONArray admin = (JSONArray) bot.getPerms().get("admin");
        //JSONArray mod = (JSONArray) bot.getPerms().get("mod");
        bot.getMessenger().sendMessage(channel, "<@" + admin.get(0) + ">");
        bot.getMessenger().sendMessage(channel, String.valueOf(admin.contains(sender.getUser().getIdLong())));

        System.out.println("All Admins: " + admin.toString());
        if (admin.contains(sender.getUser().getIdLong())) {
            if (args.length == 1) {
                if (args[0].toLowerCase().equals("reload")) {
                    bot.reloadPerms();
                } else {
                    return CommandResult.INVALIDARGS;
                }
            } else if (args.length < 3) {
                return CommandResult.INVALIDARGS;
            } else {
                Long user = bot.getJDA().getUserById(Long.valueOf(args[1].replace("!", ""))).getIdLong();
                switch (args[0].toLowerCase()) {
                    case "add":
                        switch (args[2].toLowerCase()) {
                            case "admin":
                                bot.getPerms().get(PermType.ADMIN).add(user); //call directly on the one in the list
                                bot.reloadPerms();
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
        return "!perm <add|remove> user <admin|mod>";
    }

    @Override
    public String[] aliases() {
        return new String[]{"perms"};
    }
}
