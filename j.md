## 比较器
```java
package com.lukang.www;

import java.util.Arrays;
import java.util.Comparator;

import javax.management.RuntimeErrorException;

/**
 * Java中的对象，正常情况下只能进行==或！=操作
 * 自然排序{@link Comparable}
 * 像String实现了comparable接口，重写了compareTo()方法
 * 定制排序{@link java.util.Comparator}
 */
public class WindowTest {
  public static void main(String[] args) {
    String[] arr=new String[]{"AA","KK","CC","MM"};
    Arrays.sort(arr);
    System.out.println(Arrays.toString(arr));

    Goods[] arr2=new Goods[5];
    arr2[0]=new Goods("len",34);
    arr2[1]=new Goods("dell",43);
    arr2[2]=new Goods("xiaomi",12);
    arr2[3]=new Goods("huawei",65);
    arr2[4]=new Goods("huawei",63);
    Arrays.sort(arr2);
    System.out.println(Arrays.toString(arr2));

    //定制排序
    Arrays.sort(arr,new Comparator() {
      @Override
      public int compare(Object o1, Object o2) {
        if(o1 instanceof String && o2 instanceof String){
          String s1=(String)o1;
          String s2=(String)o2;
          return -s1.compareTo(s2);
        }
        throw new RuntimeException("传入的数据类型不一致!!!");
      }
    });
    System.out.println(Arrays.toString(arr));
    Arrays.sort(arr2,new Comparator() {
      @Override
      public int compare(Object o1, Object o2) {
        if(o1 instanceof Goods && o2 instanceof Goods){
          Goods g1=(Goods)o1;
          Goods g2=(Goods)o2;
          if(g1.getName().equals(g2.getName())){
            return -Double.compare(g1.getPrice(), g2.getPrice());
          }else{
            return g1.getName().compareTo(g2.getName());
          }
        }
        throw new RuntimeException("传入的数据类型不一致!!!");
      }
    });
    System.out.println(Arrays.toString(arr2));
  }
}

class Goods implements Comparable{
  private String name;
  private double price;
  public Goods(){

  }
  public Goods(String name,double price) {
    this.name=name;
    this.price=price;
  }
  /**
   * @return the name
   */
  public String getName() {
    return name;
  }
  /**
   * @return the price
   */
  public double getPrice() {
    return price;
  }
  @Override
  public int compareTo(Object o) {
    System.out.println("***********");
    if(o instanceof Goods){
      Goods goods=(Goods)o;
      if(this.price>goods.price){
        return 1;
      }else if(this.price<goods.price){
        return -1;
      }else{
        return 0;
      }
    }
    throw new RuntimeException("传入的数据类型不一致!!!");
  }
}
```
