# [Gra 1.0](http://uw-team.org/hackme/)


## [Level 1](http://uw-team.org/hackme/level1.htm)

Hasło: `a jednak umiem czytac`

1. Wyświetlić źródło strony.

2. W pliku `level1.htm` wejść w _body > script_ i odczytać hasło.



## [Level 2](http://uw-team.org/hackme/ok_next.htm)

Hasło: `to bylo za proste`

1. Wyświetlić zasoby strony.

2. Znaleźć plik `haselko.js`.

3. Odczytać zmienną `has`.

4. Przepisać wartość `has` do okienka z hasłem.

_Alternatywnie_: przepisać bezpośrednio wartość zmiennej `adresik` tj. `formaster.htm` w adres.



## [Level 3](http://uw-team.org/hackme/formaster.htm)

Hasło: `cdqwenow`

1. Wyświetlić źródło strony.

2. Z pliku `formaster.htm` przetworzyć mentalnie fragment kodu z _head > script_:

    ```js
    var dod='unknow';
    /* ... */
    var literki='abcdefgh';
    var ost='';
    function losuj(){
        ost=literki.substring(2,4)+'qwe'+dod.substring(3,6);
    }
    ```

    ```js
    literki.substring(2,4) = 'cd'
    // 'qwe'
    dod.substring(3,6) = 'now'
    ```

3. Hasłem jest wartość zmiennej `ost`.



## [Level 4](http://uw-team.org/hackme/cdqwenow.htm)

Hasło: `171`

1. Wyświetlić źródło strony.

2. Znaleźć linijkę w _head > script_:
   ```js
   (Math.round(6%2)*(258456/2))+(300/4)*2/3+121;
   ```
   i ją przeliczyć:

   `(Math.round(6%2)*(258456/2)) + (300/4)*2/3 + 121 =
    (0*(258456/2)) + (300/4)*2/3 + 121 =
    0 + 75*2/3 + 121 = 
    50 + 121 = 
    171`

3. Wynik wpisać jako hasło.



## [Level 5](http://uw-team.org/hackme/go171.htm)

1. Wejść w źródło strony i _head > script_ w plik `go171.htm`.

2. Zwrócić uwagę na funkcję `sprawdz()` i linijkę:
   ```js
   ile=((seconds*(seconds-1))/2)*(document.getElementById('pomoc').value%2);
   ```

3. Wykluczyć z obliczeń wartość `document.getElementById('pomoc').value%2`, którą wpisuje użytkownik – powinna być to **dowolna liczba nieparzysta**. Wtedy całość ewaluuje się do 1.

4. Wpisać w pole z etykietą _Cyfra pomocnicza_ dowolną liczbę nieparzystą.

5. Obliczyć miejsca zerowe powstałej funkcji kwadratowej:

   `861 = (seconds * (seconds-1))/2`

   `seconds^2 - seconds - 1722 = 0`

   `Δ = b^2 - 4ac = 1 + 4*1722 = 6889`

   `x_1 = (a-sqrt(Δ))/2 = (1-83)/2 = -41`

   `x_2 = (a+sqrt(Δ))/2 = (1+83)/2 = 42`

6. Dodatnia z wartości (`42`) to wymagana liczba sekund, na którą trzeba poczekać by przejść do następnego poziomu klikając w odpowiedniej chwili przycisk `[wejdz]`.



## [Level 6](http://uw-team.org/hackme/42x.htm)

Hasło: `bxd_ex_ex`

1. Otworzyć źródło strony i rozwinąć _head > script_.

2. Przeanalizować fragment kodu:
   ```js
    var lit='abcdqepolsrc';
    function sprawdz() {
        var licznik=0;
        var hsx='';
        var znak='';
        /* ... */
        for (i=1; i<=5; i+=2) {
            licznik++;
            if ((licznik%2)==0) {
                znak='_';
            } else {
                znak='x';
            }
            hsx+=lit.substring(i,i+1)+znak;
        }
        hsx+=hsx.substring(hsx.length-3,hsx.length);
        /* ... */
    }
   ```

    i | licznik | znak | lit.substring(i,i+1)+znak | hsx
    --|---------|------|---------------------------|----------
    1 | 1       | 'x'  | 'bx'                      | 'bx'
    3 | 2       | '_'  | 'd\_'                     | 'bxd\_'
    5 | 3       | 'x'  | 'ex'                      | 'bxd\_ex'

    `hsx.substring(hsx.length-3,hsx.length) = hsx.substring(3,6) = '\_ex'` 



## [Level 7](http://uw-team.org/hackme/bxd_ex_ex.htm)

Hasło: `kocham cie`

1. Otworzyć źródło strony i rozwinąć _head > script_.

2. Fragment z instrukcjami warunkowymi `if` potraktować jak słownik.

3. Zmienna `wyn` po tłumaczeniu przez słownik powinna mieć wartość `'plxszn_xrv'`.

4. Przeanalizować wartość `'plxszn_xrv'` tłumacząc w drugą stronę niż kodowanie słownika.



## [Level 8](http://uw-team.org/hackme/plxszn_xrv.htm)

Hasło: `qrupjf162`

1. Przeliczyć fragmenty kodu z `plxszn_xrv.htm` (sekcja _head > script:nth-child(1)_) oraz `zsedcx.js`:

    ```js
    ax=eval(2+2*2);
    bx=eval(ax/2);
    cx=eval(ax+bx);
    /* ... */
    wyn=''; 
    alf='qwertyuioplkjhgfdsazxcvbnm';
    qet=0; 
    for (i=0; i<=10; i+=2) {
        /* ... */
        wyn+=alf.charAt(qet+i); 
        qet++;
    }
    wyn+=eval(ax*bx*cx);
    ```

    `ax=6, bx=3, cx=9`

    i | qet | qet+i | alf.charAt(qet+i)
    --|-----|-------|------------------
    0 | 0   | 0     | q
    2 | 1   | 3     | r
    4 | 2   | 6     | u
    6 | 3   | 9     | p
    8 | 4   | 12    | j 
    10| 5   | 15    | f 

    `ax*bx*cx = 162`



-----------------------------------



# [Gra 2.0](http://uw-team.org/hm2)

## [Level 1](http://uw-team.org/hm2/level1.htm)

Hasło: `text`

1. Otworzyć źródło strony i zwrócić uwagę na ukryty element:
   
   `<input value="text" name="formularz" id="formularz" type="hidden">`

2. W konsoli odczytać wartość ukrytego elementu:
   `formularz.value`.


## [Level 2](http://uw-team.org/hm2/text.php)

Hasło: `banalne`

1. Zewaluować z konsoli wyrażenie z _body > script_ ze źródła strony:
    ```js
    unescape('%62%61%6E%61%6C%6E%65')
    ```


## [Level 3](http://uw-team.org/hm2/banalne.php)

Hasło: `1234`

1. Wyświetlić źródło strony i _body > script_.

2. Przeliczyć ciąg binarny `100 1101 0010` na system dziesiętny:
   
   `4*256 + 13*16 + 2*1 = 1024 + 208 + 2 = 1234`


## [Level 4](http://uw-team.org/hm2/1234.php)

Hasło: `102`

1. Wejść w źródło strony.

2. Wybrać zakładkę "Sieć".

3. Kliknąć prawym klawiszem na plik źródłowy `1234.htm`.

4. Kopiuj jako cURL.

5. Usunąć ostatnie dwie linijki ze skopiowanego:
    ```bash
    $ curl 'http://uw-team.org/hm2/1234.htm' \
    -XGET \
    -H 'Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8' \
    -H 'Upgrade-Insecure-Requests: 1' \
    -H 'User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_1) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/12.0.1 Safari/605.1.15'
    ```

6. Wysłać zapytanie w konsoli systemowej.
   
   Odpowiedź:
    ```html
    <html><head><title>Hackme 2.0 - by Unknow</title></head><body text="white" bgcolor="black" link="yellow" vlink="yellow" alink="yellow">
    <script>
    haslo='';
    cos=parseInt(unescape('%32%35%38'));
    while ((haslo!=cos.toString(16)) && (haslo!='X') ){
    haslo=prompt('podaj haslo:\nwpisz X aby zatrzymac skrypt','');
    }
    if (haslo==cos.toString(16)) { self.location=haslo+'.php';} else {self.location='http://www.uw-team.org/';}
    </script>
    <h3>Hackme 2.0 - level #4</h3>
    <a href="hehehe.htm">Kliknij mnie :)</a>
    </body></html>%
    ```
7. Zewaluować fragmenty odebranego pliku źródłowego strony:
   ```js
   cos=parseInt(unescape('%32%35%38'))
   ``` 
   => `cos=258`.

   ```js
   cos.toString(16)
   ```
   => `1*16^2 + 0*16 + 2*1 = 102`

_Alternatywnie_: zamiast kroków 2–6 wystarczy **wyłączyć obsługę JavaScript** w ustawieniach wykorzystywanej przeglądarki.



## [Level 5](http://uw-team.org/hm2/102.php)

1. Ustawiając wartości dla loginu i hasła, są one przesyłane jawnie jako **parametry zapytania** w URLu.

2. Zastosowane w implementacji, na którą nie mamy wpływu, porównanie wartości `login` i `haslo` operatorem `==` nie jest skuteczne dla danych pozyskiwanych od użytkownika.

3. Ustawić zmienne `log` i `has` bezpośrednio w URLu:
`http://uw-team.org/hm2/102.php?log=1&has=1`.



## [Level 6](http://uw-team.org/hm2/url.php)

1. W konsoli otworzyć zakładkę "Pamięć".

2. Wejść w zakładkę "Pliki cookie".

3. Dla ciasteczka `nastepna_strona` przypisana jest wartość `ciastka.htm`, co jest adresem następnego poziomu.



## [Level 7](http://uw-team.org/hm2/ciastka.htm)

1. Wyłączyć obsługę JavaScript w przeglądarce.

2. W źródle strony `http://uw-team.org/hm2/zle.htm` podano wskazówkę, by wykorzystać "pewną własność serwera Apache".

3. Wejść na `http://uw-team.org/hm2/include` i z pliku `cosik.js` wyciągnąć adres kolejnego etapu tj. `listing.php`.

4. Włączyć obsługę JavaScript, aby móc robić kolejne poziomy.


## [Level 8](http://uw-team.org/hm2/listing.php)

Hasło: `kxnxgxnxa`

1. Odczytać element _#ukryte_ wraz z kolejnymi `font` składającymi się w napis "haslem do tego etapu jest slowo kxnxgxnxa".

2. Pokazuje się prośba (alert) o poczekanie do godziny 1, aby dostać się pod odpowiedni adres.

3. [Link](http://uw-team.org/hm2/pokaz.php).
   Wyłączyć i włączyć JavaScript (_na Macu zadziałało_).



## [Level 9](http://uw-team.org/hm2/pokaz.php)

Hasło: `bezkvu6r`

1. Nie analizować rzeczy związanych z szyfrem Cezara, bo nic z nich nie wynika:

   `rot13(cbavmfml nqerf mbfgny mnxbqbjnal m cemrfhavrpvrz b 2) = ponizszy adres zostal zakodowany z przesunieciem o 2`

   `rot2(jvvr://yyy.ukvkpvjgeqtpgt.eqo/dkpcta/dkpcta.rjr) = http://www.sitinthecorner.com/binary/binary.php`

2. Przetłumaczyć podany ciąg binarny na:

   `Gratuluje :) Udalo ci sie rozkodowac ten etapik :] Nie bylo to specjalnie trude... Wystarczylo zrobic sobie program konwertujacy, lub wejsc na www.google.pl i wpisac "text to binary". To byl juz ostatni etap tej gry. Aby byc wpisanym na liste zwyciezcow przeslij haslo "bezkvu6r" na adres unkn0w@wp.pl`

3. Wysłać maila i dowiedzieć się, że adres nie istnieje.
