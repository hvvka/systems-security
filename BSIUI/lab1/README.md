# Diffie-Helmann communicator

ZADANIE NA ZA 2 TYG

Chwilowo użyty projekt, aby zapewnić sobie architekturę klient-serwer: https://github.com/DeBukkIt/SimpleServerClient

Komunikator z wykorzystaniem protokołów [Diffiego-Helmanna][1].
Może działać dowolnie, np. p2p (peer-to-peer), klient-serwer.

p, g – klucze

Ustalić, liczbę (klucz) sesji, tak aby osoba podsłuchująca nie mogła go wyliczyć.

Preferowane klient-serwer.

Serwer 		Klient
A –––––––––– B


### Algorytm

1. Klient prosi serwer o kluce p i g.
2. Serwer wysyła `g^a mod p`
3. Klient wysyła `g^a mod p`

B^A = S
A^B = S
S – klucz sesji

Komunikacja przez JSONa.


### Szyfrowania

Napisać dodatkowo 3 szyfrowania:
1. XOR (najmłodszym bajtem S-a).
2. Cezar.
3. None.

Klient wybiera sposób szyfrowania.


XOR, Cezar i implementacja Diffego-Helmanna muszą być napisane przez nas. Jedynie można wygenerować sobie p i g jakąs libką, ewentualnie zahardkodować w serwerze.

Można skorzystać nawet z biblioteki, która robi gotowy czat.


Bonusowe punkty:
- co jak p i g jest dynamicznie wyznaczane, i klient w czasie komunikacji wysyła do serwera żądanie innego szyfrowania?


### Możliwy problem: JSON i XOR

```json
{
	"msg": "...",
	"from": "John"
}
```

Jakiś znak w `msg` może psuć strukturę JSONa.
Rozwiązanie: kodowanie transportowe. Dla nas: base64.

`msg = base64(encrypt(txt));`


Nasi klienci i serwery powinny być ze sobą kompatybilne, bo stosują ten sam protokół.


----------

[1]: https://en.wikipedia.org/wiki/Diffie–Hellman_key_exchange

