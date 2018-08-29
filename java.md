#### java技术栈

https://my.oschina.net/javaroad/

http://outofmemory.cn/java/guava/

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

#### jps

| 配置项 | 作用                                          |
| ------ | --------------------------------------------- |
| -q     | 忽略主类的名称，只输出pid                     |
| -m     | 输出启动类main函数的参数                      |
| -l     | 输出主类名,如果进程执行的为jar，则输出jar路径 |
| -v     | 输出具体进程启动时jvm参数                     |

- **jps -lv** ： 输出启动类名与启动时jvm参数，可以方便的看到各个tomcat的自定义参数配置
- **jps -lv |grep project_name** ： 在上述基础上过滤出自己想要查看的项目的信息



#### jinfo

显示虚拟机配置信息

- **jinfo pid** : 显示jvm系统属性与vm参数信息
- **jinfo -flags pid** : 显示jvm vm参数信息,如最大最小堆，默认堆，垃圾收集器参数等
- **jinfo -sysprops pid** : 显示jvm系统属性
- **jinfo -flag** : 显示特定vm参数值,例如 **jinfo -flag MaxHeapSize pid** 输出pid的最大堆内存



#### **jstat** 

收集虚拟机各方面运行数据

jstat [ option pid [interval[s|ms][count]]]
说明： interval 表示循环时间间隔,默认单位为ms，可以在直接使用s/ms指定单位，如 60ms/1s， count 表示输出几次	例：
**jstat -gc pid 1s 20**: 每1s查询一次gc情况，查询20次 



查看类装载卸载情况 jstat -class pid

| 属性     | 释义                   |
| -------- | ---------------------- |
| Loaded   | 装载总数量             |
| Bytes    | 装载总大小             |
| Unloaded | 卸载类的数量           |
| Bytes    | 卸载总大小             |
| Time     | 加载和卸载类总共的耗时 |

查看GC情况 jstat -gc pid

| 属性 | 释义                              |
| ---- | --------------------------------- |
| S0C  | 新生代survivor0容量               |
| S1C  | 新生代survivor1容量               |
| S0U  | 新生代survivor0已使用大小         |
| S1U  | 新生代survivor1已使用大小         |
| EC   | 新生代eden区容量                  |
| EU   | 新生代eden区已使用大小            |
| OC   | 老年代容量                        |
| OU   | 老年代已使用大小                  |
| MC   | 元数据容量，即方法区容量          |
| MU   | 元数据已使用空间                  |
| CCSC | 压缩类空间大小                    |
| CCSU | 压缩类空间使用大小                |
| YGC  | 新生代gc次数(young gc)            |
| YGCT | 新生代gc时间(s)                   |
| FGC  | 老生代gc次数(full gc)             |
| GCT  | 总的gc时间，包括young gc和full gc |

查看GC情况，以百分比显示

**jstat -gcutil pid**

查看新生代GC情况

**jstat -gcnew pid**

| 属性 | 释义                       |
| ---- | -------------------------- |
| S0C  | 新生代survivor0容量        |
| S1C  | 新生代survivor1容量        |
| S0U  | 新生代survivor0已使用大小  |
| S1U  | 新生代survivor1已使用大小  |
| TT   | 对象在新生代存活的次数     |
| MTT  | 对象在新生代存活的最大次数 |
| DSS  | 期望的幸存区大小           |
| EC   | eden区大小                 |
| EU   | eden区已使用大小           |
| YGC  | young gc次数               |
| YGCT | young gc 时间 (秒)         |

查看编译情况

jstat -compiler pid

#### **jmap** 

虚拟机堆快照工具

**jmap -histo pid** 列出当前heap中对象状况，附字节码与java对象映射表 



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

```
Resource resource = new ClassPathResource("public.cert");
String publicKey = null;
publicKey = new String(FileCopyUtils.copyToByteArray(resource.getInputStream()));
```

#### 四、cpu使用过高排查

使用top发现PID为14883的Java进程占用CPU高达100%，出现故障。

```
PID USER      PR  NI    VIRT    RES    SHR S  %CPU %MEM     TIME+ COMMAND
14883 root      20   0 4325916  52400  12828 S 100.0  0.7   5:18.11 java
```

通过ps aux | grep PID命令，可以进一步确定是tomcat进程出现了问题。但是，怎么定位到具体线程或者代码呢



找出进程下的线程列表

ps -mp pid -o THREAD,tid,time 或者  ps p 15373 -L -o pcpu,pmem,pid,tid,time,tname,cmd

```
[root@localhost ~]# ps -mp 14883 -o THREAD,tid,time
USER     %CPU PRI SCNT WCHAN  USER SYSTEM   TID     TIME
root      100   -    - -         -      -     - 00:08:05
root      0.0  19    - futex_    -      - 14883 00:00:00
root     99.6  19    - -         -      - 14884 00:08:03
root      0.0  19    - futex_    -      - 14885 00:00:00
```

找到了耗时最高的线程14884，占用CPU时间快两个小时了！

把线程id转为16进制  printf "%x\n" tid

```
printf "%x\n" 14884
3a24
```

打印线程堆栈信息

jstack pid |grep tid -A 30   或者  jstack -l 15373> jstack.log，然后搜索3a24

```
jstack 14883 |grep 3a24 -A 30

"main" #1 prio=5 os_prio=0 tid=0x00007f9db8009000 nid=0x3a24 runnable [0x00007f9dc1e6f000]
   java.lang.Thread.State: RUNNABLE
        at java.lang.Throwable.fillInStackTrace(Native Method)
        at java.lang.Throwable.fillInStackTrace(Throwable.java:783)
        - locked <0x00000000dc2fa490> (a java.io.IOException)
        at java.lang.Throwable.<init>(Throwable.java:265)
        at java.lang.Exception.<init>(Exception.java:66)
        at java.io.IOException.<init>(IOException.java:58)
        at java.io.FileOutputStream.writeBytes(Native Method)
        at java.io.FileOutputStream.write(FileOutputStream.java:326)
        at java.io.BufferedOutputStream.flushBuffer(BufferedOutputStream.java:82)
        at java.io.BufferedOutputStream.write(BufferedOutputStream.java:126)
        - locked <0x0000000092a06a58> (a java.io.BufferedOutputStream)
        at java.io.PrintStream.write(PrintStream.java:480)
        - locked <0x0000000092a06a70> (a java.io.PrintStream)
        at sun.nio.cs.StreamEncoder.writeBytes(StreamEncoder.java:221)
        at sun.nio.cs.StreamEncoder.implFlushBuffer(StreamEncoder.java:291)
        at sun.nio.cs.StreamEncoder.flushBuffer(StreamEncoder.java:104)
        - locked <0x0000000092a0a140> (a java.io.OutputStreamWriter)
        at java.io.OutputStreamWriter.flushBuffer(OutputStreamWriter.java:185)
        at java.io.PrintStream.newLine(PrintStream.java:546)
        - eliminated <0x0000000092a06a70> (a java.io.PrintStream)
        at java.io.PrintStream.println(PrintStream.java:807)
        - locked <0x0000000092a06a70> (a java.io.PrintStream)
        at CpuReaper.main(CpuReaper.java:12)
```

问题解决，修改代码



方式二：

1. 通过top命令查看当前CPU及内存情况

   ```
   top
   86786  java         98.4 13:22.7
   ```

2. 获得pid,通过`top -H -p 86786`查看有问题的线程

   > 说明： -H 指显示线程，-p 是指定进程

3. 可以看到两个CPU或内存占用较高的线程，记下PID（ 此处的PID即为线程ID标识） ，将其从十进制转成十六进制表示，如0x7f1

   ```
   printf "%x\n" tid
   0x7f1
   ```

4. 通过jstack命令获取当前线程栈，可暂时保存到一个文件tempfile.txt中，在tempfile.txt中查找nid=0x7f1的线程

   ```
   jstack pid | grep '0x7f1'
   ```



#### 五、高内存占用

top查看内存使用情况

```
 PID USER      PR  NI    VIRT    RES    SHR S  %CPU %MEM     TIME+ COMMAND
15373 root      20   0 4325808  21156  11956 S 100.0  24  39:24.70 java
20852 root      20   0 1278452  59204   7280 S   5.0  0.8 774:36.15 node
20862 root      20   0 1278456  58592   7276 S   4.3  0.8 772:26.12 node
```

发现进程15373的内存占用24%

jmap -histo:live [pid] > a.log

```
 num     #instances         #bytes  class name
----------------------------------------------
   1:          1154          95176000  [C
   2:           493          56256000  java.lang.Class
   3:            33          34168  [B
```

发现java.lang.Class和[C占用大量内存，分析代码



#### 六、三种内存溢出错误的处理方法

第一种OutOfMemoryError： PermGen space

发生这种问题的原意是程序中使用了大量的jar或class，使java虚拟机装载类的空间不够，与Permanent Generation space有关。解决这类问题有以下两种办法：

1. 增加java虚拟机中的XX:PermSize和XX:MaxPermSize参数的大小，其中XX:PermSize是初始永久保存区域大小，XX:MaxPermSize是最大永久保存区域大小。如针对tomcat6.0，在catalina.sh 或catalina.bat文件中一系列环境变量名说明结束处（大约在70行左右） 增加一行： `JAVA_OPTS=" -XX:PermSize=64M -XX:MaxPermSize=128m"` 如果是windows服务器还可以在系统环境变量中设置。感觉用tomcat发布sprint+struts+hibernate架构的程序时很容易发生这种内存溢出错误。使用上述方法，我成功解决了部署ssh项目的tomcat服务器经常宕机的问题。
2. 清理应用程序中web-inf/lib下的jar，如果tomcat部署了多个应用，很多应用都使用了相同的jar，可以将共同的jar移到tomcat共同的lib下，减少类的重复加载。这种方法是网上部分人推荐的，我没试过，但感觉减少不了太大的空间，最靠谱的还是第一种方法。

第二种OutOfMemoryError：  Java heap space

发生这种问题的原因是java虚拟机创建的对象太多，在进行垃圾回收之间，虚拟机分配的到堆内存空间已经用满了，与Heap space有关。解决这类问题有两种思路：

1. 检查程序，看是否有死循环或不必要地重复创建大量对象。找到原因后，修改程序和算法。 我以前写一个使用K-Means文本聚类算法对几万条文本记录（每条记录的特征向量大约10来个）进行文本聚类时，由于程序细节上有问题，就导致了Java heap space的内存溢出问题，后来通过修改程序得到了解决。
2. 增加Java虚拟机中Xms（初始堆大小）和Xmx（最大堆大小）参数的大小。如：`set JAVA_OPTS= -Xms256m -Xmx1024m`

第三种OutOfMemoryError：unable to create new native thread

在java应用中，有时候会出现这样的错误：OutOfMemoryError: unable to create new native thread.这种怪事是因为JVM已经被系统分配了大量的内存(比如1.5G)，并且它至少要占用可用内存的一半。有人发现，在线程个数很多的情况下，你分配给JVM的内存越多，那么，上述错误发生的可能性就越大。

那么是什么原因造成这种问题呢？

每一个32位的进程最多可以使用2G的可用内存，因为另外2G被操作系统保留。这里假设使用1.5G给JVM，那么还余下500M可用内存。这500M内存中的一部分必须用于系统dll的加载，那么真正剩下的也许只有400M，现在关键的地方出现了：当你使用Java创建一个线程，在JVM的内存里也会创建一个Thread对象，但是同时也会在操作系统里创建一个真正的物理线程(参考JVM规范)，操作系统会在余下的400兆内存里创建这个物理线程，而不是在JVM的1500M的内存堆里创建。在jdk1.4里头，默认的栈大小是256KB，但是在jdk1.5里头，默认的栈大小为1M每线程，因此，在余下400M的可用内存里边我们最多也只能创建400个可用线程。



#### 七、Apache Commons Lang包的常用方法总结

```java
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.CharSet;
import org.apache.commons.lang3.CharSetUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.time.StopWatch;

/*转自霸气的小码农的博客*/
public class TestLangDemo {

    public void charSetDemo() {
        System.out.println ("**CharSetDemo**");
        CharSet charSet = CharSet.getInstance ("aeiou");
        String demoStr = "The quick brown fox jumps over the lazy dog.";
        int count = 0;
        for (int i = 0, len = demoStr.length(); i < len; i++) {
            if (charSet.contains (demoStr.charAt (i) ) ) {
                count++;
            }
        }
        System.out.println ("count: " + count);
    }

    public void charSetUtilsDemo() {
        System.out.println ("**CharSetUtilsDemo**");
        System.out.println ("计算字符串中包含某字符数.");
        System.out.println (CharSetUtils.count (
                                "The quick brown fox jumps over the lazy dog.", "aeiou") );

        System.out.println ("删除字符串中某字符.");
        System.out.println (CharSetUtils.delete (
                                "The quick brown fox jumps over the lazy dog.", "aeiou") );

        System.out.println ("保留字符串中某字符.");
        System.out.println (CharSetUtils.keep (
                                "The quick brown fox jumps over the lazy dog.", "aeiou") );

        System.out.println ("合并重复的字符.");
        System.out.println (CharSetUtils.squeeze ("a  bbbbbb     c dd", "b d") );
    }

    public void objectUtilsDemo() {
        System.out.println ("**ObjectUtilsDemo**");
        System.out.println ("Object为null时，默认打印某字符.");
        Object obj = null;
        System.out.println (ObjectUtils.defaultIfNull (obj, "空") );

        System.out.println ("验证两个引用是否指向的Object是否相等,取决于Object的equals()方法.");
        Object a = new Object();
        Object b = a;
        Object c = new Object();
        System.out.println (ObjectUtils.equals (a, b) );
        System.out.println (ObjectUtils.equals (a, c) );

        System.out.println ("用父类Object的toString()方法返回对象信息.");
        Date date = new Date();
        System.out.println (ObjectUtils.identityToString (date) );
        System.out.println (date);

        System.out.println ("返回类本身的toString()方法结果,对象为null时，返回0长度字符串.");
        System.out.println (ObjectUtils.toString (date) );
        System.out.println (ObjectUtils.toString (null) );
        System.out.println (date);
    }

    public void serializationUtilsDemo() {
        System.out.println ("*SerializationUtils**");
        Date date = new Date();
        byte[] bytes = SerializationUtils.serialize (date);
        System.out.println (ArrayUtils.toString (bytes) );
        System.out.println (date);

        Date reDate = (Date) SerializationUtils.deserialize (bytes);
        System.out.println (reDate);
        System.out.println (ObjectUtils.equals (date, reDate) );
        System.out.println (date == reDate);

        FileOutputStream fos = null;
        FileInputStream fis = null;
        try {
            fos = new FileOutputStream (new File ("d:/test.txt") );
            fis = new FileInputStream (new File ("d:/test.txt") );
            SerializationUtils.serialize (date, fos);
            Date reDate2 = (Date) SerializationUtils.deserialize (fis);

            System.out.println (date.equals (reDate2) );

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void randomStringUtilsDemo() {
        System.out.println ("**RandomStringUtilsDemo**");
        System.out.println ("生成指定长度的随机字符串,好像没什么用.");
        System.out.println (RandomStringUtils.random (500) );

        System.out.println ("在指定字符串中生成长度为n的随机字符串.");
        System.out.println (RandomStringUtils.random (5, "abcdefghijk") );

        System.out.println ("指定从字符或数字中生成随机字符串.");
        System.out.println (RandomStringUtils.random (5, true, false) );
        System.out.println (RandomStringUtils.random (5, false, true) );

    }

    public void stringUtilsDemo() {
        System.out.println ("**StringUtilsDemo**");
        System.out.println ("将字符串重复n次，将文字按某宽度居中，将字符串数组用某字符串连接.");
        String[] header = new String[3];
        header[0] = StringUtils.repeat ("*", 50);
        header[1] = StringUtils.center ("  StringUtilsDemo  ", 50, "^O^");
        header[2] = header[0];
        String head = StringUtils.join (header, "\n");
        System.out.println (head);

        System.out.println ("缩短到某长度,用...结尾.");
        System.out.println (StringUtils.abbreviate (
                                "The quick brown fox jumps over the lazy dog.", 10) );
        System.out.println (StringUtils.abbreviate (
                                "The quick brown fox jumps over the lazy dog.", 15, 10) );

        System.out.println ("返回两字符串不同处索引号.");
        System.out.println (StringUtils.indexOfDifference ("aaabc", "aaacc") );

        System.out.println ("返回两字符串不同处开始至结束.");
        System.out.println (StringUtils.difference ("aaabcde", "aaaccde") );

        System.out.println ("截去字符串为以指定字符串结尾的部分.");
        System.out.println (StringUtils.chomp ("aaabcde", "de") );

        System.out.println ("检查一字符串是否为另一字符串的子集.");
        System.out.println (StringUtils.containsOnly ("aad", "aadd") );

        System.out.println ("检查一字符串是否不是另一字符串的子集.");
        System.out.println (StringUtils.containsNone ("defg", "aadd") );

        System.out.println ("检查一字符串是否包含另一字符串.");
        System.out.println (StringUtils.contains ("defg", "ef") );
        System.out.println (StringUtils.containsOnly ("ef", "defg") );

        System.out.println ("返回可以处理null的toString().");
        System.out.println (StringUtils.defaultString ("aaaa") );
        System.out.println ("?" + StringUtils.defaultString (null) + "!");

        System.out.println ("去除字符中的空格.");
        System.out.println (StringUtils.deleteWhitespace ("aa  bb  cc") );

        System.out.println ("分隔符处理成数组.");
        String[] strArray = StringUtils.split ("a,b,,c,d,null,e", ",");
        System.out.println (strArray.length);
        System.out.println (strArray.toString() );

        System.out.println ("判断是否是某类字符.");
        System.out.println (StringUtils.isAlpha ("ab") );
        System.out.println (StringUtils.isAlphanumeric ("12") );
        System.out.println (StringUtils.isBlank ("") );
        System.out.println (StringUtils.isNumeric ("123") );
    }

    public void systemUtilsDemo() {
        System.out.println (genHeader ("SystemUtilsDemo") );
        System.out.println ("获得系统文件分隔符.");
        System.out.println (SystemUtils.FILE_SEPARATOR);

        System.out.println ("获得源文件编码.");
        System.out.println (SystemUtils.FILE_ENCODING);

        System.out.println ("获得ext目录.");
        System.out.println (SystemUtils.JAVA_EXT_DIRS);

        System.out.println ("获得java版本.");
        System.out.println (SystemUtils.JAVA_VM_VERSION);

        System.out.println ("获得java厂商.");
        System.out.println (SystemUtils.JAVA_VENDOR);
    }

    public void classUtilsDemo() {
        System.out.println (genHeader ("ClassUtilsDemo") );
        System.out.println ("获取类实现的所有接口.");
        System.out.println (ClassUtils.getAllInterfaces (Date.class) );

        System.out.println ("获取类所有父类.");
        System.out.println (ClassUtils.getAllSuperclasses (Date.class) );

        System.out.println ("获取简单类名.");
        System.out.println (ClassUtils.getShortClassName (Date.class) );

        System.out.println ("获取包名.");
        System.out.println (ClassUtils.getPackageName (Date.class) );

        System.out.println ("判断是否可以转型.");
        System.out.println (ClassUtils.isAssignable (Date.class, Object.class) );
        System.out.println (ClassUtils.isAssignable (Object.class, Date.class) );
    }

    public void stringEscapeUtilsDemo() {
        System.out.println (genHeader ("StringEcsapeUtils") );
        System.out.println ("转换特殊字符.");
        System.out.println ("html:" + StringEscapeUtils.escapeHtml3 (" ") );
        System.out.println ("html:" + StringEscapeUtils.escapeHtml4 (" ") );
        System.out.println ("html:" + StringEscapeUtils.unescapeHtml3 ("<p>") );
        System.out.println ("html:" + StringEscapeUtils.unescapeHtml4 ("<p>") );
    }

    private final class BuildDemo {
        String name;
        int age;

        public BuildDemo (String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String toString() {
            ToStringBuilder tsb = new ToStringBuilder (this, ToStringStyle.MULTI_LINE_STYLE);
            tsb.append ("Name", name);
            tsb.append ("Age", age);
            return tsb.toString();
        }

        public int hashCode() {
            HashCodeBuilder hcb = new HashCodeBuilder();
            hcb.append (name);
            hcb.append (age);
            return hcb.hashCode();
        }

        public boolean equals (Object obj) {
            if (! (obj instanceof BuildDemo) ) {
                return false;
            }
            BuildDemo bd = (BuildDemo) obj;
            EqualsBuilder eb = new EqualsBuilder();
            eb.append (name, bd.name);
            eb.append (age, bd.age);
            return eb.isEquals();
        }
    }

    public void builderDemo() {
        System.out.println (genHeader ("BuilderDemo") );
        BuildDemo obj1 = new BuildDemo ("a", 1);
        BuildDemo obj2 = new BuildDemo ("b", 2);
        BuildDemo obj3 = new BuildDemo ("a", 1);

        System.out.println ("toString()");
        System.out.println (obj1);
        System.out.println (obj2);
        System.out.println (obj3);

        System.out.println ("hashCode()");
        System.out.println (obj1.hashCode() );
        System.out.println (obj2.hashCode() );
        System.out.println (obj3.hashCode() );

        System.out.println ("equals()");
        System.out.println (obj1.equals (obj2) );
        System.out.println (obj1.equals (obj3) );
    }

    public void numberUtils() {
        System.out.println (genHeader ("NumberUtils") );
        System.out.println ("字符串转为数字(不知道有什么用).");
        System.out.println (NumberUtils.toInt ("ba", 33) );

        System.out.println ("从数组中选出最大值.");
        System.out.println (NumberUtils.max (new int[] { 1, 2, 3, 4 }) );

        System.out.println ("判断字符串是否全是整数.");
        System.out.println (NumberUtils.isDigits ("123.1") );

        System.out.println ("判断字符串是否是有效数字.");
        System.out.println (NumberUtils.isNumber ("0123.1") );
    }

    public void dateFormatUtilsDemo() {
        System.out.println (genHeader ("DateFormatUtilsDemo") );
        System.out.println ("格式化日期输出.");
        System.out.println (DateFormatUtils.format (System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss") );

        System.out.println ("秒表.");
        StopWatch sw = new StopWatch();
        sw.start();

        for (Iterator iterator = DateUtils.iterator (new Date(), DateUtils.RANGE_WEEK_CENTER); iterator.hasNext();) {
            Calendar cal = (Calendar) iterator.next();
            System.out.println (DateFormatUtils.format (cal.getTime(),
                                "yy-MM-dd HH:mm") );
        }

        sw.stop();
        System.out.println ("秒表计时:" + sw.getTime() );

    }

    private String genHeader (String head) {
        String[] header = new String[3];
        header[0] = StringUtils.repeat ("*", 50);
        header[1] = StringUtils.center ("  " + head + "  ", 50, "^O^");
        header[2] = header[0];
        return StringUtils.join (header, "\n");
    }

    private void validateDemo() {
        String[] strarray = {"a", "b", "c"};
        System.out.println ("验证功能");
        System.out.println (Validate.notEmpty (strarray) );
    }

    private void wordUtilsDemo() {
        System.out.println ("单词处理功能");
        String str1 = "wOrD";
        String str2 = "ghj\nui\tpo";
        System.out.println (WordUtils.capitalize (str1) ); //首字母大写
        System.out.println (WordUtils.capitalizeFully (str1) ); //首字母大写其它字母小写
        char[] ctrg = {'.'};
        System.out.println (WordUtils.capitalizeFully ("i aM.fine", ctrg) ); //在规则地方转换
        System.out.println (WordUtils.initials (str1) ); //获取首字母
        System.out.println (WordUtils.initials ("Ben John Lee", null) ); //取每个单词的首字母
        char[] ctr = {' ', '.'};
        System.out.println (WordUtils.initials ("Ben J.Lee", ctr) ); //按指定规则获取首字母
        System.out.println (WordUtils.swapCase (str1) ); //大小写逆转
        System.out.println (WordUtils.wrap (str2, 1) ); //解析\n和\t等字符
    }

    /**
     * @param args
     */
    public static void main (String[] args) {
        TestLangDemo langDemo = new TestLangDemo();

        langDemo.charSetDemo();
        langDemo.charSetUtilsDemo();
        langDemo.objectUtilsDemo();
        langDemo.serializationUtilsDemo();
        langDemo.randomStringUtilsDemo();
        langDemo.stringUtilsDemo();
        langDemo.systemUtilsDemo();
        langDemo.classUtilsDemo();
        langDemo.stringEscapeUtilsDemo();
        langDemo.builderDemo();
        langDemo.numberUtils();
        langDemo.dateFormatUtilsDemo();
        langDemo.validateDemo();
        langDemo.wordUtilsDemo();
    }
}
```



#### 八、Java中获取文件名、类名、方法名、行号的方法

```java
import java.util.ArrayList;
import java.util.List;

public class CpuReaper {

	static List<User> list = new ArrayList<>();
	
	public static void main(String args[]) throws InterruptedException {
			 getClassName();
	}

	 static void  getClassName() {
		 
		 StackTraceElement[] stackTrace = new Throwable().getStackTrace();
		 System.out.println(stackTrace[1].getClassName());
		 System.out.println(stackTrace[1].getMethodName());
		 System.out.println(stackTrace[1].getLineNumber());
		 System.out.println(stackTrace[1].getFileName());
	}
}
```

#### 九、得到当前方法的名字

```java
import java.util.ArrayList;
import java.util.List;

public class CpuReaper {

	static List<User> list = new ArrayList<>();
	
	public static void main(String args[]) throws InterruptedException {
			 getClassName();
	}

	 static void  getClassName() {
		 String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		 System.out.println(methodName);
	}
}

输出：
getClassName
```

#### 十、java读取jar包内的文件

```java
InputStream is = this.getClass().getResourceAsStream(  
                "/templates/TemplateDO.ja");  
//如果是静态方法则 DeadLock.class.getClass().getResourceAsStream(name)

BufferedReader br;  
        StringBuilder strBlder = new StringBuilder("");  
        try {  
            br = new BufferedReader(new InputStreamReader(is));  
            String line = "";  
            while (null != (line = br.readLine())) {  
                strBlder.append(line + "\n");  
            }  
            br.close();  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
return strBlder.toString();  
```

#### 十一、System.getProperty()方法获取大全

```java
System.out.println("java版本号：" + System.getProperty("java.version")); // java版本号
System.out.println("Java提供商名称：" + System.getProperty("java.vendor")); // Java提供商名称
System.out.println("Java提供商网站：" + System.getProperty("java.vendor.url")); // Java提供商网站
System.out.println("jre目录：" + System.getProperty("java.home")); // Java，哦，应该是jre目录
System.out.println("Java虚拟机规范版本号：" + System.getProperty("java.vm.specification.version")); // Java虚拟机规范版本号
System.out.println("Java虚拟机规范提供商：" + System.getProperty("java.vm.specification.vendor")); // Java虚拟机规范提供商
System.out.println("Java虚拟机规范名称：" + System.getProperty("java.vm.specification.name")); // Java虚拟机规范名称
System.out.println("Java虚拟机版本号：" + System.getProperty("java.vm.version")); // Java虚拟机版本号
System.out.println("Java虚拟机提供商：" + System.getProperty("java.vm.vendor")); // Java虚拟机提供商
System.out.println("Java虚拟机名称：" + System.getProperty("java.vm.name")); // Java虚拟机名称
System.out.println("Java规范版本号：" + System.getProperty("java.specification.version")); // Java规范版本号
System.out.println("Java规范提供商：" + System.getProperty("java.specification.vendor")); // Java规范提供商
System.out.println("Java规范名称：" + System.getProperty("java.specification.name")); // Java规范名称
System.out.println("Java类版本号：" + System.getProperty("java.class.version")); // Java类版本号
System.out.println("Java类路径：" + System.getProperty("java.class.path")); // Java类路径
System.out.println("Java lib路径：" + System.getProperty("java.library.path")); // Java lib路径
System.out.println("Java输入输出临时路径：" + System.getProperty("java.io.tmpdir")); // Java输入输出临时路径
System.out.println("Java编译器：" + System.getProperty("java.compiler")); // Java编译器
System.out.println("Java执行路径：" + System.getProperty("java.ext.dirs")); // Java执行路径
System.out.println("操作系统名称：" + System.getProperty("os.name")); // 操作系统名称
System.out.println("操作系统的架构：" + System.getProperty("os.arch")); // 操作系统的架构
System.out.println("操作系统版本号：" + System.getProperty("os.version")); // 操作系统版本号
System.out.println("文件分隔符：" + System.getProperty("file.separator")); // 文件分隔符
//等价于File.separator

System.out.println("路径分隔符：" + System.getProperty("path.separator")); // 路径分隔符
System.out.println("直线分隔符：" + System.getProperty("line.separator")); // 直线分隔符
System.out.println("操作系统用户名：" + System.getProperty("user.name")); // 用户名
System.out.println("操作系统用户的主目录：" + System.getProperty("user.home")); // 用户的主目录
System.out.println("当前程序所在目录：" + System.getProperty("user.dir")); // 当前程序所在目录
```

#### 十二、nohup命令输出到指定的文件， 而不是默认的nohup.out

nohup 默认输出到当前目录的nohup.out， 可以通过下面的命令来制定nohup输出位置

```
nohup some_command &> nohup2.out&
```

或者也可以这样设置:

```
nohup some_command > nohup2.out 2>&1&
```


#### 十三、下载文件

引入commons-io-2.4.jar

```java
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class DeadLock{
	public  static void main(String[] args) throws IOException {
		 URL httpurl=new URL("https://images2018.cnblogs.com/blog/380866/201803/380866-20180327125308429-1074804504.png");
	        HttpURLConnection httpConn=(HttpURLConnection)httpurl.openConnection();
	        httpConn.setDoOutput(true);// 使用 URL 连接进行输出
	        httpConn.setDoInput(true);// 使用 URL 连接进行输入
	        httpConn.setUseCaches(false);// 忽略缓存
	        httpConn.setRequestMethod("GET");// 设置URL请求方法
	        //可设置请求头
	        httpConn.setRequestProperty("Content-Type", "application/octet-stream");
	        httpConn.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
	        httpConn.setRequestProperty("Charset", "UTF-8");
	        //可设置请求头
	        InputStream inputStream = httpConn.getInputStream();
	        FileUtils.writeByteArrayToFile(new File("C:\\Users\\789\\Desktop\\logs\\a.png"), IOUtils.toByteArray(inputStream));
	}
}
```

#### 十四、Java8  Stream

```java
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class StreamOfJava8 {

	public static void main(String[] args) {
		learnStream();
	}
	private static void learnStream() {
		//首先,创建一个1-6乱序的List
		List<Integer> lists = new ArrayList<>();
		lists.add(4);
		lists.add(3);
		lists.add(6);
		lists.add(1);
		lists.add(5);
		lists.add(2);
        
        lists.forEach((e)->System.out.println(e));
        long count = lists.stream().filter((e)->1==e).count();
        
		// 看看List里面的数据是什么样子的先
		System.out.print("List里面的数据:");
		for (Integer elem : lists)
			System.out.print(elem + " ");
		// 最小值
		System.out.print("List中最小的值为:");
		Optional<Integer> min = lists.stream().min(Integer::compareTo);
		if (min.isPresent()) {
			System.out.println(min.get());
		}
		// 最大值
		System.out.print("List中最大的值为:");
		lists.stream().max(Integer::compareTo).ifPresent(System.out::println);
		// 排序
		System.out.print("将List流进行排序:");
		Stream<Integer> sorted = lists.stream().sorted();

		sorted.forEach(elem -> System.out.print(elem + " "));
		System.out.println();
		System.out.print("过滤List流,只剩下那些大于3的元素:");
		lists.stream().filter(elem -> elem > 3).forEach(elem -> System.out.print(elem + " "));
		System.out.println("过滤List流,只剩下那些大于0并且小于4的元素:\n=====begin=====");
		lists.stream()
				.filter(elem -> elem > 0)
				.filter(elem -> elem < 4)
				.sorted(Integer::compareTo)
				.forEach(System.out::println);
		System.out.println("=====end=====");
		// 经过了前面的这么多流操作,我们再来看看List里面的值有没有发生什么改变
		System.out.print("原List里面的数据:");
		for (Integer elem : lists)
			System.out.print(elem + " ");
	}
    
    //两个list合并去掉重复
     List<String> collect = Stream.of(listA, listB)
       .flatMap(Collection::stream)
        .distinct()
        .collect(Collectors.toList());
}
```

```java
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamTest {

	public static void main(String[] args)

	{
		System.out.println("过滤－找出年纪大于18岁的人");
		List<User> list = initList();
		list.stream().filter((User user)-> user.getAge()>18).collect(Collectors.toList()).forEach(System.out::println);

		System.out.println("最大值－找出最大年纪的人");
		list = initList();
		Optional<User> max = list.stream().max((u1, u2)-> u1.getAge() - u2.getAge());
		System.out.println(max.get());
		System.out.println("映射-规纳－求所有人的年纪总和");
		list = initList();
		Optional<Integer> reduce = list.stream().map(User::getAge).reduce(Integer::sum);
		System.out.println(reduce.get());
		System.out.println();
		System.out.println("分组－按年纪分组");
		list = initList();
		Map<Integer,List<User>> userMap = list.stream().collect(Collectors.groupingBy(User::getAge));

		Stream<User> userStream = Stream.of(new User("u1",1),new User("u2",21),new User("u2",21));
		System.out.println(userStream.distinct().count());
	}

	public static List<User> initList() {

		List<User> list = new ArrayList<>();
		list.add(new User("oaby",23));
		list.add(new User("tom",11));
		list.add(new User("john",16));
		list.add(new User("jennis",26));
		list.add(new User("tin",26));
		list.add(new User("army",26));
		list.add(new User("mack",19));
		list.add(new User("jobs",65));
		list.add(new User("jordan",23));
		return list;
	}
}
```

#### 十五、BASE64

base64编码解码已经被加入到了jdk8中了。

```java
import java.nio.charset.StandardCharsets;

import java.util.Base64;

public class Base64Test {
	public static void main(String[] args) {
		
		String text = "hello javastack";
		String encoded = Base64.getEncoder().encodeToString(text.getBytes(StandardCharsets.UTF_8));
		System.out.println(encoded);
		String decoded = new String(Base64.getDecoder().decode(encoded), StandardCharsets.UTF_8);
		System.out.println(decoded);
	}
}
```

#### 十六、Date/Time API(JSR 310)

```java
import java.time.Clock;

public class Base64Test {
	
	public static void main(String[] args) {

		Clock clock = Clock.systemUTC();
		System.out.println(clock.instant());
		System.out.println(clock.millis());
        //可以代替 System.currentTimeMillis()方法。
	}
}
```

#### 十七、金融系统中正确的金额计算及存储方式

- 金额运算尽量使用BigDecimal(String val)进行运算。
- 数据库存储金额，一般有整型和浮点型两种存储方式。如果是有汇率转换的，建议使用浮点数decimal进行存储，可以灵活的控制精度，decimal直接对应java类型BigDecimal。当然，用整数存储分这种形式也可以，转账的时候单位为元而如果忘了转换分为元，那就悲剧了。

```java
import java.math.BigDecimal;

public class Base64Test {

	public static void main(String[] args) {

		double totalAmount = 0.09;
		double feeAmount = 0.02;
		
		BigDecimal tradeAmount = new BigDecimal(String.valueOf(totalAmount)).subtract(new BigDecimal(String.valueOf(feeAmount)));
		System.out.println(tradeAmount);//正确
		
		BigDecimal tradeAmountNew = new BigDecimal(totalAmount).subtract(new BigDecimal(feeAmount));
		System.out.println(tradeAmountNew);//错误
	}
}

0.07
0.0699999999999999962529972918900966760702431201934814453125
```



#### 十八、JAVA常用工具类



一. org.apache.commons.io.IOUtils

```
closeQuietly：关闭一个IO流、socket、或者selector且不抛出异常，通常放在finally块
toString：转换IO流、 Uri、 byte[]为String
copy：IO流数据复制，从输入流写到输出流中，最大支持2GB
toByteArray：从输入流、URI获取byte[]
write：把字节. 字符等写入输出流
toInputStream：把字符转换为输入流
readLines：从输入流中读取多行数据，返回List<String>
copyLarge：同copy，支持2GB以上数据的复制
lineIterator：从输入流返回一个迭代器，根据参数要求读取的数据量，全部读取，如果数据不够，则失败
```

二. org.apache.commons.io.FileUtils

```
deleteDirectory：删除文件夹
readFileToString：以字符形式读取文件内容
deleteQueitly：删除文件或文件夹且不会抛出异常
copyFile：复制文件
writeStringToFile：把字符写到目标文件，如果文件不存在，则创建
forceMkdir：强制创建文件夹，如果该文件夹父级目录不存在，则创建父级
write：把字符写到指定文件中
listFiles：列举某个目录下的文件(根据过滤器)
copyDirectory：复制文件夹
forceDelete：强制删除文件
```

三. org.apache.commons.lang.StringUtils

```
isBlank：字符串是否为空 (trim后判断)
isEmpty：字符串是否为空 (不trim并判断)
equals：字符串是否相等
join：合并数组为单一字符串，可传分隔符
split：分割字符串EMPTY：返回空字符串
trimToNull：trim后为空字符串则转换为null
replace：替换字符串
```

四. org.apache.http.util.EntityUtils

```
toString：把Entity转换为字符串
consume：确保Entity中的内容全部被消费。可以看到源码里又一次消费了Entity的内容，假如用户没有消费，那调用Entity时候将会把它消费掉
toByteArray：把Entity转换为字节流
consumeQuietly：和consume一样，但不抛异常
getContentCharset：获取内容的编码
```

五. org.apache.commons.lang3.StringUtils

```
isBlank：字符串是否为空 (trim后判断)
isEmpty：字符串是否为空 (不trim并判断)
equals：字符串是否相等join：合并数组为单一字符串，可传分隔符
split：分割字符串EMPTY：返回空字符串
replace：替换字符串
capitalize：首字符大写
```

六. org.apache.commons.io.FilenameUtils

```
getExtension：返回文件后缀名
getBaseName：返回文件名，不包含后缀名
getName：返回文件全名
concat：按命令行风格组合文件路径(详见方法注释)
removeExtension：删除后缀名
normalize：使路径正常化
wildcardMatch：匹配通配符
seperatorToUnix：路径分隔符改成unix系统格式的，即/
getFullPath：获取文件路径，不包括文件名
isExtension：检查文件后缀名是不是传入参数(List<String>)中的一个
```

七. org.springframework.util.StringUtils

```
hasText：检查字符串中是否包含文本
hasLength：检测字符串是否长度大于0
isEmpty：检测字符串是否为空（若传入为对象，则判断对象是否为null）
commaDelimitedStringToArray：逗号分隔的String转换为数组
collectionToDelimitedString：把集合转为CSV格式字符串replace 替换字符串
delimitedListToStringArray：相当于split
uncapitalize：首字母小写
collectionToDelimitedCommaString：把集合转为CSV格式字符串
tokenizeToStringArray：和split基本一样，但能自动去掉空白的单词
```

八. org.apache.commons.lang.ArrayUtils

```
contains：是否包含某字符串
addAll：添加整个数组
clone：克隆一个数组
isEmpty：是否空数组
add：向数组添加元素
subarray：截取数组
indexOf：查找某个元素的下标
isEquals：比较数组是否相等
toObject：基础类型数据数组转换为对应的Object数组
```

九. org.apache.commons.lang.StringEscapeUtils

```
参考十五：org.apache.commons.lang3.StringEscapeUtils
```

十. org.apache.http.client.utils.URLEncodedUtils

```
format：格式化参数，返回一个HTTP POST或者HTTP PUT可用application/x-www-form-urlencoded字符串parse：把String或者URI等转换为List<NameValuePair>
```

十一. org.apache.commons.codec.digest.DigestUtils

```
md5Hex：MD5加密，返回32位字符串
sha1Hex：SHA-1加密
sha256Hex：SHA-256加密
sha512Hex：SHA-512加密
md5：MD5加密，返回16位字符串
```

十二. org.apache.commons.collections.CollectionUtils

```
isEmpty：是否为空
select：根据条件筛选集合元素
transform：根据指定方法处理集合元素，类似List的map()
filter：过滤元素，雷瑟List的filter()
find：基本和select一样
collect：和transform 差不多一样，但是返回新数组
forAllDo：调用每个元素的指定方法
isEqualCollection：判断两个集合是否一致
```

十三. org.apache.commons.lang3.ArrayUtils

```
contains：是否包含某个字符串
addAll：添加整个数组
clone：克隆一个数组
isEmpty：是否空数组
add：向数组添加元素
subarray：截取数组
indexOf：查找某个元素的下标
isEquals：比较数组是否相等
toObject：基础类型数据数组转换为对应的Object数组
```

十四. org.apache.commons.beanutils.PropertyUtils

```
getProperty：获取对象属性值
setProperty：设置对象属性值
getPropertyDiscriptor：获取属性描述器
isReadable：检查属性是否可访问
copyProperties：复制属性值，从一个对象到另一个对象
getPropertyDiscriptors：获取所有属性描述器
isWriteable：检查属性是否可写
getPropertyType：获取对象属性类型
```

十五. org.apache.commons.lang3.StringEscapeUtils

```
unescapeHtml4：转义html
escapeHtml4：反转义html
escapeXml：转义xml
unescapeXml：反转义xml
escapeJava：转义unicode编码
escapeEcmaScript：转义EcmaScript字符
unescapeJava：反转义unicode编码
escapeJson：转义json字符
escapeXml10：转义Xml10
```

这个现在已经废弃了，建议使用commons-text包里面的方法。

十六. org.apache.commons.beanutils.BeanUtils

```
copyPeoperties：复制属性值，从一个对象到另一个对象
getProperty：获取对象属性值
setProperty：设置对象属性值
populate：根据Map给属性复制
copyPeoperty：复制单个值，从一个对象到另一个对象
cloneBean：克隆bean实例
```

现在你只要了解了以上16种最流行的工具类方法，你就不必要再自己写工具类了，不必重复造轮子。大部分工具类方法通过其名字就能明白其用途，如果不清楚的，可以看下别人是怎么用的，或者去网上查询其用法。



#### 十九、阿里祭出大器，Java代码检查插件

代码检测插件放到了github上：https://github.com/alibaba/p3c

Eclipse安装：Help-->install new softWare--><https://p3c.alibaba.com/plugin/eclipse/update>

安装完成后，项目右键-->阿里编码规约扫描。

或者在上方菜单栏选择切换 插件的语言，旁边也有个扫描的按钮。



#### 二十、Hibernate-Validator

Hibernate-Validator的相关依赖

如果项目的框架是spring boot的话，在spring-boot-starter-web 中已经包含了Hibernate-validator的依赖，我们点开spring-boot-starter-web的pom.xml则可以看到相关的依赖内容。

```
<dependencies>
	<dependency>
			<groupId>org.hibernate</groupId>
			<artifactId>hibernate-validator</artifactId>
	</dependency>
	<dependency>
			<groupId>com.fasterxml.jackson.core</groupId>
			<artifactId>jackson-databind</artifactId>
	</dependency>
</dependencies>
```

如果是其他的框架风格的话，引入如下的依赖就可以了。

```
<dependency>
    <groupId>org.hibernate</groupId>
    <artifactId>hibernate-validator</artifactId>
    <version>6.0.10.Final</version>
</dependency>
```

## 

> 以下代码环境均在Spring boot 1.5.10的版本下运行。

Hibernate-Validator的主要使用的方式就是注解的形式，并且是“零配置”的，无需配置也可以使用。下面用一个最简单的案例。

- Hibernate-Validator 最基本的使用

  1.添加一个普通的接口信息，参数是@RequestParam类型的，传入的参数是id，且id不能小于10。

```
@RestController
@RequestMapping("/example")
@Validated
public class ExampleController {

    /**
     *  用于测试
     * @param id id数不能小于10 @RequestParam类型的参数需要在Controller上增加@Validated
     * @return
     */
    @RequestMapping(value = "/info",method = RequestMethod.GET)
    public String test(@Min(value = 10, message = "id最小只能是10") @RequestParam("id")
                                   Integer id){
        return "恭喜你拿到参数了";
    }
}
```

##### 全局验证异常的处理

```java
@Slf4j
@ControllerAdvice
@Component
public class GlobalExceptionHandler {

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handle(ConstraintViolationException exception, HttpServletRequest request) {
        Set<ConstraintViolation<?>> violations = exception.getConstraintViolations();
        StringBuffer errorInfo = new StringBuffer();
        for (ConstraintViolation<?> item : violations) {
            /**打印验证不通过的信息*/
            errorInfo.append(item.getMessage());
            errorInfo.append(",");
        }
        log.error("{}接口参数验证失败，内容如下：{}",request.getRequestURI(),errorInfo.toString());
        return "您的请求失败，参数验证失败，失败信息如下："+ errorInfo.toString();
    }
}
```

3.一个简单的测试。

![验证失败的案例.png](http://img.mukewang.com/5b70e78300010c7013840682.png)

- 验证复杂参数的案例

  1.添加一个vo的实体信息。

```java
/**
 * 用户的vo类
 * @author dengyun
 */
@Data
public class ExampleVo {

    @NotBlank(message = "用户名不能为空")
    private String userName;

    @Range(min = 18,max = 60,message = "只能填报年龄在18~60岁的")
    private String age;
}
```

2.添加一个POST请求的接口。

```java
    /**
     * 用于测试
     * @param vo 按照vo的验证
     * @return
     */
    @RequestMapping(value = "/info1",method = RequestMethod.POST)
    public String test1(@Valid  @RequestBody ExampleVo vo){
        return "恭喜你拿到参数了";
    }
```

3.在全局异常拦截中添加验证处理的结果

```java
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handle(MethodArgumentNotValidException exception,HttpServletRequest request) {
        StringBuffer errorInfo=new StringBuffer();
        List<ObjectError> errors = exception.getBindingResult().getAllErrors();
        for(int i=0;i<errors.size();i++){
            errorInfo.append(errors.get(i).getDefaultMessage()+",");
        }
        log.error("{},接口参数验证失败：{}",request,errorInfo.toString());
        return "您的请求失败，参数验证失败，失败信息如下:"+errorInfo.toString();
    }
```

4.一个简单的测试

![复杂参数校验失败.png](http://img.mukewang.com/5b70e87f00014f2813000898.png)

> 我个人比较推荐使用全局异常拦截处理的方式去处理Hibernate-Validator的验证失败后的处理流程，这样能能减少Controller层或Services层的代码逻辑处理。虽然它也能在Controller中增加BindingResult的实例来获取数据，但是并不推荐。

更加灵活的运用

首先列举一下Hibernate-Validator所有的内置验证注解。

> [@Null](https://my.oschina.net/u/561366) 被注释的元素必须为 null
> [@NotNull](https://my.oschina.net/notnull) 被注释的元素必须不为 null
> @AssertTrue 被注释的元素必须为 true
> [@AssertFalse](https://my.oschina.net/u/2430840) 被注释的元素必须为 false
> @Min(value) 被注释的元素必须是一个数字，其值必须大于等于指定的最小值 @Max(value) 被注释的元素必须是一个数字，其值必须小于等于指定的最大值 @DecimalMin(value) 被注释的元素必须是一个数字，其值必须大于等于指定的最小值
> @DecimalMax(value) 被注释的元素必须是一个数字，其值必须小于等于指定的最大值
> @Size(max=, min=) 被注释的元素的大小必须在指定的范围内
> @Digits (integer, fraction) 被注释的元素必须是一个数字，其值必须在可接受的范围内
> @Past 被注释的元素必须是一个过去的日期
> @Future 被注释的元素必须是一个将来的日期
> @Pattern(regex=,flag=) 被注释的元素必须符合指定的正则表达式
> Hibernate Validator 附加的 constraint
> @NotBlank(message =) 验证字符串非null，且长度必须大于0
> @Email 被注释的元素必须是电子邮箱地址
> @Length(min=,max=) 被注释的字符串的大小必须在指定的范围内
> @NotEmpty 被注释的字符串的必须非空
> @Range(min=,max=,message=) 被注释的元素必须在合适的范围内

这些注解能适应我们绝大多数的验证场景，但是为了应对更多的可能性，我们需要增加注解功能配合Hibernate-Validator的其他的特性，来满足验证的需求。

1. 自定义注解

- 添加自定义注解

我们一定会用到这么一个业务场景，vo中的属性必须符合枚举类中的枚举。Hibernate-Validator中还没有关于枚举的验证规则，那么，我们则需要自定义一个枚举的验证注解。

```
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EnumCheckValidator.class)
public @interface EnumCheck {
    /**
     * 是否必填 默认是必填的
     * @return
     */
    boolean required() default true;
    /**
     * 验证失败的消息
     * @return
     */
    String message() default "枚举的验证失败";
    /**
     * 分组的内容
     * @return
     */
    Class<?>[] groups() default {};

    /**
     * 错误验证的级别
     * @return
     */
    Class<? extends Payload>[] payload() default {};

    /**
     * 枚举的Class
     * @return
     */
    Class<? extends Enum<?>> enumClass();

    /**
     * 枚举中的验证方法
     * @return
     */
    String enumMethod() default "validation";
}
```

- 注解的业务逻辑实现类

```
public class EnumCheckValidator implements ConstraintValidator<EnumCheck,Object> {
    private EnumCheck enumCheck;

    @Override
    public void initialize(EnumCheck enumCheck) {
        this.enumCheck =enumCheck;
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
        // 注解表明为必选项 则不允许为空，否则可以为空
        if (value == null) {
            return this.enumCheck.required()?false:true;
        }
        //最终的返回结果
        Boolean result=Boolean.FALSE;
        // 获取 参数的数据类型
        Class<?> valueClass = value.getClass();
        try {
            Method method = this.enumCheck.enumClass().getMethod(this.enumCheck.enumMethod(), valueClass);
            result = (Boolean)method.invoke(null, value);
            result= result == null ? false : result;
            //所有异常需要在开发测试阶段发现完毕
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }finally {
            return result;
        }
    }
}
```

- 编写枚举类

```
public enum  Sex{
    MAN("男",1),WOMAN("女",2);

    private String label;
    private Integer value;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    Sex(String label, int value) {
        this.label = label;
        this.value = value;
    }

    /**
     * 判断值是否满足枚举中的value
     * @param value
     * @return
     */
    public static boolean validation(Integer value){
        for(Sex s:Sex.values()){
            if(Objects.equals(s.getValue(),value)){
                return true;
            }
        }
        return false;
    }
}
```

- 使用方式

```
    @EnumCheck(message = "只能选男：1或女:2",enumClass = Sex.class)
    private Integer sex;
```

- 一个简单的测试

![自定义注解的验证.png](http://img.mukewang.com/5b70e96f000141a522440836.png)

> 我们甚至可以在自定义注解中做更加灵活的处理，甚至把与数据库的数据校验的也写成自定义注解，来进行数据验证的调用。

2. Hibernate-Validator的分组验证

同一个校验规则，不可能适用于所有的业务场景，对此，对每一个业务场景去编写一个校验规则，又显得特别冗余。这里我们刚好可以用到Hibernate-Validator的分组功能。

- 添加一个名为ValidGroupA的接口（接口内容可以是空的，所以就不列举代码）
- 添加一个需要分组校验的字段

```
@Data
public class ExampleVo {

    @NotNull(message = "主键不允许为空",groups = ValidGroupA.class)
    private Integer id;

    @NotBlank(message = "用户名不能为空",groups = Default.class)
    private String userName;
    
    @Range(min = 18,max = 60,message = "只能填报年龄在18~60岁的",groups = Default.class)
    private String age;

    @EnumCheck(message = "只能选男：1或女:2",enumClass = Sex.class,groups = Default.class)
    private Integer sex;
}
```

- 改动接口的内容

```
    @RequestMapping(value = "/info1",method = RequestMethod.POST)
    public String test1(@Validated({ValidGroupA.class,Default.class})  @RequestBody ExampleVo vo){
        return "恭喜你拿到参数了";
    }
```

这里我们可以注意一下，校验的注解由 **@Valid** 改成了 **@Validated**。

- 进行测试，保留ValidGroupA.class和去掉ValidGroupA.class的测试。
  - 保留ValidGroupA.class ![保留分组的测试.png](http://img.mukewang.com/5b70eb44000122e322440844.png)
  - 去掉ValidGroupA.class ![去掉分组后的测试.png](http://img.mukewang.com/5b70eb620001904222720874.png)



#####          单独异常处理

```java
package com.gwghk.gts2.client.models.vo;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MobiOrderDTO implements Serializable {
	
	@JsonProperty("transaction_type")
	private Integer type;
	
	@JsonProperty("transaction_status")
	private Integer status;

	private String pno;

	@JsonProperty("mobi_pno")
	private String mobiPno;

	@JsonProperty("currency_code")
	private String currency;

	@NotEmpty(message = "amount is not empty")
	private String amount;
}
```

```java
	@RequestMapping(value = "/transaction", method = RequestMethod.POST)
	@ResponseBody
	public ResultBean<Boolean> transactionCallback(@RequestBody @Valid MobiOrderDTO dto, BindingResult bindingResult) {
		ResultBean<Boolean> resultBean = new ResultBean<Boolean>(false);
		resultBean.setCode(ResultBean.FAIL);
		if (bindingResult.hasErrors()) {
			StringBuffer sb = new StringBuffer();
			for (ObjectError objectError : bindingResult.getAllErrors()) {
				sb.append(((FieldError) objectError).getField() + " : ").append(objectError.getDefaultMessage());
			}
			resultBean.setMsg(sb.toString());
			LOGGER.error(sb.toString());
			return resultBean;
		}
		return ManagerFactory.getInstance(true).getBackofficeManager(this.getSessionId()).transactionCallback(dto);
	}
```



#### 二十三、Disruptor

https://www.cnblogs.com/binarylei/p/9221560.html



#### 二十四、设计模式

https://www.cnblogs.com/binarylei/category/1159281.html



#### 二十五、Guava

https://www.cnblogs.com/wihainan/p/7091775.html

https://blog.csdn.net/dgeek/article/details/76221746

http://ifeve.com/google-guava-strings/

http://ifeve.com/google-guava-io/

```xml
<dependency>
    <groupId>com.google.guava</groupId>
    <artifactId>guava</artifactId>
    <version>18.0</version>
</dependency>
```

#### 二十六、Orika 实现bean 映射

https://blog.csdn.net/neweastsun/article/details/80559868?from=timeline&isappinstalled=0



#### 二十七、JodaTime



#### 二十八、FastJson

