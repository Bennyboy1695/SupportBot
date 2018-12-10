package uk.co.netbans.supportbot.Support.Command.Support;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.requests.RestAction;
import uk.co.netbans.supportbot.Support.Command.Command;
import uk.co.netbans.supportbot.Support.Command.CommandResult;
import uk.co.netbans.supportbot.NetBansBot;

import java.awt.*;

public class Ticket implements Command {

    private NetBansBot bot;

    @Override
    public CommandResult onExecute(NetBansBot bot, Member sender, TextChannel channel, String label, String[] args) {
        this.bot = bot;
        RestAction<PrivateChannel> pmChannel = sender.getUser().openPrivateChannel();
        bot.getMessenger().sendEmbed(channel, bot.getMessenger().getCommonEmbed()
                .setColor(new Color(127, 255, 212))
                .setTitle("Ticket Help")
                .setDescription("The bot will now send you instructions on how to create support ticket! \n" +
                        "Click the link to open your private message (will only work for the user running this command). \n"
                 + "Please make sure you have private messages turned on for this server to receive a message from the bot!")
                .addField("Private Message", "https://discordapp.com/channels/@me/" + pmChannel.complete().getIdLong(), false)
                .build(), 30);

        bot.getMessenger().sendPrivateMessage(sender.getUser(), new EmbedBuilder()
                .setTitle("Ticket Creation Instructions!")
                .setColor(new Color(127, 255, 212))
                .setDescription("To create a ticket simply type a message here and it will be used to create a ticket containing that message you provided! \n" +
                        "Please note: Multiple message will not be combined together so your message needs to be one message NOT multiple little ones! \n" +
                        "Also if you upload a file we will take the file and put it into the support channel too!").build()
                ).addReaction("\u2705").complete();
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
        return "ticket";
    }

    @Override
    public String[] aliases() {
        return new String[0];
    }
}
