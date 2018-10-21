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

    public SecureServer(int port) {
        super(port);
        this.diffieHellman = new DiffieHellman(privateKey);
        this.packetJsonSerializer = new PacketJsonSerializer();
    }

    @Override
    public void preStart() {
        registerRequestPGMethod();
        registerRequestPublicKeyMethod();
    }

    // TODO: refactor below methods to functional interface
    private void registerRequestPGMethod() {
        registerMethod("REQUEST_P_G", (msg, socket) -> {
            LOG.info("Received REQUEST_P_G: {}", msg);
            ExchangePacket exchangePacket = new ExchangePacketBuilder()
                    .setP(diffieHellman.getP())
                    .setG(diffieHellman.getG())
                    .createExchangePacket();
            String json = packetJsonSerializer.toJson(exchangePacket);
            sendReply(socket, json);
        });
    }

    private void registerRequestPublicKeyMethod() {
        registerMethod("REQUEST_KEY", (msg, socket) -> {
            LOG.info("Received REQUEST_KEY: {}", msg);
            ExchangePacket exchangePacket = new ExchangePacketBuilder()
                    .setPublicKey(diffieHellman.calculatePublicKey())
                    .createExchangePacket();
            String json = packetJsonSerializer.toJson(exchangePacket);
            sendReply(socket, json);
        });
    }
}
