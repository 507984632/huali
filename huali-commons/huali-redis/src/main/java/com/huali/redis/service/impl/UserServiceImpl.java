package com.huali.redis.service.impl;

import com.huali.redis.mapper.UserMapper;
import com.huali.redis.model.User;
import com.huali.redis.service.UserService;
import com.huali.utils.exception.CheckedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author myUser
 * @date 2021-01-20 19:41
 **/
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User getUser(Long id) {
        User userById = userMapper.getUserById(id);
        if (Objects.isNull(userById)) {
            throw new CheckedException("该用户未注册");
        }
        return userById;
    }

    @Override
    public User getUserByUsername(String username) {
        return userMapper.getUserByUsername(username);
    }
}
