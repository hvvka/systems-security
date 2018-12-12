package pwr.bsiui.message

import pwr.bsiui.message.encryption.CaesarEncryption
import pwr.bsiui.message.encryption.Encryption
import pwr.bsiui.message.encryption.NoneEncryption
import pwr.bsiui.message.encryption.XorEncryption
import spock.lang.Specification
import spock.lang.Unroll

/**
 * @author <a href="mailto:226154@student.pwr.edu.pl">Hanna Grodzicka</a>
 */
class SecureMessageSpec extends Specification {

    private SecureMessage secureMessage

    @Unroll("'#message' is encoded to '#expected'")
    def "encode"() {
        given:
        Encryption encryption = encryptionType
        secureMessage = new SecureMessage(encryption)

        when:
        def result = secureMessage.encode(message)

        then:
        expected == result

        where:
        encryptionType         | message || expected
        new NoneEncryption()   | ""      || ""
        new NoneEncryption()   | "abc"   || "YWJj"
        new NoneEncryption()   | "123"   || "MTIz"
        new XorEncryption("3") | "abc"   || "UlFQ"
        new CaesarEncryption() | "abc"   || "bm9w"
    }

    @Unroll("'#message' is decoded to '#expected'")
    def "decode"() {
        given:
        Encryption encryption = encryptionType
        secureMessage = new SecureMessage(encryption)

        when:
        def result = secureMessage.decode(message)

        then:
        expected == result

        where:
        encryptionType         | message || expected
        new NoneEncryption()   | ""      || ""
        new NoneEncryption()   | "YWJj"  || "abc"
        new NoneEncryption()   | "MTIz"  || "123"
        new XorEncryption("3") | "UlFQ"  || "abc"
        new CaesarEncryption() | "bm9w"  || "abc"
    }
}
