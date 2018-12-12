import base64
import logging
from typing import Tuple

LOG = logging.getLogger(__name__)


class Alice:

    def __init__(self):
        self.rozwal = 'ROZWAL_{'
        self.text = '''PAADREshCgcRDgZPBAoPSxgGBBEOAR0ABksVEUUeDw4YCksoBlQWAg5PBgoRGBUNAxkEGBUGS
        x8KGkUfDgQHEUVLIgEWAgoDVAoFSw0NBksFBhEGBEsLGBARGBUNSUsKDQ1FBgQVGAwcDk8WHA
        cETwQXEQ4fBgocCgsOAAUCClQADQ4EABwcBQoTCksKGxUOHkVPOQoRDk8eABgRDA4ASx8dGwY
        DDk8EBA8PBhoCHksVEQcSSwEdAEsJFhgKSxEOVBEZHgsaCkVLIB9JSw0DFQIKSxsbRTkkNSMk
        JzQUNQkCCAo9FiIGHwYAGBgKEBg=='''

    def __str__(self):
        alice_solution = self.solve()
        LOG.info('Klucz: ' + alice_solution[0])
        LOG.info('Rozkodowana Alice: ' + alice_solution[1])
        LOG.info('Flaga: ' + self.__repr__())

    def __repr__(self) -> str:
        return 'ROZWAL_{AliceIsImpressed}'

    def solve(self) -> Tuple[str, str]:
        decoded_bytes = base64.b64decode(self.text)
        self.find_key(decoded_bytes)

        key = 'kotek'
        result = []
        for j in range(0, len(decoded_bytes)):
            result.append(decoded_bytes[j] ^ ord(key[j % len(key)]))

        return key, "".join(map(chr, result))

    def find_key(self, decoded_bytes):
        for j in range(0, len(decoded_bytes) - len(self.rozwal)):
            key = []
            for i in range(0, len(self.rozwal)):
                key.append(decoded_bytes[i + j] ^ ord(self.rozwal[i]))
            result = "".join(map(chr, key))
            LOG.debug(str(j) + ' ' + result)
