package com.lukang.www;
/**
 * 将字符串指定部分进行反转
 */
public class WindowTest {
  public String reverseA(String str,int beginIndex,int endIndex){
    char[] arr=str.toCharArray();
    for(int x=beginIndex,y=endIndex-1;x<y;x++,y--){
      char temp=arr[x];
      arr[x]=arr[y];
      arr[y]=temp;
    }
    return new String(arr);
  }
  public String reverseB(String str,int beginIndex,int endIndex) {
    String reverseStr=str.substring(0,beginIndex);
    for(int i=endIndex-1;i>=beginIndex;i--){
      reverseStr+=str.charAt(i);
    }
    reverseStr+=str.substring(endIndex);
    return reverseStr;
  }
  public String reverseC(String str,int beginIndex,int endIndex) {
    StringBuilder reverseStr=new StringBuilder(str.length());
    reverseStr.append(str.substring(0,beginIndex));
    for(int i=endIndex-1;i>=beginIndex;i--){
      reverseStr.append(str.charAt(i));
    }
    reverseStr.append(str.substring(endIndex));
    return reverseStr.toString();
  }
  public static void main(String[] args) {
    System.out.println(new WindowTest().reverseA("lukang", 1, 5));
    System.out.println(new WindowTest().reverseB("lukang", 1, 5));
    System.out.println(new WindowTest().reverseC("lukang", 1, 5));
  }
}
