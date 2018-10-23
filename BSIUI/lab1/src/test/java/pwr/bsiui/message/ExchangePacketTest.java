package pwr.bsiui.message;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import pwr.bsiui.message.model.Packet;
import pwr.bsiui.message.model.PacketBuilder;

import static org.junit.Assert.assertEquals;

/**
 * @author <a href="mailto:226154@student.pwr.edu.pl">Hanna Grodzicka</a>
 */
public class ExchangePacketTest {

    private ExchangePacket exchangePacket;

    @Before
    public void setUp() {
        exchangePacket = new ExchangePacket();
    }

    @Test
    public void toSecureJson() throws JSONException {
        // given
        Packet packet = new PacketBuilder("xor").setMessage("ABC").createExchangePacket();

        // when
        String json = exchangePacket.toSecureJson(packet);

        // then
        String expectedJson = "{\"message\":\"cXJz\",\"encryption\":\"xor\"}";
        JSONAssert.assertEquals(expectedJson, json, true);
    }

    @Test
    public void fromSecureJson() {
        // given
        String json = "{\"message\":\"cXJz\",\"encryption\":\"xor\"}";

        // when
        Packet packet = exchangePacket.fromSecureJson(json);

        // then
        Packet expectedPacket = new PacketBuilder("xor").setMessage("ABC").createExchangePacket();
        assertEquals(expectedPacket, packet);
    }
}