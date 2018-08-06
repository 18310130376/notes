# 一、运行方式

### 开始

```java
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.example</groupId>
    <artifactId>myproject</artifactId>
    <version>0.0.1-SNAPSHOT</version>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>1.5.9.RELEASE</version>
    </parent>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

</project>
```

```java
package com.docker;

import java.util.concurrent.CountDownLatch;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication 
public class DockerApplication {

	private static final Log logger = LogFactory.getLog(DockerApplication.class);
	
    public static void main(String[] args) throws InterruptedException{
    	
	new SpringApplicationBuilder().sources(DockerApplication.class).web(false).run(args);
	logger.info("==Project started successfull==");
   }
}
```

或者

```java
package com.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

@SpringBootApplication
public class FeignConsumerApplication {

	public static void main(String[] args) {
		SpringApplication.run(FeignConsumerApplication.class, args);
		System.out.println("====application start successful==");
	}
}
```



### 打包

```
<plugin>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-maven-plugin</artifactId>
</plugin>
```

此种方式只能使用java -jar启动运行

### 启动

```java
java -jar springboot-release.jar --server.port=9090
java -jar springboot-release.jar  --spring.prifiles.active=dev &
nohup java -jar yourapp.jar &
java -jar yourapp.jar &
mvn spring-boot:run
java -jar myapp.jar --debug
或者
application.properties配置  debug=true
java -jar demo.jar --Dspring.config.location=conf/application.properties


java -jar -Dloader.path=.,3rd-lib test-0.0.1-SNAPSHOT-classes.jar
```

### 完全可执行Jar

```shell
ps -ef|grep java 
##拿到对于Java程序的pid
kill -9 pid
ps aux | grep spring | xargs kill -9
```

如果配置下面方式，则可以把jar包当做linux服务

```xml
<plugin>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-maven-plugin</artifactId>
    <configuration>
        <executable>true</executable>
    </configuration>
</plugin>
```

启动方式：

1、 可以直接`./yourapp.jar` 来启动

2、注册为服务

也可以做一个软链接指向你的jar包并加入到`init.d`中，然后用命令来启动。

init.d 例子:

```
ln -s /var/yourapp/yourapp.jar /etc/init.d/yourapp
chmod +x /etc/init.d/yourapp
```

这样就可以使用`stop`或者是`restart`命令去管理你的应用。

```
/etc/init.d/yourapp start|stop|restart
```

或者

```
service yourapp start|stop|restart
```

### 生产运维

查看JVM参数的值

可以根据java自带的jinfo命令：

```
jinfo -flags pid
```

来查看jar 启动后使用的是什么gc、新生代、老年代分批的内存都是多少，示例如下：

```
-XX:CICompilerCount=3 -XX:InitialHeapSize=234881024 -XX:MaxHeapSize=3743416320 -XX:MaxNewSize=1247805440 -XX:MinHeapDeltaBytes=524288 -XX:NewSize=78118912 -XX:OldSize=156762112 -XX:+UseCompressedClassPointers -XX:+UseCompressedOops -XX:+UseFastUnorderedTimeStamps -XX:+UseParallelGC
```

- `-XX:CICompilerCount` ：最大的并行编译数
- `-XX:InitialHeapSize` 和 `-XX:MaxHeapSize` ：指定JVM的初始和最大堆内存大小
- `-XX:MaxNewSize` ： JVM堆区域新生代内存的最大可分配大小
- `-XX:+UseParallelGC` ：垃圾回收使用Parallel收集器

# 二、属性配置

## @value赋值

###  一、给静态变量赋值

@Value必须修饰在方法上，且set方法不能有static  

```java
package com.integration.boot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class StaticProperties {

	public static String  SPRING_APPLICATION_NAME;
	
	@Value("${spring.application.name}")
    @Value("${spring.application.name:abc}") //如果没设置值则用默认值
	private void setApplicationName(String applicationName){
		this.SPRING_APPLICATION_NAME = applicationName;
	}
}
```

### 二、注入普通字符串

```java
@Value("normal")
private String normal; // 注入普通字符串
```

### 三 、注入操作系统属性

```java
@Value("#{systemProperties['os.name']}")
private String systemPropertiesName; // 注入操作系统属性
```

### 四 、注入表达式结果

```java
@Value("#{ T(java.lang.Math).random() * 100.0 }")
private double randomNumber; //注入表达式结果
```

### 五 、注入其他Bean属性

```java
@Value("#{beanInject.another}")
    private String fromAnotherBean; //注入其他Bean属性：注入beanInject对象的属性another，类具体定义见下面
```

### 六 、注入文件资源

```java
@Value("classpath:com/hry/spring/configinject/config.txt")
private Resource resourceFile; // 注入文件资源
```

### 七、 指定文件的属性

@Value("#{configProperties['t1.msgname']}")这种形式的配置中有“configProperties”，其实它指定的是配置文件的加载对象：配置如下

```xml
<bean id="settings" class="org.springframework.beans.factory.config.PropertiesFactoryBean">  
        <property name="locations">  
            <list>  
                <value>classpath:/config/t1.properties</value>  
            </list>  
        </property>  
</bean> 

或者
<util:properties id="settings" location="WEB-INF/classes/META-INF/spring/test.properties" />
```

```java

 private String imageDir;   

 @Value("#{settings['test.abc']}")     
    public void setImageDir(String val) {     
        this.imageDir = val;     
  }
```

### 八、占位符属性

application.properties

可以在配置文件中引用前面配置过的属性

```properties
aaaa=4789
test.abc=123${aaaa}
```

获取test.abc的值应为1234789

### 九、数组注入

属性文件中

```properties
aaaa=4789
test.abc[0]=123${aaaa}
test.abc[1]=aaaaa${aaaa}
```

```java
@ConfigurationProperties(prefix = "test")
public class ConfigBean {
	
	private List<String> abc;

	public List<String> getAbc() {
		return abc;
	}

	public void setAbc(List<String> abc) {
		this.abc = abc;
	}
}
```

`configBean.getAbc()` 输出

```json
[1234789, aaaaa4789]
```

### 十、默认值

```properties
server.port=${port:8080}
```

那么就可以使用更短的–port=9090，当不提供该参数的时候使用默认值8080。

### 十一、PropertiesLoaderUtils

PropertiesLoaderUtils加载properties

```
 PropertiesLoaderUtils.loadAllProperties("application.properties");
```

### 十一、Environment

```java
 public static void main(String[] args) throws InterruptedException {
    	  
    	  loadLogConfig();
    	  ConfigurableApplicationContext run = SpringApplication.run(Application.class, args);
    	  Environment environment = run.getBean(Environment.class);
    	  String property = environment.getProperty("spring.dubbo.application.name");
    	  System.out.println(property);
          logger.info("======dubbo-consumer started successfull ======");
    }
```

```java
   @Autowired
    private Environment environment;
    
    @Test
    public void loadProperties() {
    	String property = environment.getProperty("spring.dubbo.application.name");
    	System.out.println(property);
  }
```



## @PropertySource自定义配置文件

```java
@Component   // 或者 @Configuration
//引入外部配置文件组：${app.configinject}的值来自config.properties。
@PropertySource({"classpath:com/hry/spring/configinject/config.properties",
    "classpath:com/hry/spring/configinject/config_${anotherfile.configinject}.properties"})

//@PropertySource(value = "application.properties", ignoreResourceNotFound = true)

public class ConfigurationFileInject{
    @Value("${app.name}")
    private String appName; // 这里的值来自application.properties，spring boot启动时默认加载此文件

    @Value("${book.name}")
    private String bookName; // 注入第一个配置外部文件属性

    @Value("${book.name.placeholder}")
    private String bookNamePlaceholder; // 注入第二个配置外部文件属性

    @Autowired
    private Environment env;  // 注入环境变量对象，存储注入的属性值
}
```

或者ConfigurationProperties注入实体

```java
@Configuration
@PropertySource(value = "classpath:test.properties")
@ConfigurationProperties(prefix = "com.forezp")
public class User {
    private String name;
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
```

如果实体不加@Component，则在启动类上加：

```java
@EnableConfigurationProperties({User.class，ConfigurationFileInject.class})
```

## Environment

## @ConfigurationProperties 

```java

@ConfigurationProperties(locations = "classpath:mail.properties", ignoreUnknownFields = false, 
                         prefix = "mail")
@Component
public class MailProperties { 
  public static class Smtp {  
    private boolean auth;  
    private boolean starttlsEnable;  
    //getters and setters 
  }
  private String host;
  private int port;  
  private String from; 
  private String username;
  private String password; 
}
```

注意点：类上必须加 @Component（用下面方式可以不加@Component）

如果在类上不加 @Component，则需要在启动类上加入：

```java
@SpringBootApplication
@EnableConfigurationProperties({ConfigBean.class})
public class Application{

    private static final Log logger = LogFactory.getLog(Application.class);
    
    @Bean
    public CountDownLatch closeLatch() {
        return new CountDownLatch(1);
    }
    
    public static void main(String[] args) throws InterruptedException {

    	  ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
          logger.info("======dubbo-consumer started successfull ======");
          CountDownLatch closeLatch = context.getBean(CountDownLatch.class);
          closeLatch.await();
    }
}
```

## ImportResource引入xml配置

```java
@SpringBootApplication
@ImportResource("classpath:spring-dubbo.xml")
public class Application {

	private static final Log logger = LogFactory.getLog(Application.class);

	@Bean
	public CountDownLatch closeLatch() {
		return new CountDownLatch(1);
	}

	public static void main(String[] args) throws InterruptedException {
		ApplicationContext ctx = new SpringApplicationBuilder()
				.sources(Application.class).web(false).run(args);
		logger.info("======dubbo-provider started successfull ======");
		CountDownLatch closeLatch = ctx.getBean(CountDownLatch.class);
		closeLatch.await();
	}
}
```





# 三 、Configuration、SpringBootConfiguration声明bean

```
@SpringBootConfiguration继承自@Configuration，二者功能也一致，标注当前类是配置类
并会将当前类内声明的一个或多个以@Bean注解标记的方法的实例纳入到spring容器中，并且实例名就是方法名。
```

```java
package com.dxz.demo.configuration;

import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfiguration {
    public TestConfiguration() {
        System.out.println("TestConfiguration容器启动初始化。。。");
    }
}
```

测试

```java
package com.dxz.demo.configuration;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class TestMain {
  public static void main(String[] args) {

//@Configuration注解的spring容器加载方式，用AnnotationConfigApplicationContext替换ClassPathXmlApplicationContext
  ApplicationContext context = new AnnotationConfigApplicationContext(TestConfiguration.class);
//如果加载spring-context.xml文件：
//ApplicationContext context = new ClassPathXmlApplicationContext("spring-context.xml");
    }
}
```



# 四 、多环境

## @Profile 

```java
public interface  ProfileService {
    String getProfileDomain();
}
```

```java
@Profile(value = "prod")
@Service
public class ProfileProdServiceImpl implements ProfileService {
 
    public ProfileProdServiceImpl() {
 
        System.out.println("我是生产环境。。。。。");
    }
 
    @Override
    public String getProfileDomain() {
        StringBuilder sb = new StringBuilder();
        sb.append("我在生产环境，").append("我可以吃鸡鸭鱼牛羊肉。。。。");
        return sb.toString();
    }
}
```



```java
@Profile(value = "dev")
@Service
public class ProfileDevServiceImpl implements ProfileService {
 
    public ProfileDevServiceImpl() {
 
        System.out.println("我是开发环境。。。。。");
    }
 
    @Override
    public String getProfileDomain() {
        StringBuilder sb = new StringBuilder();
        sb.append("我在开发环境，").append("我只能吃加班餐：大米饭。。。。");
        return sb.toString();
    }
}
```

测试，当我们的spring.profiles.active为prod的时候 或者dev时看到不同的效果：

```java
@RestController
public class ProfileController {
 
    @Autowired
    private ProfileDomain profileDomain;
    @Autowired
    private ProfileService profileService;
 
    @RequestMapping("testProfile")
    public ProfileDomain testProfile() {
 
        return profileDomain;
    }
    @RequestMapping("testProfile2")
    public String testProfile2() {
        return profileService.getProfileDomain();
    }
}
```

## Spring Boot Profile多环境配置

### 方式一 (推荐)

#### 原理

实现：根据mvn打包时更新application.properties值，打包对应环境的properties 到jar包里。

#### 创建多环境配置文件

`application.properties`

```
spring.profiles.active=@profileActive@  //替换成pom的profileActive值
```

`application-dev.properties `

```
test.name=vermouth.dev
test.age=18.dev
```

`application-prod.properties `

```
test.name=sherry.prod
test.age=20.dev
```

#### 修改pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">

    <groupId>com.example</groupId>
    <artifactId>demo</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <packaging>war</packaging>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.0.2.RELEASE</version>
        <relativePath/>
    </parent>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
        <java.version>1.8</java.version>
    </properties>

    <dependencies>
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

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
        <resources>
            <resource>
                <directory>src/main/resources</directory>
                <excludes>
                    <exclude>application*.properties</exclude>
                </excludes>
            </resource>
            <resource>
                <directory>src/main/resources</directory>
                <filtering>true</filtering>
                <includes>      <!-- 根据打包时激活的profile来记载对应的配置文件 -->
                    <include>application.properties</include>
                    <include>application-${profileActive}.properties</include>
                </includes>
            </resource>
        </resources>
    </build>

    <profiles>
        <profile>
            <id>dev</id>
            <activation>
                <activeByDefault>true</activeByDefault>
            </activation>
            <properties>
                <profileActive>dev</profileActive>
            </properties>
        </profile>

        <profile>
            <id>prod</id>
            <properties>
                <profileActive>prod</profileActive>
            </properties>
        </profile>
    </profiles>
</project>
```

#### 打包

```
mvn clean package -P prod
```

如图可见，打包后就只加载了`application.properties`和`application-prod.properties` 

打包后的application.properties中：

```
spring.profiles.active=prod
```



### 方式二

#### 原理

把环境信息配置在不同的配置文件中，同时在`application.properties`中使用占位符，再编译打包时动态地把那些占位符替换掉 

#### 创建多个配置文件

- appication.properties:

  ```
  test.name=@name@            # spring boot的application.properties中的默认占位符是@..@
  test.age=${age}             # 这个占位符可以通过在pom.xml中配置来使用
  ```

- application-dev.properties

  ```
  name=vermouth.dev
  age=18.dev
  ```

- application-prod.properties

  ```
  name=sherry.prod
  age=20.prod
  ```

#### 修改pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.example</groupId>
    <artifactId>demo</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <packaging>war</packaging>

    <name>demo</name>
    <description>Demo project for Spring Boot</description>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.0.3.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
        <java.version>1.8</java.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
            <exclusions>
                <exclusion>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-starter-logging</artifactId>
                </exclusion>
            </exclusions>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-log4j</artifactId>
            <version>1.3.8.RELEASE</version>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <profiles>
        <profile>
            <id>prod</id>
            <properties>
                <build.profile.id>prod</build.profile.id>
            </properties>
        </profile>
        <profile>
            <id>dev</id>
            <activation>
                <activeByDefault>true</activeByDefault>
            </activation>
            <properties>
                <build.profile.id>dev</build.profile.id>
            </properties>
        </profile>
    </profiles>

    <build>
        <filters>
            <filter>  
                <!-- filter中的文件表示替换源，即用这里面的文件中的value值替换那些占位符 -->
                <!-- 如果filters中有多个filter，且这些filter中有相同的key，则后面的filter中的value值为最终值 -->
                ${basedir}/src/main/resources/application-${build.profile.id}.properties
            </filter>
        </filters>

        <resources>
            <resource>
                <filtering>true</filtering>     
                <!-- 这个表示对src/main/resources中的配置文件中的占位符进行动态替换 -->
                <directory>src/main/resources</directory>
            </resource>
        </resources>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-resources-plugin</artifactId>
                <configuration>
                    <delimiters>
                        <delimiter>${*}</delimiter>     <!-- 使用${..}作为占位符 -->
                    </delimiters>
                    <useDefaultDelimiters>true</useDefaultDelimiters>
                    <!-- 使用默认的占位符（@..@） -->
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>

```

#### 打包

```
mvn clean package -P prod
```

打包后的application.properties中：

```
test.name=sherry.prod
test.age=20.prod
```

### 方式三

原理：指定文件内容拷贝到新的文件中

```xml
<plugin>
				<groupId>com.coderplus.maven.plugins</groupId>
				<artifactId>copy-rename-maven-plugin</artifactId>
				<version>1.0</version>
				<executions>
					<execution>
						<id>copy-file</id>
						<phase>prepare-package</phase>
						<goals>
							<goal>copy</goal>
						</goals>
						<configuration>
							<fileSets>
								<fileSet>								<sourceFile>src/main/resources/release/init_${releaseEnv}.properties</sourceFile>
<destinationFile>${basedir}/target/classes/init.properties</destinationFile>
								</fileSet>
							</fileSets>
						</configuration>
					</execution>
				</executions>
			</plugin>
```

### 方式四

```xml
<profiles>
		<profile>
			<id>dev</id>
			<properties>
				<active.profile>dev</active.profile>
				<application.name>dubbo-provider</application.name>
				<registry.address>zookeeper://127.0.0.1:2181</registry.address>
				<protocol.name>dubbo</protocol.name>
				<protocol.port>20880</protocol.port>
				<scan.basepackage>com.integration.boot.provider.service</scan.basepackage>
			</properties>
			<activation>
				<activeByDefault>true</activeByDefault>
			</activation>
		</profile>
		<profile>
			<id>test</id>
			<properties>
				<active.profile>test</active.profile>
				<application.name>dubbo-demo-server-test</application.name>
				<registry.address>N/A</registry.address>
				<protocol.name>dubbo</protocol.name>
				<protocol.port>20880</protocol.port>
				<scan.basepackage>cn.wolfcode.dubbo.service</scan.basepackage>
			</properties>
		</profile>
		<profile>
			<id>prd</id>
			<properties>
				<active.profile>prd</active.profile>
				<application.name>dubbo-demo-server</application.name>
				<registry.address>zookeeper://192.168.56.101:2181</registry.address>
				<protocol.name>dubbo</protocol.name>
				<protocol.port>20880</protocol.port>
				<scan.basepackage>cn.wolfcode.dubbo.service</scan.basepackage>
			</properties>
		</profile>
	</profiles>
```

```
#dubbo 服务名
#spring.dubbo.application.name=@application.name@
#注册中心地址（N/A表示为不启用）
#spring.dubbo.registry.address=@registry.address@
#rpc 协议实现使用 dubbo 协议
#spring.dubbo.protocol.name=@protocol.name@
# 服务暴露端口
#spring.dubbo.protocol.port=@protocol.port@
#基础包扫描
#spring.dubbo.base-package=@scan.basepackage@
```



# 五 、添加https支持

## 一 、 证书生成

java自带的命令：

```java
keytool -genkey -alias tomcat  -storetype PKCS12 -keyalg RSA -keysize 2048  -keystore keystore.p12 -validity 3650
```

当前用户目录下会生成一个keystore.p12文件，将这个文件拷贝到我们项目的根目录下，然后修改application.properties文件，添加HTTPS支持。在application.properties中添加如下代码

```
server.ssl.key-store=keystore.p12
server.ssl.key-store-password=111111
server.ssl.keyStoreType=PKCS12
server.ssl.keyAlias:tomcat
server.port=8084
```

## 二 、http请求不跳转成https访问

```java
package com.integration.boot.config;

import org.apache.catalina.connector.Connector;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpsSupport {
	
	@Bean
	public EmbeddedServletContainerFactory servletContainerFactory(){
	 
	TomcatEmbeddedServletContainerFactory tomcatConfig = new TomcatEmbeddedServletContainerFactory();
	 
	tomcatConfig.addAdditionalTomcatConnectors(this.newHttpConnector());
	return tomcatConfig;
	}
	 
	private Connector newHttpConnector() {
	Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
	connector.setScheme("http");
	connector.setPort(8083);
	connector.setSecure(false);
	return connector;
	 
	}
}
```

注意：connector.setPort(8083);端口和配置文件的server.port不能一样。其实server.port不用配置

此时访问：

http://localhost:8083/getUserInfo    可以访问

https://localhost:8080/getUserInfo  可以访问

http://localhost:8080/getUserInfo  不可以访问

## 三 、将http请求强制跳转到https

有时候我们的一些旧业务是使用的http，但是新业务以及将来的框架都必须强制使用https，那就需要做一下跳转，把收到的http请求强制跳转到https上面。

```
server.port=8443
```

```java
package com.integration.boot.config;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.deploy.SecurityCollection;
import org.apache.catalina.deploy.SecurityConstraint;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TomcatConfig {

	@Bean
	public EmbeddedServletContainerFactory servletContainerFactory(){
	 
	TomcatEmbeddedServletContainerFactory tomcatConfig = new TomcatEmbeddedServletContainerFactory(){
	 
	@Override
	 
	protected void postProcessContext(Context context) {
	 
	SecurityConstraint securityConstraint = new SecurityConstraint();
	 
	securityConstraint.setUserConstraint("CONFIDENTIAL");
	 
	SecurityCollection collection = new SecurityCollection();
	collection.addPattern("/*");
	//另外还可以配置哪些请求必须走https，这表示以/home/开头的请求必须走https
	collection.addPattern("/home/*");
	 
	securityConstraint.addCollection(collection);
	 
	context.addConstraint(securityConstraint);
	}
	};
	 
	tomcatConfig.addAdditionalTomcatConnectors(this.newHttpConnector());
	return tomcatConfig;
	}
	 
	private Connector newHttpConnector() {
	 
	Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
	connector.setScheme("http");
	connector.setPort(8081);
	connector.setSecure(false);
	//如果只需要支持https访问，这里把收到的http请求跳转到https的端口
	connector.setRedirectPort(8443);
	return connector;
	}
}
```



此时访问 

http://localhost:8081/getUserInfo    会跳转到 https://localhost:8443/getUserInfo



# 六 、单元测试

引入依赖

```xml
<dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
</dependency>
```

测试代码

```java
package springBoot;

import javax.ws.rs.core.MediaType;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.integration.boot.Application;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes=Application.class)
public class UserConrollerTest {
	@Test
	public void contextLoads() {
		
	}
	private MockMvc mockMvc;
	  
    @Autowired  
    private WebApplicationContext wac;

    @Before
    public void setup() {  
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();  
    }  

    @Test  
    public void getUserInfoTest() throws Exception {  
    	  String uri = "/getUserInfo";
          MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri).param("username", "123").content("").contentType(MediaType.APPLICATION_FORM_URLENCODED)
        		  .accept(MediaType.APPLICATION_JSON))
                  .andReturn();
          String content = mvcResult.getResponse().getContentAsString();
          System.out.println(content);
    }  
}
```

指定加载的环境配置

```java
package com.boot.util;

import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes=Application.class)
/**单元测试手动制定需要加载的环境**/
@ActiveProfiles("prod")
public class UserTest {
	
}
```



# 七  、开发环境热部署

目前发现问题，引入后dubbo消费报错。

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-devtools</artifactId>
        <optional>true</optional>
    </dependency>
</dependencies>
```

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
            <configuration>
                <fork>true</fork>
            </configuration>
        </plugin>
</plugins>
</build>
```



# 八 、Api签名

## 方案一

调用方和服务端约定secretKey=2Rv3grt8gP2QHcivuJy9tQ4mZCBGKt

调用方：

对参数按照规则排序，如a=b&c=d&e=f,加上约定参数key=secretKey。

调用方最终参数形式如：sign=md5(a=b&c=d&e=f&key=secretKey).toUpperCase()

```json
{
    'a':'b',
    'c':'d',
    'e':'f',
    'sign':sign
}
```

服务端：

sign=md5(a=b&c=d&e=f&key=secretKey).toUpperCase()，获取的结果和客户端参数sign对比，一致则通过。

```java
private String signParameters(TreeMap<String, String> params)
			throws NoSuchAlgorithmException, UnsupportedEncodingException {

		String sign = params.entrySet().stream().map(e -> e.getKey() + '=' + e.getValue())
				.collect(Collectors.joining("&"));
		LOGGER.info("sign before without key {{}}", sign);
		sign += "&key=" + secretKey;

		return HashUtil.md5(sign).toUpperCase();
}
```

## 方案二



## 方案三

JWT



# 九、 RSA加解密

```java
/**
 * 
 */
package com.boot.util;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class RSAUtil {

	public static final String KEY_ALGORITHM = "RSA";
	/** 貌似默认是RSA/NONE/PKCS1Padding，未验证 */
	public static final String CIPHER_ALGORITHM = "RSA";
	public static final String PUBLIC_KEY = "publicKey";
	public static final String PRIVATE_KEY = "privateKey";

	static String publicKeyStr = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCHM9qA9gJoKc7XgXeaO3qrio3fP2KvNeOeLl4uivL7I7yrhkoQ3Y9hxXcP+x7asA6+Cztu6hle2z5DyAG2usMIsilzpOpiW9DaSeA/sdgPR9RaJBGxCk9tt3Jq9qh90kT5x+z8rQh1LcLeY6Wk5RIhAMOw4PaDeD21ucvBQ7D8+QIDAQAB";
	static String privateKeyStr = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIcz2oD2AmgpzteBd5o7equKjd8/Yq81454uXi6K8vsjvKuGShDdj2HFdw/7HtqwDr4LO27qGV7bPkPIAba6wwiyKXOk6mJb0NpJ4D+x2A9H1FokEbEKT223cmr2qH3SRPnH7PytCHUtwt5jpaTlEiEAw7Dg9oN4PbW5y8FDsPz5AgMBAAECgYB1sRy+7+eudt5YWJodhzNEikrvkES+UoG+m4xepZPYLAa7pR1qSwPaT0NShP4ZzfI3Wp208lF9cgpkhIGBaFgHQLhuS+/JIFXdKuJQdsl0lUkwkJZDdX+D97PoWJ11QU/BRaI8bGia/EAULh49VJ5HTx+RS/oEkmwFLY6RPW6PxQJBAMERTooyxdOjwIMaZKMUhlHmZyurxZO4Qyz/ZW3XZwV+qom7nLy3N/29Bm6JUxH4wHF16BRjz9cSGZi+DuypqCsCQQCzRfKMSag5uTiXpPPmOITjJXjVhMCWZaepQ784MEVxUm33WluPh3Qi1rFeo2Mw/XmcaFNFvn3Qpy4udDUA0plrAkEAptcH4hFCN0QZBrJ2KR+Be6D5oxmLYb4n/uithCBSnML7KI8CQWqrbhA9UKRHLw6hbusPJR+j6h1wFTiYuRdbhQJAerANDSs/gLmc3FMcQ9s8PoOfjWi7sHHDa1ic3eRTMfm6nkRtUu3dchZB7sWclaNy/bJ8AsgaLJitR0dqGrUGHwJAFvRtzbIkChbDSYFt5SgF8FDP93ec3oKkW3O2JfXRuPYWQI7FhqszxXaBdyDo5J8FkbF894l0oqLEfPDxMbgH2w==";

	/** RSA密钥长度必须是64的倍数，在512~65536之间。默认是1024 */
	public static final int KEY_SIZE = 1024;

	public static final String PLAIN_TEXT = "abc123!";

	public static void main(String[] args) {

		String password = "123456";

		PublicKey publicKey = restorePublicKey(Base64.getDecoder().decode(publicKeyStr.getBytes()));
		// 加密
		byte[] encodedText = RSAEncode(publicKey, password.getBytes());
		System.out.println("RSA encoded: " + Base64.getEncoder().encodeToString(encodedText));

		// 解密
		PrivateKey privateKey = restorePrivateKey(Base64.getDecoder().decode(privateKeyStr));
		// 密文
		String test1 = "S0PnJL8Ac+StEAir3mr6z0I9wGzQrqcQglPMXe5XExODaCfXsWCdiq4Ve+lv9QaAtOpzHtDWA44S5fSD87iAjcGK8PxMpDNjYGv8OE2fphYx+8UlM2ExPHMDbMNTRpmaMRUXS3byI8kk6Ct/cJ/zjjAQ8+rR5EwYGMEjOZV8ghs=";
		System.out.println("RSA decoded: " + RSADecode(privateKey, Base64.getDecoder().decode(test1)));
		System.out.println("RSA decoded: " + RSADecode(privateKey, encodedText));
	}

	public static PrivateKey getPrivateKey() {
		return restorePrivateKey(Base64.getDecoder().decode(privateKeyStr));
	}

	/**
	 * 生成密钥对。注意这里是生成密钥对KeyPair，再由密钥对获取公私钥
	 * @return
	 */
	public static Map<String, byte[]> generateKeyBytes() {

		try {
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
			keyPairGenerator.initialize(KEY_SIZE);
			KeyPair keyPair = keyPairGenerator.generateKeyPair();
			RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
			RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

			Map<String, byte[]> keyMap = new HashMap<String, byte[]>();

			System.out.println(Base64.getEncoder().encodeToString(publicKey.getEncoded()));
			System.out.println(Base64.getEncoder().encodeToString(privateKey.getEncoded()));

			keyMap.put(PUBLIC_KEY, publicKey.getEncoded());
			keyMap.put(PRIVATE_KEY, privateKey.getEncoded());
			return keyMap;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 还原公钥，X509EncodedKeySpec 用于构建公钥的规范
	 * @param keyBytes
	 * @return
	 */
	public static PublicKey restorePublicKey(byte[] keyBytes) {
		X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);

		try {
			KeyFactory factory = KeyFactory.getInstance(KEY_ALGORITHM);
			PublicKey publicKey = factory.generatePublic(x509EncodedKeySpec);
			return publicKey;
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 还原私钥，PKCS8EncodedKeySpec 用于构建私钥的规范
	 * @param keyBytes
	 * @return
	 */
	public static PrivateKey restorePrivateKey(byte[] keyBytes) {
		PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
		try {
			KeyFactory factory = KeyFactory.getInstance(KEY_ALGORITHM);
			PrivateKey privateKey = factory.generatePrivate(pkcs8EncodedKeySpec);
			return privateKey;
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param key
	 * @param plainText
	 * @return
	 */
	public static byte[] RSAEncode(PublicKey key, byte[] plainText) {

		try {
			Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, key);
			return cipher.doFinal(plainText);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException
				| BadPaddingException e) {
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * @param key
	 * @param encodedText
	 * @return
	 */
	public static String RSADecode(PrivateKey key, byte[] encodedText) {

		try {
			Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, key);
			return new String(cipher.doFinal(encodedText));
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException
				| BadPaddingException e) {
			e.printStackTrace();
		}
		return null;

	}
}
```





# 十 、属性文件加密

## 一、普通java项目

```xml
<dependency>
    <groupId>org.jasypt</groupId>
    <artifactId>jasypt</artifactId>
    <version>1.9.0</version>
</dependency>
```



```java
package com.boot.util;
import org.jasypt.util.text.BasicTextEncryptor;
import org.junit.Test;

public class EncypterTest {
	
	private String password = "password";
	
	 /**
	  * 解密
	  * **/
	 @org.junit.Test
	 public void decrypt() {
		 BasicTextEncryptor textEncryptor = new BasicTextEncryptor();  
		 textEncryptor.setPassword(password);  
	     String oldPassword = textEncryptor.decrypt("A/muZ2yRS9wQoph5gSvOlw==");  
	     System.out.println(oldPassword);  
		 
	 }
	 
	 /**
	  * 加密
	  * **/
	 @Test
	 public void encrypt() {
		  BasicTextEncryptor textEncryptor = new BasicTextEncryptor();  
	      textEncryptor.setPassword(password);  
	      String newPassword = textEncryptor.encrypt("123456");  
	      System.out.println(newPassword);  
	 }
}
```



## 二、springBoot项目

```xml
<dependency>
    <groupId>com.github.ulisesbocchio</groupId>
    <artifactId>jasypt-spring-boot-starter</artifactId>
    <version>1.5-java7</version>
</dependency>
```

配置文件中：

```properties
spring.datasource.password=ENC(zjdITWU2MWpHaOdcF8nG+w==) //需要解密的配置使用ENC()将其包围
jasypt.encryptor.password=f6car  //加解密所需要的密钥 
```

如何获得密文？

用方式一或者如下方式

```java
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class JasyptTest {
    @Autowired
    private StringEncryptor stringEncryptor;
 
    @Test
    public void testEnvironmentProperties() {
        System.out.println(stringEncryptor.encrypt("123456"));
    }
}
```

使用时@value注入或者其他方式获取`spring.datasource.password`，拿到的直接就是明文。

# 十一  、DES加密

```java
package com.boot.util;

import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;  
   
public class DESUtils {  
   
    private static Key key;  
    private static String KEY_STR = "myKey";
    private static String CHARSETNAME = "UTF-8";
    private static String ALGORITHM = "DES";
   
    static {  
        try {  
            KeyGenerator generator = KeyGenerator.getInstance(ALGORITHM);  
            generator.init(new SecureRandom(KEY_STR.getBytes()));  
            key = generator.generateKey();  
            generator = null;  
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
    }  
 
    public static String getEncryptString(String str) {  
        BASE64Encoder base64encoder = new BASE64Encoder();  
        try {  
            byte[] bytes = str.getBytes(CHARSETNAME);  
            Cipher cipher = Cipher.getInstance(ALGORITHM);  
            cipher.init(Cipher.ENCRYPT_MODE, key);  
            byte[] doFinal = cipher.doFinal(bytes);  
            return base64encoder.encode(doFinal);  
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
    }  
    
    public static String getDecryptString(String str) {  
        BASE64Decoder base64decoder = new BASE64Decoder();  
        try {  
            byte[] bytes = base64decoder.decodeBuffer(str);  
            Cipher cipher = Cipher.getInstance(ALGORITHM);  
            cipher.init(Cipher.DECRYPT_MODE, key);  
            byte[] doFinal = cipher.doFinal(bytes);  
            return new String(doFinal, CHARSETNAME);  
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
    }  
   
    public static void main(String[] args) {
    	
    	String str = "root";
    	System.out.println(getEncryptString(str));
    	System.out.println(getDecryptString("WnplV/ietfQ="));
  }
    
}   
```

BASE64Encoder一致报错，eclipse选中项目--->buildpath-->libraries ---> jre system library ---> access rules---> edit ---> add ---> accessble ---> ** ---> ok



# 十二  、JDBC返回对象

```java
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author dalaoyang
 * @project springboot_learn
 * @package com.dalaoyang.entity
 * @email yangyang@dalaoyang.cn
 * @date 2018/7/25
 */
public class User implements RowMapper<User> {
    private int id;
    private String user_name;
    private String pass_word;

    public User(int id, String user_name, String pass_word) {
        this.id = id;
        this.user_name = user_name;
        this.pass_word = pass_word;
    }

    public User() {
    }

    public User(String user_name, String pass_word) {
        this.user_name = user_name;
        this.pass_word = pass_word;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPass_word() {
        return pass_word;
    }

    public void setPass_word(String pass_word) {
        this.pass_word = pass_word;
    }

    @Override
    public User mapRow(ResultSet resultSet, int i) throws SQLException {
        User user = new User();
        user.setId(resultSet.getInt("id"));
        user.setUser_name(resultSet.getString("user_name"));
        user.setPass_word(resultSet.getString("pass_word"));
        return user;
    }
}
```

```java
public User getUserById(Integer id){
        String sql = "SELECT * FROM USER WHERE ID = ?";
        User user= jdbcTemplate.queryForObject(sql,new User(),new Object[]{id});
        return user;
 }
```



# 十三 、整合springSecurity

## 一、 介绍

  Spring Security是一个能够为基于Spring的企业应用系统提供声明式的安全访问控制解决方案的安全框架。它提供了一组可以在Spring应用上下文中配置的Bean，充分利用了Spring IoC，DI（控制反转Inversion of Control ,DI:Dependency Injection 依赖注入）和AOP（面向切面编程）功能，为应用系统提供声明式的安全访问控制功能，减少了为企业系统安全控制编写大量重复代码的工作。

## 二、 环境搭建

  建立springboot2项目,加入security依赖,mybatis依赖

```java
<dependency>
   <groupId>org.springframework.boot</groupId>
   <artifactId>spring-boot-starter-security</artifactId>
</dependency>
<dependency>
   <groupId>org.mybatis.spring.boot</groupId>
   <artifactId>mybatis-spring-boot-starter</artifactId>
   <version>1.3.2</version>
</dependency>
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <scope>runtime</scope>
</dependency>
```

数据库为传统的用户--角色--权限，权限表记录了url和method，springboot配置文件如下：

```java
mybatis:
  type-aliases-package: com.example.demo.entity
server:
  port: 8081
spring:
  datasource:
    driver-class-name: com.mysql.jdbc.Driver
    url: jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf-8&useSSL=true
    username: root
    password: 123456
  http:
    encoding:
      charset: utf-8
      enabled: true
```

springboot启动类中加入如下代码,设置路由匹配规则。

```java
@Override
protected void configurePathMatch(PathMatchConfigurer configurer) {
    configurer.setUseSuffixPatternMatch(false) //设置路由是否后缀匹配，譬如/user能够匹配/user.,/user.aa
        .setUseTrailingSlashMatch(false); //设置是否后缀路径匹配，比如/user能够匹配/user,/user/
}
```

## 三、 security配置

  默认情况下security是无需任何自定义配置就可使用的，我们不考虑这种方式，直接讲如何个性化登录过程。

#### 1、 建立security配置文件,目前配置文件中还没有任何配置。

```java
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
}
```

#### 2、 个性化登录，security中的登录如下：

![img](data:image/jpeg;base64,/9j/4QAYRXhpZgAASUkqAAgAAAAAAAAAAAAAAP/sABFEdWNreQABAAQAAAAZAAD/4QOTaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wLwA8P3hwYWNrZXQgYmVnaW49Iu+7vyIgaWQ9Ilc1TTBNcENlaGlIenJlU3pOVGN6a2M5ZCI/PiA8eDp4bXBtZXRhIHhtbG5zOng9ImFkb2JlOm5zOm1ldGEvIiB4OnhtcHRrPSJBZG9iZSBYTVAgQ29yZSA1LjMtYzAxMSA2Ni4xNDU2NjEsIDIwMTIvMDIvMDYtMTQ6NTY6MjcgICAgICAgICI+IDxyZGY6UkRGIHhtbG5zOnJkZj0iaHR0cDovL3d3dy53My5vcmcvMTk5OS8wMi8yMi1yZGYtc3ludGF4LW5zIyI+IDxyZGY6RGVzY3JpcHRpb24gcmRmOmFib3V0PSIiIHhtbG5zOnhtcE1NPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvbW0vIiB4bWxuczpzdFJlZj0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL3NUeXBlL1Jlc291cmNlUmVmIyIgeG1sbnM6ZGM9Imh0dHA6Ly9wdXJsLm9yZy9kYy9lbGVtZW50cy8xLjEvIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6MTdFRTJGMTQ4RTFFMTFFOEFCNzlFMzVDMTkzNERBNDIiIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6MTdFRTJGMTM4RTFFMTFFOEFCNzlFMzVDMTkzNERBNDIiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENTNiBXaW5kb3dzIj4gPHhtcE1NOkRlcml2ZWRGcm9tIHN0UmVmOmluc3RhbmNlSUQ9InV1aWQ6ZmFmNWJkZDUtYmEzZC0xMWRhLWFkMzEtZDMzZDc1MTgyZjFiIiBzdFJlZjpkb2N1bWVudElEPSI2NDVGMTQxMkU0QjRCRTBBNTU0N0YyN0YzNjBGQUNFMSIvPiA8ZGM6Y3JlYXRvcj4gPHJkZjpTZXE+IDxyZGY6bGk+ZnhiPC9yZGY6bGk+IDwvcmRmOlNlcT4gPC9kYzpjcmVhdG9y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)

- security需要一个user的实体类实现`UserDetails`接口,该实体类最后与系统中用户的实体类分开，代码如下：

```java
public class SecurityUser implements UserDetails{
    private static final long serialVersionUID = 1L;
    private String password;
    private String name;
    List<GrantedAuthority> authorities;
    
    public User(string name,string password) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.age = age;
    }

    public void setAuthorities(List<GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override //获取校验用户名
    public String getUsername() {
        return String.valueOf(this.id);
    }

    @Override //获取校验用密码
    public String getPassword() {
        return password;
    }

    @Override //账户是否未过期
    public boolean isAccountNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override  //账户是否未锁定
    public boolean isAccountNonLocked() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override  //帐户密码是否未过期，一般有的密码要求性高的系统会使用到，比较每隔一段时间就要求用户重置密码
    public boolean isCredentialsNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override //账户是否可用
    public boolean isEnabled() {
        // TODO Auto-generated method stub
        return true;
    }
}
```

- 编写了实体类还需要编写一个服务类SecurityService实现`UserDetailsService`接口，重写loadByUsername方法，通过这个方法根据用户名获取用户信息，代码如下：

```java
@Component
public class SecurityUserService implements UserDetailsService {
    @Autowired
    private JurisdictionMapper jurisdictionMapper;
    @Autowired
    private UserMapper userMapper;
    private Logger log = LoggerFactory.getLogger(this.getClass());


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("登录用户id为：{}",username);
        int id = Integer.valueOf(username);
        User user = userMapper.getById(id);
        if(user==null) {
            //抛出错误，用户不存在
            throw new UsernameNotFoundException("用户名 "+username+"不存在");
        }
        //获取用户权限
        List<GrantedAuthority> authorities = new ArrayList<>();
        List<Jurisdiction> jurisdictions = jurisdictionMapper.selectByUserId(id);
        for(Jurisdiction item : jurisdictions) {
            GrantedAuthority authority = new MyGrantedAuthority(item.getMethod(),item.getUrl());
            authorities.add(authority);
        }
        SecurityUser securityUser = new SecurityUser(user.getName(),user.getPassword(),authority):
        user.setAuthorities(authorities);
        return securityUser;
    }
}
```

- 通常我们会对密码进行加密，所有还要编写一个passwordencode类，实现PasswordEncoder接口，代码如下：

```java
@Component
public class MyPasswordEncoder implements PasswordEncoder {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Override //不清楚除了在下面方法用到还有什么用处
    public String encode(CharSequence rawPassword) {
        return StringUtil.StringToMD5(rawPassword.toString());
    }

    //判断密码是否匹配
    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encodedPassword.equals(this.encode(rawPassword));
    }
}
```

#### 3、 编辑配置文件

- 编写config Bean以使用上面定义的验证逻辑,securityUserService、myPasswordEncoder通过@Autowired引入。

```java
@Override
protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(securityUserService)
        .passwordEncoder(myPasswordEncoder);
}
```

- 然后编写configure Bean（和上一个不一样，参数不同），实现security验证逻辑,代码如下：

```java
@Override
    protected void configure(HttpSecurity http) throws Exception {
        http
        .csrf() //跨站
        .disable() //关闭跨站检测
        .authorizeRequests()//验证策略策略链
            .antMatchers("/public/**").permitAll()//无需验证路径
           .antMatchers("/login").permitAll()//放行登录
            .antMatchers(HttpMethod.GET, "/user").hasAuthority("getAllUser")//拥有权限才可访问
            .antMatchers(HttpMethod.GET, "/user").hasAnyAuthority("1","2")//拥有任一权限即可访问
            //角色类似，hasRole(),hasAnyRole()
            .anyRequest().authenticated()
        .and()
        .formLogin()
            .loginPage("/public/unlogin") //未登录跳转页面,设置了authenticationentrypoint后无需设置未登录跳转页面
            .loginProcessingUrl("/public/login")//处理登录post请求接口，无需自己实现
            .successForwardUrl("/success")//登录成功转发接口
            .failureForwardUrl("/failed")//登录失败转发接口
            .usernameParameter("id") //修改用户名的表单name，默认为username
            .passwordParameter("password")//修改密码的表单name，默认为password
        .and()
        .logout()//自定义登出
            .logoutUrl("/public/logout") //自定义登出api，无需自己实现
            .logoutSuccessUrl("public/logoutSuccess")
    }
```

到这里便可实现security与springboot的基本整合。

## 四、实现记住我功能

#### 1、 建表

  记住我功能需要数据库配合实现，首先要在数据库建一张表用户保存cookie和用户名，数据库建表语句如下：不能做修改

```java
CREATE TABLE `persistent_logins` (
  `username` varchar(64) NOT NULL,
  `series` varchar(64) NOT NULL,
  `token` varchar(64) NOT NULL,
  `last_used` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`series`)
)
```

#### 2、 编写rememberMeservice Bean

  代码如下：

```java
    @Bean
    public RememberMeServices rememberMeServices(){
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        PersistentTokenBasedRememberMeServices rememberMeServices =
                new PersistentTokenBasedRememberMeServices("INTERNAL_SECRET_KEY",securityUserService,jdbcTokenRepository);
        //还可设置许多其他属性
       rememberMeServices.setCookieName("kkkkk"); //客户端cookie名
        return rememberMeServices;
    }
```

dataSource为@Autowired引入

#### 3、 配置文件设置remember

  在config(HttpSecurity http)中加入记住我功能

```java
.rememberMe()
    .rememberMeServices(rememberMeServices())
    .key("INTERNAL_SECRET_KEY")
```

在登录表单中设置remember-me即可实现记住我功能。





# 十四、 springboot全家桶

每个子项目会配有一篇博客文章的详细讲解 

| 项目名称                | 文章地址                                                     |
| ----------------------- | ------------------------------------------------------------ |
| springboot-thymeleaf    | [集成Thymeleaf构建Web应用](https://www.xncoding.com/2017/07/01/spring/sb-thymeleaf.html) |
| springboot-mybatis      | [集成MyBatis](https://www.xncoding.com/2017/07/02/spring/sb-mybatis.html) |
| springboot-hibernate    | [集成Hibernate](https://www.xncoding.com/2017/07/03/spring/sb-hibernate.html) |
| springboot-mongodb      | [集成MongoDB](https://www.xncoding.com/2017/07/04/spring/sb-mongodb.html) |
| springboot-restful      | [实现RESTful接口](https://www.xncoding.com/2017/07/05/spring/sb-restful.html) |
| springboot-resttemplate | [使用RestTemplate](https://www.xncoding.com/2017/07/06/spring/sb-restclient.html) |
| springboot-shiro        | [集成Shiro权限管理](https://www.xncoding.com/2017/07/07/spring/sb-shiro.html) |
| springboot-swagger2     | [集成Swagger2自动生成API文档](https://www.xncoding.com/2017/07/08/spring/sb-swagger2.html) |
| springboot-jwt          | [集成JWT实现接口权限认证](https://www.xncoding.com/2017/07/09/spring/sb-jwt.html) |
| springboot-multisource  | [多数据源配置](https://www.xncoding.com/2017/07/10/spring/sb-multisource.html) |
| springboot-schedule     | [定时任务](https://www.xncoding.com/2017/07/12/spring/sb-schedule.html) |
| springboot-websocket    | [使用WebScoket实时通信](https://www.xncoding.com/2017/07/15/spring/sb-websocket.html) |
| springboot-socketio     | [集成SocketIO实时通信](https://www.xncoding.com/2017/07/16/spring/sb-socketio.html) |
| springboot-async        | [异步线程池](https://www.xncoding.com/2017/07/20/spring/sb-async.html) |
| springboot-starter      | [教你自己写starter](https://www.xncoding.com/2017/07/22/spring/sb-starter.html) |
| springboot-aop          | [使用AOP](https://www.xncoding.com/2017/07/24/spring/sb-aop.html) |
| springboot-transaction  | [声明式事务](https://www.xncoding.com/2017/07/26/spring/sb-transaction.html) |
| springboot-cache        | [使用缓存](https://www.xncoding.com/2017/07/28/spring/sb-cache.html) |
| springboot-redis        | [Redis数据库](https://www.xncoding.com/2017/07/30/spring/sb-redis.html) |
| springboot-batch        | [批处理](https://www.xncoding.com/2017/08/01/spring/sb-batch.html) |
| springboot-rabbitmq     | [使用消息队列RabbitMQ](https://www.xncoding.com/2017/08/06/spring/sb-rabbitmq.html) |
| springboot-echarts      | [集成Echarts导出图片](https://www.xncoding.com/2017/08/19/spring/sb-echarts.html) |

GitHub地址：<https://github.com/yidao620c/SpringBootBucket>

码云地址：<https://gitee.com/yidao620/springboot-bucket>



# 十五 、springboot整合docker

参考git目录下Spring Cloud与Docker微服务架构实战.pdf。



# 十六、异步

启动类上增加：@EnableAsync，增加bean：getAsyncExecutor

```java
@SpringBootApplication
@EnableAsync
public class Application{

    private static final Log logger = LogFactory.getLog(Application.class);
    
    @Bean
    public CountDownLatch closeLatch() {
        return new CountDownLatch(1);
    }
    
    public static void main(String[] args) throws InterruptedException {

    	  ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
          logger.info("======dubbo-consumer started successfull ======");
          CountDownLatch closeLatch = context.getBean(CountDownLatch.class);
          closeLatch.await();
    }
    
    @Bean
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(3);
        executor.setMaxPoolSize(3);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("GithubLookup-");
        executor.initialize();
        return executor;
    }
}
```

声明异步方法

```java
@Service
public class GitHubLookupService {

	private static final Logger logger = LoggerFactory.getLogger(GitHubLookupService.class);

    private final RestTemplate restTemplate;

    public GitHubLookupService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

	@Async
    public Future<Map<String,Object>> findUser(String user) throws InterruptedException {
		Thread.sleep(3000);
		logger.info("Looking up " + user);
		System.out.println(Thread.currentThread().getName());
		Map<String,Object> forObject = new HashMap<>();
		forObject.put("name", user);
        return new AsyncResult<>(forObject);
    }
}
```

调用上面方法，下面的方法和上面的异步方法最好不在一个类中。

```java
@Service
public class CallGitHubLookupService {

  @Autowired
  private GitHubLookupService gitHubLookupService;
  
  private static final Logger logger = LoggerFactory.getLogger(CallGitHubLookupService.class);
    
    public void getResult() throws InterruptedException, ExecutionException {
    	 Future<Map<String,Object>> page1 = gitHubLookupService.findUser("PivotalSoftware");
         Future<Map<String,Object>> page2 = gitHubLookupService.findUser("CloudFoundry");
         Future<Map<String,Object>> page3 = gitHubLookupService.findUser("Spring-Projects");

         while (!(page1.isDone() && page2.isDone() && page3.isDone())) {
             Thread.sleep(10);
         }
         logger.info("--> " + page1.get());//阻塞
         logger.info("--> " + page2.get());//阻塞
         logger.info("--> " + page3.get());//阻塞
    }
}
```

测试

```java
 @Test
    public void getResult() throws InterruptedException, ExecutionException {
    	callGitHubLookupService.getResult();
    }
```



# 十七、定时任务

第一步：启动类上加  `@EnableScheduling`

```java
@SpringBootApplication
@EnableScheduling
public class SpringbootSchedulingTasksApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootSchedulingTasksApplication.class, args);
    }
}
```

第二步：创建一个定时任务，每过5s在控制台打印当前时间

```java
@Component
public class ScheduledTasks {

  private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Scheduled(fixedRate = 5000)
    public void reportCurrentTime() {
        System.out.println("The time is now "+ dateFormat.format(new Date()));
    }
}
```

通过在方法上加@Scheduled注解，表明该方法是一个调度任务。

- @Scheduled(fixedRate = 5000) ：上一次开始执行时间点之后5秒再执行
- @Scheduled(fixedDelay = 5000) ：上一次执行完毕时间点之后5秒再执行
- @Scheduled(initialDelay=1000, fixedRate=5000) ：第一次延迟1秒后执行，之后按fixedRate的规则每5秒执行一次
- @Scheduled(cron=” /5 “) ：通过cron表达式定义规则，什么是cro表达式，自行搜索引擎。



# 十八、集成ApiDoc

https://blog.csdn.net/forezp/article/details/71023579

# 十九 、集成swagger2

https://blog.csdn.net/forezp/article/details/71023536

# 二十 、spring Restdocs创建API文档

https://blog.csdn.net/forezp/article/details/71023510



# 二十一、启动时执行程序

我们可以通过实现ApplicationRunner和CommandLineRunner，来实现，他们都是在SpringApplication 执行之后开始执行的 .

## 方式一

```java
import org.springframework.boot.CommandLineRunner;  
import org.springframework.core.annotation.Order;  
import org.springframework.stereotype.Component; 

@Component  
@Order(value=1)
public class MyStartupRunnerTest implements CommandLineRunner  
{  
    @Override  
    public void run(String... args) throws Exception  
    {  
        System.out.println(">>>>This is MyStartupRunnerTest Order=1. Only testing CommandLineRunner...<<<<");  
    }  
}
```

```java
import org.springframework.core.Ordered;

@Component  
public class MyStartupRunnerTest2 implements ApplicationRunner,Ordered  
{  

  @Autowired  
  private SampleService sampleService;  

    @Override
    public int getOrder(){
        return 1;//通过设置这里的数字来知道指定顺序
    }

   @Override
    public void run(ApplicationArguments var1) throws Exception{
        System.out.println("MyApplicationRunner class will be execute when the project was started!");
    }
}  
```

CommandLineRunner接口的运行顺序是依据@Order注解的value由小到大执行，即value值越小优先级越高。 

## 方式二

有时候我们需要在spring boot容器启动并加载完后，开一些线程或者一些程序来干某些事情。这时候我们需要配置ContextRefreshedEvent事件来实现我们要做的事情 

```java
@Component
public class ApplicationStartup implements ApplicationListener<ContextRefreshedEvent>{
    public void onApplicationEvent(ContextRefreshedEvent event)
      {
        //在容器加载完毕后获取dao层来操作数据库
        OSSVideoRepository ossVideoRepository = (OSSVideoRepository)event.getApplicationContext().getBean(OSSVideoRepository.class);
        //在容器加载完毕后获取配置文件中的配置
        ServerConfig serverConfig = (ServerConfig)event.getApplicationContext().getBean(ServerConfig.class);
        
        ServerFileScanner fileScanner = new ServerFileScanner(
                ossVideoRepository, serverConfig.getScanpath());
        //在容器加载完毕后启动线程
        Thread thread = new Thread(fileScanner);
        thread.start();
      }
}
```



```java
@Component
@ConfigurationProperties(prefix = "server")
public class ServerConfig {
    private String aliyunossEndpoint;
    private String aliyunossAccessKeyId;
    private String aliyunossAccessKeySecret;
    private String aliyunossBucketName;
    private String scanpath;

   ......getter  setter..........
}
```

# 二十二、JWT 实现 RESTful Api 权限控制

https://blog.csdn.net/sxdtzhaoxinguo/article/details/77965226

http://www.leftso.com/blog/384.html



# 二十三、spring boot 导入本地jar包

导入本地包

```xml
<!--本地包-->
<dependency>
    <groupId>leftso</groupId>
    <artifactId>common</artifactId>
    <version>1.0</version>
    <scope>system</scope>
    <systemPath>${project.basedir}/lib/Common.jar</systemPath>
</dependency>
```

打包的时候讲本地jar包打入war中还需要添加一个war的打包插件，配置如下： 

```xml
 <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-war-plugin</artifactId>
                <configuration>
                    <webResources>
                        <resource>
                            <directory>${project.basedir}/lib</directory>
                            <targetPath>WEB-INF/lib/</targetPath>
                            <includes>
                                <include>**/*.jar</include>
                            </includes>
                        </resource>
                    </webResources>
                </configuration>
 </plugin>
```



# 二十四、Jersey2.x实现JAX-RS

```xml
<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-jersey</artifactId>
</dependency>
```

```java
public class JerseyResourceConfig extends ResourceConfig {
	public JerseyResourceConfig() {
		register(RequestContextFilter.class);
		// 配置那个包下面的会被Jersey扫描
		packages("com.leftso.rest");
	}
}
```

```java
@Configuration
public class JerseyConfig {

	@Bean
	public ServletRegistrationBean jerseyServlet() {
		ServletRegistrationBean registration = new ServletRegistrationBean(new ServletContainer(), "/rest/*");
		// our rest resources will be available in the path /rest/*
		registration.addInitParameter(ServletProperties.JAXRS_APPLICATION_CLASS, JerseyResourceConfig.class.getName());
		return registration;
	}
}
```



```java
@Path("/")
public class RestResource {

	@Path("/hello") // 具体路径
	@GET // 请求方式
	@Produces(MediaType.APPLICATION_JSON) // 返回的格式
	// @Consumes()//接受指定的MIME格式
	public Map<String, Object> hello() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code", "1");
		map.put("codeMsg", "success");
		return map;
	}
}
```



# 二十五、spring boot RPC 框架 Hessian

## 服务端

```xml
<dependency>
			<groupId>com.caucho</groupId>
			<artifactId>hessian</artifactId>
			<version>4.0.51</version>
</dependency>
```



```java
package net.xqlee.project.service;

public interface HelloWorldService {
	String sayHello(String name);
}
```



```java
package net.xqlee.project.service;

import org.springframework.stereotype.Service;

@Service("HelloWorldService")
public class HelloWorldServiceServiceImp implements HelloWorldService {

	@Override
	public String sayHello(String name) {
		return "Hello, " + name;
	}
}
```



```java
@SpringBootApplication
public class DemoSpringbootRpcHessianApplication {
	@Autowired
	private HelloWorldService helloWorldService;

	public static void main(String[] args) {
		SpringApplication.run(DemoSpringbootRpcHessianApplication.class, args);
	}

	// 发布服务
	@Bean(name = "/HelloWorldService")
	public HessianServiceExporter accountService() {
		HessianServiceExporter exporter = new HessianServiceExporter();
		exporter.setService(helloWorldService);
		exporter.setServiceInterface(HelloWorldService.class);
		return exporter;
	}
}
```



## 客户端

```xml
<dependency>
			<groupId>com.caucho</groupId>
			<artifactId>hessian</artifactId>
			<version>4.0.51</version>
		</dependency>
		<!-- 本地引入hessian服务端的接口 -->
		<dependency>
			<groupId>net.xqlee.project</groupId>
			<artifactId>rpc-hessian-demo</artifactId>
			<version>1.0</version>
			<scope>system</scope>
			<systemPath>${project.basedir}/lib/rpc-hessian-demo-interface.jar</systemPath>
</dependency>
```

```java
@SpringBootApplication
public class DemoSpringbootRpcHessianClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoSpringbootRpcHessianClientApplication.class, args);
	}

	@Bean
	public HessianProxyFactoryBean helloClient() {
		HessianProxyFactoryBean factory = new HessianProxyFactoryBean();
		factory.setServiceUrl("http://localhost:8083/HelloWorldService");
		factory.setServiceInterface(HelloWorldService.class);
		return factory;
	}
```



```java
@RestController
public class DemoHessianController {

	@Autowired
	HelloWorldService helloWorldService;

	@GetMapping("/rpchello.do")
	public Object rpcSayHello(String name) {
		return helloWorldService.sayHello(name);
	}
}
```



# 二十六、easypoi导入excel文件

http://www.leftso.com/blog/326.html

```
<dependency>
	<groupId>cn.afterturn</groupId>
	<artifactId>easypoi-web</artifactId>
	<version>3.0.3</version>
</dependency>
```



# 二十七、 spring boot项目中使用logback日志



# 二十八 、过滤器和拦截器

### 过滤器filter

```java
package com.integration.boot.config;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SessionFilter implements Filter {

	private String[] excludedUris;

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain filterChain) throws IOException, ServletException {
		
		HttpServletRequest request = (HttpServletRequest) arg0;
		HttpServletResponse response = (HttpServletResponse) arg1;
		System.out.println("requestUtl:" + request.getRequestURI());
		String uri = request.getServletPath();
		if (isExcludedUri(uri)) {
			filterChain.doFilter(request, response);
		} else if (request.getSession().getAttribute("user") != null) {
			filterChain.doFilter(request, response);
		}else {
			if (isAjaxRequest(request)) {
				 response.setContentType("application/json; charset=utf-8");  
				 Map<String,Object> resultMap = new HashMap<>();
				 resultMap.put("success", false);
				 PrintWriter out = response.getWriter();
	             out.print(new ObjectMapper().writeValueAsString(resultMap));
	             out.flush();
	             out.close();
			}else {
				  filterChain.doFilter(request, response);
				//response.sendRedirect(request.getContextPath() + "/login/toLogin");
			}
		}
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		if (StringUtils.isNotBlank(filterConfig.getInitParameter("excludedUri"))) {
			excludedUris = filterConfig.getInitParameter("excludedUri").split(",");
		}
	}

	private boolean isExcludedUri(String uri) {
		if (excludedUris == null || excludedUris.length <= 0) {
			return false;
		}
		for (String ex : excludedUris) {
			uri = uri.trim();
			ex = ex.trim();
			if (uri.toLowerCase().matches(ex.toLowerCase().replace("*", ".*")))
				return true;
		}
		return false;
	}
	
	public boolean isAjaxRequest(HttpServletRequest request) {
		String requestType = request.getHeader("X-Requested-With");
		return "XMLHttpRequest".equalsIgnoreCase(requestType);
	}
}

```

注册

```java
package com.integration.boot.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SessionFilterConfig {
	
	   @Bean
	    public FilterRegistrationBean testFilterRegistration() {
	        StringBuffer excludedUriStr = new StringBuffer();
	        excludedUriStr.append("/login/*");
	        FilterRegistrationBean registration = new FilterRegistrationBean();
	        registration.setFilter(new SessionFilter());
	        registration.addUrlPatterns("/*");
	        registration.addInitParameter("excludedUri", excludedUriStr.toString());
	        registration.setName("sessionFilter");
	        registration.setOrder(1);
	        return registration;
	    }
}
```



### 拦截器Interceptor

```java
@Component
public class MyInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
    System.out.println("hello preHandler......");
    final HandlerMethod handlerMethod = (HandlerMethod) handler;
    final Method method = handlerMethod.getMethod();
    final Class<?> clazz = method.getDeclaringClass();
        if (clazz.isAnnotationPresent(Auth.class) ||
                method.isAnnotationPresent(Auth.class)) {
          if(request.getAttribute(USER_CODE_SESSION_KEY) == null)   {
                 throw new Exception();
            }else{
                return true;
            }
        }
 }
 
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        System.out.println("hello postHandler......");
    }
 
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        System.out.println("hello afterCompletionHandler......");
    }
}
```

```java
@SpringBootConfiguration
public class MySpringMVCConfig extends WebMvcConfigurerAdapter {
    @Autowired
    private MyInterceptor myInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(myInterceptor).addPathPatterns("/**");
    }
}
```



# 二十九、静态资源映射

```java
1. classpath:/META-INF/resources/index.html
2. classpath:/resources/index.html
3. classpath:/static/index.html
4. classpath:/public/index.html
当我们访问应用根目录http://localhost:8080/时，会直接映射。
```



# 三十、spring-boot项目部署到外部tomcat环境

想要把`spring-boot`项目按照平常的`web`项目一样发布到`tomcat`容器下需要进行下列几个步骤:

## 一、修改打包形式

在`pom.xml`里设置

```
<packaging>war</packaging>
```

## 二、移除嵌入式tomcat插件

在`pom.xml`里找到`spring-boot-starter-web`依赖节点,在其中进行如下修改:

```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
    <!-- 移除嵌入式tomcat插件 -->
    <exclusions>
        <exclusion>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-tomcat</artifactId>
        </exclusion>
    </exclusions>
</dependency>
```

## 三、添加本地调试Tomcat

为了本地调试方便，在`pom.xml`文件中,`dependencies`下面添加

```
<dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-tomcat</artifactId>
        <scope>provided</scope>
</dependency>
```

## 四、修改启动类，并重写初始化方法

我们平常用`main`方法启动的方式，都有一个`Application`的启动类，代码如下：

```
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

我们需要类似于`web.xml`的配置方式来启动spring上下文，在`Application`类的同级添加一个`SpringBootStartApplication`类，其代码如下:

```
/**
 * 修改启动类，继承 SpringBootServletInitializer 并重写 configure 方法
 */
public class SpringBootStartApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        // 注意这里要指向原先用main方法执行的Application启动类
        return builder.sources(Application.class);
    }
}
```

## 五、打包部署

在项目根目录下（即包含`pom.xml`的目录），在命令行里输入：

```
mvn clean package
```

即可， 等待打包完成，出现`[INFO] BUILD SUCCESS`即为打包成功。然后把`target`目录下的war包放到`tomcat`的`webapps`目录下，启动`tomcat`，即可自动解压部署。 最后在浏览器中输入:

```
http://localhost:[端口号]/[打包项目名]/
```

发布成功。

## 六、总结

这样,只需要以上5步就可以打包成`war`包，并且部署到`tomcat`中了。需要注意的是这样部署的`request url`需要在端口后加上项目的名字才能正常访问。`spring-boot`更加强大的一点就是：即便项目是以上配置，依然可以用内嵌的tomcat来调试，启动命令和以前没变，还是：`mvn spring-boot:run`。如果需要在springboot中加上request前缀，需要在`application.properties`中添加`server.contextPath=/prefix/`即可。其中`prefix`为前缀名。这个前缀会在`war`包中失效，取而代之的是`war`包名称，如果`war`包名称和`prefix`相同的话，那么调试环境和正式部署环境就是一个`request`地址了。
**注意点:**
我测试的时候，使用的相关环境版本如下：
`jdk:`

```
jdk1.7.0_71
```

`tomcat:`

```
apache-tomcat-8.5.8
```

`spring boot:`

```
1.4.0.RELEASE
```

最开始我使用的`spring boot`版本是`1.3.0.RELEASE`,部署到`tomcat`中出现找不到类等各种问题,后来把`spring boot`版本升级





# 三十一、文件上传

```jade
package com.integration.boot.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class SingleFileUpload {

	@PostMapping("/upload") 
	public Map<String,Object> singleFileUpload(@RequestParam("file") MultipartFile file,HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<>();
	    if (file.isEmpty()) {
	    	resultMap.put("status","fail");
	        resultMap.put("msg","Please select a file to upload");
	        return resultMap;
	    }
	    try {
	        byte[] bytes = file.getBytes();
	        Path path = Paths.get("C:\\Users\\789\\Desktop\\uploadfile\\" + file.getOriginalFilename());
	        Files.write(path, bytes);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    resultMap.put("status","success");
	    resultMap.put("msg","operation successfully");
	    return resultMap;
	}
}
```

- `spring.http.multipart.enabled=true` #默认支持文件上传.
- `spring.http.multipart.file-size-threshold=0` #支持文件写入磁盘.
- `spring.http.multipart.location=`# 上传文件的临时目录
- `spring.http.multipart.max-file-size=1Mb` # 最大支持文件大小
- `spring.http.multipart.max-request-size=10Mb` # 最大支持请求大小

最常用的是最后两个配置内容，限制文件上传大小，上传时超过大小会抛出异常：

```java
@ControllerAdvice
public class GlobalExceptionHandler {

	@ResponseBody
    @ExceptionHandler(MultipartException.class)
    public Map<String,Object> handleError1(MultipartException e) {
        Map <String,Object> result = new HashMap<>();
        result.put("data", "error");
        return result;
    }
}
```

设置一个`@ControllerAdvice`用来监控`Multipart`上传的文件大小是否受限，当出现此异常时在前端页面给出提示。利用`@ControllerAdvice`可以做很多东西，比如全局的统一异常处理等，感兴趣的同学可以下来了解





# 三十二、打成war包

1、maven项目，修改pom包

将

```xml
<packaging>jar</packaging>  
```

改为

```xml
<packaging>war</packaging>
```

2、打包时排除tomcat.

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-tomcat</artifactId>
    <scope>provided</scope>
</dependency>
```

在这里将scope属性设置为provided，这样在最终形成的WAR中不会包含这个JAR包，因为Tomcat或Jetty等服务器在运行时将会提供相关的API类。

3、注册启动类

创建ServletInitializer.java，继承SpringBootServletInitializer ，覆盖configure()，把启动类Application注册进去。外部web应用服务器构建Web Application Context的时候，会把启动类添加进去。

```java
public class ServletInitializer extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }
}
```

最后执行

```java
mvn clean package  -Dmaven.test.skip=true
```

会在target目录下生成：项目名+版本号.war文件，拷贝到tomcat服务器中启动即可。



# 三十三、Spring boot配置log4j输出日志

## 一 、删除多余的包

下面两个删除

```xml
<dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-logging</artifactId>
</dependency>
<dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>log4j-over-slf4j</artifactId>
</dependency>
```

可以点开[Effective POM]查看，parent中依然有两处对[spring-boot-starter-logging]的依赖

```xml
<dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter</artifactId>
        <exclusions>
            <exclusion>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-logging</artifactId>
            </exclusion>
        </exclusions>
</dependency>
```

## 二、引入依赖

```
<dependency> 
   <groupId>org.springframework.boot</groupId>
   <artifactId>spring-boot-starter-log4j</artifactId>
</dependency>
```

## 三、增加log4j.properties配置  

```properties
#我们把INFO层级以及以上的信息输出到Console和File
#log4j.rootLogger=INFO,File,Console,DailyRollingFile ,RollingFile

#project=dubbo-consumer
#logdir=C://Users//789//Desktop//uploadfile//${project}

logdir=C://Users//789//Desktop//uploadfile//

log4j.rootLogger=INFO,Console,RollingFile
#stdout
log4j.appender.Console=org.apache.log4j.ConsoleAppender
log4j.appender.Console.Target=System.out
log4j.appender.Console.layout=org.apache.log4j.PatternLayout
log4j.appender.Console.layout.ConversionPattern=%-d{M-d HH:mm:ss} %5p[%t]%c{1}:%L: %m%n
#api
#'.'yyyy-MM: 每月
#'.'yyyy-ww: 每周 
#'.'yyyy-MM-dd: 每天
#'.'yyyy-MM-dd-a: 每天两次
#'.'yyyy-MM-dd-HH: 每小时
#'.'yyyy-MM-dd-HH-mm: 每分钟
log4j.appender.File=org.apache.log4j.FileAppender
log4j.appender.File.DatePattern='.'yyyy-MM-dd-HH-mm
log4j.appender.File.File=${logdir}//sys.log
log4j.appender.File.Append = true
log4j.appender.File.layout=org.apache.log4j.PatternLayout
log4j.appender.File.layout.ConversionPattern=%-d{M-d HH:mm:ss} %5p[%t]%c{1}:%L: %m%n


#DailyRollingFile
log4j.appender.DailyRollingFile=org.apache.log4j.DailyRollingFileAppender
log4j.appender.DailyRollingFile.File =${logdir}//sys01.log
log4j.appender.DailyRollingFile.layout = org.apache.log4j.PatternLayout
log4j.appender.DailyRollingFile.layout.ConversionPattern =%-d{M-d HH:mm:ss} %5p[%t]%c{1}:%L: %m%n


#RollingFile
log4j.appender.RollingFile = org.apache.log4j.RollingFileAppender
log4j.appender.RollingFile.File =${logdir}//log.log
log4j.appender.RollingFile.MaxFileSize=1MB
log4j.appender.RollingFile.MaxBackupIndex=3
log4j.appender.RollingFile.Append = true
log4j.appender.RollingFile.layout = org.apache.log4j.PatternLayout
log4j.appender.RollingFile.layout.ConversionPattern =%-d{M-d HH:mm:ss} %5p[%t]%c{1}:%L: %m%n


#指定com.integration.boot.service包的日志输出到指定文件
log4j.category.com.integration.boot.controller=DEBUG,controller
log4j.appender.controller=org.apache.log4j.FileAppender
log4j.appender.controller.File=${logdir}//controller.log
#log4j.appender.controller.Threshold = DEBUG ## 输出DEBUG级别以上的日志
log4j.appender.controller.layout=org.apache.log4j.PatternLayout
log4j.appender.controller.Append = true
log4j.appender.controller.layout.ConversionPattern =%-d{M-d HH:mm:ss} %5p[%t]%c{1}:%L: %m%n


#第三方jar包log输出级别
log4j.logger.org.springframework =INFO
log4j.logger.org.quartz=INFO
log4j.logger.org.apache.zookeeper=INFO
log4j.logger.com.alibaba.dubbo=INFO
#值得注意的是rocketmq自定义的log,并不是使用的包名
log4j.logger.RocketmqRemoting=INFO
log4j.logger.RocketmqClient=INFO
log4j.logger.RocketmqConsole=INFO
```



## 四、文件输出

默认情况下，Spring Boot将日志输出到控制台，不会写到日志文件。如果要编写除控制台输出之外的日志文件，则需在application.properties中设置logging.file或logging.path属性。

- logging.file，设置文件，可以是绝对路径，也可以是相对路径。如：`logging.file=log/my.log(相对)或者/log/my.log(绝对)`
- logging.path，设置目录，会在该目录下创建spring.log文件，并写入日志内容，如：`logging.path=/var/log`

如果只配置 logging.file，会在项目的当前路径下生成一个 xxx.log 日志文件。
如果只配置 logging.path，在 /var/log文件夹生成一个日志文件为 spring.log

## 五、MDC



### 集成logback

application.peoperties

```
logging.config=classpath:logback.xml
```

logback.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration debug="false">
    <!--定义日志文件的存储地址 勿在 LogBack 的配置中使用相对路径-->
    <property name="LOG_HOME" value="/test/log" />
    <!-- 控制台输出 -->
    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder">
            <!--格式化输出：%d表示日期，%thread表示线程名，%-5level：级别从左显示5个字符宽度%msg：日志消息，%n是换行符-->
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{50} - %msg%n</pattern>
        </encoder>
    </appender>
    <!-- 按照每天生成日志文件 -->
    <appender name="FILE"  class="ch.qos.logback.core.rolling.RollingFileAppender">
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <!--日志文件输出的文件名-->
            <FileNamePattern>${LOG_HOME}/TestWeb.log.%d{yyyy-MM-dd}.log</FileNamePattern>
            <!--日志文件保留天数-->
            <MaxHistory>30</MaxHistory>
        </rollingPolicy>
        <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder">
            <!--格式化输出：%d表示日期，%thread表示线程名，%-5level：级别从左显示5个字符宽度%msg：日志消息，%n是换行符-->
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{50} - %msg%n</pattern>
        </encoder>
        <!--日志文件最大的大小-->
        <triggeringPolicy class="ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy">
            <MaxFileSize>10MB</MaxFileSize>
        </triggeringPolicy>
    </appender>
    <!-- show parameters for hibernate sql 专为 Hibernate 定制 -->
  <!--   <logger name="org.hibernate.type.descriptor.sql.BasicBinder"  level="TRACE" />
    <logger name="org.hibernate.type.descriptor.sql.BasicExtractor"  level="DEBUG" />
    <logger name="org.hibernate.SQL" level="DEBUG" />
    <logger name="org.hibernate.engine.QueryParameters" level="DEBUG" />
    <logger name="org.hibernate.engine.query.HQLQueryPlan" level="DEBUG" /> -->

    <!--myibatis log configure-->
    <logger name="com.ibatis" level="DEBUG" />  
<logger name="com.ibatis.common.jdbc.SimpleDataSource" level="DEBUG" />  
<logger name="com.ibatis.common.jdbc.ScriptRunner" level="DEBUG" />  
<logger name="com.ibatis.sqlmap.engine.impl.SqlMapClientDelegate" level="DEBUG" />  
<logger name="java.sql.Connection" level="DEBUG" />  
<logger name="java.sql.Statement" level="DEBUG" />  
<logger name="java.sql.PreparedStatement" level="DEBUG" />  

    <!-- 日志输出级别 -->
    <root level="INFO">
        <appender-ref ref="STDOUT" />
        <appender-ref ref="FILE" />
    </root>
    
    <logger name="dao" level="DEBUG">  
    <!--daoFILE为实际定义的appender-->  
    <appender-ref ref="FILE" />  
</logger>  
    <!--日志异步到数据库 -->
    <!--<appender name="DB" class="ch.qos.logback.classic.db.DBAppender">-->
        <!--&lt;!&ndash;日志异步到数据库 &ndash;&gt;-->
        <!--<connectionSource class="ch.qos.logback.core.db.DriverManagerConnectionSource">-->
            <!--&lt;!&ndash;连接池 &ndash;&gt;-->
            <!--<dataSource class="com.mchange.v2.c3p0.ComboPooledDataSource">-->
                <!--<driverClass>com.mysql.jdbc.Driver</driverClass>-->
                <!--<url>jdbc:mysql://127.0.0.1:3306/databaseName</url>-->
                <!--<user>root</user>-->
                <!--<password>root</password>-->
            <!--</dataSource>-->
        <!--</connectionSource>-->
    <!--</appender>-->
</configuration>
```



# 三十四、WebJars

WebJars能使Maven的依赖管理支持OSS的JavaScript库/CSS库，比如jQuery、Bootstrap等。 

**（1）添加js或者css库** 

```xml
<dependency>  
    <groupId>org.webjars</groupId>  
    <artifactId>bootstrap</artifactId>  
    <version>3.3.7-1</version>  
</dependency>  
<dependency>  
    <groupId>org.webjars</groupId>  
    <artifactId>jquery</artifactId>  
    <version>3.1.1</version>  
</dependency>  
```

src/main/resources/static/demo.html 

```html
<html>  
    <head>  
        <script src="/webjars/jquery/3.1.1/jquery.min.js"></script>  
        <script src="/webjars/bootstrap/3.3.7-1/js/bootstrap.min.js"></script>  
        <title>WebJars Demo</title>  
        <link rel="stylesheet" href="/webjars/bootstrap/3.3.7-1/css/bootstrap.min.css" />  
    </head>  
    <body>  
        <div class="container"><br/>  
            <div class="alert alert-success">  
                <a href="#" class="close" data-dismiss="alert" aria-label="close">×</a>  
                Hello, <strong>WebJars!</strong>  
            </div>  
        </div>  
    </body>  
</html>  
```

启动应用访问 http://localhost:8080/demo.html 

**（2）省略版本号** 

很少在代码中硬编码版本号，所以需要隐藏它。 

pom.xml添加webjars-locator 
org.springframework.web.servlet.resource.WebJarsResourceResolver

```
<dependency>  
    <groupId>org.webjars</groupId>  
    <artifactId>webjars-locator</artifactId>  
    <version>0.31</version>  
</dependency>
```

src/main/resources/static/demo.html ，去掉版本号

```
<script src="/webjars/jquery/jquery.min.js"></script> 
<script src="/webjars/bootstrap/js/bootstrap.min.js"></script> 
<title>WebJars Demo</title> 
<link rel="stylesheet" href="/webjars/bootstrap/css/bootstrap.min.css" />
```

启动应用再次访问 http://localhost:8080/demo.html 结果和上边一样。 

引入的开源JavaScript库/CSS库将会以jar的形式被打包进工程! 
spring-boot-demo1-0.0.1-SNAPSHOT.jar\BOOT-INF\lib 

bootstrap-3.3.7-1.jar 
└─ META-INF 
    └─ resources 
        └─ webjars 
            └─ bootstrap 
                └─ 3.3.7-1 
                    ├─ css 
                    |   ├─ bootstrap.min.css 
                    |   ├─ bootstrap.min.css.gz # Gzip文件 
                    ...



jquery-3.1.1.jar 
└─ META-INF 
    └─ resources 
        └─ webjars 
            └─ jquery 
                └─ 3.1.1 
                    ├─ jquery.min.js 
                    ...



# 三十五、配置分离，日志和页面和jar包平行

`logging.config必须是xml后缀，否则不起作用。`

Spring程序会按优先级从下面这些路径来加载application.properties配置文件

- 当前目录下的/config目录
- 当前目录
- classpath里的/config目录
- classpath 跟目录

因此，要外置配置文件就很简单了，在jar所在目录新建config文件夹，然后放入配置文件，或者直接放在配置文件在jar目录

## 自定义配置文件

如果你不想使用application.properties作为配置文件，怎么办？完全没问题

```
java -jar myproject.jar --spring.config.location=classpath:/default.properties,classpath:/override.properties
```

或者

```
java -jar -Dspring.config.location=D:\config\config.properties springbootrestdemo-0.0.1-SNAPSHOT.jar 
```

当然，还能在代码里指定

```
@SpringBootApplication
@PropertySource(value={"file:config.properties"})
public class SpringbootrestdemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootrestdemoApplication.class, args);
    }
}
```

## 载入日志配置

DOMConfigurator载入的是log4j.xml而PropertyConfigurator载入的是log4j.properties文件

```
logdir=log  //jar包同级有个log文件件，日志会输出到这个文件件
log4j.appender.controller.File=${logdir}//controller.log
```



## JAR包中的MANIFEST.MF文件详解以及编写规范

```
以下是需要注意的各个要点： 

0.  最后一样一定要回车，空一行，不然无法识别最后一行的配置。
1. Manifest-Version、Main-Class和Class-Path后面跟着一个英文的冒号，冒号后面必须跟着一个空格，然后才是版本号、类和ClassPath。 
2. Class-Path中的各项应使用空格分隔，不是逗号或分号。 
3. Class-Path中如果有很多项，写成一行打包的时候会报错line too long，这时需要把Class-Path分多行写。注意：从第二行开始，必须以两个空格开头，三个以上我没试过，不过不用空格开头和一个空格开头都是不行的，我已经试过了。 
4. Class-Path写完之后最后一定要有一个空行。 
5. jar包内有些配置文件想放在jar包外面，比如文件config.properties：如果这个文件是以路径方式载入的，比如new file("./config/config.properties")，那么将config.properties放在jar包相同目录下的config目录下即可，也就是说“./”路径等价于jar包所在目录；如果这个文件是以ClassPath下的文件这种方式载入的，比如在Spring中载入classpath:config.properties，则在MF文件的配置文件的ClassPath中添加“./”，然后将这个配置文件与jar包放在同一个目录即可，当然也可以在MF文件的配置文件的ClassPath中添加“./config/”，然后把配置文件都放在jar包相同目录下的config目录下。
```



## 打包方式一

http://maven.apache.org/plugins/maven-assembly-plugin/assembly.html

https://www.cnblogs.com/f-zhao/p/6929814.html



配置文件放在resources / config下：

application.properties    默认加载

log4j.properties   手动加载（开发和部署）

```
public static void loadLogConfig(){
    	
	try {
			PropertyConfigurator.configure(Application.class.getClassLoader().getResource("").getPath() + File.separator+"config"+File.separator+"log4j.properties");
		} catch (Exception e) {
			logger.error("=========config not found，try to load from user.dir =======");
			PropertyConfigurator.configure(System.getProperty("user.dir") + File.separator+"config"+ File.separator+"log4j.properties");
		}
}
```

```xml
<!-- 打包插件  用maven-jar-plugin时spring-boot-maven-plugin需要去除，不然把的包执行不了-->
        	<!-- <plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
			</plugin> -->
			
			 <plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-jar-plugin</artifactId>
				<configuration>
					<archive>
					
					 <!--生成的jar中，不要包含pom.xml和pom.properties这两个文件 -->
                    <addMavenDescriptor>false</addMavenDescriptor>
					
						<manifest>
							<mainClass>com.integration.boot.Application</mainClass>
							<addClasspath>true</addClasspath><!--是否要把第三方jar放到manifest的classpath中 -->
							 <!--MANIFEST.MF中 Class-Path加入前缀 -->
							<classpathPrefix>lib/</classpathPrefix> <!--生成的manifest中classpath的前缀，因为要把第三方jar放到lib目录下，所以classpath的前缀是lib/-->
							 <!--jar包不包含唯一版本标识-->
                            <useUniqueVersions>false</useUniqueVersions>
						</manifest>
					 <manifestEntries>  
                         <Class-Path>./config/ ./config02/</Class-Path>
                     </manifestEntries> 
					</archive>
					  <!--过滤掉不希望包含在jar中的文件-->
					 <excludes>
						<exclude>config/**</exclude>
						<exclude>static/**</exclude>
						<exclude>/**/log4j.properties</exclude>
					</excludes> 
				</configuration>
			</plugin>
			
			<plugin>
				<artifactId>maven-assembly-plugin</artifactId>
				<configuration>
					<finalName>${name}</finalName>
					<appendAssemblyId>false</appendAssemblyId>
					<descriptors>
						<descriptor>src/main/build/package.xml</descriptor>
					</descriptors>
				</configuration>
				<executions>
					<execution>
						<id>make-assembly</id>
						<phase>package</phase>
						<goals>
							<goal>single</goal>
						</goals>
					</execution>
				</executions>
			</plugin> 
```



```xml
<?xml version="1.0" encoding="UTF-8"?>  
<assembly xmlns="http://maven.apache.org/plugins/maven-assembly-plugin/assembly/1.1.3" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"  
  xsi:schemaLocation="http://maven.apache.org/plugins/maven-assembly-plugin/assembly/1.1.3 http://maven.apache.org/xsd/assembly-1.1.3.xsd">  
   <id>dev</id>  <!-- id 标识符，添加到生成文件名称的后缀符。如果指定 id 的话，目标文件则是 ${artifactId}-${id}.tar.gz -->
    <formats>  
    <!-- zip,tar,tar.gz,tar.bz2,jar,dir,war -->
        <format>zip</format>  
    </formats>  
     <!--tar.gz 压缩包下是否生成和项目名相同的根目录-->
    <includeBaseDirectory>true</includeBaseDirectory>  
    
     
    <dependencySets>  
        <dependencySet>  
         <!--useProjectArtifact为true，则会把打的jar包放在zip对应的依赖包目录下，否则不会放进去-->
            <useProjectArtifact>true</useProjectArtifact>
            <outputDirectory>${file.separator}lib</outputDirectory> 
            <unpack>false</unpack>
           <!--  <scope>runtime</scope> -->
            <excludes><!--打的jar默认会 放到lib下的，这里排除掉 -->
            <!--<exclude>${groupId}:${artifactId}</exclude>-->
            </excludes>  
        </dependencySet>  
    </dependencySets>  
    
    <fileSets>  
        <fileSet>  
            <directory>bin</directory>  
            <outputDirectory>${file.separator}</outputDirectory>  
        </fileSet>  
        <fileSet>  
            <directory>src/main/resources</directory>  
            <outputDirectory>${file.separator}</outputDirectory>  
            <includes>
                <include>config/**</include>
                <include>static/**</include>
                <include>/**/log4j.properties</include>
           </includes>
            <!--打包时是否进行文件置换(将 maven profile 中的 properties与配置文件引用置换)-->
            <filtered>true</filtered>
        </fileSet>  
        
        <fileSet>  
            <directory>${project.build.directory}</directory>  
            <outputDirectory>/</outputDirectory>  
            <includes>  
                <include>*.jar</include>  
            </includes>  
        </fileSet>  
       <!--  <fileSet>
            <directory>src/main/assembly/bin</directory>
            <outputDirectory>/bin</outputDirectory>
            <includes>
                <include>*.sh</include>
            </includes>
                                 分配脚本文件可执行权限
            <fileMode>0755</fileMode>
        </fileSet> -->
    </fileSets>  
    
 <!--<files>  
    <file>  
        <source>b.txt</source>  
        <outputDirectory>/</outputDirectory>  
        <destName>b.txt.bak</destName>
    </file>  
</files> -->
     <!-- <files>
	    <file>
	    <source>target/springboot4Docker-1.0-SNAPSHOT.jar</source>
	    <outputDirectory>./lib</outputDirectory>
	    <outputDirectory>./</outputDirectory>
	    <destName>app.jar</destName>
	    </file>
    </files> -->
</assembly>  
```

## 打包方式二

application.properties 放在config目录或者resource下(默认加载)

log4j.properties  放在resource下(默认加载)

重点`<Class-Path>./config/</Class-Path>` 打包后把/config/设置为classpath，log4j默认在此位置加载

- dependencySets用来定义选择依赖并定义最终打包到什么目录，这里我们声明的一个depenencySet默认包含所有所有 依赖，而useProjectArtifact表示将项目本身生成的构件也包含在内，最终打包至输出包内的lib路径下（由 outputDirectory指定）。



```xml
<plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-jar-plugin</artifactId>
				<configuration>
					<archive>
						<manifest>
							<mainClass>com.docker.DockerApplication</mainClass>
							<addClasspath>true</addClasspath>
							<classpathPrefix>lib/</classpathPrefix>
						</manifest>
						 <manifestEntries>
                         <!--MANIFEST.MF 中 Class-Path加入资源文件目录,只能配置一个，后面的覆盖前面的 -->
                         <Class-Path>./config/</Class-Path>
                         </manifestEntries>
					</archive>
					<excludes>
						<exclude>config/**</exclude>
						<exclude>log4j.properties</exclude>
					</excludes>
				</configuration>
			</plugin>

			<plugin>
				<artifactId>maven-assembly-plugin</artifactId>
				<configuration>
					<finalName>${name}</finalName>
					<appendAssemblyId>false</appendAssemblyId>
					<descriptors>
						<descriptor>src/main/build/package.xml</descriptor>
					</descriptors>
				</configuration>
				<executions>
					<execution>
						<id>make-assembly</id>
						<phase>package</phase>
						<goals>
							<goal>single</goal>
						</goals>
					</execution>
				</executions>
	</plugin>
```

```xml
<?xml version="1.0" encoding="UTF-8"?>  
<assembly xmlns="http://maven.apache.org/plugins/maven-assembly-plugin/assembly/1.1.3" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"  
  xsi:schemaLocation="http://maven.apache.org/plugins/maven-assembly-plugin/assembly/1.1.3 http://maven.apache.org/xsd/assembly-1.1.3.xsd">  
    <id>dev</id>  
    <formats>  
        <format>zip</format>  
    </formats>  
    <includeBaseDirectory>true</includeBaseDirectory>  
    <fileSets>  
        <!-- <fileSet>  
            <directory>bin</directory>  
            <outputDirectory>/</outputDirectory>  
        </fileSet>  --> 
        <fileSet>  
            <directory>src/main/resources</directory>  
            <outputDirectory>/</outputDirectory>  
            <includes>
                <include>config/**</include>
           </includes>
            <!--打包时是否进行文件置换(将 maven profile 中的 properties与配置文件引用置换)-->
            <filtered>true</filtered>
        </fileSet>  
        
         <fileSet>  
            <directory>src/main/resources</directory>  
            <outputDirectory>/config</outputDirectory>  
            <includes>
                <include>log4j.properties</include>
           </includes>
        </fileSet> 
        
        <fileSet>  
            <directory>${project.build.directory}</directory>  
            <outputDirectory>/</outputDirectory>  
            <includes>  
                <include>*.jar</include>  
            </includes>  
        </fileSet>
        
       <!--  <fileSet>
            <directory>src/main/assembly/bin</directory>
            <outputDirectory>/bin</outputDirectory>
            <includes>
                <include>*.sh</include>
            </includes>
                                 分配脚本文件可执行权限
            <fileMode>0755</fileMode>
        </fileSet> -->
    </fileSets>  
    
    <dependencySets>  
        <dependencySet>  
         <!--useProjectArtifact为true，则会把打的jar包放在zip对应的依赖包目录下，否则不会放进去-->
            <useProjectArtifact>true</useProjectArtifact>
            <outputDirectory>lib</outputDirectory> 
            <!-- <unpack>false</unpack>  
            <scope>runtime</scope> -->
            <excludes><!--打的jar默认会 放到lib下的，这里排除掉 -->
            <!--<exclude>${groupId}:${artifactId}</exclude>-->
            </excludes>  
        </dependencySet>  
    </dependencySets>  
    
 <!--文件重命名<files>  
    <file>  
        <source>b.txt</source>  
        <outputDirectory>/</outputDirectory>  
        <destName>b.txt.bak</destName>
    </file>  
</files> -->

</assembly>  
```



# 三十六、Lombok

# 三十七、日期格式化

```
spring.jackson.date-format=yyyy-MM-dd HH:mm:ss
```



# 三十八 、邮件

see:https://blog.csdn.net/u011244202/article/details/54809696

http://blog.didispace.com/springbootmailsender/

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-mail</artifactId>
</dependency>
```



```properties
# JavaMailSender 邮件发送的配置
#若使用QQ邮箱发送邮件，则需要修改为spring.mail.host=smtp.qq.com，同时spring.mail.password改为QQ邮箱的授权码。 
#QQ邮箱->设置->账户->POP3/SMTP服务:开启服务后会获得QQ的授权码 
spring.mail.host=smtp.qq.com
spring.mail.username=475402366@qq.com
spring.mail.password=aaaaa
spring.mail.properties.mail.smtp.auth=false
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.starttls.required=true
```

```java
package springBoot;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.integration.boot.Application;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes=Application.class)
public class MailTest {
	
	@Autowired
	private JavaMailSender mailSender;
	
	
	/**
	 * 发送简单的邮件
	 **/
	
	@Test
	public void sendSimpleMail() throws Exception {
		
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("475402366@qq.com");
		message.setTo("69404905@qq.com");
		message.setSubject("123");
		message.setText("1234");
		try {
			mailSender.send(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void sendAttachmentsMail() throws Exception {
		
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
		
		helper.setFrom("475402366@qq.com");
		helper.setTo("475402366@qq.com");
		helper.setSubject("主题：有附件");
		helper.setText("有附件的邮件");
		FileSystemResource file = new FileSystemResource(new File("C:\\Users\\Administrator\\Desktop\\QQ截图20170603165155.png"));
		helper.addAttachment("附件-1.jpg", file);
		helper.addAttachment("附件-2.jpg", file);
		mailSender.send(mimeMessage);
	}
	
	@Test
	public void sendInlineMail() throws Exception {
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
		helper.setFrom("475402366@qq.com");
		helper.setTo("475402366@qq.com");
		helper.setSubject("主题：嵌入静态资源");
		helper.setText("<html><body><img src=\"cid:thisisaimg\" ></body></html>", true);
		FileSystemResource file = new FileSystemResource(new File("C:\\Users\\Administrator\\Desktop\\QQ截图20170603165155.png"));
		helper.addInline("thisisaimg", file);
		mailSender.send(mimeMessage);
	}

	@Test
	public void sendTemplateMail() throws Exception {
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
		helper.setFrom("dyc87112@qq.com");
		helper.setTo("dyc87112@qq.com");
		helper.setSubject("主题：模板邮件");
		Map<String, Object> model = new HashMap();
		model.put("username", "didi");
//		String text = VelocityEngineUtils.mergeTemplateIntoString(
//				velocityEngine, "template.vm", "UTF-8", model);
//		helper.setText(text, true);
		mailSender.send(mimeMessage);
	}
}
```







# 三十九、AOP

http://blog.didispace.com/springbootaoplog/

```java
package com.integration.boot.filter;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target({ ElementType.PARAMETER, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AspectAction {
	
	String value() default "";

}

```

```java
package com.integration.boot.filter;

import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogAspect {

	
	    @Pointcut("@annotation(com.boot.annotation.AspectAction)")
	    public void log() {

	    }
	 
	 /**
	     * 前置通知
	     */
	    @Before("log()")
	    public void doBeforeController(JoinPoint joinPoint) {
	        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
	        Method method = signature.getMethod();
	        AspectAction action = method.getAnnotation(AspectAction.class);
	        System.out.println("action名称 " + action.value()); // ⑤
	    }

	    /**
	     * 后置通知
	     */
	    @AfterReturning(pointcut = "log()", returning = "retValue")
	    public void doAfterController(JoinPoint joinPoint, Object retValue) {
	        System.out.println("retValue is:" + retValue);
	    }
}

```



# 四十、事件通知

# 四十一、tomcat日志

```properties
server.session-timeout=30
#日志开关
server.tomcat.access-log-enabled=true
#日志格式
server.tomcat.access-log-pattern=%h %l %u %t "%r" %s %b %D "%{Referer}i" "%{User-Agent}i"
#日志输出目录，这里是设置为当前目录下
server.tomcat.basedir=./
```



# 四十二  、自定义tomcat参数

```java
@SpringBootApplication
@EnableEurekaClient
public class ApplicationWebController {   
	 
	public static void main(String[] args) {
		new SpringApplicationBuilder(ApplicationWebController.class).web(true).run(args);
          System.out.println("======webController start successful==========");
          /**配置文件读取规则:根目录config下的application.properties/无config,resource下的config，resource下的application.properties/**/
          /**
          new SpringApplicationBuilder(Application.class).properties("spring-config-location=classpath:/abc.properties").run(args);**/
      }
	
	
	/**
	@Component
    public static class CustomServletContainer implements EmbeddedServletContainerCustomizer{

        @Override
        public void customize(ConfigurableEmbeddedServletContainer container) {
            container.setPort(8888);//①
            container.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/404.html"));//②
            container.setSessionTimeout(10,TimeUnit.MINUTES);//③
        }
    }**/
	
	/**
	@Bean
	public ConfigurableServletWebServerFactory webServerFactory() {
		TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
		factory.setPort(9000);
		factory.setSessionTimeout(10, TimeUnit.MINUTES);
		factory.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/notfound.html"));
		return factory;
	}**/
  }
```



# 四十三 @ComponentScan

@ComponentScan(basepackages="com.boot")

# 参考文档

https://blog.csdn.net/column/details/zkn-springboot.html?&page=2

http://blog.didispace.com/categories/Spring-Boot/

源码例子：https://gitee.com/didispace/SpringBoot-Learning

​                   https://gitee.com/forezp/SpringBootLearning

http://blog.didispace.com/spring-boot-run-backend/

https://blog.csdn.net/k21325/article/details/52789829

http://www.cnblogs.com/ityouknow/category/914493.html