package pwr.bsiui.message;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pwr.bsiui.message.model.ExchangePacket;

import java.io.IOException;

/**
 * @author <a href="mailto:226154@student.pwr.edu.pl">Hanna Grodzicka</a>
 */
public class PacketJsonSerializer {

    private static final Logger LOG = LoggerFactory.getLogger(PacketJsonSerializer.class);

    private ObjectMapper mapper;

    public PacketJsonSerializer() {
        mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
    }

    public String toJson(ExchangePacket exchangePacket) {
        try {
            return mapper.writeValueAsString(exchangePacket);
        } catch (IOException e) {
            LOG.error("Couldn't serialize packet: {}", e);
        }
        return "";
    }

    public ExchangePacket fromJson(String json) {
        try {
            return mapper.readValue(json, ExchangePacket.class);
        } catch (IOException e) {
            LOG.error("Couldn't deserialize packet: {}", e);
        }
        return null;
    }
}
