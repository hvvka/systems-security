package pwr.bsiui.server;

import java.util.Random;

/**
 * @author <a href="mailto:226154@student.pwr.edu.pl">Hanna Grodzicka</a>
 */
class RandomResponseProvider {

    private static final String[] RESPONSES = {
            "Hi", "I'm server", "Dadada", "Morning, sir", "Morning, madam", "Guten tag", "Hola, mi amigo",
            "Guten morgen", "Ahoj", "Olá!", "Ciao", "Buenos días", "Buenos días, señor", "Willkommen"
    };

    private RandomResponseProvider() {
        throw new IllegalStateException("Utility class!");
    }

    static String get() {
        Random random = new Random();
        return RESPONSES[random.nextInt() % RESPONSES.length];
    }
}
