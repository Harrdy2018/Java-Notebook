## MySQL
* 如何连接MySQL数据库
```java
package com.harrdy.www;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;
import com.mysql.cj.jdbc.Driver;

/**
 * Demo1
 * java连接mysql数据库
 */
public class Demo1 {

  public static void main(String[] args) 
  {
    String url="jdbc:mysql://localhost:3306/test2018";
    String user="root";
    String password="1234";
    /**
     *第一种方法:创建驱动程序类对象 
     */
    try {
      //1,创建驱动程序类对象
      Driver driver=new Driver();
      //设置用户名和密码
      Properties props=new Properties();
      props.setProperty("user", user);
      props.setProperty("password", password);
      //2,连接数据库,返回连接对象
      Connection conn=driver.connect(url, props);
      System.out.println(conn);
    } catch (Exception e) {
      System.out.println(e);
    }
    /**
     * 第二种方法:使用驱动管理器类连接数据库(注册了两次，没必要)
     */
    try {
      Driver driver2=new Driver();
      //1.注册驱动程序(可以注册多个驱动程序)
      DriverManager.registerDriver(driver2);
      Connection conn=DriverManager.getConnection(url, user, password);
      System.out.println(conn);
    } catch (Exception e) {
      System.out.println(e);      
    }
    /**
     * 第三种方法
     *（推荐使用这种方式连接数据库）
     * 推荐使用加载驱动程序类  来 注册驱动程序 
     */
    try {
      //通过得到字节码对象的方式加载静态代码块，从而注册驱动程序
      Class.forName("com.mysql.cj.jdbc.Driver");
      Connection conn=DriverManager.getConnection(url, user, password);
      System.out.println(conn);
    } catch (Exception e) {
      System.out.println(e);       
    }
  }
}
```
