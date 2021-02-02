package com.huali.juc;

import lombok.Data;

@Data
class Person {
    private String personName;
}

/**
 * java 是值传递
 */
public class PassByCalueDemo {
    public void changeValue1(int age) {
        age = 20;
    }

    public void changeValue2(Person person) {
        person.setPersonName("修改过后的名称");
    }

    public void changeValue3(String str) {
        str = "修改值";
    }

    public static void main(String[] args) {
        PassByCalueDemo passByCalue = new PassByCalueDemo();
        int age = 10;
        passByCalue.changeValue1(age);
        System.out.println("age：" + age);

        Person person = new Person();
        person.setPersonName("原始值名称");
        passByCalue.changeValue2(person);
        System.out.println("personName:" + person.getPersonName());

        String str = "原始值str";
        passByCalue.changeValue3(str);
        System.out.println("str" + str);
    }
}
