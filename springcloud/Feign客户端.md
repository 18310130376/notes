# 一、Feign简介

Feign是一个声明式的伪Http客户端，它使得写Http客户端变得更简单。使用Feign，只需要创建一个接口并注解。它具有可插拔的注解特性，可使用Feign 注解和JAX-RS注解。Feign支持可插拔的编码器和解码器。Feign默认集成了Ribbon，并和Eureka结合，默认实现了负载均衡的效果。

简而言之：

- Feign 采用的是基于接口的注解
- Feign 整合了ribbon，具有负载均衡的能力
- 整合了Hystrix，具有熔断的能力

# 二、准备工作

启动eureka-server，启动两个服务提供者。本例启动两个configServer做为服务提供者。

### eureka-server

```xml
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-eureka-server</artifactId>
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
eureka.instance.hostname=eureka01
eureka.client.serviceUrl.defaultZone=http://eureka01:9001/eureka/
```

### configServer

```xml
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-eureka</artifactId>
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

```java
package com.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class ApplicationConfigServer {   
	
	public static void main(String[] args) {
          SpringApplication.run(ApplicationConfigServer.class, args);
          System.out.println("=====configServer statrt successful=========");
      }
}
```

```properties
spring.application.name=configServer
server.port=8002
eureka.client.serviceUrl.defaultZone=http://localhost:9001/eureka
```

修改端口启动两个configServer。

# 三、创建一个feign的服务

旧版本

```xml
<dependencies>
		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-eureka</artifactId>
		</dependency>
		<dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-feign</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>
</dependencies>

<plugin>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-maven-plugin</artifactId>
	<configuration>
		<mainClass>org.config.ApplicationConfigClient</mainClass>
	</configuration>
		<executions>
           <execution>
                <goals>
                    <goal>repackage</goal>
                </goals>
            </execution>
		</executions>
</plugin>
```

新版本

```xml
<dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-openfeign</artifactId>
        </dependency>
</dependencies>
```

修改application.properties，由于我使用的configClient做为消费者，所以是配置在bootstrap.properties里

```properties
server.port=7001
spring.application.name=configClient
eureka.client.serviceUrl.defaultZone=http://localhost:9001/eureka/,http://localhost:9002/eureka/
eureka.instance.preferIpAddress=true
eureka.instance.instance-id=${spring.cloud.client.ipAddress}:${server.port}
```

在启动类上加上@EnableFeignClients注解开启Feign的功能，并且加上EnableEurekaClient注解

```java
package org.config;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
public class ApplicationConfigClient {

	public static void main(String[] args) {
		new SpringApplicationBuilder(ApplicationConfigClient.class).web(true).run(args);
		System.out.println("======configClient start successful==========");
	}
}
```

定义一个feign接口，通过@ FeignClient（"服务名"），来指定调用哪个服务。

```java
package org.config.controller;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(value = "configServer")
//等同于@FeignClient(name="configServer")
public interface UserService {
	
	 @RequestMapping(value = "/provider",method = RequestMethod.GET)
	 String getClientInfo(@RequestParam("name") String name);
}
```

controller代码

```java
package org.config.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConfigController {
	
	 @Autowired
	 UserService userService;
	 
	@RequestMapping(value = "/consumer",method = RequestMethod.GET)
	public String getClientInfo(HttpServletRequest request){
	  return userService.getClientInfo(request.getParameter("name"));
	}
}
```

服务提供者代码

```java
package com.boot.controller;

import javax.servlet.http.HttpServletRequest;

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

	@RequestMapping(value = "/provider", method = RequestMethod.GET)
	public String home(HttpServletRequest request) {
		ServiceInstance instance = client.getLocalServiceInstance();
		System.out.println(request.getParameter("name"));
		System.out.println(
				"serviceId=" + instance.getServiceId() + ",host=" + instance.getHost() + ":" + instance.getPort());
		return "hello spring";
	}
}
```

此时访问消费者http://localhost:7001/consumer?name=123   发现服务提供者输出了name的值123



消费方和提供方部分代码：

消费方

```java
@RestController
public class ConfigController {
	
	 @Autowired
	 UserService userService;
	 
	@RequestMapping(value = "/consumer",method = RequestMethod.GET)
	public String getClientInfo(HttpServletRequest request){
	  return userService.getClientInfo(request.getParameter("name"));
	}
	
	@RequestMapping(value = "/consumer01/{name}",method = RequestMethod.GET)
	public String getClientInfo01(HttpServletRequest request,@PathVariable("name") String name){
	  return userService.getClientInfo01(name);
	}
	
	@RequestMapping(value = "/consumer02",method = RequestMethod.POST)
	public String getClientInfo02(HttpServletRequest request){
		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("name", "wukang");
		param.put("age", 30);
	  return userService.getClientInfo02(param);
	}
}
```

```java
@FeignClient(name = "configServer")
public interface UserService {
	
	 @RequestMapping(value = "/provider",method = RequestMethod.GET)
	 String getClientInfo(@RequestParam("name") String name);
	 
	 @RequestMapping( value = "/provider/{name}",method = RequestMethod.GET)
	 String getClientInfo01(@PathVariable("name") String name);
	 
	  @RequestMapping( value = "/provider",method = RequestMethod.POST)
	  public String getClientInfo02(@RequestBody Map<String,Object> param);
}
```

提供方

```java
@RestController
public class ConfigServerController {

	@Autowired
	private DiscoveryClient client;

	@RequestMapping(value = "/provider", method = RequestMethod.GET)
	public String provider01(HttpServletRequest request) {
		ServiceInstance instance = client.getLocalServiceInstance();
		System.out.println(request.getParameter("name"));
		System.out.println(
				"serviceId=" + instance.getServiceId() + ",host=" + instance.getHost() + ":" + instance.getPort());
		return "hello spring";
	}
	
	@RequestMapping(value = "/provider/{name}", method = RequestMethod.GET)
	public String provider02(HttpServletRequest request,@PathVariable("name") String name) {
		System.out.println("====name:"+name);
		return "hello spring provider02";
	}
	
	@RequestMapping(value = "/provider", method = RequestMethod.POST)
	public String provider03(@RequestBody Map<String,Object> param) {
		System.out.println("====name:"+param);
		return "hello spring provider03";
	}
}
```



# 四、自定义Feign的 配置

## 1、自定义Configuration

```
@Configuration
public class FooConfiguration {
    @Bean
    public Contract feignContract() {
        //这将SpringMvc Contract 替换为feign.Contract.Default
        return new feign.Contract.Default();
    }
}
```

## 2、使用自定义的Configuration

```java
//feign的注解
@FeignClient(name = "configServer",configuration=FooConfiguration.class)
public interface UserService {
	
	  @RequestLine("GET /provider/{name}")
	  String getClientInfo03(@Param("name") String name);
}
```

# 五、Feign日志的配置

```properties
logging.level.org.config.controller.UserService=DEBUG 
```

```java
package org.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.Contract;
import feign.Logger;

@Configuration
public class FooConfiguration {
	 @Bean
	 Logger.Level feignLoggerLevel() {
	        //设置日志
	  return Logger.Level.FULL;
    }
}
```

```java
@FeignClient(name = "configServer",configuration=FooConfiguration.class)
public interface UserService {
	
	  @RequestLine("GET /provider/{name}")
	  String getClientInfo03(@Param("name") String name);
}
```

# 六、Feign请求超时问题

Hystrix默认的超时时间是1秒，如果超过这个时间尚未响应，将会进入fallback代码。而首次请求往往会比较慢（因为Spring的懒加载机制，要实例化一些类），这个响应时间可能就大于1秒了
解决方案有三种，以feign为例。
方法一
hystrix.command.default.execution.isolation.thread.timeoutInMilliseconds: 5000
该配置是让Hystrix的超时时间改为5秒
方法二
hystrix.command.default.execution.timeout.enabled: false
该配置，用于禁用Hystrix的超时时间
方法三
feign.hystrix.enabled: false
该配置，用于索性禁用feign的hystrix。该做法除非一些特殊场景，不推荐使用。









