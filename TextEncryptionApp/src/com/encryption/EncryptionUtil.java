package encryption;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class EncryptionUtil {
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final int KEY_SIZE = 256;
    private static final int IV_SIZE = 12;
    private static final int TAG_SIZE = 128;
    private static final SecureRandom secureRandom = new SecureRandom();
    private static final SecretKey key = generateKey();

    public static SecretKey generateKey() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM);
            keyGen.init(KEY_SIZE, secureRandom);
            return keyGen.generateKey();
        } catch (Exception e) {
            throw new RuntimeException("Error generating encryption key", e);
        }
    }

    public static String encrypt(String plaintext) {
        try {
            byte[] iv = new byte[IV_SIZE];
            secureRandom.nextBytes(iv);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            GCMParameterSpec parameterSpec = new GCMParameterSpec(TAG_SIZE, iv);
            cipher.init(Cipher.ENCRYPT_MODE, key, parameterSpec);
            byte[] encryptedText = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
            byte[] combinedIvAndCipherText = new byte[IV_SIZE + encryptedText.length];
            System.arraycopy(iv, 0, combinedIvAndCipherText, 0, IV_SIZE);
            System.arraycopy(encryptedText, 0, combinedIvAndCipherText, IV_SIZE, encryptedText.length);
            return Base64.getEncoder().encodeToString(combinedIvAndCipherText);
        } catch (Exception e) {
            throw new RuntimeException("Error encrypting text", e);
        }
    }

    public static String decrypt(String encryptedText) {
        try {
            byte[] decodedText = Base64.getDecoder().decode(encryptedText);
            byte[] iv = new byte[IV_SIZE];
            System.arraycopy(decodedText, 0, iv, 0, IV_SIZE);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            GCMParameterSpec parameterSpec = new GCMParameterSpec(TAG_SIZE, iv);
            cipher.init(Cipher.DECRYPT_MODE, key, parameterSpec);
            byte[] originalText = cipher.doFinal(decodedText, IV_SIZE, decodedText.length - IV_SIZE);
            return new String(originalText, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Error decrypting text", e);
        }
    }
}
