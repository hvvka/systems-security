package pwr.bsiui.server;

import com.blogspot.debukkitsblog.net.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:226154@student.pwr.edu.pl">Hanna Grodzicka</a>
 */
public class SecureServer extends Server {

    private static final Logger LOG = LoggerFactory.getLogger(SecureServer.class);

    public SecureServer(int port) {
        super(port);
    }

    @Override
    public void preStart() {
        registerMethod("SOME_MESSAGE", (msg, socket) -> {
            LOG.info("{}", msg); // do sth with msg
            sendReply(socket, "Hey, thanks for your message. Greetings!");
        });
        registerMethod("IDENTIFIER", (msg, socket) -> {
            LOG.info("{} for port {}", msg, socket); // do sth with msg
            sendReply(socket, "Some Reply");
        });
        registerMethod("PING", (msg, socket) -> sendReply(socket, "Pong"));
        registerMethod("Message", (msg, socket) -> {
            LOG.info("[Message] New chat message arrived, delivering to all the clients...");
            broadcastMessage(msg); //The broadcast to all the receivers
            int reveicerCount = 10;
            sendReply(socket, String.valueOf(reveicerCount)); //The reply (NECESSARY! unless you want the client to block while waiting for this package)
        });
    }
}
