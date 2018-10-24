package pwr.bsiui.message;

import pwr.bsiui.message.model.Packet;

/**
 * @author <a href="mailto:226154@student.pwr.edu.pl">Hanna Grodzicka</a>
 */
public class ExchangePacketProvider {

    private final PacketJsonSerializer packetJsonSerializer;

    private final SecurePacketProvider securePacketProvider;

    public ExchangePacketProvider() {
        this.packetJsonSerializer = new PacketJsonSerializer();
        this.securePacketProvider = new SecurePacketProvider();
    }

    public String toSecureJson(Packet packet) {
        Packet encodedPacket = securePacketProvider.encodePacket(packet);
        return packetJsonSerializer.toJson(encodedPacket);
    }

    public Packet fromSecureJson(String json) {
        Packet encodedPacket = packetJsonSerializer.fromJson(json);
        return securePacketProvider.decodePacket(encodedPacket);
    }
}
