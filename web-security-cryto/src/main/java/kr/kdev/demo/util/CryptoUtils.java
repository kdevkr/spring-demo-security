package kr.kdev.demo.util;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.*;

public class CryptoUtils {

    public static String generateKey() {
        // using StringKeyGenerator
        return KeyGenerators.string().generateKey();
    }

    public static byte[] generateKey(int keyLength) {
        // using BytesKeyGenerator
        return KeyGenerators.shared(keyLength).generateKey();
    }

    public static PasswordEncoder createPasswordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    public static byte[] generateSecretKey() throws NoSuchAlgorithmException {
        KeyGenerator keygenerator = KeyGenerator.getInstance("DES");
        SecretKey secretKey = keygenerator.generateKey();
        return secretKey.getEncoded();
    }

    public static KeyPair generateRSAKeyPair() throws NoSuchProviderException, NoSuchAlgorithmException {
        Security.addProvider(new BouncyCastleProvider());
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", "BC");
        generator.initialize(2048);
        return generator.generateKeyPair();
    }
}
