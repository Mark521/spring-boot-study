# JDK8新特性

## 简介

#### 1、速度更快：红黑树

#### 2、代码更少：增加了Lambda表达式

#### 3、StreamAPI

#### 4、便于并行

#### 5、最大化减少空指针异常

#### 6、Nashorn引擎，允许在JVM上运行JS应用



## 一、Lambda

### 1、格式

-> lambda操作符

左边  形参列表（其实就是接口中方法的形参列表）

右边  方法体（抽象方法的方法体）

### 2、六种格式

1、无参有返回值

2、有参无返回值

3、参数类型可以省略

4、只需要一个参数， 参数 小括号可以删除

5、有两个以上参数， 多条执行语句，可以有返回值

6、只有一条语句， return和大括号可以省略

### 3、本质

作为函数式接口的实例

## 二、函数式编程

@FunctionInterface

如果一个抽象方法只声明了一个抽象方法，那么此接口就成为函数式接口

#### 四大函数式接口

消费者接口：Cumsumer<T>

供给型接口：Supplier<T>

函数时接口：Function<T,R>

断定型接口：Predicate<T>

## 三、方法引用构造器引用

### 1、使用情景

当要传递给Lambda的方法已经有实现了可以使用方法引用，也是函数时接口的实例

### 2、格式

类（对象）:: 方法名

对象：： 非静态方法

类：：静态方法

类：：非静态方法

### 3、方法引用使用要求

接口中的抽象方的形参列表与返回值类型与方法应勇的方法的形参列表和返回值类型相同

## 四、StreamAPI

### 1、介绍

强调数据的运算（CPU）

Collection时存储数据的  与内存打交道

### 2、注意

不存储数据， 不改变数据源，延迟，意味着他们会等到需要结果的时候才执行

### 3、创建

1、通过集合创建

顺序流  list.stream()   并行流 list.parallelStream()

2、通过数组

Arrays.stream()

3、Stream的of（）

4、创建无限流

Stream.iterate

Stream.generate

### 4、中间操作

1、筛选与切片

filter：筛选

limit：截断流

skip：跳过前面几个数据

distinct：去重

2、映射

map：

flatmap：把当中每个值都转换位另一个流， 然后把所有流连接成一个流

3、排序

sorted：自然排序

sorted（Comparator com）：定制排序

### 5、终止操作

1、匹配与查找

allMatch

anyMatch

noneMatch

findFirst

2、归约

reduce

reduce

3、收集

collect：list

## 五、Optional类

