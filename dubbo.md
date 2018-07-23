# dubbo角色

**节点角色说明：**

- **Provider:** 暴露服务的服务提供方。
- **Consumer:** 调用远程服务的服务消费方。
- **Registry:** 服务注册与发现的注册中心。
- **Monitor:** 统计服务的调用次调和调用时间的监控中心。
- **Container:** 服务运行容器。



# 简单代码结构

1、定义服务接口: (该接口需单独打包，在服务提供方和消费方共享)

```java
public interface CustomerService {
	public String getName();
}
```

 2、在服务提供方实现接口：(对服务消费方隐藏实现)

```java
public class CustomerServiceImpl implements CustomerService{
	@Override
	public String getName() {
		System.out.print("我打印");
		return "打印结果";
	}
}
```

3：依赖包

dubbo-2.5.3.jar ，log4j.jar，netty-3.5.7.Final.jar，slf4j.jar，slf4j-log4j.jar，zkclient.jar，zookeeper.jar

4、用Spring配置声明暴露服务applicationProvider.xml：

```xml
<?xml version="1.0" encoding="UTF-8"?>  
<beans xmlns="http://www.springframework.org/schema/beans"  
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:dubbo="http://code.alibabatech.com/schema/dubbo"  
    xsi:schemaLocation="http://www.springframework.org/schema/beans  
        http://www.springframework.org/schema/beans/spring-beans.xsd  
        http://code.alibabatech.com/schema/dubbo  
        http://code.alibabatech.com/schema/dubbo/dubbo.xsd ">   
    <!-- 具体的实现bean -->  
    <bean id="demoService" class="com.jinbin.service.customer.CustomerServiceImpl" />  
    <!-- 提供方应用信息，用于计算依赖关系 -->  
    <dubbo:application name="xixi_provider"  />    
    <!-- 使用multicast广播注册中心暴露服务地址   
    <dubbo:registry address="multicast://localhost:1234" />-->   
    <!-- 使用zookeeper注册中心暴露服务地址 -->  
    <dubbo:registry address="zookeeper://192.168.1.3:2181" />     
    <!-- 用dubbo协议在20880端口暴露服务 -->  
    <dubbo:protocol name="dubbo" port="20880" />  
    <!-- 声明需要暴露的服务接口 -->  
    <dubbo:service interface="com.jinbin.service.customer.CustomerService" ref="demoService" /> 
</beans>  

```

5、加载Spring配置，并调用远程服务：(也可以使用IoC注入)

```java
public class DubooProvider {
	public static void main(String[] args) {
	    ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(  
                new String[]{"applicationProvider.xml"});  
        context.start();  
        System.out.println("Press any key to exit.");  
        try {
			System.in.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	}
}
```



6、服务消费者

①、新建个配置文件applicationConsumer.xml，内容如下：

```xml
<?xml version="1.0" encoding="UTF-8"?>  
<beans xmlns="http://www.springframework.org/schema/beans"  
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:dubbo="http://code.alibabatech.com/schema/dubbo"  
    xsi:schemaLocation="http://www.springframework.org/schema/beans  
        http://www.springframework.org/schema/beans/spring-beans.xsd  
        http://code.alibabatech.com/schema/dubbo  
        http://code.alibabatech.com/schema/dubbo/dubbo.xsd ">        
    <!-- 消费方应用名，用于计算依赖关系，不是匹配条件，不要与提供方一样 -->  
    <dubbo:application name="consumer-of-helloworld-app" />     
      <!-- 使用multicast广播注册中心暴露发现服务地址 -->  
    <dubbo:registry  protocol="zookeeper" address="zookeeper://192.168.1.3:2181" />       
      <!-- 生成远程服务代理，可以和本地bean一样使用demoService -->  
    <dubbo:reference id="demoService" interface="com.jinbin.service.customer.CustomerService" /> 
</beans>  
```

为了在web中使用，我们在web.xml中配置在spring启动读取过程中

```xml
   <context-param>
		<param-name>contextConfigLocation</param-name>
		<param-value>/WEB-INF/application.xml /WEB-INF/applicationConsumer.xml</param-value>
    </context-param>
```

接口调用

```java
  @Autowired CustomerService demoService ;	

  @RequestMapping(value="duboo1")
  public void duboo1(){
		   demoService.getName();
}
```



# Dubbo服务的运行方式 

**1、使用Servlet容器运行（Tomcat、Jetty等）----不可取**  

缺点：增加复杂性（端口、管理） 浪费资源（内存）

官方：服务容器是一个standalone的启动程序，因为后台服务不需要Tomcat或JBoss等Web容器的功能，如果硬要用Web容器去加载服务提供方，增加复杂性，也浪费资源。

**2、自建Main方法类来运行（Spring容器） ----不建议（本地调试可用）**  

缺点： Dobbo本身提供的高级特性没用上 自已编写启动类可能会有缺陷 

官方：服务容器只是一个简单的Main方法，并加载一个简单的Spring容器，用于暴露服务。

**3、使用Dubbo框架提供的Main方法类来运行（Spring容器）----建议使用**  

优点：框架本身提供（com.alibaba.dubbo.container.Main） 

可实现优雅停机（ShutdownHook）  

官方：服务容器的加载内容可以扩展，内置了spring, jetty, log4j等加载，可通过Container扩展点进行扩展

Dubbo是通过JDK的ShutdownHook来完成优雅停机的，所以如果用户使用"kill -9 PID"等强制关闭指令，是不会执行优雅停机的，只有通过"kill PID"时，才会执行。

**原理：**

- 服务提供方
  - 停止时，先标记为不接收新请求，新请求过来时直接报错，让客户端重试其它机器。
  - 然后，检测线程池中的线程是否正在运行，如果有，等待所有线程执行完成，除非超时，则强制关闭。
- 服务消费方
  - 停止时，不再发起新的调用请求，所有新的调用在客户端即报错。
  - 然后，检测有没有请求的响应还没有返回，等待响应返回，除非超时，则强制关闭。

设置优雅停机超时时间，缺省超时时间是10秒：(超时则强制关闭)

```
< dubbo:application ...>
     < dubbo:parameter key = "shutdown.timeout" value = "60000" /> <!-- 单位毫秒 -->
</dubbo:application>
```

如果ShutdownHook不能生效，可以自行调用：

```
ProtocolConfig.destroyAll();
```



# 打包

maven打包方式： 

```
<resources>
            <resource>
                <targetPath>${project.build.directory}/classes</targetPath>
                <directory>src/main/resources</directory>
                <filtering>true</filtering>
                <includes>
                    <include>**/*.xml</include>
                    <include>**/*.properties</include>
                </includes>
            </resource>
            <!-- 结合com.alibaba.dubbo.container.Main，需要重点掌握-->
            <resource>
                <targetPath>${project.build.directory}/classes/META-INF/spring</targetPath>
                <directory>src/main/resources/spring</directory>
                <filtering>true</filtering>
                <includes>
                    <include>spring-context.xml</include>
                </includes>
            </resource>
 </resources>
```

**官网声明： Spring Container**

- **自动加载META-INF/spring目录下的所有Spring配置。**

- **配置：(配在java命令-D参数或者dubbo.properties中)**

- **dubbo.spring.config=classpath\*:META-INF/spring/*.xml ----配置spring配置加载位置**

  **所以声明必须使用maven方式配置才能将spring配置文件打包到META-INF/spring目录下**

引入相关插件 

```
<plugins>
            <!-- 打包jar文件时，配置manifest文件，加入lib包的jar依赖 -->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-jar-plugin</artifactId>
                <configuration>
                    <classesDirectory>target/classes/</classesDirectory>
                    <archive>
                        <manifest>
                            <mainClass>com.alibaba.dubbo.container.Main</mainClass>
                            <addClasspath>true</addClasspath>
                            <classpathPrefix>lib/</classpathPrefix>
                        </manifest>
                        <manifestEntries>
                            <Class-Path>.</Class-Path>
                        </manifestEntries>
                    </archive>
                </configuration>
 </plugin>
</plugins>
```



# dubbo-admin的使用

下载dubbo-admin-2.5.3.war

 将其放到tomcat下面，配置dubbo.properties

```shell
vi webapps/ROOT/WEB-INF/dubbo.properties

dubbo.registry.address=zookeeper://127.0.0.1:2181
dubbo.admin.root.password=root
dubbo.admin.guest.password=guest
```

启动

```
./bin/startup.sh
```

访问http://localhost:8088 即可

具体步骤：

Dubbo管控台可以对注册到 zookeeper 注册中心的服务或服务消费者进行管理，但管控台是否正常对Dubbo服务没有影响，管控台也不需要高可用，因此可以单节点部署。

IP: 192.168.1.100
部署容器：apache-tomcat-7.0.57 
端口：8080

1、下载最新版的Tomcat7: 
$wget http://mirrors.hust.edu.cn/apache/tomcat/tomcat-7/v7.0.57/bin/apache-tomcat-7.0.57.tar.gz 
2、解压tomcat安装包，安装在/home/jqlin/dev/
$ cd /home/jqlin/dev/
$ tar -zxvf apache-tomcat-7.0.57.tar.gz 
3、移除/home/jqlin/dev/apache-tomcat-7.0.57/webapps目录下的所有文件： 
$ cd /home/jqlin/dev/apache-tomcat-7.0.57/webapps
$ rm -rf * 
4、上传Dubbo管理控制台程序dubbo-admin-2.5.3.war到/home/jqlin/dev/apache-tomcat-7.0.57/webapps
5、解压并把目录命名为ROOT: 
$ unzip dubbo-admin-2.5.3.war -d ROOT

把dubbo-admin-2.5.3.war移到/home/jqlin/dev/package目录备份 
$ mv dubbo-admin-2.5.3.war /home/jqlin/dev/package

6、配置dubbo.properties： 
$ vi ROOT/WEB-INF/dubbo.properties

```
dubbo.registry.address=zookeeper://192.168.1.100:2181 
dubbo.admin.root.password=dubbo 
dubbo.admin.guest.password=dubbo
```

注：以上密码在正式上生产前要修改

7、防火墙开启8080端口，用root用户修改/etc/sysconfig/iptables， 
\# vi /etc/sysconfig/iptables 
增加： 
\## apache-tomcat-7.0.57:8080 
-A INPUT -m state --state NEW -m tcp -p tcp --dport 8080 -j ACCEPT 
重启防火墙： 
\# service iptables restart

8、启动Tomat7 
$ cd /home/jqlin/dev/apache-tomcat-7.0.57/bin/

$ ./startup.sh

9、浏览http://192.168.1.100:8080/



# dubbo 配置文件详解

**一、dubbo常用配置**

```
<dubbo:service/> 服务配置，用于暴露一个服务，定义服务的元信息，一个服务可以用多个协议暴露，一个服务也可以注册到多个注册中心。
eg、<dubbo:service ref="demoService" interface="com.unj.dubbotest.provider.DemoService" />

<dubbo:reference/> 引用服务配置，用于创建一个远程服务代理，一个引用可以指向多个注册中心。
eg、<dubbo:reference id="demoService" interface="com.unj.dubbotest.provider.DemoService" />

<dubbo:protocol/> 协议配置，用于配置提供服务的协议信息，协议由提供方指定，消费方被动接受。
eg、<dubbo:protocol name="dubbo" port="20880" />

<dubbo:application/> 应用配置，用于配置当前应用信息，不管该应用是提供者还是消费者。
eg、<dubbo:application name="xixi_provider" />
    <dubbo:application name="hehe_consumer" />

<dubbo:module/> 模块配置，用于配置当前模块信息，可选。
<dubbo:registry/> 注册中心配置，用于配置连接注册中心相关信息。
eg、<dubbo:registry address="zookeeper://192.168.2.249:2181" />

<dubbo:monitor/> 监控中心配置，用于配置连接监控中心相关信息，可选。
<dubbo:provider/> 提供方的缺省值，当ProtocolConfig和ServiceConfig某属性没有配置时，采用此缺省值，可选。
<dubbo:consumer/> 消费方缺省配置，当ReferenceConfig某属性没有配置时，采用此缺省值，可选。
<dubbo:method/> 方法配置，用于ServiceConfig和ReferenceConfig指定方法级的配置信息。
<dubbo:argument/> 用于指定方法参数配置。[![复制代码](http://common.cnblogs.com/images/copycode.gif](javascript:void(0);)
```

**二、服务调用超时设置**
![img](https://images2015.cnblogs.com/blog/270324/201609/270324-20160910122039394-1078905366.png)
上图中以timeout为例，显示了配置的查找顺序，其它retries, loadbalance, actives也类似。
方法级优先，接口级次之，全局配置再次之。
如果级别一样，则消费方优先，提供方次之。

其中，服务提供方配置，通过URL经由注册中心传递给消费方。
建议由服务提供方设置超时，因为一个方法需要执行多长时间，服务提供方更清楚，如果一个消费方同时引用多个服务，就不需要关心每个服务的超时设置。
理论上ReferenceConfig的非服务标识配置，在ConsumerConfig，ServiceConfig, ProviderConfig均可以缺省配置。

**三、启动时检查** 
Dubbo缺省会在启动时检查依赖的服务是否可用，不可用时会抛出异常，阻止Spring初始化完成，以便上线时，能及早发现问题，默认check=true。

如果你的Spring容器是懒加载的，或者通过API编程延迟引用服务，请关闭check，否则服务临时不可用时，会抛出异常，拿到null引用，如果check=false，总是会返回引用，当服务恢复时，能自动连上。

可以通过check="false"关闭检查，比如，测试时，有些服务不关心，或者出现了循环依赖，必须有一方先启动。

```
1、关闭某个服务的启动时检查：(没有提供者时报错)
<dubbo:reference interface="com.foo.BarService" check="false" />

2、关闭所有服务的启动时检查：(没有提供者时报错)  写在定义服务消费者一方
<dubbo:consumer check="false" />

3、关闭注册中心启动时检查：(注册订阅失败时报错)
<dubbo:registry check="false" />
```

引用缺省是延迟初始化的，只有引用被注入到其它Bean，或被getBean()获取，才会初始化。
如果需要饥饿加载，即没有人引用也立即生成动态代理，可以配置：

```
<dubbo:reference interface="com.foo.BarService" init="true" />
```

**四、订阅**
1、问题
为方便开发测试，经常会在线下共用一个所有服务可用的注册中心，这时，如果一个正在开发中的服务提供者注册，可能会影响消费者不能正常运行。

2、解决方案
可以让服务提供者开发方，只订阅服务(开发的服务可能依赖其它服务)，而不注册正在开发的服务，通过直连测试正在开发的服务。

```
禁用注册配置：
<dubbo:registry address="10.20.153.10:9090" register="false" />
或者：
<dubbo:registry address="10.20.153.10:9090?register=false" />
```

**五、回声测试(测试服务是否可用)**
回声测试用于检测服务是否可用，回声测试按照正常请求流程执行，能够测试整个调用是否通畅，可用于监控。
所有服务自动实现EchoService接口，只需将任意服务引用强制转型为EchoService，即可使用。

**六、延迟连接** 
延迟连接，用于减少长连接数，当有调用发起时，再创建长连接。
只对使用长连接的dubbo协议生效。

```
<dubbo:protocol name="dubbo" lazy="true" />
```

**七、令牌验证** 
防止消费者绕过注册中心访问提供者，在注册中心控制权限，以决定要不要下发令牌给消费者，注册中心可灵活改变授权方式，而不需修改或升级提供者

```
1、全局设置开启令牌验证：
<!--随机token令牌，使用UUID生成-->
<dubbo:provider interface="com.foo.BarService" token="true" />

<!--固定token令牌，相当于密码-->
<dubbo:provider interface="com.foo.BarService" token="123456" />

2、服务级别设置开启令牌验证：
<!--随机token令牌，使用UUID生成-->
<dubbo:service interface="com.foo.BarService" token="true" />

<!--固定token令牌，相当于密码-->
<dubbo:service interface="com.foo.BarService" token="123456" />

3、协议级别设置开启令牌验证：
<!--随机token令牌，使用UUID生成-->
<dubbo:protocol name="dubbo" token="true" />

<!--固定token令牌，相当于密码-->
<dubbo:protocol name="dubbo" token="123456" />
```

**八、日志适配** 
缺省自动查找：log4j、slf4j、jcl、jdk

可以通过以下方式配置日志输出策略：dubbo:application logger="log4j"/>

访问日志：
如果你想记录每一次请求信息，可开启访问日志，类似于apache的访问日志。此日志量比较大，请注意磁盘容量。

将访问日志输出到当前应用的log4j日志：

```
<dubbo:protocol accesslog="true" />
```

将访问日志输出到指定文件：

```
<dubbo:protocol accesslog="http://10.20.160.198/wiki/display/dubbo/foo/bar.log" />
```

**九、配置Dubbo缓存文件**

配置方法如下：

```
<dubbo:registryfile=”${user.home}/output/dubbo.cache” />
```

注意：
文件的路径，应用可以根据需要调整，保证这个文件不会在发布过程中被清除。如果有多个应用进程注意不要使用同一个文件，避免内容被覆盖。

这个文件会缓存：
注册中心的列表
服务提供者列表

有了这项配置后，当应用重启过程中，Dubbo注册中心不可用时则应用会从这个缓存文件读取服务提供者列表的信息，进一步保证应用可靠性。



# dubbo常用配置及注意事项

```
1、启动时检查
缺省会在启动时检查依赖的服务是否可用，不可用时会抛出异常，阻止Spring初始化完成，以便上线时，能及早发现问题，默认check=true。

关闭所有服务的启动时检查：(没有提供者时报错)
<dubbo:consumer check="false" />

关闭某个服务的启动时检查：(没有提供者时报错)
<dubbo:reference interface="com.foo.BarService" check="false" />
其它的启动时检查还包括：注册中心

2、直连提供者
在开发及测试环境下，经常需要绕过注册中心，只测试指定服务提供者，这时候可能需要点对点直连，
点对点直联方式，将以服务接口为单位，忽略注册中心的提供者列表。
<dubbo:reference id="xxxService" interface="com.alibaba.xxx.XxxService" url="dubbo://localhost:20890" />  

3、服务分组
当一个接口有多种实现时，可以用group区分。
provider：
<dubbo:service group="feedback" interface="com.xxx.IndexService" ref="indexServiceFeedback" />
<dubbo:service group="member" interface="com.xxx.IndexService"   ref="indexServiceMember" />

cosumer：
<dubbo:reference id="feedbackIndexService" group="feedback" interface="com.xxx.IndexService" />
<dubbo:reference id="memberIndexService" group="member" interface="com.xxx.IndexService" />

4、多版本
当一个接口实现，出现不兼容升级时，可以用版本号过渡，版本号不同的服务相互间不引用。
一般处理步骤
1）在低压力时间段，先升级一半提供者为新版本
2）再将所有消费者升级为新版本
3）然后将剩下的一半提供者升级为新版本
<dubbo:service interface="com.foo.BarService" version="1.0.0" />
<dubbo:service interface="com.foo.BarService" version="2.0.0" />
 
5、异步调用
可完成并行调用多个远程服务。异步总是不等待返回。
<dubbo:reference id="fooService" interface="com.alibaba.foo.FooService">
    <dubbo:method name="findFoo" async="true" />
</dubbo:reference>

6、延迟暴露
如果你的服务需要Warmup时间，比如初始化缓存，等待相关资源就位等，可以使用delay进行延迟暴露。
<dubbo:provider delay="-1" />
当然，也可以配置到服务级别，但有些需要地方需要注意。

7、dubbo:protocol属性
threadpool：线程池类型，可选：fixed/cached ，默认fixed 。
threads ：服务线程池大小(固定大小) ，默认为100

payload：请求及响应数据包大小限制，单位：字节，默认为88388608(=8M)
如：<dubbo:protocol name="dubbo" port="27001" threadpool="cached" threads="20"/>

ThreadPool
fixed 固定大小线程池，启动时建立线程，不关闭，一直持有。(缺省)
cached 缓存线程池，空闲一分钟自动删除，需要时重建。
limited可伸缩线程池，但池中的线程数只会增长不会收缩。(为避免收缩时突然来了大流量引起的性能问题)。

8、dubbo:application
<dubbo:application name="xxx_service" />
name必填。当前应用名称，用于注册中心计算应用间依赖关系，注意：消费者和提供者应用名不要一样
```

# 基于Dubbo框架构建分布式服务（集群容错&负载均衡）

Dubbo是Alibaba开源的分布式服务框架，我们可以非常容易地通过Dubbo来构建分布式服务，并根据自己实际业务应用场景来选择合适的集群容错模式，这个对于很多应用都是迫切希望的，只需要通过简单的配置就能够实现分布式服务调用，也就是说服务提供方（Provider）发布的服务可以天然就是集群服务，比如，在实时性要求很高的应用场景下，可能希望来自消费方（Consumer）的调用响应时间最短，只需要选择Dubbo的Forking Cluster模式配置，就可以对一个调用请求并行发送到多台对等的提供方（Provider）服务所在的节点上，只选择最快一个返回响应的，然后将调用结果返回给服务消费方（Consumer），显然这种方式是以冗余服务为基础的，需要消耗更多的资源，但是能够满足高实时应用的需求。

**一、Dubbo服务集群容错**
假设我们使用的是单机模式的Dubbo服务，如果在服务提供方（Provider）发布服务以后，服务消费方（Consumer）发出一次调用请求，恰好这次由于网络问题调用失败，那么我们可以配置服务消费方重试策略，可能消费方第二次重试调用是成功的（重试策略只需要配置即可，重试过程是透明的）；但是，如果服务提供方发布服务所在的节点发生故障，那么消费方再怎么重试调用都是失败的，所以我们需要采用集群容错模式，这样如果单个服务节点因故障无法提供服务，还可以根据配置的集群容错模式，调用其他可用的服务节点，这就提高了服务的可用性。

简单地说目前Dubbo支持的集群容错模式，每种模式适应特定的应用场景，可以根据实际需要进行选择。Dubbo内置支持如下6种集群模式：
1、Failover Cluster模式

配置值为failover。这种模式是Dubbo集群容错默认的模式选择，调用失败时，会自动切换，重新尝试调用其他节点上可用的服务。对于一些幂等性操作可以使用该模式，如读操作，因为每次调用的副作用是相同的，所以可以选择自动切换并重试调用，对调用者完全透明。可以看到，如果重试调用必然会带来响应端的延迟，如果出现大量的重试调用，可能说明我们的服务提供方发布的服务有问题，如网络延迟严重、硬件设备需要升级、程序算法非常耗时，等等，这就需要仔细检测排查了。

例如，可以这样显式指定Failover模式，或者不配置则默认开启Failover模式，配置示例如下：

```
<dubbo:service interface="org.shirdrn.dubbo.api.ChatRoomOnlineUserCounterService" 
    version="1.0.0" cluster="failover" retries="2" timeout="100" 
    ref="chatRoomOnlineUserCounterService" protocol="dubbo" >
    <dubbo:method name="queryRoomUserCount" timeout="80" retries="2" />
</dubbo:service>
```

上述配置使用Failover Cluster模式，如果调用失败一次，可以再次重试2次调用，服务级别调用超时时间为100ms，调用方法queryRoomUserCount的超时时间为80ms，允许重试2次，最坏情况调用花费时间160ms。如果该服务接口org.shirdrn.dubbo.api.ChatRoomOnlineUserCounterService还有其他的方法可供调用，则其他方法没有显式配置则会继承使用dubbo:service配置的属性值。

2、Failfast Cluster模式

配置值为failfast。这种模式称为快速失败模式，调用只执行一次，失败则立即报错。这种模式适用于非幂等性操作，每次调用的副作用是不同的，如写操作，比如交易系统我们要下订单，如果一次失败就应该让它失败，通常由服务消费方控制是否重新发起下订单操作请求（另一个新的订单）。

3、Failsafe Cluster模式

配置值为failsafe。失败安全模式，如果调用失败， 则直接忽略失败的调用，而是要记录下失败的调用到日志文件，以便后续审计。

4、Failback Cluster模式

配置值为failback。失败自动恢复，后台记录失败请求，定时重发。通常用于消息通知操作。

5、Forking Cluster模式

配置值为forking。并行调用多个服务器，只要一个成功即返回。通常用于实时性要求较高的读操作，但需要浪费更多服务资源。

6、Broadcast Cluster模式

配置值为broadcast。广播调用所有提供者，逐个调用，任意一台报错则报错（2.1.0开始支持）。通常用于通知所有提供者更新缓存或日志等本地资源信息。

上面的6种模式都可以应用于生产环境，我们可以根据实际应用场景选择合适的集群容错模式。如果我们觉得Dubbo内置提供的几种集群容错模式都不能满足应用需要，也可以定制实现自己的集群容错模式，因为Dubbo框架给我提供的扩展的接口，只需要实现接口com.alibaba.dubbo.rpc.cluster.Cluster即可。

**二、Dubbo服务负载均衡**

Dubbo框架内置提供负载均衡的功能以及扩展接口，我们可以透明地扩展一个服务或服务集群，根据需要非常容易地增加/移除节点，提高服务的可伸缩性。Dubbo框架内置提供了4种负载均衡策略，如下所示：
1、Random LoadBalance：随机策略，配置值为random。可以设置权重，有利于充分利用服务器的资源，高配的可以设置权重大一些，低配的可以稍微小一些

2、RoundRobin LoadBalance：轮询策略，配置值为roundrobin。

3、LeastActive LoadBalance：配置值为leastactive。根据请求调用的次数计数，处理请求更慢的节点会受到更少的请求

4、ConsistentHash LoadBalance：一致性Hash策略，具体配置方法可以参考Dubbo文档。相同调用参数的请求会发送到同一个服务提供方节点上，如果某个节点发生故障无法提供服务，则会基于一致性Hash算法映射到虚拟节点上（其他服务提供方）

在实际使用中，只需要选择合适的负载均衡策略值，配置即可，下面是上述四种负载均衡策略配置的示例：

```
<dubbo:service interface="org.shirdrn.dubbo.api.ChatRoomOnlineUserCounterService" version="1.0.0"
    cluster="failover" retries="2" timeout="100" loadbalance="random"
    ref="chatRoomOnlineUserCounterService" protocol="dubbo" >
    <dubbo:method name="queryRoomUserCount" timeout="80" retries="2" loadbalance="leastactive" />
</dubbo:service>
```

上述配置，也体现了Dubbo配置的继承性特点，也就是dubbo:service元素配置了loadbalance=”random”，则该元素的子元素dubbo:method如果没有指定负载均衡策略，则默认为loadbalance=”random”，否则如果dubbo:method指定了loadbalance=”leastactive”，则使用子元素配置的负载均衡策略覆盖了父元素指定的策略（这里调用queryRoomUserCount方法使用leastactive负载均衡策略）。



# springboot两种方式集成dubbo

方式一：

```
<!--dubbo-springBoot依赖-->
		<dependency>
			<groupId>com.alibaba.spring.boot</groupId>
			<artifactId>dubbo-spring-boot-starter</artifactId>
			<version>2.0.1-SNAPSHOT</version>
</dependency>

还有说或者引用，这种方式的配置内容前缀没有spring.
<dependency>
    <groupId>com.alibaba.boot</groupId>
    <artifactId>dubbo-spring-boot-starter</artifactId>
    <version>0.2.0</version>
</dependency>
```

在springboot启动类上加：

```
@EnableDubboConfiguration
```

提供者实现上

```
@Service(interfaceClass = ShopApi.class)
```

消费方springboot启动类上加

```
@EnableDubboConfiguration
```

配置文件

```
## 避免和 server 工程端口冲突
server.port=8081
## Dubbo 服务消费者配置
spring.dubbo.application.name=live-dubbo-consumer
spring.dubbo.application.id=live-dubbo-consumer
spring.dubbo.protocol.port=20800
spring.dubbo.protocol.name=dubbo
spring.dubbo.registry.address=zookeeper://127.0.0.1:2181
```



方式二：

```
<dependency>
     <groupId>io.dubbo.springboot</groupId>
     <artifactId>spring-boot-starter-dubbo</artifactId>
     <version>1.0.0</version>
</dependency>

或者
<dependency>
            <groupId>com.gitee.reger</groupId>
            <artifactId>spring-boot-starter-dubbo</artifactId>
            <version>1.0.10</version>
</dependency>
```

启动类上不加对应的注解

spring.dubbo.registry.address配置  IP：端口 

`等价于`  

 spring.dubbo.registry.address配置IP

 spring.dubbo.registry.port配置端口

注意点：

provider：spring.dubbo.scan配置的包名是声明@service类(dubbo的注解)所在的包名

```
spring.dubbo.application.name=dubbo-provider
#spring.dubbo.registry.address=zookeeper://127.0.0.1:2181
spring.dubbo.registry.address=zookeeper://127.0.0.1
spring.dubbo.registry.port=2181
spring.dubbo.protocol.name=dubbo
spring.dubbo.protocol.serialization=hessian2
spring.dubbo.server=true
# 服务调用重试次数，服务发布者不给重试，让服务调用者自己重试
spring.dubbo.provider.retries=0
spring.dubbo.protocol.port=20880
spring.dubbo.scan=com.integration.boot.provider.service
## dubbo服务发布者所在的包
#spring.dubbo.base-package=com.integration.boot.provider.service
```

consumer：spring.dubbo.scan配置的包名是声明@Reference类(dubbo的注解)所在的包名

```
spring.dubbo.application.name=dubbo-consumer
spring.dubbo.registry.address=zookeeper://127.0.0.1
spring.dubbo.registry.port=2181
spring.dubbo.protocol.name=dubbo
#spring.dubbo.protocol.serialization=hessian2
spring.dubbo.protocol.port=20880
spring.dubbo.scan=com.integration.boot
## dubbo服务发布者所在的包
#spring.dubbo.base-package=com.integration.boot.provider.service
spring.dubbo.consumer.timeout=1000
spring.dubbo.consumer.check=false
spring.dubbo.consumer.retries=2
```

详细配置：https://blog.csdn.net/jeffli1993/article/details/71480627

方式三：

官方原生dubbo依赖，boot启动类上加

```
<dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>dubbo</artifactId>
            <version>2.5.3</version>
            <exclusions>
                <exclusion> //这个排除低版本的spring
                    <groupId>org.springframework</groupId>
                    <artifactId>spring</artifactId>
                </exclusion>
            </exclusions>
</dependency>
```

```
@ImportResource({"classpath:spring-context.xml"})
```

xml里配置原生的dubbuo配置



# dubbo多个服务提供者

如果是同一台机器部署两个服务提供者，则只需要保证下面端口不一致即可

```
spring.dubbo.protocol.port=20882
```

如果是多台机器部署，每一台机器的配置一样即可。

消费方不需要配置spring.dubbo.protocol.port。dubbo决定调用哪个提供者



# springboot dubbo配置清单

```
#应用名称
spring.dubbo.application.name=dubbo-provider
#spring.dubbo.registry.address=zookeeper://127.0.0.1:2181
spring.dubbo.registry.address=zookeeper://127.0.0.1
spring.dubbo.registry.port=2181
spring.dubbo.protocol.name=dubbo
spring.dubbo.protocol.serialization=hessian2
spring.dubbo.server=true
# 服务调用重试次数，服务发布者不给重试，让服务调用者自己重试
spring.dubbo.provider.retries=0
spring.dubbo.protocol.port=20880
spring.dubbo.scan=com.integration.boot.provider.service
## dubbo服务发布者所在的包
#spring.dubbo.base-package=com.integration.boot.provider.service
# 模块版本
#spring.dubbo.application.version=xxx
# 应用负责人
#spring.dubbo.application.owner=xxx
# 环境，如：dev/test/run
#spring.dubbo.application.environment=xxx
# 日志输出方式
#spring.dubbo.application.logger=xxx
# 注册中心 0
#spring.dubbo.application.registries[0].address=zookeeper:#127.0.0.1:2181=xxx
# 注册中心 1
#spring.dubbo.application.registries[1].address=zookeeper:#127.0.0.1:2181=xxx
# 服务监控
#spring.dubbo.application.monitor.address=xxx
```



# 多个zookeeper

```
spring.dubbo:
            application:
                name: qb-api-manager
                registries[0]:
                    address: zookeeper://192.168.1.236:3181
                registries[1]:
                    address: zookeeper://192.168.1.237:3181
                registries[2]:
                    address: zookeeper://192.168.1.237:3182
             scan: com.qb.api.manager.modules.web.service
```



# 优雅停机

```
kill -9 pid  //强制停机
kill pid //优雅停机
```

服务提供方：先标记不接受新的请求，新请求来直接报错，让客户端重试其他机器。

然后检测线程池中的线程是否正在运行，如果有，等待所有线程执行完成，除非超时，则强制关闭。

消费方：停止时不再发起新新的请求，所有新的调用在客户端报错。

然后，检测有没有请求的响应还没返回。等待响应返回，除非超时，则强制关闭。

优雅停机的超时时间，默认是10秒。超时则强制关闭。

<dubbo:parameter key="shutdown.timeout" value="60000"/> 单位毫秒

如果shutdownHook不能生效，可自行调用 protocolConfig.destroyAll();



# ProtocolConfig

```
package hello.configuration;

import javax.annotation.Resource;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.ProviderConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.rpc.Exporter;

/**
 * 多端口提供dubbo服务
 *     当你使用多端口提供服务，使用默认端口提供服务：需要加入在service上加上defaultProvider
 * @author chenlili
 *
 */
@Configuration
@ConditionalOnClass(Exporter.class)
public class DubboAutoConfiguration {
    
    @Resource(name="protocolConfig1")
    private ProtocolConfig protocolConfig;
    
    @Resource(name="protocolConfig2")
    private ProtocolConfig protocolConfig2;
    
    /**
     * 默认基于dubbo协议提供服务
     * 
     * @return
     */
    @Bean(name = "protocolConfig1")
    public ProtocolConfig protocolConfig() {
        // 服务提供者协议配置
        ProtocolConfig protocolConfig = new ProtocolConfig();
        protocolConfig.setName("rmi");
        protocolConfig.setPort(20881);
        protocolConfig.setThreads(200);

        System.out.println("protocolConfig1的hashCode: " + protocolConfig.hashCode());

        return protocolConfig;
    }

    /**
     * dubbo服务提供
     * 
     * @param applicationConfig
     * @param registryConfig
     * @param protocolConfig
     * @return
     */
    @Bean(name = "providerConfig1")
    public ProviderConfig providerConfig(ApplicationConfig applicationConfig, RegistryConfig registryConfig) {
        ProviderConfig providerConfig = new ProviderConfig();
        providerConfig.setTimeout(1000);
        providerConfig.setRetries(1);
        providerConfig.setDelay(-1);
        providerConfig.setApplication(applicationConfig);
        providerConfig.setRegistry(registryConfig);
        providerConfig.setProtocol(this.protocolConfig);
        return providerConfig;
    }

    /**
     * 默认基于dubbo协议提供服务
     * 
     * @return
     */
    @Bean(name = "protocolConfig2")
    public ProtocolConfig protocolConfig2() {
        // 服务提供者协议配置
        ProtocolConfig protocolConfig = new ProtocolConfig();
        protocolConfig.setName("dubbo");
        protocolConfig.setPort(20882);
        protocolConfig.setThreads(200);

        System.out.println("protocolConfig2的hashCode: " + protocolConfig.hashCode());

        return protocolConfig;
    }

    /**
     * dubbo服务提供
     * 
     * @param applicationConfig
     * @param registryConfig
     * @param protocolConfig
     * @return
     */
    @Bean(name = "providerConfig2")
    public ProviderConfig providerConfig2(ApplicationConfig applicationConfig, RegistryConfig registryConfig) {
        ProviderConfig providerConfig = new ProviderConfig();
        providerConfig.setTimeout(1000);
        providerConfig.setRetries(1);
        providerConfig.setDelay(-1);
        providerConfig.setApplication(applicationConfig);
        providerConfig.setRegistry(registryConfig);
        providerConfig.setProtocol(protocolConfig2);
        return providerConfig;
    }
}
```

　@Service使用时，直接使用@Service(version="1.0.0")会报错，提示你找不到对应的provider，因此需要配上对应的provider，因此在默认dubboConfiguration上加了@Bean(name="defaultProvider")，用于索引默认provider。 

```
package hello.dubbo;

import com.alibaba.dubbo.config.annotation.Service;
import com.jon.show.service.IDubboDemoService;

@Service(version="1.0.0",provider="providerConfig1")
public class DubboDemoServiceImpl implements IDubboDemoService{

    @Override
    public String sayHello(String name) {
        return "hello " + name;
    }

    @Override
    public String sayYourAge(int age) {
        return null;
    }

}
```



# dubbo白名单（Filter过滤器）

实现com.alibaba.dubbo.rpc.Filter接口

```
public class AuthorityFilter implements Filter {  
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorityFilter.class);  

    private IpWhiteList ipWhiteList;  

    //dubbo通过setter方式自动注入  
    public void setIpWhiteList(IpWhiteList ipWhiteList) {  
        this.ipWhiteList = ipWhiteList;  
    }  

    @Override  
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {  
        if (!ipWhiteList.isEnabled()) {  
            LOGGER.debug("白名单禁用");  
            return invoker.invoke(invocation);  
        }  

        String clientIp = RpcContext.getContext().getRemoteHost();  
        LOGGER.debug("访问ip为{}", clientIp);  
        List<String> allowedIps = ipWhiteList.getAllowedIps();  
        if (allowedIps.contains(clientIp)) {  
            return invoker.invoke(invocation);  
        } else {  
            return new RpcResult();  
        }  
    }  
}
```

*注意：只能通过setter方式来注入其他的bean，且不要标注注解！dubbo自己会对这些bean进行注入，不需要再标注@Resource让Spring注入*

在resources目录下添加纯文本文件META-INF/dubbo/com.alibaba.dubbo.rpc.Filter，内容如下： xxxFilter=com.xxx.AuthorityFilter 

修改dubbo的provider配置文件，在dubbo:provider中添加配置的filter， 内容如下：

```
<dubbo:provider filter="xxxFilter" />  
```

