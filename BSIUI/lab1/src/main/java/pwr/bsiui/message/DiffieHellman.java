package pwr.bsiui.message;

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
        // TODO: change these temporary values to something more sophisticated
        this.pgKeys = new PGKeys(2, 1);
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

        private final long p;

        private final long g;

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
