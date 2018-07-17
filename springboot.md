see:https://github.com/vector4wang/spring-boot-quick

see:https://www.cnblogs.com/sunny3096/category/1034222.html

maven插件详细介绍

https://segmentfault.com/a/1190000015077021

# Jar方式运行

后台启动并打印日志：

nohup java  -Xms128m -Xmx512m -jar boot.jar Djava.security.egd=file:/dev/./urandom > 日志.log 2>&1 &

解决tomcat启动慢的问题: Djava.security.egd=file:/dev/./urandom

nohup java -Xms128m -Xmx512m -jar boot.jar  1>/dev/null 2>&1 &

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]

为了缩短 Tomcat 的启动时间，添加 java.security.egd 的系统属性指向 /dev/urandom 



nohup java -jar aaa.jar &
也可以指定日志文件文件名随意（如：aaa.log）

nohup java -jar aaa.jar > aaa.log 2>&1 &

3、查看进程可以使用 ps -ef|grep 'java -jar'

4、运行后，在当前路径下会生成nohup.out文件，会记录服务器的日志。

# 整合dubbo

https://github.com/bz51/SpringBoot-Dubbo-Docker-Jenkins

# 常用插件

## Spring Boot Maven plugin

Spring Boot Maven plugin的5个Goals

- spring-boot:repackage，默认goal。在mvn package之后，再次打包可执行的jar/war，同时保留mvn package生成的jar/war为.origin
- spring-boot:run，运行Spring Boot应用
- spring-boot:start，在mvn integration-test阶段，进行Spring Boot应用生命周期的管理
- spring-boot:stop，在mvn integration-test阶段，进行Spring Boot应用生命周期的管理
- spring-boot:build-info，生成Actuator使用的构建信息文件build-info.properties

```
<plugin>
       <groupId>org.springframework.boot</groupId>
         <artifactId>spring-boot-maven-plugin</artifactId>
           <configuration>
			<fork>true</fork>
			 <mainClass>com.docker.DockerApplication</mainClass>
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

project节点包含（jar是默认的打包方式）

```
 <packaging>jar</packaging>
```

执行命令

```
mvn package spring-boot:repackage 或
mvn clean package
mvn spring-boot:run
mvn clean install package spring-boot:repackage
```

```
java -jar springboot4Docker-1.0-SNAPSHOT.jar &
java -jar springboot4Docker-1.0-SNAPSHOT.jar -Dspring.config.location=applicaion.properties &

nohup java -Xmx512M -Xms512M -Djava.security.egd=/dev/urandom -jar springboot4Docker-1.0-SNAPSHOT.jar > /dev/null 2>&1 &
```

## maven-jar-plugin

```
<plugin>
       <groupId>org.apache.maven.plugins</groupId>
             <artifactId>maven-jar-plugin</artifactId>
            <version>2.3.1</version>
         <configuration>
            <archive>
                <manifest>
                <!--运行jar包时运行的主类，要求类全名-->
                <!--<mainClass>com.alibaba.dubbo.container.Main</mainClass>-->
                     <mainClass>cn.wolfcode.dubbo.main.DubboDemoServer</mainClass>
                     <!-- 是否指定项目classpath下的依赖 -->
                     <addClasspath>true</addClasspath>
                     <!-- 指定依赖的时候声明前缀 -->
                     <classpathPrefix>./</classpathPrefix>
                </manifest>
           </archive>
                    <!-- 排除 jar 包中的配置文件 -->
                <excludes>
                        <exclude>**/*.properties</exclude>
                        <exclude>**/*.xml</exclude>
               </excludes>
          </configuration>
</plugin>
```

jar项目默认的打包工具，默认情况下只会将项目源码编译生成的class文件和资源文件打包进来，不会打包进项目依赖的jar包。 



## maven-war-plugin 

```
<plugin>      

   <groupId>org.apache.maven.plugins</groupId>      

   <artifactId>maven-war-plugin</artifactId>      

   <configuration>      

    <warSourceExcludes>src/main/resources/**</warSourceExcludes> 

    <warName>gateway</warName>      

   </configuration>      

</plugin>
```

```
<dependency>

    <groupId>org.springframework.boot</groupId>

    <artifactId>spring-boot-starter-tomcat</artifactId>

    <scope>provided</scope>

</dependency>
```



## maven-assembly-plugin

下面是pom.xml 和 package.xml部分内容

```
 <plugin>  
                <artifactId>maven-assembly-plugin</artifactId>  
                <configuration>  
                    <finalName>app</finalName>  
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

```
<?xml version="1.0" encoding="UTF-8"?>  
<assembly xmlns="http://maven.apache.org/plugins/maven-assembly-plugin/assembly/1.1.3" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"  
  xsi:schemaLocation="http://maven.apache.org/plugins/maven-assembly-plugin/assembly/1.1.3 http://maven.apache.org/xsd/assembly-1.1.3.xsd">  
    <id>package</id>  
    <formats>  
        <format>zip</format>  
    </formats>  
    <includeBaseDirectory>true</includeBaseDirectory>  
    <fileSets>  
        <fileSet>  
            <directory>bin</directory>  
            <outputDirectory>/</outputDirectory>  
        </fileSet>  
        <fileSet>  
            <directory>src/main/resources</directory>  
            <outputDirectory>/config/</outputDirectory>  
        </fileSet>  
        <fileSet>  
            <directory>${project.build.directory}</directory>  
            <outputDirectory>/</outputDirectory>  
            <includes>  
                <include>*.jar</include>  
            </includes>  
        </fileSet>  
    </fileSets>  
    <dependencySets>  
        <dependencySet>  
            <outputDirectory>lib</outputDirectory>  
            <scope>runtime</scope>  
            <excludes>  <!-- 打的jar默认会 放到lib下的，这里排除掉 -->
               <!--  <exclude>${groupId}:${artifactId}</exclude> -->
              <exclude>${project.artifactId}-${project.version}.jar</exclude>   
            </excludes>  
        </dependencySet>  
    </dependencySets>  
</assembly>  
```

最终的zip的名字是 artifactId+version+".zip",

如果appendAssemblyId为true，则会加上assembly.id,即是：artifactId-version-assembly.id.zip,

如果指定finalName，则打包的zip名字包含finalName。

execution：phase加入package后，则在执行maven package时就可以调用maven-assembly-plugin插件定义的打包方式。 

dependencySet 标签是指把项目依赖的所有包都打包输出到某个路径 ，并且会把maven-jar-plugin插件自动生成的jar也导入到lib中 。本次需求是需要把该jar排除掉，于是使用exclude标签 

dependencySet中使用exclude标签中过滤的名字有点坑，不能直接写文件名。比如写<exclude>service-0.0.1-SNAPSHOT.jar</exclude>是不生效的。匹配的名字规则：

- `groupId:artifactId:type:classifier` ( `artifact.getDependencyConflictId()` )
- `groupId:artifactId` ( `ArtifactUtils.versionlessKey( artifact )` )
- `groupId:artifactId:type:classifier:version` ( `artifact.getId()` )



useProjectArtifact为true，则会把打的jar包放在zip对应的依赖包目录下，否则不会放进去。

## Maven多环境配置

```
<profiles>
        <!-- 开发环境配置 -->
        <profile>
            <id>dev</id>
            <properties>
                <active.profile>dev</active.profile>
                <application.name>dubbo-demo-server-dev</application.name>
                <registry.address>N/A</registry.address>
                <protocol.name>dubbo</protocol.name>
                <protocol.port>20880</protocol.port>
                <scan.basepackage>cn.wolfcode.dubbo.service</scan.basepackage>
            </properties>
            <activation>
                <!-- 默认使用开发环境 -->
                <activeByDefault>true</activeByDefault>
            </activation>
        </profile>
        <!-- 测试环境配置 -->
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
        <!-- 生产环境配置 -->
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
# dubbo 服务名
spring.dubbo.application.name=@application.name@
# 注册中心地址（N/A表示为不启用）
spring.dubbo.registry.address=@registry.address@
# rpc 协议实现使用 dubbo 协议
spring.dubbo.protocol.name=@protocol.name@
# 服务暴露端口
spring.dubbo.protocol.port=@protocol.port@
# 基础包扫描
spring.dubbo.base-package=@scan.basepackage@
```

PS：SpringBoot 中引用 profile 的值使用 @propertyName@，传统 Spring 项目使用 ${propertyName} 引用 

```
 <fileSet>  
            <directory>src/main/resources</directory>  
            <outputDirectory>/conf</outputDirectory>  
            <includes>
                <include>**/*.xml</include>
                <include>**/*.properties</include>
           </includes>
            <!-- 打包时是否进行文件置换(将 maven profile 中的 properties 与配置文件引用置换) -->
            <filtered>true</filtered>
        </fileSet>  
```

```
mvn package -P dev
```

## Maven Jar远程部署插件之wagon-maven-plugin

用于一键部署，把本地打包的jar文件，上传到远程服务器上，并执行服务器上的shell命令 

https://blog.csdn.net/alibert/article/details/78912523



![img](https://images2015.cnblogs.com/blog/1112611/201704/1112611-20170418214006899-1739700806.png) 

```
<extensions>
	<extension>
		<groupId>org.apache.maven.wagon</groupId>
		<artifactId>wagon-ssh</artifactId>
		<version>2.10</version>
	</extension>
</extensions>
```

```
<plugin>
				<groupId>org.codehaus.mojo</groupId>
				<artifactId>wagon-maven-plugin</artifactId>
				<version>1.0</version>
				<executions>
					<execution>
						<id>upload-deploy</id>
						<phase>package</phase>
						<goals>
							<goal>upload-single</goal>
							<goal>sshexec</goal>
						</goals>
						<configuration>
						<!-- 此处maven的setting配置用户名和密码，不配置可也以 ，用scp://root:475402366@192.168.135.133/root/app/-->
							<serverId>webserver</serverId>
							<!-- 需要部署的文件 -->
							<fromFile>target/app.tar</fromFile>
							<!-- 部署目录 -->
							<url>scp://root:475402366@192.168.135.133/root/app/</url>
							<commands>
								<!-- 关闭tomcat -->
								<command>tar -xvf /root/app/app.tar</command>
							    <!--<command>/home/hadoop/apache-tomcat-8.0.5/bin/shutdown.sh</command> -->
								<!-- 删除之前解压后的目录 -->
								<!-- <command>rm -rf /home/hadoop/apache-tomcat-8.0.5/webapps/osc-shop </command>-->
								<!-- 启动tomcat -->
								<!-- <command>/home/hadoop/apache-tomcat-8.0.5/bin/startup.sh</command> -->
							</commands>
							<displayCommandOutputs>true</displayCommandOutputs>
						</configuration>
					</execution>
				</executions>
			</plugin>
```

为了让wagon-maven-plugin插件能SSH连上Linux服务器，首先需要在Maven的配置文件settings.xml中配置好server的用户名和密码 

```
<server>  
    <id>webserver</id>  
    <username>hadoop</username>  
    <password>123</password>  
</server> 
```

## tomcat7-maven-plugin

用于远程部署Java Web项目 

```
<plugin>
    <groupId>org.apache.tomcat.maven</groupId>
    <artifactId>tomcat7-maven-plugin</artifactId>
    <version>2.2</version>
    <configuration>
        <url>http://59.110.162.178:8080/manager/text</url>
        <username>linjinbin</username>
        <password>linjinbin</password>
    </configuration>
</plugin>
```



## 利用maven的resources、filter和profile实现不同环境使用不同配置文件

see：https://blog.csdn.net/hjiacheng/article/details/57413933

基本概念说明（resources、filter和profile）： 
1.profiles定义了各个环境的变量id 
2.filters中定义了变量配置文件的地址，其中地址中的环境变量就是上面profile中定义的值 
3.resources中是定义哪些目录下的文件会被配置文件中定义的变量替换，一般我们会把项目的配置文件放在src/main/resources下，像db,bean等，里面用到的变量在打包时就会根据filter中的变量配置替换成固定值 
在我们平常的java开发中，会经常使用到很多配制文件（xxx.properties，xxx.xml），而当我们在本地开发（dev），测试环境测试（test），线上生产使用（product）时，需要不停的去修改这些配制文件，次数一多，相当麻烦。现在，利用maven的filter和profile功能，我们可实现在编译阶段简单的指定一个参数就能切换配制，提高效率，还不容易出错，详解如下。 
一，原理： 
    利用filter实现对资源文件(resouces)过滤 
maven filter可利用指定的xxx.properties中对应的key=value对资源文件中的${key}进行替换，最终把你的资源文件中的username=${key}替换成username=value 
    利用profile来切换环境 
maven profile可使用操作系统信息，jdk信息，文件是否存在，属性值等作为依据，来激活相应的profile，也可在编译阶段，通过mvn命令加参数 -PprofileId 来手工激活使用对应的profile 
结合filter和profile，我们就可以方便的在不同环境下使用不同的配制 
二，配制： 
在工程根目录下添加3个配制文件： 
    config-dev.properties  -- 开发时用 
    config-test.properties  -- 测试时用 
    config-product.properties -- 生产时用 
工程根目录下的pom文件中添加下面的设置： 

```
<build>
<resources>
    <!-- 先指定 src/main/resources下所有文件及文件夹为资源文件 -->
    <resource>
        <directory>src/main/resources</directory>
        <includes>
            <include>**/*</include>
        </includes>
    </resource>
    <!-- 设置对auto-config.properties，jdbc.properties进行过虑，即这些文件中的${key}会被替换掉为真正的值 -->
    <resource>
        <directory>src/main/resources</directory>
        <includes>
            <include>auto-config.properties</include>
            <include>jdbc.properties</include>
        </includes>
        <filtering>true</filtering>
    </resource>
</resources>
</build>

<profiles>
<profile>
    <id>dev</id>

    <!-- 默认激活开发配制，使用config-dev.properties来替换设置过虑的资源文件中的${key} -->
    <activation>
        <activeByDefault>true</activeByDefault>
    </activation>
    <build>
        <filters>
            <filter>config-dev.properties</filter>
        </filters>
    </build>
</profile>
<profile>
    <id>test</id>
    <build>
        <filters>
            <filter>config-dev.properties</filter>
        </filters>
    </build>
</profile>
<profile>
    <id>product</id>
    <build>
        <filters>
            <filter>config-product.properties</filter>
        </filters>
    </build>
</profile>
</profiles> 
```

 三，使用： 
    开发环境： 
filter是在maven的compile阶段执行过虑替换的，所以只要触发了编译动作即可，如果像以前一样正常使用发现没有替换，则手工clean一下工程（eclipse -> Project -> Clean）【这里你应该要安装上maven插件，因为替换是maven做的，不是eclipse做的，所以这里的clean应当是触发了maven的compile】，然后在Tomcat上也右键 -> Clean一下即可，然后你去tomcat目录下会发现你的工程的资源文件里面的${key}被替换为对应的config-xx的值了 
如果上面还不行，那么就使用maven插件或者手工控制台下打maven编译命令吧 
因为pom.xml中设置了dev为默认激活的，所以默认会把config-dev拿来进行替换${key} 
    测试环境 
手工编译，打包：maven clean install -Ptest	-- 激活id="test"的profile 
    生产环境 
手工编译，打包：maven clean install -Pproduct	-- 激活id="product"的profile 



## 修改编译得到的文件名

默认情况下，通过maven package命令编译得到的文件名为artifactId-version所设置的值。比如，使用下面的pom.xml文件时，通过maven package命令编译得到的文件名为“springboot4Docker-1.0-SNAPSHOT.jar”：

```
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	<artifactId>springboot4Docker</artifactId>
	<groupId>com.docker</groupId>
	<version>1.0-SNAPSHOT</version>
	<modelVersion>4.0.0</modelVersion>
	<packaging>jar</packaging>
	<properties>
		<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
		<docker.image.prefix>13662241921</docker.image.prefix>
		<spring.boot.version>1.3.3.RELEASE</spring.boot.version>
	</properties>
</project>	
```

### 手工设置编译得到的文件名

在上面的pom.xml中进行修改，在build节点下添加finalName节点。finalName的值作为编译得到的文件名使用。比如使用下面的pom.xml文件时，通过maven package命令编译得到的文件名为“newfilename.jar”：

```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" 
xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
    http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>mygroupid</groupId>
    <artifactId>test.maven.filename</artifactId>
    <version>1.0</version>
    <packaging>jar</packaging>

    <name>test</name>
...
    <build>
        <finalName>newfilename</finalName>
        <plugins>
            ...
        </plugins>
    </build>
</project>
```

还可以使用变量设置

```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" 
xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
    http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>mygroupid</groupId>
    <artifactId>test.maven.filename</artifactId>
    <version>1.0</version>
    <packaging>jar</packaging>
    <name>test</name>
...
    <build>
        <finalName>${name}-${version}</finalName>
        <plugins>
            ...
        </plugins>
    </build>
</project>
```

## 文件拷贝

see;https://www.cnblogs.com/langke93/p/3420124.html

```
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
								<fileSet>
									<sourceFile>src/main/resources/release/init_${releaseEnv}.properties</sourceFile>
									<destinationFile>${basedir}/target/classes/init.properties</destinationFile>
								</fileSet>
								<fileSet>
									<sourceFile>src/main/resources/release/log4j_${releaseEnv}.properties</sourceFile>
									<destinationFile>${basedir}/target/classes/log4j.properties</destinationFile>
								</fileSet>
								<fileSet>
									<sourceFile>src/main/resources/release/quartz.properties</sourceFile>
									<destinationFile>${basedir}/target/classes/quartz.properties</destinationFile>
								</fileSet>
							</fileSets>
						</configuration>
					</execution>
				</executions>
	</plugin>
```



## 内容替换

```
<plugin>
				<groupId>com.google.code.maven-replacer-plugin</groupId>
				<artifactId>replacer</artifactId>
				<version>1.5.3</version>
				<executions>
					<execution>
						<phase>prepare-package</phase>
						<goals>
							<goal>replace</goal>
						</goals>
					</execution>
				</executions>
				<configuration>
					<includes>
						<include>${basedir}/target/classes/log4j.properties</include>
						<include>${basedir}/target/classes/dubbo.properties</include>
						<include>${basedir}/target/classes/init.properties</include>
						<include>${basedir}/target/classes/quartz.properties</include>
					</includes>
                	<replacements>
                    	<replacement>
							<token>Goldoffice_api</token>
							<value>${project.file.parentFile.parentFile.name}/${project.file.parentFile.name}_${releaseVersion}_${releaseEnv}</value>
						</replacement>
						<!-- <replacement>
							<token>netty\.server\.port\=[0-9]{4}</token>
							<value>netty.server.port=${releaseApiPort}</value>
						</replacement> -->
                    	<replacement>
							<token>release\.version\=.+</token>
							<value>release.version=${releaseVersion}-${releaseEnv}</value>
						</replacement>
                    	<replacement>
							<token>dubbo\.service\.version=.+</token>
							<value>dubbo.service.version=${dubboServiceVersion}-${releaseEnv}</value>
						</replacement>
						<replacement>
							<token>version:version</token>
							<value>version:${releaseVersion}</value>
						</replacement>
					</replacements>
				</configuration>
			</plugin>
```



## SpringBoot命令行运行jar时指定日志位置

```
java -jar -Dserver.port=10000 -Dlogging.path=/var/logs xxx.jar &    -- 默认在/var/logs/生成spring.log文件  
  
java -jar -Dserver.port=10001 -Dlogging.file=/var/logs/yyy.log yyy.jar &  
  
-- 指定虚拟机内存、日志文件、配置文件，启动SpringBoot项目  
java -Xms256m -Xmx512m -jar -Dlogging.file=/var/logs/ebag-school.log LK-school-1.0.jar --spring.config.location=school-prod.yml & 
```



## 常用常量表达式

|                                                              |      |
| ------------------------------------------------------------ | ---- |
| ${project.build.directory}表示主源码路径，缺省为target;      |      |
| ${project.build.sourceEncoding}表示主源码的编码格式;         |      |
| ${project.build.sourceDirectory}表示主源码路径;              |      |
| ${project.build.finalName}表示输出文件名称，缺省为${project.artifactId}-${project.version}; |      |
| ${project.packaging} 打包类型，缺省为jar;                    |      |
| ${project.version}表示项目版本,与${version}相同;             |      |
| ${basedir}表示项目根目录,即包含pom.xml文件的目录;            |      |
