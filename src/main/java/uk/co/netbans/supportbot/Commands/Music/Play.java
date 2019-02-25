//package uk.co.netbans.supportbot.Commands.Music;
//
//import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
//import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
//import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
//import net.dv8tion.jda.core.entities.Message;
//import net.dv8tion.jda.core.entities.TextChannel;
//import uk.co.netbans.supportbot.CommandFramework.Command;
//import uk.co.netbans.supportbot.CommandFramework.CommandArgs;
//import uk.co.netbans.supportbot.CommandFramework.CommandCategory;
//import uk.co.netbans.supportbot.CommandFramework.CommandResult;
//import uk.co.netbans.supportbot.Music.AudioHandler;
//
//public class Play {
//    @Command(name = "play", displayName = "play", permission = "supportbot.command.music.play", category = CommandCategory.MUSIC)
//    public CommandResult onPlay(CommandArgs arguments) {
//        String[] args = arguments.getArgs();
//        AudioHandler handler = (AudioHandler) arguments.getChannel().getGuild().getAudioManager().getSendingHandler();
//        if (args.length == 0) {
//            if (handler.getPlayer().getPlayingTrack() != null && handler.getPlayer().isPaused()) {
//                handler.getPlayer().setPaused(false);
//                arguments.getBot().getMessenger().sendMessage((TextChannel) arguments.getChannel(), "Resumed **"+handler.getPlayer().getPlayingTrack().getInfo().title+"**.", 5);
//                return CommandResult.SUCCESS;
//            } else
//                return CommandResult.INVALIDARGS;
//        }
//
//        arguments.getBot().getAudioHandler().loadItemOrdered(
//                arguments.getChannel().getGuild(),
//                arguments.getMessage().getAttachments().get(0).getUrl(),
//                new ResultHandler(arguments.getMessage(), false));
//
//
//        return CommandResult.SUCCESS;
//    }
//
//    private class ResultHandler implements AudioLoadResultHandler
//    {
//        private final Message m;
//        private final boolean ytsearch;
//
//        private ResultHandler(Message m, boolean ytsearch)
//        {
//            this.m = m;
//            this.ytsearch = ytsearch;
//        }
//
//        private void loadSingle(AudioTrack track, AudioPlaylist playlist)
//        {
//            if(bot.getConfig().isTooLong(track))
//            {
//                m.editMessage(FormatUtil.filter(event.getClient().getWarning()+" This track (**"+track.getInfo().title+"**) is longer than the allowed maximum: `"
//                        +FormatUtil.formatTime(track.getDuration())+"` > `"+FormatUtil.formatTime(bot.getConfig().getMaxSeconds()*1000)+"`")).queue();
//                return;
//            }
//            AudioHandler handler = (AudioHandler)event.getGuild().getAudioManager().getSendingHandler();
//            int pos = handler.addTrack(new QueuedTrack(track, event.getAuthor()))+1;
//            String addMsg = FormatUtil.filter(event.getClient().getSuccess()+" Added **"+track.getInfo().title
//                    +"** (`"+FormatUtil.formatTime(track.getDuration())+"`) "+(pos==0?"to begin playing":" to the queue at position "+pos));
//            if(playlist==null || !event.getSelfMember().hasPermission(event.getTextChannel(), Permission.MESSAGE_ADD_REACTION))
//                m.editMessage(addMsg).queue();
//            else
//            {
//                new ButtonMenu.Builder()
//                        .setText(addMsg+"\n"+event.getClient().getWarning()+" This track has a playlist of **"+playlist.getTracks().size()+"** tracks attached. Select "+LOAD+" to load playlist.")
//                        .setChoices(LOAD, CANCEL)
//                        .setEventWaiter(bot.getWaiter())
//                        .setTimeout(30, TimeUnit.SECONDS)
//                        .setAction(re ->
//                        {
//                            if(re.getName().equals(LOAD))
//                                m.editMessage(addMsg+"\n"+event.getClient().getSuccess()+" Loaded **"+loadPlaylist(playlist, track)+"** additional tracks!").queue();
//                            else
//                                m.editMessage(addMsg).queue();
//                        }).setFinalAction(m ->
//                {
//                    try{ m.clearReactions().queue(); }catch(PermissionException ignore) {}
//                }).build().display(m);
//            }
//        }
//
//        private int loadPlaylist(AudioPlaylist playlist, AudioTrack exclude)
//        {
//            int[] count = {0};
//            playlist.getTracks().stream().forEach((track) -> {
//                if(!bot.getConfig().isTooLong(track) && !track.equals(exclude))
//                {
//                    AudioHandler handler = (AudioHandler)event.getGuild().getAudioManager().getSendingHandler();
//                    handler.addTrack(new QueuedTrack(track, event.getAuthor()));
//                    count[0]++;
//                }
//            });
//            return count[0];
//        }
//
//        @Override
//        public void trackLoaded(AudioTrack track)
//        {
//            loadSingle(track, null);
//        }
//
//        @Override
//        public void playlistLoaded(AudioPlaylist playlist)
//        {
//            if(playlist.getTracks().size()==1 || playlist.isSearchResult())
//            {
//                AudioTrack single = playlist.getSelectedTrack()==null ? playlist.getTracks().get(0) : playlist.getSelectedTrack();
//                loadSingle(single, null);
//            }
//            else if (playlist.getSelectedTrack()!=null)
//            {
//                AudioTrack single = playlist.getSelectedTrack();
//                loadSingle(single, playlist);
//            }
//            else
//            {
//                int count = loadPlaylist(playlist, null);
//                if(count==0)
//                {
//                    // todo
////                    m.editMessage(FormatUtil.filter(event.getClient().getWarning()+" All entries in this playlist "+(playlist.getName()==null ? "" : "(**"+playlist.getName()
////                            +"**) ")+"were longer than the allowed maximum (`"+bot.getConfig().getMaxTime()+"`)")).queue();
//                }
//                else
//                {
//                    m.editMessage(FormatUtil.filter(event.getClient().getSuccess()+" Found "
//                            +(playlist.getName()==null?"a playlist":"playlist **"+playlist.getName()+"**")+" with `"
//                            + playlist.getTracks().size()+"` entries; added to the queue!"
//                            + (count<playlist.getTracks().size() ? "\n"+event.getClient().getWarning()+" Tracks longer than the allowed maximum (`"
//                            + bot.getConfig().getMaxTime()+"`) have been omitted." : ""))).queue();
//                }
//            }
//        }
//
//        @Override
//        public void noMatches()
//        {
//            if(ytsearch)
//                m.editMessage(FormatUtil.filter(event.getClient().getWarning()+" No results found for `"+event.getArgs()+"`.")).queue();
//            else
//                bot.getPlayerManager().loadItemOrdered(event.getGuild(), "ytsearch:"+event.getArgs(), new ResultHandler(m,event,true));
//        }
//
//        @Override
//        public void loadFailed(FriendlyException throwable)
//        {
//            if(throwable.severity==Severity.COMMON)
//                m.editMessage(event.getClient().getError()+" Error loading: "+throwable.getMessage()).queue();
//            else
//                m.editMessage(event.getClient().getError()+" Error loading track.").queue();
//        }
//    }
//
//    public class PlaylistCmd extends MusicCommand
//    {
//        public PlaylistCmd(Bot bot)
//        {
//            super(bot);
//            this.name = "playlist";
//            this.aliases = new String[]{"pl"};
//            this.arguments = "<name>";
//            this.help = "plays the provided playlist";
//            this.beListening = true;
//            this.bePlaying = false;
//        }
//
//        @Override
//        public void doCommand(CommandEvent event)
//        {
//            if(event.getArgs().isEmpty())
//            {
//                event.reply(event.getClient().getError()+" Please include a playlist name.");
//                return;
//            }
//            Playlist playlist = bot.getPlaylistLoader().getPlaylist(event.getArgs());
//            if(playlist==null)
//            {
//                event.replyError("I could not find `"+event.getArgs()+".txt` in the Playlists folder.");
//                return;
//            }
//            event.getChannel().sendMessage(loadingEmoji+" Loading playlist **"+event.getArgs()+"**... ("+playlist.getItems().size()+" items)").queue(m ->
//            {
//                AudioHandler handler = (AudioHandler)event.getGuild().getAudioManager().getSendingHandler();
//                playlist.loadTracks(bot.getPlayerManager(), (at)->handler.addTrack(new QueuedTrack(at, event.getAuthor())), () -> {
//                    StringBuilder builder = new StringBuilder(playlist.getTracks().isEmpty()
//                            ? event.getClient().getWarning()+" No tracks were loaded!"
//                            : event.getClient().getSuccess()+" Loaded **"+playlist.getTracks().size()+"** tracks!");
//                    if(!playlist.getErrors().isEmpty())
//                        builder.append("\nThe following tracks failed to load:");
//                    playlist.getErrors().forEach(err -> builder.append("\n`[").append(err.getIndex()+1).append("]` **").append(err.getItem()).append("**: ").append(err.getReason()));
//                    String str = builder.toString();
//                    if(str.length()>2000)
//                        str = str.substring(0,1994)+" (...)";
//                    m.editMessage(FormatUtil.filter(str)).queue();
//                });
//            });
//        }
//    }
//}
