package pwr.bsiui.message.model;

public class PacketBuilder {

    private long p;

    private long g;

    private String id;

    private String message = "";

    private long publicKey;

    private String encryption;

    public PacketBuilder() {
    }

    public PacketBuilder(String encryption) {
        this.encryption = encryption;
    }

    public PacketBuilder setP(long p) {
        this.p = p;
        return this;
    }

    public PacketBuilder setG(long g) {
        this.g = g;
        return this;
    }

    public PacketBuilder setId(String id) {
        this.id = id;
        return this;
    }

    public PacketBuilder setMessage(String message) {
        this.message = message;
        return this;
    }

    public PacketBuilder setPublicKey(long publicKey) {
        this.publicKey = publicKey;
        return this;
    }

    public PacketBuilder setEncryption(String encryption) {
        this.encryption = encryption;
        return this;
    }

    public Packet createExchangePacket() {
        return new Packet(p, g, id, message, publicKey, encryption);
    }
}