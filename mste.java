package com.lukang.www;

/**
 * 字符串拼接
 * 常量与常量的拼接结果在常量池。且常量池中不会存在相同内容的常量。
 * 只要其中有一个是变量，结果就在堆中
 * 如果拼接的结果调用intern()方法，返回值就在常量池中
 */
public class KK {

  public static void main(String[] args) {
    String s1="hello";
    String s2="world";
    String s3="hello"+"world";
    String s4=s1+"world";
    String s5=s1+s2;
    String s6=(s1+s2).intern();
    System.out.println(s3==s4);//false
    System.out.println(s3==s5);//false
    System.out.println(s4==s5);//false
    System.out.println(s3==s6);//true
  }
}
