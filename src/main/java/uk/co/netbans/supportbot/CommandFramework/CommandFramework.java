package uk.co.netbans.supportbot.CommandFramework;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import uk.co.netbans.supportbot.Message.Messenger;
import uk.co.netbans.supportbot.NetBansBot;
import uk.co.netbans.supportbot.Storage.SQLManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandFramework extends ListenerAdapter {

    private NetBansBot bot;
    private Map<String, Map.Entry<Method, Object>> commandMap = new HashMap<String, Map.Entry<Method, Object>>();


    public CommandFramework(NetBansBot bot) {
        this.bot = bot;
    }

    public void registerCommands(Object obj) {
        for (Method m : obj.getClass().getMethods()) {
            if (m.getAnnotation(Command.class) != null) {
                Command command = m.getAnnotation(Command.class);
                registerCommand(command, command.name(), m, obj);
                for (String alias : command.aliases()) {
                    registerCommand(command, alias, m, obj);
                }
            }
        }
    }

    public void registerCommand(Command command, String label, Method m, Object obj) {
        commandMap.put(label.toLowerCase(), new AbstractMap.SimpleEntry<Method, Object>(m, obj));
        String cmdLabel = label.split("\\.")[0].toLowerCase();
    }

    public Map<String, Map.Entry<Method, Object>> getCommandMap() {
        return commandMap;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        if (!e.getMessage().getContentRaw().startsWith(bot.getCommandPrefix()) || e.getAuthor().isBot() || e.getMessage().getContentRaw().length() <= 1)
            return;

        CommandResult result = CommandResult.INVALIDCOMMAND;
        String label = getCommandLabel(e.getMessage().getContentRaw()).replaceAll("([!?])", "");
        String[] args = quotesOrSpaceSplits(e.getMessage().getContentRaw());
        args[0] = args[0].substring(1);
        List<String> list = new ArrayList<String>(Arrays.asList(args));
        list.remove(args[0]);
        args = list.toArray(new String[0]);
        for (int i = args.length; i >= 0; i--) {
            StringBuffer buffer = new StringBuffer();
            buffer.append(label.toLowerCase());
            for (int x = 0; x < i; x++) {
                buffer.append("~" + args[x].toLowerCase());
            }
            String cmdLabel = buffer.toString();
            if (commandMap.containsKey(cmdLabel)) {
                Method method = commandMap.get(cmdLabel).getKey();
                Object methodObject = commandMap.get(cmdLabel).getValue();
                Command command = method.getAnnotation(Command.class);
                if (command.permission().equals("none") || userHasPerm(e.getMember().getUser().getIdLong(), command.permission())) {
                    try {
                        result = (CommandResult) method.invoke(methodObject, new CommandArgs(bot, e.getTextChannel(), e.getMember(), e.getMessage(), cmdLabel, args, cmdLabel.split("\\.").length - 1));
                    } catch (IllegalAccessException e1) {
                        e1.printStackTrace();
                    } catch (IllegalArgumentException e1) {
                        e1.printStackTrace();
                    } catch (InvocationTargetException e1) {
                        e1.printStackTrace();
                    }
                } else {
                    result = CommandResult.NOPERMS;
                    bot.getMessenger().sendEmbed(e.getTextChannel(), Messenger.NO_PERMS(command.permission()), 10);
                }
                if (result == CommandResult.INVALIDCOMMAND) {
                    bot.getMessenger().sendEmbed(e.getTextChannel(), Messenger.INVALID_COMMAND, 10);
                }
                if (result == CommandResult.INVALIDARGS) {
                    bot.getMessenger().sendEmbed(e.getTextChannel(), Messenger.INVALID_ARGS(command.usage()), 10);
                }
            } else {
                result = CommandResult.DEFAULT;
            }
            bot.getMessenger().delMessage(e.getTextChannel(), e.getMessageIdLong(), 5);
        }
    }

    private boolean userHasPerm(long userID, String perm) {
        SQLManager sqlManager = bot.getSqlManager();
        for (String groups : sqlManager.getUsersGroups(userID)) {
            for (String perms : sqlManager.getGroupsPerms(groups)) {
                if (perm.equals(perms)) return true;
            }
        }
        return sqlManager.userAlreadyHasPerm(userID, perm);
    }

    private String[] quotesOrSpaceSplits(String str) {
        str += " ";
        ArrayList<String> strings = new ArrayList<String>();
        boolean inQuote = false;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == '"' || c == ' ' && !inQuote) {
                if (c == '"')
                    inQuote = !inQuote;
                if (!inQuote && sb.length() > 0) {
                    strings.add(sb.toString());
                    sb.delete(0, sb.length());
                }
            } else
                sb.append(c);
        }
        return strings.toArray(new String[0]);
    }

    public boolean isEmptyStringArray(String [] array){
        for(int i=0; i<array.length; i++){
            if(array[i]!=null){
                System.out.println("Not Empty!");
                return false;
            }
        }
        return true;
    }

    public String getCommandLabel(String message) {
        String regex = "(([!?])[a-z]*)";
        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(message);
        while (matcher.find()) {
            return matcher.group(1);
        }
        return message;
    }
}
