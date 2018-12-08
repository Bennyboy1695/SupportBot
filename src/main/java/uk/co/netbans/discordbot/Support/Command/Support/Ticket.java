package uk.co.netbans.discordbot.Support.Command.Support;

import com.vdurmont.emoji.EmojiManager;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.requests.RestAction;
import uk.co.netbans.discordbot.Support.Command.Command;
import uk.co.netbans.discordbot.Support.Command.CommandResult;
import uk.co.netbans.discordbot.NetBansBot;

import java.awt.*;

public class Ticket implements Command {

    @Override
    public CommandResult onExecute(NetBansBot bot, Member sender, TextChannel channel, String label, String[] args) {
        RestAction<PrivateChannel> pmChannel = sender.getUser().openPrivateChannel();
        bot.getMessenger().sendEmbed(channel, bot.getMessenger().getCommonEmbed()
                .setColor(new Color(127, 255, 212))
                .setTitle("Ticket Help")
                .setDescription("The bot will now send you instructions on how to create support ticket! \n" +
                        "Click the link to open your private message (will only work for the user running this command). \n"
                 + "Please make sure you have private messages turned on for this server to receive a message from the bot!")
                .addField("Private Message", "https://discordapp.com/channels/@me/" + pmChannel.complete().getIdLong(), false)
                .build(), 30);

        bot.getMessenger().sendPrivateMessage(sender.getUser(), "instructions!").addReaction("\u2705").complete();
        return CommandResult.SUCCESS;
    }

    @Override
    public String name() {
        return "ticket";
    }

    @Override
    public String desc() {
        return "Provides you with information on how to create a ticket!";
    }

    @Override
    public String usage() {
        return "!ticket";
    }

    @Override
    public String[] aliases() {
        return new String[0];
    }
}
