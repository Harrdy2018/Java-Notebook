# `java.io.File`
* 源码
```java
public class File
    implements Serializable, Comparable<File>
{
//对象FileSystem fs代表平台本地文件系统
private static final FileSystem fs = DefaultFileSystem.getFileSystem();
private final String path;
//enum类型表明文件路径的状态
private static enum PathStatus { INVALID, CHECKED };
//文件路径是否合法
private transient PathStatus status = null;
}
private final transient int prefixLength;

//构造函数
    public File(String pathname) {
        if (pathname == null) {
            throw new NullPointerException();
        }
        this.path = fs.normalize(pathname);
        this.prefixLength = fs.prefixLength(this.path);
    }

//获取路径
 public String getPath() {
        return path;
    }
```
* 理解`private static final FileSystem fs = DefaultFileSystem.getFileSystem()`
```java
//java.io.DefaultFileSystem
package java.io;
class DefaultFileSystem {
    public static FileSystem getFileSystem() {
        return new WinNTFileSystem();
    }
}

//java.io.WinNTFileSystem
package java.io;
class WinNTFileSystem extends FileSystem {

    private final char slash;
    private final char altSlash;
    private final char semicolon;
    private final String userDir;

    public WinNTFileSystem() {
        Properties props = GetPropertyAction.privilegedGetProperties();
        slash = props.getProperty("file.separator").charAt(0);
        semicolon = props.getProperty("path.separator").charAt(0);
        altSlash = (this.slash == '\\') ? '/' : '\\';
        userDir = normalize(props.getProperty("user.dir"));
    }
}

//sun.security.action.GetPropertyAction
package sun.security.action;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Properties;
public class GetPropertyAction implements PrivilegedAction<String> {
    private String theProp;
    private String defaultVal;
    public GetPropertyAction(String theProp) {
        this.theProp = theProp;
    }
    public GetPropertyAction(String theProp, String defaultVal) {
        this.theProp = theProp;
        this.defaultVal = defaultVal;
    }
    public String run() {
        String value = System.getProperty(theProp);
        return (value == null) ? defaultVal : value;
    }
    public static String privilegedGetProperty(String theProp) {
        if (System.getSecurityManager() == null) {
            return System.getProperty(theProp);
        } else {
            return AccessController.doPrivileged(
                    new GetPropertyAction(theProp));
        }
    }
    public static String privilegedGetProperty(String theProp,
            String defaultVal) {
        if (System.getSecurityManager() == null) {
            return System.getProperty(theProp, defaultVal);
        } else {
            return AccessController.doPrivileged(
                    new GetPropertyAction(theProp, defaultVal));
        }
    }
    public static Properties privilegedGetProperties() {
        if (System.getSecurityManager() == null) {
            return System.getProperties();
        } else {
            return AccessController.doPrivileged(
                    new PrivilegedAction<Properties>() {
                        public Properties run() {
                            return System.getProperties();
                        }
                    }
            );
        }
    }
}
/*
System.getProperties()
public final class System{
    private static volatile SecurityManager security;
    public static SecurityManager getSecurityManager() {
        return security;
    }
    public static Properties getProperties() {
        SecurityManager sm = getSecurityManager();
        if (sm != null) {
            sm.checkPropertiesAccess();
        }

        return props;
    }

}
所以System.getProperties()可以直接拿来用，不用深究了
*/
```
* 测试
```java
package com.lukang.www;
import java.io.File;
/**
 * App
 */
public class App {
  public static void main(String[] args) {
    File f=new File("lukang.txt");
    System.out.println(f.getPath()); //lukang.txt
  }
}
```
