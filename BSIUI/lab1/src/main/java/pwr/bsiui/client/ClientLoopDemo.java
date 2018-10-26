package pwr.bsiui.client;

import com.blogspot.debukkitsblog.net.Client;
import pwr.bsiui.message.DiffieHellman;
import pwr.bsiui.message.ExchangePacketProvider;
import pwr.bsiui.message.model.Packet;
import pwr.bsiui.message.model.PacketBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author <a href="mailto:226154@student.pwr.edu.pl">Hanna Grodzicka</a>
 */
class ClientLoopDemo {

    private static final Map<Integer, String> MENU = new HashMap<>();

    static {
        MENU.put(1, "Request [server] public keys (P and G)");
        MENU.put(2, "Request [server] public key");
        MENU.put(3, "Send your public key to [server]");
        MENU.put(4, "Send broadcast message to [server]");
        MENU.put(5, "Change message encryption [\"none\", \"xor\", \"caesar\"]");
        MENU.put(6, "View your private key");
        MENU.put(7, "View your public key");
        MENU.put(8, "View shared secret key");
        MENU.put(9, "Stop");
    }

    private final long privateKey;

    private final Client client;

    private final ExchangePacketProvider exchangePacketProvider;

    private final Scanner reader;

    private DiffieHellman diffieHellman;

    private String encryptionName;

    private boolean running;

    ClientLoopDemo(int port) {
        this.privateKey = ThreadLocalRandom.current().nextLong(Long.MAX_VALUE);
        this.client = new SecureClient("localhost", port);
        registerReceiveBroadcast();
        this.exchangePacketProvider = new ExchangePacketProvider();
        this.reader = new Scanner(System.in);
        this.encryptionName = "none";
        this.running = true;
    }

    private void registerReceiveBroadcast() {
        this.client.registerMethod("BROADCAST", (msg, socket) -> {
            System.err.println(msg.get(1));
            Packet packet = exchangePacketProvider.fromSecureJson((String) msg.get(1));
            System.err.printf("Received broadcast message from user %s: '%s'", packet.getId(), packet.getMessage());
        });
    }

    void start() {
        while (running) {
            MENU.keySet().forEach(k -> System.out.printf("%d. %s\n", k, MENU.get(k)));
            switch (reader.nextInt()) {
                case 1: requestPG();
                    break;
                case 2: requestPublicKey();
                    break;
                case 3: sendYourPublicKey();
                    break;
                case 4: sendMessage();
                    break;
                case 5: changeMessageEncryption();
                    break;
                case 6: System.err.printf("Your private key: %s\n", privateKey);
                    break;
                case 7: System.err.printf("Your public key: %s\n", diffieHellman.calculatePublicKey());
                    break;
                case 8: System.err.printf("Shared secret key: %s\n", diffieHellman.calculateSharedSecretKey());
                    break;
                case 9: running = false;
                    client.stop();
                    reader.close();
                    break;
                default: System.err.printf("Invalid option\n");
                    break;
            }
        }
    }

    // TODO: refactor below methods to functional interface / parametrized method
    private void requestPG() {
        String response = (String) client.sendMessage("REQUEST_P_G").get(1);
        Packet packet = exchangePacketProvider.fromSecureJson(response);
        System.err.printf(">> Received P=%s, G=%s\n", packet.getP(), packet.getG());
        this.diffieHellman = new DiffieHellman(packet.getP(), packet.getG(), privateKey);
    }

    private void requestPublicKey() {
        String response = (String) client.sendMessage("REQUEST_KEY").get(1);
        Packet packet = exchangePacketProvider.fromSecureJson(response);
        System.err.printf(">> Received server's public key=%s\n", packet.getPublicKey());
        this.diffieHellman.setOthersPublicKey(packet.getPublicKey());
    }

    private void sendYourPublicKey() {
        if (diffieHellman == null) {
            requestPG();
            requestPublicKey();
        }
        Packet packet = new PacketBuilder(encryptionName)
                .setId(Client.DEFAULT_USER_ID)
                .setPublicKey(diffieHellman.calculatePublicKey())
                .createExchangePacket();
        String response = (String) client.sendMessage("SEND_PUBLIC_KEY", exchangePacketProvider.toSecureJson(packet)).get(1);
        Packet receivedPacket = exchangePacketProvider.fromSecureJson(response);
        System.err.printf(">> %s\n", receivedPacket.getMessage());
    }

    private void sendMessage() {
        System.out.print("Type in your message: ");
        reader.nextLine();
        String message = reader.nextLine();
        Packet packet = new PacketBuilder(encryptionName)
                .setId(Client.DEFAULT_USER_ID)
                .setMessage(message)
                .createExchangePacket();
        String response = (String) client.sendMessage("SEND_MESSAGE", exchangePacketProvider.toSecureJson(packet)).get(1);
        Packet receivedPacket = exchangePacketProvider.fromSecureJson(response);
        System.err.printf(">> Response: '%s'\n", receivedPacket.getMessage());
    }

    private void changeMessageEncryption() {
        System.out.print("Choose new encryption method: ");
        this.encryptionName = reader.next();
        Packet packet = new PacketBuilder(encryptionName)
                .setId(Client.DEFAULT_USER_ID)
                .createExchangePacket();
        String response = (String) client.sendMessage("SEND_CHANGE_ENCRYPTION_METHOD", exchangePacketProvider.toSecureJson(packet)).get(1);
        Packet receivedPacket = exchangePacketProvider.fromSecureJson(response);
        System.err.printf(">> %s\n", receivedPacket.getMessage());
    }
}
