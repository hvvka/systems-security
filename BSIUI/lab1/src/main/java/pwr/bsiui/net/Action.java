package pwr.bsiui.net;

import pwr.bsiui.message.model.Packet;

/**
 * Set of functional interfaces for registering server and client methods.
 *
 * @author <a href="mailto:226154@student.pwr.edu.pl">Hanna Grodzicka</a>
 */
@FunctionalInterface
interface Action {
    Packet perform();
}

@FunctionalInterface
interface ServerResponse {
    Packet perform(Packet packet);
}

@FunctionalInterface
interface ClientRequest {
    void perform(Packet packet);
}
