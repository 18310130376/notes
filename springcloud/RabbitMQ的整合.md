## 消息总线Spring Cloud Netflix Bus

在本教程第三讲[Spring Cloud 入门教程(三)： 配置自动刷新](http://www.cnblogs.com/chry/p/7260778.html)中，通过POST方式向客户端发送/refresh请求， 可以让客户端获取到配置的最新变化。但试想一下， 在分布式系统中，如果存在很多个客户端都需要刷新改配置，通过这种方式去刷新也是一种非常痛苦的事情。那有没有什么办法让系统自动完成呢？ 之前我们提到用githook或者jenkins等外部工具来触发。现在说另外一种思路， 如果refresh命令可以发送给config server，然后config server自动通知所有config client， 那么就可以大大简化配置刷新工作。这样，虽然仍然需要通过refresh命令触发， 但通过webhook等钩子方式， 我们只需要将关联的命令挂到配置中心上，而不需要每个配置客户端都去关联。

现在，我们通过整合消息队列Rabbitmq来完成这件事。我们的目标是， 当git仓库中的配置一旦更改，将refresh命令发送给配置中心，然后配置中心通过消息队列，自动通知所有使用了该配置的刷新各自配置。

Spring Cloud Netflix Bus是Spring Cloud的消息机制,当Git Repository 改变时,通过POST请求Config Server的/bus/refresh,Config Server 会从repository获取最新的信息并通过amqp传递给client,如图所示.



# 一、RabbitMQ的安装和配置

自行解决



# 二、配置服务

1. config Server和config client两个项目中均添加一些内容, 在pom.xml中增加

```xml
<dependency>  
   <groupId>org.springframework.boot</groupId>  
    <artifactId>spring-boot-starter-actuator</artifactId>  
</dependency>
<dependency>  
    <groupId>org.springframework.cloud</groupId>  
    <artifactId>spring-cloud-starter-bus-amqp</artifactId>  
</dependency>
```

2. 在配置文件application.properties（config client里面是bootstrap.properties）中增加关于RabbitMQ的连接和用户信息:

```
spring：
    rabbitmq:
       host: localhost
       port: 5672
       username: springcloud
       password: 123456
```

3. 以上配置高好后，配置刷新，就只需要向config server发送 /bus/refresh的POST请求就可以了. 
4. 如果用poster等发送POST请求是出现错误：“`Full authentication is required to access this resource.", 那么可以在yml配置文件中加入以下内容禁止Config server的权限拦截。`

```
management:
  security:
    enabled: false  #忽略权限拦截
```



