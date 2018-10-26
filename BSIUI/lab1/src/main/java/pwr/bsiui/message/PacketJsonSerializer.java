package pwr.bsiui.message;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pwr.bsiui.message.model.Packet;

import java.io.IOException;

/**
 * @author <a href="mailto:226154@student.pwr.edu.pl">Hanna Grodzicka</a>
 */
class PacketJsonSerializer {

    private static final Logger LOG = LoggerFactory.getLogger(PacketJsonSerializer.class);

    private ObjectMapper mapper;

    PacketJsonSerializer() {
        mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
    }

    String toJson(Packet packet) {
        try {
            String json = mapper.writeValueAsString(packet);
            LOG.info("Converting to: {}", json);
            return json;
        } catch (IOException e) {
            LOG.error("Couldn't serialize packet: {}", e);
        }
        return "";
    }

    Packet fromJson(String json) {
        LOG.info("Converting from: {}", json);
        try {
            return mapper.readValue(json, Packet.class);
        } catch (IOException e) {
            LOG.error("Couldn't deserialize packet: {}", e);
        }
        return null;
    }
}
