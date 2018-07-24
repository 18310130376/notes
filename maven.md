see：https://blog.csdn.net/column/details/16112.html

https://www.cnblogs.com/homeword/p/7118803.html?utm_source=itdadao&utm_medium=referral

https://www.w3cschool.cn/maven/eljx1hts.html

new：

https://www.cnblogs.com/YatHo/category/1022056.html

#### maven安装

maven依赖于jdk，先检查是否安装jdk

```
[root@wk03 nexus]# java -version
java version "1.8.0_131"
Java(TM) SE Runtime Environment (build 1.8.0_131-b11)
Java HotSpot(TM) 64-Bit Server VM (build 25.131-b11, mixed mode)
```

1 下载地址：

```
http://maven.apache.org/download.cgi 
cd /usr/local
mkdir maven
wget  http://mirrors.cnnic.cn/apache/maven/maven-3/3.3.9/binaries/apache-maven-3.3.9-bin.tar.gz
```

2 将下载到的apache-maven-3.3.9-bin.tar.gz文件上传到/temp目录下，然后切换到root用户下，执行如下命令 ：

linux：

```
tar zxvf apache-maven-3.3.9-bin.tar.gz 
mv /temp/apache-maven-3.3.9 . 

sudo vim /etc/profile  或 sudo vim ~/.bashrc

export M2_HOME=/opt/apache-maven-3.3.9
export PATH={M2_HOME}/bin:$PATH 
source /etc/profile   /  source ~/.bashrc

完整如下：
export JAVA_HOME=/opt/jdk1.8.0_131
export M2_HOME=/opt/apache-maven-3.3.9
export CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar
export PATH=$JAVA_HOME/bin:$M2_HOME/bin:$PAT
```

```
如果环境变量配错，导致大部分命令无法用，则：
export PATH=/usr/bin:/usr/sbin:/bin:/sbin:/usr/X11R6/bin
```

windows：

```
MAVEN_HOME=D:\programFiles\maven-3.3.9
在系统变量中找到 Path的变量，在变量中添加 %MAVEN_HOME%\bin，注意多个值之间用;（注意中英文）隔开，确定即可
```

3：查看maven版本信息

```
mvn -v
```

4：查看环境变量

```
echo %MAVEN_HOME%  // windows
echo $MAVEN_HOME   // linux
env  //显示所有环境变量
unset $MAVEN_HOME #删除环境变量MAVEN_HOME
env|grep MAVEN_HOME #此命令没有输出，证明环境变量MAVEN_HOME已经存在了
```

5：MAVEN_OPTS环境变量

```
运行mvn实际上是执行的Java命令，既然是运行Java，那么运行Java命令可用的参数当然也应该在运行mvn命令时可用，这个时候就需要MAVEN_OPTS环境变量了。通常设置MAVEN_OPTS的值为 –Xms128m –Xmx512m，因为Java默认的最大可用内存往往不能够满足Maven的需要，比如在项目比较大时，使用Maven生成项目站点需要占用大量的内存，则很容易得到java.lang.OutOfMemeryError，因此，一开始就配置这个是推荐的做法。

MAVEN_OPTS=-Xms128m  -Xmx512m
```



#### eclipse配置

**一、安装maven**① 下载地址：maven.apache.org/，解压
② 配置环境变量：新增M2_HOME：E:\maven\apache-maven-3.1.1，添加path：E:\\maven\apache-maven-3.1.1\bin
③ 将maven的conf中的settings.xml文件拷贝出来，放到任意位置，并在文件中指定本地仓库位置

**二、IDE中配置maven**
在MyEclipse或STS中配置maven是一样的：
preference—>maven—>Installations选择解压的maven目录(不用内嵌的maven)
preference—>maven—>User Settings—>选择放在任意位置的settings.xml(该文件从maven/conf里面拷贝出来，修改本地仓库地址)

**三、settings.xml**

配置localRepository， **<localRepository>e:/repository</localRepository>**

如果没有该目录，项目下载jar包时自动创建该目录**，**默认是**.m2/repository**里面

#### **利用Nexus来构建企业级Maven仓库**

**4.1 为什么使用Nexus**

Nexus是Maven仓库管理器，用来搭建一个本地仓库服务器，这样做的好处是便于管理，节省网络资源，速度快，还有一个非常有用的功能就是可以通过项目的SNAPSHOT版本管理，来进行模块间的高效依赖开发。

虽然你可以通过中央仓库来获取你所需要的jar包，但是现实往往是存在很多问题：

问题1
网速慢，你可能需要花很长的时间来下载你所需要的jar

问题2
如果你的公司很大，有几百甚至几千人再用Maven，那么这些人都去通过中央仓库来获取jar，那么这是一个很大的资源浪费。如果存在模块之间的依赖开发，你的snapshot版本是不能够被你的伙伴很方便的获取。在实际开发过程中，有些jar的版本可能在中央仓库里面不存在，或者更新不及时，你是获取不到这个jar的。

总结

所有以上问题通过Nexus这个日益流行的仓库管理器可以轻松的解决。

1 这个仓库是本地的，下载的速度是从远程下载不可比的。

2 可以为你公司所有的Maven使用者服务，可以进行统一管理

3 后面我会介绍如何通过nexus来进行存在模块依赖的项目的开发

4 你可以添加自己的第三方包

**4.2 安装**

Nexus提供了两种安装方式，第一种是内嵌Jetty的bundle，只要你有JRE就能直接运行see：https://blog.csdn.net/nanjing0412/article/details/77149895。第二种方式是WAR，你只须简单的将其发布到web容器中即可使用。

方式一：下载地址:https://www.sonatype.com/download-nexus-repository-trial 选择Choose Your OS，此时下载的是内嵌jetty包。see：https://www.cnblogs.com/kevingrace/p/6201984.html

```
cd /opt/nexus
wget https://sonatype-download.global.ssl.fastly.net/nexus/3/nexus-3.12.1-01-unix.tar.gz
tar -zvxf nexus-3.12.1-01-unix.tar.gz 
启动nexus（默认端口是8081）
/opt/nexus/nexus/bin/nexus

[root@wk01 bin]# ./nexus start
WARNING: ************************************************************
WARNING: Detected execution as "root" user.  This is NOT recommended!
WARNING: ************************************************************
Starting nexus
[root@wk01 bin]#

------------------------------begin  以下为非必须步骤-----------------------------

上面在启动过程中出现告警：不推荐使用root用户启动。这个告警不影响nexus的正常访问和使用。
去掉上面WARNING的办法：
[root@master-node src]# vim /etc/profile
......
export RUN_AS_USER=root
[root@master-node src]# source /etc/profile

------------------------------end  以上为非必须步骤-----------------------------

[root@wk01 bin]# netstat -anp | grep 8081   //nexus服务启动成功后，需要稍等一段时间，8081端口才起来
tcp        0      0 0.0.0.0:8081            0.0.0.0:*               LISTEN      2066/java

此时如果访问不了则设置防火墙开放端口
添加
firewall-cmd --zone=public --add-port=8081/tcp --permanent （--permanent永久生效，没有此参数重启后失效）
重新载入
firewall-cmd --reload
查看
firewall-cmd --zone=public --query-port=8081/tcp

访问nexus，即http://localhost:8081    （如果出现404，就访问http://localhost:8081/nexus）
```

方式二：https://sonatype-download.global.ssl.fastly.net/nexus/oss/nexus-2.6.1-02.war 版本比较老，官方已经不提供新版本的war包下载了。

你可以同过war的方式以web应用的形式发布到你的应用服务器，比如tomcat。你所要做的就是下载war版本的文件，然后放到应用服务器的发布目录即可。

我的版本是nexus-2.2-01，把war包改为 所以访问http://127.0.0.0:8080/nexus

当然我们要操作的话需要登录，默认用户名admin，密码admin123。

**4.3 使用**

mvn:deploy在整合或者发布环境下执行，将最终版本的包拷贝到远程的repository，使得其他的开发者或者工程可以共享。

以将Goldoffice_api-1.0.0-IX-TD-SIT.jar传到nexus中的maven-releases为例

4.3.1  配置settings.xml

因为nexus是需要登陆操作，当然可以通过配置免登陆，这是后话。

在settings.xml的<servers></servers>

```
<server>   
    <id>thirdparty</id>   
    <username>admin</username>
    <password>admin123</password>   
</server>
```

当然如果你要上传包去其他仓库，可依照此例，如

```
<server>   
    <id>maven-releases</id>   
    <username>admin</username>   
    <password>admin123</password>   
</server>
```

如果进行deploy时返回Return code is: 401错误，则需要进行用户验证或者你已经验证的信息有误。

4.3.2  发布包到nexus

mvn deploy:deploy-file -DgroupId=com.abbott -DartifactId=stax-api -Dversion=0.0.1 -Dpackaging=jar -Dfile=C:\Users\789\Desktop\新建文件夹\stax-api-1.0.1.jar  -Durl=http://192.168.48.131:8081/repository/maven-releases/  -DrepositoryId=maven-releases

 

DgroupId和DartifactId构成了该jar包在pom.xml的坐标，项目就是依靠这两个属性定位。自己起名字也行。

Dfile表示需要上传的jar包的绝对路径。

Durl私服上仓库的位置，打开nexus——>repositories菜单，可以看到该路径。

DrepositoryId服务器的表示id，在nexus的configuration可以看到。

Dversion表示版本信息，**怎样得到一个jar包准确的版本呢？**

解压该包，会发现一个叫MANIFEST.MF的文件，这个文件就有描述该包的版本信息。

比如Manifest-Version: 1.0可以知道该包的版本了。

上传成功后，在nexus界面点击maven-releases仓库可以看到这包。或者在browse或者search可以找到。

遇见的问题：

在敲击该命令的时候，有时候看到提示需要POM文件，但上传包是不需要pom文件的

**可能原因**

最大可能是你语句打错了如多了一个空格和换行，这样语句直接截断到换行前面，cmd就找它可以认识的语句执行，比如直接执行mvn，而mvn是对项目打包，是要pom文件的。所以细心的把命令敲击一遍。

**4.4 修改用户密码**

点击左侧菜单栏的Security——>Users，你可以看到系统默认的用户。双击进入详情页面，上方More下拉出现Change password，这样就可以设置新密码了。

**4.5 添加新用户**

步骤同上，用户列表上方Create local user



#### pom.xml简介以及客户端下载包的流程

<repositories>资源地址，所有的依赖包将从次地址下载，其中如果snapshot为资源快照，相对不稳定，而release为稳定版本。

<pluginRepositories> 插件地址，因为maven的所有功能都是使用插件来实现功能的，因此需要从特定的地址下载插件包。

其中**repositories和pluginRepositories就是放私服的地址**，即我们前几讲的nexus的public仓库组地址。

```
<project >

    <modelVersion>4.0.0</modelVersion>
    <groupId>com.xy.company</groupId>
    <artifactId>MavenResource</artifactId>
    <packaging>jar</packaging>
    <version>1.0-SNAPSHOT</version>
    <name>MavenResource</name>
    <url>http://maven.apache.org</url>

<repositories>
    <repository>
     <id>xy-central</id>
     <name>xycentral</name>
     <url>http://localhost:8080/nexus/content/groups/public/</url>
     <releases>
      <enabled>true</enabled>
     </releases>
     <snapshots>
      <enabled>false</enabled>
     </snapshots>
    </repository>
  </repositories>

  <pluginRepositories>
  <pluginRepository>
   <id>xy-central</id>
   <name>xycentral</name>
   <url>http://localhost:8080/nexus/content/groups/public/</url>
   <layout>default</layout>
   <snapshots>
    <enabled>false</enabled>
   </snapshots>
   <releases>
    <updatePolicy>never</updatePolicy>
   </releases>
  </pluginRepository>
 </pluginRepositories>

    <dependencies>
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>3.8.1</version>
            <scope>test</scope>
        </dependency>
    </dependencies>

</project>
```



比如客户端需要一个junit的jar包

第一步：到本地仓库找该jar包，找到结束。没找到下一步。

第二步：到pom配置的私服仓库去找，即pom.xml配置的**repositories**标签。如找到下载到本地仓库并引用。没找到下一步。

第三步：到maven的中央仓库去找，如找到**同时下载到本地仓库和私服的central仓库并引用。**



#### pom.xml或settings.xml对nexus的配置

**一 、在pom中配置Nexus仓库**

```
 <project>
         ...
         <repositories>
            <repository>
                 <id>nexus</id>
                 <name>Nexus</name>
                 <url>http://localhost:8081/nexus/content/groups/public/<url>
                 <release><enabled>true</enabled></release>
                 <snapshots><enabled>true></enabled></snapshots>
             </repository>
        </repositories>
        <pluginRepositories>
             <pluginRepository>
                 <id>nexus</id>
                 <name>Nexus</name>
                 <url>http://localhost:8081/nexus/content/groups/public/<url>
                 <release><enabled>true</enabled></release>
                 <snapshots><enabled>true></enabled></snapshots>
              </pluginRepository>
         </pluginRepositories>
         ...
    </project>
```

上述配置只对当前项目有效，若需让本机所有Maven项目均使用Mavne私服，应该在setting.xml中进行配置

二  、**在setting.xml中配置Nexus仓库**

```
<?xml version="1.0" encoding="UTF-8"?>
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
                      http://maven.apache.org/xsd/settings-1.0.0.xsd">
	 			
	<!--
	<localRepository>C:\Users\charles.so\.m2\r2</localRepository>
	-->
	 
	<profiles>
		<profile>
			<repositories>
				<repository>
					<id>internal</id>
					<name>Archiva Managed Internal Repository</name>
					<url>http://192.168.35.238:8080/repository/internal/</url>
					<releases>
						<enabled>true</enabled>
					</releases>
					<snapshots>
						<enabled>false</enabled>
					</snapshots>
				</repository>
				<repository>
					<id>snapshots</id>
					<name>Archiva Managed Snapshot Repository</name>
					<url>http://192.168.35.238:8080/repository/snapshots/</url>
					<releases>
						<enabled>false</enabled>
					</releases>
					<snapshots>
						<enabled>true</enabled>
					</snapshots>
				</repository>
				<repository>
					<id>jetty</id>
					<name>jetty Repository</name>
					<url>http://oss.sonatype.org/content/groups/jetty/</url>
					<releases>
						<enabled>true</enabled>
					</releases>
					<snapshots>
						<enabled>false</enabled>
					</snapshots>
				</repository>
			</repositories>
			<pluginRepositories>
				<pluginRepository>
					<id>plugin_internal</id>
					<name>Archiva Managed Internal Repository</name>
					<url>http://192.168.35.238:8080/repository/internal/</url>
					<releases>
						<enabled>true</enabled>
					</releases>
					<snapshots>
						<enabled>false</enabled>
					</snapshots>
				</pluginRepository>
				<pluginRepository>
					<id>plugin_snapshots</id>
					<name>Archiva Managed Snapshot Repository</name>
					<url>http://192.168.35.238:8080/repository/snapshots/</url>
					<releases>
						<enabled>false</enabled>
					</releases>
					<snapshots>
						<enabled>true</enabled>
						<updatePolicy>always</updatePolicy>
					</snapshots>
				</pluginRepository>
			</pluginRepositories>
			<id>archiva</id>    <!--------------与下面activeProfile一致--->
		</profile>
	</profiles>
	<servers> 
		<server> 
			<id>internal</id> 
			<username>developer</username> 
			<password>password123</password> 
		</server> 
		<server> 
			<id>snapshots</id> 
			<username>developer</username> 
			<password>password123</password> 
		</server> 
		<server>   
			<id>maven-releases</id>   
			<username>admin</username>   
			<password>admin123</password>   
		</server>
	</servers>
	<activeProfiles>
		<activeProfile>archiva</activeProfile>
	</activeProfiles>
</settings>
```

activeProfiles用来激活。 



三、**配置镜像让Maven只使用私服**

```
  <settings>
          ...
          <mirrors>
              <mirror>
                  <id>nexus</id>
                  <mirrorOf>*<?mirrorOf>
                  <url>http://localhost:8081/nexus/content/groups/public/</url>
              </mirror>
          </mirrors>
          <profiles>
                <profile>
                    <id>nexus</id>
                    <repositories>
                        <repository>
                          <id>central</id>
                          <name>http://central</name>                        
                          <release><enabled>true</enabled></release>
                          <snapshots><enabled>true></enabled></snapshots>
                        </repository>
                     </repositories>
                     <pluginRepositories>
                        <pluginRepository>
                            <id>central</id>
                            <name>http://central</name>                          
                            <release><enabled>true</enabled></release>
                            <snapshots><enabled>true></enabled></snapshots>
                       </pluginRepository>
                     </pluginRepositories>
                </profile>
           </profiles>
           <activeProfiles>
               <activeProfile>nexus</activeProfiles>
           </activaProfiles>
     </settings>
```

只要mirrorOf中的工程需要下载jar，都会自动来找该镜像。如果镜像地址有，就下载下来。若镜像地址没有，mirrorOf中的工厂也不会到中央资源库下载，而是由镜像去下载。这是推荐的做法。若镜像下载不到，就下载失败。

#### maven常用命令

https://blog.csdn.net/u011280083/article/details/78787610

-D 传入属性参数 
-P 使用pom中指定的配置 
-e 显示maven运行出错的信息 
-o 离线执行命令,即不去远程仓库更新包 
-X 显示maven允许的debug信息 
-U 强制去远程参考更新snapshot包 
例如 mvn install -Dmaven.test.skip=true -Poracle 
其他参数可以通过mvn help 获取

mvn clean package -e    查看详细错误信息

```
mvn -version/-v  显示版本信息
mvn package && java -jar target/gs-spring-boot-docker-0.1.0.jar  //打包并且运行
mvn package docker:build -DpushImage
mvn dependency:list   查看当前项目已被解析的依赖
mvn dependency:tree  //查看依赖树
mvn dependency:analyze  //分析项目依赖
mvn clean install //将生成的jar包安装到本地仓库  包含mvn compile，mvn package，然后上传到本地仓库
mvn clean install -U  //强制让mvn检查更新
mvn compile //编译源代码
mvn test-compile //编译测试代码
mvn test  //运行测试
mvn clean  清理项目生产的临时文件,一般是模块下的target目录
mvn eclipse:eclipse  生成eclipse项目
mvn idea:idea   生成idea项目
mvn -Dtest package 组合使用goal命令，如只打包不测试
mvn jar:jar  只打jar包
mvn eclipse:clean   清除eclipse的一些系统设置
mvn deploy 上传到私服    包含mvn install,然后，上传到私服
mvn archetype:create -DgroupId=com.oreilly -DartifactId=my-app    创建mvn项目
mvn jetty:run 
mvn -e   显示详细错误 信息
mvn validate 验证工程是否正确，所有需要的资源是否可用
mvn verify 运行任何检查，验证包是否有效且达到质量标准
mvn install -Dmaven.test.skip=true   给任何目标添加maven.test.skip 属性就能跳过测试 
mvn eclipse:eclipse  将项目转化为Eclipse项目（生成.project和.classpath文件）
mvn eclipse:clean

mvn exec命令可以执行项目中的main函数 :
首先需要编译java工程：mvn compile
不存在参数的情况下：mvn exec:java -Dexec.mainClass="***.Main"
存在参数：mvn exec:java -Dexec.mainClass="***.Main" -Dexec.args="arg0 arg1 arg2"
指定运行时库：mvn exec:java -Dexec.mainClass="***.Main" -Dexec.classpathScope=runtime

发布第三方Jar到本地库中
mvn install:install-file -DgroupId=com -DartifactId=client -Dversion=0.1.0 -Dpackaging=jar -Dfile=d:\client-0.1.0.jar


mvnDebug tomcat:run
mvn tomcat:run

----------------------------------更新版本-------------------------------

mvn versions:set -DgenerateBackupPoms=false -DnewVersion=1.0.2

----------------------------------传参数---------------------------------

mvn -DpropertyName=propertyValue clean package
如果propertyName不存在pom.xml，它将被设置。
如果propertyName已经存在pom.xml，其值将被作为参数传递的值覆盖-D
要发送多个变量，请使用多个空格分隔符加-D
mvn -DpropA=valueA -DpropB=valueB -DpropC=valueC clean package

例：

如果你的pom.xml如下：

<properties>
    <theme>myDefaultTheme</theme>
</properties>
那么在这个执行过程中mvn -Dtheme=halloween clean package会覆盖theme的值，具有如下效果：

<properties>
    <theme>halloween</theme>
</properties>


-----------------------------------profile-----------------------

<profile>
   <id>test</id>
   <activation>
      <property>
         <name>env</name>
         <value>test</value>
      </property>
   </activation>
   ...
</profile>
mvn test -Ptest
mvn test -Penv=test
```



假设两台机器A，B部署同样的项目，发布时只需要在一台机器上A上deploy，此时A的target有了jar，执行运行。远程仓库有了jar，给第三方提供依赖。此时部署B时只需要mvn clean install 在B上打同样的jar包就可以，不用deploy到远程仓库了。



#### maven多模块打包

首先切换到工程的根目录

单独构建模块jsoft-web，同时会构建jsoft-web模块依赖的其他模块

mvn install -pl jsoft-web -am
单独构建模块jsoft-common，同时构建依赖模块jsoft-common的其他模块 

mvn install -pl jsoft-common -am -amd


按照上面的配置好以后，执行下面的命令就好了

  mvn clean package
1
但是如果使用了多个模块，上面的命令是会吧全部的模块都执行打包的，如果只是打包某个模块的话，可以用

  mvn -pl A -am install



#### maven打包 install package deploy区别

mvn clean package依次执行了clean、resources、compile、testResources、testCompile、test、jar(打包)等７个阶段。

mvn clean install依次执行了clean、resources、compile、testResources、testCompile、test、jar(打包)、install等8个阶段。

mvn clean deploy依次执行了clean、resources、compile、testResources、testCompile、test、jar(打包)、install、deploy等９个阶段。



`maven package`：打包到本项目，一般是在项目target目录下。如果a项目依赖于b项目，打包b项目时，只会打包到b项目下target下，编译a项目时就会报错 `没有布署到本地maven仓库和远程maven私服仓库`

`maven install`：打包到本地仓库，如果没有设置过maven本地仓库，一般在用户/.m2目录下。如果a项目依赖于b项目，那么install b时，会在本地仓库同时生成pom文件和jar文件，此时package A项目 会下载依赖的B项目。并且`把打好的可执行jar包布署到本地maven仓库，但没有布署到远程maven私服仓库`

`maven deploy`：打包上传到远程仓库，如：私服nexus等，需要配置pom文件,命令完成了项目编译、单元测试、打包功能，`同时把打好的可执行jar包（war包或其它形式的包）布署到本地maven仓库和远程maven私服仓库`



#### 多模块

https://blog.csdn.net/liyanlei5858/article/details/79047884

多模块打包

https://blog.csdn.net/Ser_Bad/article/details/78433340

第一步：eclipse创建maven project

第二步：选择父项目右键选择创建maven module项目

第三步：修改父项目：

```
<packaging>jar</packaging>修改为<packaging>pom</packaging>
```

查看父项目有

```
<modules>
    <module>system-domain</module>
</modules>
```

第四步：修改子项目

```
把<groupId>me.gacl</groupId>和<version>1.0-SNAPSHOT</version>去掉，因为groupId和version会继承system-parent中的groupId和version，，加上<packaging>jar</packaging>
```

同时子模块里

```
<parent>
  <groupId>me.gacl</groupId>
  <artifactId>system-parent</artifactId>
  <version>1.0-SNAPSHOT</version>
</parent>
```

第五步：假设另外一个子模块C 模块需要依赖上面的子模块B，则在C的pom里配置

```
<dependencies>
      <dependency>
       <groupId>me.gacl</groupId>
       <artifactId>system-B</artifactId>
       <version>${project.version}</version>
     </dependency>
</dependencies>
```

第六步：假设此时子模块D需要依赖模块B和模块C，此时D模块里只需要引入C即可，因为C已经依赖了B



#### Maven私服Nexus3.x环境构建操作记录

**Maven介绍**
Apache Maven是一个创新的软件项目管理和综合工具。
Maven提供了一个基于项目对象模型（POM）文件的新概念来管理项目的构建，可以从一个中心资料片管理项目构建，报告和文件。
Maven最强大的功能就是能够自动下载项目依赖库。
Maven提供了开发人员构建一个完整的生命周期框架。开发团队可以自动完成项目的基础工具建设，Maven使用标准的目录结构和默认构建生命周期。
在多个开发团队环境时，Maven可以设置按标准在非常短的时间里完成配置工作。由于大部分项目的设置都很简单，并且可重复使用，Maven让开发人员的工作更轻松，同时创建报表，检查，构建和测试自动化设置。
Maven项目的结构和内容在一个XML文件中声明，pom.xml 项目对象模型（POM），这是整个Maven系统的基本单元。

Maven提供了开发人员的方式来管理：
1）Builds
2）Documentation
3）Reporting
4）Dependencies
5）SCMs
6）Releases
7）Distribution
8）mailing list
概括地说，Maven简化和标准化项目建设过程。处理编译，分配，文档，团队协作和其他任务的无缝连接。 
Maven增加可重用性并负责建立相关的任务。
Maven最初设计，是以简化Jakarta Turbine项目的建设。在几个项目，每个项目包含了不同的Ant构建文件。 JAR检查到CVS。
Apache组织开发Maven可以建立多个项目，发布项目信息，项目部署，在几个项目中JAR文件提供团队合作和帮助。

Maven主要目标是提供给开发人员：
1）项目是可重复使用，易维护，更容易理解的一个综合模型。
2）插件或交互的工具，这种声明性的模式。

**私服介绍**
私服是指私有服务器，是架设在局域网的一种特殊的远程仓库，目的是代理远程仓库及部署第三方构建。有了私服之后，当 Maven 需要下载构件时，直接请求私服，私服上存在则下载到本地仓库；否则，私服请求外部的远程仓库，将构件下载到私服，再提供给本地仓库下载。

![img](https://images2015.cnblogs.com/blog/907596/201612/907596-20161220162726057-620806393.png)

**Nexus介绍**
Nexus是一个强大的Maven仓库管理器，它极大地简化了本地内部仓库的维护和外部仓库的访问。
如果使用了公共的Maven仓库服务器，可以从Maven中央仓库下载所需要的构件（Artifact），但这通常不是一个好的做法。
正常做法是在本地架设一个Maven仓库服务器，即利用Nexus私服可以只在一个地方就能够完全控制访问和部署在你所维护仓库中的每个Artifact。
Nexus在代理远程仓库的同时维护本地仓库，以降低中央仓库的负荷,节省外网带宽和时间，Nexus私服就可以满足这样的需要。
Nexus是一套“开箱即用”的系统不需要数据库，它使用文件系统加Lucene来组织数据。 
Nexus使用ExtJS来开发界面，利用Restlet来提供完整的REST APIs，通过m2eclipse与Eclipse集成使用。 
Nexus支持WebDAV与LDAP安全身份认证。 
Nexus还提供了强大的仓库管理功能，构件搜索功能，它基于REST，友好的UI是一个extjs的REST客户端，它占用较少的内存，基于简单文件系统而非数据库。

为什么要构建Nexus私服？
如果没有Nexus私服，我们所需的所有构件都需要通过maven的中央仓库和第三方的Maven仓库下载到本地，而一个团队中的所有人都重复的从maven仓库下载构件无疑加大了仓库的负载和浪费了外网带宽，如果网速慢的话，还会影响项目的进程。很多情况下项目的开发都是在内网进行的，连接不到maven仓库怎么办呢？开发的公共构件怎么让其它项目使用？这个时候我们不得不为自己的团队搭建属于自己的maven私服，这样既节省了网络带宽也会加速项目搭建的进程，当然前提条件就是你的私服中拥有项目所需的所有构件。

总之，在本地构建nexus私服的好处有：
1）加速构建；
2）节省带宽；
3）节省中央maven仓库的带宽；
4）稳定（应付一旦中央服务器出问题的情况）；
5）控制和审计；
6）能够部署第三方构件；
7）可以建立本地内部仓库；
8）可以建立公共仓库
这些优点使得Nexus日趋成为最流行的Maven仓库管理器。

接着配置系统环境变量，在/etc/profile文件底部添加如下内容：
[root@master-node src]# java -version
openjdk version "1.8.0_111"
OpenJDK Runtime Environment (build 1.8.0_111-b15)
OpenJDK 64-Bit Server VM (build 25.111-b15, mixed mode)
[root@master-node src]# vim /etc/profile
.....
export JAVA_HOME=/usr/lib/jvm/java-1.8.0-openjdk                                //java的环境变量设置
export CLASSPATH=.:$JAVA_HOME/jre/lib/rt.jar:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar
export PATH=$PATH:$JAVA_HOME/bin

export MAVEN_HOME=/usr/local/maven                                                 //maven的环境变量设置
export PATH=$PATH:$MAVEN_HOME/bin
[root@master-node src]# source /etc/profile

最后验证是否安装成功，出现如下信息，说明安装成功
[root@master-node src]# mvn --version                   # 最好按照java jdk
Apache Maven 3.3.9 (bb52d8502b132ec0a5a3f4c09453c07478323dc5; 2015-11-11T00:41:47+08:00)
Maven home: /usr/local/maven
Java version: 1.8.0_111, vendor: Oracle Corporation
Java home: /usr/lib/jvm/java-1.8.0-openjdk-1.8.0.111-2.b15.el7_3.x86_64/jre
Default locale: en_US, platform encoding: UTF-8
OS name: "linux", version: "3.10.0-327.el7.x86_64", arch: "amd64", family: "unix"

**Nexus安装**
Nexus的安装有两种实现方式：
1）war包安装方式
下载地址：<https://sonatype-download.global.ssl.fastly.net/nexus/oss/nexus-2.14.2-01.war>
直接将war包放在tomcat的根目录下，启动tomcat就可以用了

在部署机上的iptables里打开8081端口
[root@master-node src]# vim /etc/sysconfig/iptables
....
-A INPUT -p tcp -m state --state NEW -m tcp --dport 8081 -j ACCEPT
[root@master-node src]# /etc/init.d/iptables restart

访问nexus，即http://localhost:8081    （如果出现404，就访问http://localhost:8081/nexus）

![img](https://images2015.cnblogs.com/blog/907596/201612/907596-20161220163017714-1339327062.png)

出现上述页面，说明配置nexus成功！
点击右上角“Log in”，
输入默认用户名(admin)和默认密码（admin123）登录

可以点击上面的“设置”图标，在“设置”里可以添加用户、角色，对接LDAP等的设置，如下：

![img](https://images2015.cnblogs.com/blog/907596/201612/907596-20161220154712776-67407111.png)

![img](https://images2015.cnblogs.com/blog/907596/201612/907596-20161220154756276-1502291718.png)

可以在“管理”里查看nexus的系统信息

![img](https://images2015.cnblogs.com/blog/907596/201612/907596-20161220160738011-558914286.png)

Nexus仓库分类的概念：
1）Maven可直接从宿主仓库下载构件,也可以从代理仓库下载构件,而代理仓库间接的从远程仓库下载并缓存构件 
2）为了方便,Maven可以从仓库组下载构件,而仓库组并没有时间的内容(下图中用虚线表示,它会转向包含的宿主仓库或者代理仓库获得实际构件的内容).

![img](https://images2015.cnblogs.com/blog/907596/201612/907596-20161220164452667-1588016524.png)

 

**Nexus的web界面功能介绍**

**1.Browse Server Content**

![img](https://images2015.cnblogs.com/blog/907596/201612/907596-20161221105344026-1095529496.png)

1.1  Search
这个就是类似Maven仓库上的搜索功能，就是从私服上查找是否有哪些包。
注意：
1）在Search这级是支持模糊搜索的，如图所示：

![img](https://images2015.cnblogs.com/blog/907596/201612/907596-20161221105547167-2017264888.png)

2）如果进入具体的目录，好像不支持模糊搜索，如图所示：

![img](https://images2015.cnblogs.com/blog/907596/201612/907596-20161221105634245-19314456.png)

1.2  Browse

![img](https://images2015.cnblogs.com/blog/907596/201612/907596-20161221105808261-1531078817.png)

1）Assets
这是能看到所有的资源，包含Jar，已经对Jar的一些描述信息。
2）Components
这里只能看到Jar包。

**2.Server Adminstration And configuration**

看到这个选项的前提是要进行登录的，如上面已经介绍登陆方法，右上角点击“Sign In”的登录按钮，输入admin/admin123,登录成功之后，即可看到此功能，如图所示：

![img](https://images2015.cnblogs.com/blog/907596/201612/907596-20161221110207479-318443915.png)

2.1 Blob Stores
文件存储的地方，创建一个目录的话，对应文件系统的一个目录，如图所示：

![img](https://images2015.cnblogs.com/blog/907596/201612/907596-20161221110317932-1418604671.png)

2.2 Repositories

![img](https://images2015.cnblogs.com/blog/907596/201612/907596-20161221110524073-153249455.png)

1）Proxy
这里就是代理的意思，代理中央Maven仓库，当PC访问中央库的时候，先通过Proxy下载到Nexus仓库，然后再从Nexus仓库下载到PC本地。
这样的优势只要其中一个人从中央库下来了，以后大家都是从Nexus私服上进行下来，私服一般部署在内网，这样大大节约的宽带。
创建Proxy的具体步骤
1--点击“Create Repositories”按钮

![img](https://images2015.cnblogs.com/blog/907596/201612/907596-20161221110640089-1389328954.png)

2--选择要创建的类型

![img](https://images2015.cnblogs.com/blog/907596/201612/907596-20161221110758542-1042948386.png)

3--填写详细信息
Name：就是为代理起个名字
Remote Storage: 代理的地址，Maven的地址为: https://repo1.maven.org/maven2/
Blob Store: 选择代理下载包的存放路径

![img](https://images2015.cnblogs.com/blog/907596/201612/907596-20161221111233651-1321037653.png)

2）Hosted
Hosted是宿主机的意思，就是怎么把第三方的Jar放到私服上。
Hosted有三种方式，Releases、SNAPSHOT、Mixed
Releases: 一般是已经发布的Jar包
Snapshot: 未发布的版本
Mixed：混合的
Hosted的创建和Proxy是一致的，具体步骤和上面基本一致。如下：

![img](https://images2015.cnblogs.com/blog/907596/201612/907596-20161221111325104-723208432.png)

![img](https://images2015.cnblogs.com/blog/907596/201612/907596-20161221111344573-1949446719.png)

![img](https://images2015.cnblogs.com/blog/907596/201612/907596-20161221111419870-2005652543.png)

**注意事项：**
Deployment Pollcy: 需要把策略改成“Allow redeploy”。

![img](https://images2015.cnblogs.com/blog/907596/201612/907596-20161221111455589-1303191395.png)

3）Group
能把两个仓库合成一个仓库来使用，目前没使用过，所以没做详细的研究。

2.3 Security
这里主要是用户、角色、权限的配置（上面已经提到了在这里添加用户和角色等）

![img](https://images2015.cnblogs.com/blog/907596/201612/907596-20161221111747745-1991046629.png)

2.4 Support
包含日志及数据分析。

![img](https://images2015.cnblogs.com/blog/907596/201612/907596-20161221111920261-269936853.png)

2.5 System
主要是邮件服务器，调度的设置地方
这部分主要讲怎么和Maven做集成,集成的方式主要分以下种情况：代理中央仓库、Snapshot包的管理、Release包的管理、第三方Jar上传到Nexus上。

**代理中央仓库**
只要在PMO文件中配置私服的地址（比如http://192.168.1.14:8081）即可，配置如下：

**Snapshot包的管理**
1）修改Maven的settings.xml文件，加入认证机制

2）修改工程的Pom文件

**注意事项:**

![img](https://images2015.cnblogs.com/blog/907596/201612/907596-20161221112731354-1284881495.png)

上面修改的Pom文件如截图中的名字要跟/usr/local/maven/conf/settings.xml文件中的名字一定要对应上。

3）上传到Nexus上

1--项目编译成的jar是Snapshot(POM文件的头部)

2--使用mvn deploy命令运行即可（运行结果在此略过）

3--因为Snapshot是快照版本，默认他每次会把Jar加一个时间戳，做为历史备份版本。

**Releases包的管理**

1）与Snapshot大同小异，只是上传到私服上的Jar包不会自动带时间戳
2）与Snapshot配置不同的地方，就是工程的PMO文件，加入repository配置

3）打包的时候需要把Snapshot去掉

**第三方Jar上传到Nexus**

[root@master-node src]# mvn deploy:deploy-file -DgroupId=org.jasig.cas.client -DartifactId=cas-client-core -Dversion=3.1.3 -Dpackag
**注意事项：**
-DrepositoryId=nexus 对应的就是Maven中settings.xml的认证配的名字。



#### 插件

http://www.cnblogs.com/wade-luffy/p/7080280.html



#### POM文件使用技巧

①dependencyManagement

是用于帮助管理chidren的dependencies的。例如如果parent使用dependencyManagement定义了一个dependencyon junit:junit4.0,那么 它的children就可以只引用 groupId和artifactId,而version就可以通过parent来设置，这样的好处就是可以集中管理 依赖的详情

②profiles

主要是针对环境配置的切换

③finalName

指定去掉后缀的工程名字，例如：默认为`${artifactId}-${version}`,即是打包的包名，不包含后缀。

④多环境filters

如，在 spring.xml 中要配置上传文件的路径

```
<beans>
  <bean id="uploadService" class="com.oist.project.service.UploadServiceImpl">
    <property name="uploadDir" value="${spring.uploadDir}"/>
  </bean>
</beans>
```

在 pom.xml 中进行以下配置

```
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
                             http://maven.apache.org/xsd/maven-4.0.0.xsd">
  ...
  <build>
    <filters> <!-- 指定 filter -->
    <!--${profiles.active}是profiles里指定激活环境的，mvn构建时指定环境，则是指定环境的-->
      <filter>src/main/filters/${profiles.active}.properties</filter>
    </filters>
    <resources>
      <resource>
      <!--指定build资源到哪个目录，默认是base directory-->
      <targetPath>META-INF/plexus</targetPath> 
      <!-- spring.xml 应该在 src/main/resource 目录下 -->
        <directory>src/main/resources</directory>
        <filtering>true</filtering> <!-- 是否使用过滤器 -->
          <includes> 
          <include>configuration.xml</include> 
        </includes> 
        <excludes> 
          <exclude>**/*.properties</exclude> 
        </excludes> 
      </resource>
    </resources>
  </build>

  <profiles>
    <profile>
      <id>development</id>
      <activation>
        <activeByDefault>true</activeByDefault>
      </activation>
      <propertys>
        <profiles.active>develop</deploy.env>
      </propertys>
    </profile>
    
    <profile>
      <id>production</id>
      <propertys>
        <profiles.active>production</deploy.env>
      </propertys>
    </profile>
  </profiles>
</project>
```

src/main/filters/develop.properties 文件

```
# 上传路径：
spring.uploadDir=c:/uploadDir
```

src/main/filters/test.properties 文件

```
# 上传路径：
spring.uploadDir=/tmp/upload_dir
```

src/main/filters/production.properties 文件

```
# 上传路径：
spring.uploadDir=/app/project/upload_dir
```

如果配置了多个 filter，并且两个 filter 中有相同的 key，则后面的 value 为最终取值。

```
<build>
  <filters>
    <filter>src/main/filters/production.properties</filter>
    <filter>src/main/filters/test.properties</filter>
  </filters>
</build>
```

```
mvn clean compile war:war -Pproduction
```



总结：上面是通过filter路径的文件的属性值，替换directory 节点下resources路径下的文件中的占位符。

上面的profile节点里也可以有peoperties节点，用来配置key value不推荐。



⑤pluginManagement 放在父模块pom然后让子pom来决定是否引用 

父模块pom

```
<pluginManagement> 

      <plugins> 
        <plugin> 
          <groupId>org.apache.maven.plugins</groupId> 
          <artifactId>maven-jar-plugin</artifactId> 
          <version>2.2</version> 
          <executions> 
            <execution> 
              <id>pre-process-classes</id> 
              <phase>compile</phase> 
              <goals> 
                <goal>jar</goal> 
              </goals> 
              <configuration> 
                <classifier>pre-process</classifier> 
              </configuration> 
            </execution> 
          </executions> 
        </plugin> 
      </plugins> 
</pluginManagement>
```

子模块pom

```
 <build>
 <plugins> 
      <plugin> 
        <groupId>org.apache.maven.plugins</groupId> 
        <artifactId>maven-jar-plugin</artifactId> 
      </plugin> 
</plugins>
</build>
```





see：https://blog.csdn.net/fengchao2016/article/details/72726101/

​          https://www.cnblogs.com/0201zcr/p/6262762.html 写的不对



0718截止;https://www.cnblogs.com/qq78292959/p/3711501.html



#### Maven引入第三方包的方案

如果自己写的包或者非开源的包需要引入，有三种方案

1.本地安装这个插件install plugin

例如：mvn install:intall-file -Dfile=non-maven-proj.jar -DgroupId=som.group -DartifactId=non-maven-proj -Dversion=1

2.创建自己的repositories并且部署这个包，使用类似上面的deploy:deploy-file命令，

3.设置scope为system,并且指定系统路径。但是打包时默认不会打进去，需要插件支持。

例如依赖了 `${project.basedir}/lib` 目录下的 jar 包 

```
<dependency>
            <groupId>com.dingtalk.open</groupId>
            <artifactId>client-sdk.api</artifactId>
            <version>1.0.2</version>
            <scope>system</scope>
            <systemPath>${project.basedir}/lib/client-sdk.api-1.0.2.jar</systemPath>
</dependency>
```

打包时, system 依赖的 jar 包默认不会打到 target 中, 所以需要 maven-dependency-plugin 

```
<plugin>
       <groupId>org.apache.maven.plugins</groupId>
       <artifactId>maven-dependency-plugin</artifactId>
       <version>2.10</version>
       <executions>
           <execution>
               <id>copy-dependencies</id>
               <phase>compile</phase>
               <goals>
                   <goal>copy-dependencies</goal>
               </goals>
               <configuration>
                   <outputDirectory>${project.build.directory}/${project.build.finalName}/WEB-INF/lib</outputDirectory>
                   <includeScope>system</includeScope>
               </configuration>
           </execution>
       </executions>
</plugin>
```

也可以使用 maven-war-plugin 

```
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-war-plugin</artifactId>
    <version>2.3</version>
    <configuration>
        <warName>${project.artifactId}</warName>
        <webResources>
            <resource>
                <directory>lib/</directory>
                <targetPath>WEB-INF/lib</targetPath>
                <includes>
                    <include>**/*.jar</include>
                </includes>
            </resource>
        </webResources>                    
    </configuration>
</plugin>
```



#### 国内的maven镜像站

在 `~/.m2` 目录下的 `settings.xml` 文件中增加 ，目的：本地--私服--镜像站（加速）--maven中央仓库

```
<mirrors>
<mirror>  
  <id>alimaven</id>  
  <name>aliyun maven</name>  
  <url>http://maven.aliyun.com/nexus/content/groups/public/</url>  
  <mirrorOf>central</mirrorOf>          
</mirror>
</mirrors>
```

#### 设置HTTP代理

修改settings.xml,添加proxies元素：可以声明多个proxy,第一个被激活的proxy会生效 

```
<settings>
  ...
  <proxies>
    <proxy>
      <id>my-proxy</id>
      <active>true</active>
      <protocol>http</protocol>
      <host>1.1.1.1</host>
      <port>2222</port>
      <!--
      <username>***</username>
      <password>***</password>
      <nonProxyHosts>repository.mycom.com|*.google.com</nonProxyHosts>
      -->
    </proxy>
  </proxies>
  ...
</settings>
```

#### 配置用户范围的 settings.xml

将 $M2_HOME/conf/settings.xml 复制到~/.m2/目录下修改 

```
mkdir ~/.m2;
cp $M2_HOME/conf/settings.xml ~/.m2/ 
```

一个是全局的,一个是用户级的,还有就是不担心升级带来的配置更改 

#### 仓库

本地仓库和远程仓库
远程仓库又包括中央仓库、私服、其他公共库

##### 本地仓库

本地仓库在~/.m2/reposity/
可以通过修改settings文件：

```
<localRepository>D:\mavenrepo\</localRepository>
```

执行下面的命令 会将生成的jar包安装到本地仓库

```
mvn clean install
```

##### 中央仓库

在 $M2_HONE/lib/maven-x.y.z-uber.jar 中的 org/apache/maven/model/pom-4.0.0.xml 中
有一个超级POM文件,其中定义了中央仓库的位置,
其中<snapshots>属性是false,表示不从中央仓库下载快照版本

##### 私服

即使是个人机器也应该建立私服,好处多多:
加速构建、
节省带宽、
部署第三方构件、
降低中央仓库的负荷

##### 远程仓库的配置

在项目的pom文件里声明，只针对当前项目有效，应该在setting.xml配置

```
<repositories>
      <repository>
        <id>xx</id>
        <name>xx</name>
        <url>xx</url>
        <releases>true|false</release>
        <enabled>true | false </release>
        <updatePolicy>daily| never | always | interval:X分钟 </updatePolicy>
        <checksumPolicy>ignore | warn | fail </checksumPolicy>
        <snapshots>true|false</release>
      </repository>
</repositories>
```

##### 远程仓库的认证

认证信息必须在settings.xml中,

```
<servers>
      <server>
        <id>my-proj</id>
        <username>repo-user</username>
        <password>repo-pwd</password>
      </server>
</servers>
```

##### 部署到远程仓库

需要配置pom.xml文件,配置 distributionManagement 元素,
如果需要认证,同上面一样

```
<distributionManagement>
     <repository>
        <id>xx</id>
        <name>xx</name>
        <url>xx</url>
     </repository>
     <snapshotRepository>
        <id>xx</id>
        <name>xx</name>
        <url>xx</url>
     </snapshotRepository>
</distributionManagement>
```

##### 镜像

mirror相当于一个拦截器，它会拦截maven对remote repository的相关请求，把请求里的remote repository地址，重定向到mirror里配置的地址



流程：先从repositories配置的私有仓库下载①，下载不到就到maven配置的默认仓库(public)②，但是如果配置了mirrorOf=center或者mirrorOf=*的mirrors③，则会把②的请求拦截转向③配置的url（③是②的一个镜像）

多个mirrors按顺序请求（Apache Maven 3.3.9）。

1、在mirrorOf与repositoryId相同的时候优先是使用mirror的地址

2、mirrorOf等于*的时候覆盖所有repository配置（控制台直接请求mirror）

3、存在多个mirror配置的时候mirrorOf等于*放到最后（控制台直接请求mirror）

4、只配置mirrorOf为central的时候可以不用配置repository



在settings.xml中配置

```
<mirrors>
        <!-- 阿里云仓库 -->
        <mirror>
            <id>alimaven</id>
            <mirrorOf>central</mirrorOf>
            <name>aliyun maven</name>
            <url>http://maven.aliyun.com/nexus/content/repositories/central/</url>
        </mirror>
    
        <!-- 中央仓库1 -->
        <mirror>
            <id>repo1</id>
            <mirrorOf>central</mirrorOf>
            <name>repo1</name>
            <url>http://repo1.maven.org/maven2/</url>
        </mirror>
    
        <!-- 中央仓库2 -->
        <mirror>
            <id>repo2</id>
            <mirrorOf>central</mirrorOf>
            <name>repo2</name>
            <url>http://repo2.maven.org/maven2/</url>
        </mirror>
</mirrors>
```

- 可以使用*,还可以使用 external:*表示匹配所有远程仓库
- 还可以 *,!repo1表示匹配所有仓库,repo1除外

镜像仓库会屏蔽被镜像仓库,因此要保证镜像仓库的稳定性



##### 配置梳理

一  配置Maven 从私服上下载构件

pom的repositories配置只针对当前项目有效，如果需要全部项目使用就有了下面的配置在setting.xml里

```
<profiles>
	<profile>
		<id>nexus</id>
		<repositories>
			<repository>
				<id>nexus</id>
				<name>Nexus</name>
				<url>http://192.168.53.55:8081/nexus/content/groups/public/</url>
				<releases>
					<enabled>true</enabled>
				</releases>
				<snapshots>
					<enabled>true</enabled>
				</snapshots>
			</repository>
		</repositories>
		<pluginRepositories>
			<pluginRepository>
				<id>nexus</id>
				<name>Nexus</name>
				<url>http://192.168.53.55:8081/nexus/content/groups/public/</url>
				<releases>
					<enabled>true</enabled>
				</releases>
				<snapshots>
					<enabled>true</enabled>
				</snapshots>
			</pluginRepository>
		</pluginRepositories>
	</profile>
</profiles>
<activeProfiles>
<activeProfile>nexus</activeProfile>
```

二  配置自动发布构件到私服

POM.XML 配置

```
<distributionManagement>
	  <repository>
	    <id>releases</id>
	    <url>http://localhost:8081/nexus/content/repositories/thirdparty/</url>
	  </repository>
</distributionManagement>
```

在命令行键入：mavn  deploy 则构件自动发布到本地和上传到私服 <http://localhost:8081/nexus/content/repositories/thirdparty> 这个目录下

需要注意2点：

1、发布的版本类型必须和nexus里的Policy类型一致。

2、setting.xml 文件必须配置servers，其中id必须和repository下的id一致。

```
<servers>
		<server>
			<id>releases</id>
			<username>admin</username>
			<password>admin123</password>
		</server>
		<server>
			<id>snapshots</id>
			<username>admin</username>
			<password>admin123</password>
		</server>
		<server>
			<id>deploymentRepo</id>
			<username>admin</username>
			<password>admin123</password>
		</server>
</servers>
```



##### 常见问题

- 如果出现400，需要注意项目下的pom.xml文件和maven使用的setting.xml文件的配置是否一致。
- 如果出现401，需要检查maven使用的setting.xml中的帐号和密码是否正确，相应的repository是否为“Allow Redeploy”。
- 如果使用的intellij、eclipse或myeclipse需要注意ide中使用的setting.xml和maven命令行下的setting.xml是否一致；否则或出现许多莫名其妙的问题

在项目的顶层pom上执行
mvn versions:set -DnewVersion=1.0.1-SNAPSHOT

mvn versions:revert 退回

这样就可以改变整个项目的版本号了。他会自动更改引用关系的（子模块更改parent节点的version）。（子项目没有version节点）

以下是打包名称：Goldoffice_api-1.0.0-IX-TD-SIT.jar

```
<finalName>{project.artifactId}-{releaseVersion}-${releaseEnv}</finalName>
```

project.artifactId：Goldoffice_api，pom定义

releaseVersion：properties定义

releaseEnv：mvn clean install -Dmaven.test.skip=true -DreleaseEnv=${env}传入

```
git checkout .
git pull
env=${1-DEV}
dos2unix ./version_${env}.properties
. ./version_${env}.properties
sed -i "s/<trade.version>.*<\/trade.version>/<trade.version>${tradeVersion}<\/trade.version>/g" pom.xml
mvn versions:set -DgenerateBackupPoms=false -DnewVersion=${releaseVersion}-${env}-SNAPSHOT
mvn --update-snapshots clean deploy -Dmaven.test.skip=true -DreleaseEnv=${env}
```



#### pom节点说明



**pom.xml**

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" 
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
    xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    
  <modelVersion>4.0.0</modelVersion>
  <groupId>com.xrq.withmaven</groupId>
  <artifactId>withmaven</artifactId>
  <version>0.0.1-SNAPSHOT</version>
  <build/>
</project>
```



因为这个配置文件是Maven的核心，因此有必要详细解读一下pom.xml，来先看一下上面的几个：

**1、modelVersion**

　　指定了当前Maven模型的版本号，对于Maven2和Maven3来说，它只能是4.0.0

**2、groupId**

　　顾名思义，这个应该是公司名或是组织名。一般来说groupId是由三个部分组成，每个部分之间以"."分隔，第一部分是项目用途，比如用于商业的就是"com"，用于非营利性组织的就　　是"org"；第二部分是公司名，比如"tengxun"、"baidu"、"alibaba"；第三部分是你的项目名

**3、artifactId**

　　可以认为是Maven构建的项目名，比如你的项目中有子项目，就可以使用"项目名-子项目名"的命名方式

**4、version**

　　版本号，SNAPSHOT意为快照，说明该项目还在开发中，是不稳定的版本。在Maven中很重要的一点是，**groupId、artifactId、version三个元素生成了一个Maven项目的基本坐标**，这非常重要，我在使用和研究Maven的时候多次感受到了这点。

在上面的这些元素之外，还有一些元素，同样罗列一下：

**1、packing**

　　项目打包的类型，可以使jar、war、rar、ear、pom，默认是jar

**2、dependencies和dependency**

　　前者包含后者。前面说了，Maven的一个重要作用就是统一管理jar包，为了一个项目可以build或运行，项目中不可避免的，会依赖很多其他的jar包，在Maven中，这些依赖就被称为dependency。

　　说到这里，就有一个**本地仓库**和**远程仓库**的概念了。官方下载的本地仓库的配置在"%MAVEN_HOME%\conf\settings.xml"里面，找一下"localRepository"就可以了；MyEclipse默认的本地仓库的地址在"{user.home}/.m2/repository"路径下，同样找一下"localRepository"就可以找到MyEclipse默认的本地仓库了。

**3、properties**

　　properties是用来定义一些配置属性的，例如project.build.sourceEncoding（项目构建源码编码方式），可以设置为UTF-8，防止中文乱码，也可定义相关构建版本号，便于日后统一升级。

**4、本地仓库中确定已经有jar包了，工程里面却报错，说找不到jar包，该怎么办？**

应该有很多解决办法，目前解决的一种办法是，MyEclipse->Window->Preferences->搜索Maven->User Settings,Update Settings和Reindex点一下就好了。另外，可以尝试一下把本地Maven仓库内的jar包删除一下，然后重新build workspace，可能也可以。



#### 学习文档

http://maven.apache.org/guides/mini/guide-mirror-settings.html   //官网

https://blog.csdn.net/column/details/mavenbasic.html



https://blog.csdn.net/yztezhl/article/details/21239191

https://www.cnblogs.com/kevingrace/p/6201984.html

https://blog.csdn.net/woshixuye/article/details/8133050



https://www.jianshu.com/p/0906e26abd19

https://www.cnblogs.com/jingmoxukong/p/6050172.html?utm_source=gold_browser_extension