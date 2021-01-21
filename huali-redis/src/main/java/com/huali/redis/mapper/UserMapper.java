package com.huali.redis.mapper;

import com.huali.redis.model.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {
    User getUserById(Long id);
}
