package com.huali.redis.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author myUser
 * @date 2021-01-20 19:44
 **/
@Data
@AllArgsConstructor
public class User {
    private Long id;
    private String name;
}
