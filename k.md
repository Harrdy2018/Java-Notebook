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
### 增删改查
* 建表
```mysql
MariaDB [sqlTestDb]> show create table emp \G;
*************************** 1. row ***************************
       Table: emp
Create Table: CREATE TABLE `emp` (
  `empno` int(4) NOT NULL,
  `ename` varchar(10) DEFAULT NULL,
  `job` varchar(9) DEFAULT NULL,
  `hiredate` date DEFAULT NULL,
  `sal` float(7,2) DEFAULT NULL,
  PRIMARY KEY (`empno`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8
1 row in set (0.00 sec)
```
* 插入数据
```mysql
MariaDB [sqlTestDb]> select * from emp;
+-------+-----------+-----------+------------+---------+
| empno | ename     | job       | hiredate   | sal     |
+-------+-----------+-----------+------------+---------+
|  6060 | lixinghua | jingli    | 2009-09-16 | 2000.30 |
|  7369 | zhangsan  | zongjian  | 2003-10-09 | 1500.90 |
|  7698 | five      | cahngzhan | 2005-03-12 |  800.00 |
|  7762 | Qing      | shuji     | 2005-03-09 | 1000.00 |
|  7782 | zhanggang | zuzhang   | 2005-01-12 | 2500.00 |
|  7839 | caochao   | caiwu     | 2006-09-01 | 2500.00 |
|  8964 | four      | boss      | 2003-10-01 | 3000.00 |
+-------+-----------+-----------+------------+---------+
7 rows in set (0.05 sec)
```
#### 查询数据
```java
package com.harrdy.www;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;

/**
 * SQLDemo
 * Database sqlTestDb
 * table    emp
 * port     3306
 * userName root
 * password 1234
 */
public class SQLDemo {

  public static void main(String[] args) {
    String url="jdbc:mysql://localhost:3306/sqlTestDb";
    String user="root";
    String password="1234";
    String driver="com.mysql.cj.jdbc.Driver";
    try {
      Class.forName(driver);
      Connection con=DriverManager.getConnection(url, user, password);
      Statement statement=con.createStatement();
      String sql="select * from emp";
      ResultSet rs=statement.executeQuery(sql);
      System.out.println("-------------------");
      System.out.println("执行结果如下所示:");
      System.out.println("-------------------");
      System.out.println("姓名"+"\t"+"职称");
      System.out.println("-------------------");

      String ename;
      String job;
      while(rs.next()){
        //System.out.println(rs.next());
        ename=rs.getString("ename");
        job=rs.getString("job");
        System.out.println(ename+"\t"+job);
      }
    } catch (Exception e) {
      //TODO: handle exception
      System.out.println(e);
    }finally{
      System.out.println("success-------------success");
    }
  }
}
```
#### 增加数据
```java
package com.harrdy.www;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.ResultSet;

/**
 * SQLDemo
 * Database sqlTestDb
 * table    emp
 * port     3306
 * userName root
 * password 1234
 */
public class SQLDemo {

  public static void main(String[] args) {
    String url="jdbc:mysql://localhost:3306/sqlTestDb";
    String user="root";
    String password="1234";
    String driver="com.mysql.cj.jdbc.Driver";
    try {
      Class.forName(driver);
      Connection con=DriverManager.getConnection(url, user, password);

      //添加数据
      PreparedStatement psql=con.prepareStatement("insert into emp (empno,ename,job,hiredate,sal) values (?,?,?,?,?)");
      psql.setInt(1, 6666);
      psql.setString(2, "bwj");
      psql.setString(3, "jsksd");
      DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
      Date myDate=df.parse("2010-09-13");
      psql.setDate(4, new java.sql.Date(myDate.getTime()));
      psql.setFloat(5, (float)2000.3);
      psql.executeUpdate();

      //查询
      Statement statement=con.createStatement();
      String sql="select * from emp";
      ResultSet rs=statement.executeQuery(sql);
      System.out.println("-------------------");
      System.out.println("执行结果如下所示:");
      System.out.println("-------------------");
      System.out.println("姓名"+"\t"+"职称");
      System.out.println("-------------------");

      String ename;
      String job;
      while(rs.next()){
        //System.out.println(rs.next());
        ename=rs.getString("ename");
        job=rs.getString("job");
        System.out.println(ename+"\t"+job);
      }
    } catch (Exception e) {
      //TODO: handle exception
      System.out.println(e);
    }finally{
      System.out.println("success-------------success");
    }
  }
}
```
#### 更新数据
```java
package com.harrdy.www;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.ResultSet;

/**
 * SQLDemo
 * Database sqlTestDb
 * table    emp
 * port     3306
 * userName root
 * password 1234
 */
public class SQLDemo {

  public static void main(String[] args) {
    String url="jdbc:mysql://localhost:3306/sqlTestDb";
    String user="root";
    String password="1234";
    String driver="com.mysql.cj.jdbc.Driver";
    try {
      Class.forName(driver);
      Connection con=DriverManager.getConnection(url, user, password);

      //更新数据
      PreparedStatement psql=con.prepareStatement("update emp set ename=?,job=? where empno=?");
      psql.setString(1, "wanggang");
      psql.setString(2, "big_boss");
      psql.setInt(3, 3213);
      psql.executeUpdate();

      //查询
      Statement statement=con.createStatement();
      String sql="select * from emp";
      ResultSet rs=statement.executeQuery(sql);
      System.out.println("-------------------");
      System.out.println("执行结果如下所示:");
      System.out.println("-------------------");
      System.out.println("姓名"+"\t"+"职称");
      System.out.println("-------------------");

      String ename;
      String job;
      while(rs.next()){
        //System.out.println(rs.next());
        ename=rs.getString("ename");
        job=rs.getString("job");
        System.out.println(ename+"\t"+job);
      }
    } catch (Exception e) {
      //TODO: handle exception
      System.out.println(e);
    }finally{
      System.out.println("success-------------success");
    }
  }
}
```
#### 删除数据
```java
```
