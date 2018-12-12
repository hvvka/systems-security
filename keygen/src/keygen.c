#include <stdio.h>

int main () {
    
    char* key = "lrjz1ust0obix7vp6ym9kcd4q3egw8an5h2f";
    int password_length = 32;
    int index;

    printf("Podaj indeks: ");
    scanf("%d", &index);
    
    int offset = index % 36;
    int digit = index / 100 % 10;

    char cipher[32];

    for (int i = 0; i < password_length; i++, offset++)
    {
        if (offset >= 36)
        {
            offset = 0;
        }

        if (digit % 2)
        {
            cipher[i] = key[offset];
        } else
        {
            cipher[i] = key[35 - offset];
        }
    }

    printf("Hasło: %.32s\n", cipher);

    return 0;
}
