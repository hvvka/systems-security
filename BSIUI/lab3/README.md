# rozwal


[zadanka](stary.rozwal.to)

Crypto

Bob uwielbia xorować



zrobić jeszcze Alicję

wiemy, że:
x ^ 0 = x
x ^ x = 0


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

klucz jest < 8



i sqlowe zrobić


--------------------------------

# Crypto

[Bob](http://training.securitum.com/rozwal/abc/6.php)

[src](crypt/bob.py)


[Alice](http://training.securitum.com/rozwal/abc/7.php)

[src](crypt/alice.py)


# SQL injection

1. [SQL injection 1](http://training.securitum.com/rozwal/gim/gimnazjum1/)

<!-- todo -->

1. [SQL injection 2](http://training.securitum.com/rozwal/gim/gimnazjum2/?id=3)

```php
query('SELECT name FROM flags WHERE id='.$_GET['id'].' LIMIT 1'); 
while($r=$res->fetchArray()) { 
    echo $r['name']; 
}
```

`id` jest odczytywane z kontekstu. Wystarczy je podać w parametrze zapytania (GET).

Metodą prób i błędów (inaczej zwaną _bruteforce_) `id=3`.

> ROZWAL_{ZjazdGimboli}



