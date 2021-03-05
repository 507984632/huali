package com.huali.io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 客户端实现 NIO 非阻塞聊天系统
 *
 * @author Yang_my
 * @sine 2021/3/5
 */
public class Client {
    /**
     * 定义客户端相关属性 选择器，客户端通道，端口
     */
    private Selector selector;
    private SocketChannel sChannel;
    public static final Integer PORT = 9999;

    /**
     * 创建线程池来操作
     */
    private static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 1, 1,
            TimeUnit.SECONDS, new LinkedBlockingQueue<>(),
            Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());


    /**
     * 初始化客户端
     */
    public Client() {
        try {
            // 创建客户端选择器
            selector = Selector.open();
            // 链接服务端
            sChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", PORT));
            // 设置非阻塞模式
            sChannel.configureBlocking(false);
            // 将通道注册到选择器
            sChannel.register(selector, SelectionKey.OP_READ);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送消息到服务端
     *
     * @param msg 消息
     */
    private void sendToServer(String msg) {
        try {
            sChannel.write(ByteBuffer.wrap(("‘客户端名称’ 的消息：[" + msg + "]").getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取服务端发送过来的消息
     */
    private void readInfo() throws IOException {
        // 循环判断是否有消息
        while (selector.select() > 0) {
            // 通过选择器遍历所有的 事件 key 并遍历
            Iterator<SelectionKey> it = selector.selectedKeys().iterator();
            while (it.hasNext()) {
                // 获得当前事件
                SelectionKey sk = it.next();
                // 判断事件的类型是否是服务端 发送过来的消息
                if (sk.isReadable()) {
                    // 获得该通道
                    SocketChannel sc = (SocketChannel) sk.channel();
                    // 创建缓存流读取消息
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    sc.read(buffer);
                    System.out.println("客户端读取到了消息:" + new String(buffer.array()).trim());
                }
                // 移除事件
                it.remove();
            }
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
        System.out.println("客户端初始化完成，等待接受任务");

        threadPoolExecutor.execute(() -> {
            try {
                client.readInfo();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // 负责写
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String msg = scanner.next();
            client.sendToServer(msg);
        }
    }

}
