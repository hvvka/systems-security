package pwr.bsiui.message;

import pwr.bsiui.message.model.Packet;

/**
 * Top-level class that can be used for client-server message exchange.
 * Can convert JSON to Packet and Packet to JSON.
 * Internally does required encryption or decryption
 * by combining actions of PacketJsonSerializer and SecurePacketProvider.
 *
 * @author <a href="mailto:226154@student.pwr.edu.pl">Hanna Grodzicka</a>
 * @see pwr.bsiui.message.model.Packet
 * @see pwr.bsiui.message.PacketJsonSerializer
 * @see pwr.bsiui.message.SecurePacketProvider
 */
public class ExchangePacketProvider {

    private final PacketJsonSerializer packetJsonSerializer;

    private final SecurePacketProvider securePacketProvider;

    private long secretKey;

    public ExchangePacketProvider() {
        this.packetJsonSerializer = new PacketJsonSerializer();
        this.securePacketProvider = new SecurePacketProvider();
    }

    public String toSecureJson(Packet packet) {
        Packet encodedPacket = securePacketProvider.encryptPacket(packet, secretKey);
        return packetJsonSerializer.toJson(encodedPacket);
    }

    public Packet fromSecureJson(String json) {
        Packet encodedPacket = packetJsonSerializer.fromJson(json);
        return securePacketProvider.decryptPacket(encodedPacket, secretKey);
    }

    public void setSecretKey(long secretKey) {
        this.secretKey = secretKey;
    }
}
