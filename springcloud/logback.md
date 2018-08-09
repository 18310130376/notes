# 一、日志

## 1.1 配置控制台日志的debug级别

默认情况下，spring boot从控制台打印出来的日志级别只有ERROR, WARN 还有INFO，如果你想要打印debug级别的日志，可以通过application.properites配置debug=true

```
debug=true
```

## 1.2 在生产环境环境

```
java -jar C:\Users\Administrator\Desktop\xx\demo.jar --debug
```

## 1.3包的日志级别

```
logging.level.root=INFO
logging.level.org.springframework.web=DEBUG
logging.level.org.hibernate=ERROR
```

## 1.4 将日志输出到文件中

默认情况下spring boot是不将日志输出到日志文件中，但你可以通过在application.properites文件中配置logging.file文件名称和logging.path文件路径，将日志输出到文件中

```
logging.path=F:\\demo
logging.file=demo.log
logging.level.root=info
```

这里需要注意几点：

1. 这里若不配置具体的包的日志级别，日志文件信息将为空

2. 若只配置logging.path，那么将会在F:\demo文件夹生成一个日志文件为spring.log（ps：该文件名是固定的，不能更改）。如果path路径不存在，会自动创建该文件夹

3. 若只配置logging.file，那将会在项目的当前路径下生成一个demo.log日志文件。这里可以使用绝对路径如，会自动在e盘下创建文件夹和相应的日志文件。

   ```
   logging.file=e:\\demo\\demo.log
   ```


4. logging.path和logging.file同时配置，不会在这个路径有F:\demo\demo.log日志生成，logging.path和logging.file不会进行叠加（要注意）

5. logging.path和logging.file的value都可以是相对路径或者绝对路径

这就是基础的日志配置，可以直接在application.properties配置，我们还可以在classpath路径下，通过定义具体的日志文件来配置——logback.xml

## 1.5 logback

主要依赖

```
logback-access-1.0.0.jar
logback-classic-1.0.0.jar
logback-core-1.0.0.jar
slf4j-api-1.6.0.jar
```

maven配置的话会下载上面的所有依赖

```
<dependency>  
    <groupId>ch.qos.logback</groupId>  
    <artifactId>logback-classic</artifactId>  
    <version>1.0.11</version>  
</dependency> 
```



 **1、Logback默认配置的步骤**

​     (1). 尝试在 classpath 下查找文件 logback-test.xml；

​     (2). 如果文件不存在，则查找文件 logback.xml；

​     (3). 如果两个文件都不存在，logback 用 Bas icConfigurator 自动对自己进行配置，这会导致记录输出到控制台。

https://logback.qos.ch/manual/configuration.html

application.properties里

```
logging.config=classpath:logback.xml
logging.path=d:/logs

#像eureka服务注册信息时，使用ip地址，默认使用hostname
eureka.instance.preferIpAddress=true
eureka.instance.instance-id=${spring.cloud.client.ipAddress}:${server.port}
eureka.client.serviceUrl.defaultZone=http://01.server.eureka:8081/eureka/

#开启健康检查(需要spring-boot-starter-actuator依赖)
eureka.client.healthcheck.enabled=true
#租期到期时间，默认90秒
eureka.instance.lease-expiration-duration-in-seconds=30
#租赁更新时间间隔，默认30，即30秒发送一次心跳
eureka.instance.lease-renewal-interval-in-seconds=10


#启动负载均衡的重试机制，默认false
spring.cloud.loadbalancer.retry.enabled=true
#Hystrix是否启用超时时间
hystrix.command.default.execution.timeout.enabled=true
#Hystrix断路器的超时时间，默认是1s，断路器的超时时间需要大于ribbon的超时时间，不然不会触发重试。
hystrix.command.default.execution.isolation.thread.timeoutInMilliseconds=2000


#ribbon请求连接的超时时间
ribbon.ConnectTimeout=250
#请求处理的超时时间
ribbon.ReadTimeout=1000
#对所有请求操作都进行重试
ribbon.OkToRetryOnAllOperations=true
#对当前服务的重试次数（第一次分配给9082的时候，如果404，则再重试MaxAutoRetries次，如果还是404，则切换到其他服务MaxAutoRetriesNextServer决定）
ribbon.MaxAutoRetries=0
#切换服务的次数(比如本次请求分配给9082处理，发现404，则切换分配给9081处理，如果还是404，则返回404给客户端）
ribbon.MaxAutoRetriesNextServer=1
```

示例：

```
package chapters.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyApp1 {
  final static Logger logger = LoggerFactory.getLogger(MyApp1.class);

  public static void main(String[] args) {
    logger.info("Entering application.");

    Foo foo = new Foo();
    foo.doIt();
    logger.info("Exiting application.");
  }
}
```



```
<configuration scan="true" scanPeriod="10 seconds">
	<!-- <property file="application.properties" /> -->
	<!-- <property name="USER_HOME" value="/home/sebastien" /> -->

	<springProperty scope="context" name="LOG_HOME" source="logging.path" />
	<appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
		<encoder>
			<pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} -
				%msg%n</pattern>
		</encoder>
	</appender>

	<appender name="INFO_FILE"
		class="ch.qos.logback.core.rolling.RollingFileAppender">
		<File>${LOG_HOME}/info.log</File>
		<rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
			<fileNamePattern>${LOG_HOME}/info-%d{yyyyMMdd}.log.%i
			</fileNamePattern>
			<timeBasedFileNamingAndTriggeringPolicy
				class="ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP">
				<maxFileSize>500MB</maxFileSize>
			</timeBasedFileNamingAndTriggeringPolicy>
			<!--日志文件保留天数 -->
			<maxHistory>2</maxHistory>
		</rollingPolicy>
		<!-- <layout class="ch.qos.logback.classic.PatternLayout">
			<Pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36}
				-%msg%n
			</Pattern>
		</layout> -->
		<encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder">
                <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{50} - %msg%n</pattern>
       </encoder>
		<!--日志文件最大的大小 -->
		<triggeringPolicy
			class="ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy">
			<MaxFileSize>10MB</MaxFileSize>
		</triggeringPolicy>

	</appender>

	<appender name="ERROR_FILE"
		class="ch.qos.logback.core.rolling.RollingFileAppender">
		<filter class="ch.qos.logback.classic.filter.ThresholdFilter">
			<level>ERROR</level>
		</filter>
		<File>${logging.path}/error.log</File>
		<rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
			<fileNamePattern>${LOG_PATH}/error-%d{yyyyMMdd}.log.%i
			</fileNamePattern>
			<timeBasedFileNamingAndTriggeringPolicy
				class="ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP">
				<maxFileSize>500MB</maxFileSize>
			</timeBasedFileNamingAndTriggeringPolicy>
			<maxHistory>2</maxHistory>
		</rollingPolicy>
		<layout class="ch.qos.logback.classic.PatternLayout">
			<Pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36}
				-%msg%n</Pattern>
		</layout>
	</appender>

	<!--配置mybatis sql 日志 -->
	<logger name="com.pf.org.cms.mapper" level="DEBUG" />

	<root level="INFO">
		<appender-ref ref="INFO_FILE" />
	</root>

	<springProfile name="dev">
		<root level="INFO">
			<appender-ref ref="INFO_FILE" />
		</root>
	</springProfile>

	<springProfile name="pro">
		<root level="INFO">
			<appender-ref ref="INFO_FILE" />
		</root>
	</springProfile>

	<!--日志异步到数据库 -->
	<!-- <appender name="DB" class="ch.qos.logback.classic.db.DBAppender"> <connectionSource 
		class="ch.qos.logback.core.db.DriverManagerConnectionSource"> <dataSource 
		class="com.mchange.v2.c3p0.ComboPooledDataSource"> <driverClass>com.mysql.jdbc.Driver</driverClass> 
		<url>jdbc:mysql://127.0.0.1:3306/databaseName</url> <user>root</user> <password>root</password> 
		</dataSource> </connectionSource> </appender> -->

</configuration>
```

## 1.6多环境日志

多环境时命名为logback-spring.xml，

```
logging.config=classpath:logback-spring.xml
```

```
<springProfile name="dev">
		<root level="INFO">
			<appender-ref ref="INFO_FILE" />
		</root>
	</springProfile>

	<springProfile name="pro">
		<root level="INFO">
			<appender-ref ref="INFO_FILE" />
		</root>
	</springProfile>
```



# 二、log4j转为logback

https://logback.qos.ch/translator/