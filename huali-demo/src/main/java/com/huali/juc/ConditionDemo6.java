package com.huali.juc;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class ShareData {
    // 标志位
    private int num = 1; //A:1 B:2 C:3
    private Lock lock = new ReentrantLock();
    private Condition c1 = lock.newCondition();
    private Condition c2 = lock.newCondition();
    private Condition c3 = lock.newCondition();

    public void print5() {
        lock.lock();
        try {
            // 判断
            while (num != 1) {
                c1.await();
            }
            // 干活
            for (int i = 0; i < 5; i++) {
                System.out.println((i + 1) + "AA");
            }
            // 通知
            num = 2;// 先更改标志位
            c2.signal();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void print10() {
        lock.lock();
        try {
            // 判断
            while (num != 2) {
                c2.await();
            }
            // 干活
            for (int i = 0; i < 10; i++) {
                System.out.println((i + 1) + "BB");
            }
            // 通知
            num = 3;// 先更改标志位
            c3.signal();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void print15() {
        lock.lock();
        try {
            // 判断
            while (num != 3) {
                c3.await();
            }
            // 干活
            for (int i = 0; i < 15; i++) {
                System.out.println((i + 1) + "CC");
            }
            // 通知
            num = 1;// 先更改标志位
            c1.signal();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}


/**
 * 备注：多线程之间按顺序调用，实现 A -> B -> C
 * 三个线程启动，要求如下：
 * <p>
 * AA 打印5次，BB 打印10次，CC 打印15次
 * 接着
 * AA 打印5次，BB 打印10次，CC 打印15次
 * 来10轮
 * <p>
 * 通过锁的 newCondition(); 可以配置多个要是， 每一个要是相当于一个线程，可以通过这个单独唤醒某一个等待的 线程
 */
public class ConditionDemo6 {
    public static void main(String[] args) {
        ShareData shareData = new ShareData();
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                shareData.print5();
            }
        }, "A").start();
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                shareData.print10();
            }
        }, "B").start();
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                shareData.print15();
            }
        }, "C").start();

    }
}
