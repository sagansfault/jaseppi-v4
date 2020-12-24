package sh.sagan.jaseppi.util;

import java.util.concurrent.ThreadLocalRandom;

public enum Const {
    INVALID_COMMAND("that aint even a command", "nah", "dumbass", "not a command", "do it again",
            "you real smart hey?", ":|", "good one", "tard", ":(", "try another", "aint too bright ey?"),
    INVALID_USER_SAY_REQUEST("think you're funny?", "dumbass", "nice1", ":|"),
    TAG_JASEPPI("man just leave me alone", "please stop :(", "jaseppi just wanna be loved", "bruh i'm just chillin",
            "???", "why u taggin me", ":)", "yea?", "bruh", "okay"),
    TAG_JASEPPI_QUESTION_WHAT_WHY_WHEN_WHERE("why should I know", "idk", "huh?", "???", "shit question", "¯\\_(ツ)_/¯"),
    TAG_JASEPPI_QUESTION_OTHER("why should I know", "idk", "huh?", "???", "shit question", "¯\\_(ツ)_/¯",
            "mhmm", "yea", "no", "yes", "nah", "maybe", "perhaps", "i suppose", "for sure")
    ;

    public static final String PREFIX = ".";
    public static final String SAGAN_ID = "203347457944322048";

    public String[] messages;
    Const(String... messages) {
        this.messages = messages;
    }

    public String random() {
        if (this.messages.length == 0) {
            return "shit broken";
        }

        return this.messages[ThreadLocalRandom.current().nextInt(messages.length)];
    }
}
