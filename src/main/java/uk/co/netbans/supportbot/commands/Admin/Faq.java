package uk.co.netbans.supportbot.commands.admin;


import me.bhop.bjdautilities.command.annotation.Command;
import me.bhop.bjdautilities.command.annotation.Execute;
import me.bhop.bjdautilities.command.result.CommandResult;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import uk.co.netbans.supportbot.NetBansBot;

import java.awt.*;
import java.util.List;

@Command(label = "faq", permission = Permission.ADMINISTRATOR)
public class Faq {

    @Execute
    public CommandResult onFAQ(Member member, TextChannel channel, Message message, String label, List<String> args, NetBansBot bot) {
        EmbedBuilder embed = new EmbedBuilder().setColor(Color.GREEN).setTitle("Support F.A.Q");
        embed.addField("**What's this category for ?**", "This category is for channels created by the bot, to deal with support tickets! ", true);
        embed.addField("**Why can't I see any channels in this category except this one?**", "You can't see any channels because they are private between staff and the user that made the ticket!", true);
        embed.addField("**How do I create a channel ?**", "Simply run `!ticket` and you will receive all the info you need to create a channel!", true);
        embed.addField("**What happens when I've finished my ticket ?**", "Once you have finished your ticket, you simply react with a \u2705 and the channel will then be closed and a log of everything said in the channel will be sent to you!", true);
        embed.addField("**Why can't I create another channel ?**", "You are unable to create a channel because of multiple reasons. One reason might be that you already have a ticket open, therefore by extension a channel, and to prevent spam we have limited the amount you can have open and how often you can open them! The other reason might be that you abused the system and have been given the anti-support role, meaning you are unable to create a channel for help!", true);

        bot.getMessenger().sendEmbed(channel, embed.build());
        return CommandResult.success();
    }
}
