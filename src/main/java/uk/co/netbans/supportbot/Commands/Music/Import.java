package uk.co.netbans.supportbot.Commands.Music;

import uk.co.netbans.supportbot.CommandFramework.Command;
import uk.co.netbans.supportbot.CommandFramework.CommandArgs;
import uk.co.netbans.supportbot.CommandFramework.CommandCategory;
import uk.co.netbans.supportbot.CommandFramework.CommandResult;

public class Import {

    @Command(name = "import", displayName = "import", permission = "supportbot.command.music.import", category = CommandCategory.MUSIC)
    public CommandResult onImport(CommandArgs commandArgs) {

        return CommandResult.SUCCESS;
    }
}
