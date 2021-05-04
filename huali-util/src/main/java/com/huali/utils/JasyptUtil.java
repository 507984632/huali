package com.huali.utils;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentPBEConfig;

/**
 * 项目中关于加密配置文件 内容的工具类
 *
 * 使用方法:
 * 1. 在pom中添加以下内容
 * <p>
 *  <dependency>
 *      <groupId>com.github.ulisesbocchio</groupId>
 *      <artifactId>jasypt-spring-boot</artifactId>
 *      <version>You Version</version>
 *  </dependency>
 * </p>
 *
 * 2. 在 application.yml 中配置读取其他配置文件添加上
 *      spring.profiles.active= jasypt,dev,prod,...
 *
 * 3. 添加 application-jasypt.yml 文件中添加 【添加配置文件的原因是不想让这个文件中的盐值外泄出去，也可搭配 nacos 配置中心拉去内容来实现 】
 * <p>
 *  jasypt:
 *    encryptor:
 *      password: 盐值串 # jasypt 中配置的盐值，不可外泄
 *      algorithm: PBEWithMD5AndDES # 配置 jasypt 中的加密算法
 *      iv-generator-classname: org.jasypt.iv.NoIvGenerator #暂时未知，但是 3.0.0 以后的版本得加上
 *      property:
 *        prefix: 前缀        # 加密用法的前缀 默认为 ENC(
 *        suffix: 后缀        # 加密用法的后缀 默认为 )
 * </p>
 *
 * 4. 使用的时候直接在配置文件内容中加上 上面配置的 【前缀 + 加密后的密码 + 后缀】
 *
 * @author Yang_my
 * @since 2021/5/4
 */
public class JasyptUtil {

    public static String encrypt(String pwd) {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        EnvironmentPBEConfig config = new EnvironmentPBEConfig();
        // 加密算法， 默认 PBEWithMD5AndDES
        config.setAlgorithm("PBEWithMD5AndDES");
        // 相当于加密盐值 尽量保证不会外泄
        config.setPassword("hello");
        encryptor.setConfig(config);
        // 真实密码
        String encrypt = encryptor.encrypt(pwd);
        System.out.println(encrypt);
        return encrypt;
    }

    public static void decrypt(String encryptPwd) {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        EnvironmentPBEConfig config = new EnvironmentPBEConfig();
        // 加密算法， 默认 PBEWithMD5AndDES
        config.setAlgorithm("PBEWithMD5AndDES");
        // 相当于加密盐值 尽量保证不会外泄
        config.setPassword("hello");
        encryptor.setConfig(config);
        String decrypt = encryptor.decrypt(encryptPwd);
        System.out.println(decrypt);
    }

    public static void main(String[] args) {
        String encrypt = encrypt("123");
        decrypt(encrypt);
    }


}
