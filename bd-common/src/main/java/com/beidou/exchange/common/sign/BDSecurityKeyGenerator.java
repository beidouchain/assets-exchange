package com.beidou.exchange.common.sign;

import lombok.Data;
import org.mindrot.jbcrypt.BCrypt;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.Base64;

public class BDSecurityKeyGenerator {

    private static final String base = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static String randomStr(int num) {
        StringBuilder randomBuilder = new StringBuilder(num);
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < num; ++i) {
            randomBuilder.append(base.charAt(random.nextInt(62)));
        }
        return randomBuilder.toString();
    }

    public static String generateToken32(String key) {
        StringBuilder keyBuilder = new StringBuilder();
        keyBuilder.append(key).append("-")
                .append(System.currentTimeMillis()).append("-")
                .append(randomStr(6));
        String token = getMessageDigestBase62(keyBuilder.toString(), "SHA-256").substring(0, 32);
        return token;
    }

    public static String generateToken32(String key, String secret) {
        StringBuilder keyBuilder = new StringBuilder();
        keyBuilder.append(key).append("-")
                .append(System.currentTimeMillis()).append("-")
                .append(randomStr(6)).append("-")
                .append(secret);
        String token = getMessageDigestBase62(keyBuilder.toString(), "SHA-256").substring(0, 32);
        return token;
    }

    public static String getMessageDigestBase36(String source, String algorithm) {
        if (source == null) {
            return null;
        }
        MessageDigest digestGenerator;
        try {
            digestGenerator = MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        String key = new BigInteger(1, digestGenerator.digest(source.getBytes())).toString(36);
        return key;
    }

    public static String getMessageDigestBase62(String source, String algorithm) {
        if (source == null) {
            return null;
        }
        MessageDigest digestGenerator;
        try {
            digestGenerator = MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        String key = Base62.base62Encode(digestGenerator.digest(source.getBytes()));
        return key;
    }

    public static byte[] getMessageDigest(String source, String algorithm) {
        if (source == null) {
            return null;
        }
        MessageDigest digestGenerator;
        try {
            digestGenerator = MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        return digestGenerator.digest(source.getBytes());
    }

    public static String getMessageDigestBase64(String source, String algorithm) {
        if (source == null) {
            return null;
        }
        MessageDigest digestGenerator;
        try {
            digestGenerator = MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        String key = base64Encode(digestGenerator.digest(source.getBytes()));
        return key;
    }

    public static String base64Encode(byte[] data) {
        final Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(data);
    }


    public static byte[] base64Decode(String data) {
        final Base64.Decoder decoder = Base64.getDecoder();
        return decoder.decode(data);
    }

    public static KeyPair generateKeyPair(String appId) {
        StringBuilder keyBuilder = new StringBuilder();
        keyBuilder.append(appId).append("-")
                .append(System.currentTimeMillis()).append("-")
                .append(randomStr(6));
        String key = getMessageDigestBase36(keyBuilder.toString(), "MD5").substring(0, 16);
        System.out.print("key: " + key + " , length: " + key.length());
        keyBuilder.append(key);
        String secret = getMessageDigestBase36(keyBuilder.toString(), "SHA-256").substring(0, 32);
        System.out.print("secret: " + secret + " , length: " + secret.length());
        return new KeyPair(key, secret);
    }

    public static String signature(String key, String secret) {
        StringBuilder signatureBuilder = new StringBuilder();
        Long timestamp = System.currentTimeMillis();
        signatureBuilder.append(key).append("-").append(secret).append("-")
                .append(timestamp);
        String signature = getMessageDigestBase64(signatureBuilder.toString(), "SHA-256");
        return signature;
    }

    private static SecretKeySpec genAESKey(String rawKey)
            throws NoSuchAlgorithmException, NoSuchProviderException {
        byte[] keybyte = getMessageDigest(rawKey, "MD5");
        SecretKeySpec key = new SecretKeySpec(keybyte, "AES");// 转换为AES专用密钥
        return key;
    }

    public static String aesEncrypt(String content, String key) {
        try {
            SecretKeySpec keySpec = genAESKey(key);
            Cipher cipher = Cipher.getInstance("AES");// 创建密码器
            byte[] byteContent = content.getBytes();
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);// 初始化为加密模式的密码器
            byte[] result = cipher.doFinal(byteContent);// 加密
            return base64Encode(result);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String aesDecrypt(String content, String key) {
        try {
            SecretKeySpec keySpec = genAESKey(key);
            Cipher cipher = Cipher.getInstance("AES");// 创建密码器
            cipher.init(Cipher.DECRYPT_MODE, keySpec);// 初始化为解密模式的密码器
            byte[] data = base64Decode(content);
            byte[] result = cipher.doFinal(data);
            return new String(result); // 明文
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String bcryptHash(String data, String salt) {
        return BCrypt.hashpw(data, salt);
    }

    public static BcryptHashResult bcryptHash(String data) {
        return bcryptHash(data, 10);
    }

    public static BcryptHashResult bcryptHash(String data, int round) {
        BcryptHashResult bcryptHashResult = new BcryptHashResult();
        String salt = BCrypt.gensalt(round);
        bcryptHashResult.setSalt(salt);
        String hash = BCrypt.hashpw(data, salt);
        bcryptHashResult.setHash(hash);
        return bcryptHashResult;
    }

    public static boolean safeCheckEqual(String text1, String text2) {
        byte[] bytes1 = text1.getBytes();
        byte[] bytes2 = text2.getBytes();

        if (bytes1.length != bytes2.length) {
            return false;
        } else {
            byte ret = 0;
            for(int i = 0; i < bytes1.length; ++i) {
                ret = (byte)(ret | bytes1[i] ^ bytes2[i]);
            }
            return ret == 0;
        }
    }

    @Data
    public static class KeyPair {
        public final String key;
        public final String secret;

        public KeyPair(String key, String secret) {
            this.key = key;
            this.secret = secret;
        }
    }

    @Data
    public static class BcryptHashResult {
        private String salt;
        private String hash;
    }
}
