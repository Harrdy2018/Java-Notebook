package com.lukang.www;
/**
 * 将字符串指定部分进行反转
 */
public class WindowTest {
  public String reverse(String str,int startIndex,int endIndex){
    char[] arr=str.toCharArray();
    for(int x=startIndex,y=endIndex;x<y;x++,y--){
      char temp=arr[x];
      arr[x]=arr[y];
      arr[y]=temp;
    }
    return new String(arr);
  }
  public static void main(String[] args) {
    System.out.println(new WindowTest().reverse("lukang", 1, 2));
  }
}
