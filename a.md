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
***
```java
    Integer i=new Integer(100);
    int j=100;
    System.out.print(i==j);//true
```
```
Integer变量和int变量比较时，只要两个变量的值是向等的，则结果为true
（因为包装类Integer和基本数据类型int比较时，java会自动拆包装为int，然后进行比较，实际上就变为两个int变量的比较）
```
***
```java
    Integer i=new Integer(100);
    Integer j=100;
    System.out.print(i==j);//false
```
```
非new生成的Integer变量和new Integer()生成的变量比较时，结果为false。
（因为非new生成的Integer变量指向的是java常量池中的对象，而new Integer()生成的变量指向堆中新建的对象，两者在内存中的地址不同）
```
***
```java
    Integer i=100;
    Integer j=100;
    System.out.print(i==j);//true

    Integer i1=128;
    Integer j1=128;
    System.out.print(i1==j1);//false
```
```
对于两个非new生成的Integer对象，进行比较时，如果两个变量的值在区间-128到127之间，则比较结果为true，
如果两个变量的值不在此区间，则比较结果为false
原因？？
java源码
    @HotSpotIntrinsicCandidate
    public static Integer valueOf(int i) {
        if (i >= IntegerCache.low && i <= IntegerCache.high)
            return IntegerCache.cache[i + (-IntegerCache.low)];
        return new Integer(i);
    }
java在编译Integer i = 100 ;时，会翻译成为Integer i = Integer.valueOf(100)
java对于-128到127之间的数，会进行缓存，Integer i = 127时，会将127进行缓存，下次再写Integer j = 127时，就会直接从缓存中取，就不会new了
```
