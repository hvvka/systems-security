package pwr.bsiui.message.encryption;

import java.math.BigInteger;

/**
 * @author <a href="mailto:226154@student.pwr.edu.pl">Hanna Grodzicka</a>
 */
public class EncryptionFactory {

    private BigInteger secretKey;

    public Encryption getEncryption(String name) {
        switch (name) {
            case "xor":
                return new XorEncryption(String.valueOf(secretKey));
            case "caesar": return new CaesarEncryption();
            case "none":
            default:
                return new NoneEncryption();
        }
    }

    public void setSecretKey(BigInteger secretKey) {
        this.secretKey = secretKey;
    }
}
