see：https://blog.csdn.net/column/details/16112.html

https://www.cnblogs.com/homeword/p/7118803.html?utm_source=itdadao&utm_medium=referral

https://www.w3cschool.cn/maven/eljx1hts.html



new：

https://www.cnblogs.com/YatHo/category/1022056.html

#### maven安装

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

#### maven命令

```
mvn package && java -jar target/gs-spring-boot-docker-0.1.0.jar  //打包并且运行
mvn package docker:build -DpushImage
```

#### maven jetty插件

```
<plugins>
         <!--配置Jetty插件-->
         <plugin>
             <groupId>org.mortbay.jetty</groupId>
             <artifactId>maven-jetty-plugin</artifactId>
         </plugin>
</plugins>
```

运行命令

```
mvn clean install
```

```
mvn jetty:run
```



#### 项目部署到远程tomcat

#### buildArgs 

[docker-maven-plugin](https://github.com/spotify/docker-maven-plugin)是spotify出品的一款针对spring boot项目的docker插件，可将spring boot项目打包到docker镜像中。

如果在编译docker镜像时需要设置build arg，只需要在maven的配置文件pom.xml中，configuration下增加buildArgs。标签的key和值对应build arg的key和值，如下所示，docker镜像编译过程中，会有一个build arg名为ARG_TIME_ZONE，而其值则为OS的环境变量TIME_ZONE。

```
<build>
    <plugins>
      <plugin>
        <groupId>com.spotify</groupId>
        <artifactId>docker-maven-plugin</artifactId>
        <version>0.4.13</version>
        <executions>
          <execution>
            <phase>package</phase>
            <goals>
              <goal>build</goal>
            </goals>
          </execution>
        </executions>
        <configuration>
          <imageName>${docker.registry.name}/${project.artifactId}:latest</imageName>
          <dockerDirectory>src/main/docker</dockerDirectory>
          <buildArgs>
            <ARG_TIME_ZONE>${env.TIME_ZONE}</ARG_TIME_ZONE>
          </buildArgs>
          <resources>
            <resource>
              <targetPath>/</targetPath>
              <directory>${project.build.directory}</directory>
              <include>${project.build.finalName}.jar</include>
            </resource>
          </resources>
        </configuration>
      </plugin>
    </plugins>
  </build>
```

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