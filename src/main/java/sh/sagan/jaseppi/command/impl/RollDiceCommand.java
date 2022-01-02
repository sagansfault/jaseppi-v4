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
            message.reply("I need something like '!roll 4d6' or '!roll 6d2+3'");
            return;
        }

        String arg = args[0];
        String[] parts = arg.split("d|\\+");
        if (parts.length < 2) {
            message.reply("I need something like '!roll 4d6' or '!roll 6d2+3'");
            return;
        }
        String countString = parts[0];
        String facesCountString = parts[1];
        String addString = parts.length >= 3 ? parts[2] : "0";

        int count;
        int faces;
        int add;
        try {
            count = Integer.parseInt(countString);
            faces = Integer.parseInt(facesCountString);
            add = Integer.parseInt(addString);
        } catch (NumberFormatException ignored) {
            message.reply("I need something like '!roll 4d6' or '!roll 6d2+3'");
            return;
        }

        if (count > 50 || count < 1 || faces < 2 || faces > 1000 || Math.abs(add) > 1000) {
            message.reply("Number of dice has to be between 1 and 50. Number of faces has to be between 1 and 1000. Add cannot be more than 1000");
            return;
        }

        int[] results = new int[count];
        int sum = 0;
        for (int i = 0; i < count; i++) {
            int result = random.nextInt(faces) + 1; // 0 inclusive and faces exclusive so + 1
            results[i] = result;
            sum += result;
        }
        sum += add;

        if (add == 0) {
            message.reply("You rolled: `" + Arrays.toString(results) + "` Total: `" + sum + "`");
        } else {
            message.reply("You rolled: `" + Arrays.toString(results) + " + " + add + "` Total: `" + sum + "`");
        }
    }
}
