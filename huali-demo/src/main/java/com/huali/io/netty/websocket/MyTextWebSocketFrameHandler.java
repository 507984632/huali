package com.huali.io.netty.websocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

import java.time.LocalDateTime;

/**
 * @author Yang_my
 * @sine 2021/3/5
 * @see WebSocketFrame 抽象类， 该类下有六个子类，分别用于不同的场景
 * @see TextWebSocketFrame 类型，表示一个文本类型帧(frame)
 */
public class MyTextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    /**
     * 当 web 客户端链接时 触发方法
     *
     * @param ctx 上下文
     * @throws Exception .
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        // id 表示唯一的值，LongText 是唯一的， ShortText 不是唯一的
        System.out.println("handlerAdded 被调用" + ctx.channel().id().asLongText());
        System.out.println("handlerAdded 被调用" + ctx.channel().id().asShortText());
    }

    /**
     * 当 web 客户端 离线时调用
     *
     * @param ctx 上下文
     * @throws Exception .
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        // id 表示唯一的值，LongText 是唯一的， ShortText 不是唯一的
        System.out.println("handlerRemoved 被调用" + ctx.channel().id().asLongText());
        System.out.println("handlerRemoved 被调用" + ctx.channel().id().asShortText());
    }

    /**
     * 发生异常时触发 一般是关闭链接使用
     *
     * @param ctx   上下文
     * @param cause 异常信息
     * @throws Exception .
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.err.println("异常发生" + cause.getMessage());
        // 关闭链接
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        System.out.println("服务端收到消息：" + msg.text());

        // 回复消息
        ctx.channel().writeAndFlush(new TextWebSocketFrame("服务器时间" + LocalDateTime.now() + " " + msg.text()));
    }


}
