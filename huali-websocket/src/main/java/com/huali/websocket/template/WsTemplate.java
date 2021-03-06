package com.huali.websocket.template;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * springboot 集成 websocket
 * 后简单的配置既可以跟前端交互了
 * -@ServerEndpoint 注解中的 value 属性需要跟前端对应，如果对应不上，则会链接不上 WebSocket
 * -路径中的 userId 可以替换成 shiro 中session 的id来对应，这样就可以直接拿到当前用户
 * -本类中所有 添加注解的方法都是可以直接在前端 直接通过前端的 websocket 直接访问到的方法
 *
 * @author Yang_my
 * @since 2021/3/6
 */
@Slf4j
//@Component
//@ServerEndpoint(value = "/connectWebSocket/{userId}")
public class WsTemplate {
    /**
     * 在线人数
     */
    public static AtomicInteger onlineNumber = new AtomicInteger(0);
    /**
     * 以用户的姓名为key，WebSocket为对象保存起来
     */
    private static final Map<String, Session> CLIENTS = new ConcurrentHashMap<>();

    /**
     * 建立连接
     *
     * @param session 客户端session
     */
    @OnOpen
    public void onOpen(@PathParam("userId") String userId, Session session) {
        onlineNumber.getAndIncrement();
        log.info("现在来连接的客户id：{},用户名：{}", session.getId(), userId);
        try {
            //把自己的信息加入到map当中去
            CLIENTS.put(userId, session);
            log.info("有连接创建！当前在线人数：{}", onlineNumber.get());
        } catch (Exception e) {
            log.info(userId + "上线的时候通知所有人发生了错误");
        }
    }

    /**
     * 出现异常信息时触发
     *
     * @param session 客户端session
     * @param error   错误信息
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.info("服务端发生了错误：{},SessionId：{}", error.getMessage(), session.getId());
        close(session);
    }

    /**
     * 断开链接，或关闭浏览器时触发
     *
     * @param session 客户端session
     */
    @OnClose
    public void onClose(Session session) {
        close(session);
    }

    /**
     * 收到客户端的消息
     *
     * @param message 消息
     * @param session 会话
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        try {
            log.info("来自客户端消息：" + message + "客户端的id是：" + session.getId());
            this.sendMessageAll(message, getClientKey(session));
        } catch (Exception e) {
            e.printStackTrace();
            log.warn("发生了错误了");
        }
    }


    /**
     * 单点发送
     *
     * @param message  消息
     * @param touserId 接收人Id
     */
    public void sendMessageTo(String message, String touserId) {
        Session session = CLIENTS.get(touserId);
        if (Objects.nonNull(session)) {
            // 向客户端发送消息
            session.getAsyncRemote().sendText(message);
        }
    }

    /**
     * 批量发送
     *
     * @param message    消息
     * @param fromuserId 发送人Id
     */
    public void sendMessageAll(String message, String fromuserId) {
        CLIENTS.forEach((k, v) -> {
            // 将自己排除发送的列表
            if (k.equals(fromuserId)) {
                return;
            }
            // 向客户端发送消息
            v.getAsyncRemote().sendText(message);
        });
    }

    /**
     * 实际关闭链接的操作
     *
     * @param session 客户端session
     */
    private static void close(Session session) {
        // 实际在线人数 -1
        onlineNumber.getAndDecrement();
        // 遍历 在线用户集合
        CLIENTS.forEach((k, v) -> {
            // 跟当前用户不匹配直接跳过
            if (!session.equals(v)) {
                return;
            }
            try {
                // 找到当前用户关闭 websocket 的链接
                session.close();
                // 将该用户从在线集合中删除
                CLIENTS.remove(k);
            } catch (IOException e) {
                e.printStackTrace();
                log.error("关闭客户端出现异常,sessionId：{}", session.getId());
            }
        });
    }

    /**
     * 通过 session 获得 key
     *
     * @param session 客户端session
     * @return key值
     */
    private static String getClientKey(Session session) {
        AtomicReference<String> key = new AtomicReference<>(null);
        CLIENTS.forEach((k, v) -> {
            if (v.equals(session)) {
                key.set(k);
            }
        });
        return key.get();
    }

}
