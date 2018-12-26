package uk.co.netbans.supportbot.Support.Command.Admin;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import uk.co.netbans.supportbot.NetBansBot;
import uk.co.netbans.supportbot.PermType;
import uk.co.netbans.supportbot.Support.Command.Command;
import uk.co.netbans.supportbot.Support.Command.CommandResult;

import java.awt.*;

public class ConfigReload implements Command {
    private NetBansBot bot;

    @Override
    public CommandResult onExecute(NetBansBot bot, Member sender, TextChannel channel, String label, String[] args) {
        this.bot = bot;
        try {
            bot.reloadConfig();
            bot.getMessenger().sendEmbed(channel, new EmbedBuilder().setDescription("Reloaded Config!").setColor(Color.GREEN).build());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CommandResult.SUCCESS;
    }

    @Override
    public String name() {
        return "configreload";
    }

    @Override
    public String desc() {
        return "Reloads the config!";
    }

    @Override
    public String usage() {
        return ("configreload");
    }

    @Override
    public String[] aliases() {
        return new String[0];
    }

    @Override
    public PermType getPermission() {
        return PermType.ADMIN;
    }
}
