package com.huali.jvm;

import java.util.Random;

/**
 * GC log 日记分析
 */
public class GCLog {
    public static void main(String[] args) {
        /**
         * [GC (Allocation Failure) [PSYoungGen: 511K->488K(1024K)] 511K->504K(1536K), 0.0006886 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
         *
         * 1. [GC (Allocation Failure) [PSYoungGen: GC 的类型用于辨别是 Full GC 还是 GC ， PSYoungGen 是辨别 清理的是哪一个空间这里是年轻代
         * 2. 511K->488K(1024K)] : 511K是 PSYoungGen 空间 清理前的内存大小，488K是清理后的内存大小， 1024K 是总共 PSYoungGen 空间的大小
         * 3. 511K->504K(1536K)：511K是清理前堆内存大小 504K是清理后堆的大小， 1536K 是堆的总大小
         * 4. 0.0006886 secs]：是 清理耗时的时长，
         * 5. [Times: user=0.00 sys=0.00, real=0.00 secs]：user是GC 用户耗时，sys是GC 系统耗时，real是实际耗时
         */

        /**
         * [Full GC (Ergonomics) [PSYoungGen: 984K->171K(1024K)] [ParOldGen: 508K->461K(512K)] 1492K->633K(1536K), [Metaspace: 3155K->3155K(1056768K)], 0.0035181 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
         *  不同的是
         *  1. [ParOldGen: 508K->461K(512K)]：老年代，
         *  2. [Metaspace: 3155K->3155K(1056768K)], 元空间，java8 java7之前叫永久代
         */
        String a = "啊手动阀手动阀";
        while (true) {
            a = a + new Random().nextInt(800000) + new Random().nextInt(800000);
        }
    }
}
