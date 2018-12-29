package uk.co.netbans.supportbot;

import net.dv8tion.jda.core.hooks.ListenerAdapter;
import uk.co.netbans.supportbot.BenCMDFramework.CommandFramework;

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
}
