see：http://www.runoob.com/linux/linux-tutorial.html

免密码登录：https://www.cnblogs.com/wulaoer/p/5486579.html

#### 系统

```
[root@centos-linux ~]# uname -s  
Linux  
[root@centos-linux ~]# uname -m  
x86_64  
```

#### Vim编辑器

首先：卸载旧版本vi

```
sudo apt-get remove vim-common
```

然后：安装新版本vim

```
sudo apt-get install vim
```

| 命令                                      | 功能                                                         | 命令             | 功能                                                         |
| ----------------------------------------- | ------------------------------------------------------------ | ---------------- | ------------------------------------------------------------ |
| h                                         | 光标向左移动一个字符                                         | ndd              | 向下删除n行                                                  |
| j                                         | 光标向下移动一个字符                                         | d$               | 删除光标所在处到该行行尾的所有内容                           |
| k                                         | 光标向上移动一个字符                                         | yy               | 复制光标所在行                                               |
| l                                         | 光标向右移动一个字符                                         | dd               | 删除光标所在行                                               |
| /word                                     | 从光标所在处向下搜索word                                     | p,P              | p为在下一行粘贴复制的内容,P为在上一行                        |
| ?word                                     | 从光标所在处向上搜索word                                     | u                | 撤销前一个动作                                               |
| x,X                                       | x为向后删除一个字符,X为向前删除一个字符                      | .                | 重复前一个动作                                               |
| nx                                        | 向后删除n个字符                                              | i,I  （shift+i） | i为在光标所在处插入,I为在光标所在行的第一个非空字符处插入    |
| dd                                        | 删除光标所在行                                               | a,A              | a为在光标所在处的下一个字符串插入,A为在光标所在行的最后一个字符串插入 |
| o,O                                       | o为在光标所在行的下一行插入新行,O为光标所在行的上一行插入新行 | ZZ               | 文档未改变,则不保存离开。有改变,则保存离开                   |
| :w filename                               | 另存为                                                       | :r filename      | 将filename的内容添加到光标所在行的后面                       |
| :n1,n2 w filename                         | 将n1到n2行的内容保存到filename中                             | ctrl+R           | 重做：如果撤销了多次，可以使用ctrl+R来反转撤销               |
| dw                                        | 可以删除一个word                                             | :q!              | 丢弃所有的修改并退出                                         |
| :e!                                       | 放弃所有修改并重新载入该文件的原始内容                       | $                | 将光标移动到当前行行尾                                       |
| vim -d file1 file2 或 vimdiff file1 file2 | 文件比较                                                     | CTRL+A           | 将光移动到本行开头                                           |
| CTRL+E                                    | 将光标移动到本行末尾                                         | CTRL+Y           | 粘贴文本                                                     |
| CTRL+K                                    | 从光标处剪切文本直到本行结束                                 | Shift+Insert     | 将文本粘贴到终端                                             |

| 命令                       | 功能                         | 命令                        | 功能                           |
| -------------------------- | ---------------------------- | --------------------------- | ------------------------------ |
| uname -a                   | 查看内核/操作系统/CPU信息    | head -n 1 /etc/issue        | 查看操作系统版本               |
| cat /proc/cpuinfo          | 查看CPU信息                  | hostname                    | 查看计算机名                   |
| lsmod                      | 列出加载的内核模块           | env                         | 查看环境变量                   |
| free -m                    | 查看内存使用量和交换区使用量 | df -h                       | 查看各分区使用情况             |
| du -sh < 目录名>           | 查看指定目录的大小           | grep MemTotal /proc/meminfo | 查看内存总量                   |
| grep MemFree /proc/meminfo | 查看空闲内存量               | uptime                      | 查看系统运行时间、用户数、负载 |
| cat /proc/loadavg          | 查看系统负载                 | mount \| column -t          | 查看挂接的分区状态             |
| fdisk -l                   | 查看所有分区                 | ifconfig                    | 查看所有网络接口的属性         |
| iptables -L                | 查看防火墙设置               | netstat -lntp               | 查看所有监听端口               |
| netstat -antp              | 查看所有已经建立的连接       | netstat -s                  | 查看网络统计信息               |
| ps -ef                     | 查看所有进程                 | top                         | 实时显示进程状态               |
| w                          | 查看活动用户                 | id < 用户名>                | 查看指定用户信息               |
| last                       | 查看用户登录日志             | cut -d: -f1 /etc/passwd     | 查看系统所有用户               |
| cut -d: -f1 /etc/group     | 查看系统所有组               | crontab -l                  | 查看所有用户的定时任务         |

>Linux centos重启命令：
>
>1、reboot 
>
>2、shutdown -r now 立刻重启(root用户使用) 
>
>3、shutdown -r 10 过10分钟自动重启(root用户使用) 
>
>4、shutdown -r 20:35 在时间为20:35时候重启(root用户使用)  如果是通过shutdown命令设置重启的话，可以用shutdown -c命令取消重启 
>
>Linux centos关机命令：
>
>1、halt 立刻关机 
>
>2、poweroff 立刻关机 
>
>3、shutdown -h now 立刻关机(root用户使用) 
>
>4、shutdown -h 10 10分钟后自动关机  
>
>如果是通过shutdown命令设置关机的话，可以用shutdown -c命令取消重启 很多人使用hostname 主机名 来修改,其实这个只是做为暂时的,重启后将恢复到原来的名字.很多人说修改/etc/hosts文件,其实这个文件里的主机名只是为来提供给dns解析的.如果你用不上dns,只需要修改主机名,那修改这个没用.其实是修改这个文件etc/sysconfig/network这个文件里的主机名.
>
>NETWORKING=yes
>
>HOSTNAME=主机名
>
>记得重启!!!------------------------------------------------------------------------
>
>完整:第一步：#hostname oratest 
>
>第二步：修改/etc/sysconfig/network中的hostname
>
>第三步：修改/etc/hosts文件 

#### SSH

```
sudo stop ssh
apt-get -- purge remove openssh-server   //sudo yum remove sshd
apt-get update
apt-get upgrade

sudo apt-get install openssh-client    
sudo apt-get install openssh-server  // sudo yum install openssh-server
sudo /etc/init.d/ssh start / restart
sudo service sshd start  / restart
ssh服务默认的端口是22，可以更改端口，使用如下命令打开ssh配置文件：
sudo gedit /etc/ssh/sshd_config
sudo service sshd stop
sudo service sshd status 
关闭防火墙命令：# /etc/init.d/iptables stop
```

修改SSH配置文件以下选项，去掉#注释，将四个选项启用：

```
$ vi /etc/ssh/sshd_config
RSAAuthentication yes #启用 RSA 认证
PubkeyAuthentication yes #启用公钥私钥配对认证方式
AuthorizedKeysFile .ssh/authorized_keys #公钥文件路径（和上面生成的文件同）
PermitRootLogin yes #root能使用ssh登录
```

重启ssh服务，并设置开机启动：

```
$ service sshd restart
$ chkconfig sshd on
```



#### 文件操作

```
more, less, head tail
tail -f /var/log/messages
```

```
ls > ls.txt
cat ls.txt
说明: 
> 是把输出转向到指定的文件，如文件已存在的话也会重新写入，文件原内容不会保留
>>是把输出附向到文件的后面，文件原内容会保留下来
```

```
sort sort.txt   对文件内容排序
```

```
touch file 创建一个空的文件
```

```
echo 12345 | tee a.text 标准输出并且结果写入文件。如果文件不存在，则创建；如果已经存在，则覆盖之
```

```
tee -a file 输出到标准输出的同时，追加到文件file中。如果文件不存在，则创建；如果已经存在，就在末尾追加内容
```

```
tee file1 file2 -  输出到标准输出两次，同时保存到file1和file2中。（标准输出的次数=-出现的次数+1）
```

清空文件内容

```
$ : > filename
$ > filename
$ echo "" > filename
$ echo > filename
$ cat /dev/null > filename
```

复制文件内容到另一个文件(a中的内容复制到b中，b中之前的内容会被清空)

```
cat a.json > b.json
```

目录带空格

```
cd a\ bcd
```

查找

```
sudo find / -name 0001.txt -print  (print不要也可以)
sudo find / -name 'study' -type d  （study为目录）
```

清空日志内容

```
cat /dev/null > nohup.out 清空日志nohup.out文件内容
```

合并对个文件内容到一个新文件,新内容和cat文件顺序有关

```
cat a.text b.text > c.text
```

文件内容替换

```
cat a.text > b.text    //b的内容替换为a的内容，b之前的内容不会保留
cat a.text >> b.text   //b的内容后追加a的内容
```

解压

```
tar zxf jdk-7u80-linux-x64.gz -C /usr/java/       // -C表示解压到哪个目录
```



列出文件夹和里面的内容

```
ls | grep topic02
```

```
ls -R __consumer_offsets-*    //列出目录和里面的内容
```



查看文件类型

```
[root@localhost workspace]# file deploy-all-td-sit.sh
deploy-all-td-sit.sh: ASCII text
[root@localhost workspace]#
[root@localhost workspace]# file chestbox/
chestbox/: directory
[root@localhost workspace]#
```



#### 软链接

ln 命令

软连接相当于windows快捷方式

```shell
ln -s a.txt a
```

当a.txt不存在或者被删除后，`ls -l`查看文件不停闪烁

参数：

-b 删除，覆盖以前建立的链接
-d 允许超级用户制作目录的硬链接
-f 强制执行
-i 交互模式，文件存在则提示用户是否覆盖
-n 把符号链接视为一般目录
-s 软链接(符号链接)
-v 显示详细的处理过程



**1**.源文件被删除后，并没有影响硬链接文件；软链接文件在centos系统下不断的闪烁，提示源文件已经不存在

**2**.重建源文件后，软链接不在闪烁提示，说明已经链接成功，找到了链接文件系统；重建后，硬链接文件并没有受到源文件影响，硬链接文件的内容还是保留了删除前源文件的内容，说明硬链接已经失效



#### 日志

```
tail -300f shopbase.log #倒数300行并进入实时监听文件写入模式
tail -fn 300 Goldoffice_api_1.0.0-IX_TD-SIT.log
```



#### sed

|                                                              |                                                              |                                                            |
| ------------------------------------------------------------ | ------------------------------------------------------------ | ---------------------------------------------------------- |
| 追加（行下）：a\命令                                         | 将 123 追加到 以test 开头的行后面                            | sed '/^test/a\123' file                                    |
|                                                              | 在 file文件第2行之后插入 123                                 | sed -i '2a\123' file                                       |
| 插入（行上）：i\命令                                         | 将 123 追加到以test开头的行前面                              | sed '/^test/i\123' file                                    |
|                                                              | 在file文件第5行之前插入123                                   | sed -i '5i\123' file                                       |
| 替换文本中的字符串 s命令                                     | 替换所有的book为books                                        | sed 's/book/books/' file                                   |
| 替换每行第一个匹配到的                                       | 会匹配file文件中每一行的第一个book替换为books                | sed -i 's/book/books/g' file                               |
| -表示针对file文件中的第三行，将其中的aaa替换为fff            | sed -i '3s/aaa/fff/' file                                    | sed -i '3s/aaa/fff/' file                                  |
| 表示针对文件，找出包含xxx的行，并将其中的aaa替换为fff        | sed -i '/xxx/s/aaa/fff/g' file                               | sed -i '/xxx/s/aaa/fff/g' file                             |
| 表示针对文件第1行，将其中的#号或是*号替换为fff               | sed -i '1s/[#*]/fff/gp' file                                 | sed -i '1s/[#*]/fff/gp' file                               |
| 全面替换标记g                                                | 使用后缀 /g 标记会替换每一行中的所有匹配                     | sed 's/book/books/g' file                                  |
| 第5行替换为xxxxx                                             | sed  -i  '5s/^.*$/xxxxx/'  file                              | sed  -i  '5s/^.*$/xxxxx/'  file                            |
| 当需要从第N处匹配开始替换时，可以使用 /Ng                    | 替换第二个匹配到的                                           | echo sksksksksksk \| sed 's/sk/SK/2g'                      |
| / 转义符                                                     | sed 's/\/bin/\/usr\/local\/bin/g'                            | sed 's/\/bin/\/usr\/local\/bin/g'                          |
| 删除空白行                                                   | sed '/^$/d' file                                             | sed '/^$/d' file                                           |
| 删除文件的第2行                                              | sed '2d' file                                                | sed '2d' file                                              |
| 删除文件的第2行到末尾所有行                                  | sed '2,$d' file                                              | sed '2,$d' file                                            |
| 删除文件最后一行                                             | sed '$d' file                                                | sed '$d' file                                              |
| 删除文件中所有开头是test的行                                 | sed '/^test/'d file                                          | sed '/^test/'d file                                        |
| 所有以192.168.0.1开头的行都会被替换成它自已加localhost：     | sed 's/^192.168.0.1/&localhost/' file 192.168.0.1localhost   | sed 's/^192.168.0.1/&localhost/' file 192.168.0.1localhost |
| sed表达式可以使用单引号来引用，但是如果表达式内部包含变量字符串，就需要使用双引号。 | test=hello  echo hello WORLD \| sed "s/$test/HELLO"  输出：HELLO WORLD |                                                            |
| 把1~10行内所有abcde转变为大写，注意，正则表达式元字符不能使用这个命令 | sed '1,10y/abcde/ABCDE/' file                                | sed '1,10y/abcde/ABCDE/' file                              |

假设文档内容如下： 

```
[root@localhost ~]# cat /tmp/input.txt
null
000011112222
 
test
```

**要求：在1111之前添加AAA,方法如下：** 

sed -i 's/指定的字符/要插入的字符&/'  文件 

```
[root@localhost ~]# sed -i  's/1111/AAA&/' /tmp/input.txt                     
[root@localhost ~]# cat /tmp/input.txt                   
null
0000AAA11112222
 
test
```

**要求：在1111之后添加BBB，方法如下：** 

sed -i 's/指定的字符/&要插入的字符/'  文件 

```
[root@localhost ~]# sed -i  's/1111/&BBB/' /tmp/input.txt    
[root@localhost ~]# cat /tmp/input.txt                   
null
0000AAA1111BBB2222
 
test
```

**要求：(1) 删除所有空行；(2) 一行中，如果包含"1111"，则在"1111"前面插入"AAA"，在"11111"后面插入"BBB"** 

```
[root@localhost ~]# sed '/^$/d;s/1111/AAA&/;s/1111/&BBB/' /tmp/input.txt   
null
0000BBB1111AAA2222
test
```

 **要求：在每行的头添加字符，比如"HEAD"，命令如下：** 

```
[root@localhost ~]# sed -i 's/^/HEAD&/' /tmp/input.txt 
[root@localhost ~]# cat /tmp/input.txt
HEADnull
HEAD000011112222
HEAD
HEADtest
```

  **要求：在每行的尾部添加字符，比如"tail"，命令如下：** 

```
[root@localhost ~]# sed -i 's/$/&tail/' /tmp/input.txt      
[root@localhost ~]# cat /tmp/input.txt                
HEADnulltail
HEAD000011112222tail
HEADtail
HEADtesttail

或者：
sed 's/$/tail/' file1
```

说明：
1."^"代表行首，"$"代表行尾

2.'s/$/&tail/g'中的字符g代表每行出现的字符全部替换，如果想在特定字符处添加，g就有用了，否则只会替换每行第一个，而不继续往后找。



#### 目录操作

```
mkdir -p /home/yiibai/docker/ubuntu-in-docker
```
```
rm -rf 目录名 (非空文件夹)
rmdir 空文件夹
```

```
ls -l | grep "^-" | wc -l    （文件个数 不包含子目录）
```

```
ls -lR| grep "^-" | wc -l  （文件个数 包含子目录）
```

```
ls -lR | grep "^d" | wc -l （文件个数 目录下文件夹的个数，包含子目录）
```

```
ls -l |grep "^d"|wc -l d 表示 目录  统计当前文件内目录的个数
```

```
ls -lR|grep "^d"|wc -l  统计当前文件夹下文件的个数，包括子文件夹里的 r 表示连级
```

```
du -h --max-depth=1  文件夹小
```

#### 用户管理

```
passwd
sudo passwd root //设置root密码
```

1 允许root用户远程登录

修改ssh服务配置文件

```
sudo vi /etc/ssh/sshd_config
```

调整PermitRootLogin参数值为yes

2 允许无密码登录 

同上，修改ssh服务配置文件，两种情况：

1） 将PermitEmptyPasswords yes前面的#号去掉

2） 将PermitEmptyPasswords 参数值修改为yes，如下图：

无论哪种，最后PermitEmptyPasswords参数值为yes

以上两种配置，均需要重启ssh服务

```
service sshd restart  # 或者
/etc/initd.d/sshd restart
```

为了安全起见，FreeBSD默认情况下是不允许root用户进行SSH远程登录的，需要进行以下操作才可以进行Root用户的ssh远程登录。 

首先vi编辑/etc/inetd.conf,去掉ssh前的#注释，保存后:wq退出 (开启监听ssh服务)

编辑/etc/rc.conf， 最后加入:sshd_enable=”yes”即可
激活sshd服务：

```
#/etc/rc.d/sshd start
```

检查服务是否启动，在22端口应该有监听。

```
# check port number 22
#netstat -an # 或
#netstat -tnlp
```

最后，编辑ssh配置文件

```
#vi  /etc/ssh/sshd_config
```

在/etc/ssh/sshd_config最后中加入

```
PermitRootLogin yes #允许root登录
PermitEmptyPasswords no #不允许空密码登录
PasswordAuthentication yes # 设置是否使用口令验证。
```

修改完配置文件后，重新启动sshd服务器(/etc/rc.d/sshd restart)即可。

补充：

1 如果重启后还是不行， 请重新载入sshd_config 文件

```
/etc/rc.d/sshd reload
```

2 如果出现using keyboard-interactive authentication

password:
请确认配置文件中，PasswordAuthentication参数值是否已经改成yes
另外如果客户端是putty， 那么请确认”尝试’智能键盘’认证（SSH-2）”的勾是否有去掉!!!!

3 如果是使用root帐号登陆

请确认密码是否为空
空密码无法登陆

4 请确认是否有安装SSH

确认sysinstall>>>configure>>>networking>>>sshd是否的勾是否有打上.

#### 用户和组管理

##### 1.添加用户

```
useradd tom -d /home/hello    # 添加一个tom用户，并指定tom用户的家目录为hello
```

##### 2.删除用户

```
userdel -r tom    # 删除tom用户及其home目录
```

##### 3.创建组

```
useradd tom -g public    # 创建一个tom用户并为其指定组public
```

##### 4.删除组

```
groupdel public    # 删除组，如果该组有用户成员，则必须先删除其用户才能删除组
```

##### 5.查看用户组

```
id tom    # 查看tom用户的UID和GID
```

##### 6.切换到root用户

```
su - root    # 切换到root用户
```

#### 权限操作

```
chmod u+x   表示该档案的拥有者权限
```

```
chmod 777 exam.txt    # 对exam.txt赋予所有用户所有权限
chmod u=rwx,g=rx,o=rx exam.txt    # 对exam.txt的所属用户赋予rwx（读，写，执行）权限，所属组赋予rx权限，其它用户赋予rx权限
```

#### nohup 

Linux 运行jar包命令如下：

方式一：

java -jar XXX.jar

特点：当前ssh窗口被锁定，可按CTRL + C打断程序运行，或直接关闭窗口，程序退出

那如何让窗口不锁定？

方式二

java -jar XXX.jar &

&代表在后台运行。

特定：当前ssh窗口不被锁定，但是当窗口关闭时，程序中止运行。

继续改进，如何让窗口关闭时，程序仍然运行？

方式三

nohup java -jar XXX.jar &

nohup 意思是不挂断运行命令,当账户退出或终端关闭时,程序仍然运行

当用 nohup 命令执行作业时，缺省情况下该作业的所有输出被重定向到nohup.out的文件中，除非另外指定了输出文件。

方式四

nohup java -jar XXX.jar >temp.txt &

解释下 >temp.txt

command >out.file

command >out.file是将command的输出重定向到out.file文件，即输出内容不打印到屏幕上，而是输出到out.file文件中。

可通过jobs命令查看后台运行任务

jobs

那么就会列出所有后台执行的作业，并且每个作业前面都有个编号。

如果想将某个作业调回前台控制，只需要 fg + 编号即可。

fg 23

查看某端口占用的线程的pid

netstat -nlp |grep :9181

#### 网络操作

关闭网卡

```
sudo ifconfig eth0 down 
```

启用网卡

```
sudo ifconfig eth0 up 
```

监听端口

```
ss -tunl | grep 2376
```

端口使用情况

```
netstat -ntulp |grep 8080
netstat -nap | grep 端口号
```

```
apt-get install net-tools
### ifconfig
apt-get install iputils-ping
### ping
apt-get install iproute2
#### ip
```



#### 公网ip查询

方式一：百度 `公网ip查询`

```
https://www.baidu.com/s?ie=utf-8&f=3&rsv_bp=1&rsv_idx=1&tn=baidu&wd=%E5%85%AC%E7%BD%91ip%E6%9F%A5%E8%AF%A2&oq=%25E5%2585%25AC%25E7%25BD%2591ip%25E6%259F%25A5%25E8%25AF%25A2&rsv_pq=a0db30150001ef82&rsv_t=d46d0RVQx98zSNKCl%2BnxmD14twj6T8FI4l3sdyWXcRH7pWz6UFjqHi2cQYE&rqlang=cn&rsv_enter=0&prefixsug=%25E5%2585%25AC%25E7%25BD%2591ip%25E6%259F%25A5%25E8%25AF%25A2&rsp=0
```

方式二：命令行输入

```
curl ifconfig.me
```

#### 系统维护

更新资源

```
apt-get update
```

升级系统

```
apt-get upgrade
```

#### 模拟请求

```
POST：curl -d "user=nickwolfe http://www.linuxidc.com/login.cgi
```

```
GET ：curl http://www.linuxidc.com/login.cgi？user=nickwolfe
```

```
$ curl -u name：passwd ftp://ip：port/path/file 
$ curl ftp://name：passwd@ip：port/path/file
```
#### 别名

```
alias dia='docker images -a'
```

```
vi ~/.bashrc 增加:
# my
alias la='ls -al --color=auto'
# my-end
立即生效:
source ～/.bashrc
```

查看已配置的别名

```
docker@default:~$ alias
ls='ls -p'
cp='cp -i'
df='df -h'
dia='docker images -a'
d='dmenu_run &'
ce='cd /etc/sysconfig/tcedir'
ll='ls -l'
```

删除已配置的别名

```
 unalias docim
```

#### 快捷键

- `Ctrl + l` ：清除屏幕，同clear
- `Ctrl + a` ：将光标定位到命令的开头
- `Ctrl + e` ：与上一个快捷键相反，将光标定位到命令的结尾
- `Ctrl + u` ：剪切光标之前的内容，在输错命令或密码
- `Ctrl + k` ：与上一个快捷键相反，剪切光标之后的内容
- `Ctrl + y` ：粘贴以上两个快捷键所剪切的内容。Alt+y粘贴更早的内容
- `Ctrl + w` ：删除光标左边的参数（选项）或内容（实际是以空格为单位向前剪切一个word）
- `Ctrl + /` ：撤销，同`Ctrl+x` + `Ctrl+u`
- `Ctrl + f` ：按字符前移（右向），同→
- `Ctrl + b` ：按字符后移（左向），同←
- `Alt + f` ：按单词前移，标点等特殊字符与空格一样分隔单词（右向），同Ctrl+→
- `Alt + b` ：按单词后移（左向），同Ctrl+←
- `Alt + d` ：从光标处删除至字尾。可以Ctrl+y粘贴回来
- `Alt + \` ：删除当前光标前面所有的空白字符
- `Ctrl + d` ：删除光标处的字符，同Del键。没有命令是表示注销用户
- `Ctrl + h` ：删除光标前的字符
- `Ctrl + r` ：逆向搜索命令历史，比history好用
- `Ctrl + g` ：从历史搜索模式退出，同ESC
- `Ctrl + p` ：历史中的上一条命令，同↑
- `Ctrl + n` ：历史中的下一条命令，同↓
- `Alt + .`：同!$，输出上一个命令的最后一个参数（选项or单词）。
- shift+insert 粘贴到终端

https://www.cnblogs.com/toughlife/p/5633510.html





#### centos

1 ： ifconfig出现command not found 或是ip addr没有显示IP

在虚拟机中以最小化方式安装CentOS7，后无法上网，因为CentOS7默认网卡未激活。 

而且在sbin目录中没有ifconfig文件，这是因为CentOS7已经不使用 ifconfig命令了，已经用ip addr命令代替； 

并且网卡名称也不是eth0了，而是改成eno16777736了。 

解决ifconfig不可用：ip addr 即查看分配网卡情况。 

激活网卡：在文件 `/etc/sysconfig/network-scripts/ifcfg-eno16777736` 中 

进入编辑模式，将 `ONBOOT=no 改为 ONBOOT=yes`，就OK

保存后重启网卡： `service network restart` 

#### 防火墙

```
yum install firewalld 安装firewalld防火墙

#@root# : firewall-cmd --state  #查看状态
running

firewall-cmd --reload  #修改完需要重启防火墙

启动一个服务：systemctl start firewalld.service
关闭一个服务：systemctl stop firewalld.service
重启一个服务：systemctl restart firewalld.service
显示一个服务的状态：systemctl status firewalld.service
在开机时启用一个服务：systemctl enable firewalld.service
在开机时禁用一个服务：systemctl disable firewalld.service
查看服务是否开机启动：systemctl is-enabled firewalld.service
查看已启动的服务列表：systemctl list-unit-files|grep enabled
查看启动失败的服务列表：systemctl --failed

查看开启的端口和服务
irewall-cmd --permanent --zone=public --list-services //服务空格隔开 例如 dhcpv6-client https ss

firewall-cmd --permanent --zone=public --list-ports //端口空格隔开 例如 8080-8081/tcp 8388/tcp 80/tcp

设置某个ip 访问某个服务
firewall-cmd --permanent --zone=public --add-rich-rule="rule family="ipv4" source address="192.168.0.4/24" service name="http" accept"
ip 192.168.0.4/24 访问 http

删除规则
firewall-cmd --permanent --zone=public --remove-rich-rule="rule family="ipv4" source address="192.168.0.4/24" service name="http" accept"

检查是否生效
iptables -L -n | grep 21
ACCEPT     tcp  --  0.0.0.0/0            0.0.0.0/0            tcp dpt:21 ctstate NEW

firewall-cmd --list-all


添加
firewall-cmd --zone=public --add-port=8081/tcp --permanent    （--permanent永久生效，没有此参数重启后失效）
重新载入
firewall-cmd --reload
查看
firewall-cmd --zone=public --query-port=8081/tcp
删除
firewall-cmd --zone=public --remove-port=80/tcp --permanent

```



```
iptables -L  列出iptables规则
iptables -F  清除iptables内置规则
iptables -X  清除iptables自定义规则
```



#### 文件双机备份

图片上传双机互备（rsync+inotify）



#### 命令TAB补全

```
yum install -y bash -completion
```



#### SSH批量免密码登录

https://www.cnblogs.com/kevingrace/category/924885.html



#### 常用命令

|                                                   |                      |
| ------------------------------------------------- | -------------------- |
| pidof  java                                       | java的进程ID         |
| pkill java                                        | 按照进程名杀死进程   |
| pgrep -l  java                                    | 指定要查找的进程名称 |
| jps                                               | 显示java进程         |
| sudo ln -s /var/myapp/myapp.jar /etc/init.d/myapp | 软链接为服务         |
| service myapp start                               | 启动服务             |
|                                                   |                      |
|                                                   |                      |
|                                                   |                      |

#### 制作systemd Service

`systemd` is the successor of the System V init system and is now being used by many modern Linux distributions. Although you can continue to use `init.d` scripts with `systemd`, it is also possible to launch Spring Boot applications by using `systemd` ‘service’ scripts.

Assuming that you have a Spring Boot application installed in `/var/myapp`, to install a Spring Boot application as a `systemd` service, create a script named `myapp.service` and place it in `/etc/systemd/system` directory. The following script offers an example:

```
[Unit]
Description=myapp
After=syslog.target

[Service]
User=myapp
ExecStart=/var/myapp/myapp.jar
SuccessExitStatus=143

[Install]
WantedBy=multi-user.target
```

| ![[Important]](https://docs.spring.io/spring-boot/docs/2.0.3.RELEASE/reference/htmlsingle/images/important.png) | Important |
| ------------------------------------------------------------ | --------- |
| Remember to change the `Description`, `User`, and `ExecStart` fields for your application. |           |

| ![[Note]](https://docs.spring.io/spring-boot/docs/2.0.3.RELEASE/reference/htmlsingle/images/note.png) |
| ------------------------------------------------------------ |
| The `ExecStart` field does not declare the script action command, which means that the `run` command is used by default. |

Note that, unlike when running as an `init.d` service, the user that runs the application, the PID file, and the console log file are managed by `systemd` itself and therefore must be configured by using appropriate fields in the ‘service’ script. Consult the [service unit configuration man page](https://www.freedesktop.org/software/systemd/man/systemd.service.html) for more details.

To flag the application to start automatically on system boot, use the following command:

```
$ systemctl enable myapp.service
```

Refer to `man systemctl` for more details.



### shell编程

#### 入门 hello world

```
vim hello.sh

#!/bin/bash
#第一个shell小程序
echo hello world!
```

执行

```
chmod +x hello.sh //赋权限
./hello.sh 或
/bin/sh hello.sh
```

#### 变量

注意：定义变量不用**$**符号，使用变量要加**$**就行了 

在第5行中，我们在自定义变量时，使用了双引号，在shell编程中， 如果变量出现空格或者引号，那么也必须加引号， 否则就可以省略。

还有一点需要注意，定义变量的时候，“=”左右千万不要有空格啊

```
#!/bin/bash
#使用环境变量
echo $PATH
#自定义变量hello
hello="hello world"
echo $hello
echo `pwd`
```

使用变量时使用**$**符号加上变量名就行了。记住：定义变量不用**$**符号，使用变量要加**$**就行了。 

##### 将linux命令执行结果赋值给变量

```
#!/bin/bash
path=$(pwd)
files=`ls -al`
echo current path: $path
echo files: $files
```

注意：第三行的符号不是单引号，是键盘上“～”这个按键 



#### 基本数据类型运算

| 符号 | 语义         | 描述                           |
| ---- | ------------ | ------------------------------ |
| +    | 加           | 10+10，结果为20                |
| -    | 减           | 10-3， 结果为7                 |
| *    | 乘           | 10*2，结果为20                 |
| /    | 除           | 10/3, 结果为3（取整数）        |
| %    | 求余         | 10%3, 结果为1 (取余数)         |
| ==   | 判断是否相等 | 两数相等返回1，否则0           |
| !=   | 判断是否不等 | 两数不等返回1，否则0           |
| >    | 大于         | 前者大于后者返回1，否则0       |
| >=   | 大于或等于   | 前者大于或等于后者返回1，否则0 |
| <    | 小于         | 前者小于后者返回1，否则0       |
| <=   | 小于或等于   | 前者小于或等于后者返回1，否则0 |

 上述操作符与其它语言相比，并无特殊之处。

在shell中，对于基本数据类型的运算主要分为两种，**整数运算**和**浮点数（小数）运算**。下面就分别来看看这两种运算:

##### 整数运算

###### expr

在shell中，有两种方式能实现整数运算，一种是使用**expr**命令， 另外一种是通过方括号（**$[]**）来实现。下面分别来看看： 

```
#!/bin/bash
#输出13
expr 10 + 3

#输出10+3
expr 10+3

#输出7
expr 10 - 3

#输出30
expr 10 \* 3

#输出3
expr 10 / 3

#输出1
expr 10 % 3

#将计算结果赋值给变量
num1=$(expr 10 % 3)

#将计算结果赋值给变量
num2=`expr 10 % 3`
```

注意:

在以上的乘法(*)中，我们用了反斜线（）来转义，不然会报错。

运算符前后必须还有空格，否则会被直接当作字符串返回。

如果要将计算结果保存到变量，就需要用到我们上篇文章讲到的那两种方式（$() 或者 ``）来替换命令了。

###### 方括号($[])

```
#!/bin/bash
num1=10
num2=3
#输出num1 + num2=13
echo "num1 + num2=$[$num1 + $num2]"

#输出num1+num2=13
echo "num1+num2=$[$num1+$num2]"

#输出num1 - num2=7
echo "num1 - num2=$[$num1 - $num2]"

#输出num1 * num2=30
echo "num1 * num2=$[$num1 * $num2]"

#输出num1 > num2=1
echo "num1 > num2=$[$num1 > $num2]"

#输出num1 < num2=0
echo "num1 < num2=$[$num1 < $num2]"

#将运算结果赋值给变量，输出num3=3
num3=$[$num1 / $num2]
echo "num3=$num3"
```



##### 浮点运算

在shell中，做浮点运算一般是用bash的计算器(bc)。在shell脚本中，一般我们的使用方法是：

variable=$(echo "options; expression" | bc)

> options是bc的一些选项，例如： 可以通过scale去设置保留的小数位数。具体有哪些参数，可以man bc进行查看
>
> expression就是我们具体的表达式，例如 10 * 3
>
> **" | "** 这个符号，对于熟悉linux系统的人来说，这个再熟悉不过了。它叫做管道， 之所以会叫做管道，其实很形象，你可以把它看作一根水管，水管一头接入前一个命令的返回结果, 一头接入下一个命令。表示将前一个命令的执行结果作为后一个命令的参数输入。以上，表示将我们的表达式作为bc的参数输入。

```
#!/bin/bash
#表示 10/3， 保留2位小数，将结果赋值给了num, 输出3.33
num=$(echo "scale=2; 10 / 3" | bc)
echo $num
```

#### 字符串操作

##### 拼接字符串

```
your_name="qinjx"
greeting="hello, "$your_name" !"
greeting_1="hello, ${your_name} !"
echo $greeting $greeting_1
```

##### 获取字符串长度

```
string="abcd"
echo ${#string} #输出 4
```

##### 提取子字符串

以下实例从字符串第 **2** 个字符开始截取 **4** 个字符： 

```
string="runoob is a great site"
echo ${string:1:4} # 输出 unoo
```

##### 查找子字符串

 查找字符 "**i** 或 **s**" 的位置：

```
string="runoob is a great company"
echo `expr index "$string" is`  # 输出 8
```

**注意：** 以上脚本中 ` 是反引号，而不是单引号 '，不要看错了哦。 



| -z "string" | 若string长度为0，则真   |
| ----------- | ----------------------- |
| -n "string" | 若string长度步为0，则真 |

#### 条件选择

```
if command
then
    commands
fi
```

在shell脚本的if其实是根据紧跟后面的那个命令的**退出状态码**来判断是否执行then后面的语句的。 

关于退出状态码，你只需要记住：正常退出（命令执行正常）的状态码是0， 非正常退出的状态码不是0（有不少）。

以上语句的语义为： 如果if后面的命令执行正常（状态码0），那么就执行then后面的语句。否则不执行。 fi代表if语句的结束。

例子：

```
#!/bin/bash
#这儿由于pwd是linux内置的命令，因此执行后会正常退出（状态码0），所以会执行then中的语句
#如果此处替换为一个不存在的命令（例如: pw），那么就会非正常退出，不会执行then中的语句
if pwd
then
   echo 执行then里面的语句
fi
```

if-then还可以简写为 

```
if command; then
    commands
fi
```

因此，以上代码还可以写成以下： 

```
#!/bin/bash
if pwd; then
   echo 执行then里面的语句
fi
```



以上，如果我要判断处理异常退出（状态码非0）情况，该怎么办？

别着急： else 来帮你。

```
if command
then
    commands
else
    commands
fi
```

与if-then语句相比，这回多了个else语句，else语句用来判断if后面的命令非正常退出的情况。 

```
#!/bin/bash
if pwd
then
    echo 正常退出
else 
    echo 非正常退出
fi      
```

甚至，我们还可以变形写出更多的else: 

```
if command1 
then
    commands 
elif 
    command2 
then
    command3
fi
```

但是上面就只能根据退出状态码判断，不能写表达式，你还让我怎么写？ 我各个编程语言直接吊打你！

不要慌，客官，请接着往下看：

```
#!/bin/bash
num1=100
num2=200
if (( num1 > num2 )) 
then
    echo "num1 > num2"
else 
    echo "num2 <= num2" 
```

((  expression  )) 注意：括号里面两边都需要有空格 

| 比较          | 描述                            |
| ------------- | ------------------------------- |
| str1 = str2   | 判断str1是否与str2相同          |
| str1 !＝ str2 | 判断str1是否与str2不相同        |
| str1 < str2   | 判断str1是否比str2小(根据ASCII) |
| str1 > str2   | 判断str1是否比str2大(根据ASCII) |
| -n str1       | 判断str1的长度是否非0           |
| -z str1       | 判断str1的长度是否为0           |

 双方括号命令提供了针对字符串比较的高级特性 

```
#!/bin/bash
var1=test
var2=Test
if [[ $test < $test2 ]]
then
    echo "test1 < test2"
else
    echo "test1 >= test2"
fi      
```



在使用if-then-else语句中，如果碰到条件很多的情况，如下 

```
#!/bin/bash
num=3
if (( $num == 1 ))
then
    echo "num=1"
elif (( $num == 2 ))
then
    echo "num=2"
elif (( $num == 3 ))
then
    echo "num=3"    
elif (( $num == 4 ))
then
    echo "num=4"
fi  
```

如果再多点条件，看起来是不是很多？ 此时，其实还有一种替代方案，那就是使用case.

```
case variable in
pattern1 | pattern2) commands1;; pattern3) commands2;;
*) default commands;;
esac
```

将以上代码替换为case 

```
#!/bin/bash
case $num in
1)
    echo "num=1";;
2)
    echo "num=2";;
3)
    echo "num=3";;
4)
    echo "num=4";;
*)
    echo "defaul";;
esac
```

#### 循环语句

##### for循环

基本语法

```
for var in list 
do
    commands
done
```

list代表要循环的值，在每次循环的时候，会把当前的值赋值给var（变量名而已，随意定）, 这样在循环体中就可以直接通过$var获取当前值了。 

例子：

```
#!/bin/bash
for str in a b c d e
do
    echo $str
done
```

以上会根据空格将abcde分割，然后依次输出出来。 

如果以上例子不是以空格分割，而是以逗号(,)分割呢？ 

```
#!/bin/bash
list="a,b,c,d,e"
for str in $list
do 
    echo $str
done
```

结果输出a,b,c,d,e

造成这个结果的原因是：for...in循环默认是循环一组通过空格或制表符（tab键）或换行符（Enter键）分割的值。这个其实是由**内部字段分隔符**配置的，它是由系统环境变量IFS定义的。当然，既然是由环境变量定义的，那当然也就能修改啊。

###### 修改IFS值

```
#!/bin/bash
#定义一个变量oldIFS保存未修改前的IFS的值
oldIFS=$IFS
#修改IFS值，以逗号为分隔符
IFS=$','
list=a,b,c,d,e
list2="a b c d e"
for var in $list
do
    echo $var
done
for var2 in $list2
do
    echo $var2
done
#还原IFS的值
IFS=$oldIFS
```

以上第一个循环会分别输出abcde几个值。而第二个循环会输出a b c d e(即未处理)。因为我们把IFS的值设置为逗号了， 当然，不一定要是逗号，想设置什么，你说了算! 



```
#!/bin/bash
for (( i = 0; i <= 10; i++ ))
do
    echo $i
done   
```

上面例子循环11次，从0到10依次输出。稍微有过编程基础的都对此应该很熟悉。就不做详细阐述了。 

##### while循环

示例一

```
#!/bin/bash
flag=0
while test $flag -le 10
do
    echo $flag
    # 如果没有这句，那么flag的值一直为0，就会无限循环执行
    flag=$[$flag + 1]
done
```

以上判断flag是否大于或者等于10， 如果满足条件，那么输出当前flag的值，然后再将flag的值加1。最终输出的结果为0到10的结果。

结合上一篇文章test的写法，我们还可以将以上示例变形为如下：

实例二

```
#!/bin/bash
flag=0
while [ $flag -le 10 ]
do
    echo $flag
    flag=$[$flag + 1]
done
```

示例三

```
flag=0
while (( $flag <= 10 ))
do
    echo $flag
    flag=$[$flag + 1]
done
```

##### until循环语句

在掌握while循环语句之后， until语句就很简单了。until语句就是与while语句恰好相反， while语句是在test命令退出状态码为0的时候执行循环， 而until语句是在test命令退出状态码不为0的时候执行。 

```
#!/bin/bash
flag=0
until (( $flag > 10 ))
do
    echo $flag
    flag=$[ $flag + 1 ]
done
```

以上输出0到10的值。until后面的条件与上面while例子完全相反。 

##### 控制循环

1：**break用于跳出当前循环** 

示例一： 

```
#!/bin/bash
for (( flag=0; flag <= 10; flag++ ))
do
    if (( $flag == 5 ))
    then
        break
    fi
    echo $flag
done
```

以上当flag的值为5的时候，退出循环。输出结果为0-4的值。 

2：continue

continue表示终止当前的一次循环，进入下一次循环，注意,continue后面的语句不会执行。

continue的语法与break一样，因此，就只做一个示例演示啦。

```
flag=0
while (( $flag <= 10 ))
do
    if (( $flag == 5 ))
    then
        flag=$[$flag+1]
        continue
    fi
    echo "outerFlag=$flag"
    for (( innerFlag=11; innerFlag < 20; innerFlag++ ))
    do
        if (( $innerFlag == 16 ))
        then
            flag=$[$flag+1]
            continue 2
        fi
        echo "innerFlag=$innerFlag"
    done
done
```

以上例子： 当for循环中innerFlag的值为16的时候会跳到外层while循环，当外层循环的flag的值为5的时候，会直接跳过本次循环，然后进入下一次循环，因此在输出的结果中，不会出现outerFlag=5的情况。 



#### 命令行参数处理

bash shell可根据参数位置获取参数。通过 **$1** 到 **$9** 获取第1到第9个的命令行参数。**$0**为shell名。如果参数超过9个，那么就只能通过**${}**来获取了， 例如获取第10个参数，那么可以写为${10}。 

示例一

```
#!/bin/bash
#testinput.sh
echo "file name: $0"
echo "base file name: $(basename $0)"
echo "param1: $1"
echo "param2: ${2}"
```

运行上面shell

```
./testinput.sh 12 34
```

最终得到的结果如下：

file name: ./testinput4.sh

base file name: testinput4.sh

param1: 12

param2: 34

成功的得到文件名和命令行输入的参数（命令行参数以空格分隔，如果参数包含了空格，那么久必须添加引号了） 

**$0**默认会获取到当前shell文件的名称，但是，它也包含(./)，如果你以完整路径运行，那么这还会包含目录名。因此，上面通过basename命令来获取单纯的文件名$(basename $0)。 

###### 读取所有参数

方法一

既然bash shell通过位置可获取参数，那意味着如果我们知道参数的总个数就可以通过循环依次获取参数。那么如何获取参数总个数呢？

在bash shell中通过 **$#** 可获取参数总数。

示例：（循环获取参数） 

```shell
#!/bin/bash
for (( index=0; index <= $#; index++ ))
do
    echo ${!index}
done
```

```
以上示例，我们通过 $# 获取总参数个数。然后通过循环获取每个位置的参数。注意： 按照正常的理解，上面的 ${!index} 应该是 ${$index}才对， 对吧？ 但是，由于${}内不能再写$符号，bash shell在这个地方是用了!符号，所以以上才写为了${!index}。
```



方法二

在bash shell中还可以通过 $* 和 $@ 来获取所有参数。但是这两者之间有着很大的区别：

$* 会将命令行上提供的所有参数当作一个单词保存, 我们得到的值也就相当于是个字符串整体。

$@ 会将命令行上提供的所有参数当作同一字符串中的多个独立的单词。

```
#!/bin/bash
#testinput.sh
var1=$*
var2=$@
echo "var1: $var1"
echo "var2: $var2"
countvar1=1
countvar2=1
for param in "$*"
do
    echo "first loop param$countvar1: $param"
    countvar1=$[ $countvar1 + 1 ]
done
echo "countvar1: $countvar1"

for param in "$@"
do
    echo "second param$countvar2: $param"
    countvar2=$[ $countvar2 + 1 ]
done
echo "countvar2: $countvar2"
```

执行

```
./testinput.sh 12 34 56 78  
```

#### 获得用户输入

##### 单个输入

有时候，我们在shell执行过程中获取用户的输入，以此与用户进行交互。这是通过**read**命令来实现的。下面就来看看其用法： 

示例一 

```
#!/bin/bash
echo -n "yes or no(y/n)?"
read choice
echo "your choice: $choice"
```

运行以上示例，首先会输出”yes or no(y/n)?“， 然后会等待用户输入（-n参数表示不换行，因此会在本行等待用户输入），当用户输入后，会把用户输入的值赋值给choice变量， 然后最终输出 “your choice: (你输入的内容)”。

事实上，我们可以不指定**read**后面的变量名，如果我们不指定， read命令会将它收到的任何数据都放进特殊环境变量REPLY中。如下：

示例二： 

```
#!/bin/bash
echo -n "yes or no(y/n)?"
read
echo "your choice: $REPLY"
```

以上示例与示例一是等价的。

有时候，我们需要用户输入多个参数，当然，shell是支持一次接受多个参数输入的。

#### 多个输入

示例三 

```
#!/bin/bash
read -p "what's your name?" first last
echo first: $first
echo last: $last
```

以上示例首先输出“what's your name?”， 然后在本行等待用户输入（此处用read -p实现以上示例的echo -n + read命令的不换行效果），输入的参数以空格分隔，shell会把输入的值依次赋值给first和last两个变量。如果输入的值过多，假如我输入了3个值，那么shell会把剩下的值都赋值给最后一个变量（即第二三两个的值都会赋值给last变量）。

细想一下，有个问题，假如用户一直不输入，怎么办？一直等待？

#### 超时设置

我们可以通过read -t 来指定超时时间（单位为秒），如果用户在指定时间内没输入，那么read命令就会返回一个非0的状态码。 

示例四 

```
#/bin/bash
if read -t 5 -p "Please enter your name: " name 
then
    echo "Hello $name"
else
    echo "Sorry, timeout! "
fi
```

运行以上示例，如果超过5秒没输入，那么就会执行else里面的。 



#### 注释

以"#"开头的行就是注释，会被解释器忽略。

sh里没有多行注释，只能每一行加一个#号。只能像这样：

```
#--------------------------------------------
# 这是一个注释
# author：菜鸟教程
# site：www.runoob.com
# slogan：学的不仅是技术，更是梦想！
#--------------------------------------------
##### 用户配置区 开始 #####
#
#
# 这里可以添加脚本描述信息
# 
#
##### 用户配置区 结束  #####
```

##### 多行注释

```
:<<EOF
注释内容...
注释内容...
注释内容...
EOF
```

#### 通过Shell脚本读取properties

通过Shell脚本读取properties文件中的参数时遇到\r换行符的问题

https://www.cnblogs.com/ljhoracle/p/6277064.html



#### shell脚本执行过程

https://blog.csdn.net/lf_2016/article/details/65627403

#### 编程实战一

```
#!/bin/bash
if [ $# -lt 1 -o $# -gt 2 ]
then
    echo "参数个数不正确！"
    exit -1
fi

WHOAIM=`whoami`

function release
{
    if [ $1 -le $2 ]
        then
                for id in `ipcs | sed -n $1,$2p | grep "${WHOAMI}" | \
                awk '{print $2}'`
        do
            ipcrm $3 $id
        done
        fi
}

function releasebyid
{
    ipcrm $1 $2
}


function judgetype
{
    case $1 in
    "shm")
        start=$((`ipcs | sed -n '/shmid/='`+1))
        end=$((`ipcs | sed -n '/Semaphore/='`-1))
        if [ $# -eq 2 ]
        then
            releasebyid "-m" $2
        else
            release $start $end "-m"
        fi
        ;;
    "sem")
        start=$((`ipcs | sed -n '/semid/='`+1))
        end=$((`ipcs | sed -n '/Message/='`-1))
        if [ $# -eq 2 ]
        then
            releasebyid "-s" $2
        else
            release $start $end "-s"
        fi
        ;;
    "msg")
        start=$((`ipcs | sed -n '/msqid/='`+1))
        end=$((`ipcs | sed -n '$='`-1))
        if [ $# -eq 2 ]
        then
            releasebyid "-q" $2
        else
        release $start $end "-q"
        fi
        ;;
    *)
        echo "错误的参数 [shm] [sem] [msg] [all]"
        exit 0
        ;;
    esac
}

if [ "$1" = "all" ]
then
    if [ $# -eq 2 ]
    then
        echo "[all]不可以有第二个参数！"
        exit 0
    else
        judgetype "shm"
        judgetype "sem"
        judgetype "msg"
    fi
else
    judgetype $1 $2
fi

echo "shell执行成功！"
```

#### 实战二

隔一秒检查MySQL在执行的DML语句 

```
while true; do 
  sudo mysql --default-character-set=utf8 -h ${host} -P 3306  -u${username}  -p${passwd} -e "show processlist" | grep Query; 
sleep 1; 
done
```

以逗号分隔每一行，取第一列 

```
awk -F, '{print $1}' doufen_uid_name
```

查看jar包内容

```
jar -tf xxxx.jar
```

```
# 统计一个文件的行数
# 方法1：先用wc -l算出行数，awk取第一个字段
wc -l filename | awk '{print $1}'

# 方法2：巧用END函数和内置变量NR直接输出行数
# NR变量可以输出当前行号，END函数是awk读取完文件之后执行的操作，显而易见的在文件的mo行
awk 'END{pint NR}' filename
```

#### 实战三 批量修改文件名实践

现在想要将后缀前部改为大写JPG 

方法一 

```
#!/bin/bash
for obj in $(ls *.jpg)
do
    mv ${obj} $(echo ${obj/%jpg/JPG})
done
```

方法二

```
rename 's/jpg$/JPG/' *.jpg
```


#### 实战四

```


git checkout .
git pull

env=${1-DEV}
dos2unix ./version_${env}.properties
. ./version_${env}.properties

echo $tomcat
$tomcat/bin/shutdown.sh

sed -i "s/<trade.version>.*<\/trade.version>/<trade.version>${tradeVersion}<\/trade.version>/g" pom.xml
mvn versions:set -DgenerateBackupPoms=false -DnewVersion=${releaseVersion}-${env}-SNAPSHOT
mvn clean install -Dmaven.test.skip=true -DreleaseEnv=${env}

if [ $? -eq 0 ]; then
    echo clean install success
else 
    exit -1
fi

apijar=Goldoffice_api*${releaseVersion}*${env}.jar

apiName=`basename Goldoffice_api/target/$apijar`

dos2unix killProcess.sh
chmod +x killProcess.sh
echo start API ${apiName}
./killProcess.sh Goldoffice_api/target/${apiName}
nohup java -Xms128m -Xmx512m -jar Goldoffice_api/target/${apiName} 1>/dev/null 2>&1 &

if [ $? -eq 0 ]; then
    echo start api success
else 
    exit -1
fi

chmod +x killTomcat.sh
dos2unix killTomcat.sh
./killTomcat.sh $tomcat
rm -rf $appBase/Goldoffice_web-${releaseVersion}-${env}*
rm -rf $appBase/ROOT
\cp Goldoffice_web/target/Goldoffice_web*${releaseVersion}*${env}.war $appBase/Goldoffice_web-${releaseVersion}-${env}.war

echo start web
$tomcat/bin/startup.sh

if [ $? -eq 0 ]; then
    echo start tomcat success
else 
    exit -1
fi
```

```
releaseVersion=1.0.0-IX
releaseDate=2017-03-04
tomcat=/opt/tomcat-branch-bo
appBase=/web_branch_bo
tradeVersion=1.0.0-IX-FD-AWS-SNAPSHOT
dubboServiceVersion=1.0.0
```

知识点：

A:    .  ./a.properties后，shell中可以通过$变量名获取值

B:   $?表示上一步执行的结果

C： sed -i "s/<trade.version>.*<\/trade.version>/<trade.version>${tradeVersion}<\/trade.version>/g" pom.xml 

D:  basename



自动部署tomcat

```
#!/bin/bash
#Desc 重启tomcat服务脚本
#Time Thu Jun 22 22:06:39 CST 2017

#################### 自定义变量 ####################

#定义变量:应用服务器相关文件夹
serverHome=/opt/app/tomcat/tomcat-8-8080
warName=LoadBalance

#设定运行java环境: jdk
export JAVA_HOME=/opt/app/jdk/jdk1.8.0_131

serverBin=$serverHome/bin
serverLog=$serverHome/logs
serverTemp=$serverHome/temp
serverDeploy=$serverHome/webapps
serverWork=$serverHome/work/Catalina/localhost

################### 子函数 ###################

#Desc 获取服务器进程id
get_pid(){
pid=`ps -ef | grep -v grep | grep "$serverHome" | awk "{print $2}" `
}

#Desc 使用tomcat自带脚本关闭, 1秒关闭一次
shutdown(){
#执行三次, 每隔1秒执行一次, 尝试正常关闭Tomcat
for i in 1 2 3
do
if [ -n "$pid" ] ; then
echo "[info ] try to shutdown the server ..."
$serverBin/shutdown.sh &
# 睡眠3秒,再往下执行
sleep 3
get_pid
fi
done
}

#Desc 使用linux kill 命令强制关闭服务器, 尝试3次
force_shutdown(){
for i in 1 2 3
do
if [ -n "$pid" ] ; then
echo "[info ] force shutdown the server ..."
ps -ef | grep -v grep | grep "$serverHome"| awk '{print $2}' | xargs kill -9
# 睡眠3秒,再往下执行
sleep 3
get_pid
fi
done
}

##################### 执行脚本 #####################
# 0. 检测文件是否存在
if [ ! -f "$serverTemp/$warName.war" ]; then
echo "[error] The file $serverTemp/$warName.war is not exsits !!!"
exit 1
else
echo "[info ] Find file: $serverTemp/$warName.war";
fi

# 1. 关闭服务器
#获取当前服务器的进程id
get_pid

# 如果pid 不为空, 则证明已经关闭了
if [ -n "$pid" ] ; then
shutdown
else
echo "[info ] The server is not running !"
fi

# 确认关闭服务器如果还没有关闭, 则强制关闭
if [ -n "$pid" ] ; then
force_shutdown
fi

# 最后确认服务器是否关闭, 如果服务器未关闭,则停止重部署流程
if [ -n "$pid" ] ; then
echo "[error] cannot shutdown the server,please shutdown manually!"
exit
fi

# 2. 备份老版本项目
date_time=`date "+%Y%m%d.%H%M"`
mv $serverDeploy/$warName.war $serverDeploy/$warName.war.$date_time

# 3. 删除部署项目的文件夹
echo "[info ] delete the old project: $serverDeploy/$warName"
rm -rf $serverDeploy/$warName

# 4. 清空服务器工作目录
echo "[info ] clean the project work directory: $serverWork/$warName"
rm -rf $serverWork/$warName

# 5. 清空日志文件
echo "[info ] clean the server log: $serverLog/catalina.out"
echo "" > $serverLog/catalina.out

# 6. 将新项目文件移动到服务器部署文件夹中
echo "[info ] deploy the project: $serverTemp/$warName.war"
mv $serverTemp/$warName.war $serverDeploy

# 7. 重新启动服务器
echo "[info ] start the server: $serverBin/startup.sh &"
$serverBin/startup.sh &

# 8. 监控tomcat 启动日志
tail -f $serverLog/catalina.out
```

