package uk.co.netbans.discordbot;

import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main extends ListenerAdapter {
    public static void main(String[] args) throws Exception {
        final ExecutorService console = Executors.newSingleThreadExecutor();
        final NetBansBot bot = new NetBansBot();

        console.submit(() -> {
            final Scanner input = new Scanner(System.in);

            String cmd;
            do {
                cmd = input.nextLine();
                switch (cmd) {
                    default:
                        System.out.println("Invalid Command.");
                }
            } while (!cmd.equalsIgnoreCase("exit"));

            bot.shutdown();
            System.exit(0);
        });

        bot.init(Paths.get(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent());
    }

//    @Override
//    public void onMessageReceived(MessageReceivedEvent event) {
//        if (event.getChannel().getIdLong() == 516019424747061249L && event.getMessage().getContentStripped().startsWith("!embed")) {
//            String[] args = getBetweenQuotes(event.getMessage().getContentRaw().substring(event.getMessage().getContentRaw().indexOf(' ')+1).split(" "));
//
//            EmbedBuilder embed = new EmbedBuilder();
//
//            embed.setAuthor("Created by " + event.getAuthor().getName(), null, event.getAuthor().getAvatarUrl());
//            embed.setColor(new Color(247, 207, 13));
//            embed.setThumbnail("https://i.imgur.com/sBIoRP4.png");
//
//            int start;
//            if (!args[1].contains("https://")) {
//                embed.setTitle(args[0]);
//                start = 1;
//            } else {
//                embed.setTitle(args[0], args[1]);
//                start = 2;
//            }
//
//            for (int i = start; i < args.length; i++) {
//                String[] spl = args[i].split(":");
//                embed.addField(spl[0], spl[1], false);
//            }
//
//            event.getChannel().sendMessage(embed.build()).complete();
//
//            event.getMessage().delete().reason("command").complete();
//        }
//    }
//
//    private String[] getBetweenQuotes(String[] args) {
//        StringBuilder builder = new StringBuilder();
//        int size = args.length;
//        boolean found = false;
//        String[] newArgs = new String[0];
//        for (String arg : args) {
//            if (!found) {
//                size--;
//                builder.append(arg).append(" ");
//                if (arg.endsWith("\"")) {
//                    found = true;
//                    newArgs = new String[size+1];
//                    builder.setLength(builder.length()-2);
//
//                    newArgs[0] = builder.toString().trim().substring(1);
//
//                    size = 1;
//                }
//            } else {
//                newArgs[size] = arg;
//                size++;
//            }
//
//        }
//
//
//        return newArgs;
//    }
}
