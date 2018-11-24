# [rozwal.to](stary.rozwal.to)




## [Crypto](https://stary.rozwal.to/zadanie/15)



### [Bob uwielbia xorować](http://training.securitum.com/rozwal/abc/6.php)

[Rozwiązanie](crypt/bob.py): _SingleXorByteCipher_

**Metoda**: 

1. Rozkodować podany tekst w [base64][1].

2. Wiadomo, że tekst jest zaszyfrowany jednym bajtem.
   
   [Metodą siłową][2] przetestować wszystkie bajty (0–255).
   Można jednocześnie badać czy któreś z rozszyfrowanych wersji jest alfanumeryczne 
   i zawiera w sobie ciąg "ROZWAL_{". 



### [Alice też xoruje](http://training.securitum.com/rozwal/abc/7.php)

[Rozwiązanie](crypt/alice.py): _AliceIsImpressed_

**Metoda**:

1. Znane są właściwości funkcji xor:
   ```
    x ^ 0 = x
    x ^ x = 0
   ```
   
2. Zatem:
   ```
    a ^ b = c
    a ^ b ^ a = c ^ a
    b ^ 0 = c ^ a
    b = c ^ a
    
    
    s - bajty tekstu
    k - bajty klucza
    t = "ROZWAL_{"
    
    s0 ^ R = k0
    s1 ^ O = k1
    s2 ^ Z = k2
    s3 ^ W = k3
    s4 ^ A = k4
    s5 ^ L = k5
    s6 ^ _ = k6
    s7 ^ { = k7
   ```

   Znając fragment tekstu jakim jest "ROZWAL_{" (i wiedząc, że długość klucza < 8)
   można xorować kolejne bajty tekstu przez "ROZWAL_{".

3. W pętli od początku tekstu do jego końca minus długość ciągu "ROZWAL_{" wykonywać
   operację XOR dla każdego z ośmiu znaków. Następnie trzeba się przesunąć w tekście
   o znak dalej i powtarzać do skutku.
   
4. Wśród odszyfrowanych ośmioznakowych fragmentów poszukać jakiegoś (klucza), który ma sens.

5. Przexorować cały tekst znalezionym kluczem. 



### [Cweyk funcbjqlsiluqe](http://training.securitum.com/rozwal/crypto/3.php)

[Rozwiązanie](crypt/english.py): _SubStitutionCipherIsWeak_

**Metoda**: 

1. Znaleźć [częstość występowania liter w języku angielskim][5]

2. Kilka z podstawień mamy za darmo: 'KUWQJG' = 'ROZWAL'.

3. Wzorując się na znajomości angielskiego oraz porównując częstość występowania liter
   zdefiniować słownik z postawieniami.
   
4. Podmienić litery w tekście.



### [Znajdź kolizję](http://training.securitum.com/rozwal/crypto/4.php)

[Rozwiązanie](http://training.securitum.com/rozwal/crypto/4.php?s1[]=znalazlam&s2[]=kolizje): _ItsYourBirthday!_

**Metoda**:

1. Odczytać komentarz w źródle strony.

    ```php
    function h($s) {
      $s = md5('Sx12s;!,.alxMausA_!s'.$s);
      $s = substr($s, 0, 16); // 64 bity
      return $s;
    }
    
    if(isset($_GET['s1']) && isset($_GET['s2'])) {
      $s1 = $_GET['s1'];
      $s2 = $_GET['s2'];
    
      if($s1 !== $s2) {
        echo h($s1) ." - ". h($s2)."<br/>";
        if(h($s1) === h($s2)) {
          echo "Flaga: ".$flaga;
        } else {
          echo "Hashe sie roznia<br/>";
        }
      } else {
        echo "Teksty sa identyczne<br/>";
      }
    }
    ```

   Wynika z niego, że w parametrach zapytania GET można przesłać zmienne `s1` i `s2`.
   Flaga jest wypisywna, kiedy hashe tych dwóch zmiennych skonkatenowane z ciągiem
   'Sx12s;!,.alxMausA_!s' są identyczne, przy czym same zmienne muszą być różne. 
   
   Funkcją hashującą jest [MD5][4], a dokładniej jego pierwsze 16 znaków (64 bity). 

2. Wykorzystać słabość języka PHP, który przy konkatenacji stringa z **tablicą**
   rzutuje tablię na 'Array'. Porównanie zmiennych `$s1 !== $s2` wykaże fałsz,
   bo tablice mają różną zawartość, a powstały hash będzie taki sam przez rzutowanie.
   
3. Przekazać w parametrach zapytania tablice `s1[]` i `s2[]` z dowolnym przypisaniem wartości.

   np. http://training.securitum.com/rozwal/crypto/4.php?s1[]=znalazlam&s2[]=kolizje




## [Gimnazjum SQL](https://stary.rozwal.to/zadanie/13)



### [Nie takie trudne](http://training.securitum.com/rozwal/gim/gimnazjum2/)

[Rozwiązanie](http://training.securitum.com/rozwal/gim/gimnazjum2/?id=3): _ZjazdGimboli_

**Metoda**:

1. Na stronie dany jest fragment kodu PHP:

   ```php
   query('SELECT name FROM flags WHERE id='.$_GET['id'].' LIMIT 1'); 
   while($r=$res->fetchArray()) { 
       echo $r['name']; 
   }
   ```
   Do bazy danych [SQLite][3] wysyłane jest zapytanie o flagę o podanym id,
   następnie wynik jest wyświetlany na stronie.
    
   Zmienna `id` jest pobierana z parametru zapytania GET do serwera.

2. Metodą siłową próbować podać w parametrze zapytania GET kolejne id:
   
   [http://training.securitum.com/rozwal/gim/gimnazjum2/?id=0](http://training.securitum.com/rozwal/gim/gimnazjum2/?id=0)
   
   ...
   
   Aż do skutku:
   
   [http://training.securitum.com/rozwal/gim/gimnazjum2/?id=3](http://training.securitum.com/rozwal/gim/gimnazjum2/?id=3)
   
 



[1]: https://en.wikipedia.org/wiki/Base64
[2]: https://en.wikipedia.org/wiki/Brute-force_attack
[3]: www.sqlite.org/src/
[4]: https://en.wikipedia.org/wiki/MD5
[5]: https://en.wikipedia.org/wiki/Letter_frequency
