# 前言

关系调用说明：

- 服务生产者启动时，向服务注册中心注册自己提供的服务
- 服务消费者启动时，在服务注册中心订阅自己所需要的服务
- 注册中心返回服务提供者的地址信息个消费者
- 消费者从提供者中调用服务

# 一、引入依赖

maven父模块

```xml
<dependencyManagement>
	<dependencies>
		<dependency>
				<groupId>org.springframework.cloud</groupId>
				<artifactId>spring-cloud-dependencies</artifactId>
				<version>Camden.SR6</version>
				<type>pom</type>
				<scope>import</scope>
		</dependency>
	</dependencies>
</dependencyManagement>
```

新建三个子模块，EurekaServer  和 ConfigClient和ConfigServer分别引入

EurekaServer  引入

```xml
<dependency><!--过时-->
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-eureka-server</artifactId>
</dependency>

<!--最新 cloud版本  Finchley.RELEASE-->
<dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
        </dependency>
</dependencies>
```

其余client引入

```xml
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-eureka</artifactId>
</dependency>

<!--最新 cloud版本  Finchley.RELEASE-->
 <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>

```

# 二、启动服务注册中心

只需要在启动类上加：@EnableEurekaServer

```java
package org.eurekaServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**服务注册中心**/ 
@EnableEurekaServer
@SpringBootApplication
public class EurekaServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(EurekaServerApplication.class, args);
		System.out.println("====eurekaServer start successful==========");
	}
}
```

```properties
server.port=9001
spring.application.name=eurekaServer
eureka.instance.hostname=eureka01 //本地可以配置为localhost
#是否将自身注册  
eureka.client.serviceUrl.defaultZone=http://${eureka.instance.hostname}:${server.port}/eureka/
eureka.client.serviceUrl.defaultZone=http://eureka01:9001/eureka/
#如果为true，启动时报警  
#是否检索服务
eureka.client.fetch-registry=false
#是否向服务注册中心注册自己
eureka.client.register-with-eureka=false
```

在默认情况下，服务注册中心也会把自己当做是一个服务，将自己注册进服务注册中心，所以我们可以通过配置来禁用他的客户端注册行为eureka.client.registerWithEureka=false



启动后访问 http://localhost:9001/看到控制台界面



# 三、创建一个服务提供者

当client向注册中心注册时，它会提供一些元数据，例如主机和端口，URL，主页等。Eureka server 从每个client实例接收心跳消息。 如果心跳超时，则通常将该实例从注册server中删除

```xml
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-eureka</artifactId>
</dependency>
```

通过注解@EnableEurekaClient 表明自己是一个eurekaclient.

**@EnableEurekaClient**注解是基于spring-cloud-netflix依赖，只能eureka使用

**@EnableDiscoveryClient**注解是基于spring-cloud-commons依赖，并且在classpath中实现

它们的作用是一样的

```java
package com.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableConfigServer
@EnableEurekaClient
public class ApplicationConfigServer {   
	
	public static void main(String[] args) {
          SpringApplication.run(ApplicationConfigServer.class, args);
          System.out.println("=====configServer statrt successful=========");
      }
}
```

```properties
server.port=8001
#需要指明spring.application.name,这个很重要，这在以后的服务与服务之间相互调用一般都是根据这个name 
spring.application.name=configServer
server.context-path=/
logging.config=classpath:logback.xml
eureka.client.serviceUrl.defaultZone=http://localhost:9001/eureka/

#设置心跳的时间间隔（默认是30秒）
eureka.instance.lease-renewal-interval-in-seconds=3
# 如果现在超过了5秒的间隔（默认是90秒）
eureka.instance.lease-expiration-duration-in-seconds=5
#eureka.instance.instance-id=${spring.cloud.client.ipAddress}:${server.port}
#信息列表时显示主机名称
eureka.instance.instance-id=dept-8001.com
#访问的路径变为IP地址
eureka.instance.prefer-ip-address=true
```

此时访问注册中心地址http://localhost:9001/ 看到客户端已经注册到注册中心了。

```java
package com.boot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConfigServerController {

	@Autowired
	private DiscoveryClient client;

	@RequestMapping(value = "/client", method = RequestMethod.GET)
	public String home() {
		ServiceInstance instance = client.getLocalServiceInstance();
		System.out.println(
				"serviceId=" + instance.getServiceId() + ",host=" + instance.getHost() + ":" + instance.getPort());
		return "hello spring";
	}
}
```

访问 http://localhost:8001/client 输出

```
serviceId=configServer,host=789-PC:8001
```



# 四、高可用服务注册中心

Eureka Server的高可用实际上就是将自己做为服务向其他服务注册中心注册自己，这样就可以形成一组互相注册的服务注册中心，以实现服务清单的互相同步，达到高可用的效果

Eureka Server的同步遵循着一个非常简单的原则：只要有一条边将节点连接，就可以进行信息传播与同步。可以采用`两两注册`的方式实现集群中节点完全对等的效果，实现最高可用性集群，任何一台注册中心故障都不会影响服务的注册与发现

application-9001.properties

```properties
server.port=9001
spring.application.name=eurekaServer
eureka.instance.hostname=eureka01
#是否将自身注册  
#eureka.client.serviceUrl.defaultZone=http://${eureka.instance.hostname}:${server.port}/eureka/
eureka.client.serviceUrl.defaultZone=http://eureka02:9002/eureka/
#如果为true，启动时报警  
eureka.client.fetch-registry=false
eureka.instance.preferIpAddress=true
# 设置为false表示关闭保护模式
eureka.server.enableSelfPreservation=false 
```

application-9002.properties

```properties
server.port=9002
spring.application.name=eurekaServer
eureka.instance.hostname=eureka02
#是否将自身注册  
#eureka.client.serviceUrl.defaultZone=http://${eureka.instance.hostname}:${server.port}/eureka/
eureka.client.serviceUrl.defaultZone=http://eureka01:9001/eureka/
#如果为true，启动时报警  
eureka.client.fetch-registry=false

eureka.instance.preferIpAddress=true
eureka.server.enableSelfPreservation=false
```

host配置

```
127.0.0.1 eureka02
127.0.0.1 eureka01
```

eclipse右键Run as java application，分别两次输入参数  --spring.profiles.active=9001和 --spring.profiles.active=9001

此时分别访问

http://localhost:9001/

http://localhost:9002

看到控制台DS Replicas下有了对方的信息



# 五、搭建Eureka Client集群

```xml
<dependency>
   <groupId>org.springframework.cloud</groupId>
   <artifactId>spring-cloud-starter-eureka</artifactId>
</dependency>
```

启动类上加入：

```java
package com.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableConfigServer
@EnableEurekaClient
public class ApplicationConfigServer {   
	
	public static void main(String[] args) {
          SpringApplication.run(ApplicationConfigServer.class, args);
          System.out.println("=====configServer statrt successful=========");
      }
}
```

```properties
server.port=8001
spring.application.name=configServer
eureka.client.serviceUrl.defaultZone=http://localhost:9001/eureka/,http://localhost:9002/eureka/
```

```java
package com.boot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConfigServerController {

	@Autowired
	private DiscoveryClient client;

	@RequestMapping(value = "/client", method = RequestMethod.GET)
	public String home() {
		ServiceInstance instance = client.getLocalServiceInstance();
		System.out.println(
				"serviceId=" + instance.getServiceId() + ",host=" + instance.getHost() + ":" + instance.getPort());
		return "hello spring";
	}
}
```

访问两个注册中心

http://localhost:9001/

http://localhost:9002

发现客户端都注册上了。

### 启动多个Client

修改上面的配置文件的server.port=8001和server.port=8002，分别启动，在注册中心控制台查看注册信息

### ribbon消费Eureka Client的消息

```xml
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-eureka</artifactId>
</dependency>

<!--增加ribbon的依赖 -->
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-ribbon</artifactId>
</dependency>


<!--最新-->
<dependency>
   <groupId>org.springframework.cloud</groupId>
   <artifactId>spring-cloud-starter-netflix-ribbon</artifactId>
</dependency>
```

```java
package org.config;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableEurekaClient
public class ApplicationConfigClient {

	public static void main(String[] args) {
		new SpringApplicationBuilder(ApplicationConfigClient.class).web(true).run(args);
		System.out.println("======configClient start successful==========");
	}

	@Bean
	@LoadBalanced
	RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
```

配置消费者application.properties。由于我是用的config-client做为消费者，所以配置在bootstrap.properties里

```properties
server.port=7001
spring.application.name=configClient
eureka.client.serviceUrl.defaultZone=http://localhost:9001/eureka/,http://localhost:9002/eureka/
```

```java
package org.config.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class ConfigController {
	
	
	@Autowired
	RestTemplate restTemplate;
	
	@RequestMapping("ribbon-consumer")
	public String getConfig(){
		
	  return restTemplate.getForEntity("http://configServer/client",String.class).getBody();
	}
}
```

上面调用了服务提供者的方法

```java
package com.boot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConfigServerController {

	@Autowired
	private DiscoveryClient client;

	@RequestMapping(value = "/client", method = RequestMethod.GET)
	public String home() {
		ServiceInstance instance = client.getLocalServiceInstance();
		System.out.println(
				"serviceId=" + instance.getServiceId() + ",host=" + instance.getHost() + ":" + instance.getPort());
		return "hello spring";
	}
}
```

启动应用发现也注册到了配置中心。

访问http://localhost:7001/ribbon-consumer ，发现返回了hello spring字符串。对点几次发现多个服务提供者交替输出

```
serviceId=configServer,host=789-PC:8002
serviceId=configServer,host=789-PC:8002
```

```
serviceId=configServer,host=789-PC:8001
```

# 六、常见问题

①注册中心的服务实例没有显示IP，而是主机名

```
eureka.instance.preferIpAddress=true
eureka.instance.instance-id=${spring.cloud.client.ipAddress}:${server.port}
```

配置完需要重启注册中心



# 七、Eureka的安全

```xml
<dependency>
   <groupId>org.springframework.boot</groupId>
   <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>

<!-- 安全验证 -->
<dependency>
   <groupId>org.springframework.boot</groupId>
   <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

```properties
security.user.name=admin
security.user.password=admin
security.user.role=SUPERUSER

### management.security.roles: SUPERUSER #角色

### endpoint.shutdown.enabled=true
### endpoint.shutdown.sensitive=true
```



客户端(最好eureka服务端也加上)

```properties
eureka.client.serviceUrl.defaultZone=http://admin:admin@localhost:9001/eureka/
```

停止注册中心

```shell
curl -u admin:admin -X POST http://192.168.1.108:8761/shutdown
```

