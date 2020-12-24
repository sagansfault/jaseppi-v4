package sh.sagan.jaseppi;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import sh.sagan.jaseppi.chat.TagJaseppiListener;
import sh.sagan.jaseppi.command.impl.SayCommand;
import sh.sagan.jaseppi.command.lib.CommandManager;

public class Jaseppi {

    public static void main(String[] args) {

        final String token = "";

        DiscordApi api = new DiscordApiBuilder().setToken(token).login().join();

        // commands
        CommandManager.getInstance().register(new SayCommand());

        // listeners
        api.addListener(new CommandManager.Delegator());
        api.addListener(new TagJaseppiListener(api));
    }
}
