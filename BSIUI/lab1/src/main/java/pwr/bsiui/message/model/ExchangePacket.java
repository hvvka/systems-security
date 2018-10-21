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

    private ExchangePacket() {
        // used by Jackson 2.x
    }

    ExchangePacket(long p, long g, String message, long publicKey, String encryption) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExchangePacket that = (ExchangePacket) o;

        if (p != that.p) return false;
        if (g != that.g) return false;
        if (publicKey != that.publicKey) return false;
        if (message != null ? !message.equals(that.message) : that.message != null) return false;
        return encryption != null ? encryption.equals(that.encryption) : that.encryption == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (p ^ (p >>> 32));
        result = 31 * result + (int) (g ^ (g >>> 32));
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (int) (publicKey ^ (publicKey >>> 32));
        result = 31 * result + (encryption != null ? encryption.hashCode() : 0);
        return result;
    }
}
