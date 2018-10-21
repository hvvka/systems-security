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

    private final long yourPrivateKey = 5;

    private DiffieHellman diffieHellman;

    @Before
    public void setUp() {
        diffieHellman = new DiffieHellman(P, G, yourPrivateKey);
    }

    @Test
    public void calculatePublicKey() {
        // when
        long publicKey = diffieHellman.calculatePublicKey();

        // then
        long expectedPublicKey = (long) Math.pow(G, yourPrivateKey) % P;
        assertEquals(expectedPublicKey, publicKey);
    }

    @Test
    public void calculateSharedSecretKey() {
        // given
        long othersPublicKey = 3;
        diffieHellman.setOthersPublicKey(othersPublicKey);

        // when
        long secretKey = diffieHellman.calculateSharedSecretKey();

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

        DiffieHellman serverDiffieHellman = new DiffieHellman(P, G, serverPrivateKey);
        serverDiffieHellman.setOthersPublicKey(clientPublicKey);

        DiffieHellman clientDiffieHellman = new DiffieHellman(P, G, clientPrivateKey);
        clientDiffieHellman.setOthersPublicKey(serverPublicKey);

        // when
        long serverSecretKey = serverDiffieHellman.calculateSharedSecretKey();
        long clientSecretKey = clientDiffieHellman.calculateSharedSecretKey();

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
