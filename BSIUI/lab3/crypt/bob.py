import base64
import logging
from typing import Tuple

LOG = logging.getLogger(__name__)


class Bob:

    def __init__(self):
        self.rozwal = 'ROZWAL_{'
        self.text = '''GCg7Ozs7Oy01e3oNMz4gP3ogP3ovPjs2NXoZM3opMz96KDUgKSAjPCg1LTs5ei4/MSkueiA7KSAjPCg1LTs0I3oqNTA/PiM0OSAjN3o4OzAuPzd0ehQ1ej41OCg7dno8Njs9O3ouNWB6CBUADRsWBSEJMzQ9Nj8CNSgYIy4/GTMqMj8oJw==/MSkueiA7KSAjPCg1LTs0I3oqNTA/PiM0OSAjN3o4OzAuPzd0ehQ1ej41OCg7dno8Njs9O3ouNWB6CBUADRsWBSEJMzQ9Nj8CNSgYIy4/GTMqMj8oJw=='''

    def __repr__(self) -> str:
        return 'ROZWAL_{SingleXorByteCipher}'

    def __str__(self):
        bob_solution = self.solve()
        LOG.info('Klucz: ' + bob_solution[0])
        LOG.info('Rozkodowany Bob: ' + bob_solution[1])
        LOG.info('Flaga: ' + self.__repr__())

    def solve(self) -> Tuple[str, str]:
        """
        text -> decode base64 -> xor(decoded_bytes, X)
        :return:
        """
        decoded_bytes = base64.b64decode(self.text)

        for i in range(0, 255):
            password = []
            for j in range(0, len(decoded_bytes)):
                password.append(decoded_bytes[j] ^ i)

            key = str(i)
            result = ''.join(map(chr, password))
            if result.__contains__(self.rozwal):
                return key, result

        raise Exception("Bob not found")
