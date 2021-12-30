package sh.sagan.jaseppi.command.impl.music;

import org.javacord.api.entity.message.Message;
import sh.sagan.jaseppi.Jaseppi;
import sh.sagan.jaseppi.command.lib.Command;

public class RepeatCommand extends Command {

    private final Jaseppi jaseppi;

    public RepeatCommand(Jaseppi jaseppi) {
        super("repeat", "r");
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
                    boolean newValue = !jaseppi.getTrackManager().isRepeat();
                    jaseppi.getTrackManager().setRepeat(newValue);
                    message.getChannel().sendMessage("Repeat: " + newValue);
                });
            });
        });
    }
}
