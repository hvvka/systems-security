package pwr.bsiui.server;

import com.blogspot.debukkitsblog.net.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pwr.bsiui.message.DiffieHellman;
import pwr.bsiui.message.ExchangePacketProvider;
import pwr.bsiui.message.model.Packet;
import pwr.bsiui.message.model.PacketBuilder;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author <a href="mailto:226154@student.pwr.edu.pl">Hanna Grodzicka</a>
 */
public class SecureServer extends Server {

    private static final Logger LOG = LoggerFactory.getLogger(SecureServer.class);

    private static final long PRIVATE_KEY = ThreadLocalRandom.current().nextLong(Long.MAX_VALUE);

    private final DiffieHellman diffieHellman;

    private final ExchangePacketProvider exchangePacketProvider;

    private String encryptionName;

    public SecureServer(int port) {
        super(port);
        this.diffieHellman = new DiffieHellman(PRIVATE_KEY);
        this.exchangePacketProvider = new ExchangePacketProvider();
        this.encryptionName = "none";
    }

    public synchronized void broadcastMessage(Packet packet) {
        // TODO: implement this method and use in 'registerResponseSendMessage'
    }

    @Override
    public void preStart() {
        registerRequestPG();
        registerRequestPublicKey();
        registerResponseSendPublicKey();
        registerResponseSendMessage();
        registerResponseChangeEncryptionMethod();
    }

    // TODO: refactor below methods to functional interface / parametrized method
    private void registerRequestPG() {
        registerMethod("REQUEST_P_G", (msg, socket) -> {
            LOG.info("Received {}", msg.get(0));
            long p = diffieHellman.getP();
            long g = diffieHellman.getG();
            Packet packet = new PacketBuilder(encryptionName)
                    .setP(p)
                    .setG(g)
                    .createExchangePacket();
            String json = exchangePacketProvider.toSecureJson(packet);
            LOG.info("Sending P={}, G={}", p, g);
            sendReply(socket, json);
        });
    }

    private void registerRequestPublicKey() {
        registerMethod("REQUEST_KEY", (msg, socket) -> {
            LOG.info("Received {}", msg.get(0));
            long publicKey = diffieHellman.calculatePublicKey();
            Packet packet = new PacketBuilder(encryptionName)
                    .setPublicKey(publicKey)
                    .createExchangePacket();
            String json = exchangePacketProvider.toSecureJson(packet);
            LOG.info("Sending public key={}", publicKey);
            sendReply(socket, json);
        });
    }

    private void registerResponseSendPublicKey() {
        registerMethod("SEND_PUBLIC_KEY", (msg, socket) -> {
            Packet receivedPacket = exchangePacketProvider.fromSecureJson((String) msg.get(1));
            LOG.info("Received user's public key: {}", receivedPacket.getPublicKey());
            diffieHellman.setOthersPublicKey(receivedPacket.getPublicKey());
            Packet packet = new PacketBuilder(encryptionName)
                    .setMessage("OK, server received your public key")
                    .createExchangePacket();
            String json = exchangePacketProvider.toSecureJson(packet);
            sendReply(socket, json);
        });
    }

    private void registerResponseSendMessage() {
        registerMethod("SEND_MESSAGE", (msg, socket) -> {
            Packet receivedPacket = exchangePacketProvider.fromSecureJson((String) msg.get(1));
            LOG.info("Received message '{}' from user '{}'", receivedPacket.getMessage(), receivedPacket.getId());
            broadcastMessage(msg);  // TODO: change encryption method according to client
            Packet packet = new PacketBuilder(encryptionName)
                    .setMessage(RandomResponseProvider.get())
                    .createExchangePacket();
            String json = exchangePacketProvider.toSecureJson(packet);
            sendReply(socket, json);
        });
    }

    private void registerResponseChangeEncryptionMethod() {
        registerMethod("SEND_CHANGE_ENCRYPTION_METHOD", (msg, socket) -> {
            Packet receivedPacket = exchangePacketProvider.fromSecureJson((String) msg.get(1));
            LOG.info("Received user's request to change encryption method to: {}", receivedPacket.getEncryption());
            this.encryptionName = receivedPacket.getEncryption();
            Packet packet = new PacketBuilder(encryptionName)
                    .setMessage(String.format("OK, I'm changing message encryption method to '%s'", this.encryptionName))
                    .createExchangePacket();
            String json = exchangePacketProvider.toSecureJson(packet);
            sendReply(socket, json);
        });
    }
}
