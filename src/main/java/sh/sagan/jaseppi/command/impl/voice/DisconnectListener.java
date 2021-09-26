package sh.sagan.jaseppi.command.impl.voice;

import org.javacord.api.event.channel.server.voice.ServerVoiceChannelMemberLeaveEvent;
import org.javacord.api.listener.channel.server.voice.ServerVoiceChannelMemberLeaveListener;
import sh.sagan.jaseppi.Jaseppi;

public class DisconnectListener implements ServerVoiceChannelMemberLeaveListener {

    private final Jaseppi jaseppi;

    public DisconnectListener(Jaseppi jaseppi) {
        this.jaseppi = jaseppi;
    }

    @Override
    public void onServerVoiceChannelMemberLeave(ServerVoiceChannelMemberLeaveEvent event) {
        if (event.getUser().isYourself()) {
            jaseppi.getAudioPlayer().clear();
            jaseppi.getAudioPlayer().repeat(false);
        }
    }
}
