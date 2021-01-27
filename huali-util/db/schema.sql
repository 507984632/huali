DROP TABLE IF EXISTS `huali_core_field_edit_log`;
CREATE TABLE `huali_core_field_edit_log`
(
    `id_`         bigint(20)                                             NOT NULL COMMENT 'ID',
    `class_name`  varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '类全名',
    `record_id`   bigint(20)                                             NOT NULL COMMENT '记录ID',
    `field_name`  varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NOT NULL COMMENT '字段名称',
    `old_value`   longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin     NOT NULL COMMENT '更新前的值',
    `new_value`   longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin     NOT NULL COMMENT '更新后的值',
    `deleted_`    tinyint(1)                                             NULL DEFAULT 0 COMMENT '逻辑删除',
    `create_by`   bigint(20)                                             NOT NULL COMMENT '上次更新人',
    `create_time` datetime(0)                                            NOT NULL COMMENT '上次更新时间',
    `update_by`   bigint(20)                                             NOT NULL COMMENT '本次更新人',
    `update_time` datetime(0)                                            NOT NULL COMMENT '本次更新时间',
    PRIMARY KEY (`id_`) USING BTREE,
    INDEX `class_name_index` (`class_name`) USING BTREE,
    INDEX `record_id_index` (`record_id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = '字段编辑日志表'
  ROW_FORMAT = Dynamic;