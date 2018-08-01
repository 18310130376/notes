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





# 参考文档

https://blog.csdn.net/column/details/zkn-springboot.html?&page=2

http://blog.didispace.com/categories/Spring-Boot/

源码例子：https://gitee.com/didispace/SpringBoot-Learning

http://blog.didispace.com/spring-boot-run-backend/

https://blog.csdn.net/k21325/article/details/52789829