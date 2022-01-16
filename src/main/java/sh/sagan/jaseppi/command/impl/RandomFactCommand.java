package sh.sagan.jaseppi.command.impl;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.javacord.api.entity.message.Message;
import sh.sagan.jaseppi.command.lib.Command;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class RandomFactCommand extends Command {

    private static final String URL_FORMAT = "https://uselessfacts.jsph.pl/random.json?language=en";

    private final HttpClient httpClient;

    public RandomFactCommand(HttpClient httpClient) {
        super("fact");
        this.httpClient = httpClient;
    }

    @Override
    public void run(Message message, String[] args) {

        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(URL_FORMAT)).build();

        httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(res -> {
                    JsonObject base = JsonParser.parseString(res).getAsJsonObject();
                    String text = base.get("text").getAsString();
                    message.reply(text);
                });
    }
}
