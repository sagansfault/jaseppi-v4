package sh.sagan.jaseppi.command.lib;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class CommandManager {

    private final Set<Command> commands = new HashSet<>();

    public void register(Command command) {
        this.commands.add(command);
    }

    public Optional<Command> getCommand(String root) {
        for (Command command : this.commands) {
            if (command.getRoot().equalsIgnoreCase(root)) {
                return Optional.of(command);
            }
            for (String alias : command.getAliases()) {
                if (alias.equalsIgnoreCase(root)) {
                    return Optional.of(command);
                }
            }
        }
        return Optional.empty();
    }
}
