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
public class PacketJsonSerializerTest {

    private PacketJsonSerializer packetJsonSerializer;

    @Before
    public void setUp() {
        packetJsonSerializer = new PacketJsonSerializer();
    }

    @Test
    public void toJson() throws JSONException {
        // given
        Packet packet = new PacketBuilder()
                .setP(2)
                .setG(1)
                .setMessage("hi")
                .setEncryption("none")
                .createExchangePacket();

        // when
        String json = packetJsonSerializer.toJson(packet);

        // then
        String expectedJson = "{\"p\":2,\"g\":1,\"message\":\"hi\",\"encryption\":\"none\"}";
        JSONAssert.assertEquals(expectedJson, json, true);
    }

    @Test
    public void toJsonOnEmptyPacket() throws JSONException {
        // given
        Packet packet = new PacketBuilder()
                .setEncryption("none")
                .createExchangePacket();

        // when
        String json = packetJsonSerializer.toJson(packet);

        // then
        String expectedJson = "{}";
        JSONAssert.assertEquals(expectedJson, json, false);
    }

    @Test
    public void fromJson() {
        // given
        String json = "{\"p\":2,\"g\":1,\"message\":\"hi\",\"encryption\":\"none\"}";

        // when
        Packet packet = packetJsonSerializer.fromJson(json);

        // then
        Packet expectedPacket = new PacketBuilder()
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
        String json = "{\"encryption\":\"none\"}";

        // when
        Packet packet = packetJsonSerializer.fromJson(json);

        // then
        Packet expectedPacket = new PacketBuilder().createExchangePacket();
        assertEquals(expectedPacket, packet);
    }
}
