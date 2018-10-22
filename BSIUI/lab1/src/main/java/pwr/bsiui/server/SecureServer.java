package pwr.bsiui.server;

import com.blogspot.debukkitsblog.net.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pwr.bsiui.message.DiffieHellman;
import pwr.bsiui.message.PacketJsonSerializer;
import pwr.bsiui.message.model.ExchangePacket;
import pwr.bsiui.message.model.ExchangePacketBuilder;

/**
 * @author <a href="mailto:226154@student.pwr.edu.pl">Hanna Grodzicka</a>
 */
public class SecureServer extends Server {

    private static final Logger LOG = LoggerFactory.getLogger(SecureServer.class);

    private static final long privateKey = 5;  // TODO: generate random value

    private final DiffieHellman diffieHellman;

    private final PacketJsonSerializer packetJsonSerializer;

    private String encryption;

    public SecureServer(int port) {
        super(port);
        this.diffieHellman = new DiffieHellman(privateKey);
        this.packetJsonSerializer = new PacketJsonSerializer();
        this.encryption = "none";
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
            ExchangePacket exchangePacket = new ExchangePacketBuilder(encryption)
                    .setP(p)
                    .setG(g)
                    .createExchangePacket();
            String json = packetJsonSerializer.toJson(exchangePacket);
            LOG.info("Sending P={}, G={}", p, g);
            sendReply(socket, json);
        });
    }

    private void registerRequestPublicKey() {
        registerMethod("REQUEST_KEY", (msg, socket) -> {
            LOG.info("Received {}", msg.get(0));
            long publicKey = diffieHellman.calculatePublicKey();
            ExchangePacket exchangePacket = new ExchangePacketBuilder(encryption)
                    .setPublicKey(publicKey)
                    .createExchangePacket();
            String json = packetJsonSerializer.toJson(exchangePacket);
            LOG.info("Sending public key={}", publicKey);
            sendReply(socket, json);
        });
    }

    private void registerResponseSendPublicKey() {
        registerMethod("SEND_PUBLIC_KEY", (msg, socket) -> {
            LOG.info("Received user's public key: {}", msg.get(1));
            ExchangePacket exchangePacket = new ExchangePacketBuilder(encryption)
                    .setMessage("OK, server received your public key")
                    .createExchangePacket();
            String json = packetJsonSerializer.toJson(exchangePacket);
            sendReply(socket, json);
        });
    }

    private void registerResponseSendMessage() {
        registerMethod("SEND_MESSAGE", (msg, socket) -> {
            LOG.info("Received message from user: {}", msg.get(1));
            ExchangePacket exchangePacket = new ExchangePacketBuilder(encryption)
                    .setMessage(RandomResponseProvider.get())
                    .createExchangePacket();
            String json = packetJsonSerializer.toJson(exchangePacket);
            sendReply(socket, json);
        });
    }

    private void registerResponseChangeEncryptionMethod() {
        registerMethod("SEND_CHANGE_ENCRYPTION_METHOD", (msg, socket) -> {
            LOG.info("Received user's request to change encryption method to: {}", msg.get(1));
            this.encryption = (String) msg.get(1);
            ExchangePacket exchangePacket = new ExchangePacketBuilder(encryption)
                    .setMessage("OK, I'm changing encryption method")
                    .createExchangePacket();
            String json = packetJsonSerializer.toJson(exchangePacket);
            sendReply(socket, json);
        });
    }
}
