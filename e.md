# String a="hello" String b="hello" a==b 返回true的问题分析
```
String a="hello world"; 
//在java中有一个常量池，当创建String 类型的引用变量给它赋值时，java会到它的常量池中找"hello world"是不是在常量池中已存在。
如果已经存在则返回这个常量池中的"hello world"的地址(在java中叫引用)给变量a 。
注意a并不是一个对象，而是一个引用类型的变量。它里面存的实际上是一个地址值，而这个值是指向一个字符串对象的。
在程序中凡是以"hello world"这种常量似的形式给出的都被放在常量池中。

String b=new String("hello world"); 
//这种用new关键字定义的字符串，是在堆中分配空间的。而分配空间就是由new去完成的，由new去决定分配多大空间，
并对空间初始化为字符串"hello world" 返回其在堆上的地址。
通过上面的原理，可以做如下实验： 
String a="hello world"; 
String b="hello world"; 
String c=new String("hello world"); 
String d=new String("hello world"); 
if(a==b) System.out.println("a==b"); 
else System.out.println("a!=b"); 
if(c==d) System.out.println("c==d");
else System.out.println("c!=d"); 
//输出结果： a==b c!=d 为什么会出现上面的情况呢？ 
String a="hello world"; String b="hello world"; 
通过上面的讲解可以知道，a和b都是指向常量池的同一个常量字符串"hello world"的，因此它们返回的地址是相同的。
a和b都是引用类型，相当于c语言里面的指针。java里面没有指针的概念，但是实际上引用变量里面放的确实是地址值，
只是java为了安全不允许我们对想c语言中的那样对指针进行操作(如++ 、--)等。这样就有效的防止了指针在内存中的游离。
而对于 String c=new String("hello world"); String d=new String("hello world"); 
来说是不相等的，他们是有new在堆中开辟了两块内存空间，返回的地址当然是不相等的了。
如果我们要比较这两个字符串的内容怎么办呢？
可以用下面的语句： 
if(c.equals(d)) System.out.println("c==d"); else System.out.println("c!=d"); //输出 c==d
```
