package com.huali.juc;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

class MyThread implements Callable<Integer> {
    @Override
    public Integer call() throws Exception {
        System.out.println("执行了");
        return 1024;
    }
}

/**
 * 另外一种创建 线程的方式
 * <p>
 * FutureTask 实现了 RunnableFuture 而 RunnableFuture 又实现了 Runnable 接口
 * 所以，FutureTask也就相当于实现了 Runnable 接口，
 * <p>
 * 又因为 FutureTask 构造函数中支持 Runnable 接口，又支持 CallAble 接口，
 * 所以可以使用这个类来创建线程
 * <p>
 * 创建线程的方式
 * 1. 继承 Thread 类
 * 2. 实现 Runnable 接口
 * 3. 实现 Callable 接口
 * 4. 通过线程池创建
 */
public class CallableDemo7 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        FutureTask<Integer> futureTask = new FutureTask(new MyThread());
        new Thread(futureTask, "A").start();
        Integer integer = futureTask.get();
        System.out.println(integer);
    }
}
