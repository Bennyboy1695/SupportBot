package uk.co.netbans.discordbot.Command;

public enum CommandResult {
    SUCCESS(),
    INVALIDARGS(),
    TARGETNOTFOUND(),
    NOPERMS(),
    CONSOLEONLY(),
    PLAYERONLY()
}
