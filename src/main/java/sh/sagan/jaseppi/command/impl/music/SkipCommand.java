package sh.sagan.jaseppi.command.impl.music;

import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.server.Server;
import sh.sagan.jaseppi.Jaseppi;
import sh.sagan.jaseppi.command.lib.Command;

public class SkipCommand extends Command {

    private final Jaseppi jaseppi;

    public SkipCommand(Jaseppi jaseppi) {
        super("skip");
        this.jaseppi = jaseppi;
    }

    @Override
    public void run(Message message, String[] args) {

        // Gator banned from dj commands except 'play'
        if (message.getAuthor().getId() == 160339567747137536L) {
            message.reply("https://media1.giphy.com/media/wSSooF0fJM97W/200.gif");
            return;
        }

        message.getServer().flatMap(Server::getAudioConnection).ifPresent(audioConnection -> {
            jaseppi.getTrackManager().startNext();
        });
    }
}
