package pwr.bsiui;

import pwr.bsiui.net.client.ClientLoop;

/**
 * Runs client instance on port 4001.
 *
 * @author <a href="mailto:226154@student.pwr.edu.pl">Hanna Grodzicka</a>
 */
public final class ClientRunner {

    public static void main(String[] args) {
        new ClientLoop(4001).start();
    }
}
