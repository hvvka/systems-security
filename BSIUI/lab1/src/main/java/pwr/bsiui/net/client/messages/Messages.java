package pwr.bsiui.net.client.messages;

import pwr.bsiui.message.DiffieHellman;

import java.util.Scanner;

/**
 * @author <a href="mailto:226154@student.pwr.edu.pl">Hanna Grodzicka</a>
 */
public class Messages {

    private final Scanner reader;

    private String encryptionName;

    public Messages(Scanner reader) {
        this.encryptionName = "none";
        this.reader = reader;
    }

    public Message createPublicKey(DiffieHellman diffieHellman) {
        PublicKeyMessage publicKeyMessage = new PublicKeyMessage(diffieHellman, encryptionName);
        return publicKeyMessage;
    }

    public Message createConcreteMessage() {
        return new ConcreteMessage(reader, encryptionName);
    }

    public Message createEncryptionMethod(DiffieHellman diffieHellman) {
        EncryptionMethodMessage encryptionMethodMessage = new EncryptionMethodMessage(reader, diffieHellman);
        this.encryptionName = encryptionMethodMessage.getEncryptionName();
        return encryptionMethodMessage;
    }
}
