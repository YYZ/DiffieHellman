DiffieHellman
=============

Diffie-Hellman java implementation


In order to perform the Diffie-Hellman key exchange, the program does the following:

Generates a random 1023-bit integer: this will be private key b.
Generate public key B given by gb (mod p)
Calculate the shared key s given by Ab (mod p)
Now that we have the value of the shared key s, we can use this as the key for AES encryption. 
However, this key is too large (1024 bits) to be used directly as the AES key.
Therefore we use SHA-256 to produce a 256-bit digest from the shared key a, giving a 256-bit AES key k. 
We then encrypt a single block containing 128 zero bits using AES in ECB mode with the key k, just to prove correctness.
