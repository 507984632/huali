package com.huali.utils;

import java.util.concurrent.*;

/**
 * 线程池工具类
 * 线程池 的七大参数
 *
 * @author Yang_my
 * @see java.util.concurrent.ThreadPoolExecutor#ThreadPoolExecutor(int, int, long, java.util.concurrent.TimeUnit, java.util.concurrent.BlockingQueue,
 * ---------------------------------------------------------------------java.util.concurrent.ThreadFactory, java.util.concurrent.RejectedExecutionHandler)
 * 1. int corePoolSize ：线程池中的常驻核心线程数(线程池中最少存活几个线程)
 * 2. int maximumPoolSize ：线程池中能够同时执行的最大线程数，该值必须大于等于1
 * 2.1. 一个计算为主的程序（CPU密集型程序），多线程跑的时候，可以充分利用起所有的 CPU 核心数，比如说 8 个核心的CPU ,
 * --------开8 个线程的时候，可以同时跑 8 个线程的运算任务，此时是最大效率。但是如果线程远远超出 CPU 核心数量，
 * --------反而会使得任务效率下降，因为频繁的切换线程也是要消耗时间的。因此对于 CPU 密集型的任务来说，线程数等于 CPU 数是最好的了。
 * 2.2. 如果是一个磁盘或网络为主的程序（IO密集型程序），一个线程处在 IO 等待的时候，
 * --------另一个线程还可以在 CPU 里面跑，有时候 CPU 闲着没事干，所有的线程都在等着 IO，这时候他们就是同时的了，
 * --------而单线程的话此时还是在一个一个等待的。我们都知道 IO 的速度比起 CPU 来是很慢的。此时线程数等于CPU核心数的两倍是最佳的。
 * <p>
 * 3. long keepAliveTime ：多余空闲线程的存活时间(当前线程池数量超过 corePoolSize 值，
 * 当空闲时间达到 keepAliveTime时，多余线程会被销毁，知道线程数达到 corePoolSize 值)
 * 4. TimeUnit unit ：keepAliveTime 的单位
 * 5. BlockingQueue<Runnable> workQueue ：任务队列，被提交但尚未被执行的任务
 * 6. ThreadFactory threadFactory ：表示线程池中工作线程的线程工厂，用于创建线程，一般默认即可
 * 7. RejectedExecutionHandler handler ：拒绝策略，表示当队列满了，并且工作线程数大于等于 maximumPoolSize 时，如何
 * 来拒绝请求执行的 runnable 策略
 * <p>
 * 最大能同时执行任务数量为  maximumPoolSize + BlockingQueue.size() 元素个数，如果超过这个数，执行拒绝策略
 *
 * <p>
 * 拒绝策略（只有在 阻塞队列满了，也达到了最大工作线程数时，还有任务时才会执行拒绝）
 * 1. AbortPolicy() : 默认的拒绝策略，直接抛出 RejectedExecutionException 异常
 * 2. CallerRunsPolicy() : "调用者运行"的一种机制，该策略既不会抛弃任务，也不会抛出异常，而是将任务回退给调用者，而降低新任务的流量
 * 3. DiscardPolicy() : 该策略会默默的丢失无法处理的任务，不会有额外的处理也不会抛出异常，如果允许任务丢失，这是最好的策略
 * 4. DiscardOldestPolicy() : 抛弃队列中等待最久的任务，然后把当前任务加入队列中，尝试再次提交当前任务
 *
 * <p>
 * 线程池工作流程
 * 1、在创建了线程池后，开始等待请求。
 * 2、当调用 execute（方法添加一个请求任务时，线程池会做出如下判断：
 * 2.1如果正在运行的线程数量小于 corePoolsize，那么马上创建线程运行这个任务；
 * 2.2如果正在运行的线程数量大于或等于 corePoolsize，那么将这个任务放入队列；
 * 2.3如果这个时候队列满了且正在运行的线程数量还小于皿 aximumPoolsize，那么还是要创建非核心线程立刻运行这个任务；
 * 2.4如果队列满了且正在运行的线程数量大于或等于 maximumPoolsize，那么线程池会启动饱和拒绝策略来执行
 * 3、当一个线程完成任务时，它会从队列中取下一个任务来执行。
 * 4、当一个线程无事可做超过一定的时间（ keepAliveTime）时，线程会判断：
 * 如果当前运行的线程数大于 corePoolsize，那么这个线程就被停掉。
 * 所以线程池的所有任务完成后，它最终会收缩到 corepoolsize的大小
 */
public class ExecutorServiceUtil {

    private static final ExecutorService SERVICE = new ThreadPoolExecutor(
            Runtime.getRuntime().availableProcessors(), Runtime.getRuntime().availableProcessors() * 2,
            3L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(), Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.AbortPolicy());

    public static Future<?> execute(Runnable task) {
        Future<?> submit = null;
        try {
            submit = SERVICE.submit(task);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return submit;
    }

    public static Future<?> execute(Callable<?> task) {
        Future<?> submit = null;
        try {
            submit = SERVICE.submit(task);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return submit;
    }

}
