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

十七、org.springframework.util.ReflectionUtils

十八、RandomUtils自从commons lang3.3，RandomStringUtils

```
/产生5位长度的随机字符串，中文环境下是乱码
RandomStringUtils.random(5);
//使用指定的字符生成5位长度的随机字符串
RandomStringUtils.random(5, new char[]{'a','b','c','d','e','f', '1', '2', '3'});
//生成指定长度的字母和数字的随机组合字符串
RandomStringUtils.randomAlphanumeric(5);
//生成随机数字字符串
RandomStringUtils.randomNumeric(5);
//生成随机[a-z]字符串，包含大小写
RandomStringUtils.randomAlphabetic(5);
//生成从ASCII 32到126组成的随机字符串
RandomStringUtils.randomAscii(4)
```

十九、SystemUtils

```
getJavaHome()
getJavaIoTmpDir
getUserDir()
getUserHome(),
```

二十、NumberUtils

二十一、ClassUtils  可以不用反射就可以操作java类

二十二、org.apache.commons.lang3.builder.ToStringBuilder

```java
class Person {
	String name;
	int age;
	boolean smoker;
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
	public boolean isSmoker() {
		return smoker;
	}
	public void setSmoker(boolean smoker) {
		this.smoker = smoker;
	}
}
```

```java
public static void main(String[] args) {

		Person p = new Person();
		p.setAge(10);
		p.setName("wukang");
		p.setSmoker(false);
		String a = new ToStringBuilder(p).append("name", p.getName()).append("age", p.getName()).toString();
		System.out.println(a);
	}
```



二十三、DateFormatUtils和DateUtils

```java
1.DateFormatUtils:

static String format(long millis, String pattern)
static String format(Date date, String pattern)

2.DateUtils:

static Date addDays(Date date, int amount)
addYears(Date date, int amount)
```

#### 十八、附 多返回值

org.apache.commons.lang3.tuple.ImmutablePair （两个返回值）

```java
public static void main(String[] args) {
		
ImmutablePair<String, Integer> immutablePair = new ImmutablePair<String, Integer>("test", 9527);  
	String left = immutablePair.left;
	Integer right = immutablePair.right;
	System.out.println(left);
	System.out.println(right);
}
```

org.apache.commons.lang3.tuple.ImmutableTriple（三个返回值）不可变

```java
public static void main(String[] args) {  //不可变  无set设置值
		
ImmutableTriple<String, Integer,String> immutableTriple = new ImmutableTriple<String, Integer,String>("test", 9527,"hello");  
	String left = immutableTriple.left;
	Integer middle = immutableTriple.middle;
	String right = immutableTriple.right;
	System.out.println(left);
	System.out.println(middle);
	System.out.println(right);
}
```

```java
public static void main(String[] args) { // 可变 因为有set设置值
		
		MutableTriple<String, Integer,String> mutableTriple = new MutableTriple<String, Integer,String>("test", 9527,"hello");  
         mutableTriple.setLeft("456");
		String left = mutableTriple.left;
		Integer middle = mutableTriple.middle;
		String right = mutableTriple.right;
		System.out.println(left);
		System.out.println(middle); 
		System.out.println(right);
	}
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

```java
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

```java
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

```java
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

```json
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



#### 二十九、对象转Map

```java
public static Map<String, String> PoConvertMap(Object obj){
		Map<String, String> map = new HashMap<String, String>();
		try {
			Class clazz = obj.getClass();
			//获取object的属性描述
			PropertyDescriptor[] pds = Introspector.getBeanInfo(clazz, Object.class).getPropertyDescriptors();
			for(PropertyDescriptor pd : pds){
				//获取属性名
				String name = pd.getName();
				//获取get方法
				Method readMethod = pd.getReadMethod();
				Object value = readMethod.invoke(obj);
				if(null != value){
					map.put(name, value.toString());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

PoConvertMap(propertiesVo);
```

或者

```java
package com.spi;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.Map;

public class Bean2Map {
	
	private static <T> Map<String, Object> transBeanToMap(T bean) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		String fieldName = null;
		Object fieldVal = null;
		try{
			BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor descriptor : propertyDescriptors) {
				fieldName = descriptor.getName();
				if ("class".equals(fieldName) || fieldName.isEmpty()) {
					continue;
				}
				fieldVal = descriptor.getReadMethod().invoke(bean);
				if (fieldVal == null) {
					fieldVal = "";
				}
				map.put(fieldName, fieldVal);
			}
			return map;
		}catch(Exception e){
		}
		return null;
	}
	public static void main(String[] args) {
		
		Map<String, Object> transBeanToMap = transBeanToMap(new UserInfo());
		System.out.println(transBeanToMap);	
	}
}
```

另外 apache的工具类也可以

org.apache.commons.beanutils.BeanMap（BeanMap不能实现值拷贝，不是此用途）

```java
	public static <T> T copy(T target, Object orig, boolean includedNull, String... ignoreProperties) {
		try {
			if(includedNull && ignoreProperties == null){
                 //apache的import org.apache.commons.beanutils.PropertyUtils;
				PropertyUtils.copyProperties(target, orig);
			}else{
				List<String> ignoreFields = new ArrayList<String>();
				if(ignoreProperties != null){
					for(String p : ignoreProperties){
						ignoreFields.add(p);
					}	
				}
				if(!includedNull){
                    //import org.apache.commons.beanutils.BeanMap;
					Map<String, Object> parameterMap = new BeanMap(orig);
					for (Entry<String, Object> entry : parameterMap.entrySet()) {
						if (entry.getValue() == null) {
							String fieldName = entry.getKey();
							if(!ignoreFields.contains(fieldName)){
								ignoreFields.add(fieldName);	
							}
						}
					}					
				}
				/*System.out.println("ignore" + nullFields);*/
				org.springframework.beans.BeanUtils.copyProperties(orig, target, ignoreFields.toArray(new String[ignoreFields.size()]));				
			}
			return target;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}
```

或者org.apache.commons.beanutils.BeanUtils

```java
Person person1=new Person();  
person1.setName("name1");  
person1.setSex("sex1");  
Map<String, String> map=null;  
try {  
    map = BeanUtils.describe(person1);  
}
```



#### 三十、Map转对象

org.apache.commons.beanutils.BeanUtils

```java
  public static <T> T map2Bean(Map<String, String> map, Class<T> class1) {  
        T bean = null;  
        try {  
            bean = class1.newInstance();  
            BeanUtils.populate(bean, map);  
        } catch (InstantiationException e) {  
            e.printStackTrace();  
        } catch (IllegalAccessException e) {  
            e.printStackTrace();  
        } catch (InvocationTargetException e) {  
            e.printStackTrace();  
        }  
        return bean;  
    }  
```



#### 三十一、SPI

```java
package com.spi;

public interface Animal {
	  String eat(String a);
}
```

```java
package com.spi;

public class Dog implements Animal {

    @Override
    public String eat(String a) {
		return "Dog eating..."+a;
    }
}
```

```java
package com.spi;

public class Pig implements Animal {

    @Override
    public String eat(String a) {
		return "Pig eating..."+a;
    }
}
```

```java
package com.spi;

import java.util.Iterator;
import java.util.ServiceLoader;

public class SPITest {
	
	 public static void main(String[] args) {
	        ServiceLoader<Animal> load = ServiceLoader.load(Animal.class);
	        Iterator<Animal> iterator = load.iterator();
	        while (iterator.hasNext()) {
	            Animal animal = iterator.next();
	            System.out.println("calss:" + animal.getClass().getName() +
	                    "|method:eat" + animal.eat("world"));
	        }
	    }
}
```



#### 三十二、Json转换

##### 一、jackson

```xml
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.5.3</version>
</dependency>
```



```java
import com.fasterxml.jackson.annotation.JsonProperty;  
public class Student { 
   
    @JsonProperty("name") 
    private String trueName; 
    public String getTrueName() { 
        return trueName; 
    } 
    public void setTrueName(String trueName) { 
        this.trueName = trueName; 
    } 
} 
```

```java
import com.fasterxml.jackson.core.JsonProcessingException; 
import com.fasterxml.jackson.databind.ObjectMapper; 
   
public class Main { 
    public static void main(String[] args) throws JsonProcessingException { 
        Student student = new Student(); 
        student.setTrueName("张三"); 
        System.out.println(new ObjectMapper().writeValueAsString(student)); 
    } 
}
```

输出

```json
{"name":"张三"} 
```

@JsonProperty不仅仅是在序列化的时候有用，反序列化的时候也有用，比如有些接口返回的是json字符串，命名又不是标准的驼峰形式，在映射成对象的时候，将类的属性上加上@JsonProperty注解，
里面写上返回的json串对应的名字

**1.1 忽略属性**

此注解是类注解，作用是json序列化时将java bean中的一些属性忽略掉，序列化和反序列化都受影响。

@JsonIgnoreProperties

```java
  //生成json时将name和age属性过滤
  @JsonIgnoreProperties({"name"},{"age"})
  public class  user {
  private  String name;
  private int age;
}
```

@JsonIgnore

此注解用于属性或者方法上（最好是属性上），作用和上面的@JsonIgnoreProperties一样。

```java
public class user { 
private String n；
@JsonIgnore 
private int age; 
} 	
```

**1.2 格式化日期**

@JsonFormat

此注解用于属性或者方法上（最好是属性上），可以方便的把Date类型直接转化为我们想要的模式，比如

```java
@JsonFormat(pattern = “yyyy-MM-dd HH-mm-ss”)
private Date date;
```

**1.3 @JsonSerialize**

例如：java时间戳是毫秒，现在转为秒。序列化时createTime是10位的秒

```java
package com.whf.utils.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.util.Date;
import java.io.IOException;
/**
 *该类可以将data转换成long类型
 */
public class Data2LongSerizlizer extends JsonSerializer<Date> {

    @Override
    public void serialize(Date date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        //将毫秒值转换成秒变成long型数据返回
        jsonGenerator.writeNumber(date.getTime()/1000);
    }
```

```java
//创建时间
@JsonSerialize(using = Data2LongSerizlizer.class )
private Date createTime;
//更新时间
@JsonSerialize(using = Data2LongSerizlizer.class )
private Date updateTime;
```

此注解用于属性或者getter方法上，用于在序列化时嵌入我们自定义的代码，比如序列化一个double时在其后面限制两位小数点。

**1.4 @JsonDeserialize**

此注解用于属性或者setter方法上，用于在反序列化时可以嵌入我们自定义的代码，类似于上面的@JsonSerialize

**1.5@Transient**

如果一个属性并非数据库表的字段映射，就务必将其标示为@Transient，否则ORM框架默认其注解为@Basic；

```
//表示该字段在数据库表中没有
@Transient 
public int getAge() { 
　return 1+1; 
}
```



##### 二、fasejson （Alibaba）

```xml
<dependency>
  <groupId>com.alibaba</groupId>
  <artifactId>fastjson</artifactId>
  <version>1.1.23</version>
</dependency>
```

```java
//json字符串-简单对象型
private static final String  JSON_OBJ_STR = "{\"studentName\":\"lily\",\"studentAge\":12}";
//json字符串-数组类型
private static final String  JSON_ARRAY_STR = "[{\"studentName\":\"lily\",\"studentAge\":12},{\"studentName\":\"lucy\",\"studentAge\":15}]";
//复杂格式json字符串
private static final String  COMPLEX_JSON_STR = "{\"teacherName\":\"crystall\",\"teacherAge\":27,\"course\":{\"courseName\":\"english\",\"code\":1270},\"students\":[{\"studentName\":\"lily\",\"studentAge\":12},{\"studentName\":\"lucy\",\"studentAge\":15}]}";
```

**一、JSON格式字符串与JSON对象之间的转换**

```java
/**
     * json字符串-简单对象型与JSONObject之间的转换
     */
    public static void testJSONStrToJSONObject(){

        JSONObject jsonObject = JSON.parseObject(JSON_OBJ_STR);
        //JSONObject jsonObject1 = JSONObject.parseObject(JSON_OBJ_STR); //因为JSONObject继承了JSON，所以这样也是可以的
        System.out.println(jsonObject.getString("studentName")+":"+jsonObject.getInteger("studentAge"));

}
```

**二、json字符串-数组类型与JSONArray之间的转换**

```java
/**
     * json字符串-数组类型与JSONArray之间的转换
     */
    public static void testJSONStrToJSONArray(){

        JSONArray jsonArray = JSON.parseArray(JSON_ARRAY_STR);
        //JSONArray jsonArray1 = JSONArray.parseArray(JSON_ARRAY_STR);//因为JSONArray继承了JSON，所以这样也是可以的

        //遍历方式1
        int size = jsonArray.size();
        for (int i = 0; i < size; i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            System.out.println(jsonObject.getString("studentName")+":"+jsonObject.getInteger("studentAge"));
        }

        //遍历方式2
        for (Object obj : jsonArray) {
            JSONObject jsonObject = (JSONObject) obj;
            System.out.println(jsonObject.getString("studentName")+":"+jsonObject.getInteger("studentAge"));
        }
    }
```

**三、复杂json格式字符串与JSONObject之间的转换**

```java
/**
     * 复杂json格式字符串与JSONObject之间的转换
     */
    public static void testComplexJSONStrToJSONObject(){

        JSONObject jsonObject = JSON.parseObject(COMPLEX_JSON_STR);
        //JSONObject jsonObject1 = JSONObject.parseObject(COMPLEX_JSON_STR);//因为JSONObject继承了JSON，所以这样也是可以的
        
        String teacherName = jsonObject.getString("teacherName");
        Integer teacherAge = jsonObject.getInteger("teacherAge");
        JSONObject course = jsonObject.getJSONObject("course");
        JSONArray students = jsonObject.getJSONArray("students");
    }
```

**四、JSON格式字符串与javaBean之间的转换**

```java
public class Student {

    private String studentName;
    private Integer studentAge;
    .... getter setter
}
```

```java
public class Course {

    private String courseName;
    private Integer code;
    .... getter setter
}
```

```java
public class Teacher {

    private String teacherName;
    private Integer teacherAge;
    private Course course;
    private List<Student> students;
    .... getter setter
}
```

**4.1字符串-简单对象型与javaBean之间的转换**

```java
/**
 * json字符串-简单对象与JavaBean_obj之间的转换
 */
public static void testJSONStrToJavaBeanObj(){

    Student student = JSON.parseObject(JSON_OBJ_STR, new TypeReference<Student>() {});
//Student student1 = JSONObject.parseObject(JSON_OBJ_STR, new TypeReference<Student>() {});//因为JSONObject继承了JSON，所以这样也是可以的
    System.out.println(student.getStudentName()+":"+student.getStudentAge());
}
```

或者

```java
User user1 = JSON.parseObject(userJson, User.class);
```

或者

```java
Map<String, Object> map1 = JSON.parseObject(mapJson, new TypeReference<Map<String, Object>>(){});
System.out.println(map1.get("key1"));
System.out.println(map1.get("key2"));
```



**4.2json字符串-数组类型与javaBean之间的转换**

```java
/**
 * json字符串-数组类型与JavaBean_List之间的转换
 */
public static void testJSONStrToJavaBeanList(){
        
  ArrayList<Student> students = JSON.parseObject(JSON_ARRAY_STR, new TypeReference<ArrayList<Student>>() {});
//ArrayList<Student> students1 = JSONArray.parseObject(JSON_ARRAY_STR, new TypeReference<ArrayList<Student>>() {});//因为JSONArray继承了JSON，所以这样也是可以的
   for (Student student : students) {
       System.out.println(student.getStudentName()+":"+student.getStudentAge());
    }
}
```

或者

```java
 List<Map> list1 = JSON.parseArray(listJson, Map.class);
 for(Map<String, Object> map : list1){
    System.out.println(map.get("key1"));
    System.out.println(map.get("key2"));         
}
```

**4.3复杂json格式字符串与与javaBean之间的转换**

```java
/**
 * 复杂json格式字符串与JavaBean_obj之间的转换
 */
public static void testComplexJSONStrToJavaBean(){

  Teacher teacher = JSON.parseObject(COMPLEX_JSON_STR, new TypeReference<Teacher>() {});
//Teacher teacher1 = JSON.parseObject(COMPLEX_JSON_STR, new TypeReference<Teacher>() {});//因为JSONObject继承了JSON，所以这样也是可以的
   String teacherName = teacher.getTeacherName();
   Integer teacherAge = teacher.getTeacherAge();
   Course course = teacher.getCourse();
   List<Student> students = teacher.getStudents();
}
```

1，对于JSON对象与JSON格式字符串的转换可以直接用 toJSONString()这个方法。JSONObject.toJSONString()

2，javaBean与JSON格式字符串之间的转换要用到：

​     String beanJson = JSON.toJSONString(student);

​     String mapJson =  JSON.toJSONString(map);

​     String listJson = JSON.toJSONString(list);

3，javaBean与jsonObject间的转换使用：JSON.toJSON(student)，然后使用强制类型转换，JSONObject或者JSONArray。

4，JsonObject转javaBean。  Student student=JSONObject.toJavaObject(jsonObject , Student .class);

**4.4 格式化字符串**

String listJson = JSON.toJSONString(list, true);

或者

String listJson = JSON.toJSONString(list, SerializerFeature.PrettyFormat);

```json
[
    {
        "key1": "One",
        "key2": "Two"
    },
    {
        "key3": "Three",
        "key4": "Four"
    }
]
```

**4.5日期格式化**

```java
String dateJson = JSON.toJSONString(new Date());
System.out.println(dateJson);
```

输出结果

```javascript
1401370199040
```

```
String dateJson = JSON.toJSONString(new Date(), SerializerFeature.WriteDateUseDateFormat);
System.out.println(dateJson);
```

输出结果

```
2014-05-29 21:36:24
```

```java
String dateJson = JSON.toJSONStringWithDateFormat(new Date(), "yyyy-MM-dd HH:mm:ss.SSS");
System.out.println(dateJson);
```

```java
2014-05-29 21:47:00.154
```

或者

```java
SerializeConfig mapping = new SerializeConfig(); 
mapping.put(Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss")); 
Date date = new Date();
String text = JSON.toJSONString(date, mapping);
```

**4.6使用单引号**

```
String listJson = JSON.toJSONString(list, SerializerFeature.UseSingleQuotes);
```

```
[{'key1':'One','key2':'Two'},{'key3':'Three','key4':'Four'}]
```

**4.7输出Null字段**

```json
Map<String, Object> map = new HashMap<String,Object>();
String b = null;
Integer i = 1;
map.put("a", b);
map.put("b", i);
String listJson = JSON.toJSONString(map, SerializerFeature.WriteMapNullValue);
```

输出

```
{"a":null,"b":1}
```

**4.8 序列化时写入类信息**

```json
User user = new User();
user.setAge(18);
user.setUserName("李四");
String listJson = JSON.toJSONString(user, SerializerFeature.WriteClassName);
```

```
{"@type":"User","age":18,"userName":"李四"}
```

由于序列化带了类型信息，使得反序列化时能够自动进行类型识别。

```json
User user1 = (User) JSON.parse(listJson);
System.out.println(user1.getAge());
```

如果User序列化是没有加入类型信息（SerializerFeature.WriteClassName），按照这样的做法就会报错（java.lang.ClassCastException）。

**4.9 Map转成JSONObject**

```json
Map<String, Object> map = new HashMap<String, Object>();
map.put("key1", "One");
map.put("key2", "Two");
JSONObject j = new JSONObject(map);
j.put("key3", "Three");
System.out.println(j.get("key1"));
System.out.println(j.get("key2"));
System.out.println(j.get("key3"));
```

**4.10将List对象转成JSONArray**

```java
List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
Map<String, Object> map = new HashMap<String, Object>();
map.put("key1", "One");
map.put("key2", "Two");
Map<String, Object> map2 = new HashMap<String, Object>();
map2.put("key1", "Three");
map2.put("key2", "Four");
list.add(map);
list.add(map2);
JSONArray j = JSONArray.parseArray(JSON.toJSONString(list));
 for(int i=0; i<j.size(); i++){
    System.out.println(j.get(i));
 }
```

```json
{"key1":"One","key2":"Two"}
{"key1":"Three","key2":"Four"}
```

**4.11 转Json字符串时的特性**

```
String objJson = JSON.toJSONString(Object object, SerializerFeature... features) 
```

![img](http://images.cnitblog.com/i/637684/201405/291336016976905.png)

![img](http://images.cnitblog.com/i/637684/201405/291336163067908.png)

 **4.12过滤不需要的字段或者只要某些字段**

 第一种：在对象响应字段前加注解，这样生成的json也不包含该字段

```
@JSONField(serialize=false)  
private String name;  
```

第二种：在对象对应字段前面加transient，表示该字段不用序列化，即在生成json的时候就不会包含该字段了

```
private transient String name;  
```

第三种：使用fastjson的拦截器

```java
PropertyFilter profilter = new PropertyFilter(){  
    @Override  
    public boolean apply(Object object, String name, Object value) {  
       if(name.equalsIgnoreCase("last")){  
        //false表示last字段将被排除在外  
        return false;  
       }  
      return true;  
    }  
  };  
 json = JSON.toJSONString(user, profilter);  
 System.out.println(json);   
```

第四种，直接填写属性

```java
SimplePropertyPreFilter filter = new SimplePropertyPreFilter(TTown.class, "id","townname");  
response.getWriter().write(JSONObject.toJSONString(townList,filter));   
```

**4.13 @ JSONField注解**

@JSONField
作用：在字段和方法上
1.Field:@JSONField作用在Field时，其**name**不仅定义了**输入key**的名称，同时也定义了**输出的名称**。
2.作用在setter和getter方法上

当作用在setter方法上时，就相当于根据 name 到 json中寻找对应的值，并调用该setter对象赋值。

当作用在getter上时，在bean转换为json时，其key值为name定义的值。

```java
/**
 * bean 转json 时会把bean中的name转换为project_name
 */
 @JSONField(name="project_name")
 public String getName() {
      return name;
 }
 /**
  * son 转bean 时会把json中的project_name值赋值给name
  */
 @JSONField(name="project_name")
 public void setName(String name) {
     name= name;
}
```

**3**.format :用在Date类型的字段来格式化时间格式

```java
public class A {  
   // 配置date序列化和反序列使用yyyyMMdd日期格式  
   @JSONField(format="yyyy-MM-dd")  
   public Date date;  
 }
```

**4**.布尔类型:serialize和deserialize

　　  在序列化的时候就不包含这个字段了。deserialize与之相反。但是有一点需要注意，当字段为final的时候注解放在字段上是不起作用的，这时候应该放在get或set方法上。

```
@JSONField(serialize=false) 
private String name
```

**5.**serialzeFeatures 属性:fastjson默认的序列化规则是当字段的值为null的时候，是不会序列化这个字段

```json
{"name":"LiSi","age":18,"address":null}
```

```json
对象序列化下边的类，结果是：{"name":"LiSi","age":18}
```

```java
Student s =  new Student ();
s.setName("LiSi");
s.setAge(18);
s.setAdderss(null);
```

**6**.SerializerFeature枚举：

```java
@JSONField(serialzeFeatures=SerializerFeature.WriteMapNullValue)
private String address;
```

当value的值为null的时候，依然会把它的值序列化出来： {"name":"LiSi","age":18,"address":null}

当字段类型为int类型时，会序列化成 0，需要把类型改成Integer

#### 二十三 、**不可变对象**

类是final的，并且中所有类属性都是final的，不提供设置值的setter方法

```java

public final class FinalTest {
    private final String NAME = "name";
    private final String AGE  = "age";
 
  public FinalTest(final String name, final String age) {
        this.NAME = name;
        this.AGE = age;
    }
    public String getNAME() {
        return NAME;
    }
 
    public String getAGE() {
        return AGE;
    }
}
```

#### 二十四、反射机制

ReflactionUtil工具类和原生的工具类

#### 二十五、JVM

https://www.cnblogs.com/xifengxiaoma/p/9402747.html

**JVM内存**

JVM内存结构主要有三大块：堆内存、方法区和栈

堆内存：是JVM中最大的一块由年轻代和老年代组成，而年轻代内存又被分成三部分，Eden空间、From Survivor空间、To Survivor空间,默认情况下年轻代按照8:1:1的比例来分配。此内存区域的唯一目的就是存放对象实例，几乎所有的对象实例都在这里分配内存。如果在堆中没有内存完成实例分配，并且堆也无法再扩展时，将会抛出OutOfMemoryError异常。

方法区：存储类信息、常量、静态变量等数据，是线程共享的区域，为与Java堆区分，方法区还有一个别名Non-Heap(非堆)；与Java堆一样，是各个线程共享的内存区域，它用于存储已被虚拟机加载的类信息、常量、静态变量、即时编译器编译后的代码等数据。虽然Java虚拟机规范把方法区描述为堆的一个逻辑部分，但是它却有一个别名叫做Non-Heap（非堆），目的应该是与Java堆区分开来。当方法区无法满足内存分配需求时，将抛出OutOfMemoryError异常。 

栈：又分为java虚拟机栈和本地方法栈主要用于方法的执行，线程私有的，它的生命周期与线程相同。每个方法被执行的时候都会同时创建一个栈帧（Stack Frame）用于存储局部变量表、操作栈、动态链接、方法出口等信息。每一个方法被调用直至执行完成的过程，就对应着一个栈帧在虚拟机栈中从入栈到出栈的过程。在Java虚拟机规范中，对这个区域规定了两种异常状况：如果线程请求的栈深度大于虚拟机所允许的深度，将抛出StackOverflowError异常；如果虚拟机栈可以动态扩展（当前大部分的Java虚拟机都可动态扩展，只不过Java虚拟机规范中也允许固定长度的虚拟机栈），当扩展时无法申请到足够的内存时会抛出OutOfMemoryError异常。

**控制参数**
-Xms设置堆的最小空间大小。

-Xmx设置堆的最大空间大小。

-XX:NewSize设置新生代最小空间大小。

-XX:MaxNewSize设置新生代最大空间大小。

-XX:PermSize设置永久代最小空间大小。

-XX:MaxPermSize设置永久代最大空间大小。

-Xss设置每个线程的堆栈大小。

没有直接设置老年代的参数，但是可以设置堆空间大小和新生代空间大小两个参数来间接控制。

老年代空间大小=堆空间大小-年轻代大空间大小

**类加载机制**

类的加载指的是将类的.class文件中的二进制数据读入到内存中，将其放在运行时数据区的方法区内，然后在堆区创建一个java.lang.Class对象，用来封装类在方法区内的数据结构。类的加载的最终产品是位于堆区中的Class对象，Class对象封装了类在方法区内的数据结构，并且向Java程序员提供了访问方法区内的数据结构的接口。

**类的加载过程**

类加载的过程包括了加载、验证、准备、解析、初始化五个阶段。在这五个阶段中，加载、验证、准备和初始化这四个阶段发生的顺序是确定的，而解析阶段则不一定，它在某些情况下可以在初始化阶段之后开始，这是为了支持Java语言的运行时绑定（也成为动态绑定或晚期绑定）。另外注意这里的几个阶段是按顺序开始，而不是按顺序进行或完成，因为这些阶段通常都是互相交叉地混合进行的，通常在一个阶段执行的过程中调用或激活另一个阶段。

•加载：查找并加载类的二进制数据

   加载时类加载过程的第一个阶段，在加载阶段，虚拟机需要完成以下三件事情：

​    1、通过一个类的全限定名来获取其定义的二进制字节流。

​    2、将这个字节流所代表的静态存储结构转化为方法区的运行时数据结构。

​    3、在Java堆中生成一个代表这个类的java.lang.Class对象，作为对方法区中这些数据的访问入口。

​    相对于类加载的其他阶段而言，加载阶段（准确地说，是加载阶段获取类的二进制字节流的动作）是可控性最强的阶段，因为开发人员既可以使用系统提供的类加载器来完成加载，也可以自定义自己的类加载器来完成加载。

​    加载阶段完成后，虚拟机外部的 二进制字节流就按照虚拟机所需的格式存储在方法区之中，而且在Java堆中也创建一个java.lang.Class类的对象，这样便可以通过该对象访问方法区中的这些数据。

•连接

 – 验证：确保被加载的类的正确性

验证是连接阶段的第一步，这一阶段的目的是为了确保Class文件的字节流中包含的信息符合当前虚拟机的要求，并且不会危害虚拟机自身的安全。验证阶段大致会完成4个阶段的检验动作：

文件格式验证：验证字节流是否符合Class文件格式的规范；例如：是否以0xCAFEBABE开头、主次版本号是否在当前虚拟机的处理范围之内、常量池中的常量是否有不被支持的类型。

元数据验证：对字节码描述的信息进行语义分析（注意：对比javac编译阶段的语义分析），以保证其描述的信息符合Java语言规范的要求；例如：这个类是否有父类，除了java.lang.Object之外。

字节码验证：通过数据流和控制流分析，确定程序语义是合法的、符合逻辑的。

符号引用验证：确保解析动作能正确执行。

验证阶段是非常重要的，但不是必须的，它对程序运行期没有影响，如果所引用的类经过反复验证，那么可以考虑采用-Xverifynone参数来关闭大部分的类验证措施，以缩短虚拟机类加载的时间。

 

 – 准备：为类的静态变量分配内存，并将其初始化为默认值

   准备阶段是正式为类变量分配内存并设置类变量初始值的阶段，这些内存都将在方法区中分配。对于该阶段有以下几点需要注意：

​    1、这时候进行内存分配的仅包括类变量（static），而不包括实例变量，实例变量会在对象实例化时随着对象一块分配在Java堆中。

​    2、这里所设置的初始值通常情况下是数据类型默认的零值（如0、0L、null、false等），而不是被在Java代码中被显式地赋予的值。

   假设一个类变量的定义为：public static int value = 3；

   那么变量value在准备阶段过后的初始值为0，而不是3，因为这时候尚未开始执行任何Java方法，而把value赋值为3的putstatic指令是在程序编译后，存放于类构造器<clinit>（）方法之中的，所以把value赋值为3的动作将在初始化阶段才会执行。

```
· 这里还需要注意如下几点：
· 对基本数据类型来说，对于类变量（static）和全局变量，如果不显式地对其赋值而直接使用，则系统会为其赋予默认的零值，而对于局部变量来说，在使用前必须显式地为其赋值，否则编译时不通过。
· 对于同时被static和final修饰的常量，必须在声明的时候就为其显式地赋值，否则编译时不通过；而只被final修饰的常量则既可以在声明时显式地为其赋值，也可以在类初始化时显式地为其赋值，总之，在使用前必须为其显式地赋值，系统不会为其赋予默认零值。
· 对于引用数据类型reference来说，如数组引用、对象引用等，如果没有对其进行显式地赋值而直接使用，系统都会为其赋予默认的零值，即null。
· 如果在数组初始化时没有对数组中的各元素赋值，那么其中的元素将根据对应的数据类型而被赋予默认的零值。
```

3、如果类字段的字段属性表中存在ConstantValue属性，即同时被final和static修饰，那么在准备阶段变量value就会被初始化为ConstValue属性所指定的值。

   假设上面的类变量value被定义为： public static final int value = 3；

   编译时Javac将会为value生成ConstantValue属性，在准备阶段虚拟机就会根据ConstantValue的设置将value赋值为3。回忆上一篇博文中对象被动引用的第2个例子，便是这种情况。我们可以理解为static final常量在编译期就将其结果放入了调用它的类的常量池中

 – 解析：把类中的符号引用转换为直接引用

解析阶段是虚拟机将常量池内的符号引用替换为直接引用的过程，解析动作主要针对类或接口、字段、类方法、接口方法、方法类型、方法句柄和调用点限定符7类符号引用进行。符号引用就是一组符号来描述目标，可以是任何字面量。

直接引用就是直接指向目标的指针、相对偏移量或一个间接定位到目标的句柄。

•初始化

 初始化，为类的静态变量赋予正确的初始值，JVM负责对类进行初始化，主要对类变量进行初始化。在Java中对类变量进行初始值设定有两种方式：

  ①声明类变量是指定初始值

  ②使用静态代码块为类变量指定初始值

 JVM初始化步骤

 1、假如这个类还没有被加载和连接，则程序先加载并连接该类

 2、假如该类的直接父类还没有被初始化，则先初始化其直接父类

 3、假如类中有初始化语句，则系统依次执行这些初始化语句

类初始化时机：只有当对类的主动使用的时候才会导致类的初始化，类的主动使用包括以下六种：

– 创建类的实例，也就是new的方式

– 访问某个类或接口的静态变量，或者对该静态变量赋值

– 调用类的静态方法

– 反射（如Class.forName(“com.shengsiyuan.Test”)）

– 初始化某个类的子类，则其父类也会被初始化

– Java虚拟机启动时被标明为启动类的类（Java Test），直接使用java.exe命令来运行某个主类

 

结束生命周期

•在如下几种情况下，Java虚拟机将结束生命周期

– 执行了System.exit()方法

– 程序正常执行结束

– 程序在执行过程中遇到了异常或错误而异常终止

– 由于操作系统出现错误而导致Java虚拟机进程终止



**Class.forName()和ClassLoader.loadClass()区别**

Class.forName()：将类的.class文件加载到jvm中之外，还会对类进行解释，执行类中的static块；

ClassLoader.loadClass()：只干一件事情，就是将.class文件加载到jvm中，不会执行static中的内容,只有在newInstance才会去执行static块。

注：

Class.forName(name, initialize, loader)带参函数也可控制是否加载static块。并且只有调用了newInstance()方法采用调用构造函数，创建类的对象 。

#### 二十六、jconsole

直接在jdk/bin目录下点击jconsole.exe即可启动

在弹出的框中可以选择本机的监控本机的java应用，也可以选择远程的java服务来监控，如果监控远程服务需要在tomcat启动脚本中添加如下代码：

```
 -Dcom.sun.management.jmxremote.port=6969  
 -Dcom.sun.management.jmxremote.ssl=false  
 -Dcom.sun.management.jmxremote.authenticate=false
```

连接进去之后，就可以看到jconsole概览图和主要的功能：概述、内存、线程、类、VM、MBeans

#### 二十七、VisualVM（强大）

jdk/bin目录下面双击jvisualvm.exe既可使用

插件的关注点都不同，有的主要监控GC，有的主要监控内存，有的监控线程等。

如何安装：

> 1、从主菜单中选择“工具”>“插件”。
> 2、在“可用插件”标签中，选中该插件的“安装”复选框。单击“安装”。
> 3、逐步完成插件安装程序。

因为VisualVM的插件太多，我这里主要介绍三个我主要使用几个：监控、线程、Visual GC

Visual GC 是常常使用的一个功能，可以明显的看到年轻代、老年代的内存变化，以及gc频率、gc的时间等。

VisualVM非常多的其它功能，可以分析dump的内存快照，dump出来的线程快照并且进行分析等，还有其它很多的插件大家可以去探索（线程dump和堆dump）



#### 二十八、TrueLicense实现产品License验证功能

https://www.cnblogs.com/xifengxiaoma/p/9377654.html



#### 二十九、SQL脚本文件执行器

https://www.cnblogs.com/xifengxiaoma/p/9401079.html



#### 三十、构造方法中this调用其他构造方法

```java
public class UserTest {

	UserTest() {

	}
	UserTest(int a) {
		this(a,"0");
	}
	UserTest(int a, String b) {
		
	}
}
```

#### 三十一、Spring事件机制

https://www.cnblogs.com/zhangxiaoguang/p/spring-notification.html



#### 三十二、泛型

https://segmentfault.com/a/1190000014824002

我们常见的如T、E、K、V  **?** 等形式的参数常用于表示泛型形参

```java

public class Result<T> {

	private boolean success;
	private T result;
	private Exception error;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public T getResult() {
		return result;
	}

	public void setResult(T result) {
		this.result = result;
	}

	public Exception getError() {
		return error;
	}

	public void setError(Exception error) {
		this.error = error;
	}

	public static void main(String[] args) {
		Result<UserTest> result = new Result<UserTest>();
		result.setResult(new UserTest());
		System.out.println(result.getResult());
	}
}
```

作用在构造方法上

```java

public class User<T> {

	private T data;

	public User(T data) {
		this.data = data;
	}

	public T getData() {
		return data;
	}

	public static void main(String[] args) {

		User<String> name = new User<String>("corn");
		User<Integer> age = new User<Integer>(712);
		System.out.println("name class:" + name.getClass());
		System.out.println("age class:" + age.getClass());
		System.out.println(name.getClass() == age.getClass()); // true
	}
}
```

假设User<Number>在逻辑上可以视为User<Integer>的父类，那么//1和//2处将不会有错误提示了，那么问题就出来了，通过getData()方法取出数据时到底是什么类型呢？Integer? Float? 还是Number？且由于在编程过程中的顺序不可控性，导致在必要的时候必须要进行类型判断，且进行强制类型转换。显然，这与泛型的理念矛盾，因此，**在逻辑上User<Number>不能视为User<Integer>的父类。**

错误代码：

```java

public class User<T> {

	private T data;

	public User(T data) {
		this.data = data;
	}

	public T getData() {
		return data;
	}

	public static void main(String[] args) {

		User<Number> name = new User<Number>(99);
		User<Integer> age = new User<Integer>(712);
		getData(name);
		getData(age);//The method getData(User<Number>) in the type User<T> is not applicable for the arguments (User<Integer>)
	}

	public static void getData(User<Number> data) {
		System.out.println("data :" + data.getData());
	}
}
```

正确代码：**类型通配符一般是使用 ? 代替具体的类型实参**

```java

public class User<T> {

	private T data;

	public User(T data) {
		this.data = data;
	}

	public T getData() {
		return data;
	}

	public static void main(String[] args) {

		User<String> corn = new User<String>("corn");
		User<Number> name = new User<Number>(99);
		User<Integer> age = new User<Integer>(712);
		getData(corn);
		getData(name);
		getData(age);
	}

	public static void getData(User<?> data) {
		System.out.println("data :" + data.getData());
	}
}
```



**类型通配符上限和类型通配符下限**

对类型实参又有进一步的限制：只能是Number类及其子类。此时，需要用到类型通配符上限。

上限：

```java

public class User<T> {

	private T data;

	public User(T data) {
		this.data = data;
	}

	public T getData() {
		return data;
	}

	public static void main(String[] args) {

		User<String> corn = new User<String>("corn");
		User<Number> name = new User<Number>(99);
		User<Integer> age = new User<Integer>(712);
		getData(corn);//1处调用将出现错误提示The method getData(User<? extends Number>) in the type User<T> is not applicable for the arguments (User<String>)
		getData(name);
		getData(age);
	}

	public static void getData(User<? extends Number> data) {
		System.out.println("data :" + data.getData());
	}
}
```

下限：

```java

public class User<T> {

	private T data;

	public User(T data) {
		this.data = data;
	}

	public T getData() {
		return data;
	}

	public static void main(String[] args) {

		User<String> corn = new User<String>("corn");
		User<Number> name = new User<Number>(99);
		User<Integer> age = new User<Integer>(712);
		getData(corn);//①
		getData(name);
		getData(age);//②
	}
    
    //① ②出现The method getData(User<? super Number>) in the type User<T> is not applicable for the arguments (User<String>)

	public static void getData(User<? super Number> data) {
		System.out.println("data :" + data.getData());
	}
}
```

**自定义泛型接口**     接口中泛型字母只能使用在方法中，不能使用在全局常量中

```java
/**
 * 自定义泛型接口
 *
 * 接口中泛型字母只能使用在方法中，不能使用在全局常量中
 *
 * @author Administrator
 * @param <T>
 */
public interface Comparator<T1,T2> {
  
  //public static final T1 MAX_VALUE = 100; //接口中泛型字母不能使用在全局常量中
  //T1 MAX_VALUE;
  public static final int MAX_VALUE = 100;
  
  void compare(T2 t);
  T2 compare();
  public abstract T1 compare2(T2 t);
}
```

**非泛型类中定义泛型方法**

```java
import java.io.Closeable;
import java.io.FileNotFoundException;
import java.util.List;

public class Method{
	// 泛型方法，在返回类型前面使用泛型字母
	  public static <T> T test1(T t){
		  return t;
	  }
	  
	  // T 只能是list 或者list 的子类
	  public static <T extends List> void test2(T t){
	    t.add("aa");
	  }
	  
	  // T... 可变参数   --->   T[]
	  public static <T extends Closeable> void test3(T...a) {
	    for (T temp : a) {
	     try {
	       if (null != temp) {
	         temp.close();
	       }
	     } catch (Exception e) {
	       e.printStackTrace();
	     }
	    }
	  }
	  
	  public static void main(String[] args) throws FileNotFoundException {
	    String test1 = test1("java 是门好语言");
	    System.out.println(test1);
	  }
}
```

**泛型的继承**

```java

public abstract class Father<T1, T2> {
  T1 age;
 
  public abstract void test(T2 name);
}
 
// 保留父类泛型 ----》泛型子类
// 1）全部保留
class C1<T1, T2> extends Father<T1, T2> {
 
  @Override
  public void test(T2 name) {
 
  }
}
 
// 2) 部分保留
class C2<T1> extends Father<T1, Integer> {
 
  @Override
  public void test(Integer name) {
 
  }
}
 
// 不保留父类泛型 -----》子类按需实现
// 1)具体类型
class C3 extends Father<String, Integer> {
 
  @Override
  public void test(Integer name) {
 
  }
}
 
// 2)没有具体类型
// 泛型擦除：实现或继承父类的子类，没有指定类型，类似于Object
class C4 extends Father {
 
  @Override
  public void test(Object name) {
 
  }
}
```









#### 三十三、反射机制



#### 三十四、Mybatis学习

