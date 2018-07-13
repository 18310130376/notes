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



#### 权限操作

```
chmod u+x   表示该档案的拥有者权限
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

|             |              |
| ----------- | ------------ |
| pidof  java | java的进程ID |
|             |              |
|             |              |
|             |              |
|             |              |
|             |              |
|             |              |
|             |              |
|             |              |

