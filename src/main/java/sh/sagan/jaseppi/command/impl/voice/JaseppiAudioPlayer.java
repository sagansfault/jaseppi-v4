package sh.sagan.jaseppi.command.impl.voice;

import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEvent;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventListener;
import com.sedmelluq.discord.lavaplayer.player.event.TrackEndEvent;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import java.util.ArrayList;
import java.util.List;

public class JaseppiAudioPlayer extends DefaultAudioPlayer implements AudioEventListener {

    private final List<AudioTrack> queue = new ArrayList<>();

    private boolean repeat = false;

    /**
     * @param manager Audio player manager which this player is attached to
     */
    public JaseppiAudioPlayer(DefaultAudioPlayerManager manager) {
        super(manager);
    }

    @Override
    public void playTrack(AudioTrack track) {
        this.queue.add(track);
        if (this.queue.size() == 1 && super.getPlayingTrack() == null) {
            super.startTrack(this.queue.remove(0), false);
        }
    }

    public void skip() {
        this.repeat = false;
        if (super.getPlayingTrack() != null) {
            super.stopTrack();
        }
        if (!this.queue.isEmpty()) {
            super.startTrack(queue.remove(0), false);
        }
    }

    public void clear() {
        super.stopTrack();
        this.queue.clear();
    }

    public void repeat(boolean repeat) {
        this.repeat = repeat;
    }

    public boolean isRepeat() {
        return this.repeat;
    }

    @Override
    public void onEvent(AudioEvent event) {
        if (event instanceof TrackEndEvent trackEndEvent) {
            if (this.repeat) {
                super.startTrack(trackEndEvent.track.makeClone(), false);
                return;
            }
            if (!this.queue.isEmpty()) {
                super.startTrack(this.queue.remove(0), false);
            }
        }
    }
}
