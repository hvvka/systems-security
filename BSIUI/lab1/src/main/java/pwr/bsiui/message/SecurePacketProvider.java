package pwr.bsiui.message;

import pwr.bsiui.message.encryption.EncryptionFactory;
import pwr.bsiui.message.model.Packet;

import java.math.BigInteger;

@FunctionalInterface
interface MessageEncoder {
    String getMessage(SecureMessage secureMessage);
}

/**
 * Can either encrypt or decrypt a Packet.
 * Internally uses SecureMessage to convert a message (the only attribute that is being changed by these methods).
 *
 * @author <a href="mailto:226154@student.pwr.edu.pl">Hanna Grodzicka</a>
 * @see pwr.bsiui.message.model.Packet
 * @see pwr.bsiui.message.SecureMessage
 */
class SecurePacketProvider {

    private final EncryptionFactory encryptionFactory;

    SecurePacketProvider() {
        this.encryptionFactory = new EncryptionFactory();
    }

    Packet encryptPacket(Packet packet, BigInteger secretKey) {
        return cryptMessage(packet, secureMessage -> secureMessage.encode(packet.getMessage()), secretKey);
    }

    Packet decryptPacket(Packet packet, BigInteger secretKey) {
        return cryptMessage(packet, secureMessage -> secureMessage.decode(packet.getMessage()), secretKey);
    }

    private Packet cryptMessage(Packet packet, MessageEncoder messageEncoder, BigInteger secretKey) {
        String encryption = packet.getEncryption();
        if ("xor".equals(encryption)) this.encryptionFactory.setSecretKey(secretKey);
        SecureMessage secureMessage = new SecureMessage(encryptionFactory.getEncryption(encryption));
        String decodedMessage = messageEncoder.getMessage(secureMessage);
        return new Packet(packet.getP(), packet.getG(), packet.getId(), decodedMessage, packet.getPublicKey(), encryption);
    }
}
