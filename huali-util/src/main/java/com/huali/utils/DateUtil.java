package com.huali.utils;

import org.apache.commons.lang3.time.FastDateFormat;

import java.text.ParseException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;


/**
 * @author lvws
 * @see Duration 用于计算时间的差
 * @since 2019/10/31.
 */
public class DateUtil {

    /**
     * 日期格式
     **/
    public enum DateTimePattern {

        /**
         * 时间格式
         */
        HH_MM("HH:mm"),
        HHMMSS("HHmmss"),
        HH_MM_SS("HH:mm:ss"),
        HH_MM_SS_SSS("HH:mm:ss.SSS"),

        /**
         * 日期格式
         */
        YYYYMMDD("yyyyMMdd"),
        YYYY_MM_DD("yyyy-MM-dd"),

        /**
         * 日期+时间
         */
        YYYY_MM_DD_HH_MM("yyyy-MM-dd HH:mm"),
        YYYY_MM_DD_HH_MM_SS("yyyy-MM-dd HH:mm:ss"),
        YYYY_MM_DD_HH_MM_SS_SSS("yyyy-MM-dd HH:mm:ss.SSS");

        private final String pattern;

        DateTimePattern(String pattern) {
            this.pattern = pattern;
        }

        @Override
        public String toString() {
            return pattern;
        }
    }

    public static String formatDate(Date date) {
        return format(date, DateTimePattern.YYYY_MM_DD);
    }

    public static String formatTime(Date date) {
        return format(date, DateTimePattern.HH_MM_SS);
    }

    public static String formatDateTime(Date date) {
        return format(date, DateTimePattern.YYYY_MM_DD_HH_MM_SS);
    }

    public static String format(Date date, DateTimePattern dateTimePattern) {
        return FastDateFormat.getInstance(dateTimePattern.pattern).format(date);
    }


    public static String formatDate(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern(DateTimePattern.YYYY_MM_DD.pattern));
    }

    public static String formatTime(LocalTime time) {
        return time.format(DateTimeFormatter.ofPattern(DateTimePattern.YYYY_MM_DD_HH_MM_SS.pattern));
    }

    public static String formatDateTime(LocalDateTime time) {
        return time.format(DateTimeFormatter.ofPattern(DateTimePattern.HH_MM_SS.pattern));
    }

    public static String formatDateTime(LocalDateTime time, DateTimePattern dateTimePattern) {
        return time.format(DateTimeFormatter.ofPattern(dateTimePattern.pattern));
    }


    public static Date parse(String date) throws ParseException {
        return FastDateFormat.getInstance().parse(date);
    }

    public static Date parse(String date, DateTimePattern dateTimePattern) throws ParseException {
        return FastDateFormat.getInstance(dateTimePattern.pattern).parse(date);
    }


    public static LocalDate date2LocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static LocalTime date2LocalTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
    }

    public static LocalDateTime date2LocalDateTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * 获取当前日期
     */
    public static String getDate() {
        return formatDate(LocalDate.now());
    }

    /**
     * 获取当前日期时间
     */
    public static String getDateTime() {
        return formatDateTime(LocalDateTime.now());
    }

    /**
     * 按格式获取当前日期时间
     */
    public static String getDateTime(DateTimePattern pattern) {
        return format(new Date(), pattern);
    }

    /**
     * 获取当前时间
     */
    public static String getTime() {
        return formatTime(new Date());
    }

}
