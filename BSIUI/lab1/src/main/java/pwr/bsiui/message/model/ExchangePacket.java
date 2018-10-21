package pwr.bsiui.message.model;

/**
 * @author <a href="mailto:226154@student.pwr.edu.pl">Hanna Grodzicka</a>
 */
public class ExchangePacket {

    private long p;

    private long g;

    private String message;

    private long publicKey;

    private String encryption;

    public ExchangePacket(long p, long g, String message, long publicKey, String encryption) {
        this.p = p;
        this.g = g;
        this.message = message;
        this.publicKey = publicKey;
        this.encryption = encryption;
    }

    public long getP() {
        return p;
    }

    public long getG() {
        return g;
    }

    public String getMessage() {
        return message;
    }

    public long getPublicKey() {
        return publicKey;
    }

    public String getEncryption() {
        return encryption;
    }
}
