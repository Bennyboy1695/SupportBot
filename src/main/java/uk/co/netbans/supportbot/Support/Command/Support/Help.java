package uk.co.netbans.supportbot.Support.Command.Support;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import uk.co.netbans.supportbot.BenCMDFramework.Command;
import uk.co.netbans.supportbot.BenCMDFramework.CommandArgs;
import uk.co.netbans.supportbot.NetBansBot;
import uk.co.netbans.supportbot.BenCMDFramework.CommandResult;

import java.awt.*;
import java.lang.reflect.Method;
import java.util.*;
import java.util.List;


public class Help {

    @Command(name = "help", displayName = "help", usage = "help")
    public CommandResult onExecute(CommandArgs commandArgs) {
        NetBansBot bot = commandArgs.getBot();
        String[] args = commandArgs.getArgs();
        Member sender = commandArgs.getMember();
        TextChannel channel = (TextChannel) commandArgs.getChannel();
        List<Command> commandsList = new ArrayList<Command>();
        bot.getFramework().getCommandMap().forEach((x, z) -> {
            Method method = bot.getFramework().getCommandMap().get(x).getKey();
            Command command = method.getAnnotation(Command.class);
            if (x.equals(Arrays.toString(command.aliases()).replace("[", "").replace("]", "")))
                return; //Dirty way to only do each command once as some have only one alias!
            if (x.contains("~")) return;
            commandsList.add(command);
        });
        commandsList.sort(Comparator.comparing(Command::name));
        StringBuilder commands = new StringBuilder();
        List<String> temp = new ArrayList<String>();
        final Integer limit = 800;
        for (Command cmd : commandsList) {
            String output = cmd.name().substring(0, 1).toUpperCase() + cmd.name().substring(1);
            commands.append("**" + output + ":**" + "\n" +
                    "\u00A0\u00A0**Aliases:** " + Arrays.toString(cmd.aliases()).replace("[", "").replace("]", "") + "\n" +
                    "\u00A0\u00A0**Usage:** " + bot.getCommandPrefix() + cmd.usage() + "\n" +
                    "\u00A0\u00A0\u00A0\u00A0\u00A0**Perm:** `" + cmd.permission().toLowerCase() + "`\n" + "\n");
            if (commands.length() > limit) {
                temp.add(commands.toString());
                commands.setLength(0);
            }
        }
        if (commands.length() != 0) {
            temp.add(commands.toString());
            commands.setLength(0);
        }
        for (String str : temp) {
            bot.getMessenger().sendEmbed(channel, new EmbedBuilder().setTitle("Help").setColor(Color.ORANGE).addField("Commands:", str + "\n", false).build(), 30);
        }
        return CommandResult.SUCCESS;
    }
}
