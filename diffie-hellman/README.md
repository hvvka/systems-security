# Diffie-Hellman communicator


## Spis treści
1. [Cel](#cel)
2. [Algorytm Diffiego-Hellmana](#algorytm-diffiego-hellmana)
3. [Wymagania systemowe](#wymagania-systemowe)
4. [Sposób uruchomienia i kompilacji](#sposób-uruchomienia-i-kompilacji)
5. [Dokumentacja użytkowa](#dokumentacja-użytkowa)
6. [Projekt bazowy](#projekt-bazowy)
7. [Wnioski](#wnioski)



## Cel

Projekt powstał w celach edukacyjnych, aby wdrożyć i zademonstrować działanie protokołu [Diffiego-Hellmana][Diffie–Hellman].

Protokół wykorzystywany jest do komunikacji w modelu klient-serwer.
Wymiana wiadomości odbywa się za pomocą formatu danych JSON (_JavaScript Object Notation_).  

Treść przesyłanych wiadomości jest szyfrowana przy pomocy jednego z trzech sposobów:
1. _none_ – brak szyfrowania (domyślne).
2. _xor_ – szyfrowanie xor jednobajtowe (najmłodszym bajtem sekretu).
3. _ceasar_ – [szyfr cezara][caesar].

Po zaszyfrowaniu, treść wiadomości jest kodowana przez [base64][].

Sposób szyfrowania wybierany jest przez klienta.


## Algorytm Diffiego-Hellmana

### Pojęcia

_P, G_ – klucze publiczne, z których _P_ jest liczbą pierwszą, zaś _G_ [pierwiastkiem pierwotnym][primitive root] modulo _P_.

_A_ – klucz tajny serwera.

_B_ – klucz tajny użytkownika.

_S_ – współdzielona (przez serwer i użytkownika) tajna liczba, klucz sekretu/sesji. 

### Opis wymiany 

1. Klient prosi serwer o klucze _P_ i _G_.
2. Serwer wysyła _P_ i _G_ użytkownikowi.
3. W dowolnej kolejności zachodzą akcje:
   - Serwer wysyła klucz sesji (_S_) wyliczony przez działanie: `G^A mod P`.
   - Klient wysyła klucz sesji (_S_) wyliczony przez działanie: `G^B mod P`. 



## Wymagania systemowe

- Java 8

   **Uzasadnienie**: zostały wykorzystane:
      
   - interfejsy funkcyjne (adnotacja `@FunctionalInterface` dla interfejsu `MessageEncoder` używana w metodach z 
   [SecurePacketProvider](src/main/java/pwr/bsiui/message/SecurePacketProvider.java) oraz 
   [Executable](src/main/java/com/blogspot/debukkitsblog/net/Executable.java), 
   interfejsy `Supplier<T>`, `Function<T, R>` w 
   [SecureServer](src/main/java/pwr/bsiui/net/SecureServer.java),
   [Messages](src/main/java/pwr/bsiui/net/client/messages/Messages.java), 
   [Requests](src/main/java/pwr/bsiui/net/client/messages/Requests.java)),
   - wyrażenia lambda (klasy: 
   [SecurePacketProvider](src/main/java/pwr/bsiui/message/SecurePacketProvider.java), 
   [ClientLoop](src/main/java/pwr/bsiui/net/ClientLoop.java), 
   [SecureServer](src/main/java/pwr/bsiui/net/SecureServer.java), 
   [Messages](src/main/java/pwr/bsiui/net/client/messages/Messages.java), 
   [Client](src/main/java/com/blogspot/debukkitsblog/net/Client.java), 
   [Server](src/main/java/com/blogspot/debukkitsblog/net/Server.java)),
   - _Java Stream API_ dla operacji na kolekcjach (np. metoda `forEach` z interfejsu `java.lang.Iterable` czy `filter`
   użyte w klasach 
   [ClientLoop](src/main/java/pwr/bsiui/net/ClientLoop.java) oraz 
   [SecureServer](src/main/java/pwr/bsiui/net/SecureServer.java)),
   - klasa Optional (
   [SecureServer](src/main/java/pwr/bsiui/net/SecureServer.java)),
   - przekazywanie referencji do metod (
   [Messages](src/main/java/pwr/bsiui/net/client/messages/Messages.java), 
   [Requests](src/main/java/pwr/bsiui/net/client/messages/Requests.java),
   [SecureServer](src/main/java/pwr/bsiui/net/SecureServer.java)).



## Sposób uruchomienia i kompilacji

### Uruchomienie archiwum JAR

Aby skorzystać z komunikatora najpierw należy uruchomić instancję serwera:

```bash
$ java -jar server.jar
```

Następnie można uruchomić dowolną liczbę instancji klienta:

```bash
$ java -jar client.jar
```

### Kompilacja archiwum

Do skompilowania projektu należy użyć zadań zdefiniowanych w `build.gradle` i dołączonego skryptu Gradle Wrapper:

```bash
$ ./gradlew buildServerJar
$ ./gradlew buildClientJar
```



## Dokumentacja użytkowa

### Struktura JSONa

Wszystkie z właściwości są opcjonalne.

Przykładowy JSON z wszystkimi wartościami wypełnionymi:

```json
{
    "p": "23",
    "g": "5",
    "id": "e761c267-f6a9-41ed-b2c5-ff6a32d8f4a1",
    "message": "{ZWVRAEZ@ZG\Az[\CV^Javrw~vz__QV[V_CUF_G\J\F",
    "publicKey": 1230,
    "encryption": "xor" 
}
```

W strukturze można przesłać klucze publiczne _P_ i _G_.

Gdy adresatem wiadomości jest użytkownik, to powinien podpisać ją przez uzupełnienie _id_.

Klucz publiczny jest wyliczany przez instancję klasy [DiffieHellman](src/main/java/pwr/bsiui/message/DiffieHellman.java), a 
następnie przesłany przy nawiązywaniu sesji z serwerem, w celu wyznaczenia klucza sekretu.


### Tworzenie i odbieranie pakietu

Służą do tego dwie specjalizowane klasy:

- `PacketBuilder` – budowniczy wiadomości. Korzysta z modelu `Packet`.
   
   Przy użyciu domyślnego konstruktora, szyfrowanie wiadomości zostaje
   ustawione na "none". W parametryzowanym konstruktorze można podać inne ciągi znaków, które są przypisane 
   do danego szyfrowania. Czyli dla aplikacji są to także "caesar" i "xor". Oprócz tego można ustawić wszystkie zmienne, 
   wymienione w JSONie z poprzedniej sekcji.
   
- `ExchangePacketProvider` – dostarcza dwie metody o sygnaturach: `toSecureJson(pakiet: Packet): String` 
   oraz `fromSecureJson(json: string): Packet`.
   
   Metody pozwalają odpowiednio: utworzyć JSONa z pakietu wcześniej stworzonego przez budowniczego,
   przekonwertować JSONa na model używany w aplikacji, `Packet`.
   
   Klasa została zaprojektowana tak, aby wewnątrz dokonywać wszelkich szyfrowań i kodowań wiadomości.

Przykłady użycia obu klas można znaleźć w klasie testowej [ExchangePacketProviderTest](src/test/java/pwr/bsiui/message/ExchangePacketProviderTest.java).



### Dodawanie nowych sposobów szyfrowania

W pakiecie [pwr.bsiui.message.encryption](src/main/java/pwr/bsiui/message/encryption) zdefiniowane są 3 sposoby szyfrowania. 
Dodanie kolejnego szyfrowania jest równoznaczne ze stworzeniem klasy implementującej interfejs 
[Encryption](src/main/java/pwr/bsiui/message/encryption/Encryption.java) z tego pakietu.

Następnie należy zmodyfikować klasę [EncryptionFactory](src/main/java/pwr/bsiui/message/encryption/EncryptionFactory.java), 
aby udostępniała nowy sposób szyfrowania.



## Projekt bazowy

Aby zapewnić architekturę klient-serwer, częściowo skorzystano z projektu [SimpleServerClient][]
udostępnionego przez serwis [GitHub][] na zasadach licencji [GNU General Public License v3.0][GPL-3.0].

Część projektu, z którego skorzystano, została nieznacznie zmodyfikowana przez np.
wydzielenie metod dla dłuższych fragmentów kodu, zamknięcie strumieni I/O, dodanie loggerów, 
zmianę niektórych modyfikatorów dostępu i formatowanie kodu. 

Kod projektu znajduje się w pakiecie [com.blogspot.debukkitsblog.net](src/main/java/com/blogspot/debukkitsblog/net).



## Wnioski

W projekcie znajdują się komentarze [Javadoc][] dla istotnych części kodu, które mogą być niejasne.
Zbyt wiele komentarzy mogłoby zaciemnić zrozumienie kodu (tworzyć redundancję), 
który został napisany w sposób samo-komentujący.

Implementację przyspieszyło napisanie testów jednostkowych potwierdzające poprawne działanie kluczowych fragmentów  
tj. sposobów szyfrowania, algorytmu Diffiego–Hellmana, tworzenia pakietu do wymiany czy JSONa.

Testy zostały napisane z wykorzystaniem szkieletów aplikacyjnych [JUnit][] i [Spock][].


----------

[Diffie–Hellman]: https://en.wikipedia.org/wiki/Diffie–Hellman_key_exchange
[caesar]: https://en.wikipedia.org/wiki/Caesar_cipher
[base64]: https://en.wikipedia.org/wiki/Base64
[primitive root]: https://en.wikipedia.org/wiki/Primitive_root_modulo_n
[SimpleServerClient]: https://github.com/DeBukkIt/SimpleServerClient
[GitHub]: https://github.com
[GPL-3.0]: https://github.com/DeBukkIt/SimpleServerClient/blob/master/LICENSE
[Javadoc]: https://en.wikipedia.org/wiki/Javadoc
[JUnit]: https://en.wikipedia.org/wiki/JUnit
[Spock]: https://en.wikipedia.org/wiki/Spock_(testing_framework)
