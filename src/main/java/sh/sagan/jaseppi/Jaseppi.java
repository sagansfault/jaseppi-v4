package sh.sagan.jaseppi;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.soundcloud.SoundCloudAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import org.javacord.api.DiscordApi;
import org.javacord.api.audio.AudioSource;
import sh.sagan.jaseppi.command.impl.RedditCommand;
import sh.sagan.jaseppi.command.impl.SayCommand;
import sh.sagan.jaseppi.command.impl.voice.DisconnectListener;
import sh.sagan.jaseppi.command.impl.voice.JaseppiAudioPlayer;
import sh.sagan.jaseppi.command.impl.voice.LeaveCommand;
import sh.sagan.jaseppi.command.impl.voice.PlayCommand;
import sh.sagan.jaseppi.command.impl.voice.RepeatCommand;
import sh.sagan.jaseppi.command.impl.voice.SkipCommand;
import sh.sagan.jaseppi.command.lib.CommandManager;
import sh.sagan.jaseppi.command.lib.IntakeDelegator;
import sh.sagan.jaseppi.function.CursedImageReply;

import java.net.http.HttpClient;

public class Jaseppi {

    public static final String PREFIX = ".";

    private final DiscordApi api;
    private final DefaultAudioPlayerManager audioPlayerManager;
    private final JaseppiAudioPlayer audioPlayer;
    private final AudioSource audioSource;
    private final SoundCloudAudioSourceManager youtubeAudioSourceManager;

    private final CommandManager commandManager;
    private final HttpClient httpClient;

    public Jaseppi(DiscordApi api) {
        this.api = api;

        commandManager = new CommandManager();
        httpClient = HttpClient.newBuilder().build();
        audioPlayerManager = new DefaultAudioPlayerManager();
        youtubeAudioSourceManager = SoundCloudAudioSourceManager.builder().withAllowSearch(true).build();
        audioPlayerManager.registerSourceManager(youtubeAudioSourceManager);
        audioPlayer = new JaseppiAudioPlayer(audioPlayerManager);
        audioSource = new LavaplayerAudioSource(api, audioPlayer);
        audioPlayer.addListener(audioPlayer);

        initListeners();
        initCommands();
    }

    private void initListeners() {
        api.addMessageCreateListener(new IntakeDelegator(commandManager));
        api.addMessageCreateListener(new CursedImageReply(this));
        api.addServerVoiceChannelMemberLeaveListener(new DisconnectListener(this));
    }

    private void initCommands() {
        commandManager.register(new PlayCommand(this));
        commandManager.register(new LeaveCommand(this));
        commandManager.register(new RepeatCommand(this));
        commandManager.register(new SkipCommand(this));

        commandManager.register(new SayCommand());
        commandManager.register(new RedditCommand(this.httpClient));
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

    public JaseppiAudioPlayer getAudioPlayer() {
        return audioPlayer;
    }

    public AudioSource getAudioSource() {
        return audioSource;
    }

    public SoundCloudAudioSourceManager getYoutubeAudioSourceManager() {
        return youtubeAudioSourceManager;
    }
}
