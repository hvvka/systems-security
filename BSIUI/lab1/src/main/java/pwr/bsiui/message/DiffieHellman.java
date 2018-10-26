package pwr.bsiui.message;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Implements Diffie–Hellman key exchange algorithm and helper methods.
 *
 * @author <a href="mailto:226154@student.pwr.edu.pl">Hanna Grodzicka</a>
 * @see <a href="https://en.wikipedia.org/wiki/Diffie–Hellman_key_exchange">Diffie–Hellman key exchange</a>
 */
public class DiffieHellman {

    private final PGKeys pgKeys;

    private final long yourPrivateKey;

    private long othersPublicKey;

    public DiffieHellman(long yourPrivateKey) {
        this.yourPrivateKey = yourPrivateKey;
        this.pgKeys = new PGKeys(23, 5);
    }

    public DiffieHellman(long p, long g, long yourPrivateKey) {
        this.pgKeys = new PGKeys(p, g);
        this.yourPrivateKey = yourPrivateKey;
    }

    /**
     * publicKey = g^yourPrivateKey mod p
     *
     * @return public key that can be used for message exchange
     */
    public long calculatePublicKey() {
        return (long) (Math.pow(pgKeys.getG(), yourPrivateKey) % pgKeys.getP());
    }

    /**
     * secretKey = othersPublicKey^yourPrivateKey mod p
     *
     * @return secret key shared by both sides and used for secure communication
     */
    public long calculateSharedSecretKey() {
        return (long) Math.pow(othersPublicKey, yourPrivateKey) % pgKeys.getP();
    }

    public long getP() {
        return pgKeys.getP();
    }

    public long getG() {
        return pgKeys.getG();
    }

    public long getOthersPublicKey() {
        return othersPublicKey;
    }

    public void setOthersPublicKey(long othersPublicKey) {
        this.othersPublicKey = othersPublicKey;
    }

    private static class PGKeys {

        /**
         * Primary number.
         */
        private final long p;

        /**
         * Primitive root modulo p.
         */
        private final long g;

        // TODO: Count primitive P and G as primitive root modulo p
        PGKeys() {
            this.p = ThreadLocalRandom.current().nextLong(Long.MAX_VALUE);
            this.g = ThreadLocalRandom.current().nextLong(Long.MAX_VALUE);
        }

        PGKeys(long p, long g) {
            this.p = p;
            this.g = g;
        }

        long getP() {
            return p;
        }

        long getG() {
            return g;
        }
    }
}
