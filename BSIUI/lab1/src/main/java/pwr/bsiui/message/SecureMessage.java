package pwr.bsiui.message;

import pwr.bsiui.message.encryption.Encryption;
import pwr.bsiui.message.encryption.NoneEncryption;

import java.util.Base64;

/**
 * @author <a href="mailto:226154@student.pwr.edu.pl">Hanna Grodzicka</a>
 */
public class SecureMessage {

    private Encryption encryption;

    public SecureMessage() {
        this.encryption = new NoneEncryption();
    }

    public SecureMessage(Encryption encryption) {
        this.encryption = encryption;
    }

    public String encode(String message) {
        String encryptedMessage = encryption.encrypt(message);
        return new String(Base64.getEncoder().encode(encryptedMessage.getBytes()));
    }

    public String decode(String message) {
        String decryptedMessage = new String(Base64.getDecoder().decode(message.getBytes()));
        return encryption.decrypt(decryptedMessage);
    }
}
