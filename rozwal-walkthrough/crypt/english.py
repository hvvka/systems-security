import logging
from collections import Counter

LOG = logging.getLogger(__name__)


class English:

    def __init__(self):
        self.rozwal = 'ROZWAL_{'
        self.text = '''
Jiubdsk uis vub tjpvdb bunje, lb'c jgg uosk bds fjfskc."Bssijvsk
Jkkscbsn li Tumfpbsk Tklms Ctjinjg", "Djtrsk Jkkscbsn jybsk Zjir Bjmfskliv"...
Njmi rlnc.Bdse'ks jgg jglrs.

Zpb nln eup, li eupk bdkss-flsts fcetduguve jin 1950'c bstdiuzkjli,
sosk bjrs j guur zsdlin bds sesc uy bds djtrsk?Nln eup sosk quinsk qdjb
mjns dlm bltr, qdjb yuktsc cdjfsn dlm, qdjb mje djos mugnsn dlm?
L jm j djtrsk, sibsk me qukgn...
Mlis lc j qukgn bdjb zsvlic qlbd ctduug... L'm cmjkbsk bdji mucb uy
bds ubdsk rlnc, bdlc tkjf bdse bsjtd pc zuksc ms...
Njmi pinskjtdlsosk.Bdse'ks jgg jglrs.

L'm li xpiluk dlvd uk dlvd ctduug.L'os glcbsisn bu bsjtdskc safgjli
yuk bds ylybssibd blms duq bu ksnpts j ykjtblui.L pinskcbjin lb."Iu, Mc.
Cmlbd, L nlni'b cduq me qukr.L nln lb li me dsjn..."
Njmi rln.Fkuzjzge tuflsn lb.Bdse'ks jgg jglrs.

L mjns j nlctuoske bunje.L yupin j tumfpbsk.Qjlb j cstuin, bdlc lc
tuug.Lb nusc qdjb L qjib lb bu.Ly lb mjrsc j mlcbjrs, lb'c zstjpcs L
ctksqsn lb pf.Iub zstjpcs lb nusci'b glrs ms...
Uk yssgc bdksjbsisn ze ms...
Uk bdlirc L'm j cmjkb jcc...
Uk nusci'b glrs bsjtdliv jin cdupgni'b zs dsks...
Njmi rln.Jgg ds nusc lc fgje vjmsc.Bdse'ks jgg jglrs.

Jin bdsi lb djffsisn... j nuuk ufsisn bu j qukgn... kpcdliv bdkupvd
bds fduis glis glrs dskuli bdkupvd ji jnnltb'c oslic, ji sgstbkuilt fpgcs lc
csib upb, j ksypvs ykum bds nje-bu-nje litumfsbsitlsc lc cupvdb... j zujkn lc
yupin.
"Bdlc lc lb... bdlc lc qdsks L zsguiv..."
L riuq soskeuis dsks... sosi ly L'os isosk msb bdsm, isosk bjgrsn bu
bdsm, mje isosk dsjk ykum bdsm jvjli... L riuq eup jgg...
Njmi rln.Beliv pf bds fduis glis jvjli.Bdse'ks jgg jglrs...

Eup zsb eupk jcc qs'ks jgg jglrs... qs'os zssi cfuui-ysn zjze yuun jb
ctduug qdsi qs dpivsksn yuk cbsjr... bds zlbc uy msjb bdjb eup nln gsb cglf
bdkupvd qsks fks-tdsqsn jin bjcbsgscc.Qs'os zssi numlijbsn ze cjnlcbc, uk
lviuksn ze bds jfjbdsblt.Bds ysq bdjb KUWQJG_{CpzCblbpbluiTlfdskLcQsjr} djn cumsbdliv bu bsjtd yupin pc qlgg-
liv fpflgc, zpb bducs ysq jks glrs nkufc uy qjbsk li bds nscskb.

Bdlc lc upk qukgn iuq... bds qukgn uy bds sgstbkui jin bds cqlbtd, bds
zsjpbe uy bds zjpn.Qs mjrs pcs uy j cskolts jgksjne salcbliv qlbdupb fjeliv
yuk qdjb tupgn zs nlkb-tdsjf ly lb qjci'b kpi ze fkuylbsskliv vgpbbuic, jin
eup tjgg pc tklmlijgc.Qs safguks... jin eup tjgg pc tklmlijgc.Qs cssr
jybsk riuqgsnvs... jin eup tjgg pc tklmlijgc.Qs salcb qlbdupb crli tuguk,
qlbdupb ijbluijglbe, qlbdupb ksglvlupc zljc... jin eup tjgg pc tklmlijgc.
Eup zplgn jbumlt zumzc, eup qjvs qjkc, eup mpknsk, tdsjb, jin gls bu pc
jin bke bu mjrs pc zsglsos lb'c yuk upk uqi vuun, esb qs'ks bds tklmlijgc.

Esc, L jm j tklmlijg.Me tklms lc bdjb uy tpkluclbe.Me tklms lc
bdjb uy xpnvliv fsufgs ze qdjb bdse cje jin bdlir, iub qdjb bdse guur glrs.
Me tklms lc bdjb uy upbcmjkbliv eup, cumsbdliv bdjb eup qlgg isosk yukvlos ms
yuk.

L jm j djtrsk, jin bdlc lc me mjilyscbu.Eup mje cbuf bdlc linlolnpjg,
zpb eup tji'b cbuf pc jgg... jybsk jgg, qs'ks jgg jglrs.
        '''
        self.letter_frequency = ['e', 't', 'a', 'o', 'i', 'n', 's', 'h', 'r', 'w', 'd', 'l', 'y', 'k', 'c', 'u', 'm',
                                 'f',
                                 'g', 'p', 'b', 'v', 'j', 'x', 'q', 'z']
        self.first_letter_frequency = ['t', 'a', 'o', 'i', 's', 'w', 'c', 'b', 'p', 'h', 'f', 'm', 'd', 'r', 'e', 'k',
                                       'l', 'n', 'g', 'u', 'v', 'y', 'j', 'g', 'x', 'z']

    def __repr__(self) -> str:
        return 'ROZWAL_{SubStitutionCipherIsWeak}'

    def __str__(self):
        LOG.info('Rozkodowany szyfr podstawieniowy: ' + self.solve())
        LOG.info('Flaga: ' + self.__repr__())

    def solve(self) -> str:
        self.print_common_letters()

        # 'KUWQJG' = 'ROZWAL'
        dictionary = {
            'j': 'a',
            'z': 'b',
            't': 'c',
            'n': 'd',
            's': 'e',
            'y': 'f',
            'v': 'g',
            'd': 'h',
            'l': 'i',
            'x': 'j',
            'r': 'k',
            'g': 'l',
            'i': 'n',
            'u': 'o',
            'f': 'p',
            'k': 'r',
            'c': 's',
            'b': 't',
            'p': 'u',
            'o': 'v',
            'q': 'w',
            'a': 'x',
            'e': 'y',
            'w': 'z',
        }

        decrypted: str = ''

        for c in self.text:
            if c.lower() in dictionary:
                replacement: str = dictionary[c.lower()]
                decrypted += replacement if c.islower() else replacement.upper()
            else:
                decrypted += c

        return decrypted

    def print_common_letters(self):
        letter_counter: Counter = self.count_letters(self.text)
        ignore = ['k', 'u', 'w', 'q', 'j', 'g']
        for word in list(letter_counter):
            if word in ignore:
                del letter_counter[word]
        common_letters = letter_counter.most_common()
        LOG.debug(common_letters)

    @staticmethod
    def count_letters(text: str) -> Counter:
        return Counter(c for c in text.lower() if c.isalpha())
