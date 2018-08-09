# 一、前言

在微服务架构中，根据业务来拆分成一个个的服务，服务与服务之间可以相互调用（RPC），在Spring Cloud可以用RestTemplate+Ribbon和Feign来调用。为了保证其高可用，单个服务通常会集群部署。由于网络原因或者自身的原因，服务并不能保证100%可用，如果单个服务出现问题，调用这个服务就会出现线程阻塞，此时若有大量的请求涌入，Servlet容器的线程资源会被消耗完毕，导致服务瘫痪。服务与服务之间的依赖性，故障会传播，会对整个微服务系统造成灾难性的严重后果，这就是服务故障的“雪崩”效应。

Netflix开源了Hystrix组件，实现了断路器模式，SpringCloud对这一组件进行了整合

当对特定的服务的调用的不可用达到一个阀值（Hystric 是5秒20次） 断路器将会被打开，断路打开后，可用避免连锁故障，fallback方法可以直接返回一个固定值。

# 二、在ribbon使用断路器

修改消费者代码

```xml
<!--新版本-->
<dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
</dependency>

<!--旧版本-->
<dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-hystrix</artifactId>
</dependency>


<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-ribbon</artifactId>
</dependency>
```



启动类上加入@EnableHystrix

```java
package org.config;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@EnableHystrix //或者 @EnableCircuitBreaker
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

```java
package org.config.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Service
public class UService {
	
	@Autowired
    RestTemplate restTemplate;
	
	@HystrixCommand(fallbackMethod = "hiError")
    public String hiService(String name) {
        return restTemplate.getForObject("http://configServer/provider?name="+name,String.class);
    }

    public String hiError(String name) {
        return "hi,"+name+",sorry,error!";
    }
}
```

```java
@RestController
public class ConfigController {
	
	 @Autowired
	 UService uService;
	
	@RequestMapping(value = "/consumer01/{name}",method = RequestMethod.GET)
	public String getClientInfo01(HttpServletRequest request,@PathVariable("name") String name){
	  return uService.hiService(name);
	}
}
```

此时访问http://localhost:7001/consumer01/456  正常

关闭服务提供者configServer，此时再次访问http://localhost:7001/consumer01/456 返回 hi,456,sorry,error!



# 三、Feign中使用断路器

Feign是自带断路器的，在D版本的Spring Cloud之后，它没有默认打开。需要在配置文件中配置打开它，在配置文件加以下代码

```properties
#hystrix.command.default.execution.isolation.thread.timeoutInMilliseconds=5000
feign.hystrix.enabled=true
```

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-hystrix</artifactId>
</dependency>
```

```java
package org.config.controller;

import org.config.FooConfiguration;
import org.springframework.cloud.netflix.feign.FeignClient;

import feign.Param;
import feign.RequestLine;


@FeignClient(name = "configServer",configuration=FooConfiguration.class,fallback = UserServiceHystrix.class)
public interface UserService {
	
	  @RequestLine("GET /provider/{name}")
	  String getClientInfo03(@Param("name") String name);
}
```

```java
package org.config.controller;

import org.springframework.stereotype.Component;

//必须加Component注解，注入到ioc容器中
@Component  
public class UserServiceHystrix implements UserService{

	@Override
	public String getClientInfo03(String name) {
		return "sorry "+name;
	}
}
```



```java
@RestController
public class ConfigController {
	
	@Autowired
	UserService userService;
	
	@RequestMapping(value = "/consumer01/{name}",method = RequestMethod.GET)
	public String getClientInfo01(HttpServletRequest request,@PathVariable("name") String name){
	  return userService.getClientInfo03(name);
	}
}
```

测试，断开服务提供者，访问http://localhost:7001/consumer01/456  提示  sorry 456



# 四、注意事项

```java
 @Service
 public class HelloService {
     
     @Autowired RestTemplate restTemplate;
     
     @HystrixCommand(fallbackMethod = "serviceFailure")
     public String getHelloContent() {
         return restTemplate.getForObject("http://SERVICE-HELLOWORLD/",String.class);
     }
     public String serviceFailure() {
         return "hello world service is not available !";
     }
}
```

第一，  fallbackMethod的返回值和参数类型需要和被@HystrixCommand注解的方法完全一致。否则会在运行时抛出异常。比如本例中，serviceFailure（）的返回值和getHelloContant()方法的返回值都是String。

第二，  当底层服务失败后，fallbackMethod替换的不是整个被@HystrixCommand注解的方法（本例中的getHelloContant), 替换的只是通过restTemplate去访问的具体服务。可以从中的system输出看到， 即使失败，控制台输出里面依然会有“call SERVICE-HELLOWORLD”。