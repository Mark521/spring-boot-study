# 泛型（JDK1.5添加）

## 一、简介

标签

编译时就会进行类型检查， 保证数据的安全。

泛型，不同引用不能相互赋值。

静态方法中不能使用泛型

异常类不能声明为泛型

不能new T[10];

父类有泛型， 子类又没保留父类泛型

子类不保留父类得泛型

1、没有类型， 擦除，相当于Object

2、具体类型

子类保留父类泛型

1、全部保留

2、部分保留

```java
class Father<T1, T2>{}
class Son1 extends Father{}//==class Son extends Father<Object,Object>
class Son extends Father<Integer, String>{}

class Son<T1,T2> extends Father<T1,T2>{}
class Son<T2> extends Father<Integer,T2>{}
```

## 二、泛型方法

1、在方法中出现了泛型结构， 泛型参数与类的泛型参数没有关系，

```java
public <E> List<E> function(E[] arr){}
加 前面得<E>是不让编译器认为E是新类
```



2、泛型方法可以声明为静态，原因，泛型参数实在调用方法时确定得

## 三、泛型在继承方面的体现

```java
List<Object> a = ;
List<String> b = ;
a = b; //编译不通过  a和b不具有字符类关系，会导致混入非String数据

//A 是 B得父类 但G<A> = G<B>;二者不具备字符类关系， 是并列关系
//A是B 得父类， 则 A<G> = B<G>得父类
```

## 四、通配符得使用?

### 1、通配符使用

```java
List<Object> list1 ;
List<String> list2;
//List<?>当成公共父类
List<?> list;
list = list1;
list = list2;

//不能向内部添加数据，除了添加null
```

### 2、有限制条件得通配符

```java
//? extends Person 小于等于person
//? supper Person 大于等于Person
```

