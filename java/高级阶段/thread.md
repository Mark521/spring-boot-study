# 线程

## 一、基本概念

### 程序——静态代码

完成特定任务，用给某种语言编写的一组指令的集合。

### 进程

程序的一次执行过程，正在执行的一个程序——有生命周期。进程作为资源分配的单位，系统在运行时会为每个京城分配不同的内存区域
a
### 线程：

用户线程和守护线程

1、进程细化为线程，是一个程序内部的一条执行路；

2、若一个进程同一时间并行执行多个线程，就是支持多线程的

3、线程作为调度和执行的单位，每个线程拥有独立的运行栈和程序计数器，线程切换的开销小

4、一个进程的多个线程共享相同的内存单元/内存地址空间，他们从同一堆中分配对象， 可以访问相同的变量和对象。这就使得线程间通信简便高效， 但是多个线程操作共性的系统资源会带来安全问题

### 单核与多核CPU的理解

java——多个线程（main线程，gc垃圾回收线程，异常处理线程）

### 并行和并发

并行：多个CPU同时执行多个线程，多人在做不同 的事

并发：一个CPU（采用时间片）同时执行多个任务，秒杀，多人做同一件事

## 二、创建多线程

#### 1、JDK5.0之前的两种方法1

a、继承Thread的子类

b、重写Thread的run方法

c、创建子类的对象

d、调用对象的start的方法

#### 2、JDK5.0之前的两种方法2

1、实现Runnable接口

2、重写run方法

3、创建实现类的对象

4、将此对象作为参数传递到Thread类的构造器中，创建Thread类的对象

5、通过Thread类的对象调用start(),启动线程，调用当前线程的run-》调用Runnable的run， 如果不为空，则调用Thread的run

#### 3、对比

优先使用Runnable

1、Runnable是实现，没有类的单继承局限性

2、实现方式更适合多个线程有共享数据的情况

相同点：都实现了run方法

#### 4、JDK5.0的新方法1

##### 1、实现Callable接口

##### 2、对比Runnable

有返回值

方法可以跑出异常

支持泛型返回值

需要借助FutureTask类，比如获取返回结果

##### 3、步骤

实现Callable的实现类

重写call方法

创建实现类对象

将对象传入FutureTask中

将FutureTask传入Thread当中，并start

调用Callable中的call方法的返回值

##### 4、优点

返回值，抛出异常，支持泛型

#### 5、JDK5.0新方法2

#### 1、使用线程池

提前创建好很多线程，放入线程池中，直接使用，使用完返回线程池

#### 2、好处

提高响应速度（减少创建新线程的时间）

降低资源消耗（重复利用线程池中线程，不用每次都创建）

便于线程管理（设置属性）：corePoolSize核心池大小，maximumPoolSize：最大线程数，keepAliveTime：线程没有任务时最多保持时间

#### 3、ExcutorService、Excutors

```java
ExchtorService;
//真正的线程池，常见子类ThreadPoolExecutor
//1、方法 execute：执行任务，没有返回值，一般来执行Runnable
//2、方法 submit：执行任务，有返回值，一般执行Callable
Excutors;
//工具类线程池的工厂类，用于创建不同类型的线程池
/**
1、newCachedThreadPool:创建一个可根据需要创建新线程的线程池
2、newFixedThreadPool:一个可重用固定线程数的线程池
3、newSingleThreadExecutor:只有一个线程的线程池
4、newScheduledThreadPool:可安排在给定延迟后运行命令或者定期执行
*/

```



## 三、Thread常用方法

### 1、start

启动当前线程，调用当前线程run；

### 2、run

调用线程的操作

### 3、currentThread

获取当前线程

### 4、setName，getName

当前线程名称

### 5、yeild

当前线程释放当前CPU的执行

### 6、join

在线程A中调用线程B的join方法，A线程进入阻塞状态，线程B完成执行，结束阻塞状态；

### 7、stop

已过时， 调用此方法，强制结束当前线程

### 8、sleep

让当前线程睡眠（线程阻塞）

### 9、isAlive

判断当前线程是否还存活

### 10、线程调度

高优先级抢占低优先级的进程，但只是从概率上讲。

优先级设置：10-1 默认5；

setPriority()，getPriority()，

## 四、声明周期

1、新建

2、就绪

3、运行

4、阻塞

5、死亡



1——2调用start

2——3获取CPU执行权

3——2失去CPU执行权

3——5执行完成，调用stop，出现ERROR，exception

3——4sleep, join(主线程阻塞)，等待同步锁，wait、suspend（挂起）

4——2sleep时间到了，join的线程结束，获取到锁，notify，resume（结束挂起状态）

## 五、线程安全

### 1、描述

多线程执行的不确定性引起执行结果的不稳定

多个线程对账本的同时操作，造成的数据信息不一致问题

### 2、问题

买票过程中出现了重票和错票

原因：某个线程操作车票的过程中，尚未操作完成时，其他线程进入

### 3、解决

加锁

#### 同步代码块

synchronized(同步监视器){

操作共享数据的过程

}

同步监视器：锁，任何一个对象都可以充当锁，多个线程必须要共用同一个锁

实现runnable接口的多线程中， 可以考虑使用this充当监视器，

继承thread类创建多线程的方式中，慎用this充当监视器，可以考虑用Class充当监视器

#### 同步方法

同步监视器为：this

Runnable 加synchronized   的默认是this

extends 加 默认是A.class

同步方法仍然用到同步监视器，

非静态使用this

静态方法使用A.class

#### 懒汉式单例模式安全问题

```java
public class Bank{
    private Bank bank = null;
    private Bank(){}
    public static Bank getInstance(){
        if(bank == null){
            synchronized(Bank.class){
                if(bank == null){
                    bank = new Bank();
                }
            }
        }
        return bank;
    }
}
```

#### 死锁问题

不同线程分别占用对方需要的同步资源不放弃，都在等对方放弃同步资源，形成了线程的死锁。

出现死锁不会异常，不会提示，只是所有线程出现阻塞状态无法继续

### 4、注意

操作同步代码时，只有一个线程参与，其他线程等待——局限性

## 六、Lock锁

JDK5.0新特性，通过显示定义同步锁对象来实现同步，

与Synchronized对比

1、Lock是显式所，sync是隐式锁

2、sync自动释放同步监视器

lock需要手动启动，手动结束

## 七、线程通信

#### 1、注意

**只能出现在同步代码块和同步方法快中**

调用者必须是同步代码块和同步方法的监视器

方法为Object中定义

### 2、方法

wait()：当前进程进入阻塞状态**并释放锁**

notify()：会唤醒被wait的线程，如果有多个，会唤醒优先级高的

notifyAll

#### 3、sleep方法和wait的异同

相同；进入阻塞状态

不同：声明位置不一样，thread中sleep，Object中wait

调用位置：sleep任何，wait必须同步代码块

关于释放锁的问题：都在同步代码块中，sleep不会释放锁，wait会释放锁

#### 4、生产者消费者问题

1、同步机制，三种方法



## 小结锁

#### 一、释放锁

1、当前同步代码块、同步方法执行完成

2、代码块，方法，遇到break，return终止了代码块。

3、遇到未处理的Error和Exception，导致异常结束

4、当前线程在同步代码块，同步方法中执行了wait方法， 当前线程暂停，释放锁

#### 二、不会释放锁

1、同步代码块或方法调用sleep、yield方法暂停当前线程的执行

2、线程执行同步代码是，其他线程调用了该线程的suspend方法将该线程挂起，该线程不会释放锁（应该避免使用suspend和resume来控制程序）