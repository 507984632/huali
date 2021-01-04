package com.huali.utils;


import org.apache.commons.lang3.StringUtils;

/**
 * @author lvws
 * @since 2019/4/2.
 */
public class NumberUtil {

    /**
     * 字符串数字自增
     */
    public static String increase(String num) {
        return increase(num, 1);
    }

    /**
     * 字符串数字按步数递增
     *
     * @param num  字符串数字
     * @param step 步数
     * @return 结果
     */
    public static String increase(String num, int step) {
        if (StringUtils.isBlank(num)) {
            throw new IllegalArgumentException(num);
        }

        long l = Long.parseLong(num);

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < num.length(); i++) {
            builder.append("0");
        }

        return builder.append(l + step).substring(builder.length() - num.length());
    }

    /**
     * 字符串数字自减
     */
    public static String decrease(String num) {
        return decrease(num, 1);
    }

    /**
     * 字符串数字按步数递减
     *
     * @param num  字符串数字
     * @param step 步数
     * @return 结果
     */
    public static String decrease(String num, int step) {
        if (StringUtils.isBlank(num)) {
            throw new IllegalArgumentException(num);
        }

        long l = Long.parseLong(num);

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < num.length(); i++) {
            builder.append("0");
        }

        return builder.append(l - step).substring(builder.length() - num.length());
    }

    /**
     * 填充前置位数0 到指定的字符长度
     *
     * @param sourceDate   原始数据： 0001 中的 1
     * @param formatLength 总共的位数 0004 总共四4为数，所以就写4
     * @return 生成的字符串
     */
    public static String completionZero(int sourceDate, int formatLength) {
        return String.format("%0" + formatLength + "d", sourceDate);
    }

    /**
     * 填充前置位数0 到指定的字符长度
     *
     * @param sourceDate   原始数据： 0001 中的 1
     * @param formatLength 总共的位数 0004 总共四4为数，所以就写4
     * @return 生成的字符串
     */
    public static String completionZero(String sourceDate, int formatLength) {
        return completionZero(Integer.parseInt(sourceDate), formatLength);
    }

}
