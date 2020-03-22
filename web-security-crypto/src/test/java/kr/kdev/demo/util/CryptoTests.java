package kr.kdev.demo.util;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.jupiter.api.Test;
import org.junit.runners.MethodSorters;
import org.springframework.util.StreamUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CryptoTests {

    private static String path = CryptoTests.class.getProtectionDomain().getCodeSource().getLocation().getPath();

    /**
     * [TEST_000] RSA 키 쌍 생성
     */
    @Test
    public void TEST_000_generateRSAKeyPair() throws NoSuchProviderException, NoSuchAlgorithmException, IOException {
        String path = CryptoTests.class.getProtectionDomain().getCodeSource().getLocation().getPath();

        KeyPair keyPair = CryptoUtils.generateRSAKeyPair();

        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();

        Assert.assertNotNull(privateKey);
        Assert.assertNotNull(publicKey);

        System.out.println("The generated file path : "+ path);
        StreamUtils.copy(privateKey.getEncoded(), new FileOutputStream(path + "private.pem"));
        StreamUtils.copy(publicKey.getEncoded(), new FileOutputStream(path + "public.pem"));
    }

    /**
     * [TEST_001] DES 비밀키 생성
     */
    @Test
    public void TEST_001_generateDESKey() throws NoSuchAlgorithmException, IOException {
        byte[] bytes = CryptoUtils.generateSecretKey();
        Assert.assertNotNull(bytes);

        StreamUtils.copy(bytes, new FileOutputStream(path + "key.ser"));
    }

    @Test
    public void TEST_002_generateSalt() {
        String salt = CryptoUtils.generateKey();
        Assert.assertNotNull(salt);

        System.out.println(salt);
    }

}
