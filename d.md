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
