package pwr.bsiui.net.server;

import java.math.BigInteger;

/**
 * Model class for storing client-specific information.
 *
 * @author <a href="mailto:226154@student.pwr.edu.pl">Hanna Grodzicka</a>
 */
class ExchangeDetails {

    private final BigInteger publicKey;

    private final BigInteger secretKey;

    private String encryption;

    ExchangeDetails(BigInteger publicKey, BigInteger secretKey, String encryption) {
        this.publicKey = publicKey;
        this.secretKey = secretKey;
        this.encryption = encryption;
    }

    BigInteger getPublicKey() {
        return publicKey;
    }

    BigInteger getSecretKey() {
        return secretKey;
    }

    String getEncryption() {
        return encryption;
    }

    void setEncryption(String encryption) {
        this.encryption = encryption;
    }
}
