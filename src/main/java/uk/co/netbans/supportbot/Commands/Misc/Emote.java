package uk.co.netbans.supportbot.Commands.Misc;


import me.bhop.bjdautilities.command.annotation.Command;
import me.bhop.bjdautilities.command.annotation.Execute;
import me.bhop.bjdautilities.command.result.CommandResult;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import uk.co.netbans.supportbot.NetBansBot;

import java.util.List;

@Command(label = {"emote", "emoji"}, permission = Permission.ADMINISTRATOR)
public class Emote {

    @Execute
    public CommandResult onEmote(Member member, TextChannel channel, Message message, String label, List<String> args, NetBansBot bot) {
        String str = args.get(0);
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
        bot.getMessenger().sendMessage(channel, builder.toString(), 30);
        return CommandResult.success();

    }
}
