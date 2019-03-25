package uk.co.netbans.supportbot.support.listeners;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import uk.co.netbans.supportbot.NetBansBot;

import java.util.ArrayList;
import java.util.List;

public class EmoteRemoverListener extends ListenerAdapter {

    private NetBansBot bot;
    public EmoteRemoverListener(NetBansBot bot) {
        this.bot = bot;
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        if (event.getAuthor().isBot() || event.getMessage().getContentRaw().startsWith(bot.getCommandPrefix()))
            return;

        for (String emote: getEmotes()) {
            if (event.getMessage().getContentRaw().contains(emote))
                event.getMessage().delete().complete();
            for (Emote emote1 : event.getMessage().getEmotes()) {
                if (emote1.getName().equals(emote))
                    event.getMessage().delete().complete();
            }
        }
    }

    public List<String> getEmotes() {
        List<String> replyArray = new ArrayList<>();
        JsonArray replies = bot.getConfig().getConfigValue("emotes");
        for (Object obj : replies) {
            JsonObject jsonObject = (JsonObject) obj;
            String word = jsonObject.get("unicode").getAsString();
            replyArray.add(word);
        }
        return replyArray;
    }
}
