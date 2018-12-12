package uk.co.netbans.supportbot.Support.Command;

public enum CommandResult {
    SUCCESS(),
    INVALIDARGS(),
    INVALIDCOMMAND(),
    TARGETNOTFOUND(),
    NOPERMS(),
    CONSOLEONLY(),
    PLAYERONLY()
}
