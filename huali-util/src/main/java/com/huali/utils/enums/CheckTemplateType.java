package com.huali.utils.enums;

/**
 * 对于校验模板的类型
 */
public enum CheckTemplateType {
    /**
     * 测试模板
     */
    Test("测试模板");

    String name;

    CheckTemplateType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public static CheckTemplateType getValue(String name) {
        for (CheckTemplateType type : CheckTemplateType.values()) {
            if (type.getName().equals(name)) {
                return type;
            }
        }
        return null;
    }
}