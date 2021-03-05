package com.huali.io.netty.simple;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.SneakyThrows;

/**
 * @author Yang_my
 * @sine 2021/3/5
 */
public class NettyClient {
    public static void main(String[] args) throws Exception {

        // 客户端需要一个事件循环组
        EventLoopGroup eventExecutors = new NioEventLoopGroup();

        try {
            // 创建启动对象 根服务端 不一样 就跟 ServerSocket 和 Socket
            Bootstrap bootstrap = new Bootstrap();

            // 设置相关参数
            bootstrap
                    // 设置线程组
                    .group(eventExecutors)
                    // 设置客户端通道的实现 (反射)
                    .channel(NioSocketChannel.class)
                    // 添加处理器对象
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            // 添加自己的处理器对象
                            socketChannel.pipeline().addLast(new NettyClientHandler());
                        }
                    });

            System.out.println("客户端准备");

            // 启动客户端去链接服务端
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 6668).sync();

            // 对关闭通道进行监听
            channelFuture.channel().closeFuture().sync();

        } finally {
            // 对 eventExecutors 的线程组进行关闭
            eventExecutors.shutdownGracefully();
        }

    }
}
