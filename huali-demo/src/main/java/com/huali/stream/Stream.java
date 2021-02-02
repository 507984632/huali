package com.huali.stream;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
/**
 * 链式编程 的注解
 * 该注解能在 new 对象的时候可以直接 .setXX().setXX() 的链式编程
 */
@Accessors(chain = true)
class MyBook {
    private Long id;
    private String name;
    private Double price;

    public MyBook get() {
        // 链式编程的用法
        return new MyBook().setId(1L).setName("链式编程").setPrice(25.6d);
    }

}

/**
 * 流式计算
 * <p>
 * <p>
 * 4 大函数式接口
 * 接口名称                         参数类型        返回值类型       用途
 * 1. Consumer<T> 消费型接口           T               void          对参数 T 的对象进行操作 包含 void accept(T t); 方法
 * 2. Supplier<T> 供给型接口          void              T            返回类型为 T 的对象，包含 T get(); 方法
 * 3. Function<T,R>函数型接口          T                R            对参数 T 的对象进行操作，并返回对象为 R 的接口，包含 R apply(T t); 方法
 * 4. Predicate<T> 断定型接口          T              boolean        确定类型为 T 的对象是否满足某约束，并返回 boolean 值，包含 boolean test(T t)方法
 */
public class Stream {
    public static void main(String[] args) {
        MyBook b1 = new MyBook(1L, "a", 23.5d);
        MyBook b2 = new MyBook(2L, "b", 33.5d);
        MyBook b3 = new MyBook(3L, "c", 43.5d);
        MyBook b4 = new MyBook(4L, "d", 53.5d);

        List<MyBook> list = Arrays.asList(b1, b2, b3, b4);
        List<Long> collect = list.stream().filter(b -> {
            return b.getPrice() > 33.7d;
        }).map(MyBook::getId).collect(Collectors.toList());
        //
        /* 说明直接遍历集合达不到那种效果
        for (MyBook myBook : list) {
            System.out.println(myBook);
        }
        */

    }

    public void test() {
        Consumer<String> consumer = s -> {
            System.out.println(s);
        };
        consumer.accept("s");

        Supplier<String> supplier = () -> {
            return "sadf";
        };
        System.out.println(supplier.get());

        Function<String, Integer> function = str -> {
            return Integer.valueOf(str);
        };
        System.out.println(function.apply("1024"));


        Predicate<String> predicate = o -> {
            return o.isEmpty();
        };
        System.out.println(predicate.test("123"));
    }
}
