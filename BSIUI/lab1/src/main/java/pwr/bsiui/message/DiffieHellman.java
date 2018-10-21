package pwr.bsiui.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implements Diffie–Hellman key exchange algorithm and helper methods.
 *
 * @author <a href="mailto:226154@student.pwr.edu.pl">Hanna Grodzicka</a>
 * @see <a href="https://en.wikipedia.org/wiki/Diffie–Hellman_key_exchange">Diffie–Hellman key exchange</a>
 */
public class DiffieHellman {

    private static final Logger LOG = LoggerFactory.getLogger(DiffieHellman.class);

    private final PublicKeys publicKeys;

    public DiffieHellman() {
        // TODO: change these temporary values to something more sophisticated
        this.publicKeys = new PublicKeys(2, 1);
    }

    public DiffieHellman(long p, long g) {
        this.publicKeys = new PublicKeys(p, g);
    }

    /**
     * publicKey = g^privateKey mod p
     *
     * @param privateKey user-specific private key
     * @return public key that can be used for message exchange
     */
    public long calculatePublicKey(long privateKey) {
        return (long) (Math.pow(publicKeys.getG(), privateKey) % publicKeys.getP());
    }

    /**
     * secretKey = othersPublicKey^yourPrivateKey mod p
     *
     * @param othersPublicKey public key of side you want to communicate to
     * @param yourPrivateKey  your own private key
     * @return secret key shared by both sides and used for secure communication
     */
    public long calculateSharedSecretKey(long othersPublicKey, long yourPrivateKey) {
        return (long) Math.pow(othersPublicKey, yourPrivateKey) % publicKeys.getP();
    }

    public long getP() {
        return publicKeys.getP();
    }

    public long getG() {
        return publicKeys.getG();
    }

    private static class PublicKeys {

        private final long p;

        private final long g;

        PublicKeys(long p, long g) {
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
