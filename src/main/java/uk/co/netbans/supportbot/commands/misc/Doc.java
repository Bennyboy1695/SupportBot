package uk.co.netbans.supportbot.commands.misc;

import me.bhop.bjdautilities.command.annotation.Command;
import me.bhop.bjdautilities.command.annotation.Execute;
import me.bhop.bjdautilities.command.result.CommandResult;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import uk.co.netbans.supportbot.EmbedTemplates;
import uk.co.netbans.supportbot.SupportBot;

import java.io.IOException;
import java.net.URL;
import java.util.List;

@Command(label = {"doc", "javadoc"}, minArgs = 1)
public class Doc {

    @Execute
    public CommandResult onDoc(Member member, TextChannel channel, Message message, String label, List<String> args, SupportBot bot) {
        String search = args.get(0);
        try {
            EmbedBuilder embedBuilder = EmbedTemplates.PRETTY_SUCCESSFULL.getEmbed();
            String docUrl = "https://jd.bhop.me/bjdautilities/me/bhop/bjdautilities/command/response/DefaultCommandResponses.html";
            Document document = Jsoup.parse(new URL(docUrl), 6000);
            //Element e= document.head().select("link[href~=.*.ico]").first();
            //String faviconUrl = e.attr("href");
            for (Element li : document.getElementsByClass("blockList")) {
                for (Element element:  li.getElementsByTag("li")) {
                    embedBuilder.setAuthor(document.title(), docUrl);
                    break;
                }
            }
            bot.getMessenger().sendEmbed(channel, embedBuilder.build(), 15);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return CommandResult.success();
    }
}
