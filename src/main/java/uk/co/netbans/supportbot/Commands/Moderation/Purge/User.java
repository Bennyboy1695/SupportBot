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

public class User {

    @Command(name = "purge~user", displayName = "user", permission = "supportbot.command.moderation.purge.user", usage = "purge user <user> amount", category = CommandCategory.MODERATION)
    public CommandResult onPurgeUser(CommandArgs commandArgs) {
        NetBansBot bot = commandArgs.getBot();
        String[] args = commandArgs.getArgs();
        TextChannel channel = (TextChannel) commandArgs.getChannel();
        int amount = Integer.parseInt(args[2]);
        int count = 0;
        if (amount > 100) {
            bot.getMessenger().sendEmbed(channel, Messenger.AMOUNT_TOO_HIGH, 10);
            return CommandResult.INVALIDARGS;
        }

        Member member = bot.getJDA().getGuildById(Long.valueOf((String) bot.getConf().get("guildID"))).getMemberById(args[1].replaceAll("<", "").replaceAll("@", "").replaceAll("!", "").replaceAll(">", ""));
        for (Message msg : channel.getHistory().retrievePast(amount).complete()) {
                if (msg.getAuthor() == member.getUser()) {
                    msg.delete().reason("purge requested by " + commandArgs.getMember().getEffectiveName()).complete();
                    count++;
                }
        }
        if (count > 0){
            bot.getMessenger().sendEmbed(channel, Messenger.DELETED_AMOUNT_OF_MESSAGES(count, member.getEffectiveName()), 10);
            return CommandResult.SUCCESS;
        }
        return CommandResult.TARGETNOTFOUND;
    }
}
