package pwr.bsiui;

import com.blogspot.debukkitsblog.net.Client;
import com.blogspot.debukkitsblog.net.Datapackage;
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
        Datapackage response = client.sendMessage("REQUEST");

        System.out.println(response);  // REPLY
        System.out.println(response.get(1));  // {"p":2,"g":1}

    }
}
