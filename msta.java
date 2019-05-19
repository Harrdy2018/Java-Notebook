package com.lukang.www;

/**
 * 父类引用指向子类对象的时候，属性和方法？
 * 隐藏于覆盖的区别，多态会覆盖方法，隐藏成员变量
 * B
 */
public class B extends A{
  public int a=1;
  public void fun(){
    System.out.println("B");
  }
  public static void main(String[] args) {
    A classA=new B();
    System.out.println(classA);//com.lukang.www.B@77556fd
    System.out.println(classA.a);//0
    classA.fun();//B
  }
}
class A{
  public int a=0;
  public void fun(){
    System.out.println("A");
  }
}
