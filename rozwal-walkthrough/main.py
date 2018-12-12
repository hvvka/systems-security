import logging

from crypt.alice import Alice
from crypt.bob import Bob
from crypt.english import English

logging.basicConfig(level=logging.INFO)


def main():
    """
    Solutions printing
    """
    Bob().__str__()
    Alice().__str__()
    English().__str__()


if __name__ == "__main__":
    main()
