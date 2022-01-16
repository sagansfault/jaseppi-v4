package sh.sagan.jaseppi.function;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;
import sh.sagan.jaseppi.Jaseppi;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.ThreadLocalRandom;

public class CursedImageReply implements MessageCreateListener {

    private final Jaseppi jaseppi;

    public CursedImageReply(Jaseppi jaseppi) {
        this.jaseppi = jaseppi;
    }

    @Override
    public void onMessageCreate(MessageCreateEvent event) {
        int rand = ThreadLocalRandom.current().nextInt(1000);

        String urlFormat = "https://www.reddit.com/r/%s/%s.json?limit=%s";

        if (rand == 0) {
            HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(String.format(urlFormat, "cursedimages", "hot", "10"))).build();

            jaseppi.getHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenAccept(res -> {
                        JsonObject base = JsonParser.parseString(res).getAsJsonObject();
                        JsonObject data = base.getAsJsonObject("data");
                        JsonArray children = data.getAsJsonArray("children");
                        JsonObject randomPost = children.get(ThreadLocalRandom.current().nextInt(2, 10)).getAsJsonObject();
                        JsonObject childData = randomPost.getAsJsonObject("data");
                        event.getChannel().sendMessage(childData.getAsJsonPrimitive("url").getAsString());
                    });
        }
    }
}
