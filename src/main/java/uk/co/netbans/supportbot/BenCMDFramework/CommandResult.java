package uk.co.netbans.supportbot.BenCMDFramework;

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
