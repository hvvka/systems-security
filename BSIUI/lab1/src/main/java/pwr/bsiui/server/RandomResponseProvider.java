package pwr.bsiui.server;

import java.util.Random;

/**
 * @author <a href="mailto:226154@student.pwr.edu.pl">Hanna Grodzicka</a>
 */
public class RandomResponseProvider {

    private static final String[] RESPONSES = {"Hi", "I'm server", "Dadada", "Wrrr", "I have limited response list"};

    public static String get() {
        Random random = new Random();
        return RESPONSES[random.nextInt() % RESPONSES.length];
    }
}
