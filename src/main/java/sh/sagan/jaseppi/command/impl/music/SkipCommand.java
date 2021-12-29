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
        message.getServer().flatMap(Server::getAudioConnection).ifPresent(audioConnection -> {
            jaseppi.getTrackManager().startNext();
        });
    }
}
