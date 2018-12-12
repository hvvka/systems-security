package pwr.bsiui.message.model;

import java.math.BigInteger;

public class PacketBuilder {

    private BigInteger p;

    private BigInteger g;

    private String id;

    private String message = "";

    private BigInteger publicKey;

    private String encryption;

    public PacketBuilder() {
        this.encryption = "none";
    }

    public PacketBuilder(String encryption) {
        this.encryption = encryption;
    }

    public PacketBuilder setP(BigInteger p) {
        this.p = p;
        return this;
    }

    public PacketBuilder setG(BigInteger g) {
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

    public PacketBuilder setPublicKey(BigInteger publicKey) {
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