#### 一、JDK安装说明

1.卸载存在的JAVA程序

```
查看Java程序：
rpm -qa|grep java
卸载：
rpm -e --nodeps java-***
#卸载内置的JDK
yum remove java-1.6.0-openjdk
yum remove java-1.7.0-openjdk
```

2.安装JDK

```
yum install wget

yum install wget wget http://download.oracle.com/otn-pub/java/jdk/8u121-b13/e9e7ea248e2c4826b92b3f075a80e441/jdk-8u121-linux-x64.rpm?AuthParam=1487483486_e29c595155c7478899db05247154bde8
wget --no-check-certificate --no-cookies --header "Cookie: oraclelicense=accept-securebackup-cookie" http://download.oracle.com/otn-pub/java/jdk/8u161-b12/2f38c3b165be4555a1fa6e98c45e0808/jdk-8u161-linux-x64.rpm
安装：rpm -ivh jdk-***.rpm

tar.gz安装

wget --no-cookies --no-check-certificate --header "Cookie: gpw_e24=http%3A%2F%2Fwww.oracle.com%2F;oraclelicense=accept-securebackup-cookie" "http://download.oracle.com/otn-pub/java/jdk/8u131-b11/d54c1d3a095b4ff2b6607d096fa80163/jdk-8u131-linux-x64.tar.gz"


tar -zxvf jdk-8u131-linux-x64.tar.gz -C /opt/soft
```

3.配置环境变量

```
# 修改配置文件
vi /etc/profile

export JAVA_HOME=/usr/java/jdk1.8.0_131
export CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar
export PATH=$JAVA_HOME/bin:$PATH
```

4.使环境变量生效

```
source /etc/profile    source ~/.bash_profile
```

5.查看JDK版本

```
java -version
```

三、tomcat安装说明：

```
wget http://apache.fayea.com/tomcat/tomcat-7/v7.0.75/bin/apache-tomcat-7.0.75.tar.gz
```



#### 二、命令

1、打包时指定了主类，可以直接用java -jar xxx.jar

2、打包是没有指定主类，可以用java -cp xxx.jar 主类名称（绝对路径）

```
java -cp $CLASSPATH:/home/cys/lib/javacsv.jar:/home/cys/test/CsvTest CsvTest

nohup java -cp ./${jarName}:./config/:$JAR_CLASSPATH  com.gwghk.ix.data.monitor.canal.CanalClient &
```

https://www.cnblogs.com/wade-luffy/category/853135.html



#### 三 、线程异常

```
	protected void start() {
		Assert.notNull(connector, "connector is null");
		thread = new Thread(new Runnable() {
			public void run() {
				process();
			}
		});
		thread.setUncaughtExceptionHandler(handler);
		thread.start();
		running = true;
		logger.info("start the canal thread!");
	}
```

```
protected Thread.UncaughtExceptionHandler handler = new Thread.UncaughtExceptionHandler() {
		public void uncaughtException(Thread t, Throwable e) {
			logger.error("parse events has an error", e);
		}
};
```

