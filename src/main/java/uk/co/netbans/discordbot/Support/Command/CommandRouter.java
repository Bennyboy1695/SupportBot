package uk.co.netbans.discordbot.Support.Command;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import uk.co.netbans.discordbot.NetBansBot;
import uk.co.netbans.discordbot.Util;

import java.util.*;

public class CommandRouter {
    private final NetBansBot bot;
    private final Set<CommandRouter> routers;
    private final Set<Command> commands;
    private final List<Long> allowedChannels;
    private final String name;
    private final String[] aliases;

    public CommandRouter(NetBansBot bot, String name, List<Long> allowedChannels, String... aliases) {
        this.bot = bot;
        this.routers = new HashSet<>();
        this.commands = new HashSet<>();
        this.allowedChannels = allowedChannels;
        this.name = name;
        this.aliases = aliases;
    }

    public CommandResult onCommand(Member sender, TextChannel channel, String[] args) {
//        if (args.length == 0) {
//            Util.sendMessage(channel, "You have not entered a valid command!"); //todo send help
//            return CommandResult.INVALIDARGS;
//        }

        if (args.length > 0) {
            Command command = getCommand(args[0]);
            if (command != null) {
                if (!command.hasPermission(bot, sender.getUser().getIdLong()))
                    return CommandResult.NOPERMS;
                String[] argsNew = new String[args.length-1];
                System.arraycopy(args, 1, argsNew, 0, args.length - 1);
                return command.onExecute(this.bot, sender, channel, args[0], argsNew);
            }

            CommandRouter router = getRouter(args[0]);
            if (router != null) {
                String[] argsNew = new String[args.length-1];
                System.arraycopy(args, 1, argsNew, 0, args.length - 1);
                return router.onCommand(sender, channel, argsNew);
            }
        }
        Command dft = getCommand("~");
        if (dft != null) {
            if (!dft.hasPermission(bot, sender.getUser().getIdLong()))
                return CommandResult.NOPERMS;
            if (args.length > 0) {
                String[] argsNew = new String[args.length-1];
                System.arraycopy(args, 1, argsNew, 0, args.length - 1);
                return dft.onExecute(this.bot, sender, channel, args[0], argsNew);
            } else {
                return dft.onExecute(this.bot, sender, channel, "~", new String[]{});
            }

        }


        Util.sendMessage(channel, "You have not entered a valid command!"); //todo send help
        return CommandResult.INVALIDARGS;
    }

    private Command getCommand(String name) {
        for (Command command : commands) {
            if (command.name().equalsIgnoreCase(name))
                return command;
            for (String alias : command.aliases())
                if (alias.equalsIgnoreCase(name))
                    return command;
        }
        return null;
    }

    private CommandRouter getRouter(String name) {
        for (CommandRouter router : routers) {
            if (router.name().equalsIgnoreCase(name))
                return router;
            for (String alias : router.aliases())
                if (alias.equalsIgnoreCase(name))
                    return router;
        }
        return null;
    }

    public String name() {
        return this.name;
    }

    public String[] aliases() {
        return this.aliases;
    }

    public List<Long> getAllowedChannels() {
        return this.allowedChannels;
    }

    public CommandRouter addSubRouter(CommandRouter router) {
        this.routers.add(router);
        return this;
    }

    public void addCommand(Command... command) {
        this.commands.addAll(Arrays.asList(command));
    }

    public NetBansBot getBot() {
        return this.bot;
    }
}
