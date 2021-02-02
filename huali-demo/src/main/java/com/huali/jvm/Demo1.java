package com.huali.jvm;

/**
 * 类加载
 * BootstrapClassLoader (根加载器，启动类加载器) C++ 用于加载 java 自带的类 Object String 等 加载的 JDK根目录下\jre\lib\rt.jar jar包
 * ExtensionClassLoader【ExtClassLoader】 (扩展类加载器) Java 加载的是 JDK 根目录下 \jre\lib\ext 中的所有jar包
 * AppClassLoader (应用程序类加载器) Java 用于加载ClassPath中所有的类
 * <p>
 * 1. 双亲委派机制
 *   就是加载一个自己写的User 类的时候，先不通过 AppClassLoader 加载类，而是先让 ExtClassLoader 去加载， 而 ExtClassLoader 也现不加载
 *      去找 BootstrapClassLoader 去加载，此时，Boot 是最顶层的加载器了，所以尝试在自己的加载路径上加载该类，如果找到了该类，则加载，
 *      找不到，则让 ExtClassLoader 去加载，还找不到 则让  AppClassLoader 去加载， 在找不到就是 ClassNotFountException 异常
 *     简单解释就是 收到加载请求后往上抛，上层能加载就加载，不能加载一层层往下，到最后还加载不了就是 ClassNotFountException 异常
 *   这样保证了 在不同的类加载器 加载到的类都是同一个类
 * <p>
 * 2. 沙箱安全机制
 *
 *
 */
public class Demo1 {
    public static void main(String[] args) {
        Object o = new Object();
        // 因为 Object 是 java 自带的类，是用 根加载器加载的，所以 在获得一个加载器之后在获得父加载器时会报出空指针
//        System.out.println(o.getClass().getClassLoader().getParent().getParent());
//        System.out.println(o.getClass().getClassLoader().getParent());
        // 又因为 BootstrapClassLoader 是用 C++ 写的，所以在Java程序中显示 null
        System.out.println(o.getClass().getClassLoader());

        System.out.println();
        System.out.println();
        System.out.println();

        Demo1 d = new Demo1();
        // 所以这里能说明 自己写的类是通过 AppClassLoader 加载的，而这个加载的父加载器为 ExtClassLoader 在往上就是 根加载器
        System.out.println(d.getClass().getClassLoader().getParent().getParent());
        System.out.println(d.getClass().getClassLoader().getParent());
        System.out.println(d.getClass().getClassLoader());
    }
}
