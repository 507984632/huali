package com.huali.io.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * 自定义 客户端 处理器 需要继承 ChannelInboundHandlerAdapter
 *
 * @author Yang_my
 * @sine 2021/3/5
 */
public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    /**
     * 当通道 处理就绪时触发该方法
     *
     * @param ctx 上下文
     * @throws Exception .
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("client ctx = " + ctx);
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello", CharsetUtil.UTF_8));
    }

    /**
     * 当通道有读取事件时触发
     *
     * @param ctx 上下文
     * @param msg 消息
     * @throws Exception .
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        System.out.println("服务器回复的消息：" + buf.toString(CharsetUtil.UTF_8));
        System.out.println("服务器地址：" + ctx.channel().remoteAddress());
    }

    /**
     * 处理异常的部分 一般是需要关闭通道
     *
     * @param ctx   上下文
     * @param cause 异常信息
     * @throws Exception .
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
