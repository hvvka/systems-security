package pwr.bsiui.net.server;

import java.math.BigInteger;

/**
 * Model class for storing client-specific information.
 *
 * @author <a href="mailto:226154@student.pwr.edu.pl">Hanna Grodzicka</a>
 */
public class ExchangeDetails {

    private final BigInteger publicKey;

    private final BigInteger secretKey;

    private String encryption;

    public ExchangeDetails(BigInteger publicKey, BigInteger secretKey, String encryption) {
        this.publicKey = publicKey;
        this.secretKey = secretKey;
        this.encryption = encryption;
    }

    public BigInteger getPublicKey() {
        return publicKey;
    }

    public BigInteger getSecretKey() {
        return secretKey;
    }

    public String getEncryption() {
        return encryption;
    }

    public void setEncryption(String encryption) {
        this.encryption = encryption;
    }
}
