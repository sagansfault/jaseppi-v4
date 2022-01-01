package sh.sagan.jaseppi;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import org.javacord.api.DiscordApi;
import sh.sagan.jaseppi.audio.TrackManager;
import sh.sagan.jaseppi.command.impl.RedditCommand;
import sh.sagan.jaseppi.command.impl.RollDiceCommand;
import sh.sagan.jaseppi.command.impl.SayCommand;
import sh.sagan.jaseppi.command.impl.music.LeaveCommand;
import sh.sagan.jaseppi.command.impl.music.PlayCommand;
import sh.sagan.jaseppi.command.impl.music.RepeatCommand;
import sh.sagan.jaseppi.command.impl.music.SkipCommand;
import sh.sagan.jaseppi.command.lib.CommandManager;
import sh.sagan.jaseppi.command.lib.IntakeDelegator;
import sh.sagan.jaseppi.function.CursedImageReply;

import java.net.http.HttpClient;

public class Jaseppi {

    public static final String PREFIX = ".";

    private final DiscordApi api;

    private final AudioPlayerManager audioPlayerManager = new DefaultAudioPlayerManager();
    private final AudioPlayer audioPlayer;
    private final TrackManager trackManager;

    private final CommandManager commandManager;
    private final HttpClient httpClient;

    public Jaseppi(DiscordApi api) {
        this.api = api;

        commandManager = new CommandManager();
        httpClient = HttpClient.newBuilder().build();

        audioPlayerManager.registerSourceManager(new YoutubeAudioSourceManager(true));
        audioPlayer = audioPlayerManager.createPlayer();
        this.trackManager = new TrackManager(api, audioPlayer);
        audioPlayer.addListener(trackManager);

        initListeners();
        initCommands();
    }

    private void initListeners() {
        api.addMessageCreateListener(new IntakeDelegator(commandManager));
        api.addMessageCreateListener(new CursedImageReply(this));
    }

    private void initCommands() {
        commandManager.register(new SayCommand());
        commandManager.register(new RedditCommand(this.httpClient));

        commandManager.register(new PlayCommand(this));
        commandManager.register(new SkipCommand(this));
        commandManager.register(new LeaveCommand(this));
        commandManager.register(new RepeatCommand(this));
        commandManager.register(new RollDiceCommand(this));
    }

    public DiscordApi getApi() {
        return api;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public HttpClient getHttpClient() {
        return httpClient;
    }

    public AudioPlayerManager getAudioPlayerManager() {
        return audioPlayerManager;
    }

    public AudioPlayer getAudioPlayer() {
        return audioPlayer;
    }

    public TrackManager getTrackManager() {
        return trackManager;
    }
}
