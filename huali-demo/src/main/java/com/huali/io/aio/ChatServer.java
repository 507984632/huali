package com.huali.io.aio;


import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ChatServer {

    private static final String LOCALHOST = "localhost";
    private static final int DEFAULT_PORT = 8888;
    private static final String EXIT = "exit";
    private static final int BUFFER = 1024;
    private static final int THREADPOOL_SIZE = 8;
    private AsynchronousChannelGroup channelGroup;
    private AsynchronousServerSocketChannel serverSocketChannel;
    private List<ClientHandler> connectedClients;
    private Charset charset = Charset.forName("UTF-8");
    private int port;

    public ChatServer(){
        this(DEFAULT_PORT);
    }
    public ChatServer(int port){
        this.port = port;
        this.connectedClients = new ArrayList<>();
    }

    /**
     * 准备退出
     * @param msg
     * @return
     */
    private boolean readyToQuit(String msg){
        return EXIT.equals(msg);
    }

    /**
     * 关闭此流并释放与之相关联的任何系统资源，如果流已经关闭，则调用此方法将不起作用。
     * @param closeable
     */
    private void close(Closeable closeable){
        if(closeable != null){
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 往客户端列表里添加一个新的客户端
     * @param clientHandler
     */
    private synchronized void addClient(ClientHandler clientHandler) {
        connectedClients.add(clientHandler);
        System.out.println(getClientName(clientHandler.clientChannel) + "已经连接到服务器");
    }

    /**
     * 将客户端链表里移除一个断开的客户端，同时关闭连接
     * @param clientHandler
     */
    private synchronized void removeClient(ClientHandler clientHandler) {
        connectedClients.remove(clientHandler);
        System.out.println(getClientName(clientHandler.clientChannel) + "已断开连接");
        close(clientHandler.clientChannel);
    }

    /**
     * 接收消息，并解码
     * @param byteBuffer
     * @return
     */
    private String receive(ByteBuffer byteBuffer){
        CharBuffer charBuffer = charset.decode(byteBuffer);
        return String.valueOf(charBuffer);
    }

    /**
     * 获取客户端的信息
     * @param clientChannel
     * @return
     */
    private String getClientName(AsynchronousSocketChannel clientChannel) {
        int clientPort = -1;
        try {
            InetSocketAddress inetSocketAddress = (InetSocketAddress) clientChannel.getRemoteAddress();
            clientPort = inetSocketAddress.getPort();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "客户端[" + clientPort + "]";
    }

    /**
     * 转发客户端的消息
     * @param clientChannel
     * @param fwdMsg
     */
    private synchronized void forwardMessage(AsynchronousSocketChannel clientChannel, String fwdMsg) {
        for (ClientHandler handler : connectedClients){
            if (!handler.clientChannel.equals(clientChannel)){
                try { // 防止发生意想不到的错误或异常
                    ByteBuffer byteBuffer = charset.encode(getClientName(handler.clientChannel) + ":" + fwdMsg);
                    handler.clientChannel.write(byteBuffer, null, handler);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 当客户端连接时，用于接收客户端请求的Handler
     */
    private class AcceptHandler implements
            CompletionHandler<AsynchronousSocketChannel, Object>{
        @Override
        public void completed(AsynchronousSocketChannel clientChannel, Object attachment) {
            if (serverSocketChannel.isOpen()){ //保证serverSocketChannel继续去监听
                serverSocketChannel.accept(null, this);
            }
            if (clientChannel != null && clientChannel.isOpen()){
                ClientHandler clientHandler = new ClientHandler(clientChannel);
                ByteBuffer byteBuffer = ByteBuffer.allocate(BUFFER);
                // 将新用户添加到在线用户列表
                addClient(clientHandler);
                //启动异步读操作，以从该通道读取到给定缓冲器中的字节序列
                clientChannel.read(byteBuffer, byteBuffer, clientHandler);
            }
        }

        @Override
        public void failed(Throwable exc, Object attachment) {
            System.out.println("连接失败:" + exc);
        }
    }

    /**
     * 处理客户端事件
     */
    private class ClientHandler implements
            CompletionHandler<Integer, Object>{

        private AsynchronousSocketChannel clientChannel;
        public ClientHandler(AsynchronousSocketChannel channel){
            this.clientChannel = channel;
        }
        @Override
        public void completed(Integer result, Object attachment) {
            ByteBuffer byteBuffer = (ByteBuffer)attachment;
            if (byteBuffer != null){  // 如果发生了读事件
                if (result <= 0){
                    // 客户端通道发生了异常
                    //  将客户从在线客户列表中去除
                    removeClient(this);
                }else {
                    byteBuffer.flip(); //转换为读操作
                    String fwdMsg = receive(byteBuffer); //读取buffer里的消息
                    System.out.println(getClientName(clientChannel) + ":" +fwdMsg);
                    forwardMessage(clientChannel, fwdMsg); //向其他用户转发消息
                    byteBuffer.clear();//清除缓冲区

                    // 检查用户是否退出
                    if (readyToQuit(fwdMsg)){
                        //  将客户从在线客户列表中去除
                        removeClient(this);
                    }else {
                        clientChannel.read(byteBuffer,byteBuffer,this);
                    }
                }
            }
        }

        @Override
        public void failed(Throwable exc, Object attachment) {
            System.out.println("读写失败: " + exc);
        }
    }

    /**
     * 启动服务器
     */
    private void start(){
        try {
            //初始化线程池
            ExecutorService executorService = Executors.newFixedThreadPool(THREADPOOL_SIZE);
            //将线程池加入到异步通道，进行资源共享
            channelGroup = AsynchronousChannelGroup.withThreadPool(executorService);
            serverSocketChannel = AsynchronousServerSocketChannel.open(channelGroup);
            serverSocketChannel.bind(new InetSocketAddress(LOCALHOST, port));
            System.out.println("启动服务器，监听端口："+ port);

            while (true){
                serverSocketChannel.accept(null, new AcceptHandler());
                System.in.read();//阻塞调用，防止占用系统资源，一直调用accept()函数
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            close(serverSocketChannel);
        }
    }

    /**
     * 主函数入口
     * @param args
     */
    public static void main(String[] args) {
        ChatServer server = new ChatServer(7777);
        server.start();
    }
}