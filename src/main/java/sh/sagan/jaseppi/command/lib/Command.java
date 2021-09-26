package sh.sagan.jaseppi.command.lib;

import org.javacord.api.entity.message.Message;

import java.util.Objects;

public abstract class Command {

    private final String root;
    private final String[] aliases;

    public Command(String root, String... aliases) {
        this.root = root;
        this.aliases = aliases;
    }

    public abstract void run(Message message, String[] args);

    public String getRoot() {
        return root;
    }

    public String[] getAliases() {
        return aliases;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Command command = (Command) o;
        return root.equalsIgnoreCase(command.root);
    }

    @Override
    public int hashCode() {
        return Objects.hash(root);
    }
}
