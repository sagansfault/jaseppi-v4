package sh.sagan.jaseppi.command.impl;

import org.javacord.api.entity.message.Message;
import sh.sagan.jaseppi.command.lib.Command;

public class SayCommand extends Command {

    public SayCommand() {
        super("say");
    }

    @Override
    public void run(Message message, String[] args) {
        message.delete();
        message.getChannel().sendMessage(String.join(" ", args));
    }
}
