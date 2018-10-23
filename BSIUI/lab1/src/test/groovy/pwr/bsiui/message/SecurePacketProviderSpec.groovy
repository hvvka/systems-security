package pwr.bsiui.message

import pwr.bsiui.message.model.Packet
import pwr.bsiui.message.model.PacketBuilder
import spock.lang.Specification
import spock.lang.Unroll

/**
 * @author <a href="mailto:226154@student.pwr.edu.pl">Hanna Grodzicka</a>
 */
class SecurePacketProviderSpec extends Specification {

    @Unroll("Packet got #message encoded")
    def "encodePacket"() {
        given:
        def packet = new PacketBuilder(encryption).setP(p).setG(g).setMessage(message).setPublicKey(publicKey).createExchangePacket()
        def exchangePacket = new SecurePacketProvider()

        when:
        def encodedPacket = exchangePacket.encodePacket(packet)

        then:
        def expectedPacket = new Packet(p, g, encodedMessage, publicKey, encryption)
        expectedPacket == encodedPacket

        where:
        p | g | message | publicKey  | encryption || encodedMessage
        1 | 2 | "aaa"   | 123        | "none"     || "YWFh"
        3 | 4 | "ABC"   | 45678      | "xor"      || "cXJz"
        5 | 6 | "AZ*b"  | 9101112    | "caesar"   || "Tk0qbw=="
        7 | 8 | "12345" | 1314161718 | "caesar"   || "MTIzNDU="
    }

    @Unroll("Packet got #message decoded")
    def "decodePacket"() {
        given:
        def packet = new PacketBuilder(encryption).setP(p).setG(g).setMessage(message).setPublicKey(publicKey).createExchangePacket()
        def exchangePacket = new SecurePacketProvider()

        when:
        def decodedPacket = exchangePacket.decodePacket(packet)

        then:
        def expectedPacket = new Packet(p, g, decodedMessage, publicKey, encryption)
        expectedPacket == decodedPacket

        where:
        p | g | message    | publicKey  | encryption || decodedMessage
        1 | 2 | "YWFh"     | 123        | "none"     || "aaa"
        3 | 4 | "cXJz"     | 45678      | "xor"      || "ABC"
        5 | 6 | "Tk0qbw==" | 9101112    | "caesar"   || "AZ*b"
        7 | 8 | "MTIzNDU=" | 1314161718 | "caesar"   || "12345"
    }
}
