package pwr.bsiui.message.encryption;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author <a href="mailto:226154@student.pwr.edu.pl">Hanna Grodzicka</a>
 */
public class NoneEncryptionTest {

    private Encryption noneEncryption;

    @Before
    public void setUp() {
        noneEncryption = new NoneEncryption();
    }

    @Test
    public void encrypt() {
        // given
        String message = "ABC 123 xyz! @abc ^&XYZ";

        // when
        String encryptedMessage = noneEncryption.encrypt(message);

        // then
        String expectedEncryption = "ABC 123 xyz! @abc ^&XYZ";
        assertEquals(expectedEncryption, encryptedMessage);
    }

    @Test
    public void decrypt() {
        // given
        String message = "DEF 123 abc! @def ^&ABC";

        // when
        String decryptedMessage = noneEncryption.decrypt(message);

        // then
        String expectedDecryption = "DEF 123 abc! @def ^&ABC";
        assertEquals(expectedDecryption, decryptedMessage);
    }

    @Test
    public void getName() {
        // when
        String encryptionName = noneEncryption.getName();

        // then
        String expectedEncryptionName = "none";
        assertEquals(expectedEncryptionName, encryptionName);
    }
}
