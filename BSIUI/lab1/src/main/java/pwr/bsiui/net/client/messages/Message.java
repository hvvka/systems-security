package pwr.bsiui.net.client.messages;

import pwr.bsiui.message.model.Packet;

/**
 * @author <a href="mailto:226154@student.pwr.edu.pl">Hanna Grodzicka</a>
 */
public interface Message {

    Packet createMessage();

    String getMethodName();

    String getInfo();
}
