package uk.co.netbans.supportbot.Support.Command.Admin;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.TextChannel;
import uk.co.netbans.supportbot.BenCMDFramework.Command;
import uk.co.netbans.supportbot.BenCMDFramework.CommandArgs;
import uk.co.netbans.supportbot.BenCMDFramework.CommandResult;

import java.awt.*;

public class Faq {

    @Command(name = "faq", displayName = "faq", permission = "supportbot.command.admin.faq", usage = "faq")
    public CommandResult onFAQ(CommandArgs args) {
        EmbedBuilder embed = new EmbedBuilder().setColor(Color.GREEN).setTitle("Support F.A.Q");
        embed.addField("**What's this category for ?**", "This category is for channels created by the bot, to deal with support tickets! ", true);
        embed.addField("**Why can't I see any channels in this category except this one?**", "You can't see any channels because they are private between staff and the user that made the ticket!", true);
        embed.addField("**How do I create a channel ?**", "Simply run `!ticket` and you will receive all the info you need to create a channel!", true);
        embed.addField("**What happens when I've finished my ticket ?**", "Once you have finished your ticket, you simply react with a \u2705 and the channel will then be closed and a log of everything said in the channel will be sent to you!", true);
        embed.addField("**Why can't I create another channel ?**", "You are unable to create a channel because of multiple reasons. One reason might be that you already have a ticket open, therefore by extension a channel, and to prevent spam we have limited the amount you can have open and how often you can open them! The other reason might be that you abused the system and have been given the anti-support role, meaning you are unable to create a channel for help!", true);

        args.getBot().getMessenger().sendEmbed((TextChannel) args.getChannel(), embed.build());
        return CommandResult.SUCCESS;
    }
}
