package com.huali.utils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashSet;

/**
 * 计算两个时间中的差异的工具类
 *
 * @author Yang_my
 * @since 2021/04/01
 */
public class DiffDateUtil {

    /**
     * char(8)
     */
    public static final String YYYYMMDD = "yyyyMMdd";
    /**
     * char(10)
     */
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    /**
     * char(10)
     */
    public static final String YYYY_U_MM_U_DD = "yyyy年MM月dd日";
    /**
     * char(10)
     */
    public static final String YYYY__MM__DD = "yyyy/MM/dd";
    /**
     * char(14)
     */
    public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
    /**
     * char(17)
     */
    public static final String YYYYMMDDHHMMSSSSS = "yyyyMMddHHmmssSSS";
    /**
     * char(17)
     */
    public static final String YYYYMMDD_HH_MM_SS = "yyyyMMdd HH:mm:ss";
    /**
     * char(19)
     */
    public static final String YYYY__MM__DD_HH_MM_SS = "yyyy/MM/dd HH:mm:ss";
    /**
     * char(19)
     */
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    /**
     * char(19)
     */
    public static final String YYYY_U_MM_U_DD_HH_MM_SS = "yyyy年MM月dd日 HH:mm:ss";
    /**
     * char(19)
     */
    public static final String YYYY_U_MM_U_DD_HH_U_MM_U_SS = "yyyy年MM月dd日 HH时mm分ss";
    /**
     * char(20)
     */
    public static final String YYYY_U_MM_U_DD_HH_U_MM_U_SS_U = "yyyy年MM月dd日 HH时mm分ss秒";

    /**
     * char(6)
     */
    public static final String HHMMSS = "HHmmss";
    /**
     * char(8)
     */
    public static final String HH_MM_SS = "HH:mm:ss";
    /**
     * char(8)
     */
    public static final String HH_U_MM_U_SS = "HH时mm分ss";
    /**
     * char(9)
     */
    public static final String HH_U_MM_U_SS_U = "HH时mm分ss秒";

    /**
     * 年
     */
    public static final String UNIT_Y = "Y";
    /**
     * 月
     */
    public static final String UNIT_M = "M";
    /**
     * 日
     */
    public static final String UNIT_D = "D";

    /**
     * 一天的毫秒数
     */
    private static final long ND = 1000 * 24 * 60 * 60;
    /**
     * 一小时的毫秒数
     */
    private static final long NH = 1000 * 60 * 60;
    /**
     * 一分钟的毫秒数
     */
    private static final long NM = 1000 * 60;
    /**
     * 一秒钟的毫秒数
     */
    private static final long NS = 1000;

    private static final int LEN_6 = 6;
    private static final int LEN_8 = 8;
    private static final int LEN_10 = 10;
    private static final int LEN_14 = 14;
    private static final int LEN_17 = 17;
    private static final int LEN_19 = 19;

    /**
     * 将支持的format 类型进行存储
     * 该 set 只存储 年 月 日 相关
     */
    private static final HashSet<String> DATE_SET = new HashSet<String>() {
        {
            add(YYYYMMDD);
            add(YYYY_MM_DD);
            add(YYYY__MM__DD);
            add(YYYY_U_MM_U_DD);
        }
    };

    /**
     * 将支持的format 类型进行存储
     * 该 set 只存储 时 分 秒 相关
     */
    private static final HashSet<String> TIME_SET = new HashSet<String>() {
        {
            add(HHMMSS);
            add(HH_MM_SS);
            add(HH_U_MM_U_SS);
            add(HH_U_MM_U_SS_U);
        }
    };

    /**
     * 将支持的format 类型进行存储
     * 该 set 只存储 年 月 日 时 分 秒 相关
     */
    private static final HashSet<String> DATETIME_SET = new HashSet<String>() {
        {
            add(YYYYMMDDHHMMSS);
            add(YYYYMMDDHHMMSSSSS);
            add(YYYYMMDD_HH_MM_SS);
            add(YYYY__MM__DD_HH_MM_SS);
            add(YYYY_MM_DD_HH_MM_SS);
            add(YYYY_U_MM_U_DD_HH_MM_SS);
            add(YYYY_U_MM_U_DD_HH_U_MM_U_SS);
            add(YYYY_U_MM_U_DD_HH_U_MM_U_SS_U);
        }
    };

    /**
     * 将所有的 format 字符串的 set 集合整合
     */
    private static final HashSet<String> ALL_DATE_TIME_SET = new HashSet<String>() {
        {
            addAll(DATE_SET);
            addAll(TIME_SET);
            addAll(DATETIME_SET);
        }
    };

    /**
     * @return yyyyMMddHHmmss
     */
    public static String getYyyymmddhhmmss() {
        return getDateTime(YYYYMMDDHHMMSS);
    }

    /**
     * @return yyyyMMddHHmmssSSS
     */
    public static String getYyyymmddhhmmsssss() {
        return getDateTime(YYYYMMDDHHMMSSSSS);
    }

    /**
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String getYyyy_mm_dd_hh_mm_ss() {
        return getDateTime(YYYY_MM_DD_HH_MM_SS);
    }

    /**
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String getYyyy__mm__dd_hh_mm_ss() {
        return getDateTime(YYYY__MM__DD_HH_MM_SS);
    }

    /**
     * @return yyyyMMdd HH:mm:ss
     */
    public static String getYyyymmdd_hh_mm_ss() {
        return getDateTime(YYYYMMDD_HH_MM_SS);
    }

    /**
     * @return yyyy年MM月dd日 HH:mm:ss
     */
    public static String getYyyyUMmUDdHhMmSs() {
        return getDateTime(YYYY_U_MM_U_DD_HH_MM_SS);
    }

    /**
     * @return yyyy年MM月dd日 HH时mm分ss
     */
    public static String getYyyyUMmUDdHhUMmUSs() {
        return getDateTime(YYYY_U_MM_U_DD_HH_U_MM_U_SS);
    }

    /**
     * @return yyyy年MM月dd日 HH时mm分ss秒
     */
    public static String getYyyyUMmUDdHhUMmUSsU() {
        return getDateTime(YYYY_U_MM_U_DD_HH_U_MM_U_SS_U);
    }

    /**
     * @return yyyyMMdd
     */
    public static String getYyyyMmDd() {
        return getDateTime(YYYYMMDD);
    }

    /**
     * @return yyyy-MM-dd
     */
    public static String getYyyy_mm_dd() {
        return getDateTime(YYYY_MM_DD);
    }

    /**
     * @return yyyy/MM/dd
     */
    public static String getYyyy__mm__dd() {
        return getDateTime(YYYY__MM__DD);
    }

    /**
     * @return yyyy年MM月dd日
     */
    public static String getYyyyUMmUDd() {
        return getDateTime(YYYY_U_MM_U_DD);
    }

    /**
     * @return HHmmss
     */
    public static String getHhmmss() {
        return getDateTime(HHMMSS);
    }

    /**
     * @return HH:mm:ss
     */
    public static String getHh_mm_ss() {
        return getDateTime(HH_MM_SS);
    }

    /**
     * @return HH时mm分ss
     */
    public static String getHhUMmUSs() {
        return getDateTime(HH_U_MM_U_SS);
    }

    /**
     * @return HH时mm分ss秒
     */
    public static String getHhUMmUSsU() {
        return getDateTime(HH_U_MM_U_SS_U);
    }

    /**
     * 获取当日 yyyy-MM-dd 00:00:00
     */
    public static String getBeginDateTime() {
        return getDateTime(getNowDate().atTime(0, 0, 0));
    }

    /**
     * 获取当日 yyyy-MM-dd 23:59:59
     */
    public static String getEndDateTime() {
        return getDateTime(getNowDate().atTime(23, 59, 59));
    }

    /**
     * 获取 yyyy-MM-dd 00:00:00
     *
     * @param date yyyy-MM-dd
     */
    public static String getBeginDateTime(String date) {
        return date + " 00:00:00";
    }

    /**
     * 获取 yyyy-MM-dd 23:59:59
     *
     * @param date yyyy-MM-dd
     */
    public static String getEndDateTime(String date) {
        return date + " 23:59:59";
    }

    /**
     * @param format 将要格式的类型
     * @return format格式的当前日期时间
     */
    public static String getDateTime(String format) {
        return getDateTime(format, LocalDateTime.now());
    }

    /**
     * @param format        将要格式的类型
     * @param localDateTime 日期时间
     * @return format格式的当前日期时间
     */
    public static String getDateTime(String format, LocalDateTime localDateTime) {
        if (!ALL_DATE_TIME_SET.contains(format)) {
            return null;
        }
        return localDateTime.format(DateTimeFormatter.ofPattern(format));
    }

    /**
     * 默认 yyyy-MM-dd HH:mm:ss
     *
     * @param localDateTime 日期时间
     * @return format格式的当前日期时间
     */
    public static String getDateTime(LocalDateTime localDateTime) {
        return localDateTime.format(DateTimeFormatter.ofPattern(YYYY_MM_DD_HH_MM_SS));
    }

    /**
     * @return 获取当前年份
     */
    public static int getYear() {
        return getDateTime().getYear();
    }

    /**
     * @return 获取月份对象
     */
    public static Month getMonth() {
        return getDateTime().getMonth();
    }

    /**
     * @return 获取当前月份
     */
    public static int getMonthValue() {
        return getDateTime().getMonthValue();
    }

    /**
     * @return 获取当前是星期几
     */
    public static DayOfWeek getDayOfWeek() {
        return getDateTime().getDayOfWeek();
    }

    /**
     * @return 获取当前是本月第几天
     */
    public static int getDayOfMonth() {
        return getDateTime().getDayOfMonth();
    }

    /**
     * @return 获取当前是本年第几天
     */
    public static int getDayOfYear() {
        return getDateTime().getDayOfYear();
    }

    private static LocalDateTime getDateTime() {
        return LocalDateTime.now();
    }

    private static LocalDate getNowDate() {
        return LocalDate.now();
    }

    /**
     * @param dateTime 待转日期时间字符串
     * @param format   待转日期时间字符串的格式类型
     * @return 将字符日期转为 LocalDateTime
     */
    public static LocalDateTime getDateTime(String dateTime, String format) {
        requireNonNull(dateTime, "dateTime");
        if (!DATETIME_SET.contains(format)) {
            return null;
        }
        DateTimeFormatter df = DateTimeFormatter.ofPattern(format);
        return LocalDateTime.parse(dateTime, df);
    }

    /**
     * @param date   待转日期字符串
     * @param format 待转日期字符串的格式类型
     * @return 将字符日期转为 LocalDateTime
     */
    public static LocalDate getDate(String date, String format) {
        requireNonNull(date, "date");
        if (!DATE_SET.contains(format)) {
            return null;
        }
        DateTimeFormatter df = DateTimeFormatter.ofPattern(format);
        return LocalDate.parse(date, df);
    }

    /**
     * @param time   待转时间字符串
     * @param format 待转时间字符串的格式类型
     * @return 将字符日期转为 LocalDateTime
     */
    public static LocalTime getTime(String time, String format) {
        requireNonNull(time, "time");
        if (!TIME_SET.contains(format)) {
            return null;
        }
        DateTimeFormatter df = DateTimeFormatter.ofPattern(format);
        return LocalTime.parse(time, df);
    }

    /**
     * 判断是否为空或空字符，当前类不能信息不能为空字符
     */
    private static <T> T requireNonNull(T t, String message) {
        if ((t == null || (t instanceof String))) {
            assert t != null;
            if ("".equals(((String) t).trim())) {
                throw new NullPointerException(message);
            }
        }
        return t;
    }

    /**
     * 获取当前 Date 类型日期时间
     */
    public static Date getDate() {
        return localToDate(LocalDateTime.now());
    }

    /**
     * 将 LocalDateTime 转换成 Date
     *
     * @param localDateTime 日期
     * @return java.util.Date
     */
    public static Date localToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }


    /**
     * 传入一个时间节点，放入参数，即可添加上 年月日时分秒毫秒的计算，
     * ------ 如果参数为正数，则会计算到未来的时间，如果参数为负数，则会计算成曾经的时间
     * 例如 ：2021-04-01T10:12:33 的时间点 调用
     * ------ 参数 ：1year 则得到 2022-04-01T10:12:33
     * ------ 参数 ：-1year 则得到 2020-04-01T10:12:33
     * 参数是 0 表示这个参数无需改变
     * 【该方法只做添加时间，计算的是未来日期】
     *
     * @param localDateTime 传入的时间
     * @param year          年份
     * @param month         月份
     * @param day           天
     * @param hour          小时
     * @param second        秒
     * @param minute        毫秒
     * @return java.time.LocalDateTime .
     */
    public static LocalDateTime add(LocalDateTime localDateTime, long year, long month, long day,
                                    long hour, long second, long minute) {
        localDateTime = localDateTime.plusYears(year);
        localDateTime = localDateTime.plusMonths(month);
        localDateTime = localDateTime.plusDays(day);
        localDateTime = localDateTime.plusHours(hour);
        localDateTime = localDateTime.plusSeconds(second);
        localDateTime = localDateTime.plusMinutes(minute);
        return localDateTime;
    }

    /**
     * 传入一个时间节点，放入参数，即可添加上 年月日时分秒毫秒的计算，
     * ------ 如果参数为正数，则会计算到未来的时间，如果参数为负数，则会计算成曾经的时间
     * 例如 ：2021-04-01T10:12:33 的时间点 调用
     * ------ 参数 ：1year 则得到 2022-04-01T10:12:33
     * ------ 参数 ：-1year 则得到 2020-04-01T10:12:33
     * 参数是 0 表示这个参数无需改变
     * 【该方法只做减去时间，计算的是曾经日期】
     *
     * @param localDateTime 传入的时间
     * @param year          年份
     * @param month         月份
     * @param day           天
     * @param hour          小时
     * @param second        秒
     * @param minute        毫秒
     * @return java.time.LocalDateTime .
     */
    public static LocalDateTime minus(LocalDateTime localDateTime, long year, long month, long day,
                                      long hour, long minute, long second) {
        localDateTime = localDateTime.minusYears(year);
        localDateTime = localDateTime.minusMonths(month);
        localDateTime = localDateTime.minusDays(day);
        localDateTime = localDateTime.minusHours(hour);
        localDateTime = localDateTime.minusMinutes(minute);
        localDateTime = localDateTime.minusSeconds(second);
        return localDateTime;
    }

    /**
     * 判断月日是否重复，可用于判断生日节日等
     *
     * @param localDateTime 日期
     * @param month         月份
     * @param day           月份的第几天
     * @return boolean
     */
    public static boolean repetitionMonthDay(LocalDateTime localDateTime, int month, int day) {
        return MonthDay.of(month, day).equals(MonthDay.from(localDateTime));
    }

    /**
     * differenceDay 【可能存在问题】
     * 只是天数，最大为30
     */
    @Deprecated
    public static int diffDay(LocalDate startDate, LocalDate endDate) {
        return Period.between(startDate, endDate).getDays();
    }

    /**
     * differenceMonth 【可能存在问题】
     * 只是月数，最大11
     */
    @Deprecated
    public static int diffMonth(LocalDate startDate, LocalDate endDate) {
        return Period.between(startDate, endDate).getMonths();
    }

    /**
     * differenceYear 【可能存在问题】
     */
    @Deprecated
    public static int diffYear(LocalDate startDate, LocalDate endDate) {
        return Period.between(startDate, endDate).getYears();
    }

    /**
     * interval 计算年份的间隔
     *
     * @param startDateTime 开始时间
     * @param endDateTime   结束时间
     * @return long .
     */
    public static long diffYear(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return ChronoUnit.YEARS.between(startDateTime, endDateTime);
    }

    /**
     * interval 计算月份的间隔
     *
     * @param startDateTime 开始时间
     * @param endDateTime   结束时间
     * @return long .
     */
    public static long diffMonth(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return ChronoUnit.MONTHS.between(startDateTime, endDateTime);
    }

    /**
     * interval 计算天数的间隔
     *
     * @param startDateTime 开始时间
     * @param endDateTime   结束时间
     * @return long .
     */
    public static long diffDay(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return ChronoUnit.DAYS.between(startDateTime, endDateTime);
    }

    /**
     * interval 计算小时的间隔
     *
     * @param startDateTime 开始时间
     * @param endDateTime   结束时间
     * @return long .
     */
    public static long diffHour(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return ChronoUnit.HOURS.between(startDateTime, endDateTime);
    }

    /**
     * interval 计算分钟的间隔
     *
     * @param startDateTime 开始时间
     * @param endDateTime   结束时间
     * @return long .
     */
    public static long diffMinute(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return ChronoUnit.MINUTES.between(startDateTime, endDateTime);
    }

    /**
     * interval 计算秒数的间隔
     *
     * @param startDateTime 开始时间
     * @param endDateTime   结束时间
     * @return long .
     */
    public static long diffSecond(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return ChronoUnit.SECONDS.between(startDateTime, endDateTime);
    }

    /**
     * interval 计算年份的间隔
     *
     * @param startDateTime 开始时间
     * @param endDateTime   结束时间
     * @param format        日期格式
     * @return long .
     */
    public static long diffYear(String startDateTime, String endDateTime, String format) {
        LocalDateTime beginDateTime = getDateTime(startDateTime, format);
        LocalDateTime overDateTime = getDateTime(endDateTime, format);
        assert beginDateTime != null;
        return ChronoUnit.YEARS.between(beginDateTime, overDateTime);
    }

    /**
     * interval 计算月份的间隔
     *
     * @param startDateTime 开始时间
     * @param endDateTime   结束时间
     * @param format        日期格式
     * @return long .
     */
    public static long diffMonth(String startDateTime, String endDateTime, String format) {
        LocalDateTime beginDateTime = getDateTime(startDateTime, format);
        LocalDateTime overDateTime = getDateTime(endDateTime, format);
        assert beginDateTime != null;
        return ChronoUnit.MONTHS.between(beginDateTime, overDateTime);
    }

    /**
     * interval 计算天数的间隔
     *
     * @param startDateTime 开始时间
     * @param endDateTime   结束时间
     * @param format        日期格式
     * @return long .
     */
    public static long diffDay(String startDateTime, String endDateTime, String format) {
        LocalDateTime beginDateTime = getDateTime(startDateTime, format);
        LocalDateTime overDateTime = getDateTime(endDateTime, format);
        assert beginDateTime != null;
        return ChronoUnit.DAYS.between(beginDateTime, overDateTime);
    }

    /**
     * interval 计算小时的间隔
     *
     * @param startDateTime 开始时间
     * @param endDateTime   结束时间
     * @param format        日期格式
     * @return long .
     */
    public static long diffHour(String startDateTime, String endDateTime, String format) {
        LocalDateTime beginDateTime = getDateTime(startDateTime, format);
        LocalDateTime overDateTime = getDateTime(endDateTime, format);
        assert beginDateTime != null;
        return ChronoUnit.HOURS.between(beginDateTime, overDateTime);
    }

    /**
     * interval 计算分钟的间隔
     *
     * @param startDateTime 开始时间
     * @param endDateTime   结束时间
     * @param format        日期格式
     * @return long .
     */
    public static long diffMinute(String startDateTime, String endDateTime, String format) {
        LocalDateTime beginDateTime = getDateTime(startDateTime, format);
        LocalDateTime overDateTime = getDateTime(endDateTime, format);
        assert beginDateTime != null;
        return ChronoUnit.MINUTES.between(beginDateTime, overDateTime);
    }

    /**
     * intervalYear 计算秒数的间隔
     *
     * @param startDateTime 开始时间
     * @param endDateTime   结束时间
     * @param format        日期格式
     * @return long .
     */
    public static long diffSecond(String startDateTime, String endDateTime, String format) {
        LocalDateTime beginDateTime = getDateTime(startDateTime, format);
        LocalDateTime overDateTime = getDateTime(endDateTime, format);
        assert beginDateTime != null;
        return ChronoUnit.SECONDS.between(beginDateTime, overDateTime);
    }

    /*-------------------------------------------------测试方法-------------------------------------------------*/

    /**
     * 查看所有的 format 格式字符串
     */
    public static void viewAllDateOrTime() {
        int i = 0;
        for (String s : ALL_DATE_TIME_SET) {
            System.out.println(i++ + "格式  --> " + s);
            System.out.println("时间样式  --> " + getDateTime(s));
        }
    }

    /**
     * 通过参数来达到添加 / 减去 年月日时分秒
     */
    public static void viewAddOrMinus() {
        LocalDateTime dateTime = getDateTime("2018-06-01 10:12:05", YYYY_MM_DD_HH_MM_SS);
        LocalDateTime add = add(dateTime, -1, 0, 0, 0, 0, 0);
        LocalDateTime minus = minus(dateTime, -1, -1, -1, -1, -1, -1);
        System.out.println(add);
        System.out.println(minus);
    }

    /**
     * 获得某一天中， 00:00:00 / 23:59:59 的时间点
     */
    public static void viewBeginAndEndDateTime() {
        System.out.println(getBeginDateTime());
        System.out.println(getEndDateTime());

        System.out.println(getBeginDateTime("2018-01-01"));
        System.out.println(getEndDateTime("2018-01-01"));
    }

    public static void viewDiff() {
        /*
         * 2019年03月09日 01时30分12秒
         * 8
         * 2
         * 1
         * 2018-01-01 2019-03-09 相差 1年2月8日
         * */
        LocalDate date = getDate("2018-01-01", YYYY_MM_DD);
        System.out.println(getYyyyUMmUDdHhUMmUSsU());
        System.out.println(diffDay(date, LocalDate.now()));
        System.out.println(diffDay(LocalDate.now(), date));
        System.out.println(diffMonth(date, LocalDate.now()));
        System.out.println(diffYear(date, LocalDate.now()));
    }

    /**
     * 计算两个时间节点相差的时间量
     */
    public static void viewDiffLong() {
        /*
         * 2018-01-01T10:12:05
         * 2019-03-09T01:51:15.028
         * 相差年：1
         * 相差月：14
         * 相差日：431
         * 相差时：10359
         * 相差分：621579
         * 相差秒：37294750
         * */
        LocalDateTime dateTime = getDateTime("2018-01-01 10:12:05", YYYY_MM_DD_HH_MM_SS);
        LocalDateTime now = LocalDateTime.now();
        System.out.println(dateTime);
        System.out.println(now);
        System.out.println("相差年：" + diffYear(dateTime, now));
        System.out.println("相差月：" + diffMonth(dateTime, now));
        System.out.println("相差日：" + diffDay(dateTime, now));
        System.out.println("相差时：" + diffHour(dateTime, now));
        System.out.println("相差分：" + diffMinute(dateTime, now));
        System.out.println("相差秒：" + diffSecond(dateTime, now));
    }

    /**
     * 判断执行代码的时间 的月份 天数 是否是 4月 1号
     */
    public static void viewRepetition() {
        /*
         * 2019年03月09日 01时14分11秒
         * true
         * */
        System.out.println(getYyyyUMmUDdHhUMmUSsU());
        System.out.println(repetitionMonthDay(LocalDateTime.now(), 4, 1));
    }

    public static void viewStringToLocalDateTime() {
        /*
         *
         * 2018-06-01T10:12:05
         * 2018-06-01T10:12:05
         * 2018-06-01T10:12:05
         * 2018-06-01T10:12:05
         * 2018-06-01
         * 2018-06-01
         * 10:12:05
         *
         * */
        System.out.println(getDateTime("2018-06-01 10:12:05", YYYY_MM_DD_HH_MM_SS));
        System.out.println(getDateTime("2018年06月01日 10时12分05秒", YYYY_U_MM_U_DD_HH_U_MM_U_SS_U));
        System.out.println(getDateTime("2018年06月01日 10时12分05", YYYY_U_MM_U_DD_HH_U_MM_U_SS));
        System.out.println(getDateTime("2018/06/01 10:12:05", YYYY__MM__DD_HH_MM_SS));
//        System.out.println(getDateTime(null, YYYY__MM__DD_HH_MM_SS));

        System.out.println(getDate("2018-06-01", YYYY_MM_DD));
        System.out.println(getDate("2018/06/01", YYYY__MM__DD));

        System.out.println(getTime("10:12:05", HH_MM_SS));
    }

    public static void viewYearOrMonthOrWeek() {
        // 2019年03月08日 22时49分38秒
        System.out.println(getYyyyUMmUDdHhUMmUSsU());
        // 2019
        System.out.println(getYear());
        // march
        System.out.println(getMonth());
        // 3
        System.out.println(getMonthValue());
        // friday
        System.out.println(getDayOfWeek());
        // 8
        System.out.println(getDayOfMonth());
        // 67
        System.out.println(getDayOfYear());
    }

    public static void main(String[] args) {
        viewAllDateOrTime();
        viewAddOrMinus();
        viewBeginAndEndDateTime();
        viewDiff();
        viewDiffLong();
        viewRepetition();
        viewStringToLocalDateTime();
        viewYearOrMonthOrWeek();
    }

}
