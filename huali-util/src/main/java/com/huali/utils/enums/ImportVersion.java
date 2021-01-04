package com.huali.utils.enums;

/**
 * <p>
 * 导入数据的版本 导入的数据必须是同一个版本中才能导出
 * </p>
 *
 * @author Yang_my
 * @since 2020/12/16
 */
public enum ImportVersion {

    /**
     * 示例 类型
     */
    STUDENT_DTO("学生导出");

    String name;

    ImportVersion(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public static ImportVersion getValue(String name) {
        for (ImportVersion type : ImportVersion.values()) {
            if (type.getName().equals(name)) {
                return type;
            }
        }
        return null;
    }
}
