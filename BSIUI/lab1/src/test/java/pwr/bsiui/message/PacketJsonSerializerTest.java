package pwr.bsiui.message;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import pwr.bsiui.message.model.ExchangePacket;
import pwr.bsiui.message.model.ExchangePacketBuilder;

import static org.junit.Assert.assertEquals;

/**
 * @author <a href="mailto:226154@student.pwr.edu.pl">Hanna Grodzicka</a>
 */
public class PacketJsonSerializerTest {

    private PacketJsonSerializer packetJsonSerializer;

    @Before
    public void setUp() {
        packetJsonSerializer = new PacketJsonSerializer();
    }

    @Test
    public void toJson() throws JSONException {
        // given
        ExchangePacket exchangePacket = new ExchangePacketBuilder()
                .setP(2)
                .setG(1)
                .setMessage("hi")
                .setEncryption("none")
                .createExchangePacket();

        // when
        String json = packetJsonSerializer.toJson(exchangePacket);

        // then
        String expectedJson = "{\"p\":2,\"g\":1,\"message\":\"hi\",\"encryption\":\"none\"}";
        JSONAssert.assertEquals(expectedJson, json, false);
    }

    @Test
    public void toJsonOnEmptyPacket() throws JSONException {
        // given
        ExchangePacket exchangePacket = new ExchangePacketBuilder().createExchangePacket();

        // when
        String json = packetJsonSerializer.toJson(exchangePacket);

        // then
        String expectedJson = "{}";
        JSONAssert.assertEquals(expectedJson, json, false);
    }

    @Test
    public void fromJson() {
        // given
        String json = "{\"p\":2,\"g\":1,\"message\":\"hi\",\"encryption\":\"none\"}";

        // when
        ExchangePacket packet = packetJsonSerializer.fromJson(json);

        // then
        ExchangePacket expectedPacket = new ExchangePacketBuilder()
                .setG(1)
                .setP(2)
                .setEncryption("none")
                .setMessage("hi")
                .createExchangePacket();
        assertEquals(expectedPacket, packet);
    }

    @Test
    public void fromEmptyJson() {
        // given
        String json = "{}";

        // when
        ExchangePacket packet = packetJsonSerializer.fromJson(json);

        // then
        ExchangePacket expectedPacket = new ExchangePacketBuilder().createExchangePacket();
        assertEquals(expectedPacket, packet);
    }
}
