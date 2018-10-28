package pwr.bsiui.net.client.messages;

import com.blogspot.debukkitsblog.net.Client;
import pwr.bsiui.message.DiffieHellman;
import pwr.bsiui.message.model.Packet;
import pwr.bsiui.message.model.PacketBuilder;

import java.util.Scanner;

/**
 * @author <a href="mailto:226154@student.pwr.edu.pl">Hanna Grodzicka</a>
 */
public class EncryptionMethodMessage implements Message {

    private final Scanner reader;

    private final DiffieHellman diffieHellman;

    private String encryptionName;

    EncryptionMethodMessage(Scanner reader, DiffieHellman diffieHellman) {
        this.reader = reader;
        this.diffieHellman = diffieHellman;
    }

    @Override
    public Packet createMessage() {
        System.out.print("Choose new encryption method: ");
        this.encryptionName = reader.next();
        return new PacketBuilder(encryptionName)
                .setId(Client.DEFAULT_USER_ID)
                .createExchangePacket();
    }

    @Override
    public String getMethodName() {
        return "SEND_CHANGE_ENCRYPTION_METHOD";
    }

    @Override
    public String getInfo() {
        return "Change message encryption [\"none\", \"xor\", \"caesar\"]";
    }

    String getEncryptionName() {
        return encryptionName;
    }
}
