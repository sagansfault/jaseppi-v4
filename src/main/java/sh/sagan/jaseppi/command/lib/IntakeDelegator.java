package sh.sagan.jaseppi.command.lib;

import org.javacord.api.entity.message.Message;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;
import sh.sagan.jaseppi.Jaseppi;

import java.util.Arrays;
import java.util.Optional;

public class IntakeDelegator implements MessageCreateListener {

    private final CommandManager manager;

    public IntakeDelegator(CommandManager manager) {
        this.manager = manager;
    }

    @Override
    public void onMessageCreate(MessageCreateEvent event) {

        Message message = event.getMessage();
        if (message.getAuthor().isWebhook() || message.getAuthor().isBotUser() || !message.getAuthor().isRegularUser()) {
            return;
        }

        String content = message.getContent();
        if (!content.startsWith(Jaseppi.PREFIX)) {
            return;
        }

        String[] parts = content.substring(1).split(" ");
        String root = parts[0];
        String[] args = Arrays.copyOfRange(parts, 1, parts.length);

        Optional<Command> commandOptional = manager.getCommand(root);
        if (commandOptional.isPresent()) {
            Command command = commandOptional.get();
            command.run(message, args);
        } else {
            message.reply("????");
            return;
        }
    }
}
