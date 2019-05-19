package com.lukang.www;

/**
 * 考察点：for循环三个判断条件和循环体的执行循序
 * 第一轮 ABDC
 * 第二轮 BDC
 * 第三轮 B
 * for(1,初始化表达式;2,判断表达式;4,循环后表达式){
 *      3,循环语句
 * }
 * 初始化表达式最先执行，且执行一次
 * 1234-->234-->234-->234
 * TestFor
 */
public class TestFor {
  static boolean out(char c){
    System.out.println(c);
    return true;
  }
  public static void main(String[] args) {
    int i=0;
    for(out('A');out('B') && (i<2) ; out('C')){
      i++;
      out('D');
    }
  }
}
