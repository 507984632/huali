package com.huali.redis.mapper;

import com.huali.redis.model.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    User getUserById(Long id);

    User getUserByUsername(String username);
}
