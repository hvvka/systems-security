package pwr.bsiui.message

import pwr.bsiui.message.model.Packet
import pwr.bsiui.message.model.PacketBuilder
import spock.lang.Specification
import spock.lang.Unroll

/**
 * @author <a href="mailto:226154@student.pwr.edu.pl">Hanna Grodzicka</a>
 */
class SecurePacketProviderSpec extends Specification {

    @Unroll("Packet got #message encrypted to #encryptedMessage")
    def "encryptPacket"() {
        given:
        def packet = new PacketBuilder(encryption)
                .setP(p)
                .setG(g)
                .setId(id)
                .setMessage(message)
                .setPublicKey(publicKey)
                .createExchangePacket()
        def exchangePacket = new SecurePacketProvider()

        when:
        def encryptedPacket = exchangePacket.encryptPacket(packet, secretKey)

        then:
        def expectedPacket = new Packet(p, g, id, encryptedMessage, publicKey, encryption)
        expectedPacket == encryptedPacket

        where:
        p | g | id        | message | publicKey  | encryption | secretKey || encryptedMessage
        1 | 2 | "2f3_4"   | "aaa"   | 123        | "none"     | 543       || "YWFh"
        3 | 4 | "4567sge" | "ABC"   | 45678      | "xor"      | 4356      || "d3R1"
        5 | 6 | "534g23"  | "AZ*b"  | 9101112    | "caesar"   | 0         || "Tk0qbw=="
        7 | 8 | "434_5g"  | "12345" | 1314161718 | "caesar"   | 54178     || "MTIzNDU="
    }

    @Unroll("Packet got #message decrypted to #decryptedMessage")
    def "decryptPacket"() {
        given:
        def packet = new PacketBuilder(encryption)
                .setP(p)
                .setG(g)
                .setId(id)
                .setMessage(message)
                .setPublicKey(publicKey)
                .createExchangePacket()
        def exchangePacket = new SecurePacketProvider()

        when:
        def decryptedPacket = exchangePacket.decryptPacket(packet, secretKey)

        then:
        def expectedPacket = new Packet(p, g, id, decryptedMessage, publicKey, encryption)
        expectedPacket == decryptedPacket

        where:
        p | g | id        | message    | publicKey  | encryption | secretKey || decryptedMessage
        1 | 2 | "2f3_4"   | "YWFh"     | 123        | "none"     | 543       || "aaa"
        3 | 4 | "4567sge" | "d3R1"     | 45678      | "xor"      | 4356      || "ABC"
        5 | 6 | "534g23"  | "Tk0qbw==" | 9101112    | "caesar"   | 0         || "AZ*b"
        7 | 8 | "434_5g"  | "MTIzNDU=" | 1314161718 | "caesar"   | 54178     || "12345"
    }
}
