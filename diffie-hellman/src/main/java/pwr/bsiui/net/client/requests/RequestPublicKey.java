package pwr.bsiui.net.client.requests;

import pwr.bsiui.message.DiffieHellman;
import pwr.bsiui.message.model.Packet;

import java.math.BigInteger;

/**
 * Sends request for server's public key.
 *
 * @author <a href="mailto:226154@student.pwr.edu.pl">Hanna Grodzicka</a>
 */
public class RequestPublicKey implements Request {

    private final DiffieHellman diffieHellman;

    RequestPublicKey(DiffieHellman diffieHellman) {
        this.diffieHellman = diffieHellman;
    }

    @Override
    public void createRequest(Packet packet) {
        System.err.printf(">> Received server's public key=%s\n", packet.getPublicKey());
        this.diffieHellman.setOthersPublicKey(packet.getPublicKey());
    }

    @Override
    public String getMethodName() {
        return "REQUEST_KEY";
    }

    @Override
    public String getInfo() {
        return "Request [server] public key";
    }

    BigInteger getSecretKey() {
        return this.diffieHellman.calculateSharedSecretKey();
    }
}
