package com.beidou.exchange.common;

import com.beidou.exchange.common.exception.BizException;
import org.bouncycastle.util.encoders.Base64;

import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Locale;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class AESUtil {

    /**
     * 密钥算法
     */
    private static final String ALGORITHM = "AES";
    /**
     * 加解密算法/工作模式/填充方式
     */
    private static final String ALGORITHM_MODE_PADDING = "AES/ECB/PKCS5Padding";
    private static final String pk = "3d6ecd1d264b7224684af2c0a73f8d9b";
    /**
     * 生成key
     */
    private static SecretKeySpec key = new SecretKeySpec(pk.getBytes(), ALGORITHM);

    /**
     * AES加密
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static String encryptData(String data) {
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance(ALGORITHM_MODE_PADDING);
            // 初始化
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return new String(Base64.encode(cipher.doFinal(data.getBytes())));
        } catch (Exception ex) {
            throw new BizException(ErrorEnum.AES_ERROR);
        }
    }

    static SecretKey secretKey;

    /**
     * AES解密
     *
     * @param data 待解密的数据
     * @return
     * @throws Exception
     */
    public static String decryptData(String data)  {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM_MODE_PADDING);
            secretKey = new SecretKeySpec(pk.getBytes("utf-8"), "AES");

            cipher.init(Cipher.DECRYPT_MODE, secretKey);//使用解密模式初始化 密钥
            java.util.Base64.Decoder base64 = java.util.Base64.getDecoder();
            byte[] decrypt = cipher.doFinal(base64.decode(data));
            return new String(decrypt, "utf-8");
        } catch (Exception ex) {
            throw new BizException(ErrorEnum.AES_ERROR);
        }


    }


}
