package pwr.bsiui.net.client;

import com.blogspot.debukkitsblog.net.Client;

/**
 * @author <a href="mailto:226154@student.pwr.edu.pl">Hanna Grodzicka</a>
 */
public class SecureClient extends Client {

    public SecureClient(String id, int port) {
        super(id, port);
        start();
    }
}
