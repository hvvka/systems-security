package pwr.bsiui.message.encryption;

/**
 * @author <a href="mailto:226154@student.pwr.edu.pl">Hanna Grodzicka</a>
 */
public class NoneEncryption implements Encryption {

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
