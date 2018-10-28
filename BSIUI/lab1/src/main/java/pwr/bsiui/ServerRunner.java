package pwr.bsiui;

import pwr.bsiui.net.server.SecureServer;

/**
 * @author <a href="mailto:226154@student.pwr.edu.pl">Hanna Grodzicka</a>
 */
public final class ServerRunner {

    public static void main(String[] args) {
        new SecureServer(4001);
    }
}
