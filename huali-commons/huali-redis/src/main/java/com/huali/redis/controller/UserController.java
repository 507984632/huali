package com.huali.redis.controller;

import com.huali.redis.model.User;
import com.huali.redis.service.UserService;
import com.huali.redis.util.RedisUtil;
import com.huali.utils.DateUtil;
import com.huali.utils.base.BaseController;
import com.huali.utils.web.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author myUser
 * @date 2021-01-20 19:36
 **/
@RestController
@RequestMapping("user")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate<String, Serializable> redisTemplate;

    @GetMapping("get/{id}")
    public ResponseEntity<JsonResult<User>> getUser(@PathVariable Long id) {
        User user = userService.getUser(id);
        return success(user);
    }

    @GetMapping("createdUser/{id}")
    public ResponseEntity<JsonResult<?>> createdUser(@PathVariable Long id) {
        User user = new User(id, id + 1 + "", "123");
        // key 只能存储 string 类型的值
//        redisTemplate.opsForValue().set("2", user);
        String key = user.getClass().getName() + ":" + user.getId();

        redisTemplate.boundValueOps(key)
                .set(user, 15L, TimeUnit.DAYS);
        return success();
    }

    @GetMapping("getRedis/{id}")
    public ResponseEntity<JsonResult<User>> getRedisUser(@PathVariable Long id) {
//        return success((User) RedisUtil.get(new User().getClass().getName() + ":" + id));
        String key = User.class.getName() + ":" + id;
        return success((User) redisTemplate.boundValueOps(key).get());
    }

    @GetMapping("getExpire/{id}")
    public ResponseEntity<JsonResult<String>> getExpire(@PathVariable Long id) {
        return success(DateUtil.format(new Date(RedisUtil.getExpire(id.toString())), DateUtil.DateTimePattern.YYYY_MM_DD_HH_MM_SS));
    }

}
