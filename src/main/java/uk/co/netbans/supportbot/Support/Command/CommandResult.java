package uk.co.netbans.supportbot.Support.Command;

public enum CommandResult {
    SUCCESS(),
    INVALIDARGS(),
    TARGETNOTFOUND(),
    NOPERMS(),
    CONSOLEONLY(),
    PLAYERONLY()
}
