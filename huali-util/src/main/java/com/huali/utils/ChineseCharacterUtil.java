package com.huali.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author lvws
 * @since 2020/6/3
 */
public final class ChineseCharacterUtil {

    /***
     * ^[\u2E80-\u9FFF]+$ 匹配所有东亚区的语言
     * ^[\u4E00-\u9FFF]+$ 匹配简体和繁体
     * ^[\u4E00-\u9FA5]+$ 匹配简体
     */
    private static final String REG_EXP = "^[\u4E00-\u9FFF]+$";

    /**
     * 匹配
     * <p>
     * 根据字符和正则表达式进行匹配
     *
     * @param str 源字符串
     * @return true：匹配成功  false：匹配失败
     */
    private static boolean match(String str) {
        Pattern pattern = Pattern.compile(REG_EXP);
        Matcher matcher = pattern.matcher(str);
        return matcher.find();
    }

    /**
     * 获取汉字首字母或全拼大写字母
     *
     * @param chinese 汉字
     * @param isFull  是否全拼 true:表示全拼 false表示：首字母
     * @return 全拼或者首字母大写字符窜
     */
    public static String getUpperCase(String chinese, boolean isFull) {
        return convertHanzi2Pinyin(chinese, isFull).toUpperCase();
    }

    /**
     * 获取汉字首字母或全拼小写字母
     *
     * @param chinese 汉字
     * @param isFull  是否全拼 true:表示全拼 false表示：首字母
     * @return 全拼或者首字母小写字符窜
     */
    public static String getLowerCase(String chinese, boolean isFull) {
        return convertHanzi2Pinyin(chinese, isFull).toLowerCase();
    }

    /**
     * 将汉字转成拼音
     * <p>
     * 取首字母或全拼
     *
     * @param hanzi  汉字字符串
     * @param isFull 是否全拼 true:表示全拼 false表示：首字母
     * @return 拼音
     */
    private static String convertHanzi2Pinyin(String hanzi, boolean isFull) {
        StringBuilder sb = new StringBuilder();
        if (hanzi == null || "".equals(hanzi.trim())) {
            return "";
        }
        String pinyin;
        for (int i = 0; i < hanzi.length(); i++) {
            char unit = hanzi.charAt(i);
            //是汉字，则转拼音
            if (match(String.valueOf(unit))) {
                pinyin = convertSingleHanzi2Pinyin(unit);
                if (isFull) {
                    sb.append(pinyin);
                } else {
                    sb.append(pinyin.charAt(0));
                }
            } else {
                sb.append(unit);
            }
        }
        return sb.toString();
    }

    /**
     * 将单个汉字转成拼音
     *
     * @param hanzi 汉字字符
     * @return 拼音
     */
    private static String convertSingleHanzi2Pinyin(char hanzi) {
        HanyuPinyinOutputFormat outputFormat = new HanyuPinyinOutputFormat();
        outputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        String[] res;
        StringBuilder sb = new StringBuilder();
        try {
            res = PinyinHelper.toHanyuPinyinStringArray(hanzi, outputFormat);
            //对于多音字，只用第一个拼音
            sb.append(res[0]);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return sb.toString();
    }

}
