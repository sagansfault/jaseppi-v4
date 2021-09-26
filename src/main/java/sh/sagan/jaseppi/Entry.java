package sh.sagan.jaseppi;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;

public class Entry {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("!!! MISSING TOKEN !!!");
            return;
        }
        DiscordApi api = new DiscordApiBuilder().setToken(args[0]).login().join();

        new Jaseppi(api);
    }
}
