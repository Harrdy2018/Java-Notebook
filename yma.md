# `String`类源码分析
```java
public final class String
    implements java.io.Serializable, Comparable<String>, CharSequence {
    
    @Stable
    private final byte[] value;
    private final byte coder;
    private int hash;
    private static final long serialVersionUID = -6849794470754667710L;
    static final boolean COMPACT_STRINGS;
    static {
        COMPACT_STRINGS = true;
    }
    
    String(byte[] value, byte coder) {
        this.value = value;
        this.coder = coder;
    }

    byte coder() {
        return COMPACT_STRINGS ? coder : UTF16;
    }

    byte[] value() {
        return value;
    }

    private boolean isLatin1() {
        return COMPACT_STRINGS && coder == LATIN1;
    }

    @Native static final byte LATIN1 = 0;
    @Native static final byte UTF16  = 1;
}
```
## `StringUTF16`类重要方法
* char数组转变为byte数组 返回byte[]长度
```java
package com.lukang.www;
/**
 * App
 */
public class App {
  public static void main(String[] args) {
    char[] cc=new char[]{'a','b','c'};
    for(char c: cc){
      System.out.println(c);
    }
    String ss=new String(cc);
    System.out.println(ss);
    byte[] ret = new byte[cc.length];
    int ll=Test.compress(cc, 0, ret, 0, cc.length);
    System.out.println(ll);
    for(byte rr:ret){
      System.out.println(rr);//ret-->[97,98,99]
    }
  }
}
class Test{
  // compressedCopy char[] -> byte[]
  public static int compress(char[] src, int srcOff, byte[] dst, int dstOff, int len) {
      for (int i = 0; i < len; i++) {
          char c = src[srcOff];
          if (c > 0xFF) {
              len = 0;
              break;
          }
          dst[dstOff] = (byte)c;
          srcOff++;
          dstOff++;
      }
      return len;
  }
}
```
* 如果能压缩,返回转换之后的byte[]
```java
public static byte[] compress(char[] val, int off, int len) {
        byte[] ret = new byte[len];
        if (compress(val, off, ret, 0, len) == len) {
            return ret;
        }
        return null;
    }
```
* 当char[]-->>byte[],编码方式是UTF16时，需要根据长度翻倍，也就是构建一个翻倍的byte数组
```java
  static final int MAX_LENGTH = Integer.MAX_VALUE >> 1;
  //Integer.MAX_VALUE=0x7fffffff=2147483647=Math.pow(2, 31)-1
  //MAX_LENGTH=
  public static byte[] newBytesFor(int len) {
        if (len < 0) {
            throw new NegativeArraySizeException();
        }
        if (len > MAX_LENGTH) {
            throw new OutOfMemoryError("UTF16 String size is " + len +
                                       ", should be less than " + MAX_LENGTH);
        }
        //翻倍存储
        return new byte[len << 1];
    }
```
## 分析
```java
//先加载静态代码块 COMPACT_STRINGS = true; 
//默认采用 LATIN1编码(一个字符一字节表示)，否则采用UTF16编码(一个字符两个字节表示)

//以char数组作为构造参数演示
    char[] cc=new char[]{'a','b','c'};
    for(char c: cc){
      System.out.println(c);
    }
    String ss=new String(cc);
    System.out.println(ss);
//上面代码到底发生了什么？
//构造函数源码
 public String(char value[]) {  //value-->['a','b','c']
        this(value, 0, value.length, null);
    }
//调用自身String()函数
String(char[] value, int off, int len, Void sig) {
        if (len == 0) {
            this.value = "".value;
            this.coder = "".coder;
            return;
        }
        if (COMPACT_STRINGS) {
            //value-->['a','b','c'] off-->0 len-->3
            byte[] val = StringUTF16.compress(value, off, len); //返回压缩之后的byte数组
           // val-->[97,98,99]
            if (val != null) {
                this.value = val;        //最后得到实例的两个private属性
                this.coder = LATIN1;     //value-->>byte数组 [97,98,99] coder-->>byte类型 编码方式LATIN1(0静态的常量)
                return;
            }
        }
        //如果上述条件不满足更改编码类型为UTF16
        this.coder = UTF16;
        this.value = StringUTF16.toBytes(value, off, len);
    }
```
