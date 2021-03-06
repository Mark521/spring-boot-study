# Java常用类

## 一、字符串相关类

### 1、String的特性

1、不可变的字符序列

当重新字符串赋值时，需要重写指定内存区域赋值，不能使用原有value进行赋值

当进行字符串拼接时，————————————————————

当修改字符或字符串，

2、字符串是常量，用引号引起来，他们的值在创建后不可改变

3、内部定义final 的字符数组value[]，不可性字符

4、不可被继承

implements Serizable, 支持序列化，

comparable 可比较大小

通过字面量的方式给一个字符串对象赋值，此时字符串在常量池中，常量池中不存放重复数据

如果调用intern()，返回值在常量池中

```java
String s1 = "abc";//字面量定义方式
//存放在方法区的字符串常量池
String s2 = "abc";
s1 = s2;

String a="a";
//创建了两个对象，一个是堆中new结构，另一个是char[]对应的常量池中的数据
String a= new String("a");

//字符串拼接的问题
s1 = "aa";
s3 = "aabb";
s4 = "aa" + "bb";
s3 = s4
//只要有变量参与的都是重新new的，结果就在堆当中
s5 = s1 + "bb";
s5 != s3;

s8 = s5.intern();//返回得到的s8使用常量池已经存在的信息
s2=s8
```

![image-20200429235238241](image-20200429235238241.png)

![image-20200429235359169](image-20200429235359169.png)

![image-20200430000137275](image-20200430000137275.png)

#### 2、JVM结构解析

1、Sun的hotspot，BEA的JRockit、IBM的J9VM

2、堆分为：新生区，养老区，永久存储区（方法区）

### 3、常用方法

1、length

2、charAt：取指定位置的字符

3、isEmpty;

4、trim：去除首尾空格

5、concat：等价于+

6、substring(a,b)：[a,b)

7、replace：

8、replaceAll：正则表达式

### 4、与char[]之间的转换

String --> char[]， String 的 toCharArray()

char[]——>String 调用String的构造器

### 5、与byte[]之间的转换

String——>byte[] 调用getBytes()

byte[]——>String调用构造函数

### 6、StringBuffer、StringBuilder使用

#### 1、区别

String：不可变的字符序列1.0

StringBuffer：可变的字符序列（修改对象，修改了元变量值），1.0，线程安全，效率低

StinrgBuilder：可变的字符序列，1.5新增，线程不安全，效率高

底层都用char[]数组进行存储

```java
StringBuilder sb = new StringBuilder();//创建了一个长度为16的字符数组
StringBuilder sb = new StringBuilder("abc");//创建了一个长度为3+16的字符数组
//扩容问题
默认：value.length << 1 + 2，原有数组复制到新数组;

```

#### 2、常用方法 

1、append

2、delete

3、replace

4、insert

5、reverse：字符序列逆序

6、

## 二、日期时间

### 1、JDK8之前日期API

![image-20200430080503641](image-20200430080503641.png)

```java
/*1、System.currentTimeMillis()19700101000000域当前的毫秒时间戳
2、Java.util.Date;
两个构造器：
new Date()：创建对应当前时间的
new Data(long)：创建指定毫秒数的对象
两个方法：
toString
getTime：毫秒数
3、java.sql.date：对应数据库中的Date的类型
如何实例化
对象转换
4、SimpleDateFormat
5、Calendar:抽象类
a-Calendar.getInstance()
b-创建子类的对象

常用方法：
get:获取属性值
set：设置属性值，修改对象的信息
add：加天数或者属性值
getTime：日历类——>Date
setTime：Date——>日历类
*/
```

### 2、JDK8的日期时间API

1、原因

应该不能修改日期的信息 

偏移量问题：日期都是从1900年开始的，而月份是从0开始

格式化：格式化对Date有用，Calendar无效

此外，不是线程安全的，不能处理闰秒

2、吸收了joda-time_java.time_java.tim.format

```java
//LocalDate LocalTime LocalDateTime
now();//获取当前时间
of();//指定时间 
getXXX();//获取属性
withDayOfMonth();//设置返回日期对象，不改变原对象信息
plusXXX();//加某个属性指返回日期对象，不改变原对象信息
minusXXX();//减某个属性指返回日期对象，不改变原对象信息
```

3、Instant瞬时

```java
Instant.now();//获取本初子午线的时间值
instant.toEpochMilli();//获取毫秒数
Instant.ofEpochMilli();//通过给定毫秒获取实例
```

4、DataTimeFormatter：类似于SimpleDateFormat

```java
//实例化ISO_LOCAL_DATE_TIME;
formatter.format();
formatter.parse();
//本地化ofLocalizedDate， ofLocalizedDateTime
DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT);
//自定义
DateTimeFormatter.ofPattern("yyyy-MM-dd");
```

## 三、Java比较器

自然排序：Comparable：当前对象大于形参正整数，小于负整数，等于返回0

定制排序：Comparator：零时性的比较

## 四、System类

系统及的很多属性和方法

exit(0)正常退出，非零异常退出

## 五、Math类

## 六、BigInteger和BigDecimal 

## 七、枚举类

方式一、JDK5.0前，自定义枚举类

```java
class season{
    private final String name;
    private final String desc;
    private seaon(String name, String desc){}
    public static final seaon SPRING = new Sean(a,b);
    public static final seaon SPRING = new Sean(a,b);
    public static final seaon SPRING = new Sean(a,b);
    public static final seaon SPRING = new Sean(a,b);
}
```

方式二、JDK5.0后，可以使用enum关键字定义枚举类

```Java
//1、提供当前对象，多个对象分号隔开
//父类不是Object， 是java.lang.Enum
enum seaon{
    Spring(a,b),
    Autumn(c,d);
}
```

方法

1、values

2、valueOf(对象名称)：根据提供的名称，返回对应名称的对象，如果没有，则抛异常

3、enmu实现接口，实现抽象方法，相同则在类中定义即可，如果每个对象不同，则需要在每个对象后实现接口信息

## 八、Annotation注解

框架=注解+反射+设计模式

### 1、举例

#### 1、生成文档的注解

#### 2、编译进行格式检查

override deprecated, suppresswarings

#### 3、跟踪代码依赖性，实现配置文件功能

### 2、介绍

jdk5.0新增

代码里的特殊标记，不加入代码的情况下，嵌入添加一些补充信息

### 3、自定义注解

```java
//①注解生命位@interface
public @intrface a{
    //②成员变量，无参数方法形式提供，默认值 default
    //③可以不提供成员变量，成为标记
    String[] value();
}
//元注解 修饰其他注解的注解
@Retention只能修饰注解的生命周期；
    SOURCE：javac时不会保留
    RUNTIME：加载到内存中
    CLASS：编译时有，运行时不执行
@Target：用于修饰注解能修饰那些元素
    TYPE：
    FIELD：
    METHOD：
    ...
@Document：表明所修饰的注解被javadoc解释时保留下来
@Inherited：被修饰的注解具有继承性，继承的子类是否有父类的注解;

JDK8.0新特性;
可重复注解:@Repeatable,其实还是放到新注解的注解数组中（Retention 和Target需要一直），只是显式的能看到可以重复写;
类型注解:1.8 @Target新增 
    TYPE_PARMETER：可注解泛型的变量类
    TYPE_USE：变量的信息;
```

