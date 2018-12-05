# Keygen



## Spis treści
1. [Cel](#cel)
2. [Wymagania systemowe](#wymagania-systemowe)
3. [Analiza program 8](#analiza-programu-8)
4. [Keygen](#keygen)
5. [Podsumowanie](#podsumowanie)



## Cel
W ramach zajęć należało przenalizować działanie programu [8](./src/8), a następnie napisać osobny program, który będzie generatorem haseł dla niego. W tym celu należało podjąć kolejne kroki:

1. Deasemblacja programu [8](./src/8) przy pomocy `objdump`.

2. Analiza jego działania, w tym znalezienie prawidłowego hasła dla własnego numeru indeksu.

3. Napisanie programu `keygen`. Powinien on przyjmować indeks i wyświetlać dla niego prawidłowe rozwiązanie. `Keygen` nie powinien modyfikować pliku [8](./src/8), ani z niego korzystać. Jest to inżynieria wsteczna algorytmu kodowania hasła z pierwszego programu.



## Wymagania systemowe
W celu odtworzenia środowiska uruchomieniowego konieczne jest wyłącznie oprogramowanie [Docker](https://www.docker.com).

Uruchomienie skryptu:
```bash
$ ./run.sh
```
powoduje zbudowanie obrazu, a następnie kontenera. Obraz bazowy zawiera w sobie system [Ubuntu 14.04](https://hub.docker.com/_/ubuntu) razem z programi potrzebnymi do pracy z językiem assemblera (w tym `gdb`). Ponadto obraz rozszerzono w pliku [Dockerfile](./src/Dockerfile) o biblioteki dla języka C w wersji 32-bitowej.

Domyślnym katalogiem roboczym w kontenerze jest `/workspace`, do którego kopiowana jest cała zawartość folderu [./src](./src)

Po zakończeniu pracy, kontener oraz obraz można usunąć skryptem:
```bash
$ ./clear.sh
```



## Analiza program 8

Na początku programu, w funkcji `main` alokowane są bloki pamięci (funkcja systemowa `calloc`).

Kolejny istotnym fragmentem jest:

```
 8048749: e8 ea fd ff ff        call   8048538 <__isoc99_scanf@plt>
 804874e: e8 65 fd ff ff        call   80484b8 <getchar@plt>
 8048753: 59                    pop    %ecx
 8048754: 58                    pop    %eax
 8048755: 68 c2 9e 04 08        push   $0x8049ec2
 804875a: 6a 01                 push   $0x1
 804875c: e8 77 fd ff ff        call   80484d8 <__printf_chk@plt>
 8048761: 83 c4 0c              add    $0xc,%esp
 8048764: ff 35 40 b0 04 08     pushl  0x804b040
 804876a: 6a 22                 push   $0x22
 804876c: 53                    push   %ebx
 804876d: e8 86 fd ff ff        call   80484f8 <fgets@plt>
 8048772: 83 c4 0c              add    $0xc,%esp
 8048775: c6 43 20 00           movb   $0x0,0x20(%ebx)
 8048779: 6a 20                 push   $0x20
 804877b: 56                    push   %esi
 804877c: ff 75 e4              pushl  -0x1c(%ebp)
 804877f: e8 a0 fe ff ff        call   8048624 <generate_key>
 8048784: 83 c4 0c              add    $0xc,%esp
 8048787: 6a 20                 push   $0x20
 8048789: 56                    push   %esi
 804878a: 53                    push   %ebx
 804878b: e8 c8 fd ff ff        call   8048558 <strncmp@plt>
```

Następuje tu pobranie ciągów znaków od użytkownika, kolejno loginu (numeru indeksu) i hasła, następnie wywoływana jest funkcja `generate_key`, a jej wynik zostaje porównany z otrzymanym hasłem. Jeśli hasła się zgadzają, w dalszej części programu (nieuwzględnionej na listingu) program wywołuje funkcję `winner`, w przeciwnym wypadku - `looser`.

Miejscem wartym postawienia breakpointa jest adres 0x0804878b. Podczas debuggowania programu można było podejrzeć hasło, do którego przyrównywane jest hasło pobrane ze standardowego wejścia.

```
(gdb) b *0x0804878b
Breakpoint 1 at 0x804878b
(gdb) r
Starting program: /workspace/8
  ____   _____ _ _    _ _____
 |  _ \ / ____(_) |  | |_   _|
 | |_) | (___  _| |  | | | |
 |  _ < \___ \| | |  | | | |
 | |_) |____) | | |__| |_| |_
 |____/|_____/|_|\____/|_____|
     *********************************
     *** Authorized Personnel Only ***
     *********************************

=== AUTHORIZATION REQUIRED ===
USER: 226154
KEY: ee

Breakpoint 1, 0x0804878b in main ()
(gdb) x/s $esi
0x804c030:  "jz1ust0obix7vp6ym9kcd4q3egw8an5h"
(gdb) x/s $ebx
0x804c008:  "ee\n"
```

Rejestr ESI zawiera prawidłowe hasło dla indeksu "226154".



Dalszej analizie podlegała funkcja `generate_key`:
```
08048624 <generate_key>:
 8048624: 55                    push   %ebp
 8048625: b9 24 00 00 00        mov    $0x24,%ecx
 804862a: 89 e5                 mov    %esp,%ebp
 804862c: 31 d2                 xor    %edx,%edx
 804862e: 57                    push   %edi
 804862f: 56                    push   %esi
 8048630: 53                    push   %ebx
 8048631: 83 ec 08              sub    $0x8,%esp
 8048634: 8b 5d 08              mov    0x8(%ebp),%ebx
 8048637: 8b 75 0c              mov    0xc(%ebp),%esi
 804863a: 89 d8                 mov    %ebx,%eax
 804863c: f7 f1                 div    %ecx
 804863e: b1 64                 mov    $0x64,%cl
 8048640: 89 d8                 mov    %ebx,%eax
 8048642: 89 d7                 mov    %edx,%edi
 8048644: 31 d2                 xor    %edx,%edx
 8048646: f7 f1                 div    %ecx
 8048648: 30 c9                 xor    %cl,%cl
 804864a: 89 c3                 mov    %eax,%ebx
 804864c: 83 e3 01              and    $0x1,%ebx
 804864f: 89 5d ec              mov    %ebx,-0x14(%ebp)
 8048652: eb 27                 jmp    804867b <generate_key+0x57>
 8048654: 8d 14 39              lea    (%ecx,%edi,1),%edx
 8048657: bb 24 00 00 00        mov    $0x24,%ebx
 804865c: 89 d0                 mov    %edx,%eax
 804865e: 31 d2                 xor    %edx,%edx
 8048660: f7 f3                 div    %ebx
 8048662: 83 7d ec 00           cmpl   $0x0,-0x14(%ebp)
 8048666: 75 09                 jne    8048671 <generate_key+0x4d>
 8048668: b8 23 00 00 00        mov    $0x23,%eax
 804866d: 29 d0                 sub    %edx,%eax
 804866f: 89 c2                 mov    %eax,%edx
 8048671: 8a 82 90 88 04 08     mov    0x8048890(%edx),%al
 8048677: 88 04 0e              mov    %al,(%esi,%ecx,1)
 804867a: 41                    inc    %ecx
 804867b: 3b 4d 10              cmp    0x10(%ebp),%ecx
 804867e: 72 d4                 jb     8048654 <generate_key+0x30>
 8048680: 58                    pop    %eax
 8048681: 5a                    pop    %edx
 8048682: 5b                    pop    %ebx
 8048683: 5e                    pop    %esi
 8048684: 5f                    pop    %edi
 8048685: 5d                    pop    %ebp
 8048686: c3                    ret    
```

Przed wywołaniem tej funkcji, na stos odkładane są trzy argumenty, z których ona korzyta:
- 0x20 – rozmiar bufora,
- 0x0804c030 – adres podanego hasła,
- 0x0003736a – podany indeks zapisany szesnastkowo.

W funkcji `generate_key` zapisana jest pętla, która się wykonuje 32 razy. Licznikiem tej funkcji jest rejestr ECX. Istotne są dwie linjki:

```
8048671: 8a 82 90 88 04 08     mov    0x8048890(%edx),%al
8048677: 88 04 0e              mov    %al,(%esi,%ecx,1)
```

Spod adresu 0x8048890 + przesunięcie zgodne z zawartością rejestru EDX kopiowany jest bajt (znak), a następnie jest on przepisywany pod adres w ESI + licznik.

Zatem w pętli kopiowany jest pewien ciąg znaków spod adresu 0x8048890. Podglądając jego zawartość otrzymano następujący rezultat:

```
(gdb) x/s 0x8048890
0x8048890:  "lrjz1ust0obix7vp6ym9kcd4q3egw8an5h2f"
```

Zauważono pewną zgodność z poprzednio oglądanym hasłem dla indeksu "226154".

Hasło: `jz1ust0obix7vp6ym9kcd4q3egw8an5h`
Ciąg:  `lrjz1ust0obix7vp6ym9kcd4q3egw8an5h2f`

Hasło ma 32 bajty, zaś ciąg 36. Hasło jest kopią ciągu od pewnego jego miejsca. Ponieważ hasło jest inne dla każdego indeksu, to musi mieć on jakiś udział w wyborze miejsca, od którego zaczyna się kopiowanie.

Zamiast dogłębnie analizować zdeasemblowaną funkcję, postawiono na metodę prób i błędów: debuggowanie miejsca przy adresie 0x0804878b (wywołanie funkcji systemowej `strncmp`) sprawdzając zawartość rejestru ESI, gdzie jest odłożone hasło powstałe w wyniku działania funkcji `generate_key` dla różnych indeksów.

```
226000: tsu1zjrlf2h5na8wge3q4dck9my6pv7x
...
226152: lrjz1ust0obix7vp6ym9kcd4q3egw8an
226153: rjz1ust0obix7vp6ym9kcd4q3egw8an5
226154: jz1ust0obix7vp6ym9kcd4q3egw8an5h
226155: z1ust0obix7vp6ym9kcd4q3egw8an5h2
226156: 1ust0obix7vp6ym9kcd4q3egw8an5h2f
...
226164: x7vp6ym9kcd4q3egw8an5h2flrjz1ust
...
226254: u1zjrlf2h5na8wge3q4dck9my6pv7xib
```

Indeksy sąsiadujące z "226154" są przesunięte względnie tylko o jedną pozycję. Przesunięcie względem pierwotnego ciągu równe 0 jest uzyskane dla indeksu "226152". Sprawdzenie dalszych indeksów dało pewność, że do konstrukcji hasła jest wykorzystywany wyłącznie ciąg.

Działanie 226152 modulo 36, czyli długość ciągu, daje 0. Zatem punkt początku kopiowania 32 bajtów da się określić na podstawie działania `indeks % 36`. W przypadku osiągnięcia końcowych znaków ciągu, należy go zapętlić (zacząć od pierwszego jego znaku).

Ponadto, w zależności od tego czy trzecia cyfra od końca indeksu jest parzysta, ciąg spod adresu 0x0804878b może być kopiowany od tyłu.



## Keygen
Program [keygen](./src/keygen) został napisany w języku C i może być skompilowany skryptem [compile.sh](./src/compile.sh). Pozwala on na wygenerowanie hasła dla dowolnego indeksu

```
root@16a5c1c8b60e:/workspace# ./compile.sh
root@16a5c1c8b60e:/workspace# ./keygen
Podaj indeks: 226154
Hasło: jz1ust0obix7vp6ym9kcd4q3egw8an5h
```

Działanie programu zostało przetestowane przez skrypt [bruteforce.sh](./src/bruteforce.sh), który uruchamia program [keygen](./src/keygen.c) dla indeksów z zakresu [000000, 999999], pobiera hasło i uruchamia program [8](./src/8) dla danego indeksu i wygenerowanego hasła. W wyniku działania skryptu nie pojawił się ani jeden błąd.



## Podsumowanie
Zadanie zostało prawie ukończone podczas laboratorium, na którym prawdopodobnie zbyt skupiono się na analizie i próbie odtworzenia poszczególnych operacji w assemblerze, co naturalnie naprowadziło na sposób tworzenia hasła. Najważniejsze było znalezienie miejsce w pamięci ciągu, z którego hasła są czerpane oraz zauważenie, że sprawdzana jest trzecia cyfra indeksu od końca (cyfra jest odkładana na stos i adresowana pośrednio).





<!-- # Keygen

1. Objdump -D 8

Znalezienie w funkcji `main` wywołanie funkcji systemowej `strcmp` (string compare) i zastawienie tam breakpointa.

```
$ gdb 8

(gdb) b *0x0804878b
Breakpoint 1 at 0x804878b

(gdb) r
Starting program: /workspace/8
warning: Error disabling address space randomization: Operation not permitted
  ____   _____ _ _    _ _____
 |  _ \ / ____(_) |  | |_   _|
 | |_) | (___  _| |  | | | |
 |  _ < \___ \| | |  | | | |
 | |_) |____) | | |__| |_| |_
 |____/|_____/|_|\____/|_____|
     *********************************
     *** Authorized Personnel Only ***
     *********************************

=== AUTHORIZATION REQUIRED ===

USER: 226154
```

ECX = 0x24
EDX = 0

-----
EBX = adres hasła
ESI = 




 8048779: 6a 20                 push   $0x20
 804877b: 56                    push   %esi
 804877c: ff 75 e4              pushl  -0x1c(%ebp)
 804877f: e8 a0 fe ff ff        call   8048624 <generate_key>


0x20 # 32 – rozmiar bufora
0x0804c030  # adres hasła
0x00037378  # indeks 226168



indeks / 100
pętla 32 razy !
0 / 36

porównywanie zera z tym co pokazuje pointer

pętla dużo da

8048624:  55                    push   %ebp
 8048625: b9 24 00 00 00        mov    $0x24,%ecx
 804862a: 89 e5                 mov    %esp,%ebp
 804862c: 31 d2                 xor    %edx,%edx
 804862e: 57                    push   %edi
 804862f: 56                    push   %esi
 8048630: 53                    push   %ebx
 8048631: 83 ec 08              sub    $0x8,%esp
 8048634: 8b 5d 08              mov    0x8(%ebp),%ebx
 8048637: 8b 75 0c              mov    0xc(%ebp),%esi
 804863a: 89 d8                 mov    %ebx,%eax
 804863c: f7 f1                 div    %ecx
 804863e: b1 64                 mov    $0x64,%cl
 8048640: 89 d8                 mov    %ebx,%eax
 8048642: 89 d7                 mov    %edx,%edi
 8048644: 31 d2                 xor    %edx,%edx
 8048646: f7 f1                 div    %ecx
 8048648: 30 c9                 xor    %cl,%cl
 804864a: 89 c3                 mov    %eax,%ebx
 804864c: 83 e3 01              and    $0x1,%ebx
 804864f: 89 5d ec              mov    %ebx,-0x14(%ebp)
 8048652: eb 27                 jmp    804867b <generate_key+0x57>
 8048654: 8d 14 39              lea    (%ecx,%edi,1),%edx # edi+ecx, ecx to licznik pętli. edi+1, edi+2, edi+3. Iterowanie po tablicy. Jakiej tablicy i co tam jest?
 8048657: bb 24 00 00 00        mov    $0x24,%ebx
 804865c: 89 d0                 mov    %edx,%eax
 804865e: 31 d2                 xor    %edx,%edx
 8048660: f7 f3                 div    %ebx
 8048662: 83 7d ec 00           cmpl   $0x0,-0x14(%ebp)
 8048666: 75 09                 jne    8048671 <generate_key+0x4d>
 8048668: b8 23 00 00 00        mov    $0x23,%eax
 804866d: 29 d0                 sub    %edx,%eax
 804866f: 89 c2                 mov    %eax,%edx
 8048671: 8a 82 90 88 04 08     mov    0x8048890(%edx),%al
 8048677: 88 04 0e              mov    %al,(%esi,%ecx,1)
 804867a: 41                    inc    %ecx  # inkrementacja licznika
 804867b: 3b 4d 10              cmp    0x10(%ebp),%ecx  # porównanie 0x20 ze stosu z licznikiem (ecx)
 804867e: 72 d4                 jb     8048654 <generate_key+0x30>
 8048680: 58                    pop    %eax
 8048681: 5a                    pop    %edx
 8048682: 5b                    pop    %ebx
 8048683: 5e                    pop    %esi
 8048684: 5f                    pop    %edi
 8048685: 5d                    pop    %ebp
 8048686: c3                    ret    




zawartość spod adresu '0x8048890': '' (string)

Rejestr ESI wskazuje na zaalokowany obszar pamięci (funkcją systemową `calloc`) do którego zostanie zapisane zakodowane hasło.





Calloc rezerwuje pamięć na początku programu. Na początku tablica jest zerowana

Od edx cośtam eax

mov edx do eax 
czy 3. cyfra indeksu jest parzysta/nieparzysta
przez 100 modulo coś



do soboty raport
co tam się dzieje w programie



hasło dla mnie (226154): 'jz1ust0obix7vp6ym9kcd4q3egw8an5h'

 -->