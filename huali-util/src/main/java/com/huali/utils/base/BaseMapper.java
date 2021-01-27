package com.huali.utils.base;

import org.apache.ibatis.annotations.Select;

/**
 * @author myUser
 * @date 2021-01-23 16:19
 **/
public interface BaseMapper<T extends BaseModel> extends com.baomidou.mybatisplus.core.mapper.BaseMapper<T> {

    /**
     * 查询上一条sql的总行数
     *
     * 注：1、要开启事务
     *    2、查询sql中需要加上 SQL_CALC_FOUND_ROWS
     *
     * @return 总行数
     */
    @Select("SELECT found_rows()")
    int queryTotal();

}
