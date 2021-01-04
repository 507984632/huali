package com.huali.utils.annotation;

import com.huali.utils.enums.ExportVersion;

import java.lang.annotation.*;

/**
 * 导出模板中用到的注解，
 * 在属性上添加该注解，说明该属性会当作excel列
 * columnName() 值会被当作表头
 * columnNum() 值会被当作列数
 * 列从0开始，请按照顺序填写 0 - 6...
 *
 * @author Yang_my
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ExcelParams {

    /**
     * 导出模板中 这个列的列名
     *
     * @return 某一列的名称
     */
    String columnName() default "";

    /**
     * 导出模板中 这个列所在的是第几列
     *
     * @return 所在的是第几列
     */
    int columnNum() default -1;

    /**
     * 导出模板中 这个列的列宽
     *
     * @return 设置该单元格的列宽
     */
    int columnWidthNum() default 20;

    /**
     * 导出版本
     */
    ExportVersion exportVersion();

    /**
     * 是否是对象
     * 根据这个参数判断是否按照 对象(true)/参数(false) 的方式获取值
     *
     * @return
     */
    boolean isObject() default false;
}
