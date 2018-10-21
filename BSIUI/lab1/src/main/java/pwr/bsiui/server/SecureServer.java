package pwr.bsiui.server;

import com.blogspot.debukkitsblog.net.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pwr.bsiui.message.DiffieHellman;
import pwr.bsiui.message.model.ExchangePacket;
import pwr.bsiui.message.model.ExchangePacketBuilder;

/**
 * @author <a href="mailto:226154@student.pwr.edu.pl">Hanna Grodzicka</a>
 */
public class SecureServer extends Server {

    private static final Logger LOG = LoggerFactory.getLogger(SecureServer.class);

    private final DiffieHellman diffieHellman;

    public SecureServer(int port) {
        super(port);
        this.diffieHellman = new DiffieHellman();
    }

    @Override
    public void preStart() {
        registerRequestMethod();

        // TODO: delete
        exampleRegistration();
    }

    private void exampleRegistration() {
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
            int receiverCount = 10;
            sendReply(socket, String.valueOf(receiverCount)); //The reply (NECESSARY! unless you want the client to block while waiting for this package)
        });
    }

    private void registerRequestMethod() {
        registerMethod("REQUEST", (msg, socket) -> {
            LOG.info("Received REQUEST: {}", msg);
            ExchangePacket exchangePacket = new ExchangePacketBuilder()
                    .setP(diffieHellman.getP())
                    .setG(diffieHellman.getG())
                    .createExchangePacket();
            sendReply(socket, exchangePacket);
        });
    }
}
