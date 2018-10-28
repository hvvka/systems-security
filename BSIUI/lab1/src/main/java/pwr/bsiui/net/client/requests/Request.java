package pwr.bsiui.net.client.requests;

import pwr.bsiui.message.model.Packet;

/**
 * @author <a href="mailto:226154@student.pwr.edu.pl">Hanna Grodzicka</a>
 */
public interface Request {

    void createRequest(Packet packet);

    String getMethodName();

    String getInfo();
}
