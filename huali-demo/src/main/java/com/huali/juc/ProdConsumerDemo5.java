package com.huali.juc;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Aircodition {
    private int num = 0;

    //  新版 lock  先获得 Condition  然后使用 await() 等待， signalAll() 唤醒
    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    public void increment() throws Exception {
        lock.lock();
        try {
            // 1 判断
            while (num != 0) {
                condition.await(); // this.wait();
            }
            // 2.干活
            num++;
            System.out.println(Thread.currentThread().getName() + "\t" + num);
            // 3. 干活
            condition.signalAll(); // this.notifyAll();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void decrement() throws Exception {
        lock.lock();
        try {
            // 1 判断
            while (num != 0) {
                condition.await(); // this.wait();
            }
            // 2.干活
            num--;
            System.out.println(Thread.currentThread().getName() + "\t" + num);
            // 3. 干活
            condition.signalAll(); // this.notifyAll();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    // ------------------- synchronized  版本  wait() 让线程 等待， notifAll() 唤醒线程

//    public synchronized void increment() throws Exception {
//        // 1 判断
//        while (num != 0) {
//            this.wait();
//        }
//        // 2.干活
//        num++;
//        System.out.println(Thread.currentThread().getName() + "\t" + num);
//        // 3. 干活
//        this.notifyAll();
//    }
//
//    public synchronized void decrement() throws Exception {
//        while (num == 0) {
//            this.wait();
//        }
//        num--;
//        System.out.println(Thread.currentThread().getName() + "\t" + num);
//
//        this.notifyAll();
//    }

}

/**
 * 题目：现在两个线程，可以操作初始值为零的一个变量，
 * 实现一个线程对该变量加1，一个线程对该变量减1，
 * 实现交替，来10轮，变量初始值为0
 * 1. 高聚低合前提下， 线程操作资源类
 * 2. 判断/干活/通知
 * 3. 防止虚假唤醒 需要在使用 wait 时使用 while 循环判断
 * <p>
 * <p>
 * 总结：多线程之间调用 应该使用 while 判断 + 新版锁 lock
 */
public class ProdConsumerDemo5 {
    public static void main(String[] args) throws Exception {
        Aircodition a = new Aircodition();
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    a.increment();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "A").start();

        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    a.decrement();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "B").start();

        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    a.increment();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "C").start();

        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    a.decrement();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "D").start();
    }
}
