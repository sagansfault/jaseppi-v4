package sh.sagan.jaseppi.command.impl.voice;

import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.entity.message.Message;
import sh.sagan.jaseppi.Jaseppi;
import sh.sagan.jaseppi.command.lib.Command;

import java.util.Optional;

public class SkipCommand extends Command {

    private final Jaseppi jaseppi;

    public SkipCommand(Jaseppi jaseppi) {
        super("skip", "s");

        this.jaseppi = jaseppi;
    }

    @Override
    public void run(Message message, String[] args) {
        Optional<ServerVoiceChannel> channelOptional = message.getAuthor().getConnectedVoiceChannel();
        if (channelOptional.isPresent()) {
            if (jaseppi.getApi().getYourself().isConnected(channelOptional.get())) {
                jaseppi.getAudioPlayer().skip();
            }
        } else {
            message.reply("对你妈妈的诅咒");
        }
    }
}
