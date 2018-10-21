package pwr.bsiui.message.encryption;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author <a href="mailto:226154@student.pwr.edu.pl">Hanna Grodzicka</a>
 */
public class CaesarEncryptionTest {

    private Encryption caesarEncryption;

    @Before
    public void setUp() {
        caesarEncryption = new CaesarEncryption(3);
    }

    @Test
    public void encrypt() {
        // given
        String message = "ABC 123 xyz! @abc ^&XYZ";

        // when
        String encryptedMessage = caesarEncryption.encrypt(message);

        // then
        String expectedEncryption = "DEF 123 abc! @def ^&ABC";
        assertEquals(expectedEncryption, encryptedMessage);
    }

    @Test
    public void decrypt() {
        // given
        String message = "DEF 123 abc! @def ^&ABC";

        // when
        String decryptedMessage = caesarEncryption.decrypt(message);
        String expectedDecryption = "ABC 123 xyz! @abc ^&XYZ";

        // then
        assertEquals(expectedDecryption, decryptedMessage);
    }

    @Test
    public void getName() {
        // when
        String encryptionName = caesarEncryption.getName();

        // then
        String expectedEncryptionName = "caesar";
        assertEquals(expectedEncryptionName, encryptionName);
    }
}