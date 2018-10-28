package pwr.bsiui.net.client.requests;

import pwr.bsiui.message.DiffieHellman;

import java.math.BigInteger;
import java.util.function.Supplier;

/**
 * @author <a href="mailto:226154@student.pwr.edu.pl">Hanna Grodzicka</a>
 */
public class Requests {

    private final BigInteger privateKey;

    private Supplier<DiffieHellman> diffieHellmanSupplier;

    private Supplier<BigInteger> secretKeySupplier;

    public Requests(BigInteger privateKey) {
        this.privateKey = privateKey;
    }

    public Request createRequestPG() {
        RequestPG requestPG = new RequestPG(privateKey);
        this.diffieHellmanSupplier = requestPG::getDiffieHellman;
        return requestPG;
    }

    public Request createRequestKey() {
        RequestPublicKey requestPublicKey = new RequestPublicKey(this.diffieHellmanSupplier.get());
        this.secretKeySupplier = requestPublicKey::getSecretKey;
        return requestPublicKey;
    }

    public DiffieHellman getDiffieHellman() {
        return this.diffieHellmanSupplier.get();
    }

    public BigInteger getSecretKey() {
        return this.secretKeySupplier.get();
    }
}
