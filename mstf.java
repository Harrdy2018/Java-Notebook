package com.lukang.www;
/**
 * 实现trim()
 */
public class WindowTest {

  public static void main(String[] args) {
    String s=" lukang ";
    char[] c=s.toCharArray();
    for(char kk:c){
      System.out.println(kk);
    }
    System.out.println("*************");
    int left=0;
    int right=c.length-1;
    for(;left<c.length;left++){
      if(c[left]!=' '){
        break;
      }
    }
    for(;right>=0;right--){
      if(c[right]!=' '){
        break;
      }
    }
    System.out.println(left);
    System.out.println(right);
    System.out.println(s.substring(left, right+1));
  }
}
