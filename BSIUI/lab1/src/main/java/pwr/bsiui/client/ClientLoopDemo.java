package pwr.bsiui.client;

import com.blogspot.debukkitsblog.net.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pwr.bsiui.message.DiffieHellman;
import pwr.bsiui.message.PacketJsonSerializer;
import pwr.bsiui.message.model.Packet;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * @author <a href="mailto:226154@student.pwr.edu.pl">Hanna Grodzicka</a>
 */
class ClientLoopDemo {

    private static final Logger LOG = LoggerFactory.getLogger(ClientLoopDemo.class);

    private static final Map<Integer, String> MENU = new HashMap<>();

    private static final long privateKey = 10;  // TODO: generate random value

    static {
        MENU.put(1, "1. Request [server] public keys (P and G)");
        MENU.put(2, "2. Request [server] public key");
        MENU.put(3, "3. View your private key");
        MENU.put(4, "4. View your public key");
        MENU.put(5, "5. View shared secret key");
        MENU.put(6, "6. Send your public key to [server]");
        MENU.put(7, "7. Send message to [server]");
        MENU.put(8, "8. Change message encryption [\"none\", \"xor\", \"caesar\"]");
        MENU.put(9, "9. Stop");
    }

    private final Client client;

    private final PacketJsonSerializer packetJsonSerializer;

    private final Scanner reader;

    private DiffieHellman diffieHellman;

    private boolean running;

    ClientLoopDemo(int port) {
        this.client = new SecureClient("localhost", port);
        this.packetJsonSerializer = new PacketJsonSerializer();
        this.reader = new Scanner(System.in);
        this.running = true;
    }

    void start() {
        while (running) {
            MENU.values().forEach(System.out::println);
            switch (reader.nextInt()) {
                case 1: requestPG();
                    break;
                case 2: requestPublicKey();
                    break;
                case 3: LOG.info("Your private key: {}", privateKey);
                    break;
                case 4: LOG.info("Your public key: {}", diffieHellman.calculatePublicKey());
                    break;
                case 5: LOG.info("Shared secret key: {}", diffieHellman.calculateSharedSecretKey());
                    break;
                case 6: sendYourPublicKey();
                    break;
                case 7: sendMessage();
                    break;
                case 8: changeMessageEncryption();
                    break;
                case 9: running = false;
                    break;
                default: LOG.info("Invalid option");
                    break;
            }
        }
    }

    // TODO: refactor below methods to functional interface / parametrized method
    private void requestPG() {
        String response = (String) client.sendMessage("REQUEST_P_G").get(1);
        Packet packet = packetJsonSerializer.fromJson(response);
        System.err.printf(">> Received P=%s, G=%s\n", packet.getP(), packet.getG());
        this.diffieHellman = new DiffieHellman(packet.getP(), packet.getG(), privateKey);
    }

    private void requestPublicKey() {
        String response = (String) client.sendMessage("REQUEST_KEY").get(1);
        Packet packet = packetJsonSerializer.fromJson(response);
        System.err.printf(">> Received server's public key=%s\n", packet.getPublicKey());
        this.diffieHellman.setOthersPublicKey(packet.getPublicKey());
    }

    private void sendYourPublicKey() {
        if (diffieHellman == null) {
            requestPG();
            requestPublicKey();
        }
        String response = (String) client.sendMessage("SEND_PUBLIC_KEY", diffieHellman.calculatePublicKey()).get(1);
        Packet packet = packetJsonSerializer.fromJson(response);
        System.err.printf(">> %s\n", packet.getMessage());
    }

    private void sendMessage() {
        System.out.print("Type in your message: ");
        String message = reader.nextLine();
        String response = (String) client.sendMessage("SEND_MESSAGE", message).get(1);
        Packet packet = packetJsonSerializer.fromJson(response);
        System.err.printf(">> Response: '%s'\n", packet.getMessage());
    }

    private void changeMessageEncryption() {
        System.out.print("Choose new encryption method: ");
        String encryption = reader.next();
        String response = (String) client.sendMessage("SEND_CHANGE_ENCRYPTION_METHOD", encryption).get(1);
        Packet packet = packetJsonSerializer.fromJson(response);
        System.err.printf(">> %s\n", packet.getMessage());
    }
}
