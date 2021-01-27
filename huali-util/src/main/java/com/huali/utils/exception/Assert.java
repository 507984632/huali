package com.huali.utils.exception;

import com.huali.utils.AccurateCalculator;
import com.huali.utils.base.BaseModel;
import org.apache.commons.lang3.StringUtils;

/**
 * @author myUser
 * @date 2021-01-23 17:26
 **/
public final class Assert {

    private static final String VALIDATE_ID = "无效的%sID【%s】";
    private static final String VALIDATE_PROPERTY = "无效的%s【%s】";
    private static final String NOT_NULL = "【%s】为非空！";
    private static final String DUPLICATE = "重复的【%s】";

    private static String getMessage(String key, Object... args) {
        return String.format(key, args);
    }

    public static void validateId(Object object, String name, String id) {
        if (object == null) {
            throw new CheckedException(getMessage(VALIDATE_ID, name, id));
        }
    }

    public static void validateId(Object object, String name, Long id) {
        if (object == null) {
            throw new CheckedException(getMessage(VALIDATE_ID, name, id));
        }
    }

    public static void validateProperty(Object object, String name, String value) {
        if (object == null) {
            throw new CheckedException(getMessage(VALIDATE_PROPERTY, name, value));
        }
    }

    public static void duplicate(BaseModel unqiue, BaseModel update, String name) {
        if (unqiue != null && !unqiue.getId().equals(update.getId())) {
            throw new CheckedException(getMessage(DUPLICATE, name));
        }
    }

    public static void notNull(Object object, String name) {
        if (object == null) {
            throw new CheckedException(getMessage(NOT_NULL, name));
        }
    }

    public static void notNull(String str, String name) {
        if (StringUtils.isBlank(str)) {
            throw new CheckedException(getMessage(NOT_NULL, name));
        }
    }

    public static void isPositive(String num) {
        if (!AccurateCalculator.positive(num)) {
            throw new CheckedException("num '" + num + "' must be positive");
        }
    }

}
