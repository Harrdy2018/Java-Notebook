# private/default/protected/public
***
## protected保护的属性和方法  很特别
### 同一个包中 子类可以直接或者间接的获取父类保护的属性和方法
* Person.java
```java
package com.lukang.www;

/**
 * Person
 */
public class Person {

  protected int a=4;
  protected int getA(){
    return this.a;
  }
}
```
* Student.java
```java
package com.lukang.www;

/**
 * Student
 */
public class Student extends Person{

  protected int getB() {
    return this.getA();
  }
}
```
* Manager.java
```java
package com.lukang.www;

/**
 * Manager
 */
public class Manager {

  public static void main(String[] args) {
    Student s=new Student();
    System.out.println(s.getB());//4         子类间接获取父类保护的属性和方法
    System.out.println(s.getA());//4         子类直接获取父类保护的属性和方法
  }
}
```
### 不同一个包中 子类只能间接获取父类保护的属性和方法
* Person.java
```java
package com.harrdy.www;

/**
 * Person
 */
public class Person {

  protected int a=4;
  protected int getA(){
    return this.a;
  }
}
```
* Student.java
```java
package com.lukang.www;

import com.harrdy.www.Person;

/**
 * Student
 */
public class Student extends Person{

  protected int getB() {
    return this.getA();
  }
}
```
* Manager.java
```java
package com.lukang.www;

/**
 * Manager
 */
public class Manager {

  public static void main(String[] args) {
    Student s=new Student();
    System.out.println(s.getB());//4
    System.out.println(s.getA());//The method getA() from the type Person is not visible
  }
}
```
### 总结
```
对于protected的成员或方法，要分子类和基类是否在同一个包中。
与基类不在同一个包中的子类，只能访问自身从基类继承而来的受保护成员和方法，而不能访问基类实例本身的受保护成员和方法。
在相同包时，protected和public是一样的。
```
***
<table>
  <thead>
    <tr>
      <th>修饰符</th>
      <th>当前类</th>
      <th>同包</th>
      <th>子类</th>
      <th>其他包</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>public</td>
      <td>&radic;</td>
      <td>&radic;</td>
      <td>&radic;</td>
      <td>&radic;</td>
    </tr>
    <tr>
      <td>protected</td>
      <td>&radic;</td>
      <td>&radic;</td>
      <td>&radic;</td>
      <td>X</td>
    </tr>
    <tr>
      <td>default</td>
      <td>&radic;</td>
      <td>&radic;</td>
      <td>X</td>
      <td>X</td>
    </tr>
    <tr>
      <td>private</td>
      <td>&radic;</td>
      <td>X</td>
      <td>X</td>
      <td>X</td>
    </tr>
  </tbody>
</table>
