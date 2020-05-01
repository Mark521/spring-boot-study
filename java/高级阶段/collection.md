# 集合框架

## 一、集合概述

对多个对象进行存储与数组的比较

数组在存储的特点：数组一旦 定义，长度和元素都确定了

缺点：  

长度确定，不可修改，扩展不方便，

数组提供方法非常有限，操作不方便，效率不高。

存放有序和可重复的数据，对于无需，不可重复 的需求不能满足

## 二、框架分类：

Collection：单列数据

List:有序，可重复

ArrayList LinkList Vector

Set：无需，不重复

HashSet,LinkedHashSet,TreeSet

Map：双列数据，key-value数据

HashMap,LinkedHashmap, TreeMap, HashTable, Properties

## 三、Collection接口API（15）

1、add，addAll

2、clear

3、contains调用equals()方法

4、removeAll差集

5、retainAll交集

6、Iterator遍历

```java
迭代器设计模式;
Collection每次iterator都会返回一个对象;
next 先下移，再把位置的元素返回;
默认游标在第一个元素之前;
foreach 方法,集合内部使用Iterator;
增强for循环 不修改数组的内容
   int[] a = {1,2,3};
for(int b : a){
    b = 2;
}
不能改变数组的内容，临时变量
```

## 四、List

### 1、介绍

List:有序，可重复

ArrayList ：jdk1.2线程不安全，效率高，底层使用Object[] elementData存储

LinkList ：底层使用双向链表存储，频繁添加删除效率高

Vector：jdk1.2线程安全，效率低，底层使用Object[] elementData存储

相同点： 都是List的实现类， 存储数据特点：有序，可重复的内容

### 2、Array List源码分析

#### JDK7

底层创建长度为10的Object数组

grow()扩容至1.5倍，Arrays.copyOf(新数组，旧数组)

#### JDK8

在初始化的时初始化为： Object[] element = {}，在第一次调用Add才创建长度为10的数组

后续添加扩容操作一致；

#### 原因：延迟数组初始化，节省内存

### 3、LinkedList源码分析

```java 
Node<E> first;
Node<E> last;
//Node为内部类
add()->{linkedLast()}

```

### 4、Vector源码分析

扩容方式不一样：扩容到二倍

### 5、List的常用方法

## 五、Set接口

### 1、介绍

HashSet：作为Set基本实现类，线程不安全，可存放null值

LinkedHashSet：作为HashSet的子类， 便利内部数据时， 可以按照添加的顺序遍历

Tree：可以按照添加元素的制定属性，进行排序

无序性：不等于随机性，存储的顺序在底层数组中并非按照数组索引的顺序添加，而是通过数据的哈希值排序

不可重复性：保证添加的元素按照equals方法，不能返回true。即相同元素只能添加一个

### 2、添加元素的过程：HashSet为例

先计算元素的Hash值

像HashSet中，添加元素a，先调用元素所在类的hashcode()方法，计算元素的哈希值

此哈希值按着通过某种算法计算出Hash Set在底层数组中的存放位置

如果当前位置没元素，直接添加

如果位置上有其他元素（以链表方式存储元素），比较元素a和元素b的值，

如果哈希值不同，则添加元素a成功

如果hash值相同，则调用元素a所在类的equals方法

equals true 添加失败

equals false 添加成功

> 链表存储时，jdk7中 元素a放到数组中，只想原来元素，JDK8中，原元素在数组中，指向元素a——————七上八下

#### 为什么在HashCode方法中会有31这个数字

1、选择系数的时候要选择尽量大的系数，因为如果计算出来的hash地址越大，所谓的冲突就越少，查询效率也会提高

2、31值占用5bits，相乘造成数据溢出的概率较小

3、31可以由i* 31 == i<<5 -1来表示，现在虚拟机里面都有做优化

4、31是个素数，素数的作用就是如果我用一个数字来诚意这个素数， 最终出来的结果只能被素数本身和被除数与1来整除（减少冲突）

### 4、LinkedHashSet

添加的时候存储位置是无序的， 但是还维护了两个变量，前一个指向后一个，添加的先后顺序。访问效率高于HashSet

### 5、TreeSet,可以按照对象制定属性排序

TreeSet中的数据，要求是相同类的对象。不能添加不同类的对象

对象需要实现Comparable接口

自然排序中，比较两个对象的标准是compareTo返回0 不再是equals()

定制排序中，比较两个对象的标准是compare()返回0， 不再是equals()

