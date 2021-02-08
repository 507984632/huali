package com.huali.shiro.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.session.Session;

/**
 * @author myUser
 * @date 2021-02-07 19:57
 **/
@Slf4j
public class SessionListener implements org.apache.shiro.session.SessionListener {
    @Override
    public void onStart(Session session) {
        log.info("创建了一个Session连接:[" + session.getId() + "]");
    }

    @Override
    public void onStop(Session session) {
        log.info("销毁了一个Session连接:[" + session.getId() + "]");
    }

    @Override
    public void onExpiration(Session session) {
        log.info("过期了一个Session连接:[" + session.getId() + "]");
    }
}
