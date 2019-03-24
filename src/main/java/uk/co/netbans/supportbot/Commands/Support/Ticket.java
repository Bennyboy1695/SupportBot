package uk.co.netbans.supportbot.Commands.Support;

import me.bhop.bjdautilities.command.annotation.Command;
import me.bhop.bjdautilities.command.annotation.Execute;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.requests.RestAction;
import uk.co.netbans.supportbot.CommandFramework.CommandResult;
import uk.co.netbans.supportbot.NetBansBot;

import java.awt.*;
import java.util.List;

@Command(label = {"ticket", "report"}, usage = "ticket")
public class Ticket {

    @Execute
    public CommandResult onExecute(Member member, TextChannel channel, Message message, String label, List<String> args, NetBansBot bot) {
        RestAction<PrivateChannel> pmChannel = member.getUser().openPrivateChannel();
//        bot.getMessenger().sendEmbed(channel, bot.getMessenger().getCommonEmbed()
//                .setColor(new Color(127, 255, 212))
//                .setTitle("Ticket Help")
//                .setDescription("The bot will now send you instructions on how to create support ticket! \n" +
//                        "Click the link to open your private message (will only work for the user running this command). \n"
//                 + "Please make sure you have private messages turned on for this server to receive a message from the bot!")
//                .addField("Private Message", "https://discordapp.com/channels/@me/" + pmChannel.complete().getIdLong(), false)
//                .build(), 30);
//
//        bot.getMessenger().sendPrivateMessage(sender.getUser(), new EmbedBuilder()
//                .setTitle("Ticket Creation Instructions!")
//                .setColor(new Color(127, 255, 212))
//                .setDescription("To create a ticket simply type a message here and it will be used to create a ticket containing that message you provided! \n" +
//                        "Please note: Multiple message will not be combined together so your message needs to be one message NOT multiple little ones! \n" +
//                        "Also if you upload a file we will take the file and put it into the support channel too!").build()
//                );
        return CommandResult.SUCCESS;
    }
}
