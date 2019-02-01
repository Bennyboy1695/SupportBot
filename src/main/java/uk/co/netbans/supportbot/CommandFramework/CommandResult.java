package uk.co.netbans.supportbot.CommandFramework;

public enum CommandResult {
    DEFAULT,
    SUCCESS(),
    INVALIDARGS(),
    INVALIDCOMMAND(),
    TARGETNOTFOUND(),
    NOPERMS(),
    CONSOLEONLY(),
    PLAYERONLY()
}
