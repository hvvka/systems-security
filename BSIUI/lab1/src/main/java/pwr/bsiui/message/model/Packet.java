package pwr.bsiui.message.model;

import java.math.BigInteger;

/**
 * @author <a href="mailto:226154@student.pwr.edu.pl">Hanna Grodzicka</a>
 */
public class Packet {

    private BigInteger p;

    private BigInteger g;

    private String id;

    private String message = "";

    private BigInteger publicKey;

    private String encryption;

    private Packet() {
        // used by Jackson 2.x
    }

    public Packet(BigInteger p, BigInteger g, String id, String message, BigInteger publicKey, String encryption) {
        this.p = p;
        this.g = g;
        this.id = id;
        this.message = message;
        this.publicKey = publicKey;
        this.encryption = encryption;
    }

    public BigInteger getP() {
        return p;
    }

    public BigInteger getG() {
        return g;
    }

    public String getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public BigInteger getPublicKey() {
        return publicKey;
    }

    public String getEncryption() {
        return encryption;
    }

    public void setEncryption(String encryption) {
        this.encryption = encryption;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Packet packet = (Packet) o;

        if (p != null ? !p.equals(packet.p) : packet.p != null) return false;
        if (g != null ? !g.equals(packet.g) : packet.g != null) return false;
        if (id != null ? !id.equals(packet.id) : packet.id != null) return false;
        if (message != null ? !message.equals(packet.message) : packet.message != null) return false;
        if (publicKey != null ? !publicKey.equals(packet.publicKey) : packet.publicKey != null) return false;
        return encryption != null ? encryption.equals(packet.encryption) : packet.encryption == null;
    }

    @Override
    public int hashCode() {
        int result = p != null ? p.hashCode() : 0;
        result = 31 * result + (g != null ? g.hashCode() : 0);
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (publicKey != null ? publicKey.hashCode() : 0);
        result = 31 * result + (encryption != null ? encryption.hashCode() : 0);
        return result;
    }
}
