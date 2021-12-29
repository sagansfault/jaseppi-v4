package sh.sagan.jaseppi.command.impl.music;

import com.sedmelluq.discord.lavaplayer.player.FunctionalResultHandler;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import org.javacord.api.audio.AudioConnection;
import org.javacord.api.audio.AudioSource;
import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.server.Server;
import sh.sagan.jaseppi.Jaseppi;
import sh.sagan.jaseppi.audio.LavaplayerAudioSource;
import sh.sagan.jaseppi.audio.TrackManager;
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

        if (message.getServer().isEmpty()) {
            message.getChannel().sendMessage("Only works in a sever");
            return;
        }

        Server server = message.getServer().get();
        String linkOrQuery = String.join(" ", args);

        Optional<ServerVoiceChannel> voiceChannelOpt = message.getAuthor().getConnectedVoiceChannel();
        Optional<AudioConnection> presentConnOpt = server.getAudioConnection();

        if (voiceChannelOpt.isPresent()) {
            ServerVoiceChannel voiceChannel = voiceChannelOpt.get();
            if (presentConnOpt.isPresent()) {
                AudioConnection presentConn = presentConnOpt.get();

                // both connected to voice and both in the same channel
                if (voiceChannel.getId() == presentConn.getChannel().getId()) {
                    this.play(message, presentConn.getChannel(), presentConn, linkOrQuery);
                } else {
                    // both connected but not in the same voice channel, move over
                    voiceChannel.connect().thenAccept(properAudioConn -> {
                        this.play(message, properAudioConn.getChannel(), properAudioConn, linkOrQuery);
                    });
                }
            } else {
                // user is connected but we aren't
                voiceChannel.connect().thenAccept(audioConn -> {
                    this.play(message, audioConn.getChannel(), audioConn, linkOrQuery);
                });
            }
        } else {
            // they are not connected but we are, just play the new song, don't move
            if (presentConnOpt.isPresent()) {
                AudioConnection presentConn = presentConnOpt.get();
                this.play(message, presentConn.getChannel(), presentConn, linkOrQuery);
            } else {
                // neither we nor they are connected to a voice channel
                message.getChannel().sendMessage("We ain't in vc ( O_o)");
            }
        }
    }

    public void play(Message message, ServerVoiceChannel channel, AudioConnection audioConn, String linkOrQuery) {
        AudioSource source = new LavaplayerAudioSource(jaseppi.getApi(), jaseppi.getAudioPlayer());
        audioConn.setAudioSource(source);
        audioConn.setSelfDeafened(true);

        TrackManager trackManager = jaseppi.getTrackManager();
        TextChannel textChannel = message.getChannel();

        boolean isUrl = isUrl(linkOrQuery);

        jaseppi.getAudioPlayerManager().loadItemOrdered(
                channel.getServer(),
                isUrl ? linkOrQuery : "ytsearch:" + linkOrQuery,
                new FunctionalResultHandler(track -> {
                    trackManager.queue(track);
                    textChannel.sendMessage("Queued" + (isUrl ? " it" : ": " + track.getInfo().uri));
                }, playlist -> {
                    // fuck playlists
                    AudioTrack first = playlist.getTracks().get(0);
                    trackManager.queue(first);
                    textChannel.sendMessage("Queued" + (isUrl ? " it" : ": " + first.getInfo().uri));
                }, () -> {
                    textChannel.sendMessage("No matches found");
                }, exception -> {
                    textChannel.sendMessage("Something went wrong: " + exception.getMessage());
                })
        );
    }

    private boolean isUrl(String input) {
        return input.startsWith("https://") || input.startsWith("http://");
    }
}
