package uk.co.netbans.supportbot.Task;

import uk.co.netbans.supportbot.NetBansBot;

import java.util.TimerTask;

import static uk.co.netbans.supportbot.Utils.Util.doExpiryCheck;

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
