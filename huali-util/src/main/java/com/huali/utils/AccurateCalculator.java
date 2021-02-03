package com.huali.utils;


import java.math.BigDecimal;

/**
 * 精准计算机
 *
 * @author lvws
 * @serial 2019/7/10
 */
public final class AccurateCalculator {

    public static int DEFAULT_SCALE = 2;

    public static final String ZERO = bigDecimal().toString();

    public static BigDecimal bigDecimal() {
        return bigDecimal(null, DEFAULT_SCALE);
    }

    public static BigDecimal bigDecimal(Object num) {
        return bigDecimal(num, DEFAULT_SCALE);
    }

    public static BigDecimal bigDecimal(Object num, int scale) {
        BigDecimal result;
        if (num == null) {
            result = new BigDecimal("0");
        } else {
            try {
                result = new BigDecimal(String.valueOf(num).replaceAll(",", ""));
            } catch (NumberFormatException e) {
                throw new NumberFormatException("Please give me a numeral. Not " + num);
            }
        }

        return result.setScale(scale, BigDecimal.ROUND_HALF_EVEN);
    }


    public static boolean positive(Object num) {
        return bigDecimal(num).doubleValue() > 0;
    }

    public static boolean negative(Object num) {
        return bigDecimal(num).doubleValue() < 0;
    }


    public static String format(Object num) {
        return format(num, DEFAULT_SCALE);
    }

    public static String format(Object num, int scale) {
        return bigDecimal(num, scale).toString();
    }


    public static AccurateCalculator add(Object num1, Object num2) {
        return add(num1, num2, DEFAULT_SCALE);
    }

    public static AccurateCalculator add(Object num1, Object num2, int scale) {
        BigDecimal result = bigDecimal(num1).add(bigDecimal(num2));
        return new AccurateCalculator(result.setScale(scale, BigDecimal.ROUND_HALF_EVEN), scale);
    }


    public static AccurateCalculator subtract(Object num1, Object num2) {
        return subtract(num1, num2, DEFAULT_SCALE);
    }

    public static AccurateCalculator subtract(Object num1, Object num2, int scale) {
        BigDecimal result = bigDecimal(num1).subtract(bigDecimal(num2));
        return new AccurateCalculator(result.setScale(scale, BigDecimal.ROUND_HALF_EVEN), scale);
    }


    public static AccurateCalculator multiply(Object num1, Object num2) {
        return multiply(num1, num2, DEFAULT_SCALE);
    }

    public static AccurateCalculator multiply(Object num1, Object num2, int scale) {
        BigDecimal result = bigDecimal(num1).multiply(bigDecimal(num2));
        return new AccurateCalculator(result.setScale(scale, BigDecimal.ROUND_HALF_EVEN), scale);
    }


    public static AccurateCalculator divide(Object num1, Object num2) {
        return divide(num1, num2, DEFAULT_SCALE);
    }

    public static AccurateCalculator divide(Object num1, Object num2, int scale) {
        BigDecimal divide = bigDecimal(num1).divide(bigDecimal(num2), scale, BigDecimal.ROUND_HALF_EVEN);
        return new AccurateCalculator(divide);
    }


    private BigDecimal money = bigDecimal();

    private int scale = DEFAULT_SCALE;

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public AccurateCalculator() {
    }

    public AccurateCalculator(BigDecimal money) {
        this.money = money;
    }

    public AccurateCalculator(BigDecimal money, int scale) {
        this.money = money;
        this.scale = scale;
    }

    public String get() {
        return money.toString();
    }

    public boolean positive() {
        return this.money.doubleValue() > 0;
    }

    public boolean negative() {
        return this.money.doubleValue() < 0;
    }

    public AccurateCalculator add(Object num) {
        return add(this, num, this.scale);
    }

    public AccurateCalculator subtract(Object num) {
        return subtract(this, num, this.scale);
    }

    public AccurateCalculator multiply(Object num) {
        return multiply(this, num, this.scale);
    }

    public AccurateCalculator divide(Object num) {
        return divide(this, num, this.scale);
    }

    public int getScale() {
        return scale;
    }

    public AccurateCalculator setScale(int scale) {
        this.scale = scale;
        return this;
    }

    @Override
    public String toString() {
        return money.toString();
    }

}
