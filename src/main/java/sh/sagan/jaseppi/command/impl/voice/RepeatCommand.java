package sh.sagan.jaseppi.command.impl.voice;

import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.entity.message.Message;
import sh.sagan.jaseppi.Jaseppi;
import sh.sagan.jaseppi.command.lib.Command;

import java.util.Optional;

public class RepeatCommand extends Command {

    private final Jaseppi jaseppi;

    public RepeatCommand(Jaseppi jaseppi) {
        super("repeat");

        this.jaseppi = jaseppi;
    }

    @Override
    public void run(Message message, String[] args) {
        Optional<ServerVoiceChannel> channelOpt = message.getAuthor().getConnectedVoiceChannel();
        if (channelOpt.isPresent()) {
            if (jaseppi.getApi().getYourself().isConnected(channelOpt.get())) {
                jaseppi.getAudioPlayer().repeat(!jaseppi.getAudioPlayer().isRepeat());
                boolean currentState = jaseppi.getAudioPlayer().isRepeat();
                message.reply(currentState ? "repeating on" : "repeating off");
            } else {
                message.reply("_1-42414-413486767812");
            }
        }
    }
}
