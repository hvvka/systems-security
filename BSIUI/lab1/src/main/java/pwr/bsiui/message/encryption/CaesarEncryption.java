package pwr.bsiui.message.encryption;

/**
 * @author <a href="mailto:226154@student.pwr.edu.pl">Hanna Grodzicka</a>
 */
public class CaesarEncryption implements Encryption {

    private static final int ALPHABET_SIZE = 26;

    private final int shift;

    public CaesarEncryption() {
        shift = 13;
    }

    public CaesarEncryption(int shift) {
        this.shift = shift;
    }

    @Override
    public String encrypt(String message) {
        return encodeCaesar(message, shift);
    }

    @Override
    public String decrypt(String message) {
        return encodeCaesar(message, ALPHABET_SIZE - shift);
    }

    @Override
    public String getName() {
        return "caesar";
    }

    private String encodeCaesar(String message, int offset) {
        offset = offset % ALPHABET_SIZE + ALPHABET_SIZE;
        StringBuilder encodedMessage = new StringBuilder();
        for (char c : message.toCharArray()) {
            if (Character.isLetter(c)) {
                encodedMessage.append(Character.isUpperCase(c)
                        ? (char) ('A' + (c - 'A' + offset) % ALPHABET_SIZE)
                        : (char) ('a' + (c - 'a' + offset) % ALPHABET_SIZE));
            } else {
                encodedMessage.append(c);
            }
        }
        return encodedMessage.toString();
    }
}
