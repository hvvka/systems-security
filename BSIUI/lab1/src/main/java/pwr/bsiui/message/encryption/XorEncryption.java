package pwr.bsiui.message.encryption;

/**
 * @author <a href="mailto:226154@student.pwr.edu.pl">Hanna Grodzicka</a>
 */
public class XorEncryption implements Encryption {

    private final char secretKey;

    public XorEncryption(String secretKey) {
        this.secretKey = secretKey.charAt(secretKey.length() - 1);
    }

    @Override
    public String encrypt(String message) {
        return encode(message);
    }

    @Override
    public String decrypt(String message) {
        return encode(message);
    }

    @Override
    public String getName() {
        return "xor";
    }

    private String encode(String message) {
        StringBuilder encodedMessage = new StringBuilder();
        for (byte b : message.getBytes()) {
            encodedMessage.append((char) (b ^ secretKey));
        }
        return encodedMessage.toString();
    }
}
