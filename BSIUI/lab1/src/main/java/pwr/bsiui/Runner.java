package pwr.bsiui;

import com.blogspot.debukkitsblog.net.Client;
import com.blogspot.debukkitsblog.net.Server;
import pwr.bsiui.client.SecureClient;
import pwr.bsiui.server.SecureServer;

/**
 * @author <a href="mailto:226154@student.pwr.edu.pl">Hanna Grodzicka</a>
 */
public class Runner {

    public static void main(String[] args) {
        System.out.println("Hello Diffie-Hellman chat!");

        Server server = new SecureServer(4001);
        Client client = new SecureClient("localhost", 4001);
        System.out.println(client.sendMessage("REQUEST"));
    }
}
