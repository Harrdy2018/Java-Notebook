## 枚举类
```java
package lukang;

/**
 * SeasonTest
 * 如何定义枚举类
 * jdk5.0之前，自定义枚举类
 */
public class SeasonTest {

  public static void main(String[] args) {
    Season spring=Season.SPRING;
    Season summer=Season.SUMMER;
    Season autumn=Season.AUTNUMN;
    Season winter=Season.WINTER;
    System.out.println(spring);
    System.out.println(spring.getSeasonName());
    System.out.println(spring.getSeasonDesc());
    System.out.println(summer);
    System.out.println(autumn);
    System.out.println(winter);
  }
}

class Season{
  private final String seasonName;
  private final String seasonDesc;
  private Season(String seasonName,String seasonDesc){
    this.seasonName=seasonName;
    this.seasonDesc=seasonDesc;
  };
  public static final Season SPRING=new Season("春天","春暖花开"); 
  public static final Season SUMMER=new Season("夏天","夏日炎炎"); 
  public static final Season AUTNUMN=new Season("秋天","秋高气爽"); 
  public static final Season WINTER=new Season("冬天","寒冬腊月"); 
  /**
   * @return the seasonName
   */
  public String getSeasonName() {
    return seasonName;
  };
  /**
   * @return the seasonDesc
   */
  public String getSeasonDesc() {
    return seasonDesc;
  };
}
```
