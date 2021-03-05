package com.huali.io.netty.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * 基于 Netty 的 WebSocket 实现长连接
 * 运行方式，执行main方法， 然后将 hello.html 通过 idea 运行起来，直接测试
 *
 * @author Yang_my
 * @sine 2021/3/5
 */
public class MyServer {
    public static void main(String[] args) throws Exception {
        // 创建 线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            // 创建服务端的启动对象，配置参数
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup);
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.handler(new LoggingHandler(LogLevel.INFO));
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    ChannelPipeline pipeline = socketChannel.pipeline();

                    // 因为是基于 http 协议的， 所以需要使用http 的编码和解码器
                    pipeline.addLast(new HttpServerCodec());
                    // 是以块方式写， 添加 ChunkedWriteHandler 处理器
                    pipeline.addLast(new ChunkedWriteHandler());
                    /*
                        1. HTTP 数据在传输过程中是分段的，HttpObjectAggregator 就是可以将多个段 聚合起来
                        2. 这就是为什么，当浏览器发送大量数据时，就会出现多次请求
                     */
                    pipeline.addLast(new HttpObjectAggregator(8192));
                    /*
                        1. 对于 WebSocket，他的数据是 以 帧(frame) 形式传递
                        2. 可以看到 WebSocketFrame 下面有六个自类
                        3. 浏览器请求时，ws://localhost:7000/hello 表示请求的url
                        4. WebSocketServerProtocolHandler 核心功能，
                            将HTTP协议 升级为 WS 协议，即 WebSocket 协议 保持长连接
                        5. 这里的请求路径必须跟 浏览器中的情路路径保持一直
                     */
                    pipeline.addLast(new WebSocketServerProtocolHandler("/"));
                    // 自定义的 handler 处理业务逻辑
                    pipeline.addLast(new MyTextWebSocketFrameHandler());
                }
            });
            // 启动服务器 并注册端口
            ChannelFuture channelFuture = serverBootstrap.bind(7000).sync();
            channelFuture.channel().closeFuture().sync();
        } finally {
            // 关闭 线程组
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}
