package com.huali.utils.base;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author myUser
 * @date 2021-01-23 16:03
 **/
@Data
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class BaseModel implements Serializable {

    @TableId(value = "id_", type = IdType.AUTO)
    private Long id;

    @JsonIgnore
    @TableField("create_by")
    private Long createBy;

    @JsonIgnore
    @TableField("create_time")
    private LocalDateTime createTime;

    @JsonIgnore
    @TableField("update_by")
    private Long updateBy;

    @JsonIgnore
    @TableField("update_time")
    private LocalDateTime updateTime;

    @JsonIgnore
    @TableField("deleted_")
    @TableLogic
    private Boolean deleted;

}
