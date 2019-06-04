## 接口
```规定一个类必须做什么，而不管你如何做```
### 实现接口的类赋值给接口引用
```
我们可以定义一个接口类型的引用变量来引用实现接口的类的实例，
当这个引用调用方法时，它会根据实际引用的类的实例来判断具体调用哪个方法，这和超类对象引用访问子类对象的机制相似。
```
```java
package lukang;

/**
 * InterfaceTest
 */
public class InterfaceTest {

  public static void main(String[] args) {
    InterA a;
    a=new B();
    System.out.println(a);//lukang.B@368239c8
    System.out.println(a.getClass());//class lukang.B
    a.fun();
    a=new C();
    a.fun();
  }
}
/**
 * InterA
 */
interface InterA {

  void fun();
}
class B implements InterA{

  @Override
  public void fun() {
    System.out.println("This is B");
  }
  
}
class C implements InterA{

  @Override
  public void fun() {
    System.out.println("This is C");
  }
  
}
```
