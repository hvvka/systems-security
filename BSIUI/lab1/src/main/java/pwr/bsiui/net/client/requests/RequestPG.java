package pwr.bsiui.net.client.requests;

import pwr.bsiui.message.DiffieHellman;
import pwr.bsiui.message.model.Packet;

import java.math.BigInteger;

/**
 * Sends request for P and G server's keys.
 *
 * @author <a href="mailto:226154@student.pwr.edu.pl">Hanna Grodzicka</a>
 */
public class RequestPG implements Request {

    private final BigInteger privateKey;

    private DiffieHellman diffieHellman;

    RequestPG(BigInteger privateKey) {
        this.privateKey = privateKey;
    }

    @Override
    public void createRequest(Packet packet) {
        System.err.printf(">> Received P=%s, G=%s%n", packet.getP(), packet.getG());
        this.diffieHellman = new DiffieHellman(packet.getP(), packet.getG(), privateKey);
    }

    @Override
    public String getMethodName() {
        return "REQUEST_P_G";
    }

    @Override
    public String getInfo() {
        return "Request [server] public keys (P and G)";
    }

    DiffieHellman getDiffieHellman() {
        return diffieHellman;
    }
}
