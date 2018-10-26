package pwr.bsiui.server;

import com.blogspot.debukkitsblog.net.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pwr.bsiui.message.DiffieHellman;
import pwr.bsiui.message.ExchangePacketProvider;
import pwr.bsiui.message.model.Packet;
import pwr.bsiui.message.model.PacketBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author <a href="mailto:226154@student.pwr.edu.pl">Hanna Grodzicka</a>
 */
public class SecureServer extends Server {

    private static final Logger LOG = LoggerFactory.getLogger(SecureServer.class);

    private static final long PRIVATE_KEY = ThreadLocalRandom.current().nextLong(Long.MAX_VALUE);

    private final DiffieHellman diffieHellman;

    private final ExchangePacketProvider exchangePacketProvider;

    private Map<String, ExchangeDetails> clientEncryption;


    public SecureServer(int port) {
        super(port);
        this.diffieHellman = new DiffieHellman(PRIVATE_KEY);
        this.exchangePacketProvider = new ExchangePacketProvider();
        this.clientEncryption = new HashMap<>();
    }

    public synchronized void broadcastMessage(Packet packet) {
        for (RemoteClient current : this.clients) {
            LOG.info(current.getId());  // TODO: delete
            if (this.clientEncryption.containsKey(current.getId())) {
                String encryption = this.clientEncryption.get(current.getId()).getEncryption();
                packet.setEncryption(encryption);
                sendMessage(current.getId(), "BROADCAST", this.exchangePacketProvider.toSecureJson(packet));
            }
        }
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
            Packet packet = new PacketBuilder()
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
            Packet packet = new PacketBuilder()
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
            clientEncryption.put(receivedPacket.getId(), new ExchangeDetails(receivedPacket.getPublicKey(), receivedPacket.getEncryption()));
            diffieHellman.setOthersPublicKey(receivedPacket.getPublicKey());
            Packet packet = new PacketBuilder(receivedPacket.getEncryption())
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
            String encryption = this.clientEncryption.get(receivedPacket.getId()).getEncryption();
            broadcastMessage(receivedPacket);
            Packet packet = new PacketBuilder(encryption)
                    .setMessage(RandomResponseProvider.get())
                    .createExchangePacket();
            String json = exchangePacketProvider.toSecureJson(packet);
            sendReply(socket, json);
        });
    }

    private void registerResponseChangeEncryptionMethod() {
        registerMethod("SEND_CHANGE_ENCRYPTION_METHOD", (msg, socket) -> {
            Packet receivedPacket = exchangePacketProvider.fromSecureJson((String) msg.get(1));
            String newEncryption = receivedPacket.getEncryption();
            LOG.info("Received user's request to change encryption method to: {}", newEncryption);
            this.clientEncryption.get(receivedPacket.getId()).setEncryption(newEncryption);
            Packet packet = new PacketBuilder(newEncryption)
                    .setMessage(String.format("OK, I'm changing message encryption method to '%s'", newEncryption))
                    .createExchangePacket();
            String json = exchangePacketProvider.toSecureJson(packet);
            sendReply(socket, json);
        });
    }
}
