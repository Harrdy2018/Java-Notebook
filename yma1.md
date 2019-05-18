# `java.lang.System`
```java
package java.lang;
public final class System {
}
```
## [System.out初始化为null?](https://stackoverflow.com/questions/31743760/system-out-is-declared-as-static-final-and-initialized-with-null)
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
