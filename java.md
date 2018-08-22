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

|                         |              |
| ----------------------- | ------------ |
| jps   jps -l   jps -lmv | 查看java进程 |
|                         |              |
|                         |              |



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