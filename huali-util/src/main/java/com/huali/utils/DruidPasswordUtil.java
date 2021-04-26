package com.huali.utils;


import com.alibaba.druid.filter.config.ConfigTools;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * druid 连接数据库加密密码工具类
 * 通过 privateKey 对原文密码进行加密操作
 * 通过 publicKey 对密文密码进行还原操作
 * <p>
 * application.yml 配置中的注意点
 * 1. filters: 中必须添加上 config 这个配置
 * 2.connect-properties:
 *      config.decrypt: true # 开启密码加密
 *      config.decrypt.key: # 公钥
 * 3. password：必须为密文密码 # 密文密码
 * 参考 commons 中 redis 的配置文件
 */
@Slf4j
public class DruidPasswordUtil {

    @SneakyThrows
    public static void main(String[] args) {
        // 存储的信息
        List<String> list = new ArrayList<>();
        String password = "test";

        String[] pair = getPublicPrivateKeyPair();
        String privateKey = pair[0];
        String publicKey = pair[1];

        list.add("privateKey: {}" + privateKey);
        list.add("");
        list.add("publicKey:{} " + publicKey);
        list.add("");

        log.info("---------       进行加密操作      ---------");
        String encrypt = encrypt(privateKey, password);
        log.info("密文密码：{}", encrypt);
        list.add("密文密码：" + encrypt);
        list.add("");

        log.info("---------       进行解密操作      ---------");
        String decrypt = decrypt(publicKey, encrypt);
        log.info("明文密码：{}", decrypt);
        list.add("明文密码：" + decrypt);

        log.info("---------       保存数据      ---------");
        String projectRootDirectory = FileUtil.getProjectRootDirectory();
        FileUtil.writer(list, projectRootDirectory + "\\druid数据库信息.text");
    }

    /**
     * 通过私钥进对明文密码进行加密操作
     *
     * @param privateKey 私钥
     * @param password   明文密码
     * @return 密文密码
     */
    @SneakyThrows
    public static String encrypt(String privateKey, String password) {
        return ConfigTools.encrypt(privateKey, password);
    }

    /**
     * 通过公钥和密文密码得到 明文密码
     *
     * @param publicKey         公钥
     * @param encryptedPassword 密文密码
     * @return 解密后的明文密码
     */
    @SneakyThrows
    public static String decrypt(String publicKey, String encryptedPassword) {
        return ConfigTools.decrypt(publicKey, encryptedPassword);
    }

    /**
     * 通过 工具类获得公私秘钥对
     * 第一个元素 为 私钥， 第二个元素为 公钥
     *
     * @return 公私钥对
     */
    @SneakyThrows
    public static String[] getPublicPrivateKeyPair() {
        return ConfigTools.genKeyPair(512);
    }

}
