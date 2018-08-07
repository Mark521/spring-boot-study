# hadoop学习笔记

## 一、介绍hadoop



#### 1、三大发行版本

- 05年Apache：原始的版本
- 08年Cloudera：在大型的互联网企业用的多，收费
- 11年Hortonworks：文档较好

#### 2、优势

- 高可靠性：会维护多个工作副本
- 高扩展性：可动态
- 高效性：
- 高容错性：

#### 3、组成

1. HDFS，分布式文件系统
   1. NameNode：存储文件元数据，文件名，文件目录结构，副本数，以及每个文件块列表和块所在的DataNode
   2. DataNode：本地文件系统存储文件块数据，以及块数据校验和
   3. SecondNameNode：监控HDFS状态的辅助后台程序，每隔一段时间获取HDFS快照
2. MapReduce：分布式的离线并行计算框架
   1. 
3. Yarn：作业调度和集群资源管理的框架
   1. ResourceManager：处理客户端请求，启动/监控AppilcaitonMater，监控NodeManager，资源分配与调度
   2. NodeManager：打那个节点上的资源管理，处理来自ResourceManager的命令，处理来自ApplicationMaster的命令
   3. ApplicationMaster：数据切分，为应用程序申请资源，并分配给内部任务，任务监控与容错
   4. Container：对任务运行环境的抽象，封装了CPU，内存等多维资源以及环境变量，启动命令等任务运行的相关信息

#### 4、大数据生态

1. hadoop，spark不是实时计算
2. SparkStreaming实时计算（秒级），Storm实时计算（毫秒，一套新的架构）

![](E:/giteeproject/spring-boot-study/typora/images/%E5%A4%A7%E6%95%B0%E6%8D%AE%E7%94%9F%E6%80%81%E4%BD%93%E7%B3%BB.png)





## 二、Hadoop安装

1、修改权限：vi /etc/sudos

2、赋权限：chown mark:mark module

3、安装jdk

4、hadoop：源码下载是32位环境的jar包

5、tar -xzvf * -C '解压路径'

## 三、Hadoop运行模式