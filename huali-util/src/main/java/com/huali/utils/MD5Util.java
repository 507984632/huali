package com.huali.utils;


import java.security.MessageDigest;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * MD5加密工具类
 *
 * @author lvws
 */
public class MD5Util {

    private static final char[] DIGITS =
            new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private static MessageDigest digest;

    private MD5Util() {
    }

    public static String encrypt(String plaintext) {
        if (digest == null) {
            Lock lock = new ReentrantLock();
            lock.lock();
            try {
                digest = MessageDigest.getInstance("MD5");
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            } finally {
                lock.unlock();
            }
        }
        digest.update(plaintext.getBytes());
        byte[] cipher = digest.digest();
        char[] str = new char[32];
        int p = 0;
        for (int i = 0; i < 16; ++i) {
            byte _b = cipher[i];
            str[p++] = DIGITS[_b >>> 4 & 15];
            str[p++] = DIGITS[_b & 15];
        }
        return new String(str);
    }
}