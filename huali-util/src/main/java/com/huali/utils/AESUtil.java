package com.huali.utils;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.security.SecureRandom;

/**
 * AES 可逆加密运算
 *
 * @author Yang_my
 * @since 2021/04/25
 */
public class AESUtil {

    /**
     * 默认密钥，可以自定义
     */
    private static final String KEY = "123@abc!";
    /**
     * 向量密钥，可以自定义，必须128位(16字节)
     */
    private static final String PARAM_KEY = "1234561234567890";
    /**
     * 转型形式
     */
    private static final String CIPHER_KEY = "AES/CBC/PKCS5Padding";
    /**
     * 编码
     */
    private static final String CHARSET = "utf-8";
    /**
     * 算法名
     */
    public static final String MODE_AES = "AES";

    /**
     * 加密
     *
     * @param sSrc 内容
     * @return 加密后的字符串
     */
    public static String encrypt(String sSrc) {
        return encrypt(sSrc, KEY);
    }

    /**
     * 加密
     *
     * @param sSrc 内容
     * @param sKey 密钥
     * @return 加密后的字符串
     */
    public static String encrypt(String sSrc, String sKey) {
        try {
            Cipher cipher = Cipher.getInstance(CIPHER_KEY);//创建加密Cipher类实例
            KeyGenerator kgen = KeyGenerator.getInstance(MODE_AES);//AES加密密钥生成器
            kgen.init(128, new SecureRandom(sKey.getBytes(CHARSET)));//生成密钥128位(16字节)
            SecretKey secretKey = kgen.generateKey();//生成密钥
            IvParameterSpec iv = new IvParameterSpec(PARAM_KEY.getBytes(CHARSET));//向量iv
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);//加密初始化
            byte[] encrypted = cipher.doFinal(sSrc.getBytes(CHARSET));//完成加密操作
            return Base64.encodeBase64String(encrypted);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 解密
     *
     * @param sSrc 内容
     * @return 解密后的字符串
     */
    public static String decrypt(String sSrc) {
        return decrypt(sSrc, KEY);
    }

    /**
     * 解密
     *
     * @param sSrc 内容
     * @param sKey 密钥
     * @return 解密后的字符串
     */
    public static String decrypt(String sSrc, String sKey) {
        try {
            Cipher cipher = Cipher.getInstance(CIPHER_KEY);//创建加密Cipher类实例
            KeyGenerator kgen = KeyGenerator.getInstance(MODE_AES);//AES加密密钥生成器
            kgen.init(128, new SecureRandom(sKey.getBytes(CHARSET)));//生成密钥128位(16字节)
            SecretKey secretKey = kgen.generateKey();//生成密钥
            IvParameterSpec iv = new IvParameterSpec(PARAM_KEY.getBytes(CHARSET));//向量iv
            cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);//解密初始化
            byte[] original = cipher.doFinal(Base64.decodeBase64(sSrc));//完成解密操作
            return new String(original);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        String str = "databaseEncryptionSalt";
        String encrypt = encrypt(str);
        System.out.println("加密过后的str = " + encrypt);
        String decrypt = decrypt(encrypt);
        System.out.println("解密过后的str = " + decrypt);
    }

}
