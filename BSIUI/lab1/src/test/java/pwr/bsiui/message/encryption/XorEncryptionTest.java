package pwr.bsiui.message.encryption;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author <a href="mailto:226154@student.pwr.edu.pl">Hanna Grodzicka</a>
 */
public class XorEncryptionTest {

    /**
     * Last character is "3", that is 0b00110011
     */
    private static final String KEY = "123";

    private Encryption xorEncryption;

    @Before
    public void setUp() {
        xorEncryption = new XorEncryption(KEY);
    }

    @Test
    public void encrypt() {
        // given
        String message = "ABC 123 xyz! @abc ^&XYZ";

        // when
        String encryptedMessage = xorEncryption.encrypt(message);

        // then
        String expectedEncryption = "rqp\u0013\u0002\u0001\u0000\u0013KJI\u0012\u0013sRQP\u0013m\u0015kji";
        assertEquals(expectedEncryption, encryptedMessage);
    }

    @Test
    public void decrypt() {
        // given
        String message = "rqp\u0013\u0002\u0001\u0000\u0013KJI\u0012\u0013sRQP\u0013m\u0015kji";

        // when
        String decryptedMessage = xorEncryption.decrypt(message);
        String expectedDecryption = "ABC 123 xyz! @abc ^&XYZ";

        // then
        assertEquals(expectedDecryption, decryptedMessage);
    }

    @Test
    public void getName() {
        // when
        String encryptionName = xorEncryption.getName();

        // then
        String expectedEncryptionName = "xor";
        assertEquals(expectedEncryptionName, encryptionName);
    }
}