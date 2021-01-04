package com.huali.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.reflect.Field;

/**
 * <p>
 *
 * </p>
 *
 * @author Yang_my
 * @since 2020/12/22
 */
public abstract class ExcelUtilBase {

    /**
     * 整合的对象，用于查询某个字段属性值
     */
    @Data
    @AllArgsConstructor
    protected static class MyField<O> {
        /**
         * 具体的某个类
         */
        private O o;
        /**
         * 某个类的属性字段
         */
        private Field field;
        /**
         * 属性字段的值
         */
        private String str;

        public MyField(Field field, String str) {
            this.field = field;
            this.str = str;
        }
    }

    /**
     * 插入图片
     * 坐标点：单个单元格的左上角的点。
     * 想要在第 2 行的 第 3 个格子上，插入一个 长 2 个单元格，高 3 个单元格的图片，
     * 则 startCellNum 为 3， startRowNum 为 2， endCellNum 为 5，endRowNum 为 5
     */
    @Data
    @AllArgsConstructor
    public static class Images {
        /**
         * 图片的字符数组
         */
        private byte[] imageByte;
        /**
         * 插入图片的起始行坐标点
         */
        private Integer startRowNum;
        /**
         * 插入图片的起始列坐标点
         */
        private Integer startCellNum;
        /**
         * 插入图片的结束行坐标点
         */
        private Integer endRowNum;
        /**
         * 插入图片的结束列坐标点
         */
        private Integer endCellNum;
    }
}
