package pwr.bsiui.net.server;

import com.blogspot.debukkitsblog.net.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pwr.bsiui.message.DiffieHellman;
import pwr.bsiui.message.ExchangePacketProvider;
import pwr.bsiui.message.model.Packet;
import pwr.bsiui.message.model.PacketBuilder;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Server that uses Diffie–Hellman protocol with JSON message exchange and interacts with new clients.
 *
 * @author <a href="mailto:226154@student.pwr.edu.pl">Hanna Grodzicka</a>
 * @see <a href="https://en.wikipedia.org/wiki/Diffie–Hellman_key_exchange">Diffie–Hellman key exchange</a>
 */
public class SecureServer extends Server {

    private static final Logger LOG = LoggerFactory.getLogger(SecureServer.class);

    private static final BigInteger PRIVATE_KEY = BigInteger.valueOf(ThreadLocalRandom.current().nextInt(500));

    private final DiffieHellman diffieHellman;

    private final ExchangePacketProvider exchangePacketProvider;

    private Map<String, ExchangeDetails> clientEncryption;

    public SecureServer(int port) {
        super(port);
        LOG.info("Server's private key: {}", PRIVATE_KEY);
        this.diffieHellman = new DiffieHellman(PRIVATE_KEY);
        this.exchangePacketProvider = new ExchangePacketProvider();
        this.clientEncryption = new HashMap<>();
    }

    public synchronized void broadcastMessage(Packet packet) {
        this.clients.stream()
                .filter(c -> this.clientEncryption.containsKey(c.getId()))
                .forEach(c -> {
                    String encryption = this.clientEncryption.get(c.getId()).getEncryption();
                    packet.setEncryption(encryption);
                    exchangePacketProvider.setSecretKey(clientEncryption.get(c.getId()).getSecretKey());
                    sendMessage(c.getId(), "BROADCAST", this.exchangePacketProvider.toSecureJson(packet));
                });
    }

    @Override
    public void preStart() {
        registerRequest("REQUEST_P_G", this::sendPG);
        registerRequest("REQUEST_KEY", this::sendPublicKey);
        registerResponse("SEND_PUBLIC_KEY", this::respondClientPublicKey);
        registerResponse("SEND_MESSAGE", this::respondClientMessage);
        registerResponse("SEND_CHANGE_ENCRYPTION_METHOD", this::respondClientChangeEncryption);
    }

    private void registerRequest(String methodName, Supplier<Packet> packetSupplier) {
        registerMethod(methodName, (msg, socket) -> {
            LOG.info("Received {}", msg.get(0));
            Packet packet = packetSupplier.get();
            String json = exchangePacketProvider.toSecureJson(packet);
            sendReply(socket, json);
        });
    }

    private Packet sendPG() {
        BigInteger p = diffieHellman.getP();
        BigInteger g = diffieHellman.getG();
        LOG.info("Sending P={}, G={}", p, g);
        return new PacketBuilder()
                .setP(p)
                .setG(g)
                .createExchangePacket();
    }

    private Packet sendPublicKey() {
        BigInteger publicKey = diffieHellman.calculatePublicKey();
        LOG.info("Sending public key={}", publicKey);
        return new PacketBuilder()
                .setPublicKey(publicKey)
                .createExchangePacket();
    }

    private void registerResponse(String methodName, Function<Packet, Packet> packetPacketFunction) {
        registerMethod(methodName, (msg, socket) -> {
//            Optional<String> clientId = clientEncryption.keySet()
//                    .stream()
//                    .filter(((String) msg.get(1))::contains)
//                    .findFirst();
            Packet receivedPacket = exchangePacketProvider.fromSecureJson((String) msg.get(1));
            Packet packet = packetPacketFunction.apply(receivedPacket);
            String json = exchangePacketProvider.toSecureJson(packet);
            sendReply(socket, json);
        });
    }

    private Packet respondClientPublicKey(Packet receivedPacket) {
        LOG.info("Received user's public key: {}", receivedPacket.getPublicKey());
        diffieHellman.setOthersPublicKey(receivedPacket.getPublicKey());
        clientEncryption.put(receivedPacket.getId(), new ExchangeDetails(receivedPacket.getPublicKey(),
                diffieHellman.calculateSharedSecretKey(), receivedPacket.getEncryption()));
        return new PacketBuilder(receivedPacket.getEncryption())
                .setMessage("OK, server received your public key")
                .createExchangePacket();
    }

    private Packet respondClientMessage(Packet receivedPacket) {
        LOG.info("Received message '{}' from user '{}'", receivedPacket.getMessage(), receivedPacket.getId());
        String encryption = this.clientEncryption.get(receivedPacket.getId()).getEncryption();
        broadcastMessage(receivedPacket);
        return new PacketBuilder(encryption)
                .setMessage(RandomResponseProvider.get())
                .createExchangePacket();
    }

    private Packet respondClientChangeEncryption(Packet receivedPacket) {
        String newEncryption = receivedPacket.getEncryption();
        LOG.info("Received user's request to change encryption method to: {}", newEncryption);
        this.clientEncryption.get(receivedPacket.getId()).setEncryption(newEncryption);
        return new PacketBuilder(newEncryption)
                .setMessage(String.format("OK, I'm changing message encryption method to '%s'", newEncryption))
                .createExchangePacket();
    }
}
