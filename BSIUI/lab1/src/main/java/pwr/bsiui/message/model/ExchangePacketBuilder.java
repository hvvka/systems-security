package pwr.bsiui.message.model;

public class ExchangePacketBuilder {

    private long p;

    private long g;

    private String message;

    private long publicKey;

    private String encryption;

    public ExchangePacketBuilder() {
    }

    public ExchangePacketBuilder(String encryption) {
        this.encryption = encryption;
    }

    public ExchangePacketBuilder setP(long p) {
        this.p = p;
        return this;
    }

    public ExchangePacketBuilder setG(long g) {
        this.g = g;
        return this;
    }

    public ExchangePacketBuilder setMessage(String message) {
        this.message = message;
        return this;
    }

    public ExchangePacketBuilder setPublicKey(long publicKey) {
        this.publicKey = publicKey;
        return this;
    }

    public ExchangePacketBuilder setEncryption(String encryption) {
        this.encryption = encryption;
        return this;
    }

    public ExchangePacket createExchangePacket() {
        return new ExchangePacket(p, g, message, publicKey, encryption);
    }
}