package com.lukang.www;

/**
 * 静态块，类加载
 * 静态块，当类加载的时候会被执行；而且子类加载会寻找他的父类；
 * 属性本身加了一个final类型，是常量，JVM在解析之前就把这个空间生成出来了，直接调用，
 * 调用常量的时候是不需要把类加载的；
 * 如果把final去掉，就是ABC
 * TestABC
 */
public class TestABC {

  public static void main(String[] args) {
    System.out.println(BX.c);
  }
}
class AX{
  static{
    System.out.println("A");
  }
}
class BX extends AX{
  static{
    System.out.println("B");
  }
  public final static String c="C";
}
