see：https://blog.csdn.net/column/details/16112.html

#### maven安装

1 下载地址：

>http://maven.apache.org/download.cgi 

2 将下载到的apache-maven-3.3.9-bin.tar.gz文件上传到/temp目录下，然后切换到root用户下，执行如下命令 

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

3：查看maven版本信息

```
mvn -v
```

#### maven命令

```
mvn package && java -jar target/gs-spring-boot-docker-0.1.0.jar  //打包并且运行
mvn package docker:build -DpushImage
```
