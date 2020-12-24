package sh.sagan.jaseppi.chat;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;
import sh.sagan.jaseppi.util.Const;

import java.util.concurrent.ThreadLocalRandom;

public class TagJaseppiListener implements MessageCreateListener {

    public final DiscordApi api;

    public TagJaseppiListener(DiscordApi api) {
        this.api = api;
    }

    @Override
    public void onMessageCreate(MessageCreateEvent event) {
        // dont reply to bot messages
        if (event.getMessage().getAuthor().isBotUser()) {
            return;
        }

        // dont care if they dont tag jaseppi
        if (event.getMessage().getMentionedUsers().stream().noneMatch(User::isYourself)) {
            return;
        }

        if (event.getMessageContent().endsWith("?")) {
            String firstWord = event.getMessageContent().split(" ")[1];
            if (firstWord.equalsIgnoreCase("what") || firstWord.equalsIgnoreCase("where") ||
                    firstWord.equalsIgnoreCase("when") || firstWord.equalsIgnoreCase("why")) {
                event.getChannel().sendMessage(Const.TAG_JASEPPI_QUESTION_WHAT_WHY_WHEN_WHERE.random());
            } else {
                event.getChannel().sendMessage(Const.TAG_JASEPPI_QUESTION_OTHER.random());
            }
        } else {
            if (ThreadLocalRandom.current().nextInt(5) == 1) {
                event.getChannel().sendMessage(Const.TAG_JASEPPI.random());
            }
        }
    }
}
