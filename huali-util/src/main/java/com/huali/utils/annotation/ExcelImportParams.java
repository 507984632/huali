package com.huali.utils.annotation;

import com.huali.utils.enums.ImportVersion;

import java.lang.annotation.*;

/**
 * 导入excel 的注解
 *
 * @author Yang_my
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ExcelImportParams {

    /**
     * 导入的列名
     */
    String title() default "";

    /**
     * 是否是对象
     * true 是对象 false 是字段
     */
    boolean isObject() default false;

    /**
     * 是否必填
     */
    boolean isRequired() default false;

    /**
     * 必填校验的方法
     * 校验方法必须以类的完全限定名进行书写
     */
    String isRequiredMethod() default "";

    /**
     * 是否验证重复
     */
    boolean isRepeat() default false;

    /**
     * 重复验证的方法
     */
    String isRepeatMethod() default "";

    /**
     * 是否唯一
     */
    boolean isOnly() default false;

    /**
     * 唯一校验的方法
     */
    String isOnlyMethod() default "";

    /**
     * 导入的版本
     */
    ImportVersion version();

}
