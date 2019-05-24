# `String`类源码分析
```
String:字符串，使用一对""引起来表示
1,String申明为final,不可被继承
2,String实现了java.io.Serializable接口：表示字符串是支持序列化的
        实现了Comparable<String>接口：表示String可以比较大小
4,String:代表不可变的字符序列。简称：不可变性
  体现：1,当对字符串重新赋值时，需要重写指定内存区域赋值，不能使用原有的value进行赋值
        2,当对现有的字符串进行链接操作时，也需要重新指定内存区域赋值，不能使用原有的value进行赋值
        3，当调用replace方法方法修改字符或者字符串时，也需要重新指定内存区域赋值
3,String内部定义了final char[] value用于存储字符串数据
5,通过字面量的方式(区别于new)给一个字符串赋值，此时的字符串值申明在字符串常量池中
6,字符串常量池中是不会存储相同内容的字符串的
    //如果是引用类型，==比较的是地址
    String a="qwer";
    String b="qwer";
    System.out.println(a==b);//true
```
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
* StringUTF16.putChar()是将字符串以UTF16储存,下面只是一个例子,'abc'本身是以LATIN1存储的
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
                我们再看LATIN1编码，第一个0，由于byte截取，第二个零由于break,为默认值，看上面代码即可
                a-->value-->[-1,0,0]
                b-->value-->[-1,0,0]
                */
                return isLatin1() ? StringLatin1.equals(value, aString.value)
                                  : StringUTF16.equals(value, aString.value);
            }
        }
        return false;
    }
```
## `String`类一些方法的实现
* `public String substring(int beginIndex){}`
```java
    //不改变源字符串
    String ss=new String("lukang");
    String res=ss.substring(1);
    System.out.println(ss); //lukang
    System.out.println(res); //ukang
    for(int i=0;i<ss.length();i++){
      System.out.println(ss.codePointAt(i));//108,117,107,97,110,103
    }
    
//第一步
/*
ss是字符串实例 value可以用(byte)(char 每一个lukang计算出来)
this.coder---LATIN1
this.value---[108,117,107,97,110,103]
*/
    public String substring(int beginIndex) {
        if (beginIndex < 0) {
            throw new StringIndexOutOfBoundsException(beginIndex);
        }
        int subLen = length() - beginIndex;
        if (subLen < 0) {
            throw new StringIndexOutOfBoundsException(subLen);
        }
        if (beginIndex == 0) {
            return this;
        }
        return isLatin1() ? StringLatin1.newString(value, beginIndex, subLen)
                          : StringUTF16.newString(value, beginIndex, subLen);
    }
//第二步调用StringLatin1.newString([108,117,107,97,110,103], 1, 5)
    public static String newString(byte[] val, int index, int len) {
        return new String(Arrays.copyOfRange(val, index, index + len),LATIN1);
    }
//第三步调用Arrays.copyOfRange([108,117,107,97,110,103], 1, 6)
   public static byte[] copyOfRange(byte[] original, int from, int to) {
        int newLength = to - from;
        if (newLength < 0)
            throw new IllegalArgumentException(from + " > " + to);
        byte[] copy = new byte[newLength];
        System.arraycopy(original, from, copy, 0,
                         Math.min(original.length - from, newLength));
        return copy;
    }
//返回计算new String(byte数组[117,107,97,110,103],LATIN1)
//这个LATIN1来自import static java.lang.String.LATIN1;是一个byte类型
//调用default默认修饰的构造器
    String(byte[] value, byte coder) {
        this.value = value;
        this.coder = coder;
    }
//这样得到了子串res
this.coder---LATIN1
this.value---[117,107,97,110,103]
```
***
## 思考`字符串以UTF16存储的过程`
* 字符串什么时候采用UTF16存储?
```
每一个字符串的实例都有两个私有属性
        this.coder------byte类型，解决字符串以哪种方式进行存储，0代表LATIN1(一个字节存储，默认的方式),1代表UTF16(两个字节存储)
        this.value------byte[]类型，字符串每个字符存储的值
问题：既然字符串默认采用LATIN1存储，则最大可存储到01111111=127,超过127之后怎么办？
我们采用255,256,257作演示例子，
首先我们要知道这三个数字代表什么字符？
System.out.println((char)255); //ÿ
System.out.println((char)256); //Ā
System.out.println((char)257); //ā
```
* 存储过程
```java
//整个抽象过程
    char[] c=new char[]{'ÿ','Ā','ā'};
    String s=new String(c);
    System.out.println(s); //ÿĀā
//第一步 进入到构造函数
 public String(char value[]) {
        this(value, 0, value.length, null);
    }
//调用
    /*
    char[]------['ÿ','Ā','ā']
    int off------0
    int len------3
    Void sig-----null
    */
    String(char[] value, int off, int len, Void sig) {
        //不执行
        if (len == 0) {
            this.value = "".value;
            this.coder = "".coder;
            return;
        }
        /****************************************************************************************************
        //执行
        //但是调用byte[] val = StringUTF16.compress(value, off, len);是这样一个过程，分为两步
        public static byte[] compress(char[] val, int off, int len) {
            byte[] ret = new byte[len];
            //从下面分析可以得到compress(val, off, ret, 0, len)=0，所以函数返回null
            if (compress(val, off, ret, 0, len) == len) {
                return ret;
            }
            return null;
        }
        
        //在将char[]->byte[]过程中，只要有一个字符大于0xFF=255,我们就将byte[]长度返回0
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
        ****************************************************************************************************/
        if (COMPACT_STRINGS) {
            byte[] val = StringUTF16.compress(value, off, len);
            if (val != null) {
                this.value = val;
                this.coder = LATIN1;
                return;
            }
        }
        //从上面分析可以得到此时应该采用UTF16编码
        this.coder = UTF16;
        this.value = StringUTF16.toBytes(value, off, len);
    }
//存储结果
    /*
    char[] value-------['ÿ','Ā','ā']
    int off------------0
    int len------------3
    */
    public static byte[] toBytes(char[] value, int off, int len) {
        //查看源码知道，实际上使val=len*2
        byte[] val = newBytesFor(len);
        for (int i = 0; i < len; i++) {
            putChar(val, i, value[off]);
            off++;
        }
        return val;
    }
    /*
    value[off]--int c把每一个char类型化为32的整形
    ÿ--255--00000000 00000000 00000000 11111111--[0,-1]
    Ā--256--00000000 00000000 00000001 00000000--[1,0]
    ā--257--00000000 00000000 00000001 00000001--[1,1]
    
    ----------------------------------------------------[0,-1,1,0,1,1]
    */
    static void putChar(byte[] val, int index, int c) {
        assert index >= 0 && index < length(val) : "Trusted caller missed bounds check";
        index <<= 1;
        val[index++] = (byte)(c >> HI_BYTE_SHIFT);//截取高8位
        val[index]   = (byte)(c >> LO_BYTE_SHIFT);//截取低8位
    }
//存储最终结果
    char[] c=new char[]{'ÿ','Ā','ā'};
    String s=new String(c);
    System.out.println(s); //ÿĀā
对于字符串"ÿĀā"来说，他的两个不可见的私有属性为：
  this.coder------byte类型，1代表UTF16(两个字节存储)
  this.value------byte[]类型，[0,-1,1,0,1,1]
```
## 思考`字符串编码的过程`
* 既然字符串是以byte[]存储，那是如何编码的呢？
```
预备知识：获取系统信息？
  System.out.println(System.getProperty("java.version"));//11.0.3
  System.out.println(Charset.defaultCharset());//UTF-8
```
* 编码过程
```java
//整个抽象过程
    char[] c=new char[]{'ÿ','Ā','ā'};
    String s=new String(c);
    byte[] b=s.getBytes();//[-61,-65,-60,-128,-60,-127]
    for(byte bb:b){
      System.out.println(bb);
    }
//第一步
    public byte[] getBytes() {
        return StringCoding.encode(coder(), value);
    }
//调用coder()
    byte coder() {
        //COMPACT_STRINGS是静态的常量，为true,也就是coder,此时coder也就是UTF16
        return COMPACT_STRINGS ? coder : UTF16;
    }
//继续调用
   static byte[] encode(byte coder, byte[] val) {
        //执行第一个if,因为文件的默认编码就是UTF-8
        Charset cs = Charset.defaultCharset();
        if (cs == UTF_8) {
            return encodeUTF8(coder, val, true);
        }
        if (cs == ISO_8859_1) {
            return encode8859_1(coder, val);
        }
        if (cs == US_ASCII) {
            return encodeASCII(coder, val);
        }
        StringEncoder se = deref(encoder);
        if (se == null || !cs.name().equals(se.cs.name())) {
            se = new StringEncoder(cs, cs.name());
            set(encoder, se);
        }
        return se.encode(coder, val);
    }
//继续调用
   /*
   byte coder----1 UTF16
   byte[] val----[0,-1,1,0,1,1]
   boolean doReplace----true
   */
    private static byte[] encodeUTF8(byte coder, byte[] val, boolean doReplace) {
        //执行第一个if
        if (coder == UTF16)
            return encodeUTF8_UTF16(val, doReplace);

        if (!hasNegatives(val, 0, val.length))
            return Arrays.copyOf(val, val.length);

        int dp = 0;
        byte[] dst = new byte[val.length << 1];
        for (int sp = 0; sp < val.length; sp++) {
            byte c = val[sp];
            if (c < 0) {
                dst[dp++] = (byte)(0xc0 | ((c & 0xff) >> 6));
                dst[dp++] = (byte)(0x80 | (c & 0x3f));
            } else {
                dst[dp++] = c;
            }
        }
        if (dp == dst.length)
            return dst;
        return Arrays.copyOf(dst, dp);
    }
//继续调用
    private static byte[] encodeUTF8_UTF16(byte[] val, boolean doReplace) {
        int dp = 0;
        int sp = 0;
        //s1=3
        int sl = val.length >> 1;
        byte[] dst = new byte[sl * 3];
        char c;
        //(char)'\u0080'=-128 每一个c都比这个大，不执行
        while (sp < sl && (c = StringUTF16.getChar(val, sp)) < '\u0080') {
            // ascii fast loop;
            dst[dp++] = (byte)c;
            sp++;
        }
        //执行
        while (sp < sl) {
            /*
                //下面的StringUTF16.getChar(val, sp++)是用来提取以UTF16编码的字符
                //byte[] val----[0,-1,1,0,1,1] 执行StringUTF16.getChar(val, 1)
                *****************************************************************************
                index=1---index=2
                val[2] & 0xff---00000001
                ((val[2] & 0xff) << HI_BYTE_SHIFT)---00000001 00000000
                index=3
                val[3] & 0xff---00000000
                ((val[3]   & 0xff) << LO_BYTE_SHIFT)---00000000
                (char)00000001 00000000---(char)256---'Ā'
                ****************************************************************************
                static char getChar(byte[] val, int index) {
                    assert index >= 0 && index < length(val) : "Trusted caller missed bounds check";
                    index <<= 1;
                    return (char)(((val[index++] & 0xff) << HI_BYTE_SHIFT) |
                                  ((val[index]   & 0xff) << LO_BYTE_SHIFT));
                }
            */
            /***************************************************************************************************************************
            int s1=3----sp=0,1,2
            sp=0 c='ÿ'             sp=1
            dp=0 dst[0]=(byte)(1100 0000 | (0000 0000 1111 1111 >>6))=(byte)(1100 0000 | 00 0000 0011)=(byte)(00 1100 0011)=11000011=-61
            dp=1 dst[1]=(byte)(1000 0000 | (0000 0000 1111 1111 & 0011 1111))=(byte)(1011 1111)=10111111=-65
            loop
            loop
            dp=6 dst=[-61,-65,-60,-128,-60,-127]
            ***************************************************************************************************************************/
            c = StringUTF16.getChar(val, sp++);
            if (c < 0x80) {
                dst[dp++] = (byte)c;
            //0x800=16*16*8=2048执行这个
            } else if (c < 0x800) {
                dst[dp++] = (byte)(0xc0 | (c >> 6));
                dst[dp++] = (byte)(0x80 | (c & 0x3f));
            } else if (Character.isSurrogate(c)) {
                int uc = -1;
                char c2;
                if (Character.isHighSurrogate(c) && sp < sl &&
                    Character.isLowSurrogate(c2 = StringUTF16.getChar(val, sp))) {
                    uc = Character.toCodePoint(c, c2);
                }
                if (uc < 0) {
                    if (doReplace) {
                        dst[dp++] = '?';
                    } else {
                        throwUnmappable(sp - 1, 1); // or 2, does not matter here
                    }
                } else {
                    dst[dp++] = (byte)(0xf0 | ((uc >> 18)));
                    dst[dp++] = (byte)(0x80 | ((uc >> 12) & 0x3f));
                    dst[dp++] = (byte)(0x80 | ((uc >>  6) & 0x3f));
                    dst[dp++] = (byte)(0x80 | (uc & 0x3f));
                    sp++;  // 2 chars
                }
            } else {
                // 3 bytes, 16 bits
                dst[dp++] = (byte)(0xe0 | ((c >> 12)));
                dst[dp++] = (byte)(0x80 | ((c >>  6) & 0x3f));
                dst[dp++] = (byte)(0x80 | (c & 0x3f));
            }
        }
        if (dp == dst.length) {
            return dst;
        }
        return Arrays.copyOf(dst, dp);
    }
```
