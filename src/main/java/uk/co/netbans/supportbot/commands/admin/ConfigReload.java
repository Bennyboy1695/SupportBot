package uk.co.netbans.supportbot.commands.admin;

import me.bhop.bjdautilities.command.annotation.Command;
import me.bhop.bjdautilities.command.annotation.Execute;
import me.bhop.bjdautilities.command.result.CommandResult;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import uk.co.netbans.supportbot.SupportBot;

import java.awt.*;
import java.util.List;

@Command(label = {"configreload","reloadconfig"}, permission = Permission.ADMINISTRATOR)
public class ConfigReload{

    @Execute
    public CommandResult onConfigReload(Member member, TextChannel channel, Message message, String label, List<String> args, SupportBot bot) {
        if (args.size() > 0) return CommandResult.invalidArguments();
        try {
            bot.reloadConfig();
            bot.getMessenger().sendEmbed(channel, new EmbedBuilder().setDescription("Reloaded Config!").setColor(Color.GREEN).build());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CommandResult.success();
    }
}
