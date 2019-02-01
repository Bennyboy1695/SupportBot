package uk.co.netbans.supportbot.CommandFramework;

import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import uk.co.netbans.supportbot.NetBansBot;

public class CommandArgs {

    private NetBansBot bot;
    private Member member;
    private Message message;
    private Channel channel;
    private String label;
    private String[] args;

    public CommandArgs(NetBansBot bot, Channel channel, Member sender, Message message, String label, String[] args, int subCommand) {
        String[] modArgs = new String[args.length - subCommand];
        for (int i = 0; i < args.length - subCommand; i++) {
            modArgs[i] = args[i + subCommand];
        }

        StringBuffer buffer = new StringBuffer();
        buffer.append(label);
        for (int x = 0; x < subCommand; x++) {
            buffer.append("~" + args[x]);
        }
        String cmdLabel = buffer.toString();
        this.bot = bot;
        this.channel = channel;
        this.member = sender;
        this.message = message;
        this.label = cmdLabel;
        this.args = modArgs;
    }

    public NetBansBot getBot() {
        return bot;
    }

    public Channel getChannel() {
        return channel;
    }

    public Message getMessage() {
        return message;
    }

    public String getLabel() {
        return label;
    }

    public String[] getArgs() {
        return args;
    }

    public String getArgs(int index) {
        return args[index];
    }

    public int length() {
        return args.length;
    }

    public Member getMember() {
        if (member instanceof Member) {
            return (Member) member;
        } else {
            return null;
        }
    }
}
