# 参考

http://blog.didispace.com/categories/Spring-Cloud/page/2/

http://blog.didispace.com/Spring-Cloud%E5%9F%BA%E7%A1%80%E6%95%99%E7%A8%8B/

方志鹏

https://blog.csdn.net/forezp/article/details/70148833

https://blog.csdn.net/forezp

http://www.cnblogs.com/skyblog/p/5129603.html

官方文档：

https://springcloud.cc/spring-cloud-dalston.html#_stub_runner_for_messaging

狄永超

https://www.jianshu.com/p/6db3f2ce3a53

# 一、分布式配置中心SpringCloud

## 一、构建一个config Server

```xml
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-config-server</artifactId>
</dependency>
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

## 二、开启配置服务器的功能

```java
package com.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class ApplicationConfigCenterServer {   
	
	public static void main(String[] args) {
          SpringApplication.run(ApplicationConfigCenterServer.class, args);
          System.out.println("=====config server statrt successful=========");
      }
}
```

## 三、配置文件

在程序的配置文件application.properties文件配置以下

```properties
spring.application.name=configServer
server.port=8001
server.context-path=/
logging.config=classpath:logback-boot.xml
spring.cloud.config.server.git.uri=https://github.com/18310130376/configCenterServer/
#git仓库地址下的相对地址，可以配置多个，用,分割
spring.cloud.config.server.git.searchPaths=src/main/resources/conf/
#spring.profiles.active=native
#spring.cloud.config.server.native.searchLocations=classpath:/conf
#spring.cloud.config.server.git.basedir=config-repo
#spring.cloud.config.server.git.clone-on-start=true
spring.cloud.config.label=master
spring.cloud.config.server.git.username=475402366@qq.com
spring.cloud.config.server.git.password=wk69404905
```

提示：如果Git仓库为公开仓库，可以不填写用户名和密码。

spring.cloud.config.server.git.uri指定的配置文件仓库地址可以不独立，不和服务端代码在一起。

## 四、测试

https://github.com/18310130376/configCenterServer/仓库的src/main/resources/conf/有个文件

configClient-dev.properties，内容如下：

```
spring.data.source.username=123456
```

此时访问： http://localhost:8001/spring.data.source.username/dev  得到信息如下：

```json
{
	"name": "spring.data.source.username",
	"profiles": [
		"dev"
	],
	"label": "master",
	"version": "3c95c039a88c755b87d0a5ad3a082c0d30de0dcc",
	"state": null,
	"propertySources": []
}
```

http请求地址和资源文件映射如下:

- /{application}/{profile}[/{label}]
- /{application}-{profile}.yml
- /{label}/{application}-{profile}.yml
- /{application}-{profile}.properties
- /{label}/{application}-{profile}.properties



假设我们需要访问configClient应用的dev环境的配置，则访问url如下：

http://localhost:8001/configClient/dev/master

```json
{
	"name": "configClient",
	"profiles": ["dev"],
	"label": "master",
	"version": "d18cb50d7a06934fb3f8e3ba3d9f05c209756bea",
	"state": null,
	"propertySources": [{
		"name": "https://github.com/18310130376/configdata/data/configClient-dev.properties",
		"source": {
			"spring.data.source.username": "123456"
		}
	}]
}
```

原理：配置服务器从Git中获取配置信息后，会存储一份在configServer的文件系统中。防止GIt仓库出现故障引起服务不可用。



## 五、构建一个config client

```xml
   <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
   </dependency>
    <dependency>
		<groupId>org.springframework.cloud</groupId>
		<artifactId>spring-cloud-starter-config</artifactId>
    </dependency>
<dependencyManagement>
		<dependencies>
			<dependency>
				<groupId>org.springframework.cloud</groupId>
				<artifactId>spring-cloud-dependencies</artifactId>
				<version>Brixton.SR5</version>
				<type>pom</type>
				<scope>import</scope>
			</dependency>
		</dependencies>
</dependencyManagement>
```

配置文件**bootstrap.properties**

```properties
spring.application.name=configClient   //对应配置文件规则的{application}部分 
spring.cloud.config.label=master   //对应配置文件规则的{label}部分 
spring.cloud.config.profile=dev    //对应配置文件规则的{profile}部分 
spring.cloud.config.uri= http://localhost:8001/  //配置中心configServer的地址 
server.port=8002
```

- spring.cloud.config.label 指明远程仓库的分支
- spring.cloud.config.profile
  - dev开发环境配置文件
  - test测试环境
  - pro正式环境
- spring.cloud.config.uri= <http://localhost:8888/> 指明配置服务中心的网址。

注意：

config-client默认会找8888端口的配置中心，如果配置中心使用8888端口，config-client可以使用application.properties配置文件。如果配置中心没有使用8888端口，那么config-client需要使用bootstrap文件,因为bootstrap文件会被优先读取（意思是如果配置中心不是8888则client必须使用bootstrap）

原因：因为本身就是为了统一配置，所以bootstrap的优先级需要高于其他配置文件的加载顺序。

```java
package org.configClient;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class ApplicationConfigClient {   
	 
	public static void main(String[] args) {
		new SpringApplicationBuilder(ApplicationConfigClient.class).web(true).run(args);
          System.out.println("======configClient start successful==========");
      }
  }
```





## 六、客户端测试

```java
import org.configClient.ApplicationConfigClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes=ApplicationConfigClient.class)
public class ConfigClientTest {
	@Test
	public void contextLoads() {
	}
    @Autowired
    private Environment environment;
    @Test
    public void loadProperties() {
    	String property = environment.getProperty("spring.data.source.username");
    	System.out.println(property);
    }
}
```

## 七、原理

客户端配置

```properties
spring.application.name=configClient
spring.cloud.config.label=master
spring.cloud.config.profile=dev
spring.cloud.config.uri= http://localhost:8001/
server.port=8002
```

则服务端从git仓库找的文件为：

默认：`${spring.application.name}-${spring.cloud.config.profile}.proprties`

但是，如果配置了spring.cloud.config.name=configClient01

则找`${spring.cloud.config.name}-${spring.cloud.config.profile}.proprties`

总结为：

spring.cloud.config.name=configClient
需要读取的properties文件名前半部分，不配置系统默认读取spring.application.name

①如果客户端指定找spring.cloud.config.profile=dev，但是git仓库文件是configClient.properties，则也会匹配到。如果git仓库存在configClient-dev.properties，则只会加载这个文件。

②如果客户端没指定spring.cloud.config.profile=dev，则会找configClient.properties，并不会找configClient-dev.properties

## 八、其他配置

```properties
spring.cloud.config.name=configClient01
spring.cloud.config.discovery.enabled=true 
当链接服务端时如果服务端因没启动等原因，客户端会迅速响应
spring.cloud.config.failFast=true
最大重试6次，不配置默认是6
spring.cloud.config.retry.max-attempts=6
#see：https://springcloud.cc/spring-cloud-config.html
spring.cloud.config.server.bootstrap=false
spring.cloud.config.discovery.serviceId=configCenterServer
```



## 九、配置敏感数据加密

在springcloud config中，通过在属性值前加{cipher}前缀来标注内容是一个加密值。在客户端加载配置时，配置中心自动为带有{cipher}前缀的值进行解密。

配置中心安装不限长度的JCE版本，从oracle下载。下载后把local_policy.jar和US_export.policy.jar两个文件复制到$JAVA_HOME/jre/lib/security目录下，覆盖原有的内容。

完成了安装后，启动配置中心，控制台会输出一些配置中心特有的端点信息。主要有：

- /encrypt/status:查看加密状态功能的端点
- /key 查看秘钥的端点
- /encrypt  对请求的body内容进行加密的端点
- /decrypt 对请求的body内容进行解密的端点

可以尝试get方式访问/encrypt/status,得到如下内容：

```
{
    "description":"NO key was install for encryption service"
    "status":"NO_KEY"
}
```

以上表示加密还不可用，没配置秘钥

我们可以在配置文件指定秘钥

```
encrypt.key=123456
```

再次访问/encrypt/status得到如下内容

```
{
    "status":"OK"
}
```

此时才表示加解密功能可用

/encrypt   POST请求

/decrypt   POST请求

```
curl localhost:8001/encrypt -d root
```

```
curl localhost:8001/decrypt -d 上面的加密内容
```

上面只通过encrypt.key就实现了加解密功能。

我们也可以通过环境变量ENCRYPT_KEY来设置秘钥，从而更安全。

## 十、参考文档

https://springcloud.cc/spring-cloud-config.html

## 十一、指定本地文件系统

```
spring.cloud.config.server.git.uri=file://${user.home}/config-repo
```

以上方式仅用于测试，生产环境务必搭建Git仓库。

## 十二、一服务对应一个Git仓库目录

```
spring.cloud.config.server.git.uri=https://github.com/18310130376/configdata/{application}
或
spring.cloud.config.server.git.uri=https://github.com/18310130376/configdata/{application}-config
```

{application}代表了应用名，所以客户端向Config Server发起获取配置的请求时，config Server会根据客户端的spring.application.name信息来填充{application}占位符以定位配置资源的存储位置，从而实现根据微服务应用属性动态获取不同位置的配置。

这些占位符中，{label}参数较为特殊，如果Git的分支和标签名包含"/"，那么{label}参数在Http的URL中应该使用“（_）”来代替。

## 十三、根据searchPaths应对多服务

对于上面的一个服务一个仓库，如果为了省事可以只需要一个仓库，仓库下根据应用名建立不同的文件夹，文件夹用{application}命名。

```
spring.cloud.config.server.git.searchPaths={application}
```

## 十四、服务端对Git仓库的连通性

```
spring.cloud.config.server.health.repositories.check.name=check-repo  //已存在的仓库
spring.cloud.config.server.health.repositories.check.label=master
spring.cloud.config.server.health.repositories.check.profiles=default
```

如果不想使用健康检查，则使用如下方式关闭：

```
spring.cloud.config.server.health.enabled=false
```

## 十五、共同属性 / 属性覆盖

```
spring.cloud.config.server.overrides.name=zhangsan
spring.cloud.config.server.overrides.from=shenzhen
```

以上在configServer配置的属性，每个客户端都会获取到，从而实现了应用配置的共同属性或默认属性。

## 十六、配置中心安全保护

在configServer的pom增加

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency
```

默认情况下我们会得一个名为user的用户，并在配置中心启动的时候在日志中打印出该用户的随机密码。

大多数情况下，并不会使用随机密码，我们在配置文件中指定用户和密码，比如

```
security.user.name=user
security.user.password=37cc5635-559d-4e6f-b633-7e932b813f73
```

由于对configServer做了安全保护，如果这时连接到配置中心的客户端没有设置安全信息，在获取配置信息的时候返回401错误。所以需要通过配置的方式在客户端加入安全信息来通过校验，比如：

```
spring.cloud.config.username=user
spring.cloud.config.password=37cc5635-559d-4e6f-b633-7e932b813f73
```



# 二、配置中心SpringCloud   高可用

## 一 、引入eureka依赖

```xml
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-eureka-server</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```



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
		System.out.println("====eureka server start successful==========");
	}
}
```

application.properties

```properties
server.port=9002
spring.application.name=eurekaServer
eureka.instance.hostname=eureka01
#是否将自身注册  
eureka.client.serviceUrl.defaultZone=http://${eureka.instance.hostname}:${server.port}/eureka/
eureka.client.serviceUrl.defaultZone=http://eureka01:9001/eureka/

#在默认配置中，Eureka Server在默认90s没有得到客户端的心跳，则注销该实例，但是往往因为微服务跨进程调用，网络通信往往会面临着各种问题，比如微服务状态正常，但是因为网络分区故障时，Eureka Server注销服务实例则会让大部分微服务不可用，这很危险，因为服务明明没有问题。
#
#为了解决这个问题，Eureka 有自我保护机制，通过在Eureka Server配置如下参数，可启动保护机制
#它的原理是，当Eureka Server节点在短时间内丢失过多的客户端时（可能发送了网络故障），那么这个节点将进入自我保护模式，不再注销任何微服务，当网络故障回复后，该节点会自动退出自我保护模式。
eureka.client.register-with-eureka=false
#如果为true，启动时报警  
eureka.client.fetch-registry=false
eureka.server.enable-self-preservation=true
```

操作系统hosts文件配置

```properties
127.0.0.1      eureka01 
127.0.0.1      eureka02
```



## 二、改造config-server

在pom.xml引入

```xml
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-eureka</artifactId>
</dependency>

<!-- 用来提供刷新端点 -->
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

修改application.properties文件，新增eureka.client.serviceUrl.defaultZone

```properties
spring.application.name=configServer
server.port=8001
server.context-path=/
logging.config=classpath:logback.xml
spring.cloud.config.server.git.uri=https://github.com/18310130376/configCenterServer/
spring.cloud.config.server.git.searchPaths=src/main/resources/conf/
spring.cloud.config.label=master
spring.cloud.config.server.git.username=475402366@qq.com
spring.cloud.config.server.git.password=wk69404905
eureka.client.serviceUrl.defaultZone=http://localhost:9001/eureka/
#eureka.client.serviceUrl.defaultZone=http://localhost:9001/eureka/,http://localhost:9002/eureka/
#eureka.instance.preferIpAddress=true
#eureka.instance.instance-id=${spring.cloud.client.ipAddress}:${server.port}
```

修改启动类，加上注解@EnableEurekaClient

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
          System.out.println("=====config server statrt successful=========");
      }
}
```

## 三、改造config-client

```xml
   <dependency>
		<groupId>org.springframework.cloud</groupId>
		<artifactId>spring-cloud-starter-config</artifactId>
    </dependency>
    
    <dependency>
	  <groupId>org.springframework.cloud</groupId>
	  <artifactId>spring-cloud-starter-eureka</artifactId>
    </dependency>
```

配置文件bootstrap.properties，注意是bootstrap 加上服务注册地址为<http://localhost:9001/eureka/> 

```properties
server.port=8002
spring.application.name=configClient
spring.cloud.config.label=master
spring.cloud.config.profile=dev
#spring.cloud.config.uri= http://localhost:8001/
eureka.client.serviceUrl.defaultZone=http://localhost:9001/eureka/
spring.cloud.config.discovery.enabled=true
spring.cloud.config.discovery.serviceId=configServer
#eureka.instance.preferIpAddress=true
#eureka.instance.instance-id=${spring.cloud.client.ipAddress}:${server.port}
```

- spring.cloud.config.discovery.enabled 是从配置中心读取文件。
- spring.cloud.config.discovery.serviceId 配置中心的servieId，即服务名。

这时发现，在读取配置文件不再写ip地址，而是服务名，这时如果配置服务部署多份，通过负载均衡，从而高可用。

依次启动eureka-servr,config-server,config-client 

访问网址：<http://localhost:9002/>  （eureka服务端）

## 小结

configClient通过spring.cloud.config.discovery.serviceId=configServer去注册中心寻找configServer（多个），

eurekaServer对多个configServer其中的一台进行访问。

configServer把自己注册到注册中心（多个）。



# 附

## 注解

|               |      |
| ------------- | ---- |
| @refreshScope |      |
|               |      |
|               |      |
|               |      |
|               |      |

