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

## 一、 引入依赖

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
spring.application.name=configClient
spring.cloud.config.label=master
spring.cloud.config.profile=dev
spring.cloud.config.uri= http://localhost:8001/
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

`${spring.application.name}-${spring.cloud.config.profile}.proprties`

①如果客户端指定找spring.cloud.config.profile=dev，但是git仓库文件是configClient.properties，则也会匹配到。如果git仓库存在configClient-dev.properties，则只会加载这个文件。

②如果客户端没指定spring.cloud.config.profile=dev，则会找configClient.properties，并不会找configClient-dev.properties