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

    private final BigInteger yourPrivateKey;

    private BigInteger othersPublicKey;

    public DiffieHellman(BigInteger yourPrivateKey) {
        this.pgKeys = new PGKeys();
        this.yourPrivateKey = yourPrivateKey;
    }

    public DiffieHellman(BigInteger p, BigInteger g, BigInteger yourPrivateKey) {
        this.pgKeys = new PGKeys(p, g);
        this.yourPrivateKey = yourPrivateKey;
    }

    /**
     * publicKey = g^yourPrivateKey mod p
     *
     * @return public key that can be used for message exchange
     */
    public BigInteger calculatePublicKey() {
        return pgKeys.getG().modPow(yourPrivateKey, pgKeys.getP());
    }

    /**
     * secretKey = othersPublicKey^yourPrivateKey mod p
     *
     * @return secret key shared by both sides and used for secure communication
     */
    public BigInteger calculateSharedSecretKey() {
        return othersPublicKey.modPow(yourPrivateKey, pgKeys.getP());
    }

    public BigInteger getP() {
        return pgKeys.getP();
    }

    public BigInteger getG() {
        return pgKeys.getG();
    }

    public BigInteger getOthersPublicKey() {
        return othersPublicKey;
    }

    public void setOthersPublicKey(BigInteger othersPublicKey) {
        this.othersPublicKey = othersPublicKey;
    }

    /**
     * Server public keys (P and G).
     */
    private static class PGKeys {

        /**
         * Primary number.
         */
        private final BigInteger p;

        /**
         * Primitive root modulo p.
         */
        private final BigInteger g;

        PGKeys() {
            this.p = new BigInteger(63, 12, new SecureRandom());
            this.g = new BigInteger(String.valueOf(4 - new SecureRandom().nextInt(3)));
        }

        PGKeys(BigInteger p, BigInteger g) {
            this.p = p;
            this.g = g;
        }

        BigInteger getP() {
            return p;
        }

        BigInteger getG() {
            return g;
        }
    }
}
