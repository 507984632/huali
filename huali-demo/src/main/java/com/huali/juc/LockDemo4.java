package com.huali.juc;

import java.util.concurrent.TimeUnit;

/**
 * 1  标准访问，请问先打印邮件还是短信
 * 2  暂停4秒钟在邮件方法，请问先打印邮件还是短信
 * 3  新增普通 sayHeLlo方法，请问先打印邮件还是 hello
 * 4  两部手机，请问先打印邮件还是短信
 * 5  两个静态同步方法，同一部手机，请问先打印邮件还是短信
 * 6  两个静态同步方法，2部手机，请问先打印邮件还是短信
 * 7  1个静态同步方法，1个普通同步方法同一部手机，请问先打印邮件还是短信
 * 8  1个静态同步方法，1个普通同步方法2部手机，请问先打印邮件还是短
 */
public class LockDemo4 {
    public static void main(String[] args) {
        LockDemo4 lockDemo4 = new LockDemo4();
        int num = 7;
        /**
         *  1.2 题 说明
         *      一个对象里面如果又多个 synchronized 方法，某一个时刻内，只要又一个线程去调用其中的一个 synchronized 方法了，
         *      其他的线程都只能等待，换句话说，某一个时刻内，只能有唯一一个线程去访问这些 synchronized 方法
         *
         *      原因，锁的是当前对象 this ，被锁定后，其它线程都不能进入到当前对象的其它 synchronized 方法
         *
         *  3. 题 说明
         *      访问普通方法，跟同步锁无关，上面解释的很清楚，某一个时刻内，只能有唯一一个线程去访问这些 synchronized 方法
         *      换句话说  如果访问的是普通方法，则无需等待释放对象
         *
         *  4. 题 说明
         *      如果访问的是两个不同的资源类，就看谁先被分配到了 ，但是这里发送邮件的操作需要等4秒，所以短信先打印
         *
         *  5.6 题 说明
         *      static synchronized (全局锁 锁的是 Class) 和 synchronized (对象锁 锁的是this) ，
         *      前者是无需关系多少个资源类，只要被访问了，都需要其他访问资源的线程等待
         *      而后者是在同一个资源下，让线程等待
         *      这两个案例中方法都获得的是 全局锁，锁的都是 Class
         *
         *  7. 题 说明
         *      邮件方法锁的是Class 对象，而 短信方法锁的是实例对象, 是不同的锁，所以是短信 先打印
         *
         *  8. 题 说明
         *      锁的都是 Class 对象, 但是 是不同的 实例对象 , 所以先打印的是短信
         *
         * 总结：
         *      就看两个线程 锁的对象， 如果都是全局锁，则等待，如果一个是全局锁，一个是对象锁，则无需等待全局锁
         *
         */
        switch (num) {
            case 1:
                lockDemo4.test1();
                break;
            case 2:
                lockDemo4.test2();
                break;
            case 3:
                lockDemo4.test3();
                break;
            case 4:
                lockDemo4.test4();
                break;
            case 5:
                LockDemo4.test5();
                break;
            case 6:
                lockDemo4.test6();
                break;
            case 7:
                lockDemo4.test7();
                break;
            case 8:
                lockDemo4.test8();
                break;
            default:
        }

    }

    /**
     * 1. 标准访问，请问先打印邮件还是短信
     */
    public void test1() {
        Phone1 phone = new Phone1();
        new Thread(() -> {
            phone.sendEmail();
        }, "A").start();
        new Thread(() -> {
            phone.sendSMS();
        }, "B").start();
    }

    /**
     * 2. 暂停4秒在邮件方法，请问先打印那个
     */
    public void test2() {
        Phone2 phone = new Phone2();
        new Thread(() -> {
            phone.sendEmail();
        }, "A").start();
        new Thread(() -> {
            phone.sendSMS();
        }, "B").start();
    }

    /**
     * 3.新增普通 sayHeLlo方法，请问先打印邮件还是 hello
     */
    public void test3() {
        Phone3 phone = new Phone3();
        new Thread(() -> {
            phone.sendEmail();
        }, "A").start();
        new Thread(() -> {
            phone.sayHello();
        }, "B").start();
    }

    /**
     * 4.两部手机，请问先打印邮件还是短信
     */
    public void test4() {
        Phone4 p1 = new Phone4();
        Phone4 p2 = new Phone4();
        new Thread(() -> {
            p1.sendEmail();
        }, "A").start();
        new Thread(() -> {
            p2.sendSMS();
        }, "B").start();
    }

    /**
     * 5.两个静态同步方法，同一部手机，请问先打印邮件还是短信
     */
    public static void test5() {
        Phone5 phone = new Phone5();
        new Thread(() -> {
            phone.sendEmail();
        }, "A").start();
        new Thread(() -> {
            phone.sendSMS();
        }, "B").start();
    }

    /**
     * 6.两个静态同步方法，2部手机，请问先打印邮件还是短信
     */
    public void test6() {
        Phone6 p1 = new Phone6();
        Phone6 p2 = new Phone6();
        new Thread(() -> {
            p1.sendEmail();
        }, "A").start();
        new Thread(() -> {
            p2.sendSMS();
        }, "B").start();
    }

    /**
     * 7.1个静态同步方法，1个普通同步方法同一部手机，请问先打印邮件还是短信
     */
    public void test7() {
        Phone7 p1 = new Phone7();
        new Thread(() -> {
            p1.sendEmail();
        }, "A").start();
        new Thread(() -> {
            p1.sendSMS();
        }, "B").start();
    }

    /**
     * 8.1个静态同步方法，1个普通同步方法2部手机，请问先打印邮件还是短
     */
    public void test8() {
        Phone8 p1 = new Phone8();
        Phone8 p2 = new Phone8();
        new Thread(() -> {
            p1.sendEmail();
        }, "A").start();
        new Thread(() -> {
            p2.sendSMS();
        }, "B").start();
    }

}

class Phone1 {
    public synchronized void sendEmail() {
        System.out.println("********SendEmail");
    }

    public synchronized void sendSMS() {
        System.out.println("********SendSMS");
    }
}

class Phone2 {
    public synchronized void sendEmail() {
        try {
            TimeUnit.SECONDS.sleep(4);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("********SendEmail");
    }

    public synchronized void sendSMS() {
        System.out.println("********SendSMS");
    }
}

class Phone3 {
    public synchronized void sendEmail() {
        try {
            TimeUnit.SECONDS.sleep(4);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("********SendEmail");
    }

    public void sayHello() {
        System.out.println("********Hello");
    }
}

class Phone4 {
    public synchronized void sendEmail() {
        try {
            TimeUnit.SECONDS.sleep(4);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("********SendEmail");
    }

    public synchronized void sendSMS() {
        System.out.println("********SendSMS");
    }
}

class Phone5 {
    public static synchronized void sendEmail() {
        try {
            TimeUnit.SECONDS.sleep(4);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("********SendEmail");
    }

    public static synchronized void sendSMS() {
        System.out.println("********SendSMS");
    }
}

class Phone6 {
    public static synchronized void sendEmail() {
        try {
            TimeUnit.SECONDS.sleep(4);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("********SendEmail");
    }

    public static synchronized void sendSMS() {
        System.out.println("********SendSMS");
    }
}

class Phone7 {
    public static synchronized void sendEmail() {
        try {
            TimeUnit.SECONDS.sleep(4);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("********SendEmail");
    }

    public synchronized void sendSMS() {
        System.out.println("********SendSMS");
    }
}

class Phone8 {
    public static synchronized void sendEmail() {
        try {
            TimeUnit.SECONDS.sleep(4);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("********SendEmail");
    }

    public synchronized void sendSMS() {
        System.out.println("********SendSMS");
    }
}
