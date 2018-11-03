package pwr.bsiui.message.encryption;

/**
 * Does no modification on message.
 *
 * @author <a href="mailto:226154@student.pwr.edu.pl">Hanna Grodzicka</a>
 */
public class NoneEncryption implements Encryption {

    public NoneEncryption() {
        // used by Jackson 2.x
    }

    @Override
    public String encrypt(String message) {
        return message;
    }

    @Override
    public String decrypt(String message) {
        return message;
    }

    @Override
    public String getName() {
        return "none";
    }
}
