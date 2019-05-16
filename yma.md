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
```java
//先加载静态代码块 COMPACT_STRINGS = true; 默认采用 LATIN1编码(一个字符一字节表示)，否则采用UTF16编码(一个字符两个字节表示)

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
            byte[] val = StringUTF16.compress(value, off, len);
            if (val != null) {
                this.value = val;
                this.coder = LATIN1;
                return;
            }
        }
        this.coder = UTF16;
        this.value = StringUTF16.toBytes(value, off, len);
    }
```
