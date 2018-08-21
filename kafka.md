# 导读

1.Kafka独特设计在什么地方？
2.Kafka如何搭建及创建topic、发送消息、消费消息？
3.如何书写Kafka程序？
4.数据传输的事务定义有哪三种？
5.Kafka判断一个节点是否活着有哪两个条件？
6.producer是否直接将数据发送到broker的leader(主节点)？
7.Kafa consumer是否可以消费指定分区消息？
8.Kafka消息是采用Pull模式，还是Push模式？
9.Procuder API有哪两种？
10.Kafka存储在硬盘上的消息格式是什么？



#  Kafka的特性

\- 高吞吐量、低延迟：kafka每秒可以处理几十万条消息，它的延迟最低只有几毫秒，每个topic可以分多个partition, consumer group 对partition进行consume操作。

\- 可扩展性：kafka集群支持热扩展

\- 持久性、可靠性：消息被持久化到本地磁盘，并且支持数据备份防止数据丢失

\- 容错性：允许集群中节点失败（若副本数量为n,则允许n-1个节点失败）

\- 高并发：支持数千个客户端同时读写



# Kafka的使用场景 

 日志收集：一个公司可以用Kafka可以收集各种服务的log，通过kafka以统一接口服务的方式开放给各种consumer，例如hadoop、Hbase、Solr等。

\- 消息系统：解耦和生产者和消费者、缓存消息等。

\- 用户活动跟踪：Kafka经常被用来记录web用户或者app用户的各种活动，如浏览网页、搜索、点击等活动，这些活动信息被各个服务器发布到kafka的topic中，然后订阅者通过订阅这些topic来做实时的监控分析，或者装载到hadoop、数据仓库中做离线分析和挖掘。

\- 运营指标：Kafka也经常用来记录运营监控数据。包括收集各种分布式应用的数据，生产各种操作的集中反馈，比如报警和报告。

\- 流式处理：比如spark streaming和storm

\- 事件源

# 基本概念

Kafka是一个分布式的、可分区的、可复制的消息系统。它提供了普通消息系统的功能，但具有自己独特的设计

这个独特的设计是什么样的呢？

首先让我们看几个基本的消息系统术语：
Kafka将消息以topic为单位进行归纳。
将向Kafka topic发布消息的程序成为producers.
将预订topics并消费消息的程序成为consumer.
Kafka以集群的方式运行，可以由一个或多个服务组成，每个服务叫做一个broker.
producers通过网络将消息发送到Kafka集群，集群向消费者提供消息，如下图所示：

![img](http://www.aboutyun.com/data/attachment/forum/201505/02/225851j2s4eq67aq9llaol.png)

客户端和服务端通过TCP协议通信。Kafka提供了Java客户端，并且对多种语言都提供了支持。

Topics 和Logs

先来看一下Kafka提供的一个抽象概念:topic.
一个topic是对一组消息的归纳。对每个topic，Kafka 对它的日志进行了分区，如下图所示：
![img](http://www.aboutyun.com/data/attachment/forum/201505/02/225851kqq1pnqbq81kblln.png)

每个分区都由一系列有序的、不可变的消息组成，这些消息被连续的追加到分区中。分区中的每个消息都有一个连续的序列号叫做offset,用来在分区中唯一的标识这个消息。
在一个可配置的时间段内，Kafka集群保留所有发布的消息，不管这些消息有没有被消费。比如，如果消息的保存策略被设置为2天，那么在一个消息被发布的两天时间内，它都是可以被消费的。之后它将被丢弃以释放空间。Kafka的性能是和数据量无关的常量级的，所以保留太多的数据并不是问题。

实际上每个consumer唯一需要维护的数据是消息在日志中的位置，也就是offset.这个offset有consumer来维护：一般情况下随着consumer不断的读取消息，这offset的值不断增加，但其实consumer可以以任意的顺序读取消息，比如它可以将offset设置成为一个旧的值来重读之前的消息。

以上特点的结合，使Kafka consumers非常的轻量级：它们可以在不对集群和其他consumer造成影响的情况下读取消息。你可以使用命令行来"tail"消息而不会对其他正在消费消息的consumer造成影响。

将日志分区可以达到以下目的：首先这使得每个日志的数量不会太大，可以在单个服务上保存。另外每个分区可以单独发布和消费，为并发操作topic提供了一种可能。

每个分区都由一个服务器作为“leader”，零或若干服务器作为“followers”,`leader负责处理消息的读和写`，followers则去复制leader.如果leader down了，followers中的一台则会自动成为leader。`集群中的每个服务都会同时扮演两个角色：作为它所持有的一部分分区的leader，同时作为其他分区的followers`，这样集群就会据有较好的负载均衡。



Producers

Producer将消息发布到它指定的topic中,并负责决定发布到哪个分区。通常简单的由负载均衡机制随机选择分区，但也可以`通过特定的分区函数选择分区`。使用的更多的是第二种。

Consumers

发布消息通常有两种模式：队列模式（queuing）和发布-订阅模式(publish-subscribe)。队列模式中，consumers可以同时从服务端读取消息，每个消息只被其中一个consumer读到；发布-订阅模式中消息被广播到所有的consumer中。Consumers可以加入一个consumer 组，共同竞争一个topic，topic中的消息将被分发到组中的一个成员中。同一组中的consumer可以在不同的程序中，也可以在不同的机器上。如果所有的consumer都在一个组中，这就成为了传统的队列模式，在各consumer中实现负载均衡。如果所有的consumer都不在不同的组中，这就成为了发布-订阅模式，所有的消息都被分发到所有的consumer中。更常见的是，每个topic都有若干数量的consumer组，每个组都是一个逻辑上的“订阅者”，为了容错和更好的稳定性，每个组由若干consumer组成。这其实就是一个发布-订阅模式，只不过订阅者是个组而不是单个consumer。



![img](http://www.aboutyun.com/data/attachment/forum/201505/02/225852ng3ur3gmtc9v489o.png) 

由两个机器组成的集群拥有4个分区 (P0-P3) 2个consumer组. A组有两个consumerB组有4个

相比传统的消息系统，Kafka可以很好的保证有序性。
传统的队列在服务器上保存有序的消息，如果多个consumers同时从这个服务器消费消息，服务器就会以消息存储的顺序向consumer分发消息。虽然服务器按顺序发布消息，但是消息是被异步的分发到各consumer上，所以当消息到达时可能已经失去了原来的顺序，这意味着并发消费将导致顺序错乱。为了避免故障，这样的消息系统通常使用“专用consumer”的概念，其实就是只允许一个消费者消费消息，当然这就意味着失去了并发性。

在这方面Kafka做的更好，通过分区的概念，Kafka可以在多个consumer组并发的情况下提供较好的有序性和负载均衡。将每个分区分只分发给一个consumer组，这样一个分区就只被这个组的一个consumer消费，就可以顺序的消费这个分区的消息。因为有多个分区，依然可以在多个consumer组之间进行负载均衡。注意`consumer组的数量不能多于分区的数量`，也就是有多少分区就允许多少并发消费。

Kafka只能保证一个分区之内消息的有序性，`在不同的分区之间是不可以的`，这已经可以满足大部分应用的需求。如果需要topic中所有消息的有序性，那就只能让这个topic只有一个分区，当然也就只有一个consumer组消费它。



# kafka与zookeeper

  一个典型的Kafka集群中包含若干Produce，若干broker（一般broker数量越多，集群吞吐率越高），若干Consumer Group，以及一个Zookeeper集群。Kafka通过Zookeeper管理集群配置，选举leader，以及在Consumer Group发生变化时进行rebalance。Producer使用push模式将消息发布到broker，Consumer使用pull模式从broker订阅并消费消息。

1)Producer端直接连接broker.list列表,从列表中返回TopicMetadataResponse,该Metadata包含Topic下每个partition leader建立socket连接并发送消息.

2)Broker端使用zookeeper用来注册broker信息,以及监控partition leader存活性.

3)Consumer端使用zookeeper用来注册consumer信息,其中包括consumer消费的partition列表等,同时也用来发现broker列表,并和partition leader建立socket连接,并获取消息。

**Zookeeper作用：**管理broker、consumer

创建Broker后，向zookeeper注册新的broker信息，实现在服务器正常运行下的水平拓展。具体的，通过注册watcher，获取partition的信息。

Topic的注册，zookeeper会维护topic与broker的关系，通/brokers/topics/topic.name节点来记录。

Producer向zookeeper中注册watcher,了解topic的partition的消息，以动态了解运行情况，实现负载均衡。Zookeepr不管理producer，只是能够提供当前broker的相关信息。

Consumer可以使用group形式消费kafka中的数据。所有的group将以轮询的方式消费broker中的数据，具体的按照启动的顺序。Zookeeper会给每个consumer group一个ID,即同一份数据可以被不同的用户ID多次消费。因此这就是单播与多播的实现。以单个消费者还是以组别的方式去消费数据，由用户自己去定义。Zookeeper管理consumer的offset跟踪当前消费的offset。



# 环境搭建

## 安装

```
wget http://archive.apache.org/dist/kafka/0.8.1.1/kafka_2.10-0.8.1.1.tgz
wget http://mirrors.hust.edu.cn/apache/kafka/1.1.0/kafka_2.12-1.1.0.tgz
tar -xzf kafka_2.10-0.8.1.1.tgz
```

## 启动服务

Kafka用到了Zookeeper，所有首先启动Zookper，下面简单的启用一个单实例的Zookkeeper服务。可以在命令的结尾加个&符号，这样就可以启动后离开控制台。

启动zookeeper

```
./zkServer.sh start 或者
bin/zkServer.sh start config/zookeeper.properties &
```

配置kafka配置文件

```
vim config/server.properties
```

修改属性 zookeeper.connect=ip:2181,ip2: 2181

kafka最为重要三个配置依次为：broker.id、log.dir、zookeeper.connect

```
zookeeper.connect=172.27.3.18:2181
# Timeout in ms for connecting to zookeeper
zookeeper.connection.timeout.ms=6000
delete.topic.enable=true
zookeeper.session.timeout.ms=30000
```

启动kafka    （参数-daemon表示后台运行） 

```
bin/kafka-server-start.sh -daemon config/server.properties &
bin/kafka-server-start.sh -daemon config/server1.properties & （一台即可）
JMX_PORT=9997 bin/kafka-server-start.sh -daemon config/server.properties &
```

```
[root@wk01 kafka_2.10-0.8.1.1]# jps
2818 Kafka
2857 Jps
2047 QuorumPeerMain
```



## 创建topic

创建一个叫做“test”的topic，它只有一个分区，一个副本

```
bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic test
```

查看topic是否创建成功

```
验证topic是否创建成功
```

topic描述

```
bin/kafka-topics.sh --describe --zookeeper localhost:2181 --topic test
```

列出所有的topic

```
bin/kafka-topics.sh --list --zookeeper localhost:2181
```



## 发送消息

Kafka 使用一个简单的命令行producer，从文件中或者从标准输入中读取消息并发送到服务端。默认的每条命令将发送一条消息

运行producer并在控制台中输一些消息，这些消息将被发送到服务端：

```
bin/kafka-console-producer.sh --broker-list localhost:9092 --topic test 
This is a message
This is another message
```

ctrl+c可以退出发送

接收消息192.168.48.131:2181

```
bin/kafka-console-consumer.sh --zookeeper localhost:2181 --topic test --from-beginning
```

新版本

```
bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic test --from-beginning
```



## 删除topic 

- 删除kafka存储目录（server.properties文件log.dirs配置，默认为"/tmp/kafka-logs"）相关topic目录
- 如果配置了delete.topic.enable=true直接通过命令删除，如果命令删除不掉，直接通过zookeeper-client 删除掉"/brokers/topics/"目录下相关topic节点。

**注意: 如果你要删除一个topic并且重建，那么必须重新启动kafka，否则新建的topic在zookeeper的/brokers/topics/test-topic/目录下没有partitions这个目录，也就是没有分区信息。**



## 搭建broker的集群

刚才只是启动了单个broker，现在启动有3个broker组成的集群，这些broker节点也都是在本机上的

```
cp config/server.properties config/server-1.properties
cp config/server.properties config/server-2.properties
```

在新的文件中修改参数（默认配置为0，不允许重复）

```
  config/server-1.properties:
    broker.id=1
    port=9093
    log.dir=/tmp/kafka-logs-1

  config/server-2.properties:
    broker.id=2
    port=9094
    log.dir=/tmp/kafka-logs-2
```



创建一个拥有3个副本的topic

```
bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 3 --partitions 1 --topic my-replicated-topic
```

现在我们搭建了一个集群，怎么知道每个节点的信息呢？运行“"describe topics”命令就可以了

```
[root@wk01 kafka_2.10-0.8.1.1]# bin/kafka-topics.sh --describe --zookeeper localhost:2181 --topic my-replicated-topic
Topic:my-replicated-topic       PartitionCount:1        ReplicationFactor:3     Configs:
        Topic: my-replicated-topic      Partition: 0    Leader: 0       Replicas: 0,1,2 Isr: 0,1,2
[root@wk01 kafka_2.10-0.8.1.1]#
```

```
[root@wk01 kafka_2.10-0.8.1.1]# bin/kafka-topics.sh --describe --zookeeper localhost:2181 --topic my-replicated-topic03
Topic:my-replicated-topic03     PartitionCount:1        ReplicationFactor:2     Configs:
        Topic: my-replicated-topic03    Partition: 0    Leader: 2       Replicas: 2,1   Isr: 2,1
[root@wk01 kafka_2.10-0.8.1.1]# bin/kafka-topics.sh --describe --zookeeper localhost:2181 --topic my-replicated-topic01
Topic:my-replicated-topic01     PartitionCount:1        ReplicationFactor:2     Configs:
        Topic: my-replicated-topic01    Partition: 0    Leader: 1       Replicas: 1,2   Isr: 1,2
[root@wk01 kafka_2.10-0.8.1.1]#
```

经过发现不同的topic，leader可以不相同。

下面解释一下这些输出。第一行是对所有分区的一个描述，然后每个分区都会对应一行，因为我们只有一个分区所以下面就只加了一行。
leader：负责处理消息的读和写，leader是从所有节点中随机选择的.
replicas：列出了所有的副本节点，不管节点是否在服务中.
isr：是正在服务中的节点.
在我们的例子中，节点0是作为leader运行。

向topic发送消息

```
bin/kafka-console-producer.sh --broker-list localhost:9092 --topic my-replicated-topic
```

消费这些消息

```
bin/kafka-console-consumer.sh --zookeeper localhost:2181 --from-beginning --topic my-replicated-topic
```

测试一下容错能力.Broker 0作为leader运行，现在我们kill掉它

```
linux ： ps -ef | grep kafka | grep server.properties 或者  ps aux | grep server-1.properties
windows：wmic process where "caption = 'java.exe' and commandline like '%server-1.properties%'" get processid

kill -p pid
```

此时查看

```
[root@wk01 kafka_2.10-0.8.1.1]# bin/kafka-topics.sh --describe --zookeeper localhost:2181 --topic my-replicated-topic
Topic:my-replicated-topic       PartitionCount:1        ReplicationFactor:3     Configs:
        Topic: my-replicated-topic      Partition: 0    Leader: 1       Replicas: 0,1,2 Isr: 1,2
[root@wk01 kafka_2.10-0.8.1.1]#
```

虽然最初负责续写消息的leader down掉了，但之前的消息还是可以消费的

```
bin/kafka-console-consumer.sh --zookeeper localhost:2181 --from-beginning --topic my-replicated-topic
```



## 常用命令

|                                                              |                                                 |
| ------------------------------------------------------------ | ----------------------------------------------- |
| bin/kafka-server-start.sh -daemon config/server.properties & |                                                 |
| nohup bin/kafka-server-start.sh config/server.properties > /dev/null 2>&1 & |                                                 |
| bin/kafka-server-stop.sh                                     |                                                 |
| ./kafka-topics.sh --zookeeper localhost:2181 --list          | 查看有哪些主题                                  |
| ./kafka-topics.sh -zookeeper 127.0.0.1:2181 -describe -topic www | 查看topic的详细信息                             |
| ./kafka-reassign-partitions.sh -zookeeper 127.0.0.1:2181 -reassignment-json-file json/partitions-to-move.json -execute | 为topic增加副本                                 |
| ./kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic testKJ1 | 创建topic                                       |
| ./bin/kafka-topics.sh –zookeeper 127.0.0.1:2181 –alter –partitions 20 –topic testKJ1 | 为topic增加partition                            |
| ./kafka-console-producer.sh --broker-list localhost:9092 --topic testKJ1 | kafka生产者客户端命令                           |
| ./kafka-console-consumer.sh -zookeeper localhost:2181 --from-beginning --topic testKJ1 | kafka消费者客户端命令                           |
| ./kafka-server-start.sh -daemon ../config/server.properties  | kafka服务启动                                   |
| ./kafka-run-class.sh kafka.admin.ShutdownBroker --zookeeper 127.0.0.1:2181 --broker #brokerId# --num.retries 3 --retry.interval.ms 60 shutdown broker | 下线broker                                      |
| ./kafka-run-class.sh kafka.admin.DeleteTopicCommand --topic testKJ1 --zookeeper 127.0.0.1:2181 | 删除topic                                       |
| ./kafka-topics.sh --zookeeper localhost:2181 --delete --topic testKJ1 |                                                 |
| ./kafka-run-class.sh kafka.tools.ConsumerOffsetChecker --zookeeper localhost:2181 --group test --topic testKJ1 | 查看consumer组内消费的offset                    |
| ./kafka-consumer-offset-checker.sh --zookeeper 192.168.0.201:12181 --group group1 --topic group1 |                                                 |
| ./kafka-topics.sh --alter --topict_test --zookeeper master:2181 --partitions 10 | 添加分区                                        |
| ./kafka-run-class.sh kafka.tools.ConsumerOffsetChecker --zookeeper 192.168.35.110:10950 --group bo_01 --topic TABLE_IX_TRADE_ALL_BASE（ 在0.9.0.0已经不支持，使用下面new-consumer方式） | //查看分区偏移量                                |
| bin/kafka-consumer-groups.sh --new-consumer --bootstrap-server 127.0.0.1:9292 --list | 查看consumer group列表                          |
| bin/kafka-consumer-groups.sh --zookeeper 127.0.0.1:2181 --list    或者./kafka-consumer-groups.sh --bootstrap-server 192.168.48.131:9092 --list | 查看consumer group列表                          |
| ./kafka-consumer-groups.sh --new-consumer --bootstrap-server 127.0.0.1:9092 --group lx_test --describe | 查看特定consumer group 详情(查看消息是否有堆积) |
| bin/kafka-consumer-groups.sh --zookeeper 127.0.0.1:2181 --group console-consumer-11967 --describe | 查看特定consumer group 详情                     |

## 配置详解

see：http://kafka.apache.org/documentation.html#quickstart

```properties
broker.id=0  #当前机器在集群中的唯一标识，和zookeeper的myid性质一样
port=19092 #当前kafka对外提供服务的端口默认是9092
host.name=192.168.7.100 #这个参数默认是关闭的，在0.8.1有个bug，DNS解析问题，失败率的问题。
num.network.threads=3 #这个是borker进行网络处理的线程数
num.io.threads=8 #这个是borker进行I/O处理的线程数
log.dirs=/opt/kafka/kafkalogs/ #消息存放的目录，这个目录可以配置为“，”逗号分割的表达式，上面的num.io.threads要大于这个目录的个数这个目录，如果配置多个目录，新创建的topic他把消息持久化的地方是，当前以逗号分割的目录中，那个分区数最少就放那一个
socket.send.buffer.bytes=102400 #发送缓冲区buffer大小，数据不是一下子就发送的，先回存储到缓冲区了到达一定的大小后在发送，能提高性能
socket.receive.buffer.bytes=102400 #kafka接收缓冲区大小，当数据到达一定大小后在序列化到磁盘
socket.request.max.bytes=104857600 #这个参数是向kafka请求消息或者向kafka发送消息的请请求的最大数，这个值不能超过java的堆栈大小
num.partitions=1 #默认的分区数，一个topic默认1个分区数
log.retention.hours=168 #默认消息的最大持久化时间，168小时，7天
log.retention.bytes=-1	控制log文件最大尺寸
message.max.byte=5242880  #消息保存的最大值5M
default.replication.factor=2  #kafka保存消息的副本数，如果一个副本失效了，另一个还可以继续提供服务
replica.fetch.max.bytes=5242880  #取消息的最大直接数
log.segment.bytes=1073741824 #这个参数是：因为kafka的消息是以追加的形式落地到文件，当超过这个值的时候，kafka会新起一个文件
log.retention.check.interval.ms=300000 #每隔300000毫秒去检查上面配置的log失效时间（log.retention.hours=168 ），到目录查看是否有过期的消息如果有，删除
log.cleaner.enable=false #是否启用log压缩，一般不用启用，启用的话可以提高性能
zookeeper.connect=192.168.7.100:12181,192.168.7.101:12181,192.168.7.107:1218 #设置zookeeper的连接端口    （多个使用逗号分隔）
zookeeper.connection.timeout.ms=6000
advertised.listeners=PLAINTEXT://192.168.135.133:9092  （如果没配置则使用listeners）
listeners=PLAINTEXT://:9092  （多个以逗号分隔）
delete.topic.enable=true   //删除topic
auto.create.topics.enable=false
```

我们需要修改 

```properties
1、broker.id：每台机器不能一样
2、zookeeper.connect：因为我有3台zookeeper服务器，所以在这里zookeeper.connect设置为3台，必须全部加进去
3、listeners：在配置集群的时候，必须设置，不然以后的操作会报找不到leader的错误
broker.id=0  每台服务器的broker.id都不能相同
#listeners
listeners=PLAINTEXT://node1:9092
#日志的目录
log.dirs=/home/hadoop/app/kafka_2.11-1.0.0/logs/
#设置zookeeper的连接端口
zookeeper.connect=192.168.7.100:12181,192.168.7.101:12181,192.168.7.107:12181
```

hostname和端口是用来建议给生产者和消费者使用的，如果没有设置，则使用listeners，如果listeners没配置则kafka服务将使用java.net.InetAddress.getCanonicalHostName()来获取这个hostname和port，基本就是localhost 。

 如果你没有配置advertised.listeners，就使用listeners的配置通告给消息的生产者和消费者，这个过程是在生产者和消费者获取源数据 

![img](http://www.aboutyun.com/data/attachment/forum/201409/28/143558xhiezfhyzzderwxd.png) 

Consumer主要配置 

![img](http://www.aboutyun.com/data/attachment/forum/201409/28/143559mzz6kyphyca2rhr2.png) 

Producer主要配置 

![img](http://www.aboutyun.com/data/attachment/forum/201409/28/143559bglynil7c1usij1i.png) 



写入数据配置

```
props.put("compression.type", "gzip");
props.put("linger.ms", "50");
props.put("acks", "all");
props.put("retries ", 30);
props.put("reconnect.backoff.ms ", 20000);
props.put("retry.backoff.ms", 20000);
```



# 搭建Kafka开发环境

搭建开发环境需要引入kafka的jar包，一种方式是将Kafka安装包中lib下的jar包加入到项目的classpath中，这种比较简单了。不过我们使用另一种更加流行的方式：使用maven管理jar包依赖。
创建好maven项目后，在pom.xml中添加以下依赖：

```
 <dependency>
        <groupId>org.apache.kafka</groupId>
        <artifactId>kafka_2.12</artifactId>
        <version>1.1.0</version>
    </dependency>
<dependency>
        <groupId>org.apache.kafka</groupId>
        <artifactId>kafka-clients</artifactId>
        <version>1.1.0</version>
</dependency>
```

# KafkaOffsetMonitor安装部署

下载

```
https://github.com/quantifind/KafkaOffsetMonitor/releases/download/v0.2.1/KafkaOffsetMonitor-assembly-0.2.1.jar
```

启动

编写shell启动比较方便

```
#! /bin/bash
java -Xms512M -Xmx512M -Xss1024K -XX:PermSize=256m -XX:MaxPermSize=512m -cp KafkaOffsetMonitor-assembly-0.2.1.jar com.quantifind.kafka.offsetapp.OffsetGetterWeb --zk 192.168.135.133:2181 --port 8089 --refresh 10.seconds --retain 7.days 1>/mydata/kafkamonitorlogs/stdout.log 2>/mydata/kafkamonitorlogs/stderr.log &
```



# zookeeper在kafka中的作用

一 、**Broker注册** 

Broker在zookeeper中保存为一个临时节点，节点的路径是/brokers/ids/[brokerid],每个节点会保存对应broker的IP以及端口等信息 

# Kafka中的消息是否会丢失和重复消费

https://blog.csdn.net/u012050154/article/category/7059799

# 消息重试对顺序消息的影响

对于一个有着先后顺序的消息A、B，正常情况下应该是A先发送完成后再发送B，但是在异常情况下，在A发送失败的情况下，B发送成功，而A由于重试机制在B发送完成之后重试发送成功了。 这时对于本身顺序为AB的消息顺序变成了BA 

max.in.flight.requests.per.connection = 1（若没有启用retries，则不必调整该参数）如果您没有启动retries亦或是调整之后还发现乱序，可以结合日志进一步研究发生乱序的原因。 



# kafka Consumer均衡算法，partition的个数和消费组组员个数的关系

https://blog.csdn.net/qq_20641565/article/details/59746101

**kafka的Consumer均衡算法**

有一个topic：lijietest，然后这个topic的partition和他们所在的broker的图如下：

![这里写图片描述](https://img-blog.csdn.net/20170302234212764?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvcXFfMjA2NDE1NjU=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

1.其中 broker有两个，也就是服务器有两台。

2.partition有6个，分布按照如图所示，按照哈希取模的算法分配。

3.消费者有8个，他们属于同一个消费组。

**如果按照如图所示，那么这一个消费组中的消费者会怎么取kafka的数据呢？** 
其实kafka的消费端有一个均衡算法，算法如下：

1.A=(partition数量/同分组消费者总个数（同groupid的线程数）) 
2.M=对上面所得到的A值小数点第一位向上取整 
3.计算出该消费者拉取数据的patition合集：Ci = [P(M*i ),P((i + 1) * M -1)]

按照如图所示，那么这里：

A=6/8=0.75

M=1

C0=[P(1*0),P((0+1)*1-1)]=[P0,P0] 
同理：

C1=[P(1*1),P((1+1)*1-1)]=[P1,P1] 
C2=[P(1*2),P((2+1)*1-1)]=[P2,P2] 
C3=[P(1*3),P((3+1)*1-1)]=[P3,P3] 
C4=[P(1*4),P((4+1)*1-1)]=[P4,P4] 
C5=[P(1*5),P((5+1)*1-1)]=[P5,P5] 
C6=[P(1*6),P((6+1)*1-1)]=[P6,P6] 
C7=[P(1*7),P((7+1)*1-1)]=[P7,P7]

那么按照上面的算法： 
C0消费者消费P0的数据 
C1消费者消费P1的数据 
C2消费者消费P2的数据 
C3消费者消费P3的数据 
C4消费者消费P4的数据 
C5消费者消费P5的数据

C6消费者消费P6的数据 
C7消费者消费P7的数据

但是partition只有P0-P5根本就没有P6和P7，所以这两个消费者相当于是会被闲置的，就相当于占用资源，却没什么用，所以在这里真正起到作用的就是C0-C5。

如下图所示：

![这里写图片描述](https://img-blog.csdn.net/20170302234452253?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvcXFfMjA2NDE1NjU=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

**如果这个消费组里面的消费者少于partition数量呢（比如5个）？**

那么还是依葫芦画瓢，根据上面的算法：

A=6/5=1.2 
M=2

C0=[P(2*0),P((0+1)*2-1)]=[P0,P1] 
C1=[P(2*1),P((1+1)*2-1)]=[P2,P3] 
C2=[P(2*2),P((2+1)*2-1)]=[P4,P5]

C3=[P(2*3),P((3+1)*2-1)]=[P6,P7] 
C4=[P(2*4),P((4+1)*2-1)]=[P8,P9]

同上面一样C3和C4没有起到任何作用。

如下所示： 
![这里写图片描述](https://img-blog.csdn.net/20170302234708945?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvcXFfMjA2NDE1NjU=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

**总结：**

1.按照如上的算法，所以如果kafka的消费组需要增加组员，最多增加到和partition数量一致，超过的组员只会占用资源，而不起作用；

2.kafka的partition的个数一定要大于消费组组员的个数，并且partition的个数对于消费组组员取模一定要为0，不然有些消费者会占用资源却不起作用；

3.如果需要增加消费组的组员个数，那么也需要根据上面的算法，调整partition的个数



假设一个topic test 被groupA消费了，现在启动另外一个新的groupB来消费test，默认test-groupB的offset不是0，而是没有新建立，除非当test有数据的时候，groupB会收到该数据，该条数据也是第一条数据，groupB的offset也是刚初始化的ofsert, 除非用显式的用–from-beginnging 来获取从0开始数据 



# Producer消息发送的应答机制

```
 设置发送数据是否需要服务端的反馈,三个值0,1,-1。
0: producer不会等待broker发送ack。 
1: 当leader接收到消息之后发送ack。
-1: 当所有的follower都同步消息成功后发送ack。
request.required.acks=0。
```



# 实战

## 代码配置

第一步：配置num.partitions数量。

注意：在配置文件server.properties中指定了partition的数量num.partitions。这指的是多单个topic的partition数量之和。若有多个broker,可能partition分布在不同的节点上，则多个broker的所有partitioin数量加起来为num.partitions

0.7中producer的配置有几项是相排斥的，设置了其一，就不能设置其二
比如：
　　broker.list 与 zk.connect 不能同时设置
　　broker.list 与 partitioner.class 不能同时设置
如果这么干，编译时无所谓，运行时会抛异常



第二步：通过消息的key决定消息落在哪个partition（非必须）

**1，**指定broker**

props.put("broker.list", "0:10.10.10.10:9092");//直接连接kafka
设置这项后，就不能设置partitioner.class了，可是我在运行的时候发现，此时所有的数据都发往10.10.10.10的4个分区，并没有只发给一个分区。我换了syncproducer里的send(topic,partitionid,list)都没用。
**2，指定partition**
props.put("partitioner.class","com.kafka.myparitioner.CidPartitioner");
props.put("zk.connect", "10.10.10.10:2181");//连接zk

上面的 com.kafka.myparitioner.CidPartitioner 为自己实现的类，注意要自己实现完整的包名
CidPartitioner继承了Partitioner类，其中实现的partition方法指定了通过key计算partition的方法



第三步：在kafka中，同一个topic，被分成了多个partition，这多个partition之间是互相独立的

之所以要分成多个partition，是为了提高并发度，多个partition并行的进行发送/消费，但这却没有办法保证消息的顺序问题。

一个解决办法是，一个topic只用一个partition，但这样很显然限制了灵活性。

还有一个办法就是，所有发送的消息，用同一个key，这样同样的key会落在一个partition里面

总结：

1：分区的总数由server.properties中指定了partition的数量num.partitions决定

2：生产者数据落在哪个partition由消息的key决定

3：如果数据落在同一个partition，就不需要多个consumer组了。



## 从头开始消费topic全量数据



消费者要从头开始消费某个topic的全量数据，需要满足2个条件（spring-kafka）：

```
（1）使用一个全新的"group.id"（就是之前没有被任何消费者使用过）;

（2）指定"auto.offset.reset"参数的值为earliest；
```

对应的spring-kafka消费者客户端配置参数为：

```
<!-- 指定消费组名 -->
<entry key="group.id" value="fg11"/>
<!-- 从何处开始消费,latest 表示消费最新消息,earliest 表示从头开始消费,none表示抛出异常,默认latest -->
<entry key="auto.offset.reset" value="earliest"/>
```

 

注意：从kafka-0.9版本及以后，kafka的消费者组和offset信息就不存zookeeper了，而是存到broker服务器上，所以，如果你为某个消费者指定了一个消费者组名称（group.id），那么，一旦这个消费者启动，这个消费者组名和它要消费的那个topic的offset信息就会被记录在broker服务器上。

比如我们为消费者A指定了消费者组（group.id）为fg11，那么可以使用如下命令查看消费者组的消费情况：

```
bin/kafka-consumer-groups.sh --bootstrap-server 172.17.6.10:9092 --describe --group fg11
```

显示结果如下：

```
TOPIC   PARTITION  CURRENT-OFFSET  LOG-END-OFFSET  LAG  CONSUMER-ID                                      HOST              CLIENT-ID 
friend  0          6               6               0    consumer-1-08c856a3-ae39-4f73-a2da-4de1795c6ad4  /192.168.207.127  consumer-1
friend  1          2               2               0    consumer-1-08c856a3-ae39-4f73-a2da-4de1795c6ad4  /192.168.207.127  consumer-1
friend  2          4               4               0    consumer-1-08c856a3-ae39-4f73-a2da-4de1795c6ad4  /192.168.207.127  consumer-1
```

 

其实friend这个topic共有3个分区，消息总数为12条，其实在消费者A启动之前，这12条消息已经被其他某个组的消费者消费过了。而我们虽然为消费者A指定了一个全新的group.id为fg11，但是如果我们在启动消费者A之前，指定的"auto.offset.reset"参数的值是latest而不是earliest的话（就算你停止消费者，然后改为earliest也是没有用的），启动之后它将不会消费以前的消息，除非friend这个topic的分区中有了新的消息它才会消费。



所以一定要在消费者启动之前就保证group.id是全新的，而且要指定earliest而不是latest。



## 指定的分区写入数据

```
producer.send(new ProducerRecord<String, String>(topic,2, businessKey, messageStr),new Callback() {
					
		@Override
		public void onCompletion(RecordMetadata metadata, Exception exception) {
		System.out.println("Producer Message: Partition:"+metadata.partition()+",Offset:"+metadata.offset());
		System.out.println(messageStr);
}
```



## kafka笔记-Kafka在zookeeper中的存储结构

### 一、topic注册信息

/brokers/topics/[topic] 

存储某个topic的partitions所有分配信息

```
Schema:
 
{
    "version": "版本编号目前固定为数字1",
    "partitions": {
        "partitionId编号": [
            同步副本组brokerId列表
        ],
        "partitionId编号": [
            同步副本组brokerId列表
        ],
        .......
    }
}
 
Example:
{
"version": 1,
"partitions": {
"0": [1, 2],
"1": [2, 1],
"2": [1, 2],
}
}
说明：紫红色为patitions编号，蓝色为同步副本组brokerId列表
```

### 二.partition状态信息

/brokers/topics/[topic]/partitions/[0...N]  其中[0..N]表示partition索引号

/brokers/topics/[topic]/partitions/[partitionId]/state

```
Schema:
 
{
"controller_epoch": 表示kafka集群中的中央控制器选举次数,
"leader": 表示该partition选举leader的brokerId,
"version": 版本编号默认为1,
"leader_epoch": 该partition leader选举次数,
"isr": [同步副本组brokerId列表]
}
 
 
Example:
 
{
"controller_epoch": 1,
"leader": 2,
"version": 1,
"leader_epoch": 0,
"isr": [2, 1]
}
```



### 三、Broker注册信息

/brokers/ids/[0...N]                 

每个broker的配置文件中都需要指定一个数字类型的id(全局不可重复),此节点为临时znode(EPHEMERAL)

```
Schema:
 
{
    "jmx_port": jmx端口号,
    "timestamp": kafka broker初始启动时的时间戳,
    "host": 主机名或ip地址,
    "version": 版本编号默认为1,
    "port": kafka broker的服务端端口号,由server.properties中参数port确定
}
 
 
Example:
 
{
    "jmx_port": 6061,
    "timestamp":"1403061899859"
    "version": 1,
    "host": "192.168.1.148",
    "port": 9092
}
```



### 四、Kafka Java API操作topic

创建topic

```
ZkUtils zkUtils = ZkUtils.apply("localhost:2181", 30000, 30000, JaasUtils.isZkSecurityEnabled());
// 创建一个单分区单副本名为t1的topic
AdminUtils.createTopic(zkUtils, "t1", 1, 1, new Properties(), RackAwareMode.Enforced$.MODULE$);
zkUtils.close();
```

删除topic

```
ZkUtils zkUtils = ZkUtils.apply("localhost:2181", 30000, 30000, JaasUtils.isZkSecurityEnabled());
// 删除topic 't1'
AdminUtils.deleteTopic(zkUtils, "t1");
zkUtils.close();
```

查询topic

```
ZkUtils zkUtils = ZkUtils.apply("localhost:2181", 30000, 30000, JaasUtils.isZkSecurityEnabled());
// 获取topic 'test'的topic属性属性
Properties props = AdminUtils.fetchEntityConfig(zkUtils, ConfigType.Topic(), "test");
// 查询topic-level属性
Iterator it = props.entrySet().iterator();
while(it.hasNext()){
    Map.Entry entry=(Map.Entry)it.next();
    Object key = entry.getKey();
    Object value = entry.getValue();
    System.out.println(key + " = " + value);
}
zkUtils.close();
```

修改topic

```
ZkUtils zkUtils = ZkUtils.apply("localhost:2181", 30000, 30000, JaasUtils.isZkSecurityEnabled());
Properties props = AdminUtils.fetchEntityConfig(zkUtils, ConfigType.Topic(), "test");
// 增加topic级别属性
props.put("min.cleanable.dirty.ratio", "0.3");
// 删除topic级别属性
props.remove("max.message.bytes");
// 修改topic 'test'的属性
AdminUtils.changeTopicConfig(zkUtils, "test", props);
zkUtils.close();
```



```
public class AdminClientTest {
 
    private static final String TEST_TOPIC = "test-topic";
 
    public static void main(String[] args) throws Exception {
        Properties props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092,localhost:9093");
 
        try (AdminClient client = AdminClient.create(props)) {
            describeCluster(client);
            createTopics(client);
            listAllTopics(client);
            describeTopics(client);
            alterConfigs(client);
            describeConfig(client);
            deleteTopics(client);
        }
    }
 
    /**
     * describe the cluster
     * @param client
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static void describeCluster(AdminClient client) throws ExecutionException, InterruptedException {
        DescribeClusterResult ret = client.describeCluster();
        System.out.println(String.format("Cluster id: %s, controller: %s", ret.clusterId().get(), ret.controller().get()));
        System.out.println("Current cluster nodes info: ");
        for (Node node : ret.nodes().get()) {
            System.out.println(node);
        }
    }
 
    /**
     * describe topic's config
     * @param client
     */
    public static void describeConfig(AdminClient client) throws ExecutionException, InterruptedException {
        DescribeConfigsResult ret = client.describeConfigs(Collections.singleton(new ConfigResource(ConfigResource.Type.TOPIC, TEST_TOPIC)));
        Map<ConfigResource, Config> configs = ret.all().get();
        for (Map.Entry<ConfigResource, Config> entry : configs.entrySet()) {
            ConfigResource key = entry.getKey();
            Config value = entry.getValue();
            System.out.println(String.format("Resource type: %s, resource name: %s", key.type(), key.name()));
            Collection<ConfigEntry> configEntries = value.entries();
            for (ConfigEntry each : configEntries) {
                System.out.println(each.name() + " = " + each.value());
            }
        }
 
    }
 
    /**
     * alter config for topics
     * @param client
     */
    public static void alterConfigs(AdminClient client) throws ExecutionException, InterruptedException {
        Config topicConfig = new Config(Arrays.asList(new ConfigEntry("cleanup.policy", "compact")));
        client.alterConfigs(Collections.singletonMap(
                new ConfigResource(ConfigResource.Type.TOPIC, TEST_TOPIC), topicConfig)).all().get();
    }
 
    /**
     * delete the given topics
     * @param client
     */
    public static void deleteTopics(AdminClient client) throws ExecutionException, InterruptedException {
        KafkaFuture<Void> futures = client.deleteTopics(Arrays.asList(TEST_TOPIC)).all();
        futures.get();
    }
 
    /**
     * describe the given topics
     * @param client
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static void describeTopics(AdminClient client) throws ExecutionException, InterruptedException {
        DescribeTopicsResult ret = client.describeTopics(Arrays.asList(TEST_TOPIC, "__consumer_offsets"));
        Map<String, TopicDescription> topics = ret.all().get();
        for (Map.Entry<String, TopicDescription> entry : topics.entrySet()) {
            System.out.println(entry.getKey() + " ===> " + entry.getValue());
        }
    }
 
    /**
     * create multiple sample topics
     * @param client
     */
    public static void createTopics(AdminClient client) throws ExecutionException, InterruptedException {
        NewTopic newTopic = new NewTopic(TEST_TOPIC, 3, (short)3);
        CreateTopicsResult ret = client.createTopics(Arrays.asList(newTopic));
        ret.all().get();
    }
 
    /**
     * print all topics in the cluster
     * @param client
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static void listAllTopics(AdminClient client) throws ExecutionException, InterruptedException {
        ListTopicsOptions options = new ListTopicsOptions();
        options.listInternal(true); // includes internal topics such as __consumer_offsets
        ListTopicsResult topics = client.listTopics(options);
        Set<String> topicNames = topics.names().get();
        System.out.println("Current topics in this cluster: " + topicNames);
    }
}
```



# 名词概念

## Offset

Offset专指Partition以及User Group而言，记录某个user group在某个partiton中当前已经消费到达的位置。

## User group

为了便于实现MQ中的多播，重复消费等引入的概念。如果ConsumerA以及ConsumerB同在一个UserGroup，那么ConsumerA消费的数据ConsumerB就无法消费了。

即：所有usergroup中的consumer使用一套offset。

User1，User2同属一个userGroup时，即表示二者共用一套Offset。因每个partition 的offset只能由一个线程维护，因此注定了每个UserGroup里只能有一个消费线程对一个partition进行消费。

同样，如果希望实现多播，那就User1和User2用两个userGroup。

## Broker

物理概念，指服务于Kafka的一个node。

## topic

MQ中的抽象概念，是一个消费标示。用于保证Producer以及Consumer能够通过该标示进行对接。可以理解为一种Naming方式。

## partition

Topic的一个子概念，一个topic可具有多个partition，但Partition一定属于一个topic。

值得注意的是：

- 在实现上都是以每个Partition为基本实现单元的。
- 消费时，每个消费线程最多只能使用一个partition。
- 一个topic中partition的数量，就是每个user group中消费该topic的最大并行度数量。

## 副本问题的提出

日志副本策略是可靠性的核心问题之一，其实现方式也是多种多样的。包括无主模型，通过paxos之类的协议保证消息顺序，但更简单直接的方式是使用主从结构，主决定顺序，从拷贝主的信息。

如果主不挂，从节点没有存在的意义。但主挂了时，我们需要从备份节点中选出一个主。与此同时，更重要的是：保证一致性。在这里一致性是指:

```
主ack了的消息，kafka切换主之后，依然可被消费。
主没有ack的消息，kafka切换主之后，依然没有被存储。
```

因此这里产生了一个trade off：Leader应该什么时候ack呢？

这个问题简直是分布式环境里永恒（最坑爹）的主题之一了。其引申出的本质问题是，你到底要什么？

- 要可靠性

当然可以，leader收到消息之后，等follower 返回ok了ack，慢死。但好处是，主挂了，哪个follower都可以做主，大家数据都一样嘛

- 要速度

当然可以，leader收到消息写入本地就ack，然后再发给follower。问题也很显而易见，最坏得情况下，有个消息leader返回ack了，但follower因为各种原因没有写入，主挂了，丢数据了。

# 如何选举Leader

　　最简单最直观的方案是，所有Follower都在Zookeeper上设置一个Watch，一旦Leader宕机，其对应的ephemeral znode会自动删除，此时所有Follower都尝试创建该节点，而创建成功者（Zookeeper保证只有一个能创建成功）即是新的Leader，其它Replica即为Follower。
　　但是该方法会有3个问题： 　　

- split-brain 这是由Zookeeper的特性引起的，虽然Zookeeper能保证所有Watch按顺序触发，但并不能保证同一时刻所有Replica“看”到的状态是一样的，这就可能造成不同Replica的响应不一致
- herd effect 如果宕机的那个Broker上的Partition比较多，会造成多个Watch被触发，造成集群内大量的调整
- Zookeeper负载过重 每个Replica都要为此在Zookeeper上注册一个Watch，当集群规模增加到几千个Partition时Zookeeper负载会过重。

　　Kafka 0.8.*的Leader Election方案解决了上述问题，它在所有broker中选出一个controller，所有Partition的Leader选举都由controller决定。controller会将Leader的改变直接通过RPC的方式（比Zookeeper Queue的方式更高效）通知需为此作出响应的Broker。同时controller也负责增删Topic以及Replica的重新分配。



# 重置kafka的offset

用下面命令可以查询到topic:DynamicRange broker:SparkMaster:9092的offset的最小值：

```
./kafka-run-class.sh kafka.tools.GetOffsetShell --broker-list 192.168.48.131:9092 --topic topic01 --time -2
```

输出

```
topic01:2:0
topic01:1:0
topic01:0:275789
```

查询offset的最大值：

```
./kafka-run-class.sh kafka.tools.GetOffsetShell --broker-list 192.168.48.131:9092 --topic topic01 --time -1
```

输出

```
topic01:2:8
topic01:1:8
topic01:0:359976
```

从上面的输出可以看出topic:topic01有三个partition:0 offset范围为:[275789,359976]

# 设置consumer group的offset 

`旧版本`：

启动zookeeper client

```
$ /opt/cloudera/parcels/CDH/lib/zookeeper/bin/zkCli.sh
```

通过下面命令设置consumer group:group1 topic:topic01 partition:0的offset为1288:

```
set /consumers/group1/offsets/topic01/0 359975
```

注意如果你的kafka设置了zookeeper root，比如为/kafka，那么命令应该改为：

```
set /kafka/consumers/group1/offsets/topic01/0 359975
```

重启相关的应用程序，就可以从设置的offset开始读数据了。 

或者

```
bin/kafka-consumer-groups.sh --zookeeper z1:2181,z2:2181,z3:2181 --group test-consumer-group --topic test --execute --reset-offsets --to-offset 359975
```



`新版本`：

新版的消费位置信息不再zookeeper上了,

查看

```
./kafka-consumer-groups.sh --new-consumer --bootstrap-server 192.168.48.131:9092 --group group1 --describe

TOPIC           PARTITION  CURRENT-OFFSET  LOG-END-OFFSET  LAG             CONSUMER-ID     HOST            CLIENT-ID
topic02         1          497893          497893          0               -               -               -
topic02         0          3706316         3706316         0               -               -                 -
topic02         2          787658          787658          0               -               -               -
topic01         1          8               8               0               -               -   
          -
topic01         2          8               8               0               -               -               -
topic01         0          359976          359976          0               -               -   -

```



```
./kafka-consumer-groups.sh --bootstrap-server 192.168.48.131:9092,192.168.48.132:9092 --group group1 --topic topic01 --execute --reset-offsets --to-offset 359975

这里设置为359975，只会对最后一行有影响。如果这里设置为6，则对倒数第二行，倒数第三行设置为6，最后一行设置为275789（前面查询的此分区的最小值）


参数解析： --bootstrap-server 代表你的kafka集群 你的offset保存在topic中
--group 代表你的消费者分组
--topic 代表你消费的主题
--execute 代表支持复位偏移
--reset-offsets 代表要进行偏移操作
--to-offset 代表你要偏移到哪个位置 是long类型数值，只能比前面查询出来的小
还有其他的--to- ** 方式可以自己验证 本人验证过--to-datetime 没有成功
```

注意：修改offset之前保证要修改的consumer 是不活动状态就是得先停应用



通过

```
./kafka-run-class.sh kafka.tools.DumpLogSegments --files /tmp/kafka-logs/topic01-0/00000000000000275789.log --print-data-log

可以查看到每条消息的offset: 359975 position: 7547672  CreateTime: 1534826578785
```







# 参考文档

http://kafka.apache.org/10/documentation.html

https://www.cnblogs.com/zlslch/p/5966004.html

kafka

https://www.cnblogs.com/liuwei6/p/6900686.html



源码解析

http://www.cnblogs.com/huxi2b/p/6124937.html



https://blog.csdn.net/csolo/article/details/52389646





