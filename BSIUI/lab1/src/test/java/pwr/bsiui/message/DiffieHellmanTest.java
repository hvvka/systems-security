package pwr.bsiui.message;

import org.junit.Before;
import org.junit.Test;

import java.math.BigInteger;

import static org.junit.Assert.assertEquals;

/**
 * @author <a href="mailto:226154@student.pwr.edu.pl">Hanna Grodzicka</a>
 */
public class DiffieHellmanTest {

    private static final BigInteger P = BigInteger.valueOf(23);

    private static final BigInteger G = BigInteger.valueOf(5);

    private final BigInteger yourPrivateKey = BigInteger.valueOf(5);

    private DiffieHellman diffieHellman;

    @Before
    public void setUp() {
        diffieHellman = new DiffieHellman(P, G, yourPrivateKey);
    }

    @Test
    public void calculatePublicKey() {
        // when
        BigInteger publicKey = diffieHellman.calculatePublicKey();

        // then
        int expectedPublicKey = (int) (Math.pow(G.intValue(), yourPrivateKey.intValue()) % P.intValue());
        assertEquals(expectedPublicKey, publicKey.intValue());
    }

    @Test
    public void calculateSharedSecretKey() {
        // given
        BigInteger othersPublicKey = BigInteger.valueOf(3);
        diffieHellman.setOthersPublicKey(othersPublicKey);

        // when
        BigInteger secretKey = diffieHellman.calculateSharedSecretKey();

        // then
        int expectedSecretKey = (int) Math.pow(othersPublicKey.intValue(), yourPrivateKey.intValue()) % P.intValue();
        assertEquals(expectedSecretKey, secretKey.intValue());
    }

    @Test
    public void sharedSecretKeysForClientAndServerAreTheSame() {
        // given
        BigInteger serverPrivateKey = BigInteger.valueOf(6);
        BigInteger clientPrivateKey = BigInteger.valueOf(15);

        DiffieHellman serverDiffieHellman = new DiffieHellman(P, G, serverPrivateKey);
        BigInteger serverPublicKey = serverDiffieHellman.calculatePublicKey();

        DiffieHellman clientDiffieHellman = new DiffieHellman(P, G, clientPrivateKey);
        BigInteger clientPublicKey = clientDiffieHellman.calculatePublicKey();

        serverDiffieHellman.setOthersPublicKey(clientPublicKey);
        clientDiffieHellman.setOthersPublicKey(serverPublicKey);

        // when
        BigInteger serverSecretKey = serverDiffieHellman.calculateSharedSecretKey();
        BigInteger clientSecretKey = clientDiffieHellman.calculateSharedSecretKey();

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
