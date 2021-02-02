package com.huali.jvm;

import java.util.Random;

/**
 * jvm 内存对象 Runtime 对象
 */
public class Demo2 {
    public static void main(String[] args) {
        System.out.println("CPU 核数：" + Runtime.getRuntime().availableProcessors());

        /**
         * JVM 最大内存 为 物理内存的 1/4
         */
        long maxMemory = Runtime.getRuntime().maxMemory();
        System.out.println("JVM 最大内存(-Mmx:MAX_MEMORY)：" + maxMemory + "(B)，大约：" + (maxMemory / (double) 1024 / 1024) + "MB");

        /**
         * 这个就是初始内存，生产环境 这两个值必须保证一样，
         * 因为要保证 JVM 的内存不能忽高忽低
         */
        long totalMemory = Runtime.getRuntime().totalMemory();
        System.out.println("JVM 初始内存(-Mms:TOTAL_MEMORY)：" + totalMemory + "(B)，大约：" + (totalMemory / (double) 1024 / 1024) + "MB");

        /**
         * 在 菜单 Run -> Edit Configurations -> 选择启动类 -> VM options 中配置如下信息
         * -Xms1024m -Xmx1024m -XX:+PrintGCDetails
         * -Xms1024m：JVM 初始内存
         * -Xmx1024m：JVM 最大内存
         * -XX:+PrintGCDetails：打印GC的执行过程
         */
        //byte[] bytes = new byte[40 * 1024 * 1024]; //或者直接 new 一个大对象

        String str = "测试OOM";
        while (true) {
            str += str + new Random().nextInt(800000) + new Random().nextInt(800000);
        }
    }
}
