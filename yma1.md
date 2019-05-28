# `java.lang.System`
```java
package java.lang;
public final class System {
}
```
## [System.out is declared as static final and initialized with null?](https://stackoverflow.com/questions/31743760/system-out-is-declared-as-static-final-and-initialized-with-null)
## 问题发现
```
System.out为什么不为null?

java源码
    public static final InputStream in = null;
    public static final PrintStream out = null;
    public static final PrintStream err = null;
调用System.out,明显是一个对象不为空
    System.out.println(System.out);//java.io.PrintStream@77556fd
    
但是
package com.lukang.www;
/**
 * App
 */
public class App {
  public static void main(String[] args) {
    System.out.println(TestA.out);//null
    TestA.out.hh();//java.lang.NullPointerException
  }
}
class TestA{
  public static final TestB out = null;
}
class TestB{
  public void hh(){
    System.out.println("ahahhahahha");
  }
}
```
## 问题解决
```java
    /* Register the natives via the static initializer.
     *
     * VM will invoke the initializeSystemClass method to complete
     * the initialization for this class separated from clinit.
     * Note that to use properties set by the VM, see the constraints
     * described in the initializeSystemClass method.
     */
     /*
     
     */
    private static native void registerNatives();
    static {
        registerNatives();
    }
```
## 既然`final`只能被初始化一次，为何又能被改变呢？
```java
//虽然可以通过反射更改静态最终变量，但在本例中，字段是通过原生方法更改的。
//重新分配标准输入流
    public static void setIn(InputStream in) {
        checkIO();
        setIn0(in);
    }
//重新分配标准输出流
   public static void setOut(PrintStream out) {
        checkIO();
        setOut0(out);
    }
//重新分配标准错误输出流
    public static void setErr(PrintStream err) {
        checkIO();
        setErr0(err);
    }
//原生方法
    private static native void setIn0(InputStream in);
    private static native void setOut0(PrintStream out);
    private static native void setErr0(PrintStream err);
```
## [What does the registerNatives method do?](https://stackoverflow.com/questions/1010645/what-does-the-registernatives-method-do)
## [Resetting Standard output Stream](https://stackoverflow.com/questions/5339499/resetting-standard-output-stream)
* 重设标准输出流
```java
//正常情况下
package com.lukang.www;
/**
 * App
 */
public class App {
  public static void main(String[] args) {
    System.err.println("err err err");//红色的 err err err
    System.out.println("out out out");//白色的 out out out  
  }
}
```
```java
package com.lukang.www;
/**
 * App
 */
public class App {
  public static void main(String[] args) {
    System.setOut(System.err);
    System.err.println("err err err");//红色的 err err err
    System.out.println("out out out");//红色的 out out out  
  }
}
```
* 将输出设置到文件中
```java
package com.lukang.www;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

/**
 * App
 */
public class App {
  public static void main(String[] args) {
    // System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
    File logFile = new File("kk.txt");
    try {
      System.setOut(new PrintStream(logFile));
      System.out.print("out out out");
      System.err.println("err err err");
    } catch (FileNotFoundException e) {
      System.out.println(e);
    }
  }
}
```
```java
package com.lukang.www;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

/**
 * App
 */
public class App {
  public static void main(String[] args) {
    /*
      文件输出流，相当于写文件
      FileOutputStream是OutputStream流的子类
      Creates a file output stream to write to the file with the specified name.
      new FileOutputStream();
    */
    try {
      System.setOut(new PrintStream(new FileOutputStream("oo.txt")));
    } catch (FileNotFoundException e) {
      System.out.println(e);
    }
    System.out.print("我要写文件");
  }
}
```
***
## 日期时间API
* System.currentTimeMillis();
```
用来返回当前时间与1970/1/1/0/0/0之间以毫秒为单位的时间差
计算时间的主要标准：
UTC-->Coordinated Universal Time
GMT-->Greenwich Mean Time
CST-->Central Standard Time
```
* `java.util.Date`
```
构造器：
Date()
Date​(long date)
方法：
long	getTime()
String	toString()
```
* `java.sql.Date`
```
构造器：
Date​(long date)
方法：
String	toString()
java.sql.Date是java.util.Date的子类
```
## `SimpleDateFormat`
```java
package com.lukang.www;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * SimpleDateFormat
 */
public class WindowTest {
  public static void main(String[] args) {
    // 实例化SimpleDateFormat:使用默认的构造器
    SimpleDateFormat sdf = new SimpleDateFormat();
    // 格式化：日期--->字符串
    Date date = new Date();
    System.out.println(date);
    String s = sdf.format(date);
    System.out.println(s);
    // 解析：格式化的逆过程,字符串--->日期
    String str = "2019/5/28 下午12:53";
    try {
      Date dd = sdf.parse(str);
      System.out.println(dd);
    } catch (ParseException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    /*
     * 指定构造器
    */
    SimpleDateFormat newSDF=new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
    SimpleDateFormat newSDF1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    System.out.println(newSDF.format(date));
    System.out.println(newSDF1.format(date));
  }
}
```
