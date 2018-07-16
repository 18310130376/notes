http://www.cnblogs.com/wade-luffy/p/5767811.html



## 独立模式安装并启动

### **1.1 安装配置**

```
mkdir -p /usr/local/services/zookeeper
cd /usr/local/services/zookeeper
wget http://mirror.bit.edu.cn/apache/zookeeper/zookeeper-3.4.10/zookeeper-3.4.10.tar.gz

tar -zxvf zookeeper-3.4.10.tar.gz

cd zookeeper-3.4.10/conf/

cp zoo_sample.cfg zoo.cfg
```

**conf/zoo.cfg**

```
tickTime=2000
# 数据文件夹
dataDir=/usr/local/services/zookeeper/zookeeper-3.4.10/data
#日志文件夹
dataLogDir=/usr/local/services/zookeeper/zookeeper-3.4.10/logs
clientPort=2181
initLimit=10
syncLimit=5
```

tickTime:指定了ZooKeeper的基本时间单位（以毫秒为单位）。

dataDir:存储内存数据快照位置。

clientPort：监听客户端连接端口。

initLimmit：启动zookeeper，从节点至主节点连接超时时间。（上面为10个tickTime）

syncLimit:zookeeper正常运行，若主从同步时间超过syncLimit，则丢弃该从节点。

**环境变量**

```
vim /etc/profile

添加
export ZOOKEEPER_HOME=/usr/local/services/zookeeper/zookeeper-3.4.9/
export PATH=$ZOOKEEPER_HOME/bin:$PATH

#环境变量生效
source /etc/profile
```

**启动服务**

```
zkServer.sh start
```

如果不配置环境变量，则需要在zookeeper下的bin目录执行

```
bin/zkServer.sh start
```

如果输出下面日志则表示成功

```
ZooKeeper JMX enabled by default
Using config: /usr/local/services/zookeeper/zookeeper-3.4.9/bin/../conf/zoo.cfg
Starting zookeeper ... STARTED
```

**查询 zookeeper 状态**

```
zkServer.sh status
```

**关闭 zookeeper 服务**

```
 zkServer.sh stop
```

 如打印如下信息则表明成功关闭：
    ZooKeeper JMX enabled by default
    Using config: /usr/local/services/zookeeper/zookeeper-3.4.9/bin/../conf/zoo.cfg
    Stopping zookeeper ... STOPPED

**重启 zookeeper 服务**

```
 zkServer.sh restart
```

如打印如下信息则表明重启成功：
    ZooKeeper JMX enabled by default
    Using config: /usr/local/services/zookeeper/zookeeper-3.4.9/bin/../conf/zoo.cfg
    ZooKeeper JMX enabled by default
    Using config: /usr/local/services/zookeeper/zookeeper-3.4.9/bin/../conf/zoo.cfg
    Stopping zookeeper ... STOPPED
    ZooKeeper JMX enabled by default
    Using config: /usr/local/services/zookeeper/zookeeper-3.4.9/bin/../conf/zoo.cfg
    Starting zookeeper ... STARTED



### 1.2 连接到zookeeper

```
$ bin/zkCli.sh -server 127.0.0.1:2181
```

 在shell命令喊中使用help查看命令列表

```
[zkshell: 0] help
```

创建一个zookeeper节点

```
create /zk_test my_data
```

获取节点数据

```
get /zk_test
```

修改节点数据

```
set /zk_test junk
```

删除节点

```
delete /zk_test
```

查看路径

```
[zk: 127.0.0.1:2181(CONNECTED) 33] ls /
[zk_test000, zk_test0000000, zk_test00, zk_test00000, zk_test00000000, zookeeper, testRootPath, zkConfig, zk_test]
```



## cruator客户端编程

```
<dependency>
            <groupId>com.netflix.curator</groupId>
            <artifactId>curator-recipes</artifactId>
            <version>1.3.0</version>
</dependency>
```



## 复制模式启动zookeeper

集群;https://blog.csdn.net/a906998248/article/details/50815031

https://www.cnblogs.com/mysql-dba/p/6102253.html

```
tickTime=2000
dataDir=/var/lib/zookeeper
clientPort=2181
initLimit=5
syncLimit=2
server.1=zoo1:2888:3888
server.2=zoo2:2888:3888
server.3=zoo3:2888:3888
```

注意：前一个端口用于leader和follower之间数据交换，后一个端口用于leader选举。







## 统一配置管理

1、公用配置不应该分散存放到各应用中，而是应该抽出来，统一存储到一个公用的位置（最容易想到的办法，放在db中，或统一的分布式cache server中，比如Redis，或其它类似的统一存储，比如ZooKeeper中）

2、对这些公用配置的添加、修改，应该有一个统一的配置管理中心应用来处理（这个也好办，做一个web应用来对这些配置做增、删、改、查即可）

3、当公用配置变化时，子应用不需要重新部署（或重新启动），就能使用新的配置参数（比较容易想到的办法有二个：一是发布/订阅模式，子应用主动订阅公用配置的变化情况，二是子应用每次需要取配置时，都实时去取最新配置）