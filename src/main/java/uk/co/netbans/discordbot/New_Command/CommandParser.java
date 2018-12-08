package uk.co.netbans.discordbot.New_Command;

import com.google.common.collect.ImmutableList;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import uk.co.netbans.discordbot.NetBansBot;
import uk.co.netbans.discordbot.New_Command.Model.Command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SuppressWarnings("unchecked")
public class CommandParser extends ListenerAdapter {
    private final NetBansBot bot;
    private final ExecutorService executor = Executors.newSingleThreadExecutor(); //todo named
    private final List<Command<?, ?>> commands;

    public CommandParser(NetBansBot bot) {
        this.bot = bot;

        this.commands = ImmutableList.<Command<?, ?>>builder()
                .add()
                .build();
    }

    public NetBansBot getBot() {
        return bot;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        if (!e.getMessage().getContentRaw().startsWith("!") || e.getAuthor().isBot() || e.getMessage().getContentRaw().length() <= 1)
            return;

        String[] args = e.getMessage().getContentRaw().split(" ");
        args[0] = args[0].substring(1);

        executor.submit(() -> {
            CommandResult result = execute(e.getMember(), e.getTextChannel(), Arrays.asList(args));
            switch (result) {
                default:
                    System.out.println("[TMP] Command Executed with result: " + result);
            }
        });
    }

    private CommandResult execute(Member member, TextChannel channel, List<String> args) {
        List<String> arguments = new ArrayList<>(args);

        if (arguments.isEmpty() || (arguments.size() == 1 && arguments.get(0).trim().isEmpty())) {
            getBot().getMessenger().sendMessage(channel, "", 10);
            return CommandResult.OK;
        }

        Optional<Command<?, ?>> opt = this.commands.stream()
                .filter(cmd -> cmd.getLabels().contains(arguments.get(0)))
                .findFirst();

        if (!opt.isPresent())
            return CommandResult.INVALID_ARGUMENTS;

        final String label = arguments.get(0);
        final Command main = opt.get();
        if (!main.canSend(channel))
            return CommandResult.INVALID_CHANNEL;

        arguments.remove(0);

        if (main.getMinArgs() > arguments.size())
            return CommandResult.INVALID_ARGUMENTS;

        return main.execute(this.bot, member, channel, label, null, arguments);
    }
}
