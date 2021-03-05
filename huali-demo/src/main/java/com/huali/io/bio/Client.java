package com.huali.io.bio;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * 聊天室客户端
 *
 * @author soft01
 * @see Server 服务端
 */
public class Client {
    Scanner scan = new Scanner(System.in);
    /**
     * java.net.Scoket;（套接字）
     * 封装了TCP协议的通讯信息，使得我们利用了TCP
     * 通讯以流的读写操作形式完成
     */
    private Socket socket;

    //主方法
    public static void main(String[] args) {
        Client client = new Client();
        client.start();
    }

    /**
     * 构造方法，用于初始化客户端
     */
    public Client() {
        try {
            /*
             * 实例化Scocket时，需要传入两个参数：
             * 1：服务端的地址信息（IP地址）
             * 2：服务端打开的端口号
             *
             * 这里实例化Socket的过程就是通过给定地址链接服务端的过程
             * ，若连接不成功构造方法会抛出异常。通过IP地址我们可以找到
             * 网络上服务端所在的机器，通过端口就可以找到运行在服务端计
             * 算机上的服务端应用程序了
             */
            System.out.println("正在连接服务端...");
            //没有服务端的话会显示拒绝链接，因为就像打电话打空号一样
            socket = new Socket("localhost", 8088);
            System.out.println("已连接服务端!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 客户端开始工作的方法
     */
    public void start() {
        try {
            /*
             * 开始简历链接的时候就去创建一个线程 读取服务器发送过来的消息
             */
            ServerHandler handler = new ServerHandler();
            Thread t = new Thread(handler);
            t.start();

            /*
             * Socket提供的方法
             * OutputSream getOutputStream()
             * 通过返回的字节输出流写出的字节会通过网络发给远端计算机。
             */
            OutputStream out = socket.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(out, "UTF_8");
            BufferedWriter bw = new BufferedWriter(osw);
            //加true是为了让按行读按行写
            PrintWriter pw = new PrintWriter(bw, true);
            System.out.println("请输入内容！");

            while (true) {
                String info = scan.nextLine();
                pw.println(info);
                pw.flush();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 该线程负责循环读取服务端发送过来的消息并输出到客户端控制台上
     *
     * @author soft01
     */
    public class ServerHandler implements Runnable {
        @Override
        public void run() {
            try {
                /*
                 * 通过socket获取输入流，读取服务器端发送过来的消息
                 */
                InputStream in = socket.getInputStream();
                InputStreamReader isr = new InputStreamReader(in, "UTF_8");
                BufferedReader br = new BufferedReader(isr);
                String line = null;
                while ((line = br.readLine()) != null) {//让自己输入的消息不显示在自己的控制台
                    String[] l = line.split("说");
                    if ("192.168.31.71".equals(l[0])) {
                        continue;
                    }
                    System.out.println(line);
                }

            } catch (Exception e) {
            }
        }
    }


}
