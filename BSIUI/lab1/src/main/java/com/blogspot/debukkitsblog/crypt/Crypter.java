package com.blogspot.debukkitsblog.crypt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.charset.Charset;
import java.security.Key;

/**
 * Provides methods to encrypt and decrypt Objects
 *
 * @author Leonard Bienbeck
 */
public class Crypter {

    private static final Logger LOG = LoggerFactory.getLogger(Crypter.class);

    private static final String ALGORITHM = "AES";

    /**
     * Encrypts an Object and returns its encrypted equivalent CryptedObject
     *
     * @param o        The object to encrypt
     * @param password The password to use for encryption
     * @return The CryptedObject equivalent of the given object
     */
    public static CryptedObject encrypt(Object o, String password) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutput out = new ObjectOutputStream(bos);
            out.writeObject(o);
            return new CryptedObject(encrypt(bos.toByteArray(), password));
        } catch (Exception e) {
            LOG.error("", e);
        }
    }

    /**
     * Decrypts a CryptedObject using a password and returns its decrypted
     * equivalent Object
     *
     * @param co       The CryptedObject to decrypt
     * @param password The password to use for decryption
     * @return The decrypted object
     * @throws DecryptionFailedException If decryption fails. A wrong password will cause this.
     */
    public static Object decrypt(CryptedObject co, String password) throws DecryptionFailedException {
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(decrypt(co.getBytes(), password));
            ObjectInput in = new ObjectInputStream(bis);
            return in.readObject();
        } catch (Exception e) {
            throw new DecryptionFailedException();
        }
    }

    /**
     * Encrypts an array of bytes using the AES algorithm
     *
     * @param data   The array of bytes to encrypt
     * @param rawKey The password to use for encryption. Warning: This password will be
     *               extended or shortened to match a length of 16 bytes (128 bit).
     *               Therefore be careful! Different passwords, starting with the same
     *               sixteen characters, result in the same AES key.
     * @return An array of bytes containing the encrypted data
     * @throws Exception If something went wrong with the AES algorithm.
     */
    public static byte[] encrypt(byte[] data, String rawKey) throws Exception {
        Key key = generateKey(rawKey);
        Cipher c = Cipher.getInstance(ALGORITHM);
        c.init(Cipher.ENCRYPT_MODE, key);
        return c.doFinal(data);
    }

    /**
     * Decrypts an array of bytes using the AES algorithm
     *
     * @param encryptedData The array of bytes to decrtypt
     * @param rawKey        The password to use for decryption. Warning: This password will be
     *                      extended or shortened to match a length of 16 bytes (128 bit).
     *                      Therefore be careful! Different passwords, starting with the same
     *                      sixteen characters, result in the same AES key.
     * @return An array of bytes containing the decrypted data
     * @throws Exception If something went wrong. This might happen when the password is
     *                   wrong.
     */
    public static byte[] decrypt(byte[] encryptedData, String rawKey) throws Exception {
        Key key = generateKey(rawKey);
        Cipher c = Cipher.getInstance(ALGORITHM);
        c.init(Cipher.DECRYPT_MODE, key);
        return c.doFinal(encryptedData);
    }

    /**
     * Generates a key to use with the AES algorithm using a String <i>key</i>.<br>
     * Warning: This <i>key</i> will be extended or shortened to match a length of
     * 16 bytes (128 bit). Therefore be careful! Different passwords, starting with
     * the same sixteen characters, result in the same AES key.
     *
     * @param key A String of 128 bit (16 characters)
     * @return An AES compatible Key object used by the encrypter to encrypt the
     * data
     */
    private static Key generateKey(String key) {
        String resultKey = key;
        while (resultKey.length() < 16) {
            resultKey += resultKey;
        }
        resultKey = resultKey.substring(0, 15);

        byte[] keyValue = resultKey.getBytes(Charset.forName("UTF-8"));
        return new SecretKeySpec(keyValue, ALGORITHM);
    }

    /**
     * The exception thrown when a decryption fails.<br>
     * This might happen if the password is wrong.
     */
    public static class DecryptionFailedException extends Exception {

        @Override
        public String getMessage() {
            return "Decryption failed. Is the password correct?";
        }
    }

}
