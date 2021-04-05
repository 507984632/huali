package com.huali.redis.service;

import com.huali.redis.model.User;

public interface UserService {

    User getUser(Long id);

    User getUserByUsername(String username);
}
