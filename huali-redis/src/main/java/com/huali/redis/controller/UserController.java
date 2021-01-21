package com.huali.redis.controller;

import com.huali.redis.model.User;
import com.huali.redis.service.UserService;
import com.huali.utils.base.BaseController;
import com.huali.utils.web.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author myUser
 * @date 2021-01-20 19:36
 **/
@RestController
@RequestMapping("user")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @RequestMapping("get/{id}")
    public ResponseEntity<JsonResult<User>> getUser(@PathVariable Long id) {
        User user = userService.getUser(id);
        return success(user);
    }

}
