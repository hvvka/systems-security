package pwr.bsiui.net.server;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Random responses that server replies with every time client sends a message.
 *
 * @author <a href="mailto:226154@student.pwr.edu.pl">Hanna Grodzicka</a>
 */
public class RandomResponseProvider {

    private static final String[] RESPONSES = {
            "Hi", "I'm server", "Dadada", "Morning, sir", "Morning, madam", "Guten tag", "Hola, mi amigo",
            "Guten morgen", "Ahoj", "Ola!", "Ciao", "Buenos dias", "Buenos dias, senor", "Willkommen"
    };

    private RandomResponseProvider() {
        throw new IllegalStateException("Utility class!");
    }

    public static String get() {
        return RESPONSES[ThreadLocalRandom.current().nextInt(RESPONSES.length)];
    }
}
