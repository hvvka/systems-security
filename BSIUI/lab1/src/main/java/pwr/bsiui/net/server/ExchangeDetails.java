package pwr.bsiui.net.server;

/**
 * Model class for storing client-specific information.
 *
 * @author <a href="mailto:226154@student.pwr.edu.pl">Hanna Grodzicka</a>
 */
public class ExchangeDetails {

    private final long publicKey;

    private String encryption;

    public ExchangeDetails(long publicKey, String encryption) {
        this.publicKey = publicKey;
        this.encryption = encryption;
    }

    public long getPublicKey() {
        return publicKey;
    }

    public String getEncryption() {
        return encryption;
    }

    public void setEncryption(String encryption) {
        this.encryption = encryption;
    }
}
