package pwr.bsiui.message;

import java.math.BigInteger;
import java.security.SecureRandom;

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
        this.pgKeys = new PGKeys();
        this.yourPrivateKey = yourPrivateKey;
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

    /**
     * Server public keys (P and G).
     */
    private static class PGKeys {

        /**
         * Primary number.
         */
        private final long p;

        /**
         * Primitive root modulo p.
         */
        private final long g;

        PGKeys() {
            this.p = new BigInteger(63, 12, new SecureRandom()).longValue();
            this.g = new BigInteger(String.valueOf(4 - new SecureRandom().nextInt(3))).longValue();
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
