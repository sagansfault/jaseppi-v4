package sh.sagan.jaseppi.command.impl;

import org.javacord.api.entity.message.Message;
import sh.sagan.jaseppi.Jaseppi;
import sh.sagan.jaseppi.command.lib.Command;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class RollDiceCommand extends Command {

    private final Random random;
    private final Jaseppi jaseppi;

    public RollDiceCommand(Jaseppi jaseppi) {
        super("roll");
        this.jaseppi = jaseppi;
        random = ThreadLocalRandom.current();
    }

    @Override
    public void run(Message message, String[] args) {

        if (message.getAuthor().isBotUser()) {
            return;
        }

        if (args.length < 1) {
            message.reply("I need something like '!roll 4d6'");
            return;
        }

        String arg = args[0];
        String[] parts = arg.split("d");
        if (parts.length != 2) {
            message.reply("I need something like '!roll 4d6'");
            return;
        }
        String countString = parts[0];
        String facesCountString = parts[1];

        int count;
        int faces;
        try {
            count = Integer.parseInt(countString);
            faces = Integer.parseInt(facesCountString);
        } catch (NumberFormatException ignored) {
            message.reply("I need something like '!roll 4d6'");
            return;
        }

        if (count > 50 || count < 1 || faces < 2) {
            message.reply("Number of dice has to be between 1 and 50. Number of faces has to be more than 1");
            return;
        }

        int[] results = new int[count];
        int sum = 0;
        for (int i = 0; i < count; i++) {
            int result = random.nextInt(faces) + 1; // 0 inclusive and faces exclusive so + 1
            results[i] = result;
            sum += result;
        }

        message.reply("You rolled: `" + Arrays.toString(results) + "` Total: `" + sum + "`");
    }
}
