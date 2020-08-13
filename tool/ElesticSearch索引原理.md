### ElesticSearch索引原理

#### 1、介绍

> ElasticSearch是一个分布式可扩展的实时搜索和分析引擎

ElasticSearch建立全文搜索引擎Lucene基础上的搜索引擎，还可以进行如下工作：

a、分布式实时文件存储，并将每一个字段都编入索引，使其可以被搜索。

b、实时分析的分布式搜索引擎。

c、可以扩展导上百台服务器，处理PB级别的结构化和非结构化数据

#### 2、基本概念

面向文档型数据库，一条数据就相当于一个文档

关系型数据库=>数据库=>表=>行=>列

ElasticSearch=>索引=>类型=>文档=>字段

#### 3、索引

ElasticSearch关键的功能就是索引能力，一切设计都是为了提高搜索的性能。每条数据插入和更新的时候都对这些字段建立索引-----倒排索引。

#### 4、如何做到快速索引

##### a、B-Tree索引

传统数据库采用B-Tree/B+Tree数据结构，减少磁盘的读取，兼顾同时插入和查询的性能问题。

##### b、倒排索引

termIndex=>termDictionary=>PostingList

ID是自建文档ID，通过ID将每个字段进行分类，比如年龄，男女，名字（关键字Term，[id]PostingList)

整型数据好区分，但是字符型数据如何查询呢？

二分法查找term的序列叫做TermDirectionary

只存前缀的B-Tree叫做TermIndex，可以通过TermIndex快速定位到TermDictionary的某个offset，从这个位置开始查询。

TermIndex不需要存下所有的term，而仅仅是他们的一些前缀与termdictionary的block之间的映射关系，在结合FST压缩技术，可以将index缓存导内存中。

##### c、单索引

压缩技术----对postingList中的ID进行压缩。

Frame of Reference

> 增量编码压缩，将大数变小数，按字节存储。用前面的数据存储后面数据的一部分，减少存储空间

[73,300,302,332,343,372]---->[73,227,2,30,11,29]

最后按bit排队，按照字节存储

继续压缩

bitmap

[1,3,4,7,10]--->[1,0,1,1,0,0,1,0,0,1]用0/1表示存在，在Lucene5.0之前就是这样压缩的，但这样效率不高，1亿个文档===12.5M的存储空间，

roaring bitmaps

将postinglist按照65535界限划分，组成键值对（商，余数）

##### d、联合索引

1、利用跳表（Skip List）的数据结构快速做与运算

2、利用bitset按位与

