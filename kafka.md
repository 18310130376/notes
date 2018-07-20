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



# 环境搭建

## 安装

```
wget http://archive.apache.org/dist/kafka/0.8.1.1/kafka_2.10-0.8.1.1.tgz
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

启动kafka

```
bin/kafka-server-start.sh -daemon config/server.properties &
bin/kafka-server-start.sh -daemon config/server1.properties & （一台即可）
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

## 发送消息

Kafka 使用一个简单的命令行producer，从文件中或者从标准输入中读取消息并发送到服务端。默认的每条命令将发送一条消息

运行producer并在控制台中输一些消息，这些消息将被发送到服务端：

```
bin/kafka-console-producer.sh --broker-list localhost:9092 --topic test 
This is a messageThis is another message
```

ctrl+c可以退出发送

接收消息192.168.48.131:2181

```
bin/kafka-console-consumer.sh --zookeeper localhost:2181 --topic test --from-beginning
```

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
ps -ef | grep kafka | grep server.properties
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

# 搭建Kafka开发环境

搭建开发环境需要引入kafka的jar包，一种方式是将Kafka安装包中lib下的jar包加入到项目的classpath中，这种比较简单了。不过我们使用另一种更加流行的方式：使用maven管理jar包依赖。
创建好maven项目后，在pom.xml中添加以下依赖：

```
<dependency>
        <groupId> org.apache.kafka</groupId >
        <artifactId> kafka_2.10</artifactId >
        <version> 0.8.0</ version>
</dependency>
```



# 参考文档

https://www.cnblogs.com/skying555/p/7903457.html

https://www.cnblogs.com/zhaojiankai/p/7181910.html?utm_source=itdadao&utm_medium=referral

https://www.cnblogs.com/zlslch/p/5966004.html