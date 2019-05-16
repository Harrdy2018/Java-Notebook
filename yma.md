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
*
```java
package com.lukang.www;
/**
 * App
 */
public class App {
  public static void main(String[] args) {
    char[] cc=new char[]{'a','b','c'};
    byte[] res=Test.toBytes(cc, 0, 3);
    for(byte rr:res){
      System.out.println(rr); //res-->[0,97,0,98,0,99]
    }
  }
}
class Test{
  private static final int HI_BYTE_SHIFT=8;
  private static final int LO_BYTE_SHIFT=0;
  static final int MAX_LENGTH = Integer.MAX_VALUE >> 1;
  public static byte[] toBytes(char[] value, int off, int len) {
    byte[] val = newBytesFor(len);
    for (int i = 0; i < len; i++) {
        System.out.println(value[off]);//a b c
        putChar(val, i, value[off]);
        off++;
    }
    return val;
}
  static void putChar(byte[] val, int index, int c) {
    System.out.println(c);//97 98 99
    index <<= 1;//index*2-->0 2 4
    /*
    当index=2时；
    c=98---int 00000000 00000000 00000000 01100010
    c>>8---获取高位 00000000 00000000 00000000--byte截取 00000000
    index++ index=3
    c>>0--获取低位 00000000 00000000 00000000 01100010--byte截取 01100010
    循环一遍得到
    byte[]--[0,97,0,98,0,99]
    */
    val[index++] = (byte)(c >> HI_BYTE_SHIFT);
    val[index]   = (byte)(c >> LO_BYTE_SHIFT);
}
  public static byte[] newBytesFor(int len) {
    if (len < 0) {
        throw new NegativeArraySizeException();
    }
    if (len > MAX_LENGTH) {
        throw new OutOfMemoryError("UTF16 String size is " + len +", should be less than " + MAX_LENGTH);
    }
    return new byte[len << 1];
}
}
```
## 分析 `public String(char value[])`过程
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
            byte[] val = StringUTF16.compress(value, off, len);
            //返回压缩之后的byte数组(也就是LATIN1编码)
            // val-->[97,98,99]
            if (val != null) {
                this.value = val;
                this.coder = LATIN1;    
                return;
            }
           //最后得到实例的两个private属性
           //value-->>byte数组 [97,98,99] 
           //coder-->>byte类型 编码方式LATIN1(0静态的常量)
        }
        //如果上述条件不满足更改编码类型为UTF16
        this.coder = UTF16;
        this.value = StringUTF16.toBytes(value, off, len);
        //最后得到实例的两个private属性
        //value-->>byte数组 [0,97,0,98,0,99] 
        //coder-->>byte类型 编码方式UTF16(1静态的常量)
    }
```
## `StringUTF16`类重要方法
```java
package com.lukang.www;
/**
 * App
 */
public class App {
  public static void main(String[] args) {
    byte[] a=new byte[]{0,-1,1,0,1,1};
    byte[] b=new byte[]{0,-1,1,0,1,1};
    Test.equals(a, b);
  }
}
class Test{
  private static final int HI_BYTE_SHIFT=8;
  private static final int LO_BYTE_SHIFT=0;
  public static boolean equals(byte[] value, byte[] other) {
    //只有在长度相等的时候比较才有意义,value.length一定是偶数
    if (value.length == other.length) {
        //减半len-->3
        int len = value.length >> 1;
        for (int i = 0; i < len; i++) {
            if (getChar(value, i) != getChar(other, i)) {
                return false;
            }
        }
        return true;
    }
    return false;
  }
  static char getChar(byte[] val, int index) {
    /*
    index=0;翻倍还是0
    val[0]=0--byte类型
        00000000 
    &   11111111
        00000000
    <<8 00000000 00000000
    index++ index=1
    val[0]=-1--byte类型
        11111111
    &   11111111
        11111111
    <<0 11111111

        00000000  00000000
    || 
                  11111111 
        00000000  11111111--(char)255
    */
    /*
    index=1;翻倍是2
    val[2]=1--byte类型
      00000001 
    & 11111111
      00000001
    <<8 00000001 00000000
    index++ index=3
    val[3]=0--byte类型
        00000000
    &   11111111
        00000000
    <<0 00000000

        00000001  00000000
    || 
                  00000000 
        00000001  00000000--(char)256
    */
    index <<= 1;
    return (char)(((val[index++] & 0xff) << HI_BYTE_SHIFT) |
                  ((val[index]   & 0xff) << LO_BYTE_SHIFT));
  }
}
```
##  `StringLatin1`类重要方法
```java
    //直接比较对应的值即可
    public static boolean equals(byte[] value, byte[] other) {
        if (value.length == other.length) {
            for (int i = 0; i < value.length; i++) {
                if (value[i] != other[i]) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
```
## 分析 `a.equals(b)` 过程
```java
    public boolean equals(Object anObject) {
        //如果两个都是引用类型，直接用==比较即可，==在引用类型时，比较的是地址，如果地址一样，一定相等
        if (this == anObject) {
            return true;
        }
        //如果是String类的实例，继续
        if (anObject instanceof String) {
            String aString = (String)anObject;
            //coder()返回编码类型byte类型，默认是LATIN1编码
            //如果两个编码一样时才能比较
            if (coder() == aString.coder()) {
                /*
                String a=new String("ÿĀā");  [255,256,257]
                String b=new String("ÿĀā");
                a.equals(b);
                我们先看UTF16编码，待会讲为什么255是[0,-1]表示
                a-->value-->[0,-1,1,0,1,1]
                b-->value-->[0,-1,1,0,1,1]
                我们看LATIN1编码，第一个0，由于byte截取，第二个零由于break,为默认值，看上面代码即可
                a-->value-->[-1,0,0]
                b-->value-->[0,0,0]
                */
                return isLatin1() ? StringLatin1.equals(value, aString.value)
                                  : StringUTF16.equals(value, aString.value);
            }
        }
        return false;
    }
```
