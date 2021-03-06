package pwr.bsiui.message;

import pwr.bsiui.message.encryption.Encryption;
import pwr.bsiui.message.encryption.NoneEncryption;

import java.util.Base64;

/**
 * Can either encrypt or decrypt a message (string) by specified encryption.
 * Uses base64 encoding.
 *
 * @author <a href="mailto:226154@student.pwr.edu.pl">Hanna Grodzicka</a>
 */
class SecureMessage {

    private Encryption encryption;

    SecureMessage() {
        this.encryption = new NoneEncryption();
    }

    SecureMessage(Encryption encryption) {
        this.encryption = encryption;
    }

    String encode(String message) {
        String encryptedMessage = encryption.encrypt(message);
        return new String(Base64.getEncoder().encode(encryptedMessage.getBytes()));
    }

    String decode(String message) {
        String decryptedMessage = new String(Base64.getDecoder().decode(message.getBytes()));
        return encryption.decrypt(decryptedMessage);
    }
}
