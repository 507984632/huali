package com.huali.utils.enums;

/**
 * 自定义缓存策略
 */
public enum EntityCacheableStrategy {

    /**
     * 查询：直接缓存
     * 新增和更新：事务提交后缓存
     * 删除时：删除缓存
     */
    ALWAYS,
    /**
     * 查询：如果没有开启事务，则缓存
     * 新增和更新：事务提交后删除
     * 删除时：删除缓存
     */
    NON_TRANSACTIONAL
}
