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
        registerSendPublicKey();
        start();
    }

    private void registerSendPublicKey() {
        registerMethod("SEND_PUBLIC_KEY", (msg, socket) -> {
            LOG.info("Look! I got a new message from the server: " + msg.get(1));
            throw new IllegalStateException("Not yet implemented");
        });
    }
}
