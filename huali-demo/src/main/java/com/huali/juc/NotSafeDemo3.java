package com.huali.juc;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 写时复制类
 * 读写分离的技术
 */
public class NotSafeDemo3 {
    public static void main(String[] args) {
//        a();
//        b();
//        c();
        d();
    }

    public void interviewQuestions() {

      /*
        1. ArrayList 默认就是一个Object 的数组， 初始就是一个空引用，只有在add 元素的时候才开辟
            10 元素的空间，
        2. ArrayList 的扩容是使用的 Arrays.copyOf 的方法，默认扩容为原集合的一半(值求整数)，即第一次扩容为 10/2 = 5 +10 = 15 的空间
            第二次为 15/2 = 7 + 15 = 22 的空间，
        3. ArrayList 是线程不安全的，可以通过 多个线程同时添加元素到一个集合中测试，有时会报出 concurrentModificationException 的异常，同时修改异常
        4. 可以采用 Collections 集合工具类中 将集合变成线程安全的方法进行获取集合。也可以采用juc中 CopyOnWriteArrayList (写时复制) 来解决
        5. CopyOnWriteArrayList 简单理解就是将 读写分离，读取的时候都是以最新的数据进行读取，然后在修改(以添加为例)的时候，先拿到锁，
            然后将原集合进行复制并扩容1个空间大小，用来存储最新的数据，最后成功之后将 原集合对象的引用地址换成最新集合地址就达到了写时复制的效果
      */
    }

    /**
     * ArrayList 线程不安全的案例
     *
     * @see java.util.ConcurrentModificationException 同时修改的异常
     */
    public static void a() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            new Thread(() -> {
                list.add(UUID.randomUUID().toString().substring(0, 8));
                System.out.println(list);
            }, String.valueOf(i)).start();
        }
    }

    /**
     * 使用线程安全的类即可
     * 这个实现的方式就是将add 元素的方法变成同步方法， 就是加上 同步锁
     */
    public static void b() {
        List<String> list = new Vector<>();
        for (int i = 0; i < 30; i++) {
            new Thread(() -> {
                list.add(UUID.randomUUID().toString().substring(0, 8));
                System.out.println(list);
            }, String.valueOf(i)).start();
        }
    }

    /**
     * 通过集合工具类 collections 将 arraylist 转变成加所的集合即可
     * 通过 collections 的工具类中的 方法可以知道所有的 map list set 都是线程不不安全的
     */
    public static void c() {
        List<String> list = Collections.synchronizedList(new ArrayList<>());
        for (int i = 0; i < 30; i++) {
            new Thread(() -> {
                list.add(UUID.randomUUID().toString().substring(0, 8));
                System.out.println(list);
            }, String.valueOf(i)).start();
        }
    }

    /**
     * 通过juc 中的写时复制类来实现
     * 实现方式
     * 读取的都是最新的集合的地址，最新的集合对象，
     * 但是在有人修改的时候，先获得一把锁，然后在通过 Arrays.copyOf 的方法将集合对象复制并扩容1
     * 修改完成后在将修改后的集合对象 重新赋值给原集合对象，这样读到的就是最新的数据
     */
    public static void d() {
        List<String> list = new CopyOnWriteArrayList<>();
        for (int i = 0; i < 30; i++) {
            new Thread(() -> {
                list.add(UUID.randomUUID().toString().substring(0, 8));
                System.out.println(list);
            }, String.valueOf(i)).start();
        }
    }


}
