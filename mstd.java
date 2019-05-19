package com.lukang.www;

/**
 * 首先明白两个概念：
 * 第一：byte 字节整数 有符号 占一个字节 -128~127
 *       char 字符    无符号 占两个字节 0~65535
 *       int  整数    有符号 占四个字节
 * 第二：扩展分为符号扩展和无符号扩展
 * 符号扩展：扩展后的高位置为立即数的最高位
 * 无符号扩展：扩展后的所有高位置0
 * 例如：         0x8000         0x1000
 * 符号扩展   0xFFFF8000     0x00001000
 * 无符号扩展 0x00008000     0x00001000
 * 
 * 分析一：为什么(byte)-1是-1?
 * java中-1默认是int类型，也就是解释int-->>byte
 * int -1===0xffffffff
 * 窄化原始类型转化(narrowing primitive conversion),即截取
 * (byte)(int -1)===0xff===-1
 * 分析二：byte-->>char麻烦，因为byte有符号，而char无符号
 * 拓宽并窄化原始类型的转换(widening and narrowing primitive conversion)===byte-->>int-->>char
 * 规则描述从较窄的转换成较宽的符号扩展行为：
 *     如果最初的数值类型是有符号的，那么就执行符号扩展；
 *     如果它是char，那么不管它将要被转换成什么类型，都执行无符号扩展(零扩展)
 * (char)(byte类型的-1)--符号扩展--0xff ff
 * (int)(char类型)--无符号扩展--0x00 00 ff ff===65535
 * TestIntCharByte
 */
public class TestIntCharByte {

  public static void main(String[] args) {
    System.out.println((int)(char)(byte)-1);//65535
  }
}
