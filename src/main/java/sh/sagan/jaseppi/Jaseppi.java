package sh.sagan.jaseppi;

import org.javacord.api.DiscordApi;
import sh.sagan.jaseppi.command.impl.RedditCommand;
import sh.sagan.jaseppi.command.impl.SayCommand;
import sh.sagan.jaseppi.command.lib.CommandManager;
import sh.sagan.jaseppi.command.lib.IntakeDelegator;
import sh.sagan.jaseppi.function.CursedImageReply;

import java.net.http.HttpClient;

public class Jaseppi {

    public static final String PREFIX = ".";

    private final DiscordApi api;

    private final CommandManager commandManager;
    private final HttpClient httpClient;

    public Jaseppi(DiscordApi api) {
        this.api = api;

        commandManager = new CommandManager();
        httpClient = HttpClient.newBuilder().build();

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
}
