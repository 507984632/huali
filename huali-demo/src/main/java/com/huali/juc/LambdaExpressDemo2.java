package com.huali.juc;

/**
 * 显示定义函数式接口的注解 就是 lambda 表达式的接口
 * 一般都只有一个抽象方法的时候才能叫函数式接口，
 * 但是如果有一个非默认实现的抽象方法，其余方法都是默认实现的 也可以称之为函数式接口
 * 也可以定义多个的静态方法，也可以称之为函数式接口
 */
@FunctionalInterface
interface Foo {
    void sayHello();

    default int add(int x, int y) {
        return x + y;
    }

    default int mul(int x, int y) {
        return x * y;
    }

    static int div(int x, int y) {
        return x / y;
    }
}

/**
 * 1 lambda 表达式
 * 接口中只有一个抽象方法的时候 可以使用
 */
public class LambdaExpressDemo2 {
    public static void main(String[] args) {
//        Foo foo = () -> {
//            System.out.println("hello");
//        };
//        foo.sayHello();

        Foo foo = () -> {
            System.out.println("hello");
        };
        foo.sayHello();
        System.out.println(foo.add(2, 4));
        System.out.println(foo.mul(3, 5));
        System.out.println(Foo.div(2, 1));
    }
}
