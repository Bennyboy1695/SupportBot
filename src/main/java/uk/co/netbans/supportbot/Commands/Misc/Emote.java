package uk.co.netbans.supportbot.Commands.Misc;

import net.dv8tion.jda.core.entities.TextChannel;
import uk.co.netbans.supportbot.CommandFramework.Command;
import uk.co.netbans.supportbot.CommandFramework.CommandArgs;
import uk.co.netbans.supportbot.CommandFramework.CommandCategory;
import uk.co.netbans.supportbot.CommandFramework.CommandResult;
import uk.co.netbans.supportbot.NetBansBot;

public class Emote {

    @Command(name = "emote", displayName = "emote", aliases = "emoji", category = CommandCategory.MISC, usage = "emote \u25B6")
    public CommandResult onEmote(CommandArgs commandArgs) {
        NetBansBot bot = commandArgs.getBot();
        String str = commandArgs.getArgs(0);
        StringBuilder builder = new StringBuilder("Emoji/Character info:");
        str.codePoints().forEachOrdered(code -> {
            char[] chars = Character.toChars(code);
            String hex = Integer.toHexString(code).toUpperCase();
            while(hex.length()<4)
                hex = "0"+hex;
            builder.append("\n`\\u").append(hex).append("`   ");
            if(chars.length>1)
            {
                String hex0 = Integer.toHexString(chars[0]).toUpperCase();
                String hex1 = Integer.toHexString(chars[1]).toUpperCase();
                while(hex0.length()<4)
                    hex0 = "0"+hex0;
                while(hex1.length()<4)
                    hex1 = "0"+hex1;
                builder.append("[`\\u").append(hex0).append("\\u").append(hex1).append("`]   ");
            }
            builder.append(String.valueOf(chars)).append("   _").append(Character.getName(code)).append("_");
        });
        bot.getMessenger().sendMessage((TextChannel) commandArgs.getChannel(), builder.toString(), 30);
        return CommandResult.SUCCESS;

    }
}
