package sh.sagan.jaseppi.command.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.javacord.api.entity.channel.ChannelCategory;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import sh.sagan.jaseppi.command.lib.Command;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public class RedditCommand extends Command {

    private final String urlFormat = "https://www.reddit.com/r/%s/%s.json?limit=%s";
    private final String[] notAllowedSubs = {"gay", "men", "man", "dick", "cock", "hole", "disgusting", "poop", "feet",
            "shit", "homo", "disease", "penis", "male", "guy", "foot"};

    private final HttpClient httpClient;

    public RedditCommand(HttpClient httpClient) {
        super("reddit");

        this.httpClient = httpClient;
    }

    @Override
    public void run(Message message, String[] args) {

        if (args.length < 1) {
            message.reply("gimme a sub");
            return;
        }

        String sub = args[0];

        for (String notAllowedSub : notAllowedSubs) {
            if (sub.contains(notAllowedSub)) {
                message.reply("\uD83E\uDD14");
                return;
            }
        }

        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(String.format(urlFormat, sub, "hot", "10"))).build();

        httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(res -> {
                    JsonObject base = JsonParser.parseString(res).getAsJsonObject();
                    JsonObject data = base.getAsJsonObject("data");
                    JsonArray children = data.getAsJsonArray("children");
                    JsonObject randomPost = children.get(ThreadLocalRandom.current().nextInt(1, 10)).getAsJsonObject();
                    JsonObject childData = randomPost.getAsJsonObject("data");
                    boolean over18 = childData.getAsJsonPrimitive("over_18").getAsBoolean();
                    TextChannel channel = message.getChannel();
                    if (over18) {
                        Optional<ChannelCategory> categoryOpt = channel.asChannelCategory();
                        if (categoryOpt.isEmpty()) {
                            channel.sendMessage("discord broke");
                            return;
                        }
                        if (!categoryOpt.get().isNsfw()) {
                            channel.sendMessage("run that shit in an nsfw");
                            return;
                        }
                    }
                    message.getChannel().sendMessage(childData.getAsJsonPrimitive("url").getAsString());
                    message.delete();
                });
    }
}
