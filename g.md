## String不可变性
* ex1
```java
package com.lukang.www;

/**
 * StringTest
 * 从输出结果上来看，似乎s1的值被改变了，
 * 但是事实上只是将s1的指向由“hello”变成了“mian”，“hello”字符串并没有改变
 */
public class StringTest {
  public static void main(String[] args) {
    String s1="hello";
    System.out.println(s1);//hello
    s1="main";
    System.out.println(s1);//main
  }
}
```
* ex2
```java
package com.lukang.www;

/**
 * StringTest
 * 从输出可以看出，s1的值并没有改变，这是因为在java语言中，
 * 引用类型的数据作为函数参数传递时，虽然依然采用值传递的方法，但传递的数据是一个引用，
 * 即将s1指向的“hello”的地址传递给tell方法，在tell方法中更改的指向只是将s1的复制，
 * 在tell方法开始时，s1复制给str，导致s1和str都指向"hello",
 * 然后更改str的指向，str指向“main”，这时s1的指向并没有改变
 */
public class StringTest {
  public static void main(String[] args) {
    String s1="hello";
    System.out.println(s1);//hello
    tell(s1);
    System.out.println(s1);//hello
  }
  public static void tell(String str){
    str="main";
  }
}
```
* ex3
```java
package com.lukang.www;

/**
 * StringTest
 */
public class StringTest {
    String str=new String("good");
    char[] ch={'t','e','s','t'};
    public void change(String str,char ch[]){
      str="test ok";
      ch[0]='b';
    }
    public static void main(String[] args){
      StringTest ex=new StringTest();
      ex.change(ex.str,ex.ch);
      System.out.println(ex.str);//good
      System.out.println(ex.ch);//best
    }
}
```
