package pwr.bsiui.net;

import com.blogspot.debukkitsblog.net.Client;
import pwr.bsiui.message.DiffieHellman;
import pwr.bsiui.message.ExchangePacketProvider;
import pwr.bsiui.message.model.Packet;
import pwr.bsiui.message.model.PacketBuilder;
import pwr.bsiui.net.client.SecureClient;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * An instance of this class provides fully configured client on specified port.
 * Client has a menu of possible server interactions.
 *
 * @author <a href="mailto:226154@student.pwr.edu.pl">Hanna Grodzicka</a>
 */
public class ClientLoop {

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
        MENU.put(0, "Stop");
    }

    private final BigInteger privateKey;

    private final Client client;

    private final ExchangePacketProvider exchangePacketProvider;

    private final Scanner reader;

    private DiffieHellman diffieHellman;

    private String encryptionName;

    private boolean running;

    public ClientLoop(int port) {
        this.privateKey = BigInteger.valueOf(ThreadLocalRandom.current().nextInt(500));
        this.client = new SecureClient("localhost", port);
        registerReceiveBroadcast();
        this.exchangePacketProvider = new ExchangePacketProvider();
        this.reader = new Scanner(System.in);
        this.encryptionName = "none";
        this.running = true;
    }

    public void start() {
        while (running) {
            MENU.keySet().stream()
                    .filter(k -> ((k != 7 && k != 8) || diffieHellman != null))
                    .forEach(k -> System.out.printf("%d. %s\n", k, MENU.get(k)));
            switch (reader.nextInt()) {
                case 1: request("REQUEST_P_G", this::requestPG);
                    break;
                case 2: request("REQUEST_KEY", this::requestPublicKey);
                    break;
                case 3: send("SEND_PUBLIC_KEY", this::sendYourPublicKey);
                    break;
                case 4: send("SEND_MESSAGE", this::sendMessage);
                    break;
                case 5: send("SEND_CHANGE_ENCRYPTION_METHOD", this::changeMessageEncryption);
                    break;
                case 6: System.err.printf("Your private key: %s\n", privateKey);
                    break;
                case 7: System.err.printf("Your public key: %s\n", diffieHellman.calculatePublicKey());
                    break;
                case 8: System.err.printf("Shared secret key: %s\n", diffieHellman.calculateSharedSecretKey());
                    break;
                case 0: running = false;
                    client.stop();
                    reader.close();
                    break;
                default: System.err.println("Invalid option");
                    break;
            }
        }
    }

    private void registerReceiveBroadcast() {
        this.client.registerMethod("BROADCAST", (msg, socket) -> {
            Packet packet = exchangePacketProvider.fromSecureJson((String) msg.get(1));
            System.err.printf(">> Received broadcast message from user %s: '%s'\n", packet.getId(), packet.getMessage());
        });
    }

    private void request(String methodName, Consumer<Packet> packetConsumer) {
        String response = (String) client.sendMessage(methodName).get(1);
        Packet packet = exchangePacketProvider.fromSecureJson(response);
        packetConsumer.accept(packet);
    }

    private void requestPG(Packet packet) {
        System.err.printf(">> Received P=%s, G=%s\n", packet.getP(), packet.getG());
        this.diffieHellman = new DiffieHellman(packet.getP(), packet.getG(), privateKey);
    }

    private void requestPublicKey(Packet packet) {
        System.err.printf(">> Received server's public key=%s\n", packet.getPublicKey());
        this.diffieHellman.setOthersPublicKey(packet.getPublicKey());
    }

    private void send(String methodName, Supplier<Packet> packetSupplier) {
        Packet packet = packetSupplier.get();
        String response = (String) client.sendMessage(methodName, exchangePacketProvider.toSecureJson(packet)).get(1);
        Packet receivedPacket = exchangePacketProvider.fromSecureJson(response);
        System.err.printf(">> %s\n", receivedPacket.getMessage());
    }

    private Packet sendYourPublicKey() {
        if (diffieHellman == null) {
            throw new IllegalStateException("!! You haven't requested server's public keys !!");
        }
        exchangePacketProvider.setSecretKey(this.diffieHellman.calculateSharedSecretKey());
        return new PacketBuilder(encryptionName)
                .setId(Client.DEFAULT_USER_ID)
                .setPublicKey(diffieHellman.calculatePublicKey())
                .createExchangePacket();
    }

    private Packet sendMessage() {
        System.out.print("Type in your message: ");
        reader.nextLine();
        String message = reader.nextLine();
        return new PacketBuilder(encryptionName)
                .setId(Client.DEFAULT_USER_ID)
                .setMessage(message)
                .createExchangePacket();
    }

    private Packet changeMessageEncryption() {
        System.out.print("Choose new encryption method: ");
        this.encryptionName = reader.next();
        return new PacketBuilder(encryptionName)
                .setId(Client.DEFAULT_USER_ID)
                .createExchangePacket();
    }
}
