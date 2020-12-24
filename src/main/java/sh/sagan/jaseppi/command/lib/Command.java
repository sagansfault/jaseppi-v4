package sh.sagan.jaseppi.command.lib;

import org.javacord.api.entity.message.Message;
import sh.sagan.jaseppi.command.lib.annotations.Sub;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public abstract class Command {

    private final String root;
    private final String[] aliases;
    private Map<String, Method> subCommands = new HashMap<>();

    protected Command(String root, String... aliases) {
        this.root = root;
        this.aliases = aliases;

        for (Method method : this.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(Sub.class)) {
                Sub sub = method.getAnnotation(Sub.class);
                if (method.getParameterTypes().length >= 2 &&
                        method.getParameterTypes()[0].equals(Message.class) &&
                        method.getParameterTypes()[1].equals(String[].class)) {
                    subCommands.put(sub.sub(), method);
                }
            }
        }
    }

    public String getRoot() {
        return root;
    }

    public String[] getAliases() {
        return aliases;
    }

    public final Map<String, Method> getSubCommands() {
        return subCommands;
    }

    public abstract void baseFunction(Message message, String[] args);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Command command = (Command) o;
        return root.equalsIgnoreCase(command.root);
    }
}
