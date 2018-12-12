package pwr.bsiui.net.client.messages;

import com.blogspot.debukkitsblog.net.Client;
import pwr.bsiui.message.DiffieHellman;
import pwr.bsiui.message.model.Packet;
import pwr.bsiui.message.model.PacketBuilder;

/**
 * Sends message to server with user public key.
 *
 * @author <a href="mailto:226154@student.pwr.edu.pl">Hanna Grodzicka</a>
 */
public class PublicKeyMessage implements Message {

    private final DiffieHellman diffieHellman;

    private final String encryptionName;

    PublicKeyMessage(DiffieHellman diffieHellman, String encryptionName) {
        this.diffieHellman = diffieHellman;
        this.encryptionName = encryptionName;
    }

    @Override
    public Packet createMessage() {
        if (diffieHellman == null) {
            throw new IllegalStateException("!! You haven't requested server's public keys !!");
        }
        return new PacketBuilder(encryptionName)
                .setId(Client.DEFAULT_USER_ID)
                .setPublicKey(diffieHellman.calculatePublicKey())
                .createExchangePacket();
    }

    @Override
    public String getMethodName() {
        return "SEND_PUBLIC_KEY";
    }

    @Override
    public String getInfo() {
        return "Send your public key to [server]";
    }
}
