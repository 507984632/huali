package com.huali.io.netty.simple;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author Yang_my
 * @sine 2021/3/5
 */
public class NettyServer {
    public static void main(String[] args) throws Exception {
        /*
            1. 创建 两个线程组   bossGroup 和 workerGroup
            2. bossGroup 只是处理链接请求， 真正的和客户端业务处理都在 workerGroup 中完成
            3. 两个内部都是无限循环
         */
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {

            // 创建服务端的启动对象，配置参数
            ServerBootstrap bootstrap = new ServerBootstrap();
            // 设置两个线程组
            bootstrap.group(bossGroup, workerGroup)
                    // 设置 NioSocketChannel 作为服务器的通道实现
                    .channel(NioServerSocketChannel.class)
                    // 设置线程队列得到链接个数
                    .option(ChannelOption.SO_BACKLOG, 128)
                    // 设置保持活动链接状态
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    // 给 workerGroup 的 EventLoop 对应的管道设置处理器
                    // 并创建了一个通道测试对象(匿名对象)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        // 给pipeline 设置处理器
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new NettyServerHandler());
                        }
                    });

            System.out.println("服务端准备");

            // 绑定一个端口并且同步，生成了一个 ChannelFuture 对象
            // 启动服务器（并绑定端口）
            ChannelFuture cf = bootstrap.bind(6668).sync();

            // 对关闭通道进行监听
            cf.channel().closeFuture().sync();
        } finally {
            // 关闭 Netty 的线程组
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}
