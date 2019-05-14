# int和Integer的区别
* Integer是int的包装类，int则是java的一种基本数据类型 
* Integer变量必须实例化后才能使用，而int变量不需要 
* Integer实际是对象的引用，当new一个Integer时，实际上是生成一个指针指向此对象；而int则是直接存储数据值 
* Integer的默认值是null，int的默认值是0
```java
//关于第4点如何验证的问题?
Integer a;
int b;
System.out.println(a);
System.out.println(b);
//直接这样写是会报错的，在类中初始化即可
```
***
```java
    Integer i=new Integer(100);
    Integer j=new Integer(100);
    System.out.print(i==j);//fasle

    Integer i1=Integer.valueOf(100);
    Integer j1=Integer.valueOf(100);
    System.out.print(i1==j1);//true
```
```
第一种写法为什么不等呢？
由于Integer变量实际上是对一个Integer对象的引用，所以两个通过new生成的Integer变量永远是不相等的，现在是在比较地址

第一种写法已经被削掉了，建议第二种，第二种为什么相等，看下面即可；
```
