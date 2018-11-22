

### Kafka原理深入研究

#### 一、为什么需要 消息系统

##### 消息系统优点：

1、解耦：允许独立扩展两边的处理过程

2、冗余：消息队列把数据持久化直到他们已经被完全处理，避免数据丢失。

3、扩展性：

#### 二、Kafka架构

##### 1、拓扑结构

##### 2、相关概念

##### 3、zookeeper节点

#### 三、producer发布消息

##### 1、写入方式

##### 2、消息路由

##### 3、写入流程

##### 4、producer delivery guarantee

四、borker保存消息

#### 四、borker保存消息

##### 1、存储方式

##### 2、存储策略

##### 3、topic创建和删除

#### 五、KafkaHA

##### 1、replication

##### 2、leader failover

##### 3、broker failover

##### 4、controller failover

#### 六、consumer消费消息

##### 1、consumerAPI

##### 2、consumerGroup

##### 3、消费方式

##### 4、consumer delivery guarantee

##### 5、consumer rebalance



