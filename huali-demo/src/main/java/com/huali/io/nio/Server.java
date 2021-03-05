package com.huali.io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

/**
 * 服务端实现 NIO 非阻塞聊天系统
 *
 * @author Yang_my
 * @sine 2021/3/5
 */
public class Server {

    /**
     * 定义成员属性，选择器，服务端通道，端口
     */
    private Selector selector;
    private ServerSocketChannel ssc;
    private static final Integer PORT = 9999;

    /**
     * 初始化 服务端
     */
    public Server() {
        try {
            // 创建选择器对象
            selector = Selector.open();
            // 获得通道
            ssc = ServerSocketChannel.open();
            // 绑定客户端连接端口
            ssc.bind(new InetSocketAddress(PORT));
            // 设置非阻塞通讯模式
            ssc.configureBlocking(false);
            // 把通道注册到选择器上，并开始指定接受链接事件
            ssc.register(selector, SelectionKey.OP_ACCEPT);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 开始监听
     */
    private void listen() {
        try {
            // 循环去操作， 大于0 说明监听到事件触发
            while (selector.select() > 0) {
                // 获得选择器中所有注册通道的就绪事件
                Iterator<SelectionKey> it = selector.selectedKeys().iterator();
                // 遍历事件
                while (it.hasNext()) {
                    // 提取事件
                    SelectionKey sk = it.next();
                    // 判断这个事件的类型
                    if (sk.isAcceptable()) {
                        // acceptable 是客户端的接入请求 相当于登录
                        // 获得当前登录的客户端通道
                        SocketChannel sChannel = ssc.accept();
                        // 注册成非阻塞 模式
                        sChannel.configureBlocking(false);
                        // 注册给选择器，监听读数据的事件
                        sChannel.register(selector, SelectionKey.OP_READ);
                        System.out.println("客户端登录:" + sChannel.getRemoteAddress());

                    } else if (sk.isReadable()) {
                        // readable 是客户端发送的读取 事件 接受并实现转发到所有客户端
                        readClientData(sk);

                    }
                    // 处理完成后， 移除当前事件
                    it.remove();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 接受客户端通道信息， 并转发到所有客户端通道中
     *
     * @param sk 当前客户端的通道
     */
    private void readClientData(SelectionKey sk) {
        SocketChannel sChannel = null;
        try {
            // 直接得到当前客户端通道
            sChannel = (SocketChannel) sk.channel();
            // 创建缓冲区对象，开始接受客户端通道数据
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int count = sChannel.read(buffer);
            // 判断是否读取到信息
            if (count > 0) {
                // 提取读取到的信息
                String msg = new String(buffer.array(), 0, buffer.remaining());
                System.out.println("服务端接受消息:" + msg);
                // 把消息推送给全部客户端
                sendMsgToAllClient(msg, sChannel);
            }
        } catch (Exception e) {
            // 当前客户端离线
            try {
                System.out.println("有人离线:" + sChannel.getRemoteAddress());
                // 将事件 取消注册，并关闭客户端通道
                sk.cancel();
                sChannel.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }

    }


    /**
     * 把当前客户端消息推送给 当前全部在线注册的 channel
     *
     * @param msg      消息内容
     * @param sChannel 当前客户端通道
     */
    private void sendMsgToAllClient(String msg, SocketChannel sChannel) throws IOException {
        System.out.println("服务端开始转发这个消息：当前处理线[" + Thread.currentThread().getName() + "]");
        // 遍历所有注册通道的key
        for (SelectionKey key : selector.keys()) {
            // 通过 key 来得到这个客户端通道
            Channel channel = key.channel();
            // 判断遍历的当前通道是否是发起人
            if (channel instanceof SocketChannel && channel != sChannel) {
                ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
                ((SocketChannel) channel).write(buffer);
            }
        }
    }

    public static void main(String[] args) {
        // 执行空参构造，初始化服务端
        Server server = new Server();
        // 开始监听客户端的各种消息事件：链接，群聊，离线
        System.out.println("服务端初始化完成，等待接受任务");
        server.listen();
    }
}
