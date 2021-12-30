package sh.sagan.jaseppi.command.impl.music;

import org.javacord.api.entity.message.Message;
import sh.sagan.jaseppi.Jaseppi;
import sh.sagan.jaseppi.command.lib.Command;

public class LeaveCommand extends Command {

    private final Jaseppi jaseppi;

    public LeaveCommand(Jaseppi jaseppi) {
        super("leave", "stop");
        this.jaseppi = jaseppi;
    }

    @Override
    public void run(Message message, String[] args) {

        // Gator banned from dj commands except 'play'
        if (message.getAuthor().getId() == 160339567747137536L) {
            message.reply("https://media1.giphy.com/media/wSSooF0fJM97W/200.gif");
            return;
        }

        message.getServer().ifPresent(server -> {
            jaseppi.getApi().getYourself().getConnectedVoiceChannel(server).ifPresent(serverVoiceChannel -> {
                server.getAudioConnection().ifPresent(audioConnection -> {
                    jaseppi.getAudioPlayer().stopTrack();
                    audioConnection.close();
                    jaseppi.getTrackManager().clearQueue();
                });
            });
        });
    }
}
