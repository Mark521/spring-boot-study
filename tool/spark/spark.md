## Spark概念解释

##### 1、RDD宽窄依赖

窄依赖：父RDD和子RDD partition之间的关系是一对一（map，filter，union，）

窄依赖：父RDD和子RDD partition之间的关系是多对一(join with inputs co-partitioned)

宽依赖：父RDD与子RDD partition之间是一对多（groupByKey，join with inputs not co-partitioned）

##### 2、DAG有向无环图

需要查看hadoop->spark框架演化的过程。

##### 3、底层Stage切分

碰到宽依赖切一刀，==stage一组并行的task进程==

> 1、HDFS
>
> 2、rdd1 = sc.textFile...
>
> 3、rdd2 = rdd1.flatMap..
>
> 4、rdd3 = rdd2.mapToPair...
>
> 5、rdd4 = rdd3.reduceByKey...

##### 4、Spark处理数据模式（pipelien管道计算模式）

f3(f2(f1(sc.txtFile))) 类似高阶函数展开的形式。

##### 5、stage并行度？提高并行度？

并行度有Stage中finalRDD的partition个数决定。

提高并行度：reduceByKey(XXX,numpartition)，numpartition默认和父stage相同；join

##### 6、管道数据落地

1、shuffle.write

2、对RDD持久化



