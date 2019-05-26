package com.lukang.www;
/**
 *公共最大子串问题
 */
public class WindowTest {
  public String getMaxStr(String str1,String str2){
    String maxStr=(str1.length()>=str2.length()) ? str1:str2;
    String minStr=(str1.length()<str2.length()) ? str1:str2;
    int length=minStr.length();
    for(int i=0;i<length;i++){
      for(int x=0,y=length-i;y<=length;x++,y++){
        String subStr=minStr.substring(x, y);
        if(maxStr.contains(subStr)){
          return subStr;
        }
      }
    }
    return null;
  }
  
  public static void main(String[] args) {
    System.out.println(new WindowTest().getMaxStr("lukang", "angj"));
  }
}
