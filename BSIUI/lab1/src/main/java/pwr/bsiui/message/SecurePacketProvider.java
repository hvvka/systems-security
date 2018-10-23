package pwr.bsiui.message;

import pwr.bsiui.message.encryption.EncryptionFactory;
import pwr.bsiui.message.model.Packet;

// TODO: use consumer maybe
@FunctionalInterface
interface MessageEncoder {
    String getMessage();
}

/**
 * @author <a href="mailto:226154@student.pwr.edu.pl">Hanna Grodzicka</a>
 */
class SecurePacketProvider {

    private final EncryptionFactory encryptionFactory;

    SecurePacketProvider() {
        this.encryptionFactory = new EncryptionFactory();
    }

    Packet encodePacket(Packet packet) {
        SecureMessage secureMessage = new SecureMessage(encryptionFactory.getEncryption(packet.getEncryption()));
        return cryptMessage(packet, () -> secureMessage.encode(packet.getMessage()));
    }

    Packet decodePacket(Packet packet) {
        SecureMessage secureMessage = new SecureMessage(encryptionFactory.getEncryption(packet.getEncryption()));
        return cryptMessage(packet, () -> secureMessage.decode(packet.getMessage()));
    }

    private Packet cryptMessage(Packet packet, MessageEncoder messageEncoder) {
        String decodedMessage = messageEncoder.getMessage();
        return new Packet(packet.getP(), packet.getG(), decodedMessage, packet.getPublicKey(), packet.getEncryption());
    }
}
