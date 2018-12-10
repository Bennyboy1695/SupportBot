package uk.co.netbans.supportbot;

public enum PermType {
    ADMIN(10),
    MOD(5),
    DEFAULT(0);

    private int priority;
    PermType(int priority) {
        this.priority = priority;
    }
    public int getPriority() {
        return priority;
    }
}


