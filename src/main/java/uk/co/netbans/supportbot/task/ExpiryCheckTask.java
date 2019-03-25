package uk.co.netbans.supportbot.task;

import uk.co.netbans.supportbot.NetBansBot;

import java.util.TimerTask;

import static uk.co.netbans.supportbot.utils.Util.doExpiryCheck;

public class ExpiryCheckTask extends TimerTask {

    private NetBansBot bot;

    public ExpiryCheckTask(NetBansBot bot) {
     this.bot = bot;
    }

    @Override
    public void run() {
        doExpiryCheck(bot);
    }

}
