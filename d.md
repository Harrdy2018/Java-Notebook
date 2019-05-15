# 深拷贝与浅拷贝
## 浅拷贝
```java
package com.lukang.www;

/**
 * App
 */
public class App {

  public static void main(String[] args) {
    Father fa=new Father();
    fa.name="zhang san";
    fa.age=30;
    Father fb=(Father)fa.clone();
    System.out.println(fa==fb);//false
    System.out.println(fa.hashCode());//125130493
    System.out.println(fb.hashCode());//914504136
    System.out.println(fa.name);//zhang san
    System.out.println(fb.name);//zhang san
  }
}

class Father implements Cloneable{
  public String name;
  public int age;
  public Child child;
  @Override
  public Object clone(){
    try{
      return super.clone();
    }catch(CloneNotSupportedException e){

    }
    return null;
  }
}
class Child extends Father{
  public String name;
  public int age;
}
```
***
```java
package com.lukang.www;

/**
 * App
 */
public class App {

  public static void main(String[] args) {
    Father fa=new Father();
    fa.name="zhang san";
    fa.age=30;
    fa.child=new Child();
    fa.child.name="xiao zhang san";
    fa.child.age=5;
    Father fb=(Father)fa.clone();
    System.out.println(fa==fb);//false
    System.out.println(fa.hashCode());//914504136
    System.out.println(fb.hashCode());//166239592
    System.out.println(fa.name);//zhang san
    System.out.println(fb.name);//zhang san
    System.out.println(fa.child==fb.child);//true
    System.out.println(fa.child.hashCode());//991505714
    System.out.println(fb.child.hashCode());//991505714
  }
}

class Father implements Cloneable{
  public String name;
  public int age;
  public Child child;
  @Override
  public Object clone(){
    try{
      return super.clone();
    }catch(CloneNotSupportedException e){

    }
    return null;
  }
}
class Child extends Father{
  public String name;
  public int age;
}
```
***从最后对 child 的输出可以看到，A 和 B 的 child 对象，实际上还是指向了统一个对象，只对对它的引用进行了传递。
