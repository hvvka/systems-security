package pwr.bsiui.message;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author <a href="mailto:226154@student.pwr.edu.pl">Hanna Grodzicka</a>
 */
public class DiffieHellmanTest {

    private static final long P = 1;

    private static final long G = 2;

    private DiffieHellman diffieHellman;

    @Before
    public void setUp() {
        diffieHellman = new DiffieHellman(P, G);
    }

    @Test
    public void calculatePublicKey() {
        // given
        long privateKey = 5;

        // when
        long publicKey = diffieHellman.calculatePublicKey(privateKey);

        // then
        long expectedPublicKey = (long) Math.pow(G, privateKey) % P;
        assertEquals(expectedPublicKey, publicKey);
    }

    @Test
    public void calculateSharedSecretKey() {
        // given
        long othersPublicKey = 3;
        long yourPrivateKey = 7;

        // when
        long secretKey = diffieHellman.calculateSharedSecretKey(othersPublicKey, yourPrivateKey);

        // then
        long expectedSecretKey = (long) Math.pow(othersPublicKey, yourPrivateKey) % P;
        assertEquals(expectedSecretKey, secretKey);
    }

    @Test
    public void sharedSecretKeysForClientAndServerAreTheSame() {
        // given
        long serverPublicKey = 2;
        long serverPrivateKey = 3;
        long clientPublicKey = 4;
        long clientPrivateKey = 5;

        // when
        long serverSecretKey = diffieHellman.calculateSharedSecretKey(clientPublicKey, serverPrivateKey);
        long clientSecretKey = diffieHellman.calculateSharedSecretKey(serverPublicKey, clientPrivateKey);

        // then
        assertEquals(serverSecretKey, clientSecretKey);
    }

    @Test
    public void getP() {
        assertEquals(P, diffieHellman.getP());
    }

    @Test
    public void getG() {
        assertEquals(G, diffieHellman.getG());
    }
}
