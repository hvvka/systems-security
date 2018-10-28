package pwr.bsiui.net.client.messages;

import pwr.bsiui.message.DiffieHellman;

import java.util.Scanner;
import java.util.function.Supplier;

/**
 * @author <a href="mailto:226154@student.pwr.edu.pl">Hanna Grodzicka</a>
 */
public class Messages {

    private final Scanner reader;

    private Supplier<String> encryptionNameSupplier;

    public Messages(Scanner reader) {
        this.encryptionNameSupplier = () -> "none";
        this.reader = reader;
    }

    public Message createPublicKey(DiffieHellman diffieHellman) {
        return new PublicKeyMessage(diffieHellman, encryptionNameSupplier.get());
    }

    public Message createConcreteMessage() {
        return new ConcreteMessage(reader, encryptionNameSupplier.get());
    }

    public Message createEncryptionMethod() {
        EncryptionMethodMessage encryptionMethodMessage = new EncryptionMethodMessage(reader);
        this.encryptionNameSupplier = encryptionMethodMessage::getEncryptionName;
        return encryptionMethodMessage;
    }
}
