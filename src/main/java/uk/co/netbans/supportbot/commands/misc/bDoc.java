package uk.co.netbans.supportbot.commands.misc;

import me.bhop.bjdautilities.ReactionMenu;
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
import uk.co.netbans.supportbot.SupportBot;

@Command("bdoc")
public class bDoc {
//    private static final String BASE_URL = "https://jd.bhop.me/bjdautilities/";
    private static final String BASE_URL = "https://docs.oracle.com/javase/8/docs/api/";


    @Execute
    public CommandResult onList(Member member, TextChannel channel, Message message, String label, java.util.List<String> args, SupportBot bot) {
        String className = args.get(0).split("#")[0];
        String methodName = args.get(0).split("#")[1];


        Document classDocument = null;
        String link = null;

        try {
            Document classList = Jsoup.connect(BASE_URL + "allclasses-frame.html").get();

            for (Element element : classList.body().getElementsByClass("indexContainer")
                    .first().getElementsByTag("ul").first().getElementsByTag("li")) {
                Element a = element.getElementsByTag("a").first();
                if (!a.text().equalsIgnoreCase(className)) continue;
                link = a.attr("href");
                classDocument = Jsoup.connect(BASE_URL + link).get(); //try setting link inside of connect
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (classDocument == null) {
            System.out.println("Unable to locate class document!");
            return CommandResult.invalidArguments();
        }

        for (Element methodOuter : classDocument.body().getElementsByClass("contentContainer").first()
                .getElementsByClass("details").first()
                .getElementsByTag("ul").first()
                .getElementsByTag("li").first()
                .getElementsByTag("ul").first()
                .getElementsByTag("li").first()
                .getElementsByTag("ul")) {
            Element method = methodOuter.getElementsByClass("blockList").first();
            if (!method.getElementsByTag("h4").first().text().equalsIgnoreCase(methodName))
                continue;

            System.out.println("Found method: " + method.getElementsByTag("h4").first().text());
            EmbedBuilder embed = new EmbedBuilder();

            for (Element a : classDocument.body().getElementsByClass("contentContainer").first()
                    .getElementsByClass("details").first()
                    .getElementsByTag("ul").first()
                    .getElementsByTag("li").first()
                    .getElementsByTag("ul").first()
                    .getElementsByTag("li").first()
                    .getElementsByTag("a")) {
                if (a.attr("name").toLowerCase().startsWith(methodName.toLowerCase() + "-"))
                    embed.setTitle(args.get(0), BASE_URL + link + "#" + a.attr("name"));
            }


            embed.setDescription(method.getElementsByClass("block").first().html()
                    .replace("<p>", "\n")
                    .replace("</p>", "")
                    .replace("<code>", "`")
                    .replace("</code>", "`")
                    .replace("<i>", "*")
                    .replace("</i>", "*")
                    .replace("<b>", "**")
                    .replace("</b>", "**")
                    .replaceAll("<a[^>]*>", "")
                    .replace("</a>", ""));

            String name = null;
            StringBuilder current = null;
            for (Element el : method.getElementsByTag("dl").first().getAllElements()) {
                if (el.tagName().equals("dt")) {
                    if (current != null && name != null) {
                        if (current.charAt(current.length() - 1) == '\n')
                            current.setLength(current.length() - 1);
                        embed.addField(name, current.toString(), false);
                    }

                    name = el.text();
                    current = new StringBuilder();
                } else if (current != null) {
                    current.append(el.html()
                            .replace("<p>", "\n")
                            .replace("</p>", "")
                            .replace("<code>", "`")
                            .replace("</code>", "`")
                            .replace("<i>", "*")
                            .replace("</i>", "*")
                            .replace("<b>", "**")
                            .replace("</b>", "**")
                            .replaceAll("<a[^>]*>", "")
                            .replace("</a>", ""))
                            .append("\n");
                }
            }

            new ReactionMenu.Builder(bot.getJDA())
                    .setEmbed(embed.build())
                    .onClick("\u274C", ReactionMenu::destroy)
                    .buildAndDisplay(channel);

            break;
        }

        return CommandResult.success();
    }
}
