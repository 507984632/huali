package com.huali.utils;

import cz.mallat.uasparser.OnlineUpdater;
import cz.mallat.uasparser.UASparser;
import cz.mallat.uasparser.UserAgentInfo;
import lombok.SneakyThrows;

import java.io.IOException;

/**
 * <p>
 * 获得用户访问时，硬件设施（不太精准，因为用户可以更改设备的请求头）
 * </p>
 *
 * @author Yang_my
 * @since 2020/12/21
 */
public class UserAgentUtil {

    static UASparser uasParser = null;

    // 初始化 uasParser 对象
    static {
        try {
            uasParser = new UASparser(OnlineUpdater.getVendoredInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获得用户的 操作系统名称
     *
     * @param info 客户端请求头中的 User-Agent 的请求头
     * @return 用户的操作系统名称
     */
    @SneakyThrows
    public static String getOsFamily(String info) {
        return uasParser.parse(info).getOsFamily();
    }

    /**
     * 获得用户的 操作系统版本
     *
     * @param info 客户端请求头中的 User-Agent 的请求头
     * @return 用户的操作系统版本
     */
    @SneakyThrows
    public static String getOsName(String info) {
        return uasParser.parse(info).getOsName();
    }

    /**
     * 获得用户的 浏览器名称
     *
     * @param info 客户端请求头中的 User-Agent 的请求头
     * @return 用户浏览器名称
     */
    @SneakyThrows
    public static String getUaFamily(String info) {
        return uasParser.parse(info).getUaFamily();
    }

    /**
     * 获得用户的 浏览器版本
     *
     * @param info 客户端请求头中的 User-Agent 的请求头
     * @return 用户浏览器版本
     */
    @SneakyThrows
    public static String getBrowserVersionInfo(String info) {
        return uasParser.parse(info).getBrowserVersionInfo();
    }

    /**
     * 获得用户的 浏览器名称 + 版本
     *
     * @param info 客户端请求头中的 User-Agent 的请求头
     * @return 浏览器名称 + 版本
     */
    @SneakyThrows
    public static String getUaName(String info) {
        return uasParser.parse(info).getUaName();
    }

    /**
     * 获得用户的 设备类型
     *
     * @param info 客户端请求头中的 User-Agent 的请求头
     * @return 设备类型 【Tablet(iPad) / Smartphone(移动手机) / Personal computer(PC)】
     */
    @SneakyThrows
    public static String getDeviceType(String info) {
        return uasParser.parse(info).getDeviceType();
    }

    /**
     * 获得用户的 浏览器类型
     *
     * @param info 客户端请求头中的 User-Agent 的请求头
     * @return 浏览器的类型【Mobile Browser(移动浏览器) / Browser(浏览器)】
     */
    @SneakyThrows
    public static String getType(String info) {
        return uasParser.parse(info).getType();
    }

    public static void main(String[] args) {
        // 示例
        String str = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.130 Safari/537.36";

        // 手机qq 浏览器 Android
        String str1 = "Mozilla/5.0 (Linux; U; Android 10; zh-cn; HLK-AL00 Build/HONORHLK-AL00) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/77.0.3865.120 MQQBrowser/11.0 Mobile Safari/537.36 COVC/045429";

        // 手机qq 浏览器  pc
        String str2 = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.87 Safari/537.36";

        // 手机qq 浏览器 iPad
        String str3 = "Mozilla/5.0 (iPad; U; CPU OS 5_0 like Mac OS X; en-us) AppleWebKit/534.46 (KHTML, like Gecko) Version/5.1 Mobile/9A334 Safari/7534.48.3";

        // 手机火狐 浏览器 Android
        String str4 = "Mozilla/5.0 (Android 10; Mobile; rv:84.0) Gecko/84.0 Firefox/84.0";

        // 手机夸克 IPhone
        String str5 = "Mozilla/5.0 (iPhone; U; CPU iPhone OS 4_3_2 like Mac OS X; en-us) AppleWebKit/533.17.9 (KHTML, like Gecko) Version/5.0.2 Mobile/8H7 Safari/6533.18.5 Quark/4.5.4.154";

        str = str5;

        System.out.println(str);
        try {
            UserAgentInfo userAgentInfo = UserAgentUtil.uasParser.parse(str);
            System.out.println("操作系统名称：" + userAgentInfo.getOsFamily());//
            System.out.println("操作系统：" + userAgentInfo.getOsName());//
            System.out.println("浏览器名称：" + userAgentInfo.getUaFamily());//
            System.out.println("浏览器版本：" + userAgentInfo.getBrowserVersionInfo());//
            System.out.println("设备类型：" + userAgentInfo.getDeviceType());
            System.out.println("浏览器:" + userAgentInfo.getUaName());
            System.out.println("类型：" + userAgentInfo.getType());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
