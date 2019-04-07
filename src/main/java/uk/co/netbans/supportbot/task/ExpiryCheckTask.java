package uk.co.netbans.supportbot.task;

import uk.co.netbans.supportbot.SupportBot;

import java.util.TimerTask;

import static uk.co.netbans.supportbot.utils.Util.doExpiryCheck;

public class ExpiryCheckTask extends TimerTask {

    private SupportBot bot;

    public ExpiryCheckTask(SupportBot bot) {
     this.bot = bot;
    }

    @Override
    public void run() {
        doExpiryCheck(bot);
    }

}
