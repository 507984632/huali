package com.huali.utils.base.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.huali.utils.base.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 字段编辑日志表
 * </p>
 *
 * @author lvws
 * @since 2019-04-10
 */
@Data
@TableName("huali_core_field_edit_log")
@EqualsAndHashCode(callSuper = true)
public class FieldEditLog extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 类全名
     */
    @TableField("class_name")
    private String className;
    /**
     * 记录ID
     */
    @TableField("record_id")
    private Long recordId;
    /**
     * 字段名称
     */
    @TableField("field_name")
    private String fieldName;
    /**
     * 更新前的值
     */
    @TableField("old_value")
    private String oldValue;
    /**
     * 更新后的值
     */
    @TableField("new_value")
    private String newValue;

}