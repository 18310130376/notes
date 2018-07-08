see：https://blog.csdn.net/column/details/16112.html

https://www.cnblogs.com/homeword/p/7118803.html?utm_source=itdadao&utm_medium=referral

https://www.w3cschool.cn/maven/eljx1hts.html

#### maven安装

1 下载地址：

>http://maven.apache.org/download.cgi 

2 将下载到的apache-maven-3.3.9-bin.tar.gz文件上传到/temp目录下，然后切换到root用户下，执行如下命令 ：

linux：

>tar zxvf apache-maven-3.3.9-bin.tar.gz 
>
>mv /temp/apache-maven-3.3.9 . 
>
>sudo vim /etc/profile  或 sudo vim ~/.bashrc
>
>export M2_HOME=/opt/apache-maven-3.3.9
>
>export PATH=${M2_HOME}/bin:$$PATH 
>
>source /etc/profile   /  source ~/.bashrc

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

