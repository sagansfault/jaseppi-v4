package sh.sagan.jaseppi.command.impl.voice;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.entity.message.Message;
import sh.sagan.jaseppi.Jaseppi;
import sh.sagan.jaseppi.command.lib.Command;

import java.util.Optional;

public class PlayCommand extends Command {

    private final Jaseppi jaseppi;

    public PlayCommand(Jaseppi jaseppi) {
        super("play", "p");

        this.jaseppi = jaseppi;
    }

    @Override
    public void run(Message message, String[] args) {

        if (args.length < 1) {
            message.reply("gimme sumn");
            return;
        }

        String query = String.join(" ", args);

        Optional<ServerVoiceChannel> connectedOptional = message.getAuthor().getConnectedVoiceChannel();
        if (connectedOptional.isPresent()) {
            ServerVoiceChannel voiceChannel = connectedOptional.get();

            if (jaseppi.getApi().getYourself().isConnected(voiceChannel)) {
                this.load(message, query);
            } else {
                voiceChannel.connect().thenAccept(audioConn -> {
                    audioConn.setAudioSource(jaseppi.getAudioSource());
                    this.load(message, query);
                }).exceptionally(e -> {
                    message.reply("shit went wrong");
                    e.printStackTrace();
                    return null;
                });
            }

        } else {
            message.reply("where");
            return;
        }
    }

    private void load(Message message, String linkOrSearch) {
        String query = linkOrSearch;
        if (!linkOrSearch.startsWith("http")) {
            query = "ytmsearch:" + linkOrSearch;
        }
        String finalQuery = query;
        message.getServer().ifPresent(server -> jaseppi.getAudioPlayerManager().loadItemOrdered(server, finalQuery, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                jaseppi.getAudioPlayer().playTrack(track);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                for (AudioTrack track : playlist.getTracks()) {
                    jaseppi.getAudioPlayer().playTrack(track);
                }
            }

            @Override
            public void noMatches() {
                message.reply("couldn't find stuff");
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                exception.printStackTrace();
                message.reply("shit failed ( O_o)");
            }
        }));
    }
}
