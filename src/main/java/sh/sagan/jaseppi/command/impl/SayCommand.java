package sh.sagan.jaseppi.command.impl;

import org.javacord.api.entity.message.Message;
import sh.sagan.jaseppi.util.Const;
import sh.sagan.jaseppi.command.lib.Command;

public class SayCommand extends Command {

    public SayCommand() {
        super("say");
    }

    @Override
    public void baseFunction(Message message, String[] args) {
        message.delete();
        if (message.getAuthor().getIdAsString().equalsIgnoreCase(Const.SAGAN_ID)) {
            message.getChannel().sendMessage(String.join(" ", args));
        } else {
            message.getChannel().sendMessage(Const.INVALID_USER_SAY_REQUEST.random());
        }
    }
}
