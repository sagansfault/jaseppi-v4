package sh.sagan.jaseppi.command.lib;

import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;
import sh.sagan.jaseppi.util.Const;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandManager {

    private static CommandManager instance = null;

    private List<Command> commands = new ArrayList<>();

    private CommandManager() {}

    public static CommandManager getInstance() {
        if (instance == null) {
            instance = new CommandManager();
        }

        return instance;
    }

    public void register(Command command) {
        if (!commands.contains(command)) {
            commands.add(command);
        }
    }

    public List<Command> getCommands() {
        return commands;
    }

    public static class Delegator implements MessageCreateListener {

        @Override
        public void onMessageCreate(MessageCreateEvent event) {
            if (!event.getMessageContent().startsWith(Const.PREFIX)) {
                return;
            }

            String[] rawContent = event.getMessageContent().substring(1).split(" ");
            String root = rawContent[0];
            if (root.isEmpty() || root.isBlank()) {
                return;
            }

            Command found = null;

            // search first for matches against root
            for (Command command : CommandManager.getInstance().getCommands()) {
                if (command.getRoot().equalsIgnoreCase(root)) {
                    found = command;
                }
            }

            // if no matches found for root, then look for aliases. Allows one command whose root is another's alias to be called first
            if (found == null) {
                for (Command command : CommandManager.getInstance().getCommands()) {
                    if (Arrays.stream(command.getAliases()).anyMatch(s -> s.equalsIgnoreCase(root))) {
                        found = command;
                    }
                }
            }

            if (found == null) {
                event.getChannel().sendMessage(Const.INVALID_COMMAND.random());
                return;
            }

            if (rawContent.length <= 1) {
                found.baseFunction(event.getMessage(), new String[]{});
                return;
            }

            String sub = rawContent[1];
            String[] args = Arrays.copyOfRange(rawContent, 2, rawContent.length);

            if (found.getSubCommands().containsKey(sub)) {
                try {
                    // cast at the end to Object to ensure passing as full array and not var args strings
                    found.getSubCommands().get(sub).invoke(found, event.getMessage(), args);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    System.out.println("Could not invoke command method");
                }
            } else {
                // no sub command so split from 1 and not 2
                found.baseFunction(event.getMessage(), Arrays.copyOfRange(rawContent, 1, rawContent.length));
            }
        }
    }
}
