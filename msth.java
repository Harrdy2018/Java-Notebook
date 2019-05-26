package com.lukang.www;
/**
 *统计子串在字符串中出现的次数
 */
public class WindowTest {
  public int getCount(String str,String sub){
    int index=0;
    int count=0;
    while((index=str.indexOf(sub,index)) !=-1){
      count+=1;
      index+=sub.length();
    }
    return count;
  }
  public static void main(String[] args) {
    System.out.println(new WindowTest().getCount("aaab", "ab"));
  }
}
