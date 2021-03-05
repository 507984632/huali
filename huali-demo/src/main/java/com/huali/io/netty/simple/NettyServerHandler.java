package com.huali.io.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * 自定义 服务端处理器 需要继承 HandlerAdapter
 *
 * @author Yang_my
 * @sine 2021/3/5
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 读取客户端发送的消息
     *
     * @param ctx 上下文对象 含有 管道pipeline 通道channel 地址等
     * @param msg 客户端发送的数据
     * @throws Exception .
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("服务器当前线程" + Thread.currentThread().getName());
        System.out.println("server ctx = " + ctx);
        /*
            ByteBuf 是 Netty 中的 字节流， ByteBuffer 是 NIO 中的 字节流 ，ByteBuf的效率更高一些
         */
        ByteBuf buf = (ByteBuf) msg;
        System.out.println("客户端发送消息：" + buf.toString(CharsetUtil.UTF_8));
        System.out.println("客户端地址：" + ctx.channel().remoteAddress());
    }

    /**
     * 数据读取完毕后的操作
     *
     * @param ctx 上下文对象
     * @throws Exception .
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        /*
            writeAndFlush 是 write 和 flush 的结合版本，
            一般来说，需要将发送的东西要进行编码
         */
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello 客户端！", CharsetUtil.UTF_8));
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
