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
public class ExchangePacketProviderTest {

    private ExchangePacketProvider exchangePacketProvider;

    @Before
    public void setUp() {
        this.exchangePacketProvider = new ExchangePacketProvider();
    }

    @Test
    public void toSecureJson() throws JSONException {
        // given
        Packet packet = new PacketBuilder("xor").setMessage("ABC").createExchangePacket();

        // when
        String json = exchangePacketProvider.toSecureJson(packet);

        // then
        String expectedJson = "{\"message\":\"cXJz\",\"encryption\":\"xor\"}";
        JSONAssert.assertEquals(expectedJson, json, true);
    }

    @Test
    public void toSecureJsonWithEmptyMessage() throws JSONException {
        // given
        Packet packet = new PacketBuilder("none").createExchangePacket();

        // when
        String json = exchangePacketProvider.toSecureJson(packet);

        // then
        String expectedJson = "{\"encryption\":\"none\"}";
        JSONAssert.assertEquals(expectedJson, json, true);
    }

    @Test
    public void fromSecureJson() {
        // given
        String json = "{\"message\":\"cXJz\",\"encryption\":\"xor\"}";

        // when
        Packet packet = exchangePacketProvider.fromSecureJson(json);

        // then
        Packet expectedPacket = new PacketBuilder("xor").setMessage("ABC").createExchangePacket();
        assertEquals(expectedPacket, packet);
    }

    @Test
    public void fromSecureJsonWithEmptyMessage() {
        // given
        String json = "{\"encryption\":\"caesar\"}";

        // when
        Packet packet = exchangePacketProvider.fromSecureJson(json);

        // then
        Packet expectedPacket = new PacketBuilder("caesar").createExchangePacket();
        assertEquals(expectedPacket, packet);
    }
}