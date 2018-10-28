package pwr.bsiui.net.client;

import com.blogspot.debukkitsblog.net.Client;
import pwr.bsiui.message.ExchangePacketProvider;
import pwr.bsiui.message.model.Packet;
import pwr.bsiui.net.client.messages.Message;
import pwr.bsiui.net.client.messages.Messages;
import pwr.bsiui.net.client.requests.Request;
import pwr.bsiui.net.client.requests.Requests;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

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
    private final Requests requests;
    private final Messages messages;
    private boolean running;

    public ClientLoop(int port) {
        this.privateKey = BigInteger.valueOf(ThreadLocalRandom.current().nextInt(500));
        this.client = new SecureClient("localhost", port);
        registerReceiveBroadcast();
        this.exchangePacketProvider = new ExchangePacketProvider();
        this.reader = new Scanner(System.in);
        this.running = true;
        this.requests = new Requests(privateKey);
        this.messages = new Messages(reader);
    }

    public void start() {
        while (running) {
            MENU.keySet().stream()
//                    .filter(k -> ((k != 7 && k != 8) || requests.getDiffieHellman() != null))
                    .forEach(k -> System.out.printf("%d. %s%n", k, MENU.get(k)));
            switch (reader.nextInt()) {
                case 1: request(requests.createRequestPG());
                    break;
                case 2: request(requests.createRequestKey());
                    this.exchangePacketProvider.setSecretKey(requests.getSecretKey());
                    break;
                case 3: send(messages.createPublicKey(requests.getDiffieHellman()));
                    break;
                case 4: send(messages.createConcreteMessage());
                    break;
                case 5: send(messages.createEncryptionMethod());
                    break;
                case 6: System.err.printf("Your private key: %s%n", privateKey);
                    break;
                case 7: System.err.printf("Your public key: %s%n", requests.getDiffieHellman().calculatePublicKey());
                    break;
                case 8: System.err.printf("Shared secret key: %s%n", requests.getDiffieHellman().calculateSharedSecretKey());
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
            System.err.printf(">> Received broadcast message from user %s: '%s'%n", packet.getId(), packet.getMessage());
        });
    }

    private void request(Request request) {
        String response = (String) client.sendMessage(request.getMethodName()).get(1);
        Packet packet = exchangePacketProvider.fromSecureJson(response);
        request.createRequest(packet);
    }

    private void send(Message message) {
        Packet packet = message.createMessage();
        String response = (String) client.sendMessage(message.getMethodName(), exchangePacketProvider.toSecureJson(packet)).get(1);
        Packet receivedPacket = exchangePacketProvider.fromSecureJson(response);
        System.err.printf(">> %s%n", receivedPacket.getMessage());
    }
}
