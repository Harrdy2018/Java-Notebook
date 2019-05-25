# `String`基本知识复习
## String 常用方法
***
* int     length()
* char	  charAt(int index)
* boolean	isEmpty()
* String	toLowerCase()
* String	toUpperCase()
* String	trim()
* boolean	equals(Object anObject)
* boolean	equalsIgnoreCase(String anotherString)
* String	concat(String str)
* int	    compareTo(String anotherString)
* String	substring(int beginIndex)
* String	substring(int beginIndex, int endIndex)
***
* boolean	endsWith(String suffix)
* boolean	startsWith(String prefix)
* boolean	startsWith(String prefix, int toffset)
* boolean	contains(CharSequence s)
* int	    indexOf(String str)
* int	    indexOf(String str, int fromIndex)
* int	    lastIndexOf(String str)
* int	    lastIndexOf(String str, int fromIndex)
***
* String	replace(char oldChar, char newChar)
* String	replace(CharSequence target, CharSequence replacement)
* String	replaceAll(String regex, String replacement)
* String	replaceFirst(String regex, String replacement)
* boolean	matches(String regex)
* String[]	split(String regex)
* String[]	split(String regex, int limit)
## 转换
* String-->short,int,long
```java
Integer.parseInt(String s);
Short.parseShort(String s);
Long.parseLong(String s);
```
* 其他类型-->String
```java
static String	valueOf​(boolean b)	
static String	valueOf​(char c)	
static String	valueOf​(char[] data)	
static String	valueOf​(char[] data, int offset, int count)	
static String	valueOf​(double d)	
static String	valueOf​(float f)	
static String	valueOf​(int i)	
static String	valueOf​(long l)	
static String	valueOf​(Object obj)	
```
* char[]-->String
```java
//String构造器
String​(char[] value)	
String​(char[] value, int offset, int count)
```
* String-->char[]
```
char[]	toCharArray()
void	getChars​(int srcBegin, int srcEnd, char[] dst, int dstBegin)
```
* byte[]-->String
```java
String​(byte[] bytes)	
String​(byte[] bytes, int offset, int length)
```
* String-->byte[]
```java
byte[]	getBytes()
byte[]	getBytes​(String charsetName)	
byte[]	getBytes​(Charset charset)
```
