package uk.co.netbans.supportbot.storage;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import org.bson.BsonArray;
import org.bson.Document;
import uk.co.netbans.supportbot.SupportBot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MongoRequestRegistry {
    private final MongoController controller;

    public MongoRequestRegistry(SupportBot bot) {
        this.controller = bot.getMongoController();
    }

    public Document getGlobalSettings() {
        return controller.getCollection("config").find(new Document("_id", -1)).first();
    }

    public void createNewTicket(Guild guild, Message message, User user, long creationTime, int locked) {
        Document document = new Document();
        document.append("guildId", guild.getIdLong());
        document.append("messageId", message.getId());
        document.append("owner", user.getId());
        document.append("creationTime", creationTime);
        document.append("locked", locked);
        List<String> messages = new ArrayList<>();
        if (message.getAttachments().size() >= 1) {
            messages.add(message.getAttachments().get(0).getProxyUrl());
        } else if (message.getEmbeds().size() >= 1) {
            messages.add(message.getEmbeds().get(0).toJSONObject().toString());
        }
        if (!message.getContentRaw().isEmpty())
            messages.add(message.getContentRaw());
        document.append("messages", messages);
        controller.getCollection("tickets").insertOne(document);
    }

    public Document getTicketForMember(Member member) {
        return controller.getCollection("tickets").find(new Document("guildId", member.getGuild().getId())).filter(new Document("owner", member.getUser().getId())).limit(1).first();
    }

    public List<String> getMessagesForMember(Member member) {
        return (List<String>) getTicketForMember(member).get("messages");
    }

    public boolean addMessageToMembersMessages(Member member, Message message) {
        Document update = new Document();
        if (message.getAttachments().size() >= 1) {
            return getMessagesForMember(member).add(message.getAttachments().get(0).getProxyUrl());
        } else if (message.getEmbeds().size() >= 1) {
            return getMessagesForMember(member).add(message.getEmbeds().get(0).toJSONObject().toString());
        }
        if (!message.getContentRaw().isEmpty())
            return getMessagesForMember(member).add(message.getContentRaw());
        return controller.getCollection("tickets").updateOne(getTicketForMember(member), "");//TODO: dont know how to do this!
    }

    public void createNewGuildConfig(long guildID) {
        Document document = new Document();
        document.append("_id", guildID);
        document.append("prefix", "?");
        document.append("botName", "SupportBot");
        Document modules = new Document();
        Document tickets = new Document();
        tickets.append("enabled", true);
        Document settings = new Document();
        settings.append("categoryId", "add_me");
        tickets.append("settings", settings);
        modules.append("tickets", tickets);
        modules.append("roleTags", new Document("enabled", true));
        modules.append("emoteRemoving", new Document("enabled", true));
        document.append("modules", modules);
        document.append("listeners", new Document("roleTags", true));

        controller.getCollection("config").insertOne(document);
    }

    public Document getGuildConfig(long guildId) {
        return controller.getCollection("config").find(new Document("_id", guildId)).limit(1).first();
    }

    public Document getGuildTicketSettings(long guildId) {
        return (Document) ((Document) getGuildModules(guildId).get("tickets")).get("settings");
    }

    public Document getGuildModules(long guildId) {
        return (Document) controller.getCollection("config").find(new Document("_id", guildId)).limit(1).first().get("modules");
    }

    public boolean doesConfigExistAlready(long guildID) {
        return controller.getCollection("config").find(new Document().append("_id", guildID)).limit(1).first() != null;
    }
}
