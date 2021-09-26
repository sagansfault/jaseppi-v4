package sh.sagan.jaseppi.command.impl.voice;

import org.javacord.api.audio.AudioConnection;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.server.Server;
import sh.sagan.jaseppi.Jaseppi;
import sh.sagan.jaseppi.command.lib.Command;

public class LeaveCommand extends Command {

    private final Jaseppi jaseppi;

    public LeaveCommand(Jaseppi jaseppi) {
        super("leave", "fuckoff");

        this.jaseppi = jaseppi;
    }

    @Override
    public void run(Message message, String[] args) {
        message.getServer().flatMap(Server::getAudioConnection).ifPresent(AudioConnection::close);
    }
}
