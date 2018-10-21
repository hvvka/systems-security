package pwr.bsiui.message.encryption;

/**
 * @author <a href="mailto:226154@student.pwr.edu.pl">Hanna Grodzicka</a>
 */
public interface Encryption {

    String encrypt(String message);

    String decrypt(String message);

    String getName();
}
