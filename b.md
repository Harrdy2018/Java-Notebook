# private/default/protected/public
***
## protected保护的属性和方法  很特别
### 同一个包中
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
    System.out.println(s.getB());//4
    System.out.println(s.getA());//4
  }
}
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
