# hadoop学习笔记

## 一、介绍hadoop

### 1、概念

大数据：无法在一定范围内使用常规软件工具进行捕捉，管理和处理的数据集合。需要新处理模式才能更好的

主要解决海量数据存储和海量数据计算

### 2、特点

a)、大量；

b）、高速（双十一）；

c）、多样（图片）：结构化数据越来越少，非结构化数据越来越多，网络日志，音频，视频，图片等

d）、低价值密度（与数据量成反比）

### 3、能干啥

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

### 1、伪分布式

关闭顺序

 	1. HistoryServer
		2. yarn-nodemanager
		3. yarn-resourceManager
		4. hdfs datanode
		5. hdfs namenode

### 2、完全分布式

1. 修改配置、
   1. vi /etc/udev/rules.d/70-presistent-net.rules修改
   2. vi /etc/sysconfig/network-scripts/if*
   3. vi /etc/sysconfig/network
   4. vi /etc/hosts
2. SCP(拉数据，推数据，控制传输数据)
   1. scp -r 本地地址 root@ip:远程地址
   2. scp root@ip:远程地址 本地地址
3. ssh无密登陆
   1. 原理：A服务器生成一对公钥密钥把公钥拷贝到B服务器，再链接是用A私钥加密传输数据，B用公钥解密
   2. 实现：
      1. 生成key ssh-keygen -t rsa；生成两个文件
      2. 拷贝给：ssh-copy-id hadoop101，生成authorized_keys
      3. 拷贝对自己 ssh-copy-id 自己
4. rsync：服务间同步工具
   1. 使用：man rsync
   2. rsync -rvl $pdir/$fname $user@hadoop$host:$pdir
      1. -r递归
      2. -v显示复制过程
      3. -l拷贝符号链接
   3. 实践：rsync -rvl  /opt/tmp root@hadoot103:/opt
   4. 编写集群脚本分发脚本xsync
      1. 要求：循环复制文件到所有节点相同目录
      2. 实现
         1. /usr/local/bin创建文件
         2. basename
         3. dirname
         4. whoami
         5. cd -P去除链接
   5. xcall：在所有主机上执行相同命令
      1. $@
5. 配置集群
   1. hdfs：namenode（最好是独立机器上，独占资源），datanode，secondnamenode（默认和namenode在同一个节点）
   2. yarn：nodeManager（与datanode同步），resourceManager（很耗资源，独占资源）
   3. 配置文件
      1. core-site.xml
      2. hdfs:hadoop-env,hdfs-site,slaves
      3. yarn:yarn-env,yarn-site（可配置日志功能）
      4. mr:mapred-env,mapred-site
   4. 启动
      1. 删除原有数据/data,/logs
      2. namenode -format(102)
      3. start-dfs.sh
      4. 切换103服务器（避开namenode启动resourcemanager）
      5. start-yarn.sh
      6. 注意事项：目录用户权限
   5. 功能测试
      1. 基本功能测试
   6. 时间同步：找一台主服务器，其他服务器每隔一段时间进行同步
      1. ntp是否安装：rpm -qa | grep ntp
      2. 修改配置文件/etc/ntp.conf
      3. 其他服务器编写脚本：crontab -e :*/10 * * * * /usr/sbin/ntpdate hadoop

### 3、源码编译

1、安装包准备

​	ant,maven,hadoop,jdk,protobuf

2、安装库

​	glibc-headers,gcc-c++,make,cmake,openssl-devel,ncurses-devel

## 四、HDFS

### 1）hdfs优缺点

#### 1、优点

​	1、高容错性

​		1多个副本

​		2副本丢失可自动恢复

​	2、适合大数据处理

​		1数据规模

​		2文件规模

​	3、流式数据访问

​		1一次写入，多次读取，不能修改，只能追加

​		2保证数据一致性

​	4、可在廉价机器部署，通过提高副本数，提高可靠性 

#### 2、缺点

1、不适合低延时数据访问，毫秒级，做不到

2、无法对小文件高效的存储，（占用namenode空间浪费内存，小文件存储寻道时间超过读取时间，不可取）

3、并发写入，文件随机修改（一个文件只能一个写，不能多线程，仅支持数据append，不支持文件的随机修改）

### 2-HDFS架构

​	1、文件块：默认128M，HDFS块比磁盘块大，目的式为了最小化寻址开销。寻址时间为传输时间的1%,状态最佳
