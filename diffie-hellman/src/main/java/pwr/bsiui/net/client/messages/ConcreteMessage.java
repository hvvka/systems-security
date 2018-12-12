package pwr.bsiui.net.client.messages;

import com.blogspot.debukkitsblog.net.Client;
import pwr.bsiui.message.model.Packet;
import pwr.bsiui.message.model.PacketBuilder;

import java.util.Scanner;

/**
 * Constructs concrete message that can be send to server.
 *
 * @author <a href="mailto:226154@student.pwr.edu.pl">Hanna Grodzicka</a>
 */
public class ConcreteMessage implements Message {

    private final Scanner reader;

    private final String encryptionName;

    ConcreteMessage(Scanner reader, String encryptionName) {
        this.reader = reader;
        this.encryptionName = encryptionName;
    }

    @Override
    public Packet createMessage() {
        System.out.print("Type in your message: ");
        reader.nextLine();
        String message = reader.nextLine();
        return new PacketBuilder(encryptionName)
                .setId(Client.DEFAULT_USER_ID)
                .setMessage(message)
                .createExchangePacket();
    }

    @Override
    public String getMethodName() {
        return "SEND_MESSAGE";
    }

    @Override
    public String getInfo() {
        return "Send broadcast message to [server]";
    }
}
