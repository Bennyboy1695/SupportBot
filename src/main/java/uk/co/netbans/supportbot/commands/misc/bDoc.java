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
import uk.co.netbans.supportbot.NetBansBot;

import java.io.IOException;

@Command("bdoc")
public class bDoc {
    private static final String BASE_URL = "https://jd.bhop.me/bjdautilities/";


    @Execute
    public CommandResult onList(Member member, TextChannel channel, Message message, String label, java.util.List<String> args, NetBansBot bot) {
        String className = args.get(0).split("#")[0];
        String methodName = args.get(0).split("#")[1];


        Document classDocument = null;

        try {
            Document classList = Jsoup.connect(BASE_URL + "allclasses-frame.html").get();

            for (Element element : classList.body().getElementsByClass("indexContainer")
                    .first().getElementsByTag("ul").first().getElementsByTag("li")) {
                Element a = element.getElementsByTag("a").first();
                if (!a.text().equalsIgnoreCase(className)) continue;
                classDocument = Jsoup.connect(BASE_URL + a.attr("href")).get();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (classDocument == null)
            System.out.println("Unable to locate class document!");

        for (Element methodOuter : classDocument.body().getElementsByClass("contentContainer").first()
                .getElementsByClass("details").first()
                .getElementsByTag("ul").first()
                .getElementsByTag("li").first()
                .getElementsByTag("ul").get(1)
                .getElementsByTag("li").first()
                .getElementsByTag("ul")) {
            Element method = methodOuter.getElementsByClass("blockList").first();
            if (!method.getElementsByTag("h4").first().text().equalsIgnoreCase(methodName))
                continue;

            System.out.println("Found method: " + method.getElementsByTag("h4").first().text());
        }

        return CommandResult.success();
    }
}
