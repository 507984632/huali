package com.huali.io.bio;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;

/**
 * 聊天室服务端
 *
 * @author soft01
 * @see Client 客户端
 */
public class Server {
    /**
     * 运行在服务端的ServerSocket	(可以理解为总机（服务器）)
     * 主要有两个作用：
     * 1：向系统申请服务端口，客户端就是通过这个端口与服务端建立连接的。
     * 2：监听该端口，这样当客户端建立连接时，ServerSocket就会自动
     * 实例化一个Socket，用于与该客户端进行数据交互。
     */
    private ServerSocket server;

    /**
     * 用来存放所有客户端数出流的共享数组
     * <p>
     * 内部类可以访问外部类的属性,因此在Server上定义这样一个数组
     * 可以保证存所有ChlientHandler需要互访的输出流,做到它们
     * 之间共享数据使用
     */
    private Collection<PrintWriter> allOut = new ArrayList<>();

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }

    /**
     * 用于初始化服务端
     */
    public Server() {
        try {
            /*
             * 实例化ServerSocket时传入打开的服务端口。
             * 如果该端口已经被其他应用程序使用则实例化过程会抛出异常
             */
            System.out.println("正在启动服务端...");
            server = new ServerSocket(8088);
            System.out.println("服务端启动完毕！");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 服务端开始工作的方法
     */
    public void start() {
        try {
            /*
             * ServerSocket提供的方法：
             * Socket accept();
             * 该方法是一个阻塞方法 (程序走到这里就不往下走了，跟Scanner类似)，调用后开始等待客户端连接，
             * 当一个客户端连接后该方法会返回i一个Socket实例，通过这个Socket实例即可与该客户交互。
             *
             * 多次调用accept方法可以接受多个客户端的连接
             */
            while (true) {
                System.out.println("等待客户端连接...");
                Socket socket = server.accept();
                System.out.println("一个客户端连接了!");
                /*
                 * 关于客户端某一个的工作细节
                 */
                ClientHandler handler = new ClientHandler(socket);
                new Thread(handler).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 客户端工作细节
     */
    public class ClientHandler implements Runnable {
        private Socket socket;
        /**
         * 计算机远端地址信息(客户端)
         */
        private String host;

        public ClientHandler(Socket socket) {
            this.socket = socket;
            InetAddress address = socket.getInetAddress();
            host = address.getHostAddress();
        }

        @Override
        public void run() {
            PrintWriter pw = null;
            try {
                /*
                 * InputStream getInputStream()
                 * 通过该输入流读取到内容是远程端计算机发送过来的数据
                 */
                InputStream in = socket.getInputStream();//输入流有了，写出其他的高级流
                InputStreamReader isr = new InputStreamReader(in, "UTF_8");//给定字符集
                BufferedReader br = new BufferedReader(isr);    //按行读

                /*
                 * 通过socket获取输出流，用于将消息发送给当前客户端
                 */
                OutputStream out = socket.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(out, "UTF_8");
                BufferedWriter bw = new BufferedWriter(osw);
                pw = new PrintWriter(bw, true);

                /*
                 * 将对应该客户端的输出流存入到共享数组中,
                 * 以便其他的Clienthandler可以访问到
                 * 存在多线程安全问题.
                 */
                synchronized (allOut) {
                    // 将客户端的输出流存到数组中
                    allOut.add(pw);
                }
                System.out.println(host + "上线了,当前在线人数为:" + allOut.size());

                /*
                 * 客户端由于操作系统不同，当客户端断开连接时，服务端这里的表现也不同：
                 * 通常windows客户端断开连接时，readLine方法这里会直接抛出异常。
                 * 而linux的客户端断开连接时，readLine方法会返回null。
                 */
                String line;
                while ((line = br.readLine()) != null) {
                    //接受用户传到服务端的信息
                    System.out.println(host + "说：" + line);
                    synchronized (allOut) {    //存在安全问题
                        //将消息发送给所有客户端
                        for (PrintWriter o : allOut) {
                            o.println(host + "说：" + line);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                synchronized (allOut) {    //存在安全线程问题
                    //处理客户端断开连接后的操作
                    allOut.remove(pw);
                }

                System.out.println(host + "下线了,当前在线人数为:" + allOut.size());
                try {
                    /*
                     * Socket提供的close方法可以与远端计算机断开连接
                     * 与此同时通过这个Socket获取的输入流与输出流也就关闭了
                     */
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
