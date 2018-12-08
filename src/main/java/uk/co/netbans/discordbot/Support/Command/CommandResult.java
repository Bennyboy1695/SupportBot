package uk.co.netbans.discordbot.Support.Command;

public enum CommandResult {
    SUCCESS(),
    INVALIDARGS(),
    TARGETNOTFOUND(),
    NOPERMS(),
    CONSOLEONLY(),
    PLAYERONLY()
}
