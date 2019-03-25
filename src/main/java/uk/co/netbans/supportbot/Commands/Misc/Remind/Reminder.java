package uk.co.netbans.supportbot.Commands.Misc.Remind;

public class Reminder {

    private long userID;
    private long creation;
    private long expiry;
    private String message;

    public Reminder(long userID, long creation, long expiry, String message) {
        this.userID = userID;
        this.creation = creation;
        this.expiry = expiry;
        this.message = message;
    }

    public long getCreation() {
        return creation;
    }

    public long getExpiry() {
        return expiry;
    }

    public long getUserID() {
        return userID;
    }

    public String getMessage() {
        return message;
    }
}
