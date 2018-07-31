# 一、运行方式

```
java -jar springboot-release.jar
java -jar springboot-release.jar  --spring.prifiles.active=dev &
nohup java -jar yourapp.jar &
mvn spring-boot:run
```



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

```java
<util:properties id="settings" location="WEB-INF/classes/META-INF/spring/test.properties" />

   private String imageDir;   

   @Value("#{settings['test.abc']}")     
    public void setImageDir(String val) {     
        this.imageDir = val;     
    }
```



## @PropertySource

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





## Environment

## @ConfigurationProperties 

```java

@ConfigurationProperties(locations = "classpath:mail.properties", ignoreUnknownFields = false, 
                         prefix = "mail")
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



# 三 、Configuration声明bean

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
spring.profiles.active=@profileActive@
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



# 参考文档

https://blog.csdn.net/column/details/zkn-springboot.html?&page=2

http://blog.didispace.com/categories/Spring-Boot/

源码例子：https://gitee.com/didispace/SpringBoot-Learning

http://blog.didispace.com/spring-boot-run-backend/

https://blog.csdn.net/k21325/article/details/52789829