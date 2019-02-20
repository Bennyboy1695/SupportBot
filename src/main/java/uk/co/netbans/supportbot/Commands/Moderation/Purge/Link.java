package uk.co.netbans.supportbot.Commands.Moderation.Purge;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import uk.co.netbans.supportbot.CommandFramework.Command;
import uk.co.netbans.supportbot.CommandFramework.CommandArgs;
import uk.co.netbans.supportbot.CommandFramework.CommandCategory;
import uk.co.netbans.supportbot.CommandFramework.CommandResult;
import uk.co.netbans.supportbot.Message.Messenger;
import uk.co.netbans.supportbot.NetBansBot;
import uk.co.netbans.supportbot.Utils.Util;

public class Link {

    @Command(name = "purge~link", displayName = "link", permission = "supportbot.command.moderation.purge.link", usage = "purge link amount [<user>]", category = CommandCategory.MODERATION)
    public CommandResult onPurgeUser(CommandArgs commandArgs) {
        NetBansBot bot = commandArgs.getBot();
        String[] args = commandArgs.getArgs();
        TextChannel channel = (TextChannel) commandArgs.getChannel();
        int amount = Integer.parseInt(args[1]);
        int count = 0;
        Member member = null;
        if (amount > 100) {
            bot.getMessenger().sendEmbed(channel, Messenger.AMOUNT_TOO_HIGH, 10);
            return CommandResult.INVALIDARGS;
        }

        if (args.length == 3) {
            member = bot.getJDA().getGuildById(bot.getGuildID()).getMemberById(args[2].replaceAll("<", "").replaceAll("@", "").replaceAll("!", "").replaceAll(">", ""));
        }
        for (Message msg : channel.getHistory().retrievePast(100).complete()) {
            if (member != null && Util.containsLink(msg.getContentRaw()) && count < amount) {
                msg.delete().reason("purge requested by " + commandArgs.getMember().getEffectiveName()).complete();
                count++;
            } else if (Util.containsLink(msg.getContentRaw()) && count < amount) {
                msg.delete().reason("purge requested by " + commandArgs.getMember().getEffectiveName()).complete();
                count++;
            }
        }
        if (count > 0){
            if (member != null) {
                bot.getMessenger().sendEmbed(channel, Messenger.DELETED_AMOUNT_OF_MESSAGES(count, member.getEffectiveName()), 10);
            } else {
                bot.getMessenger().sendEmbed(channel, Messenger.DELETED_AMOUNT_OF_MESSAGES(count), 10);
            }
            return CommandResult.SUCCESS;
        } else {
            bot.getMessenger().sendEmbed(channel, Messenger.NO_MESSAGES_FOUND, 10);
        }
        return CommandResult.TARGETNOTFOUND;
    }
}
