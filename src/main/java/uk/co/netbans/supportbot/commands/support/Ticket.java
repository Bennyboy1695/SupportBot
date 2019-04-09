package uk.co.netbans.supportbot.commands.support;


import me.bhop.bjdautilities.command.annotation.Command;
import me.bhop.bjdautilities.command.annotation.Execute;
import me.bhop.bjdautilities.command.result.CommandResult;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.exceptions.ErrorResponseException;
import uk.co.netbans.supportbot.EmbedTemplates;
import uk.co.netbans.supportbot.SupportBot;

import java.awt.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Command(label = {"ticket", "report"}, usage = "ticket")
public class Ticket {
    @Execute
    public CommandResult onExecute(Member member, TextChannel channel, Message message, String label, List<String> args, SupportBot bot) {
        MessageChannel pmChannel = member.getUser().openPrivateChannel().complete();

        pmChannel.sendMessage(new EmbedBuilder()
                .setTitle("Ticket Creation Instructions!")
                .setColor(new Color(127, 255, 212))
                .setDescription("To create a ticket simply type a message here and it will be used to create a ticket containing that message you provided! \n" +
                        "Please note: Multiple message will not be combined together so your message needs to be one message NOT multiple little ones! \n" +
                        "Also if you upload a file we will take the file and put it into the support channel too!").build()).queue(worked -> {
                            channel.sendMessage(EmbedTemplates.BASE.getEmbed()
                                    .setColor(new Color(127, 255, 212))
                                    .setTitle("Ticket Help")
                                    .setDescription("The bot has now sent you instructions on how to create support ticket! \n" +
                                            "Click the link to open your private message (will only work for the user running this command).")
                                    .addField("Private Message", "https://discordapp.com/channels/@me/" + pmChannel.getIdLong(), false)
                                    .build()).queue((m) -> {
                                        m.delete().queueAfter(30, TimeUnit.SECONDS);
                                    });
                    },
                    failure -> {
                                channel.sendMessage(EmbedTemplates.PM_ERROR.getEmbed().build()).queue((m) -> {
                                    m.delete().queueAfter(10, TimeUnit.SECONDS);
                                });
                            });
        return CommandResult.success();
    }
}
