package uk.co.netbans.supportbot.support.listeners;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import uk.co.netbans.supportbot.NetBansBot;

import java.util.concurrent.ThreadLocalRandom;

public class TagListener extends ListenerAdapter {

    private NetBansBot bot;

    public TagListener(NetBansBot bot) {
        this.bot = bot;
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        Member botMember = bot.getJDA().getGuildById(bot.getGuildID()).getMemberById(bot.getJDA().asBot().getApplicationInfo().complete().getIdLong());
        if (event.getMessage().getMentionedMembers().contains(botMember)) {
            bot.getMessenger().sendMessage(event.getChannel(), randomReplies(), 10);
        }
    }

    private String randomReplies() {
        int random = ThreadLocalRandom.current().nextInt(bot.getReplies().size());
        return bot.getReplies().get(random);
    }
}
