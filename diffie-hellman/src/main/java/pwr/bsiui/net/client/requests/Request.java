package pwr.bsiui.net.client.requests;

import pwr.bsiui.message.model.Packet;

/**
 * Interface for defining request types that can be send to server.
 *
 * @author <a href="mailto:226154@student.pwr.edu.pl">Hanna Grodzicka</a>
 */
public interface Request {

    void createRequest(Packet packet);

    String getMethodName();

    String getInfo();
}
