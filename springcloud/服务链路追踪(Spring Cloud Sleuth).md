# 前言

这篇文章主要讲述服务追踪组件zipkin，Spring Cloud Sleuth集成了zipkin组件。

# 一、服务追踪分析

微服务架构上通过业务来划分服务的，通过REST调用，对外暴露的一个接口，可能需要很多个服务协同才能完成这个接口功能，如果链路上任何一个服务出现问题或者网络超时，都会形成导致接口调用失败。随着业务的不断扩张，服务之间互相调用会越来越复杂。



### 一、下载zipkin-server服务器

使用Spring Cloud为`Finchley`版的时候，已经不需要自己构建Zipkin Server了，只需要下载jar即可，
下载地址：

```
https://dl.bintray.com/openzipkin/maven/io/zipkin/java/zipkin-server
```

linux下的下载地址

```
wget https://dl.bintray.com/openzipkin/maven/io/zipkin/java/zipkin-server/2.10.2/zipkin-server-2.10.2-exec.jar
```

启动方式：

```
java -jar zipkin-server-2.10.2-exec.jar
```



### 二、构建项目

构建两个子项目，这里用configClient调用configServer，分别引入下面依赖

```
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-zipkin</artifactId>
</dependency>
```

application.properties里指定zipkin server的地址

```
spring.zipkin.base-url=http://localhost:9411
```

启动zipkin-server和这两个工程，访问configClient调用configServer