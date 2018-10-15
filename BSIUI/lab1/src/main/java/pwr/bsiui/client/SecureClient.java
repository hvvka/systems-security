package pwr.bsiui.client;

import com.blogspot.debukkitsblog.net.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:226154@student.pwr.edu.pl">Hanna Grodzicka</a>
 */
public class SecureClient extends Client {

    private static final Logger LOG = LoggerFactory.getLogger(SecureClient.class);

    public SecureClient(String id, int port) {
        super(id, port);

        registerMethod("SOME_MESSAGE", (msg, socket) -> LOG.info("Look! I got a new message from the server: " + msg.get(1)));
        registerMethod("IDENTIFIER", (msg, socket) -> LOG.info("{} on port {}", msg, socket));
        registerMethod("PING", (msg, socket) -> {
            LOG.info("Look! I got a new message from the server: {}", msg.get(1));
                // msg.get(1); should now return "Pong" in our example.
        });

        start(); // Do not forget to start the client!
    }
}
