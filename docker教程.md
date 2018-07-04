### docker安装

Docker 要求 Ubuntu 系统的内核版本高于 3.10   . 

通过 uname -r 命令查看你当前的内核版本

```
runoob@runoob:~$ uname -r 或
runoob@runoob:~$ uname -sr或
runoob@runoob:~$ uname -a
```

系统版本 

```
sudo lsb_release -a
No LSB modules are available.
Distributor ID: Ubuntu
Description:    Ubuntu 17.04
Release:        17.04
Codename:       zesty
```

查看操作系统是32位还是64位： 

```
yiibai@ubuntu:~$ sudo uname --m
[sudo] password for yiibai:
x86_64
yiibai@ubuntu:~$
```

####  获取Docker 安装包

由于apt官方库里的docker版本可能比较旧，所以先卸载可能存在的旧版本

总命令如下：

```java
sudo apt-get update && apt-get install docker
```

细节：

```
sudo apt-get remove docker docker-engine docker-ce docker.io
```

更新apt包索引：

```
sudo apt-get update
```

安装以下包以使apt可以通过HTTPS使用存储库（repository）：

```
sudo apt-get install -y apt-transport-https ca-certificates curl software-properties-common
```

添加Docker官方的GPG密钥：

```
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
```

如果出错则：gpg: no valid OpenPGP data found.

解决办法：上述命令中有管道符号，curl是个类似下载的命令，因此尝试将上述命令分开两步执行，

1、curl -O https://packages.cloud.google.com/apt/doc/apt-key.gpg #该命令执行后会在当前目录下保存一个名称为nodesource.gpg.key的文件。
2、使用apt-key命令加载获取到的文件  `apt-key add nodesource.gpg.key ` 系统回显OK，说明执行成功。

使用下面的命令来设置stable存储库：

```
$ sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable"
```

再更新一下apt包索引：

```
$ sudo apt-get update
```

安装最新版本的Docker CE：

```
$ sudo apt-get install -y docker-ce
```

在生产系统上，可能会需要应该安装一个特定版本的Docker CE，而不是总是使用最新版本：
列出可用的版本：

```
$ apt-cache madison docker-ce
$ sudo apt-get install docker-ce=<VERSION>
```

验证docker

```
docker version 或 docker -v
```

查看docker服务是否启动：

```
$ systemctl status docker
```

若未启动，则启动docker服务：

```
sudo systemctl start docker
```

经典的hello world：

```
sudo docker run hello-world
```

有以上输出则证明docker已安装成功！

docker: Got permission denied while trying to connect to the Docker daemon socket at unix:///var/run/docker.sock: Post http://%2Fvar%2Frun%2Fdocker.sock/v1.30/containers/create: dial unix /var/run/docker.sock: connect: permission denied.
See 'docker run --help'.

docker守护进程启动的时候，会默认赋予名字为docker的用户组读写Unix socket的权限，因此只要创建docker用户组，并将当前用户加入到docker用户组中，那么当前用户就有权限访问Unix socket了，进而也就可以执行docker相关命令

添加docker用户组

```
sudo groupadd docker
```

 将登陆用户加入到docker用户组中

```
sudo gpasswd -a $USER docker
sudo service docker restart
exit
```

>gpasswd命令管理工作组
>
>-a:添加用户到组
>
>-d:从组删除用户
>
>-A:指定管理员
>
>-r:删除密码
>
>如:将userA添加到groupB用户组里（会保留以前添加的组）:
>
>gpasswd -a userA groupB
>
>注意：添加用户到某一个组可以使用usermod -G groupB userA，但是以前添加的组就会被清空。
>
>将userA设置为groupA的群组管理员
>
>gpasswd -A userA  groupA

更新用户组

```
newgrp docker
```

测试docker命令是否可以使用sudo正常使用

```
docker ps
```

 升级内核

  ```
$ wget http://kernel.ubuntu.com/~kernel-ppa/mainline/v4.10.1/linux-headers-4.10.1-041001_4.10.1-041001.201702260735_all.deb
$ wget http://kernel.ubuntu.com/~kernel-ppa/mainline/v4.10.1/linux-headers-4.10.1-041001-generic_4.10.1-041001.201702260735_amd64.deb
$ wget http://kernel.ubuntu.com/~kernel-ppa/mainline/v4.10.1/linux-image-4.10.1-041001-generic_4.10.1-041001.201702260735_amd64.deb
sudo dpkg -i *.deb
  ```

安装完成后reboot，重启并验证新的内核已经被使用了：

```
$ uname-sr
```

 解决Unable to fetch some archives, maybe run apt-get update or try with --fix-missing

第一种：

```
sudo vim /etc/resolv.conf 
添加nameserver 8.8.8.8
```

第二种：

/etc/apt/sources.list的内容换成

```
deb http://old-releases.ubuntu.com/ubuntu/ raring main universe restricted multiverse

deb-src http://old-releases.ubuntu.com/ubuntu/ raring main universe restricted multiverse

deb http://old-releases.ubuntu.com/ubuntu/ raring-security main universe restricted multiverse

deb-src http://old-releases.ubuntu.com/ubuntu/ raring-security main universe restricted multiverse

deb http://old-releases.ubuntu.com/ubuntu/ raring-updates main universe restricted multiverse

deb-src http://old-releases.ubuntu.com/ubuntu/ raring-updates main universe restricted multiverse

deb http://old-releases.ubuntu.com/ubuntu/ raring-backports main restricted universe multiverse

deb-src http://old-releases.ubuntu.com/ubuntu/ raring-backports main restricted universe multiverse

deb http://old-releases.ubuntu.com/ubuntu/ raring-proposed main restricted universe multiverse

deb-src http://old-releases.ubuntu.com/ubuntu/ raring-proposed main restricted universe multiverse

```

然后sudo apt-get update一下就可以了

#### windows docker安装

docker-toolbox下载

http://mirrors.aliyun.com/docker-toolbox/windows/docker-toolbox/ 或

http://7xawqb.com2.z0.glb.qiniucdn.com/docker/toolbox/releases/download/v1.11.2/DockerToolbox-1.11.2.exe

[https://github.com/boot2docker/windows-installer/releases(这个地址国内下载很慢)](https://github.com/boot2docker/windows-installer/releases)

boot2docker不像docker toolbox那样会在桌面上生成两个快捷方式。只会在桌面上生成一个Boot2Docker Start 
的快捷方式。 
双击该快捷方式，boot2docker会在virtualbox中启动一个虚拟机，这些都不是我们需要关心的事情。

第一次启动时设置：

双击桌面的`Boot2Docker Start`快捷方式进行启动，使用`boot2docker ssh`进入到docker-machine中

```
boot2docker ssh
```

[用这个： https://get.daocloud.io/toolbox/](https://get.daocloud.io/toolbox/)

下载完成后，双击安装文件 

一路Next，接受所有默认安装 （记得选择Git for Windows，如果之前安装了GIT工具则可以不用选择）

在安装过程中，会出现几个其他的安装过程，如Ocracle Corporation等系列软件，全部选择安装即可

安装完成后Docker Quickstart Terminal找不到快捷方式：

我们双击打开，如果打不开我们右键修改下git地址，这里配置正确的路径。

boot2docker 默认用户的用户名是` docker`，密码是 `tcuser`

安装远程连接工具连接docker:

首先：连接docker时，必须要先启动Docker Quickstart Terminal

其次：输入docker的IP和端口(22)，用户名/密码：docker / tcuser

```
docker run -it --rm -p 8888:8888 -v /c/Users/tingting/dropbox/code:/root/opt/workspace -v /c/Users/tingting/dropbox/data:/root/data tingtinglu/caffe_mxnet
```

冒号前为宿主机目录，必须为绝对路径 

① /c/Users/tingting/dropbox/code: 本机的C:\Users\tingting\dropbox\code文件夹 

② /c/Users/tingting/dropbox/data 本机的C:\Users\tingting\dropbox\data文件夹 

冒号后为镜像内挂载的路径 

① /root/opt/workspace docker中的文件夹/root/opt/workspace 

② /root/data  docker中的文件夹/root/data 

这里的本机文件夹为c盘，那么，能否为d盘呢？ 

```
docker run -it --rm -p 8888:8888 -v /d/Dropbox/code:/root /opt/workspace -v /d/Dropbox/data:/root/data tingtinglu/caffe_mxnet
```

发现并不可行！这是因为目前，windows下只支持c盘下的文件夹映射

在CentOS7上安装：

```
yum install -y docker
```

 改变Docker的工作目录：

vim /etc/sysconfig/docker

OPTIONS=--selinux-enabled -H fd:// -g="/data/docker"

注：修改配置文件后重启docker服务才生效。-g="/data/docker"是将Docker的默认根路径从/var/lib/docker改成/data/docker, 比如所有的Docker images都会放到这个目录下。

#### centos安装

```
yum update
yum install docker
systemctl start docker
systemctl enable docker //开机自启动
docker -v
```

#### 配置docker服务

为了避免每次使用docker命令都需要特殊身份，可以将当前用户加入到docker用户组(docker用户组在docker安装时自动创建的)

Docker 1.7版本和Docker CE的配置文件的位置是不同的

- Docker 1.7版本 `/etc/sysconfig/docker`
- Docker CE版本使用 `/etc/docker/daemon.json`

```
sudo usermod  -aG docker USER_NAME
```

修改后退出重新登录即可。docker服务重启：

```
chkconfig docker on
sudo service docker restart
sudo service docker start   # service 命令的用法
sudo systemctl start docker #systemctl 命令的用法
```

每次重启docker服务后，可以通过查看docker版本信息确保服务已正常运行

```
docker version
```

开机启动docker服务

```
service dorcker enable
systemctl enable docker
```

设置代理：

这当然是肯定要做的事情了，因为我们公司内部的办公网和开发网都是需要设置代理才能正常访问某些网站的，所以来设置吧。

```
sudo vi /var/lib/boot2docker/profile
```

我的电脑是接入的开发网，因此这里设置的是开发网的代理地址，如果你的是公办网，请使用办公网的代理地址，可以使用vi进行编辑，因为在没有正确设置代理之前，你可能无法安装vim~~~~

```
docker@boot2docker:~$ cat /var/lib/boot2docker/profile
export HTTP_PROXY=http://dev-proxy.xxx.com:8080
export HTTPS_PROXY=https://dev-proxy.xxx.com:8080
```

重启（windows）

```
boot2docker stop
boot2docker start
```

#### 更换仓库地址

在宿主机器编辑文件

```
mkdir -p /etc/docker
tee /etc/docker/daemon.json <<- 'EOF'
{
    'registry-mirrors':["https://registry.docker-cn.com"]
}
EOF
systemctl daemon-reload
systemctl restart docker
```

#### docker日志

日志分两类，一类是 Docker 引擎日志；另一类是 容器日志。

Docker 引擎日志 
Docker 引擎日志 一般是交给了 Upstart(Ubuntu 14.04) 或者 systemd (CentOS 7, Ubuntu 16.04)。前者一般位于 /var/log/upstart/docker.log 下，后者一般通过 jounarlctl -u docker 来读取。不同系统的位置都不一样，SO上有人总结了一份列表，我修正了一下，可以参考：

##### Docker 引擎日志

```
系统	日志位置
Ubuntu(14.04)	/var/log/upstart/docker.log
Ubuntu(16.04)	journalctl -u docker.service
CentOS 7/RHEL 7/Fedora	journalctl -u docker.service
CoreOS	journalctl -u docker.service
OpenSuSE	journalctl -u docker.service
OSX	~/Library/Containers/com.docker.docker/Data/com.docker.driver.amd64-linux/log/d‌ocker.log
Debian GNU/Linux 7	/var/log/daemon.log
Debian GNU/Linux 8	journalctl -u docker.service
Boot2Docker	/var/log/docker.log
```

##### 容器日志

容器的日志 则可以通过 docker logs 命令来访问，而且可以像 tail -f 一样，使用 docker logs -f 来实时查看。如果使用 Docker Compose，则可以通过 docker-compose logs <服务名> 来查看。

如果深究其日志位置，每个容器的日志默认都会以 json-file 的格式存储于 /var/lib/docker/containers/<容器id>/<容器id>-json.log 下，不过并不建议去这里直接读取内容，因为 Docker 提供了更完善地日志收集方式 - Docker 日志收集驱动。

关于日志收集，Docker 内置了很多日志驱动，可以通过类似于 fluentd, syslog 这类服务收集日志。无论是 Docker 引擎，还是容器，都可以使用日志驱动。比如，如果打算用 fluentd 收集某个容器日志，可以这样启动容器：

```
$ docker run -d \
--log-driver=fluentd \
--log-opt fluentd-address=10.2.3.4:24224 \
--log-opt tag="docker.{{.Name}}" \
nginx
```

其中 10.2.3.4:24224 是 fluentd 服务地址，实际环境中应该换成真实的地址。

查看日志

```
docker logs leafage
docker logs –tail 10 leafage
```

使用filebeat收集日志

```json
{
"registry-mirrors": ["https://56px195b.mirror.aliyuncs.com"],
"cluster-store":"consul://192.168.56.13:8500",
"cluster-advertise": "192.168.56.11:2375",
"log-driver": "fluentd",
"log-opts": {
"fluentd-address":"192.168.56.13:24224",
"tag":"linux-node1.example.com"
}}
```

### 镜像

修改镜像源

Docker 官方中国区：https://registry.docker-cn.com1
网易： http://hub-mirror.c.163.com1
ustc： https://docker.mirrors.ustc.edu.cn1

修改方法

1：直接设置 –registry-mirror 参数，仅对当前的命令有效 

```
docker run hello-world --registry-mirror=https://docker.mirrors.ustc.edu.cn
```

2：修改 /etc/default/docker，加入 DOCKER_OPTS=”镜像地址”，可以有多个 

```
DOCKER_OPTS="--registry-mirror=https://docker.mirrors.ustc.edu.cn"
```

3:支持 systemctl

 的系统，通过 sudo systemctl edit docker.service，会生成 etc/systemd/system/docker.service.d/override.conf 覆盖默认的参数，在该文件中加入如下内容：

```
[Service] 
ExecStart= 
ExecStart=/usr/bin/docker -d -H fd:// --registry-mirror=https://docker.mirrors.ustc.edu.cn
```

4:新版的 Docker 推荐使用 json 配置文件的方式，默认为 /etc/docker/daemon.json，非默认路径需要修改 dockerd 的 –config-file，在该文件中加入如下内容： 

```
{ 
"registry-mirrors": ["https://docker.mirrors.ustc.edu.cn"] 
}
```

docker pull ubuntu  [14.04] 如果不指定TAG（通常表示版本信息）则默认为latest最新版本

docker  run  -it ubuntu:14.04  bash（或者镜像ID，通过docker images查看可能出现多个镜像指向同一镜像ID）    

docker images 列出本地主机上已有镜像的基础信息

更多命令查看 man docker images帮助

为镜像添加新的标签：

```
docker tag hello-world:latest new-hello-world:latest
```

new-hello-world:latest和hello-world:latest镜像ID完全一致，实际指向同一个镜像文件，只是别名不同而已，docker tag命令添加的标签实际上起到了类似链接的作用。

docker inspect  578c3e61a98c 或者【name:TAG】查看镜像的详细信息

docker history e38bc07ac18e 或者【name:TAG】查看镜像各层的创建信息

搜索镜像：

 docker search -s 3 nginx 或者docker search --filter=stars=3 nginx    3级以上带nginx关键字的镜像

使用标签删除镜像：

```
docker rmi  new-hello-world:latest
```

结果发现hello-world:latest不会受影响。当同一个镜像拥有多个标签时，docker rmi只删除该镜像多个标签中指定的标签而已，并不影响镜像文件。

但是，当镜像只剩下最后一个标签的时候，此时docker rmi命令将会彻底删除镜像。例如：

docker rmi  hello-world:latest 将会彻底删除镜像。

使用镜像ID删除：

```
docker rmi   e38bc07ac18e 
```

会先尝试删除所有指向该镜像的标签，然后删除该镜像文件本身。

但是，当有该镜像创建的容器存在时，镜像文件默认是无法删除的。此时可以强制删除：

`docker rmi -f e38bc07ac18e`  强制删除

但是，不建议强制删除一个存在容器依赖的镜像。正确的做法是：`先删除依赖该镜像的所有容器，再删除镜像`。

根据已有镜像的容器来创建镜像

进入运行中的容器docker exec -it containerId /bin/bash

或者docker run -it  imageId /bin/bash 进入容器后：

touch newFile 然后exit。记住此容器的ID。

docker commit -m 'add new file' -a  'docker newbee'  容器的ID  test:01

- **-a :**提交的镜像作者；
- **-c :**使用Dockerfile指令来创建镜像；
- **-m :**提交时的说明文字；
- **-p :**在commit时，将容器暂停。

docker commit c3f279d17e0a  SvenDowideit/testimage:version3

docker images 就可以看到新创建的镜像了。

docker images mymysql:v1   查看某一个镜像

#### 获取镜像

使用`docker pull`命令从网络上下载镜像。

```
docker pull NAME[:TAG]
```

例如

```
$ docker pull ubuntu
Using default tag: latest
latest: Pulling from library/ubuntu
43db9dbdcb30: Downloading 1.494 MB/49.33 MB
2dc64e8f8d4f: Download complete
670a583e1b50: Download complete
Digest: sha256:c6674c44c6439673bf56536c1a15916639c47ea04c3d6296c5df938add67b54b
Status: Downloaded newer image for ubuntu:latest
```

不指定Tag的时候默认使用`:latest`，因此，上述命令实际上是`docker pull ubuntu:latest`。

#### 加速下载镜像

为docker添加国内镜像，加速下载镜像：

修改配置文件/etc/docker/daemon.json，如果该文件没有则进行创建：

```
[root@docker ~]# cat /etc/docker/daemon.json 
{
    "dns": ["192.168.101.2","8.8.8.8"],
    "registry-mirrors": ["https://docker.mirrors.ustc.edu.cn"]
}
```

然后重启docker：

```
# systemctl restart docker
```

#### 查看镜像信息

使用`docker images`可以列出本地主机上已有的镜像列表。

```
$ docker images
REPOSITORY          TAG                 IMAGE ID            CREATED             SIZE
mongo               latest              87bde25ffc68        2 days ago          326.7 MB
ubuntu              latest              42118e3df429        9 days ago          124.8 MB
```

OPTIONS说明：

- **-a :**列出本地所有的镜像（含中间映像层，默认情况下，过滤掉中间映像层）；
- **--digests :**显示镜像的摘要信息；
- **-f :**显示满足条件的镜像；
- **--format :**指定返回值的模板文件；
- **--no-trunc :**显示完整的镜像信息；
- **-q :**只显示镜像ID。

列出本地镜像中REPOSITORY为ubuntu的镜像列表。

```
 docker images ubuntu
 docker images | grep nginx
```

显示出Image id、Repository Tag

```
docker images –format "table {{.ID}}\t{{.Repository}}\t{{.Tag}}"
```

还可以使用`docker inspect`命令查看单个镜像的详细信息

```
$ docker inspect ubuntu
[
    {
        "Id": "sha256:42118e3df429f09ca581a9deb3df274601930e428e452f7e4e9f1833c56a100a",
        "RepoTags": [
            "ubuntu:latest"
        ],
        "RepoDigests": [
            "ubuntu@sha256:c6674c44c6439673bf56536c1a15916639c47ea04c3d6296c5df938add67b54b"
        ],
          },
          ...
        "RootFS": {
            "Type": "layers",
            "Layers": [
                "sha256:ea9f151abb7e06353e73172dad421235611d4f6d0560ec95db26e0dc240642c1",
                "sha256:0185b3091e8ee299850b096aeb9693d7132f50622d20ea18f88b6a73e9a3309c",
                "sha256:98305c1a8f5e5666d42b578043e3266f19e22512daa8c6b44c480b177f0bf006",
                "sha256:9a39129ae0ac2fccf7814b8e29dde5002734c1699d4e9176061d66f5b1afc95c"
            ]
        }
    }
]
```

查看单项信息

```
$ docker inspect -f {{".Config.Hostname"}} ubuntu
827f45722fd6
```

#### 搜索镜像

使用`docker search`命令搜索远程仓库中共享的镜像。

```
docker search TERM
```

例如搜索名称为mysql的镜像

```
$ docker search mysql
NAME                DESCRIPTION                 STARS  OFFICIAL  AUTOMATED
mysql               MySQL is a widely used...   2763   [OK]
mysql/mysql-server  Optimized MySQL Server...   178    [OK]
```

#### 删除镜像

使用`docker rmi`命令删除镜像。

```
docker rmi IMAGE [IMAGE...]
```

其中IMAGE可以是镜像标签或者ID。

例如

```
docker rmi ubuntu
docker rmi php:7.0.1
```

删除所有仓库名为redis的镜像(**-q :**只显示镜像ID )：

```
docker rmi $(docker images -q redis)
```

删除所有在mongo:3.2之前的镜像：

```
docker rmi $(docker images -q -f before=mongo:3.2)
```

> 删除虚悬（悬虚）镜像（也就是名称为的镜像，一般是由于新的镜像出现导致覆盖了旧的镜像，特点：镜像有许多是none。原因：重复pull同一个tag的镜像，并且，在pull新的镜像时（与本机已有的旧镜像具有相同的tag），旧镜像已经被容器占用，那么，在pull新镜像后，之前被占用的旧镜像就会变为none）：

```
docker rmi $(docker images -q -f dangling=true)
docker images -f dangling=true
```

#### 创建镜像

创建镜像有三种方法：

- 基于已有镜像创建
- 基于本地模板导入
- 基于Dockerfile创建

#### 使用已有镜像创建

该方法主要使用`docker commit`命令。

```
docker commit [OPTIONS] CONTAINER [REPOSITORY[:TAG]]
```

包含以下主要选项

- -a --author=""，作者信息
- -m --message=""，提交信息
- -p --pause=true，提价时暂停容器运行

例如

```
$ docker run -i -t ubuntu:latest /bin/bash
root@5a86b68c4e6a:/# ls
bin   dev  home  lib64  mnt  proc  run   srv  tmp  var
boot  etc  lib   media  opt  root  sbin  sys  usr
root@5a86b68c4e6a:~# exit
exit

$ docker commit -m "create a new images" -a "mylxsw" 5a86b68c4e6a test-cont
sha256:68f1237c24a744b05a934f1317ead38fc68061ade7981eaae158a2ba8da02a9b
$ docker images
REPOSITORY          TAG                 IMAGE ID            CREATED             SIZE
test-cont           latest              68f1237c24a7        3 seconds ago       124.8 MB
php                 latest              fe1a2c2228f4        2 days ago          364 MB
mongo               latest              87bde25ffc68        2 days ago          326.7 MB
ubuntu              latest              42118e3df429        9 days ago          124.8 MB
redis               latest              4465e4bcad80        6 weeks ago         185.7 MB
nginx               latest              0d409d33b27e        8 weeks ago         182.8 MB
```

#### 基于本地模板导入

#### 基于Dockerfile创建

#### 存出镜像

使用`docker save`命令保存镜像文件为本地文件。

```
docker save -o ubuntu_latest.tar ubuntu:latest  或 
```

```
docker save 1316871b180b -o /root/dockerfile/loggermanager1.0.tar
```

>将上面的loggermanager镜像保存成一个tar文件，注意如果目录没有，需要提前建立一下，docker不会帮你建立目录的.可以分享loggermanager1.0.tar给别人了

#### 载入镜像

使用`docker load`从本地文件再导入镜像压缩包到本地镜像仓库。

```
docker load --input ubuntu_latest.tar 或 docker load < /c/Users/789/Desktop/ubuntu_latest.tar
```

#### 上传镜像

上传镜像使用`docker push`命令。

```
docker push NAME[:TAG]
```

```
docker push user/test:latest
```

默认上传镜像到DockerHub官方仓库。

### **容器**

#### 新建容器

使用`docker create`命令创建一个容器。

```
docker create -it ubuntu:latest
docker ps -a
```

使用docker创建的容器处于`停止`状态，可是使用docker start命令启动。

通过现象看到，create的时候如果本地没有指定的镜像会去仓库下载。

使用docker镜像nginx:latest创建一个容器,并将容器命名为myrunoob 

```
docker create  --name myrunoob  nginx:latest
```

#### 启动容器

通过`docker start containerId|name `命令来启动一个已经创建额容器。

```
docker start 4e4dd842029a
```

此时通过docker ps命令可以查看一个运行中的容器。

#### 新建并启动容器

```
docker run ubuntu /bin/echo 'hello world'
```

docker run就是docker create和docker start两个命令的组合。 

当用docker run来创建并启动容器时，docker在后台运行的标准操作包括：

- 检查本地是否存在指定的镜像，不存在就从公有仓库下载。
- 利用镜像创建一个容器，并启动该容器。
- 分配一个文件系统给容器，并在只读的镜像层外挂载一层可读写层。
- 从宿主机配置的网桥接口中桥接 一个虚拟接口到容器中。
- 从网桥的地址池配置一个IP地址给容器。
- 执行用户指定的应用程序。
- 执行完毕后容器自动终止。

下面的命令启动一个bash终端，允许用户进行交互:

```
docker run -it ubuntu /bin/bash
ps
```

在容器内ps查看进程，可以看到，只运行了bash应用。

用户可以Ctrl+d或者输入exit命令退出容器，退出后容器就处于退出(Exited)状态。

有时候执行docker run会出错，因为命令无法正常执行容器会直接退出，此时可以查看退出的错误代码。

   默认情况下，常见错误代码包括：

   125：Docker daemon执行出错，例如制定了不支持的Docker命令参数。

   126：所指定命令无法执行，例如权限出错。

   127：容器内命令无法找到。

命令执行出错后，会默认返回错误码。

使用--name标记可以为容器自定义命名：

```
docker run -d -P --name web training/webapp pathon app.py
```

重启容器：`docker restart`将一个运行态的容器终止，然后再重新启动它。

```
docker restart
```

自动重启：

```
docker run –restart=always –name leafage ubuntu /bin/bash 或
docker run –restart=on-failure：5 –name leafage ubuntu /bin/bash 只有容器的退出代码为非0值的时候才会重启，最多重启5次
```

#### 查看容器

查看正在运行在的容器：

```
docker ps
```

- **-a :**显示所有的容器，包括未运行的。
- **-f :**根据条件过滤显示的内容。
- **--format :**指定返回值的模板文件。
- **-l :**显示最近创建的容器。
- **-n :**列出最近创建的n个容器。
- **--no-trunc :**不截断输出。
- **-q :**静默模式，只显示容器编号。
- **-s :**显示总的文件大小。

查看所有容器

```
docker ps -a
```

列出所有创建的容器ID 

```
docker ps -a -q
```

列出最近创建的5个容器信息。

```
docker ps -n 5
```

查看容器更多信息

```
docker inspect leafage
```

查看容器的输出日志：

```
docker logs [container ID or Names]
```

OPTIONS说明：

- **-f :** 跟踪日志输出
- **--since :**显示某个开始时间的所有日志
- **-t :** 显示时间戳
- **--tail :**仅列出最新N条容器日志

跟踪查看容器mynginx的日志输出 

```
docker logs -f mynginx
```

查看容器mynginx从2016年7月1日后的最新10条日志。

```
docker logs --since="2016-07-01" --tail=10 mynginx
```

#### 守护态运行

通过添加-d参数来实现，例如下面的命令会在后台运行：

```
docker run -d ubuntu /bin/sh
```

此时，要获取容器的输出信息，可以使用docker logs命令

```
docker logs 4e4dd842029a
```

#### 终止容器

可以使用docker stop来终止一个运行中的容器。

```
docker stop 4e4dd842029a
```

也可以使用`docker kill` 强制终止容器。**-s :**向容器发送一个信号 

```
$ docker kill -s KILL mynginx
```

当Docker容器中指定的应用终结时，容器也会自动终止。

可以通过docker ps  -qa命令查看所有容器的ID，例如

```
$ docker ps -qa
f13b2d0e88a1
8ce5b4d6570b
```

处于终止状态的容器，可以使用docker start命令来重新启动:

```
docker start 4e4dd842029a
```

此外，docker restart命令会将一个运行态的容器先终止，然后再重新启动它：

```
docker restart 4e4dd842029a
```

退出容器不结束进程（开启守护进程）

> 先按下ctrl p 然后按下 ctrl q

#### 暂停/恢复进程

**docker pause** :暂停容器中所有的进程。

**docker unpause** :恢复容器中所有的进程。

```
docker pause db01
docker unpause db01
```

#### 查看进程信息 

**docker top :**查看容器中运行的进程信息，支持 ps 命令参数。 

查看容器mymysql的进程信息。 

```
runoob@runoob:~/mysql$ docker top mymysql
UID    PID    PPID    C      STIME   TTY  TIME       CMD
999    40347  40331   18     00:58   ?    00:00:02   mysqld
```

 查看所有运行容器的进程信息。

```
for i in  `docker ps |grep Up|awk '{print $1}'`;do echo \ &&docker top $i; done
```

**docker wait :** 阻塞运行直到容器停止，然后打印出它的退出代码。 

```
docker wait CONTAINER 
```

在宿主机查看docker使用cpu、内存、网络、io情况

```
docker stats CONTAINER
docker stats -a
docker stats 2b1b7a428627
```

#### 进入容器

在使用-d参数时，容器启动后会进入后台，用户无法看到容器中的信息，也无法进行操作。

这个时候如果需要进入容器进行操作，有多种方法：

#####  attach命令

重新进入正在运行的容器

```
docker attach f13b2d0e88a1
```

使用attach命令有时候并不方便。当多个窗口同时用attach命令连到同一个容器的时候，所有窗口都会同步显示。当某个窗口因命令阻塞时，其他窗口也无法继续操作。

使用`docker attach`之后，如果使用`Ctrl+C`退出，则容器也会退出运行

#####  exec命令

- [x] 最为推荐的方式

```
docker exec -it f13b2d0e88a1 /bin/bash
```

#### 删除容器

使用docker rm命令删除处于`终止或退出`的容器.

选项：

- -f --force=false 强制终止并删除一个运行中的容器
- -l --link=false 删除容器的连接，但是保留容器
- -v --volumes=false 删除容器挂载的数据卷

```
docker rm f13b2d0e88a1 f23b2d0e985a
docker rm -f f13b2d0e88a1 f23b2d0e985a
```

删除所有容器

>docker rm 'docker ps -a -q'

-a列出所有的容器、-q标志只需要返回容器的ID而不会返回其他的信息

#### 导出容器

到处容器是指导出一个已经创建的容器到一个文件，不管此时这个容器是否处于运行状态。

```
docker export -o test.tar 8ce5b4d6570b 
```

**-o :**将输入内容写到文件 

或者通过重定向：

```markdown
docker export  8ce5b4d6570b > test.tar
```

#### 导入容器

导出的文件又可以使用docker import命令导入变成镜像，该命令格式为：

```
docker import test.tar  aaaa/ubuntu
docker images
cat container-migrate.tar| docker import - test/ubuntu
```

之前介绍的docker load命令来导入一个镜像文件，与docker import命令十分相似

#### 上传文件

主机和容器之间传输文件的话需要用到容器的ID全称（是否为全称，待论证）

首先查看容器的短ID或者name

```
docker ps -a
```

然后根据这两项的任意一项拿到ID全称

```
docker inspect -f '{{.Id}}' HelloDocker
```

有了这个长长的ID的话，本机和容器之间的文件传输就简单了。

```
docker cp 本地文件路径 ID全称:容器路径
```

![img](https://img-blog.csdn.net/20170514211821739?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvTGVhZmFnZV9N/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

![img](https://img-blog.csdn.net/20170514212209844?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvTGVhZmFnZV9N/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

如果是容器传输文件到本地的话，反过来就好了

```
docker cp ID全称:容器文件路径 本地路径
```

![img](https://img-blog.csdn.net/20170514212531195?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvTGVhZmFnZV9N/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

```
# docker cp mysql:/usr/local/bin/docker-entrypoint.sh /root
# docker cp /root/docker-entrypoint.sh mysql:/usr/local/bin/
```



#### 小结

HAProxy工具来代理容器访问,这样在容器出现故障时,可以 快速切换到功能正常的容器.此外建议通过指定合适的容器重启策略，来自动重启退出的容器。

### 访问docker仓库

#### 登录

>我的账号密码：13662241921  /  QWERTYUIOP

可以通过执行docker login命令输入用户名密码和邮箱来完成注册和登录。登录成功后本地用户目录下.dockerctg中将保存用户的认证信息。首先到<https://hub.docker.com/account/signup/> 创建账号。网页版需要人机交互验证码，需要翻墙才能看到验证码。

测试登录：

```
docker login 或 docker login -u 用户名 -p 密码
```

这条命令会完成登录，并将认证信息报错起来供后面使用。个人认证信息将报错到`$HOME/ .dockercfg`文件中.

登出Docker Hub

```
docker logout
```

#### 基本操作

用户无需登录即可通过docker search命令来查找官方仓库中的镜像，并利用docker pull命令将它下载到本地。

```
docker search nginx
```

#### 自动创建

自动创建功能对于需要经常升级镜像内程序来说十分方便。有时候用户创建了镜像，安装了某个软件，如果软件发布新版本则需要手动更新镜像。

而自动创建允许用户通过docker hub指定跟踪一个目标网站（目前支持github或BitBucket）上的项目，一旦项目发生新的提交，则自动执行创建。

配置自动创建，步骤如下：

- 创建并登录docker Hub，以及目标网站；在目标网站中连接账户到Docker Hub
- 在docker Hub中配置一个“自动创建”
- 选取一个目标网站中的项目（需要包含dockerfile）和分支
- 指定dockerfile的位置，并提交创建。

之后在docker hub的’‘自动创建“页面中跟踪每次创建的状态。

### **docker私有仓库** 

**适用于：Docker 1.8**      **环境：****Centos7**

每个版本的docker修改配置内容都不同，请大家看好安装的docker版本。

1）下载 docker registry私有镜像

\# docker pull docker.io/registry

2）修改配置文件

\# gedit /etc/sysconfig/docker

OPTIONS='--selinux-enabled --insecure-registry 192.168.137.17:5000'

注：红色IP为修改的部分

3）从容器中启动Registry

\# docker run -d -p 5000:5000 --name registry -v /home/dockeruser/data:/tmp/registry  docker.io/registry

注：-d 表示该容器在后台运行

-p将主机的5000端口与容器的5000端口映射

--name 将容器的名称命名为registry

-v 表示将镜像的存储位置“/tmp/registry” 变更为宿主机的“/home/dockeruser/data”

注：这一步一定要在步骤2）之后，否则修改配置文件不会生效

4）在上传到私有的registry之前，需要指定新的Registry目的地址，需要在镜像名前加上

主机名和端口的前缀。

\#docker images  node/helloworld

得到22d56cbd9e6

\#docker tag  22d56cbd9e6  192.168.137.17:5000/node/helloworld

注：192.168.137.17:5000 为所在主机的IP地址和端口

5）将打标签的镜像上传到私有库

\# docker push 192.168.137.17:5000/node/helloworld

显示信息如下：

The push refers to a repository [192.168.137.17:5000/node/helloworld] (len: 1)

Sending image list

Pushing repository 192.168.137.17:5000/node/helloworld (1 tags)

d8bd0657b25f: Image successfully pushed

a582cd499e0f: Image successfully pushed

df45579dbc81: Image successfully pushed

Pushing tag for rev [df45579dbc81] on

{http://192.168.137.17:5000/v1/repositories/node/helloworld/tags/latest}

遇到问题：

The push refers to a repository [192.168.137.17:5000/node/helloworld] (len: 1)

unable to ping registry endpoint https://192.168.137.17:5000/v0/

v2 ping attempt failed with error: Get https://192.168.137.17:5000/v2/: dial tcp 192.168.137.17:5000: connection refused

 v1 ping attempt failed with error: Get https://192.168.137.17:5000/v1/_ping: dial tcp 192.168.137.17:5000: connection refused

解决方案：

\# gedit /etc/sysconfig/docker

OPTIONS='--selinux-enabled --insecure-registry 192.168.137.17:5000'

注：这一步一定要在创建私有仓库的容器之前【步骤2）】，否则修改配置文件不会生效

### 默认镜像和容器的位置

Docker 默认的位置在/var/lib/docker,当前所有的镜像、容器都存储在这儿。如果你有任何在运行的容器，停止这些容器，并确保没有容器在运行，然后运行以下命令，确定当前Docker使用的存储驱动。

```
＃ docker info
```

在输出的信息中，查找Storage Driver那行，并记下它。在我的主机上是devicemapper。下一步是停止Docker 服务 

```
sudo systemctl stop docker
```

下一步是在/etc/systemd/system/docker.service.d 目录下创建一个Drop-In文件“docker.conf”，默认 docker.service.d 文件夹不存在。所以你必须先创建它。 

```
# sudo mkdir /etc/systemd/system/docker.service.d
# sudo touch /etc/systemd/system/docker.service.d/docker.conf
```

我们希望Docker 服务，使用docker.conf文件中提到的特定参数，将默认服务所使用的位于/lib/systemd/system/docker.service文件中的参数进行覆盖。 

定义新的存储位置   现在打开docker.conf增加如下内容： 

```
# sudo vi /etc/systemd/system/docker.service.d/docker.conf
[Service]
ExecStart=
ExecStart=/usr/bin/dockerd --graph="/mnt/new_volume" --storage-driver=devicemapper
```

保存并退出VI编辑器，/mnt/new_volume 是新的存储位置，而devicemapper是当前docker所使用的存储驱动。如果你的存储驱动有所不同，请输入之前第一步查看并记下的值。Docker[官方文档](https://goo.gl/VnHP61)中提供了更多有关各种存储驱动器的信息。现在，你可以重新加载服务守护程序，并启动Docker服务了。这将改变新的镜像和容器的存储位置。 

```
# sudo systemctl daemon-reload
# sudo systemctl start docker
```

为了确认一切顺利，运行 # docker info 命令检查Docker 的根目录.它将被更改为/mnt/new_volume 

**如果你已经有存在的容器和镜像，该怎么办？** 

如果你想将现有的容器和镜像迁移到新的位置，*在修改docker.conf之后，不要重新加载daemon守护程序和启动docker服务* *，*（**译者注**：在不添加docker.conf文件的方式下，使用软链接的方法进行改变根目录。）将/var/lib/docker 中已存在的数据移动到新的位置里。然后创建一个符号链接。

Note：我没有尝试过以下方式，因为我不需要保留现有的容器和镜像，但这些步骤应该有效；如果你遇到任何问题和其它任何替代的方法请在下方评论。我会修改帖子。在你准备尝试冒险之前，请备份一下你的数据。

```
# cp -rp /var/lib/docker /mnt/new_volume
```

创建软链接（**译者注**：创建软链之前，请先将原/var/lib/docker目录修改为其它名字，如/var/lib/docker-backup）

```
# mv /var/lib/docker /var/lib/docker-backup-2017-0510
```

```
# ln -s /mnt/new_volume/docker /var/lib/docker
```

然后*重新加载 daemon守护程序和* 启动 docker服务。（**译者注**：这里无需重新加载daemon守护程序，只需启动docker 服务即可）

```
# sudo systemctl daemon-reload
# sudo systemctl start docker 
```

现在已存在的数据应该在软链的源目录内，以及新的容器和镜像将存储在新的位置里，即/mnt/new_volume/docker ，运行 # docker info 进行确认。

（**译者注**：步骤正确的话，此时根目录应该指向了软链接的源目录 /mnt/new_volume/docker，如下：）

```
...省略输出
Name: docker
ID: 5WBA:EF4D:WQ7P:DVRN:JCI4:LWDT:XSR2:G7RE:F5TI:PD3B:A57K:E4QA
Docker Root Dir: /mnt/new_volume/docker
...省略输出
```

### 数据管理

在介绍VOLUME指令之前，我们来看下如下场景需求：

1）容器是基于镜像创建的，最后的容器文件系统包括镜像的只读层+可写层，容器中的进程操作的数据持久化都是保存在容器的可写层上。一旦容器删除后，这些数据就没了，除非我们人工备份下来（或者基于容器创建新的镜像）。能否可以让容器进程持久化的数据保存在主机上呢？这样即使容器删除了，数据还在。

2）当我们在开发一个web应用时，开发环境是在主机本地，但运行测试环境是放在docker容器上。

这样的话，我在主机上修改文件（如html，js等）后，需要再同步到容器中。这显然比较麻烦。

3）多个容器运行一组相关联的服务，如果他们要共享一些数据怎么办？

对于这些问题，我们当然能想到各种解决方案。而docker本身提供了一种机制，可以将主机上的某个目录与容器的某个目录（称为挂载点、或者叫卷）关联起来，容器上的挂载点下的内容就是主机的这个目录下的内容，这类似linux系统下mount的机制。 这样的话，我们修改主机上该目录的内容时，不需要同步容器，对容器来说是立即生效的。 挂载点可以让多个容器共享。

容器数据管理主要有两种方式：

- 数据卷：容器内数据直接映射到本地主机环境

- 数据卷容器：使用特定容器维护数据卷


  **一、通过docker run命令** 

1、运行命令：docker run --name test -it -v /home/xqh/myimage:/data ubuntu /bin/bash

其中的 -v 标记 在容器中设置了一个挂载点 /data（就是容器中的一个目录），并将主机上的 /home/xqh/myimage 目录中的内容关联到 /data下。

这样在容器中对/data目录下的操作，还是在主机上对/home/xqh/myimage的操作，都是完全实时同步的，因为这两个目录实际都是指向主机目录。

2、运行命令：docker run --name test1 -it -v /data ubuntu /bin/bash

上面-v的标记只设置了容器的挂载点，并没有指定关联的主机目录。这时docker会自动绑定主机上的一个目录。通过docker inspect 命令可以查看到。

```
xqh@ubuntu:~/myimage$ docker inspect test1
[
{
    "Id": "1fd6c2c4bc545163d8c5c5b02d60052ea41900a781a82c20a8f02059cb82c30c",
.............................
    "Mounts": [
        {
            "Name": "0ab0aaf0d6ef391cb68b72bd8c43216a8f8ae9205f0ae941ef16ebe32dc9fc01",
            "Source": "/var/lib/docker/volumes/0ab0aaf0d6ef391cb68b72bd8c43216a8f8ae9205f0ae941ef16ebe32dc9fc01/_data",
            "Destination": "/data",
            "Driver": "local",
            "Mode": "",
            "RW": true
        }
    ],
...........................
```

上面 Mounts下的每条信息记录了容器上一个挂载点的信息，"Destination" 值是容器的挂载点，"Source"值是对应的主机目录。

可以看出这种方式对应的主机目录是自动创建的，其目的不是让在主机上修改，而是让多个容器共享。

**通过dockerfile创建挂载点** 

上面介绍的通过docker run命令的-v标识创建的挂载点只能对创建的容器有效。

通过dockerfile的 VOLUME 指令可以在镜像中创建挂载点，这样只要通过该镜像创建的容器都有了挂载点。

还有一个区别是，通过 VOLUME 指令创建的挂载点，无法指定主机上对应的目录，是自动生成的。

```
#test
FROM ubuntu
MAINTAINER hello1
VOLUME ["/data1","/data2"]
```

上面的dockfile文件通过VOLUME指令指定了两个挂载点 /data1 和 /data2.

我们通过docker inspect 查看通过该dockerfile创建的镜像生成的容器，可以看到如下信息

```
    "Mounts": [
        {
            "Name": "d411f6b8f17f4418629d4e5a1ab69679dee369b39e13bb68bed77aa4a0d12d21",
            "Source": "/var/lib/docker/volumes/d411f6b8f17f4418629d4e5a1ab69679dee369b39e13bb68bed77aa4a0d12d21/_data",
            "Destination": "/data1",
            "Driver": "local",
            "Mode": "",
            "RW": true
        },
        {
            "Name": "6d3badcf47c4ac5955deda6f6ae56f4aaf1037a871275f46220c14ebd762fc36",
            "Source": "/var/lib/docker/volumes/6d3badcf47c4ac5955deda6f6ae56f4aaf1037a871275f46220c14ebd762fc36/_data",
            "Destination": "/data2",
            "Driver": "local",
            "Mode": "",
            "RW": true
        }
    ],
```

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

可以看到两个挂载点的信息。

**三、容器共享卷（挂载点）**

docker run --name test1 -it myimage /bin/bash

上面命令中的 myimage是用前面的dockerfile文件构建的镜像。 这样容器test1就有了 /data1 和 /data2两个挂载点。

下面我们创建另一个容器可以和test1共享 /data1 和 /data2卷 ，这是在 docker run中使用 --volumes-from标记，如：

可以是来源不同镜像，如：

docker run --name test2 -it --volumes-from test1  ubuntu  /bin/bash

也可以是同一镜像，如：

docker run --name test3 -it --volumes-from test1  myimage  /bin/bash

上面的三个容器 test1 , test2 , test3 均有 /data1 和 /data2 两个目录，且目录中内容是共享的，任何一个容器修改了内容，别的容器都能获取到。

**四、最佳实践：数据容器**

如果多个容器需要共享数据（如持久化数据库、配置文件或者数据文件等），可以考虑创建一个特定的数据容器，该容器有1个或多个卷。

其它容器通过--volumes-from 来共享这个数据容器的卷。

因为容器的卷本质上对应主机上的目录，所以这个数据容器也不需要启动。

如： docker run --name dbdata myimage echo "data container"

说明：有个卷，容器之间的数据共享比较方便，但也有很多问题需要解决，如权限控制、数据的备份、卷的删除等。这些内容后续文章介绍。

**五、挂载宿主机已存在目录后，在容器内对其进行操作，报“Permission denied”。**

可通过两种方式解决：

1> 关闭selinux。

临时关闭：# setenforce 0

永久关闭：修改/etc/sysconfig/selinux文件，将SELINUX的值设置为disabled。

2> 以特权方式启动容器 

指定--privileged参数

如：# docker run -it --privileged=true -v /test:/soft centos /bin/bash

#### 数据卷[文件夹映射]

数据卷是一个可以供容器使用的特殊目录，它绕过了文件系统，提供了以下特性

- 在容器之间共享和重用
- 修改立马生效
- 对数据卷的更新不会影响镜像
- 卷会一直存在，直到没有容器使用

在运行容器的时候，使用`-v`选项创建数据卷，`可以多次`使用，创建多个数据卷。

```
$ docker run -i -t --name test-vol -v /Users/mylxsw/Downloads:/opt/aicode ubuntu /bin/bash
root@7ab155e22ec7:/# ls /opt/aicode/
PHP2016@DevLink  container-migrate.tar  removeDocker.sh  test-cont.tar  ubuntu-test.tar
```

上述命令将本地的/Users/mylxsw/Downloads目录映射到了容器的/opt/aicode目录。

如果宿主机上的目录指定的是相对目录(opt，没有/开头)，则所谓的相对路径指的是/var/lib/docker/volumes/，与宿主机的当前目录无关。 

> 可以指定`:ro`，设置映射目录为只读: `-v /Users/mylxsw/Downloads:/opt/aicode:ro`，同时，`-v`也支持挂载单个文件到容器。
>
> 加了:ro之后，容器内对所挂载数据卷内的数据就无法修改了。

**一、创建一个数据卷**

```
如下为容器添加一个数据卷，并将容器名改为data。这个数据卷在容器里的目录是/opt/data
[root@localhost ~]# docker images
REPOSITORY          TAG                 IMAGE ID            CREATED             SIZE
docker.io/ubuntu    latest              0ef2e08ed3fa        2 weeks ago         130 MB
 
[root@localhost ~]# docker run --name data -v /opt/data -t -i docker.io/ubuntu /bin/bash
root@2b9aebcf6ce8:/# cd /opt/data/
root@2b9aebcf6ce8:/opt/data# ls
root@2b9aebcf6ce8:/opt/data# echo "123" > 123
root@2b9aebcf6ce8:/opt/data# echo "123123" > 123123
root@2b9aebcf6ce8:/opt/data# ls
123  123123
 
[root@localhost volumes]# docker ps
CONTAINER ID        IMAGE               COMMAND               CREATED             STATUS              PORTS                     NAMES
2b9aebcf6ce8        docker.io/ubuntu    "/bin/bash"           49 seconds ago      Up 48 seconds                                 data
 
在宿主机上，查看对应上面的那个数据卷的目录路径：
[root@localhost ~]# docker inspect data|grep /var/lib/docker/volumes
                "Source": "/var/lib/docker/volumes/89d6562b9c1fe10dd21707cb697a5d481b3c1b000a69b762f540fa826a16972a/_data",
[root@localhost ~]# ls /var/lib/docker/volumes/89d6562b9c1fe10dd21707cb697a5d481b3c1b000a69b762f540fa826a16972a/_data
123  123123
[root@localhost ~]# echo "asdhfjashdfjk" >> /var/lib/docker/volumes/89d6562b9c1fe10dd21707cb697a5d481b3c1b000a69b762f540fa826a16972a/_data/123
[root@localhost ~]#
 
root@2b9aebcf6ce8:/opt/data# ls
123  123123
root@2b9aebcf6ce8:/opt/data# cat 123
123
asdhfjashdfjk
```

**二、挂载宿主机文件或目录到容器数据卷**

```
可以直接挂载宿主机文件或目录到容器里，可以理解为目录映射，这样就可以让所有的容器共享宿主机数据，从而只需要改变宿主机的数据源就能够影响到所有的容器数据。
 
注意：
-v后面的映射关系是"宿主机文件/目录:容器里对应的文件/目录"，其中，宿主机上的文件/目录是要提前存在的，容器里对应的文件/目录会自动创建。
 
数据卷权限：
挂载的数据默认为可读写权限。
但也可以根据自己的需求，将容器里挂载共享的数据设置为只读，这样数据修改就只能在宿主机上操作。如下实例：
 
1）挂载宿主机文件到容器上
[root@localhost ~]# cat /etc/web.list
192.168.1.100
192.168.1.101
192.168.1.103
[root@localhost ~]# docker run -t -i --name test -v /etc/web.list:/etc/web.list:ro docker.io/centos /bin/bash
[root@e21a3fefa3ae /]# cat /etc/web.list
192.168.1.100
192.168.1.101
192.168.1.103
[root@e21a3fefa3ae /]# echo "192.168.1.115" >> /etc/web.list
bash: /etc/web.list: Read-only file system
[root@e21a3fefa3ae /]#
  
在宿主机上修改共享数据
[root@localhost ~]# echo "192.168.1.115" >> /etc/web.list
[root@localhost ~]#
  
[root@e21a3fefa3ae /]# cat /etc/web.list
192.168.1.100
192.168.1.101
192.168.1.103
192.168.1.115
 
2）挂载宿主机目录到容器上
[root@localhost ~]# mkdir /var/huanqiupc
[root@localhost ~]# echo "test" > /var/huanqiupc/test
[root@localhost ~]# echo "test1" > /var/huanqiupc/test1
[root@localhost ~]# docker run -t -i --name hqsb -v /var/huanqiupc:/opt/huantime docker.io/centos /bin/bash
[root@87cf93ce46a9 /]# cd /opt/huantime/
[root@87cf93ce46a9 huantime]# ls
test  test1
[root@87cf93ce46a9 huantime]# cat test
test
[root@87cf93ce46a9 huantime]# cat test1
test1
[root@87cf93ce46a9 huantime]# echo "1231" >>test
[root@87cf93ce46a9 huantime]# echo "44444" >>test1
 
宿主机上查看
[root@localhost ~]# cat /var/huanqiupc/test
test
1231
[root@localhost ~]# cat /var/huanqiupc/test1
test1
44444
 
3）挂载多个目录
[root@localhost ~]# mkdir /opt/data1 /opt/data2
[root@localhost ~]# echo "123456" > /opt/data1/test1
[root@localhost ~]# echo "abcdef" > /opt/data2/test2
[root@localhost ~]# docker run --name data -v /opt/data1:/var/www/data1 -v /opt/data2:/var/www/data2:ro -t -i docker.io/ubuntu /bin/bash
root@cf2d57b9bee1:/# ls /var/www/data1
test1
root@cf2d57b9bee1:/# ls /var/www/data2
test2
root@cf2d57b9bee1:/# cat /var/www/data1/test1
123456
root@cf2d57b9bee1:/# cat /var/www/data2/test2
abcdef
root@cf2d57b9bee1:/# echo "date1" >> /var/www/data1/test1
root@cf2d57b9bee1:/# echo "date2" >> /var/www/data2/test2
bash: /var/www/data2/test2: Read-only file system
root@cf2d57b9bee1:/#
```

#### 数据卷容器

如果用户需要在容器之间共享一些持续更新的数据，最简单的方法是使用数据卷容器。数据卷容器实际上就是一个普通的容器，专美提供数据卷供其他容器使用。

```
$ docker run -it -v /backup --name backup ubuntu
root@be8de791d367:/#
```

上述命令创建了一个用来作为数据卷的容器，接下来创建几个server容器，用于向该数据卷写入数据，写入数据后，多个容器之间是互通的。

```
$ docker run -it --volumes-from backup --name server1 ubuntu
```

使用`--volumes-from`指定要数据卷容器。

备份数据卷容器中的内容，可以参考以下命令

```
docker run --volumes-from backup -v $(pwd):/backup --name worker ubuntu tar cvf /backup/backup.tar /backup
```

恢复则使用下面的命令

```
docker run -v /backup --name backup2 ubuntu /bin/bash
docker run --volumes-from backup2 -v $(pwd):/backup ubuntu tar xvf /backup/backup.tar
```

> 使用--volumes-from参数所挂载数据卷的容器自身并不需要保持运行状态。

删除挂在的容器，数据卷不会自动删除。如果要删除一个数据卷，必须在删除最后一个挂载它的容器显式使用docker rm -v 命令来指定同时删除关联的容器。

详情步骤：

```shell
启动一个名为xqsj_Container容器，此容器包含两个数据卷/var/volume1和/var/volume2（这两个数据卷目录是在容器里的，容器创建的时候会自动生成这两目录）
注意一个细节：
下面的创建命令中，没有加-t和-i参数，所以这个容器创建好之后是登陆不了的！
-i：表示以“交互模式”运行容器
-t：表示容器启动后会进入其命令行
[root@linux-node2 ~]# docker run -v /var/volume1 -v /var/volume2 --name xqsj_Container centos /bin/bash
[root@linux-node2 ~]#
 
所以要想创建容器后能正常登陆，就需要添加上面两个参数
[root@localhost ~]# docker run -t -i -v /var/volume1 -v /var/volume2 --name xqsj_Container centos /bin/bash
[root@73a34f3c1cd9 /]#

查看宿主机上与数据卷对应的目录路径：
[root@localhost ~]# docker inspect xqsj_Container|grep /var/lib/docker/volumes
                "Source": "/var/lib/docker/volumes/b8d2e5bcadf2550abd36ff5aa544c721a45464a4406fb50979815de773086627/_data",
                "Source": "/var/lib/docker/volumes/a34fa3a0a7a2f126b0d30a32b1034f20917ca7bd0dda346014d768b5ebb68f6b/_data",
                
 由上面命令结果可以查到，两个数据卷/var/volume1和/var/volume2下的数据在/var/lib/docker/volumes/下对于的两个目录的_data下面
 
创建App_Container容器，挂载xqsj_Container容器中的数据卷
[root@linux-node2 ~]# docker run -t -i --rm --volumes-from xqsj_Container --name App_Container centos /bin/bash
[root@b9891bcdfed0 /]# ls /var/volume1                           //发现这两个数据卷都存在
[root@b9891bcdfed0 /]# ls /var/volume2
[root@b9891bcdfed0 /]# echo "this is volume1" > /var/volume1/test1
[root@b9891bcdfed0 /]# echo "this is volume2" > /var/volume1/test2

可以再创建一个容器，挂载App_Container中从xqsj_Container挂载的数据卷。当然也可以直接挂载初始的xqsj_Container容器数据卷
[root@linux-node2 ~]# docker run -t -i --rm --volumes-from App_Container --name LastApp_Container centos /bin/bash
[root@b4c27e360614 /]# ls /var/volume1
test1
[root@b4c27e360614 /]# ls /var/volume2
test2
[root@b4c27e360614 /]# cat /var/volume1/test1 
this is volume1
[root@b4c27e360614 /]# cat /var/volume2/test2 
this is volume2

即便是删除了初始的数据卷容器xqsj_Container，或是删除了其它容器，但只要是有容器在使用该数据卷，那么它里面的数据就不会丢失！（除非是没有容器在使用它们）
```





###  网络配置

Docker目前提供了**映射容器端口到宿主主机**和**容器互联**的机制为容器提供网络服务。

#### 端口映射

容器中运行了网络服务，我们可以通过`-P`或者`-p`参数指定端口映射。

- **-P** Docker会随机映射一个49000-49900之间的端口到容器内部的开放端口。
- **-p** 可以指定要映射的端口，格式为`ip:hostPort:containerPort`，可以多次使用`-p`指定多个映射的端口。

例如：

```
docker run -d -p 127.0.0.1:5000:5000 training/webapp python app.py
docker run -d -p 5000:5000 training/webapp python app.py
docker run -d -p 5000:5000/udp training/webapp python app.py
docker run -d -P training/webapp python app.py
#使用 ip::containerPort 绑定 localhost 的任意端口到容器的 5000 端口，本地主机会自动分配一个端口
docker run -d -p 127.0.0.1::5000 training/webapp python app.py
#还可以使用 udp 标记来指定 udp 端口
docker run -d -p 127.0.0.1:5000:5000/udp training/webapp python app.py
docker run -d -p 5000:5000  -p 3000:80 training/webapp python app.py
```

使用docker port [容器名称] 容器内端口 查看端口映射绑定的地址。

```
docker port nostalgic_morse 5000
runoob@runoob:~$ docker port mymysql
3306/tcp -> 0.0.0.0:3306
```

- 容器有自己的内部网络和 IP 地址（使用 `docker inspect` 可以获取所有的变量）

#### 容器互联通信

容器的链接（linking）系统是除了端口映射外的另一种容器应用之间交互的方式，它会在源和接收容器之间创建一个隧道，接收容器可以看到源容器指定的信息。

容器之间互联通过`--link`参数指定，格式为`--link name:alias`，其中name为要链接到的容器的名称，`alias`为这个连接的别名。

```
docker run -d --name mysql-demo -e MYSQL_ROOT_PASSWORD=root mysql
docker run --rm --name web --link mysql-demo:db ubuntu env
```

![-w567](https://segmentfault.com/img/remote/1460000006760441)

使用`docker ps`可以看到容器的连接。

```
docker ps
CONTAINER ID  IMAGE                     COMMAND               CREATED             STATUS             PORTS                    NAMES
349169744e49  training/postgres:latest  su postgres -c '/usr  About a minute ago  Up About a minute  5432/tcp                 db, web/db
aed84ee21bde  training/webapp:latest    python app.py         16 hours ago        Up 2 minutes       0.0.0.0:49154->5000/tcp  web
```

`可以看到自定义命名的容器，db 和 web，db 容器的 names 列有 db 也有 web/db。这表示 web 容器链接到 db 容器，web 容器将被允许访问 db 容器的信息`。

Docker 在两个互联的容器之间创建了一个安全隧道，而且不用映射它们的端口到宿主主机上。在启动 db 容器的时候并没有使用 `-p` 和 `-P` 标记，从而避免了暴露数据库端口到外部网络上

Docker会在两个互联的容器之间创建一个安全的隧道，而且不用映射端口到宿主主机。Docker中通过两种方式为容器公开连接信息：

- **环境变量** 环境变量的方式采用连接别名的大写前缀开头，比如前面的例子中，所有以`DB_`开头的环境变量。
- **更新/ect/hosts文件** Docker也会添加host信息到父容器的`/etc/hosts`文件



使用 `env` 命令来查看 web 容器的环境变量

```
$ sudo docker run --rm --name web2 --link db:db training/webapp env
DB_NAME=/web2/db
DB_PORT=tcp://172.17.0.5:5432
DB_PORT_5000_TCP=tcp://172.17.0.5:5432
DB_PORT_5000_TCP_PROTO=tcp
DB_PORT_5000_TCP_PORT=5432
DB_PORT_5000_TCP_ADDR=172.17.0.5
. . .
```

其中 DB_ 开头的环境变量是供 web 容器连接 db 容器使用，前缀采用大写的连接别名。

除了环境变量，Docker 还添加 host 信息到父容器的 `/etc/hosts` 的文件。下面是父容器 web 的 hosts 文件

查看`/etc/hosts`文件：

```
$ sudo docker run -t -i --rm --link db:db training/webapp /bin/bash
root@aed84ee21bde:/opt/webapp# cat /etc/hosts
172.17.0.7  aed84ee21bde
. . .
172.17.0.5  db
```

这里有 2 个 hosts，第一个是 web 容器，web 容器用 id 作为他的主机名，第二个是 db 容器的 ip 和主机名。 可以在 web 容器中安装 ping 命令来测试跟db容器的连通。

```
root@aed84ee21bde:/opt/webapp# apt-get install -yqq inetutils-ping
root@aed84ee21bde:/opt/webapp# ping db
PING db (172.17.0.5): 48 data bytes
56 bytes from 172.17.0.5: icmp_seq=0 ttl=64 time=0.267 ms
56 bytes from 172.17.0.5: icmp_seq=1 ttl=64 time=0.250 ms
56 bytes from 172.17.0.5: icmp_seq=2 ttl=64 time=0.256 ms
```

用 ping 来测试db容器，它会解析成 `172.17.0.5`。 *注意：官方的 ubuntu 镜像默认没有安装 ping，需要自行安装。

用户可以链接多个父容器到子容器，比如可以链接多个 web 到 db 容器上

### ssh远程连接container

一  **container安装ssh服务**

首先进入Container，进行以下步骤

① 安装ssh

```
apt-get update
sudo apt-get install openssh-server #安装ssh服务器
安装ssh-client命令：
sudo apt-get install openssh-client
service ssh status # 查看ssh服务启动情况ps -e|grep ssh
service ssh start # 启动ssh服务sudo /etc/init.d/ssh start
```

② 配置ssh，允许root登陆

```
vi /etc/ssh/sshd_config
将PermitRootLogin的值从withoutPassword改为yes
```

允许无密码登录 

```
同上，修改ssh服务配置文件，两种情况：

1） 将PermitEmptyPasswords yes前面的#号去掉

2） 将PermitEmptyPasswords 参数值修改为yes，如下图：

PermitRootLogin without-password 改为 PermitRootLogin yes`
#PermitRootLogin yes #允许root用户以任何认证方式登录（用户名密码认证和公钥认证）
#PermitRootLogin without-password #只允许root用公钥认证方式登录
#PermitRootLogin no #不允许root用户以任何认证方式登录
PasswordAuthentication yes 改为 PasswordAuthentication no
```

无论哪种，最后PermitEmptyPasswords参数值为yes 

③ 重启ssh服务

```
service ssh restart # 重启动ssh服务   或
/etc/initd.d/sshd restart
```

二 保存Container镜像

另外开启Docker Quickstart Terminal，保存镜像

```
docker ps #查看正在运行的container
**找到所要保存的container的container id，假设为xxxxxx**
docker commit xxxxxxxx tomjerry/foobar
（注：tomjerry/foobar为要保存的新镜像的名字，可任意写）
```

三  重新运行container

```
docker run -it -p 50001:22 tomjerry/foobar /bin/bash
service ssh start
注意-p 50001:22这句，意思是将docker的50001端口和container的22端口绑定，这样访问docker的50001等价于访问container的22端口
```

四 ssh连接container

```
你可以用xshell或putty等ssh客户端工具连接container
首先假设各方的ip如下：
本地windows ip： 192.168.99.1
docker ip：192.168.99.100
container ip：172.17.0.3
那么，你要远程container，则要访问以下地址：
ssh 192.168.99.100:50001
ssh root@192.168.99.100:50001
这样通过访问docker的50001端口，就神奇的间接连通到container的22端口了，从而达到ssh连接container的目的，至此。
```

以下是用`Dockerfile`设置sshd服务容器，您可以使用连接并检查其他容器的卷,或者可以快速访问测试容器。

```
# sshd
# VERSION               0.0.1
FROM  ubuntu:12.04
MAINTAINER Thatcher R. Peskens "thatcher@dotcloud.com"
# make sure the package repository is up to date
RUN apt-get update
RUN apt-get install -y openssh-server
RUN mkdir /var/run/sshd
RUN echo 'root:screencast' |chpasswd
EXPOSE 22
CMD    ["/usr/sbin/sshd", "-D"]
```

使用如下命令构建镜像： 

```
$ sudo docker build --rm -t eg_sshd .
```

然后运行它，你可以使用`docker port`来找出容器端口22映射到主机的端口 

```
$ sudo docker run -d -P --name test_sshd eg_sshd
$ sudo docker port test_sshd 22
0.0.0.0:49154
```

现在你可以使用ssh登陆Docker进程的主机IP地址，端口是49154(IP地址可以使用ifconfig获取)： 

```
$ ssh root@192.168.1.2 -p 49154
# The password is ``screencast``.
$$
```

最后，清理停止的容器，并且删除容器，然后删除镜像。 

```
$ sudo docker stop test_sshd
$ sudo docker rm test_sshd
```

### docker守护进程配置启动

安装好docker后，需要启动docker守护进程。有多种启动方式。 

**一、服务的方式**

因为docker守护进程被安装成服务。所以，可以通过服务的方式启停docker守护进程，包括查看状态。

sudo start docker  //启动

sudo stop docker  //停止

sudo status docker  //查看状态

**二、利用docker daemon命令**

sudo docker daemon

利用sudo ps -A 可以获取守护进程的进程号

**三、让远程api可以访问docker守护进程**

`sudo docker daemon -H tcp://0.0.0.0:2375`

这需要每次都带参数，而且无法通过服务的方式启动。

可以通过在配置文件配置，来让服务启动也可以让远程访问生效。

对于ubuntu操作系统，修改/etc/default/docker文件中的DOCKER_OPTS设置，如下：

```
# Use DOCKER_OPTS to modify the daemon startup options.
#DOCKER_OPTS="--dns 8.8.8.8 --dns 8.8.4.4"
DOCKER_OPTS="-H=unix:///var/run/docker.sock -H=0.0.0.0:2375"
```

这样通过 sudo start docker启动docker后，就可以远程访问了，如：
http://192.168.142.138:2375/info   //相当于在本地 docker info访问

http://192.168.142.138:2375/containers/json  //返回活动的容器

http://192.168.142.138:2375/containers/json？all=1 返回所有容器

除了利用web访问外，还可利用docker命令远程访问，如果别的机器上也装了docker，

访问方式如： docker -H  192.168.142.138:2375 info

注意：可以采用 sudo docker daemon 方式启动守护进程，然后进行本地的docker命令操作，可以铜鼓日志查看不同命令对应的url

 在linux下，可以通过curl工具访问url，因为返回的是json串，非格式化的。可以结合python命令，转为格式化的json，让看起来比较清楚。如：

curl http://192.168.142.138:2375/images/json | python -mjson.tool



### Docker网络模式

查看当前 docker0 ip

```
sudo ifconfig docker0
```

>1：host模式  使用--net=host指定  （使用主机的IP访问）
>
>2：container模式，使用net=container:NAME_or_ID指定
>
>3：none模式，使用--net=none指定
>
>4：bridge模式，使用--net=bridge指定（docker默认）



### 容器指定自定义网段静态IP地址

**摘要：**Docker容器运行的时候默认会自动分配一个默认网桥所在网段的IP地址。但很多时候我们可能需要让容器运行在预先指定的静态IP地址上，因为早期的版本不支持静态IP，因此网上大部分方法都是借助pipework等去实现，然而在最新的版本中，Docker已经内嵌支持在启动时指定静态IP了。

Docker守护进程启动以后会创建默认网桥docker0，其IP网段通常为172.17.0.1。在启动Container的时候，Docker将从这个网段自动分配一个IP地址作为容器的IP地址。最新版(1.10.3)的Docker内嵌支持在启动容器的时候为其指定静态的IP地址。这里分为三步：

**第一步：安装最新版的Docker**

备注：操作系统自带的docker的版本太低，不支持静态IP，因此需要自定义安装。

root@localhost:~# apt-get update

root@localhost:~# apt-get install curl

root@localhost:~# curl -fsSL https://get.docker.com/ | sh

root@localhost:~# docker -v

Docker version 1.10.3, build 20f81dd

**第二步：创建自定义网络**

备注：这里选取了172.18.0.0网段，也可以指定其他任意空闲的网段

docker network create --subnet=172.18.0.0/16 shadownet

注：shadown为自定义网桥的名字，可自己任意取名。

**第三步：在你自定义的网段选取任意IP地址作为你要启动的container的静态IP地址**

备注：这里在第二步中创建的网段中选取了172.18.0.10作为静态IP地址。这里以启动shadowsocks为例。

docker run -d -p 2001:2001 --net shadownet --ip 172.18.0.10 oddrationale/docker-shadowsocks -s 0.0.0.0 -p 2001 -k 123456 -m aes-256-cfb

**其他**

备注1：这里是固定IP地址的一个应用场景的延续，仅作记录用，可忽略不看。

备注2：如果需要将指定IP地址的容器出去的请求的源地址改为宿主机上的其他可路由IP地址，可用iptables来实现。比如将静态IP地址172.18.0.10出去的请求的源地址改成公网IP104.232.36.109(前提是本机存在这个IP地址)，可执行如下命令：

iptables -t nat -I POSTROUTING -o eth0 -d  0.0.0.0/0 -s 172.18.0.10  -j SNAT --to-source 104.232.36.109

### Docker Compose 项目

http://www.ityouknow.com/docker/2018/03/22/docker-compose.html

Docker Compose 是 Docker 官方编排（Orchestration）项目之一，负责快速在集群中部署分布式应用。

Dockerfile 可以让用户管理一个单独的应用容器；而 Compose 则允许用户在一个模板（YAML 格式）中定义一组相关联的应用容器（被称为一个 `project`，即项目），例如一个 Web 服务容器再加上后端的数据库服务容器等。

安装 Compose 之前，要先安装 Docker，在此不再赘述。

### Docker Machine

see:https://www.cnblogs.com/jsonhc/p/7784466.html

Docker Machine 最主要有两个作用：

- 使用 Docker Machine 方便在不同的环境中使用 Docker ，比如：Win/Mac
- 使用 Docker Machine 方便在云环境下批量部署 Docker环境，比如：私有云，公有云批量安装Docker环境

Docker Machine 是一个用于配置和管理你的宿主机（上面具有 Docker Engine 的主机）的工具。通常，你在你的本地系统上安装 Docker Machine。Docker Machine有自己的命令行客户端 docker-machine 和 Docker Engine 客户端 docker。你可以使用 Machine 在一个或多个虚拟系统上安装 Docker Engine。 

Docker Machine 安装很简单 

```
curl -L https://github.com/docker/machine/releases/download/v0.14.0/docker-machine-`uname -s`-`uname -m` >/tmp/docker-machine && \
install /tmp/docker-machine /usr/local/bin/docker-machine

#完成后，查看版本信息。
docker-machine -v
docker-machine version 0.14.0, build 89b8332
```

查看是否存在可用的主机 

```
$  docker-machine ls
NAME   ACTIVE   DRIVER   STATE   URL   SWARM   DOCKER   ERRORS
```

创建一个主机

```
docker-machine create --driver virtualbox default
```

这个命令会下载 boot2docker，基于 boot2docker 创建一个虚拟主机。boot2docker 是一个轻量级的 linux 发行版，基于专门为运行 docker 容器而设计的 Tiny Core Linux 系统，完全从 RAM 运行，45Mb左右，启动时间约5s。 

再次查看服务列表 

```
docker-machine ls
NAME      ACTIVE   DRIVER       STATE     URL                         SWARM   DOCKER        ERRORS
default   *        virtualbox   Running   tcp://192.168.99.100:2376           v17.12.1-ce
```

创建主机成功后，可以通过 env 命令来让后续操作对象都是目标主机 

```
$ docker-machine env default
export DOCKER_TLS_VERIFY="1"
export DOCKER_HOST="tcp://192.168.99.100:2376"
export DOCKER_CERT_PATH="C:\Users\hkrt-neo\.docker\machine\machines\default"
export DOCKER_MACHINE_NAME="default"
export COMPOSE_CONVERT_WINDOWS_PATHS="true"
# Run this command to configure your shell:
# eval $("D:\Program Files\Docker Toolbox\docker-machine.exe" env default)
```

相当于配置了一个环境变量 default，后续根据提示在命令行输入命令之后就可以操作 default 主机。

可以通过 SSH 登录到主机

```
$ docker-machine ssh default

docker@default:~$ docker --version
Docker version 17.12.1-ce, build 7390fc6
```

连接到主机之后你就可以在其上使用 Docker 了，退出虚拟机使用命令：`exit` 

------

docker-machine 是docker官方提供的docker管理工具。
通过docker-machine可以轻松的做到：

　　在Windows平台和MAC平台安装和运行docker

　　搭建和管理多个docker 主机

　　搭建swarm集群

环境win下面安装的virtualbox，virtualbox安装的centos7，网络模式NAT+hostonly

ip：192.168.56.102（hostonly）

1、安装docker-machine：

```
curl -L https://github.com/docker/machine/releases/download/v0.13.0/docker-machine-`uname -s`-`uname -m` >/tmp/docker-machine &&
chmod +x /tmp/docker-machine &&
sudo cp /tmp/docker-machine /usr/local/bin/docker-machine
```

2、查看docker-machine版本：

```
# docker-machine version
[root@docker ~]# docker-machine version
docker-machine version 0.13.0, build 9ba6da9
```

3、在centos7环境下创建machine：

```
[root@localhost ~]# docker-machine create -d virtualbox default
Creating CA: /root/.docker/machine/certs/ca.pem
Creating client certificate: /root/.docker/machine/certs/cert.pem
Running pre-create checks...
Error with pre-create check: "VBoxManage not found. Make sure VirtualBox is installed and VBoxManage is in the path"
```

但是却报错了，以为virtualbox安装的centos7环境支持的是virtualbox驱动，才发现环境安装支持virtualbox驱动

使用virtualbox驱动需要安装virtualbox，而Ubuntu的解决方法为：

于是采用generic驱动，具体介绍查看官网：https://docs.docker.com/machine/drivers/generic/

```
[root@localhost ~]# docker-machine create -d generic --generic-ip-address=192.168.56.102 --generic-ssh-key ~/.ssh/id_rsa --generic-ssh-user=root vm
```

Running pre-create checks...

Creating machine...

(vm) Importing SSH key...

Waiting for machine to be running, this may take a few minutes...

Detecting operating system of created instance...

Waiting for SSH to be available...

Error creating machine: Error detecting OS: Too many retries waiting for SSH to be available. Last error: Maximum number of retries (60) exceeded

　　--generic-ip-address=192.168.56.102：这里的ip是指本机，如果需要为其他远程docker主机安装可以改为其他docker主机ip（这里是本地创建docker-machine）

依然报错，这是由于docker-machine为本机创建machine时也需要进行ssh认证：

```
[root@localhost ~]# ssh-keygen
[root@localhost ~]# ssh-copy-id root@192.168.56.102
```

将密码发给自己，然后重新继续创建machine：

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
[root@localhost ~]# docker-machine create -d generic --generic-ip-address=192.168.56.102 --generic-ssh-key ~/.ssh/id_rsa --generic-ssh-user=root vm
Running pre-create checks...
Creating machine...
(vm) Importing SSH key...
Waiting for machine to be running, this may take a few minutes...
Detecting operating system of created instance...
Waiting for SSH to be available...
Detecting the provisioner...
Provisioning with centos...
Copying certs to the local machine directory...
Copying certs to the remote machine...
Setting Docker configuration on the remote daemon...
Checking connection to Docker...
Docker is up and running!
To see how to connect your Docker Client to the Docker Engine running on this virtual machine, run: docker-machine env vm
```

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

于是终于创建machine成功了

查看docker-machine：

```
[root@localhost ~]# docker-machine ls  
NAME   ACTIVE   DRIVER    STATE     URL                         SWARM   DOCKER        ERRORS
vm     -        generic   Running   tcp://192.168.56.102:2376           v17.09.0-ce 
```

查看vm的环境变量：

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
[root@localhost ~]# docker-machine env vm
export DOCKER_TLS_VERIFY="1"
export DOCKER_HOST="tcp://192.168.56.102:2376"
export DOCKER_CERT_PATH="/root/.docker/machine/machines/vm"
export DOCKER_MACHINE_NAME="vm"
# Run this command to configure your shell: 
# eval $(docker-machine env vm)
```

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

加载环境变量：

```
[root@localhost ~]# eval $(docker-machine env vm)
```

利用ssh登录到machine中：

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
[root@localhost ~]# docker-machine ssh --help
Usage: docker-machine ssh [arg...]
Log into or run a command on a machine with SSH.
Description:
Arguments are [machine-name] [command]
[root@localhost ~]# docker-machine ssh vm
Last login: Sat Nov  4 17:55:53 2017 from 192.168.56.102
[root@vm ~]# 
```

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

现在在本地环境创建一个容器启动：

```
[root@localhost ~]# docker run -d --name=nginx nginx
[root@localhost ~]# docker ps -a
CONTAINER ID        IMAGE               COMMAND                  CREATED              STATUS              PORTS               NAMES
6e62975fab90        nginx               "nginx -g 'daemon ..."   About a minute ago   Up 59 seconds       80/tcp              nginx
```

然后ssh远程到docker-machine中：

```
[root@localhost ~]# docker-machine ssh vm           
Last login: Sat Nov  4 18:13:27 2017 from 192.168.56.102
[root@vm ~]# docker ps -a
CONTAINER ID        IMAGE               COMMAND                  CREATED              STATUS              PORTS               NAMES
6e62975fab90        nginx               "nginx -g 'daemon ..."   About a minute ago   Up About a minute   80/tcp              nginx
```

可以看见docker主机和docker-machine主机里面的容器id相同

利用docker-machine能够安装docker和创建容器

 

 

上面是本地为自己创建machine，现在为远程的docker主机创建docker-machine：

环境：centos7，192.168.101.14，vmware下面安装的docker以及docker-machine，为192.168.56.102这台docker主机创建machine：

（两个ip不同进行了转发所以可以访问（前面是vm下面的nat的静态ip，后面是virtualbox的两张网卡（nat和host only）））

1、首先将192.168.101.14和主机192.168.56.102进行ssh连接认证：

```
[root@docker ~]# ssh-keygen
[root@docker ~]# ssh-copy-id root@192.168.56.102
```

2、创建machine：

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
[root@docker ~]# docker-machine create -d generic --generic-ip-address=192.168.56.102 --generic-ssh-key ~/.ssh/id_rsa --generic-ssh-user=root default
Creating CA: /root/.docker/machine/certs/ca.pem
Creating client certificate: /root/.docker/machine/certs/cert.pem
Running pre-create checks...
Creating machine...
(default) Importing SSH key...
Waiting for machine to be running, this may take a few minutes...
Detecting operating system of created instance...
Waiting for SSH to be available...
Detecting the provisioner...
Provisioning with centos...
Copying certs to the local machine directory...
Copying certs to the remote machine...
Setting Docker configuration on the remote daemon...
Checking connection to Docker...
Docker is up and running!
To see how to connect your Docker Client to the Docker Engine running on this virtual machine, run: docker-machine env default
```

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

执行环境变量，进入到machine环境：

```
[root@docker ~]# docker-machine env default
[root@docker ~]# eval $(docker-machine env default)
```

3、查看创建的machine：

```
[root@docker ~]# docker-machine ls
NAME      ACTIVE   DRIVER    STATE     URL                         SWARM   DOCKER        ERRORS
default   -        generic   Running   tcp://192.168.56.102:2376           v17.09.0-ce  
```

可以看见在192.168.101.14环境上为远程主机192.168.56.102创建的machine

4、创建容器：

```
[root@docker ~]# docker run -d --name=nginx nginx（本地没有nginx镜像）
b1f08986f6d5dbb1ede699e915bde734bab278fbe70f93af06ec2267fae2fef3
[root@docker ~]# docker ps -a
CONTAINER ID        IMAGE               COMMAND                  CREATED             STATUS              PORTS               NAMES
b1f08986f6d5        nginx               "nginx -g 'daemon ..."   4 seconds ago       Up 3 seconds        80/tcp              nginx
```

5、ssh到machine：

```
[root@docker ~]# docker-machine ssh default
Last login: Sat Nov  4 18:51:49 2017 from 192.168.56.1
[root@default ~]# docker ps -a
CONTAINER ID        IMAGE               COMMAND                  CREATED             STATUS              PORTS               NAMES
b1f08986f6d5        nginx               "nginx -g 'daemon ..."   23 seconds ago      Up 22 seconds       80/tcp              nginx
```

现在查看远程主机是否创建了容器：

```
[root@localhost ~]# docker ps -a
could not read CA certificate "/root/.docker/machine/machines/default/ca.pem": open /root/.docker/machine/machines/default/ca.pem: no such file or directory
```

报错原因：

由于刚刚在192.168.56.102为自己设置了machine，保留了之前machine的环境变量，虽然现在删除了，但是设置了环境变量，将刚刚设置的环境变量取消：

```
[root@localhost ~]# unset DOCKER_TLS_VERIFY
[root@localhost ~]# unset DOCKER_CERT_PATH
[root@localhost ~]# unset DOCKER_MACHINE_NAME
[root@localhost ~]# unset DOCKER_HOST
```

然后重新查看：

```
[root@localhost ~]# docker ps -a
CONTAINER ID        IMAGE               COMMAND                  CREATED             STATUS              PORTS               NAMES
b1f08986f6d5        nginx               "nginx -g 'daemon ..."   8 minutes ago       Up 8 minutes        80/tcp              nginx
```

可以发现，为远程主机创建容器成功

现在192.168.101.14上面存在镜像centos_nginx:v4，而远程主机192.168.56.102没有该镜像，现在创建容器，看是否远程主机能够创建成功？

```
[root@docker ~]# docker pull registry.cn-hangzhou.aliyuncs.com/wadeson/jsonhc:v4
[root@docker ~]# docker images
REPOSITORY                                         TAG                 IMAGE ID            CREATED             SIZE
nginx                                              latest              b72d63324dbb        13 hours ago        108MB
registry.cn-hangzhou.aliyuncs.com/wadeson/jsonhc   v4                  6c5128aaff05        2 days ago          464MB
```

然后在远程主机查看：

```
[root@localhost ~]# docker images
REPOSITORY                                         TAG                 IMAGE ID            CREATED             SIZE
nginx                                              latest              b72d63324dbb        13 hours ago        108MB
registry.cn-hangzhou.aliyuncs.com/wadeson/jsonhc   v4                  6c5128aaff05        2 days ago          464MB
```

可以看见两主机的镜像同步，也是容器也是同步的

vm下面的docker可以为virtualbox下面的docker创建容器

而更多的是本地可以为云等其他环境创建容器，通过docker-machine

 

在这之前192.168.101.14的images都不见了，那是因为设置machine环境变量：

```
unset DOCKER_TLS_VERIFY
unset DOCKER_CERT_PATH
unset DOCKER_MACHINE_NAME
unset DOCKER_HOST
```

执行上面将machine的环境变量取消就可以返回原来的环境了：

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
[root@docker ~]# docker images
REPOSITORY                                         TAG                 IMAGE ID            CREATED             SIZE
centos_init                                        v1                  383ff3502443        26 hours ago        448MB
centos_nginx                                       v8                  6f792dc07c35        2 days ago          464MB
centos_nginx                                       v7                  9e875385d6be        2 days ago          464MB
centos_nginx                                       v6                  959fdf4d4288        2 days ago          464MB
centos_nginx                                       v5                  5c1131306686        2 days ago          464MB
registry.cn-hangzhou.aliyuncs.com/wadeson/jsonhc   v4                  6c5128aaff05        2 days ago          464MB
192.168.101.14:5000/centos_nginx                   v4                  6c5128aaff05        2 days ago          464MB
centos_nginx                                       v4                  6c5128aaff05        2 days ago          464MB
centos_nginx                                       v3                  0e49a2c0562f        2 days ago          464MB
centos_nginx                                       v2                  2031faf8894a        2 days ago          464MB
centos_nginx                                       v1                  78d18f16e757        3 days ago          464MB
registry                                           latest              2ba7189700c8        9 days ago          33.3MB
ubuntu                                             latest              747cb2d60bbe        3 weeks ago         122MB
centos                                             latest              196e0ce0c9fb        7 weeks ago         197MB
```

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

而如果需要返回machine环境就继续执行machine环境变量就行，这种方式很好的隔离了本地和远程镜像和容器

### docker machine 管理远程docker服务

https://www.cnblogs.com/jsonhc/p/7784466.html

https://www.cnblogs.com/52fhy/p/8413029.html

http://blog.51cto.com/hostman/2097376

https://www.jianshu.com/p/7ba1a93e6de4

http://www.cnblogs.com/onlyworld/p/5105849.html

https://www.oschina.net/translate/remote-access-to-docker-with-tls

配置服务端开启远程连接：参照:`访问远程机器docker`

第一步：查看machine列表。以下docker-machine安装在我的开发环境windows下：

```
PS C:\Users\Administrator> docker-machine ls
NAME   ACTIVE   DRIVER   STATE   URL   SWARM   DOCKER   ERRORS
PS C:\Users\Administrator>
```

第二步：配置远程主机可以免交互认证登录，因为本地开发环境windows，需要用到Git Bash用来支持下面的命令

产生公钥与私钥对：

```
ssh-keygen -t rsa  ##或 
ssh-keygen
```

`ssh-copy-id **将key写到远程机器的 ~/ .ssh/authorized_key.文件中`**ssh-copy-id命令**可以把本地主机的公钥复制到远程主机的authorized_keys文件上，ssh-copy-id命令也会给远程主机的用户主目录（home）和`~/.ssh`, 和`~/.ssh/authorized_keys`设置合适的权限  

```
ssh-copy-id root@192.168.135.131
ssh-copy-id -i 475402366.pub root@192.168.135.131  #-i：指定公钥文件(先pwd，cd到公钥文件夹下)
```

```
service sshd restart
ssh root@192.168.135.131    #此时如果上面一切正常则不会提示输入密码的

服务器文件列表：
root@wukang-virtual-machine:~/.ssh# ls
authorized_keys
root@wukang-virtual-machine:~/.ssh# pwd
/root/.ssh
```

以上如果还是需要登录密码则：

```

#①文件权限
chmod 700 ~/.ssh
chmod 600 ~/.ssh/authorized_keys
sudo chmod 700 /home/当前用户

service sshd restart
service ssh status
sudo /etc/init.d/ssh restart
#②配置项
sudo vi /etc/ssh/sshd_config：
AuthorizedKeysFile	.ssh/authorized_keys
RSAAuthentication yes （无用）
PubKeyAuthentication yes
PasswordAuthentication yes   #当完成全部设置，以密钥方式登录成功后，可以禁用密码登录：PasswordAuthentication no
PermitEmptyPasswords yes
StrictModes no
PermitRootLogin yes
#③查看日志
如果还不行，可以用ssh -vvv 目标机器ip 查看详情，根据输出内容具体问题具体分析了
ssh -vvv 192.168.135.131
tail -fn 300 /var/log/auth.log
④ 客户端的文件也要位于用户.ssh目录下，且名字最好为id_rsa.pub和id_rsa
```

第三步：远程主机上执行该命令，添加 Defaults   visiblepw 一行

```
vim /etc/sudoers
```

第四步：

```
docker-machine create --driver generic --generic-ip-address=192.168.48.128 --generic-ssh-key id_rsa  --generic-ssh-user=root wk01
```

第五步：

Error creating machine: Error checking the host: Error checking and/or regenerating the certs: There was an error validating certificates for host "192.168.48.128:2376": dial tcp 192.168.48.128:2376: i/o timeout
You can attempt to regenerate them using 'docker-machine regenerate-certs [name]'.
Be advised that this will trigger a Docker daemon restart which will stop running containers.

```
docker-machine regenerate-certs dockerStd01
docker-machine regenerate-certs -f dockerStd01

docker-machine -D ssh dockerStd01
```

```
去修改 ~/.docker/machine/machines/<NAME>/config.json，将其中的 TlsVerify 改为 false
```

第六步：

```
docker-machine restart dockerStd01
output  : sudo: sorry, you must have a tty to run sudo

vi /etc/sudoers (最好用visudo命令)
注释掉 Default requiretty 一行
#Default requiretty
```

```
output  : Connection to 192.168.48.128 closed by remote host.

1:
vi  /etc/ssh/sshd_config
# MaxSessions 10
去掉前面的"#" 并把数字改大，最后重启sshd service sshd restart 然后重新连接即可
2:每次正常退出SSH连接
每次执行完命令后用输入"exit" 退出, 防止连接数过多.
systemctl restart sshd

 Connection to 192.168.48.128 closed by remote host.
```

第七步：

```
[root@dockerStd01 ~]# docker images
Cannot connect to the Docker daemon at unix:///var/run/docker.sock. Is the docker daemon running?
vi /usr/lib/systemd/system/docker.service

```

第八步：

```
$ docker-machine ssh dockerStd01
Last login: Wed Jul  4 08:51:57 2018 from 192.168.48.1
[root@dockerStd01 ~]#
```

第九步 查看服务状态

```
 Redirecting to /bin/systemctl status docker.service
● docker.service
   Loaded: loaded (/etc/systemd/system/docker.service; static; vendor preset: disabled)
   Active: active (running) since Wed 2018-07-04 10:01:26 EDT; 6min ago
 Main PID: 3099 (dockerd)
   CGroup: /system.slice/docker.service
           ├─3099 dockerd -H tcp://0.0.0.0:5000 -H unix:///var/run/docker.sock --storage-driver devicemapper --tlsverify --tlscacert /etc/docker/ca.pem --tlscert /etc/docker/server.pem --tlskey /etc/d...
           └─3106 docker-containerd -l unix:///var/run/docker/libcontainerd/docker-containerd.sock --metrics-interval=0 --start-timeout 2m --state-dir /var/run/docker/libcontainerd/containerd --shim d...

Jul 04 10:01:27 dockerStd01 docker[3099]: time="2018-07-04T10:01:27.934582037-04:00" level=info msg="API listen on /var/run/docker.sock"
Jul 04 10:01:27 dockerStd01 docker[3099]: time="2018-07-04T10:01:27.934674496-04:00" level=info msg="API listen on [::]:5000"
Jul 04 10:01:36 dockerStd01 dockerd[3099]: http: TLS handshake error from 192.168.48.128:60080: tls: first record does not look like a TLS handshake
Jul 04 10:01:36 dockerStd01 dockerd[3099]: http: TLS handshake error from 192.168.48.128:60082: tls: first record does not look like a TLS handshake
Jul 04 10:02:22 dockerStd01 dockerd[3099]: http: TLS handshake error from 192.168.48.128:60084: tls: 
```

第十步  `此问题还未解决`

```
FATA[0000] Get 
http:///var/run/docker.sock/v1.18/images/json: dial unix /var/run/docker.sock: 
permission denied. Are you trying to connect to a TLS-enabled daemon without TLS?

ubuntu@VM-84-201-ubuntu:/usr/anyesu/docker$ docker -H tcp://127.0.0.1:2375 version
Get http://127.0.0.1:2375/v1.29/version: malformed HTTP response "\x15\x03\x01\x00\x02\x02".
* Are you trying to connect to a TLS-enabled daemon without TLS?

//如果还没有 docker group 就添加一个
sudo groupadd docker
//将用户加入该 group 内。然后退出并重新登录就生效啦。
sudo gpasswd -a ${USER} docker
//重启 docker 服务
sudo service docker restart
//切换当前会话到新 group
newgrp - docker
注意，最后一步是必须的，否则因为 groups 命令获取到的是缓存的组信息，刚添加的组信息未能生效，所以 docker images 执行时同样有错
```



```
docker-machine -D ssh wk02
```

```
docker --tlsverify -H tcp://192.168.48.128:2376 images
它会使用  ~/.docker下的证书来验证连接. 如果使用的是其他的类型, 可以通过--tls* 来查询docker --help的帮助文档


sudo docker -H tcp://0.0.0.0:2376 -H unix:///var/run/docker.sock -d &
```

第十一步：在客户端：

```
docker -H tcp://192.168.48.128:2376 images
curl 192.168.48.128:2376/info
```



### 如何快速清理 docker 资源

如果经常使用 docker，你会发现 docker 占用的资源膨胀很快，其中最明显也最容易被察觉的应该是对磁盘空间的占用。本文将介绍如何快速的清理 docker 占用的系统资源，具体点说就是删除那些无用的 镜像、容器、网络和数据卷。

`查看 docker 占用的资源`

在进行资源清理之前我们有必要搞清楚 docker 都占用了哪些系统的资源。这需要综合使用不同的命令来完成。
**docker container ls**：默认只列出正在运行的容器，-a 选项会列出包括停止的所有容器。
**docker image ls**：列出镜像信息，-a 选项会列出 intermediate 镜像(就是其它镜像依赖的层)。
**docker volume ls**：列出数据卷。
**docker network ls**：列出 network。
**docker info**：显示系统级别的信息，比如容器和镜像的数量等。

通过这些命令查看 docker 使用的资源情况后，相信你已经决定要清理 docker 占用的一些资源了！让我们先从那些未被使用的资源开始。

`只删除那些未被使用的资源`

Docker 提供了方便的 docker system prune 命令来删除那些已停止的容器、dangling 镜像、未被容器引用的 network 和构建过程中的 cache：

```
$ docker system prune
```

![img](https://images2018.cnblogs.com/blog/952033/201806/952033-20180613130828003-525835614.png)

安全起见，这个命令默认不会删除那些未被任何容器引用的数据卷，如果需要同时删除这些数据卷，你需要显式的指定 --volumns 参数。比如你可能想要执行下面的命令：

```
$ docker system prune --all --force --volumns
```

这次不仅会删除数据卷，而且连确认的过程都没有了！注意，使用 --all 参数后会删除所有未被引用的镜像而不仅仅是 dangling 镜像。
这里有必要解释一下何为 dangling images，其实可以简单的理解为未被任何镜像引用的镜像。比如在你重新构建了镜像后，那些之前构建的且不再被引用的镜像层就变成了 dangling images：

![img](https://images2018.cnblogs.com/blog/952033/201806/952033-20180613130913846-62053387.png)

在本地的镜像更新之后，就会出现类似图中红框内的 <none> 镜像。这表示旧的镜像已经不再被引用了，此时它们就变成了 dangling images。如果使用 -a 参数，你还会发现另外一种类型的 <none> 镜像，它们的 repository 和 tag 列都表现为 <none>：

![img](https://images2018.cnblogs.com/blog/952033/201806/952033-20180613130946874-1564572085.png)

这些镜像被称为 intermediate 镜像(就是其它镜像依赖的层)。

我们还可在不同在子命令下执行 prune，这样删除的就是某类资源：

```
docker container prune # 删除所有退出状态的容器
docker volume prune # 删除未被使用的数据卷
docker image prune # 删除 dangling 或所有未被使用的镜像
```

`让 docker 回到安装时的状态`

这里的 "安装时的状态" 指资源占用情况而不是 docker 的相关配置。这也是一种比较常见的用例，比如笔者就需要在一个干净的 docker 环境中自动化的还原出某天的一个生产环境(使用生产环境的备份数据)用于 bug 调查。让我们一起来看看都需要做些什么？
回想我们前面介绍的 docker system prune --all --force --volumns 命令，如果在执行这个命令前系统中所有的容器都已停止，那么这个命令就会移除所有的资源！好，现在让我们想办法停掉系统中的所有容器。
docker container stop 命令可以停止一个或多个容器，我们只需要把系统中所有在运行的容器罗列出来就可以了。由于 docker 并不介意我们再次停止一个已经停止了的容器，干脆简单粗暴点，直接列出所有的容器(包括已经停止的)！

```
$ docker container ls -a -q
```

![img](https://images2018.cnblogs.com/blog/952033/201806/952033-20180613131104642-45170848.png)

-a 显示所有的容器，-q 只显示数字形式的容器 ID。
然后把这里命令执行的结果作为 docker container stop 命令的参数：

```
$ docker container stop $(docker container ls -a -q)
```

完整的恢复 docker 环境的命令如下：

```
$ docker container stop $(docker container ls -a -q) && docker system prune --all --force --volumns
```

和前面的 prune 命令类似，也可以完全删除某一类资源：
**删除容器**：docker container rm $(docker container ls -a -q)
**删除镜像**：docker image rm $(docker image ls -a -q)
**删除数据卷**：docker volume rm $(docker volume ls -q)
**删除 network**：docker network rm $(docker network ls -q)

`创建 shell 别名`

上面的命令可以完成任务但是却很繁琐，我们可以通过 shell 的别名功能来简化这些命令的执行。

```
alias docker-clean-unused='docker system prune --all --force --volumes'
alias docker-clean-all='docker stop $(docker container ls -a -q) && docker system prune --all --force --volumes'
```

把上面的命令写入到用户的 ~/.bashrc 文件中就可以了！

![img](https://images2018.cnblogs.com/blog/952033/201806/952033-20180613131228852-299627938.png)

执行一次清理任务：

![img](https://images2018.cnblogs.com/blog/952033/201806/952033-20180613131304527-2111007780.png)

`总结`

经常清理系统资源不仅能够让系统运行的更流畅，也利于我们把精力集中在相关的重点资源上面。所以建议大家能够使用相关的资源清理命令，让 docker 保持清爽和高效。

### 通过 UI 管理 docker

安装 portainer

```
$ docker run -d -p 9000:9000 -v /var/run/docker.sock:/var/run/docker.sock portainer/portainer
```

然后在浏览器中通过 9000 端口访问 protainer 站点就可以了！

 **配置管理员信息**
第一次访问 portainer 时需要设置管理员的账号信息，我们把管理员账号命名为 admin 并设置密码：

![img](https://images2018.cnblogs.com/blog/952033/201806/952033-20180628150152934-868051380.png)

**选择 portainer 管理的对象** 简单起见我们先配置 portainer 管理本机的 docker，选择下图中的 "Local" 并点击 "connect" 按钮： 

![img](https://images2018.cnblogs.com/blog/952033/201806/952033-20180628150248969-602479751.png) 



### 命令汇总

`TODO：命令行自动补全`

see：http://www.dockerinfo.net/image%E9%95%9C%E5%83%8F

容器操作：命令中需要指定 container 时，既可使用其名称，也可使用其 id

| 操作                                                         | 命令                                                         | 示例                                                         |
| ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ |
| 创建 container                                               | docker create                                                | docker create chenhengjie123/xwalkdriver                     |
| 创建并运行 container                                         | docker run                                                   | docker run chenhengjie123/xwalkdriver /bin/bash              |
| 创建运行容器 后进入其 bash 控制台                            | docker run -t -i image /bin/bash                             | docker run -t -i ubuntu /bin/bash                            |
| 创建并运行 container 并让其在后台运行，并端口映射            | docker run -p [port in container]:[port in physical system] -d [image][command] | docker run -p 5000:5000 -d training/webapp python app.py     |
| 查看正在运行的所有 container 信息                            | docker ps 等价于**docker container ls**                      | docker ps                                                    |
| 查看最后创建的 container                                     | docker ps -l                                                 | docker ps -l                                                 |
| 查看所有 container ，包括正在运行和已经关闭的                | docker ps -a                                                 | docker ps -a                                                 |
| 输出指定 container 的 stdout 信息（用来看 log ，效果和 tail -f 类似，会实时输出。） | docker logs -f [container]                                   | docker logs -f nostalgic_morse                               |
|                                                              | docker container logs [containerID]                          |                                                              |
| 获取 container 指定端口映射关系                              | docker port [container][port]                                | docker port nostalgic_morse 5000                             |
| 查看 container容器内部运行的进程                             | docker top [container]                                       | docker top nostalgic_morse                                   |
| 查看 container 详细信息JSON格式                              | docker inspect [container]                                   | sudo docker inspect -f  "{{ .Name }}" aed84ee21bde           |
| 停止 continer                                                | docker stop [container]                                      | docker stop nostalgic_morse                                  |
|                                                              | docker container stop [container]                            |                                                              |
| 强制停止 container                                           | docker kill [container]                                      | docker kill nostalgic_morse                                  |
| 启动一个已经停止的 container                                 | docker start [container]                                     | docker start nostalgic_morse                                 |
|                                                              | docker container start [containerID]                         |                                                              |
| 重启 container (若 container 处于关闭状态，则直接启动)       | docker restart [container]                                   | docker restart nostalgic_morse                               |
| 删除 container（ 容器是停止状态）                            | docker rm [container]                                        | docker rm nostalgic_morse                                    |
| 容器卷挂载                                                   | docker run -d -P --name web -v <宿主机目录>:<容器目录> training/webapp python app.py | docker run -d -P --name web -v /webapp training/webapp python app.py |
| 首先启动了一个容器，并为这个容器增加一个数据卷/dbdata，然后启动另一个容器，共享这个数据卷 | docker run -d --volumes-from db1 --name db2 training/postgres | docker run -d --volumes-from db1 --name db2 training/postgres |
| 执行容器内命令                                               | docker run ubuntu:15.10 /bin/echo "Hello world"              | docker run ubuntu:15.10 /bin/echo "Hello world"              |
| 查看docker用户信息                                           | id docker命令                                                | id docker命令                                                |
| 查看容器root用户密码                                         | docker容器启动时root密码随机分配的                           | docker  logs f1f0c7c59254 >2&1 \| grep '^User:' \| tail -n1  |
| 查看容器内发生改变的文件                                     | docker diff mysqldb                                          | docker diff mysqldb                                          |
| 实时输出Docker服务器端的事件，包括容器的创建，启动，关闭等   | docker events                                                | docker events                                                |
| 退出登录                                                     | docker logout                                                | docker logout                                                |
| 登录                                                         | docker login                                                 | docker login                                                 |
| 此时，通过docker stats可以观察到此时的资源使用情况是固定不变的， | docker stats                                                 | docker stats                                                 |
| docker network create -d bridge webnet                       | docker network create -d bridge webnet                       | docker network create -d bridge webnet                       |
| 列出数据卷                                                   | **docker volume ls**                                         | **docker volume ls**                                         |
| 列出 network                                                 | **docker network ls**                                        | **docker network ls**                                        |
| 将文件拷贝到本机                                             | docker container cp [containID]:[/path/to/file] .            | docker container cp [containID]:[/path/to/file] .            |
| 查看服务状态                                                 | service docker status                                        | service docker status                                        |

镜像操作：

注意：image 中没有指定 tag 名称的话默认使用 latest 这个 tag 。然而 latest 的含义和 VCS 中的 head 不一样，不是代表最新一个镜像，仅仅是代表 tag 名称为 latest 的镜像。若不存在 tag 名称为 latest 的镜像则会报错。

| 操作                            | 命令                                         | 示例                                                  |
| ------------------------------- | -------------------------------------------- | ----------------------------------------------------- |
| 从 container 创建 image         | docker commit [container][imageName]         | docker commit nostalgic_morse ouruser/sinatra:v2      |
| 从 Dockerfile 创建 image        | docker build -t [imageName] [pathToFolder    | docker build ouruser/sinatra:v3 .                     |
| 查看本地所有 image              | docker images等价于**docker image ls**       | docker images                                         |
| 模糊匹配                        | `docker images  47*`                         | `docker images 47*`                                   |
|                                 | docker images -a \|grep none                 | docker images\|grep node-demo                         |
| 在 registry 中搜索镜像          | docker search [query]                        | docker search ubuntu                                  |
| 从 registry 中获取镜像          | docker pull [imageName]                      | docker pull ubuntu:14.04, docker pull training/webapp |
| 给 image 打 tag                 | docker tag [imageId][imageName]              | docker tag 5db5f8471261 ouruser/sinatra:devel         |
| 把本地 image 上传到 registry 中 | docker push [imageName]                      | docker push ouruser/sinatra                           |
| 删除本地 image                  | docker rmi [image]                           | docker rmi training/sinatra                           |
| 删除所有image                   | docker rm $(docker ps -a -q)                 | docker rm $(docker ps -a -q)                          |
| 载入镜像                        | docker load < ubuntu_14.04.tar               | docker load < ubuntu_14.04.tar                        |
| 存出镜像                        | docker save -o ubuntu_14.04.tar ubuntu:14.04 | docker save -o ubuntu_14.04.tar ubuntu:14.04          |

docker-machine：

| 操作                                              | 命令                     | 示例                                              |
| ------------------------------------------------- | ------------------------ | ------------------------------------------------- |
| docker-machine ssh  machineName                   | 会进入docker服务器的目录 | docker-machine ssh default                        |
| docker-machine ls                                 | 列出docker服务器         | docker-machine ls                                 |
| docker-machine ip default                         | 查看服务器               | docker-machine ip default                         |
| docker-machine rm default                         | 删除docker服务器         | docker-machine rm default                         |
| docker-machine start default                      | 启动docker服务器         | docker-machine start default                      |
| docker-machine status                             | 查看虚拟机状态           | docker-machine status                             |
| docker-machine restart default                    | 重启虚拟机               | docker-machine restart default                    |
| docker-machine stop                               | 停止虚拟机               | docker-machine stop                               |
| docker-machine create --driver virtualbox default | 创建docker服务器         | docker-machine create --driver virtualbox default |
| docker-machine env default                        | 提示设置环境变量         | docker-machine env default                        |
| docker-machine -v                                 | 查看版本信息             | docker-machine -v                                 |
| docker-machine inspect                            | 检查机子信息             | docker-machine inspect                            |
| docker-machine ssh default                        | 连接到default的docker    | docker-machine ssh dfault                         |

docker load与docker import

>首先，想要清楚的了解docker load与docker import命令的区别，就必须了解镜像与容器的区别：
>
>镜像：用来启动容器的只读模板，是容器启动所需的rootfs，类似于虚拟机所使用的镜像。
>
>容器：Docker 容器是一个开源的应用容器引擎，让开发者可以打包他们的应用以及依赖包到一个可移植的容器中，然后发布到任何流行的Linux机器上，也可以实现虚拟化。
>
>镜像是容器的基础，可以简单的理解为镜像是我们启动虚拟机时需要的镜像，容器时虚拟机成功启动后，运行的服务。  
>
>想要了解docker load与docker import命令的区别，还必须知道docker save与docker export命令：
>
>docker save images_name：将一个镜像导出为文件，再使用docker load命令将文件导入为一个镜像，会保存该镜像的的所有历史记录。比docker export命令导出的文件大，很好理解，因为会保存镜像的所有历史记录。
>
>docker export container_id：将一个容器导出为文件，再使用docker import命令将容器导入成为一个新的镜像，但是相比docker save命令，容器文件会丢失所有元数据和历史记录，仅保存容器当时的状态，相当于虚拟机快照。

### Window安装常见问题

> 1：启动时：
>
> ![1529994843168](C:\Users\789\AppData\Local\Temp\1529994843168.png)
>
> 查看Virtualbox中虚拟机的位数和操作系统位数是否一致。如果不一致，则重启电脑进入bios设置virtualization设置为enable。第二部：删除虚拟机，重启docker会重新创建default的虚拟机第三部：检查上面自动创建的虚拟机是否有端口转发，是否有Host-only网络

> 2：在virtual box中，无法开启虚拟机。报错为VT-x is disabled in the BIOS for all CPU modes (VERR_VMX_MSR_ALL_VMX_DISABLED).解决方法是在虚拟机的系统配置中，将RAM大小变小。我选择的是2G。这个问题也是google一些网页查找出来解决的，<http://stackoverflow.com/questions/34746462/error-vt-x-is-disabled-in-the-bios-for-all-cpu-modes-verr-vmx-msr-all-vmx-disa>。

> 3：第一个问题解决后，就冒出了第二问题。也是这次主要解决的问题。显示如下 "this kernel requires an x86-64 cpu but only detected an i686 cpu"。这里是说需要的虚拟机为64位，通常，在setting,system下可以选择虚拟机的操作系统，但是我的电脑，只显示有32bit的操作系统。解决这个问题的核心办法，是在自己的电脑中，进入到BIOS中，然后将virtualization设置为enable。我的电脑，进入BIOS的方法是开机有thinkpad显示时，点击F1(不断点击)，则可以进入BIOS界面。对于不同的电脑，可以查阅对应的办法。（Security-Virtualization）。

>4: shim error: docker-runc not installed on system
>
>服务器重启以后，执行docker命令报以上错误，解决办法如下：cd /usr/libexec/docker/sudo ln -s docker-runc-current docker-runc



>cmd命令：
>
>可以在~/.bashrc中设置alias dm='docker-machine'简化输入
>
>C:\Users\789>docker-machine env default
>SET DOCKER_TLS_VERIFY=1
>SET DOCKER_HOST=tcp://192.168.48.128:2376
>SET DOCKER_CERT_PATH=C:\Users\789\.docker\machine\machines\wu02
>SET DOCKER_MACHINE_NAME=wu02
>REM Run this command to configure your shell:
>REM     FOR /f "tokens=*" %i IN ('docker-machine env wu02') DO %i
>
>复制上面的SET指令或者eval "$(docker-machine env default)" 就可以了。
>
>最终需要执行FOR /F "tokens=*" %i IN ('docker-machine env ron-docker') DO %i 完成设置。
>
>windows的cmd命令或者浏览器输入docker version最下方出现：
>
>An error occurred trying to connect: Get http://127.0.0.1:2375/v1.22/version: dial tcp 127.0.0.1:2375: connectex: No connection could be made because the target machine actively refused it
>
>如果出现上述问题，则重新设置环境变量。
>
>
>
>Post http://127.0.0.1:2375/v1.20/containers/create: dial tcp 127.0.0.1:2375: ConnectEx tcp: No connection could be made because the target machine actively refused it..
>
> * Are you trying to connect to a TLS-enabled daemon without TLS?
>
> * Is your docker daemon up and running?
>
>   执行
>
>   docker-machine regenerate-certs default
>
>   docker-machine restart default
>
>   设置上面环境变量



```
set DOCKER_HOST=tcp://127.0.0.1:2375
set DOCKER_CERT_PATH=C:\Users\${user}\.boot2docker\certs\boot2docker-vm
set DOCKER_TLS_VERIFY=1
```



###  参考网址

kubernetes: https://github.com/GoogleCloudPlatform/kubernetes/

fig: https://github.com/docker/fig

Openstack



http://www.dockerinfo.net/image%E9%95%9C%E5%83%8F   （官方文档）

https://www.cnblogs.com/51kata/p/5262301.html   ok

https://www.cnblogs.com/sparkdev/p/7066789.html  ok

https://spring.io/guides/gs/spring-boot-docker/   docker-maven官网

https://www.jianshu.com/p/c435ea4c0cc0   （dockerfile引用pom动态参数@@）

https://www.jianshu.com/p/3b91b8958c3e

https://www.jb51.net/article/138187.htm   (springboot-docker-maven)



http://www.ituring.com.cn/article/497750  （docker-maven）

https://docs.docker.com/config/daemon/systemd/#httphttps-proxy (官方)

docker学习

https://www.cnblogs.com/kevingrace/p/6238195.html （日常操作）

### 构建

#### Dockerfile

Dockerfile`注意Dockerfile的D需要大写 `是一个文本格式的配置文件，用户可以使用Dockerfile快速创建自定义的镜像。

首先，在项目的根目录下，新建一个文本文件`.dockerignore`，写入下面的内容

```
.git
node_modules
npm-debug.log
```

上面代码表示，这三个路径要排除，不要打包进入 image 文件。如果你没有路径要排除，这个文件可以不新建。



#### 基本结构

一般来说，Dockerfile分为四部分：

- 基础镜像信息
- 维护者信息
- 镜像操作指令
- 容器启动时执行的指令

#### 指令

指令一般格式为`INSTRUCTION arguments`。

##### FROM

格式为`FROM <image>`。第一条指令必须为`FROM`指令，指定了基础镜像。

```
FROM ubuntu:latest
```

##### MAINTAINER

格式为`MAINTAINER <name>`指定维护者信息。

```
MAINTAINER mylxsw mylxsw@aicode.cc
```

##### RUN

格式为`RUN <command>`或者`RUN ["executable", "param1", "param2"...]`。每条指令将在当前镜像的基础上执行，并提交为新的镜像。

格式`RUN <command>`时将在shell终端中执行命令(直接在命令行中输入的命令一样)，也就是`/bin/sh   -c`中执行，而`RUN ["可执行文件", "param1", "param2"...]`则使用`exec`执行。指定使用其它终端可以通过第二种方式实现，例如 `RUN ["/bin/bash", "-c", "echo hello"]`

每条 `RUN` 指令将在当前镜像基础上执行指定命令，并提交为新的镜像。当命令较长时可以使用 `\` 来换行

```
RUN echo '<h1>Hello,Decoker!</h1>' > /usr/share/nginx/html/index.html
```

##### CMD

该命令提供容器启动时执行的命令，`每个Dockerfile中只能与一条CMD命令，如果指定了多条，则只有最后一条会被执行`。如果用户启动容器的时候指定了运行的命令，则会覆盖CMD指令。

格式支持三种：

- `CMD ["executable", "param1", "param2"] `使用exec执行
- `CMD command param1 param2` 使用`/bin/sh -c`执行
- `CMD ["param1", "param2"]` 提供给ENTRYPOINT的默认参数

我们知道，通过docker run 创建并启动一个容器时，命令的最后可以指定容器启动后在容器内立即要执行的指令，如：

docker run -i -t ubunu /bin/bash   //表示容器启动时立即在容器内打开一个shell终端

docker run ubuntu /bin/ps    //表示容器启动后立即运行 /bin/ps命令，显示容器的当前进程。

除了这种方式外，我们可以在dockerfile文件中通过CMD指令指定容器启动时要执行的命令。如：

```
#test
FROM ubuntu
MAINTAINER xxx
RUN echo hello1 > test1.txt
RUN echo hello2 > /test2.txt
EXPOSE 80
EXPOSE 81
CMD ["/bin/bash"]
```

上面dockerfile文件中最后一行CMD指令的参数是指定容器启动时要执行的命令，这里是bin/bash命令。

1、用docker run命令创建并启动容器（myimage  是用前面dockerfile创建的镜像的名称）：

docker run -i -t myimage  

上面命令是创建并启动容器，打开一个交互式shell。 而以前的写法是

docker run -i -t myimage  /bin/bash

这样就省去了在docker run中写命令了。

2、即使dockerfile中有CMD指令，我们仍然可以在docker run命令中带上容器启动时执行的命令，这会覆盖dockerfile中的CMD指令指定的命令。如：

docker run -i -t myimage /bin/ps

上面命令，因为/bin/ps覆盖了CMD指令，启动容器时会打印容器内的当前进程，但容器会立即停止，因为/bin/bash被覆盖了，无法打开交互式shell界面。

3、需要注意的是，dockerfile中可以有多条cmd命令，但只是最后一条有效。

4、CMD命令的参数格式，一般写成 字符串数组的方式，如上面的例子。如：

CMD  ["echo","hello world"]

虽然也可写成CMD echo hello word 方式，但这样docker会在指定的命令前加  /bin/sh -c 执行，有时有可能会出问题。 所以推荐采用数据结构的方式来存放命令

##### EXPOSE

格式为`EXPOSE <port> [<port>...]`，该指令用于告诉Docker容器要暴露的端口号，供互联系统使用。

```
EXPOSE 22 80 8443
```

上述指令暴露了22, 80, 8443端口供互联的系统使用，使用的时候可以指定`-P`或者`-p`参数进行端口映射。

##### ENV

格式为`ENV <key> <value>`。指定一个环境变量，会被后续的RUN指令使用，并且在容器运行时保持。

比如：

```
ENV PG_MAJOR 9.3
ENV PG_VERSION 9.35
```

ENV指令用来在镜像构建过程中设置环境变量。我们来看一个Dockerfile的例子：

[![复制代码](http://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
#test
FROM ubuntu
MAINTAINER hello
ENV MYDIR /mydir
RUN mkdir $MYDIR
RUN echo hello world > $MYDIR/test.txt
```

[![复制代码](http://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

假设用上面的dockerfile构建了一个叫myimage的镜像。
运行  docker run -i -t myimage /bin/bash

我们发现新建的容器中有了 /mydir目录，并有了/mydir/test.txt文件，文件内容为 hello world

1、通过ENV定义的环境变量，可以被后面的所有指令中使用，如上面的例子

2、但是不能被CMD指令使用，也不能被docker run 的命令参数引用。这个需要注意

3、通过ENV定义的环境变量，会永久的保存到该镜像创建的任何容器中。这样除了不能在上面说的启动命令中使用外，可以在后续容器的操作中使用。

4、可以在docker run 命令中通过 -e标记来传递环境变量，这样容器运行时就可以使用该变量。如：

docker run -i -t -e "TEST=hello" ubuntu /bin/bash

##### ADD

格式为`ADD <src> <dest>`。该命令复制指定的`<src>`到`<dest>`，其中`<src`可以是Dockerfile所在目录的一个相对路径（文件或目录），也可以是网络上的资源路径或者是tar包。如果`<src>`是tar包的话，会在dest位置**自动解压**为目录。

有如下注意事项：

1、如果源路径是个文件，且目标路径是以 / 结尾， 则docker会把目标路径当作一个目录，会把源文件拷贝到该目录下。

如果目标路径不存在，则会自动创建目标路径。

2、如果源路径是个文件，且目标路径是不是以 / 结尾，则docker会把目标路径当作一个文件。

如果目标路径不存在，会以目标路径为名创建一个文件，内容同源文件；

如果目标文件是个存在的文件，会用源文件覆盖它，当然只是内容覆盖，文件名还是目标文件名。

如果目标文件实际是个存在的目录，则会源文件拷贝到该目录下。 注意，这种情况下，最好显示的以 / 结尾，以避免混淆。

3、如果源路径是个目录，且目标路径不存在，则docker会自动以目标路径创建一个目录，把源路径目录下的文件拷贝进来。

如果目标路径是个已经存在的目录，则docker会把源路径目录下的文件拷贝到该目录下。

4、如果源文件是个归档文件（压缩文件），则docker会自动帮解压。

##### COPY

格式为`COPY <scr> <dest>`，复制本地主机的`<src>`（为 Dockerfile 所在目录的相对路径）到容器的`<dest>`，目标路径不存在则自动创建。使用本地目录为源目录时，推荐使用COPY。

```
注意，ADD命令和COPY命令基本上是一样的，只不过是ADD命令可以复制网络资源，同时会对压缩包进行自动解压，而COPY则是单纯的复制本地文件（目录）。

如果在Dockerfile中这样写：

COPY ./package.json /app/

这并不是要复制 执行docker build命令所在目录下的package.json，也不是复制Dockerfile所在目录下的package.json，而是复制上下文目录下的package.json
```

将主机/www/runoob目录拷贝到容器96f7f14e99ab的/www目录下。

```
docker cp /www/runoob 96f7f14e99ab:/www/
```

将主机/www/runoob目录拷贝到容器96f7f14e99ab中，目录重命名为www。

```
docker cp /www/runoob 96f7f14e99ab:/www
```

将容器96f7f14e99ab的/www目录拷贝到主机的/tmp目录中。

```
docker cp  96f7f14e99ab:/www /tmp/
```

##### ENTRYPOINT

配置容器启动后执行的命令，并且`不会`被`docker run`提供的参数覆盖。每个Dockerfile中只能有一个ENTRYPOINT，当指定多个的时候，只有最后一个生效。

格式有两种：

- ENTRYPOINT ["executable", "param1", "param2"]
- ENTRYPOINT command param1 param2

本文介绍Dockerfile的 ENTRYPOINT指令的含义。

先回顾下CMD指令的含义，CMD指令可以指定容器启动时要执行的命令，但它可以被docker run命令的参数覆盖掉。

ENTRYPOINT 指令和CMD类似，它也可用户指定容器启动时要执行的命令，但如果dockerfile中也有CMD指令，CMD中的参数会被附加到ENTRYPOINT 指令的后面。 如果这时docker run命令带了参数，这个参数会覆盖掉CMD指令的参数，并也会附加到ENTRYPOINT 指令的后面。

这样当容器启动后，会执行ENTRYPOINT 指令的参数部分。

可以看出，相对来说ENTRYPOINT指令优先级更高。

我们来看个例子，下面是Dockerfile的内容

```
#test
FROM ubuntu
MAINTAINER hello
RUN echo hello1 > test1.txt
RUN echo hello2 > /test2.txt
EXPOSE 80
ENTRYPOINT ["echo"]
CMD ["defaultvalue"]
```

假设通过该Dockerfile构建的镜像名为 myimage。

1、当运行 docker run myimage  输出的内容是 defaultvalue，可以看出CMD指令的参数得确是被添加到ENTRYPOINT指令的后面，然后被执行。

2、当运行docker run myimage hello world  输出的内容是 hello world ，可以看出docker run命令的参数得确是被添加到ENTRYPOINT指令的后面，然后被执行，这时CMD指令被覆盖了。

3、另外我们可以在docker run命令中通过 --entrypoint 覆盖dockerfile文件中的ENTRYPOINT设置，如：

docker run --entrypoint="echo" myimage good  结果输出good

 注意，不管是哪种方式，创建容器后，通过 dokcer ps查看容器信息时，COMMOND列会显示最终生效的启动命令。

##### VOLUME

格式为`VOLUME ["/data"]`，创建一个可以从本地主机或其它容器挂载的挂载点，一般用来存放数据库和需要保持的数据等。

##### USER

格式为`USER daemon`，用于指定运行容器时的用户名或者UID，后续的RUN命令也会使用指定的用户。

##### WORKDIR

格式为`WORKDIR /path/to/workdir`，用于为后续的RUN，CMD，ENTRYPOINT指令配置工作目录。

可以多次使用，如果后续指定的路径是相对路径，则会基于前面的路径。

```
WORKDIR /a
WORKDIR b
RUN pwd
```

则最后得到的路径是`/a/b`。

Dockerfile中的WORKDIR指令用于指定容器的一个目录， 容器启动时执行的命令会在该目录下执行。

相当于设置容器的工作目录了。我们来看一个dockerfile文件

[![复制代码](http://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
#test
FROM ubuntu
MAINTAINER hello
RUN mkdir /mydir
RUN echo hello world > /mydir/test.txt
WORKDIR /mydir
CMD ["more" ,"test.txt"]
```

[![复制代码](http://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

假设根据该dockerfile构建的镜像名为 myimage

1、运行 docker run myimage  输出 hello world

2、运行 docker run myimage more test.txt  和上面输出一致

可以看出，more的参数是 test.txt，但没有指定路径，却能成功，说明当前路径就是上面WORKDIR指令设置的。

如果我们在上面的dockerfile中把WORKDIR指令去掉，创建的容器运行会报文件不存在错误。

3、可以在 docker run命令中用 -w参数覆盖掉WORKDIR指令的设置，如：

执行 docker run -w / myimage

上面的-w参数将容器的工作目录设置成了根目录，而根目录下没有test.txt文件。

所以结果显示：test.txt: No such file or directory

##### ONBUILD

指定基于该镜像创建新的镜像时自动执行的命令。格式为`ONBUILD [INSTRUCTION]`。



#### 直接用Git Repo构建

docker build支持直接从URL构建

```
docker build http://github.com/twang2218/gitlab-ce-zh.git#:8.14
```

用给定tar压缩包构建

```
docker build http://server/context.tar.gz
```

从标准输入中读取上下文压缩包进行构建

```
docker build - < context.tar.gz
```

### 创建镜像

基本的格式为 `docker build [选项] 路径`，该命令将读取指定路径下（包括子目录）的 Dockerfile，并将该路径下所有内容发送给 Docker 服务端，由服务端来创建镜像。因此一般建议放置 Dockerfile 的目录为空目录。也可以通过 `.dockerignore` 文件（每一行添加一条匹配模式）来让 Docker 忽略路径下的目录和文件。

编写完Dockerfile之后，就可以通过`docker build`命令构建一个镜像了。

> 可以通过`.dockerignore`指定忽略的文件和目录，类似于git中的`.gitignore`文件。

比如

```
docker build -t build_repo/first_image /tmp/docker_builder
```

> 书写dockerfile，该dockerfile用到了本机的镜像A（From A），利用该dockerfile build 镜像B，那么，镜像B被称为镜像A的child

一般来说，应该将Dockerfile置于一个空目录下，或者项目的根目录下。如果该目录下没有所需要的文件，则需要把所需要的文件复制一份过来。如果目录下确实有东西不希望构建时传给docker引擎，则需要用.dockerignore剔除，剔除的文件不会传递给docker引擎。

默认情况下，如果不额外指定Dockerfile的话，会将上下文的名字为Dockerfile的文件作为Dockerfile。

这只是默认行为，实际上Dockerfile的文件名并不需要必须为Dockerfile，并且并不要求必须位于上下文目录中，比如可以使用 -f ../Dockerfile.php参数指定某个文件为Dockerfile。

当然，一般大家习惯性的会使用默认的文件名Dockerfile，以及将其置于构建镜像的上下文目录中。

使用当前目录的Dockerfile创建镜像。

```
docker build -t runoob/ubuntu:v1 . 
```

使用URL **github.com/creack/docker-firefox** 的 Dockerfile 创建镜像。

```
docker build github.com/creack/docker-firefox
```

```
docker build -t centos:base -f /soft/docker/Dockerfile /soft
```

>通过-f来指定Dockerfile文件的位置，后面的/soft及其目录下必须能够找到Dockerfile文件否则就会报上下文环境的错误,MV,COPY,ADD的文件位置都是相对/soft来说的。

 docker build会`递归`查找目录下的所有Dockerfile文件

事实证明上面的论证是不正确的：

![1530078683158](C:\Users\789\AppData\Local\Temp\1530078683158.png)

### 实战

#### 案例一

这第一个镜像自然是简单又实用，以官方ubuntu 14.04为基础，更改默认的软件源。我将其命名为ali.ubuntu，日后使用这个镜像，执行apt-get相关命令的时候，可以节省不少时间。 

建立项目文件夹：

```
$ mkdir ali.ubuntu
```

创建项目文件

```
$ cd ali.ubuntu
$ touch Dockerfile
$ touch sources.list
```

在项目目录中，有两个文件：

```
$ pwd
/Users/adam/workspace/ali.ubuntu
$ ls
Dockerfile  sources.list
```

将阿里源内容添加到sources.list 
下面这个源的内容，适用于ubuntu 14.04.

```
deb http://mirrors.aliyun.com/ubuntu/ trusty main multiverse restricted universe
deb http://mirrors.aliyun.com/ubuntu/ trusty-backports main multiverse restricted universe
deb http://mirrors.aliyun.com/ubuntu/ trusty-proposed main multiverse restricted universe
deb http://mirrors.aliyun.com/ubuntu/ trusty-security main multiverse restricted universe
deb http://mirrors.aliyun.com/ubuntu/ trusty-updates main multiverse restricted universe
deb-src http://mirrors.aliyun.com/ubuntu/ trusty main multiverse restricted universe
deb-src http://mirrors.aliyun.com/ubuntu/ trusty-backports main multiverse restricted universe
deb-src http://mirrors.aliyun.com/ubuntu/ trusty-proposed main multiverse restricted universe
deb-src http://mirrors.aliyun.com/ubuntu/ trusty-security main multiverse restricted universe
deb-src http://mirrors.aliyun.com/ubuntu/ trusty-updates main multiverse restricted universe
```

编译Dockerfile

1. 首先，使用FROM指令，来指定基础镜像。
2. 使用MAINTAINER说明该镜像是制作维护人，和邮箱
3. 将sources.list添加到镜像中的/etc/apt/目录下
4. 为了验证是否成功，在镜像中，执行一次apt-get update命令。

```
FROM ubuntu:14.04
MAINTAINER Abbott "4754023662qq.com"
ADD sources.list /etc/apt/
RUN apt-get update
```

至此，一切准备工作都做完成，是时候让Docker执行我们的命令了：

```
$ pwd
/Users/adam/workspace/ali.ubuntu
$docker build  -t abbott/abbott.ubuntu ./
```

docker build 命令，-t参数指定镜像名称，后面指定项目目录，下面看看执行的结果： 

```
$ docker build  -t abbott/abbott.ubuntu ./
Sending build context to Docker daemon 3.584 kB
Step 1 : FROM ubuntu:14.04
 ---> 1e0c3dd64ccd
Step 2 : MAINTAINER Adam Gao “solonot@163.com”
 ---> Running in fb678c1c7680
 ---> 71d73db293d4
Removing intermediate container fb678c1c7680
Step 3 : ADD sources.list /etc/apt/
 ---> 411de53b11b4
Removing intermediate container 3b22cc5fdebd
Step 4 : RUN apt-get update
 ---> Running in bf63399b3802
Ign http://mirrors.aliyun.com trusty InRelease
Get:1 http://mirrors.aliyun.com trusty-backports InRelease [65.9 kB]
Get:2 http://mirrors.aliyun.com trusty-proposed InRelease [65.9 kB]
Get:3 http://mirrors.aliyun.com trusty-security InRelease [65.9 kB]
Get:4 http://mirrors.aliyun.com trusty-updates InRelease [65.9 kB]
Get:5 http://mirrors.aliyun.com trusty Release.gpg [933 B]
Get:6 http://mirrors.aliyun.com trusty Release [58.5 kB]
Get:7 http://mirrors.aliyun.com trusty-backports/main Sources [10.3 kB]
Get:8 http://mirrors.aliyun.com trusty-backports/multiverse Sources [1751 B]
Get:9 http://mirrors.aliyun.com trusty-backports/restricted Sources [40 B]
...些处省略太多的log输出...
Fetched 23.2 MB in 27s (841 kB/s)
Reading package lists...
 ---> 4f1dec752b62
Removing intermediate container bf63399b3802
Successfully built 4f1dec752b62
```

我们的Dockerfile中一共有四行指令，在docker build的时候，就分成了4步。看到输出的内容，每一步都在上一步的基础上，创建了一个临时的容器用来执行命令，并且在步骤结束的时候，删除该容器。最后一行，Successfully build 4f1dec752b62说明成功生成镜像，它的ID是4f1dec752b62. 

可以在`docker build`命令中使用`-f`标志指向文件系统中任何位置的Dockerfile。 

```
$ docker build -f /path/to/a/Dockerfile
```

让我们用docker images这个命令来查看一下 

```
$ docker images
REPOSITORY          TAG                 IMAGE ID            CREATED             SIZE
kyugao/ali.ubuntu   latest              4f1dec752b62        9 hours ago         211.2 MB
```

#### 案例二

有了制作简单镜像的基础之后，今天准备在上一个Docker image基础上，加入mongodb服务。

首先看一下，现在本地有哪些镜像：

```
$ docker images
REPOSITORY          TAG                 IMAGE ID            CREATED             SIZE
kyugao/ali.ubuntu   latest              4f1dec752b62        2 days ago          211.2 MB
ubuntu              14.04               1e0c3dd64ccd        10 days ago         187.9 MB
```

开始今天的工作：

- 创建工作目录：mongodb
- 生成Dockerfile文件

```
$ mkdir mongodb
$ touch Dockerfile
```

准备mongodb的发行包，与Dockerfile放在同级目录下[下载地址](https://fastdl.mongodb.org/linux/mongodb-linux-x86_64-ubuntu1404-3.2.10.tgz)。该版本是14.04的，通过curl还是讯雷，主要看速度。

- 打开Dockerfile编写头两句:

```
FROM kyugao/ali.ubuntu
MAINTAINER Adam Gao "solonot@163.com"
```

– 第一个新命令：WORKDIR  比如，将这一次的工作目录设定为：/root/workdir 

切换目录用，可以多次切换(相当于cd命令)，对RUN,CMD,ENTRYPOINT生效

```
WORKDIR /root/workdir
```

作用一：若不存在，生成该目录  作用二：进入这个目录，并执行接下来的指令，比如通过ADD命令，将mongodb的包，复制到工作目录中。 

```
ADD mongodb-linux-x86_64-ubuntu1404-3.2.10.tar ./
```

 ADD将文件<src>拷贝到container的文件系统对应的路径<dest>  

ADD只有在build镜像的时候运行一次，后面运行container的时候不会再重新加载了。

- 如果要ADD本地文件，则本地文件必须在 docker build <PATH>，指定的<PATH>目录下
- 如果要ADD远程文件，则远程文件必须在 docker build <PATH>，指定的<PATH>目录下。比如:

```
docker build github.com/creack/docker-firefox
```

docker build github.com/creack/docker-firefox

这里将介绍ADD命令的一个新的作用，就是：自动解压。也就是说，最终这个压缩包会被解压到/root/workdir目录下。所以，在镜像里看是下面这样的 

```
$ pwd
/root/workdir/mongodb-linux-x86_64-ubuntu1404-3.2.10
```

– 第二个新命令：ENV  设置环境变量，在这里就是想将mongodb的命令加到path中： 

```
ENV PATH $PATH:/root/workdir/mongodb-linux-x86_64-ubuntu1404-3.2.10/bin
```

和linux中设置有点不一样，中间没有”=”号。这样，在镜像中的任何地方，都可以执行mongod这个命令来启动服务了。

– 第三个新命令：EXPOSE 
开放端口，容器是一个相对封闭的环境，正常运行的程序和服务，在宿主机中是无法访问的，所以我要开放mongodb服务的端口：

```
EXPOSE 27017
```

– 第三个新命令：ENTRYPOINT  ENTRYPOINT这个命令是启动容易的时候，会运行哪个命令，或程序。  我们指定mongod，这是我们想启动的服务，而指定dbpath为数据存储目录。 

```
RUN mkdir dbpath
ENTRYPOINT ["mongod", "--dbpath=./dbpath"]
```

完整的Dockerfile 

```
FROM kyugao/ali.ubuntu
MAINTAINER Adam Gao "solonot@163.com"
WORKDIR /root/workdir
ADD mongodb-linux-x86_64-ubuntu1404-3.2.10.tar ./
ENV PATH $PATH:/root/workdir/mongodb-linux-x86_64-ubuntu1404-3.2.10/bin
EXPOSE 27017
RUN mkdir dbpath
ENTRYPOINT ["mongod", "--dbpath=./dbpath"]
```

使用docker build生成镜像：

```
$ docker build -t kyugao/mongodb ./
```

下面的指令：

```
$ docker run -d -p 27017:27017 kyugao/mongodb
dfe43f4c7de3af1ccf8445d4c535ed212c2935abd0212a098b5f674483723c36
$ docker run -d -p 127.0.0.1:27016:27017 kyugao/mongodb
2465c7597164c507bdf3feb372ec15554ff25015d534ae15100a1425a69f5b4b
```

-d 参数，表示命令这个容器是一个后台服务。  -p 参数，表示面后要在宿主机和docker容器之间建立一个端口映射，当映射端口时指定了ip地址，如上面第二个docker run指令，便只能在本机访问了。  那么，如果查看这个后台的服务呢？ 

```
$ $ docker ps
CONTAINER ID        IMAGE               COMMAND                  CREATED             STATUS              PORTS                        NAMES
2465c7597164        kyugao/mongodb      "mongod --dbpath=./db"   3 minutes ago       Up 3 minutes        127.0.0.1:27016->27017/tcp   berserk_golick
dfe43f4c7de3        kyugao/mongodb      "mongod --dbpath=./db"   7 minutes ago       Up 7 minutes        0.0.0.0:27017->27017/tcp     dreamy_franklin
```

#### 案例三

公司要把j2ee的project搬到 docker里，所以，先从一个tomcat的镜像开始吧！

```
# docker pull tomcat
```

因为是在阿里云的ecs上，使用了阿里云的docker库，因此下载速度飞快！

查看本地已下载的镜像：

```
# docker images
REPOSITORY          TAG                 IMAGE ID            CREATED             VIRTUAL SIZE
ubuntu              latest              8251da35e7a7        12 days ago         188.4 MB
tomcat              latest              71093fb71661        5 weeks ago         347.7 MB
```

基于这个image，创建一个容器吧：

```
# docker create --name dev_tomcat -p 8080:8080 tomcat
```

 --name 给这个容器起一个名字
 -p host到container的端口映射

打一个比方说，一个image就相当于一个系统光盘，容器，就是一部安装了这个系统电脑。启动：

```
# docker start dev_tomcat
# docker ps
CONTAINER ID        IMAGE               COMMAND             CREATED              STATUS              PORTS                    NAMES
94e167c8b2b8        tomcat:latest       "catalina.sh run"   About a minute ago   Up About a minute   0.0.0.0:8080->8080/tcp   dev_tomcat
```

通过docker ps命令，可以看到现在这个容器的运行情况。不过既然这是启动一个tomcat的容器，如何能看到tomcat的启动情况呢： 

```
# docker logs dev_tomcat
```

看到这些log， 

1. 可以确定tomcat启动成功 
2. Tomcat自带应用已经部署成功：manager, doc, examples, root, host-manager. 
3. tomcat目录/usr/local/tomcat 

4. webapp目录/usr/local/tomcat/webapps 

来访问一下：[http://ipaddress:8080](http://ipaddress:8080/)，正常情况下，应该可能看到熟悉的tomcat的经典界面了。

上面提到，container相当于一个安装了image这个系统的电脑，那没理由不可以进去看看的吧！那就进去吧：

```
# docker exec -t -i dev_tomcat /bin/bash
//docker exec意思是：在dev_tomcat下面运行一个命令，在这里，运行的是/bin/bash
//-t 表示分配一个pseudo-TTY，-i 表示可交互
//运行之后，提示符就变成了，tomcat这个image的默认工作目录是/usr/local/tomcat，自动打开：
root@94e167c8b2b8:/usr/local/tomcat#
root@94e167c8b2b8:/usr/local/tomcat# cd webapps/
//进入webapps里面，看看是不是几个默认的应用都在里面
root@94e167c8b2b8:/usr/local/tomcat/webapps# ls
ROOT  docs  examples  host-manager  manager
```

#### 案例四

Docker与MySql

根据上一篇blog的经验，来创建一个mysql的容器：

```
# docker pull mysql
# docker create --name dev_mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=123456 mysql
// 这第二个command多了一个指定环境变量的，-e，设定root帐号的初始密码
# docker start dev_mysql
```

不出意外，现在已经可以通过mysql客户端，比如workbench来登录了。

 Docker的数据卷

 在测试阶段，我的项目，每一天，甚至每一次小的修改，都会有新的war包要部署，而我又不想每次都打包一个镜像，这样我就要将webapps的目录，也就是/usr/local/tomcat/webapps暴露出来，方便在host中，随时更新里面的war包。 

```
# docker create --name dev_mila_tomcat -v /root/test/webapps:/usr/local/tomcat/webapps tomcat
```

上面的命令，可以将镜像中的/usr/local/tomcat/webapps目录映射到host的/root/test/webapps目录下，这样，我每次只需要将war包放到/root/test/webapps中，docker中的tomcat就能自动检测到了！

运行环境的统一，有了这么一个docker image，我可以拿到任意一部服务器上跑。由于容器的封装，保证了运行环境的统一！

既然可以暴露webapps目录了，那不仿再多暴露几个，现在我的test项目会保留一些图片和音乐文件，所以我想把这两个目录暴露出来，以便在host上面，可以方便的做备份

```
# docker create --name dev_mila_tomcat \ 
    -v /root/test/webapps:/usr/local/tomcat/webapps \
    -v /root/test/data/image:/testdata/image \
    -v /root/test/data/audio:/testdata/audio \
    tomcat
```

好了，这样我可以在host上面，直接跑一个tar打包备份文件咯！  

#### 案例五

在这里，创建一个Java应用程序并使用`docker`进行运行。此示例分以下几个步骤完成。

创建一个目录

目录是组织文件所必需的，所以首先使用以下命令创建目录一个目录。

```
$ mkdir -p /home/yiibai/docker/java-docker-app
```

创建一个Java文件

现在创建一个Java文件，将此文件保存为`Hello.java`。这个 `Hello.java` 的代码内容如下 - 

```
class Hello{  
    public static void main(String[] args){  
        System.out.println("This is first java app \n by using Docker");  
    }  
}
```

将其保存在文件`Hello.java`中，并放置在 `/home/yiibai/docker/java-docker-app` 目录下。 

创建一个Dockerfile文件

创建Java文件后，还需要创建一个Dockerfile文件，其中包含了Docker的说明。 Dockerfile不包含任何文件扩展名。 所以这个文件简单使用`Dockerfile`作为名称保存即可。此 *Dockerfile* 文件的内容如下 - 

```
FROM java:8
COPY . /var/www/java  
WORKDIR /var/www/java  
RUN javac Hello.java  
CMD ["java", "Hello"]
```

所有指令要使用大写字母编写，因为这是它的惯例(约定)。将此文件放在`/home/yiibai/docker/java-docker-app`目录中。 现在将`Dockerfile`也放置在在`/home/yiibai/docker/java-docker-app`目录中，与`Hello.java`放在同一个目录。

如下 -

```
wukang@wukang-virtual-machine:/usr/local/java-docker-app$ ls
Dockerfile  Hello.java
wukang@wukang-virtual-machine:/usr/local/java-docker-app$
```

构建Docker映像

创建Dockerfile后，需要更改工作目录。 

```
$ cd  /home/yiibai/docker/java-docker-app
```

现在，按照以下命令创建一个映像。这里必须以root身份登录才能创建映像。 在这个例子中，我们已经切换为root用户的身份。 在以下命令中，`java-app`是的映像名称。当然Docker的映像名称可以是任意的。 

```
sudo docker build -t java-app .
```

如果未安装*Java 8*，那么会自动下载*Java 8*安装再执行。在成功构建映像后。 现在，我们可以运行Docker映像了 

运行Docker映像

成功创建映像后 现在可以使用`run`命令运行docker。以下命令用于运行`java-app` 

```
yiibai@ubuntu:~/docker/java-docker-app$ sudo docker run java-app
```

上面命令的运行输出结果如下 -

```
yiibai@ubuntu:~/docker/java-docker-app$ sudo docker run java-app
This is first java app
by using Docker
yiibai@ubuntu:~/docker/java-docker-app$
```

在这里可以看到，在运行`sudo docker run java-app`之后，它产生了一个输出。

在经过上**5**个步骤之后，您应该已经可在系统上成功运行docker映像了。除了所有这些以外，还可以在接下来的文章中学习和使用其他命令。

#### 安装Nginx

首先，下载nginx镜像

```
docker pull nginx
```

启动nginx

```
docker run --name nginx -d -p 80:80 nginx
```

创建Nginx配置文件目录

```
mkdir /opt/nginx/html
```

启动nginx容器后，将nginx.conf配置文件复制到nginx配置目录下/opt/nginx/，将default.conf配置文件复制到/opt/nginx/conf.d/目录下

```
docker cp nginx:/etc/nginx/nginx.conf /opt/nginx/
docker cp nginx:/etc/nginx/conf.d/ /opt/nginx/conf.d/
```

停止并删除nginx容器

```
#停止nginx容器
docker stop nginx
#删除nginx容器
docker rm nginx
#查看nginx容器是否删除
docker ps -a
```

重新启动Nginx容器

```
docker run \
-d --name nginx -p 80:80 \
-v /opt/nginx/html/:/usr/share/nginx/html \
-v /opt/nginx/nginx.conf:/etc/nginx/nginx.conf \
-v /opt/nginx/conf.d/:/etc/nginx/conf.d \
nginx
```

根据default.conf配置文件中的访问配置，需要再/opt/nginx/html/文件下添加index.html文件。

![1530007650381](C:\Users\789\AppData\Local\Temp\1530007650381.png)

访问ngix，无出错则搭建完成。

#### 安装mysql

```
$ mkdir -p ~/mysql/data ~/mysql/logs ~/mysql/conf
```

>data目录将映射为mysql容器配置的数据文件存放路径logs目录将映射为mysql容器的日志目录conf目录里的配置文件将映射为mysql容器的配置文件

```
docker run -p 3306:3306 --name mymysql -v $PWD/conf:/etc/mysql/conf.d -v $PWD/logs:/logs -v $PWD/data:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=123456 -d mysql
```

>`pwd`=$PWD 可用echo $PWD验证
>
>命令说明：
>
>-p 3306:3306：将容器的 3306 端口映射到主机的 3306 端口。
>
>-v $PWD/conf:/etc/mysql/conf.d：将主机当前目录下的 conf/my.cnf 挂载到容器的 /etc/mysql/my.cnf。
>
>-v $PWD/logs:/logs：将主机当前目录下的 logs 目录挂载到容器的 /logs。
>
>-v $PWD/data:/var/lib/mysql ：将主机当前目录下的data目录挂载到容器的 /var/lib/mysql 。       
>
>-e MYSQL_ROOT_PASSWORD=123456：初始化 root 用户的密码。

#### 安装tomcat

```
docker run --name tomcat -p 8080:8080 -v $PWD/test:/usr/local/tomcat/webapps/test -d tomcat
```

>-p 8080:8080：将容器的8080端口映射到主机的8080端口-v $PWD/test:/usr/local/tomcat/webapps/test：将主机中当前目录下的test挂载到容器的/test.
>
>主机的test和容器的test会自动创建。

Dockerfile方式：

```
FROM ubuntu:14.04
MAINTAINER boonya <boonya@sina.com> 
# now add java and tomcat support in the container 
ADD jdk-8u121-linux-x64.tar.gz /usr/local/ 
ADD apache-tomcat-8.5.16.tar.gz /usr/local/ 
ADD tomcat-users.xml /usr/local/apache-tomcat-8.5.16/conf/tomcat-users.xml
# configuration of java and tomcat ENV 
ENV JAVA_HOME /usr/local/jdk1.8.0_121 
ENV CLASSPATH $JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar 
ENV CATALINA_HOME /usr/local/apache-tomcat-8.5.16 
ENV CATALINA_BASE /usr/local/apache-tomcat-8.5.16 
ENV PATH $PATH:$JAVA_HOME/bin:$CATALINA_HOME/lib:$CATALINA_HOME/bin 
# container listener port 
EXPOSE 8080 
# startup web application services by self 
CMD /usr/local/apache-tomcat-8.5.16/bin/catalina.sh run 等同于-->
ENTRYPOINT ["/usr/share/tomcat7/bin/catalina.sh", "run" ]
```

或者

```
FROM ubuntu:14.04
MAINTAINER 13662241921 "4754063522@qq.com"
# 设置当前工具目录
# 该命令不会新增镜像层
WORKDIR /home
# 安装必要的工具
RUN sudo apt-get update && \
    apt-get install -y  wget && \
    apt-get install -y tar && \
    wget --no-check-certificate --no-cookies --header "Cookie: oraclelicense=accept-securebackup-cookie" http://download.oracle.com/otn-pub/java/jdk/8u131-b11/d54c1d3a095b4ff2b6607d096fa80163/jdk-8u131-linux-x64.tar.gz && \
    tar -xvzf jdk-8u131-linux-x64.tar.gz && \
    wget http://mirrors.tuna.tsinghua.edu.cn/apache/tomcat/tomcat-8/v8.5.32/bin/apache-tomcat-8.5.32.tar.gz && \
    tar -xvzf apache-tomcat-8.5.32.tar.gz && \ 
    mv apache-tomcat-8.5.32/ tomcat && \
    rm -f jdk-8u131-linux-x64.tar.gz && \
    rm -f apache-tomcat-8.0.47.tar.gz && \    
    apt-get clean all
# 设置环境变量
ENV JAVA_HOME /home/jdk1.8.0_131
ENV CATALINA_HOME /home/tomcat
ENV PATH $PATH:$JAVA_HOME/bin:$CATALINA_HOME/bin
# 暴露tomcat 8080端口
EXPOSE 8080
ENTRYPOINT /home/tomcat/bin/startup.sh && tail -f /home/tomcat/logs/catalina.out
# 创建容器启动tomcat
CMD ["/home/tomcat/bin/startup.sh"]
```

然后执行：

```
docker build -t tomcat-web .
docker build --no-cache -t="tomcat-web" .
```

启动tomcat：

```
docker run -p 8080:8080 tomcat-web:latest
```

或指定挂载目录：

```
docker run --name web -d -p 8080:8080  -v /home/webapp/pro:/var/lib/tomcat7/webapps/
```

本地开发部署到远程tomcat：

tomcat-users.xml 

```
<role rolename="manager-gui"/> 
<role rolename="manager-script"/> 
<user username="tomcat" password="password" roles="manager-gui, manager-script"/>
```

maven settings.xml 

```
<servers>
   <server> 
     <id>TomcatServer</id>
     <username>tomcat</username> 
     <password>password</password> 
    </server>
</servers>
```

项目 pom.xml配置 

```
<build>
        <finalName>webtest</finalName>
        <plugins>
            <plugin>
                <groupId>org.apache.tomcat.maven</groupId>
                <artifactId>tomcat7-maven-plugin</artifactId>
                <version>2.1</version>
                <configuration>
                    <url>http://192.168.99.100:8080/manager/text</url>
                    <server>TomcatServer</server>
                    <path>/webtest</path>
                </configuration>
            </plugin>
        </plugins>
</build>
```

上面部署时出现 403 Access Denied 

目录结构：

```
jdk-8u121-linux-x64.tar.gz
apache-tomcat-8.5.16.tar.gz
DockerFile
tomcat-user.xml
```

上面Docker镜像的构建是成功了的 

但是通过Maven发布项目到Tomcat8却遇到了服务器拒绝访问403的错误 

主要是要配置${TOMCAT_HOME}/conf/tomcat-users.xml和${TOMCAT_HOME}/webapps/manager/META-INF/context.xml两个文件 

编辑${TOMCAT_HOME}/conf/tomcat-users.xml： 

```
<?xml version="1.0" encoding="UTF-8"?>
 
<tomcat-users xmlns="http://tomcat.apache.org/xml"
              xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
              xsi:schemaLocation="http://tomcat.apache.org/xml tomcat-users.xsd"
              version="1.0">
<role rolename="manager"/>
<role rolename="manager-gui"/>
<role rolename="admin"/>
<role rolename="admin-gui"/>
<role rolename="manager-script"/>
<user username="tomcat" password="password" roles="admin-gui,admin,manager-gui,manager,manager-script"/>
</tomcat-users>
```

编辑${TOMCAT_HOME}/webapps/manager/META-INF/context.xml： 

```
<?xml version="1.0" encoding="UTF-8"?>
<Context antiResourceLocking="false" privileged="true" docBase="${catalina.home}/webapps/manager" >
  <Valve className="org.apache.catalina.valves.RemoteAddrValve"    allow="^.*$" />
  <Manager sessionAttributeValueClassNameFilter="java\.lang\.(?:Boolean|Integer|Long|Number|String)|org\.apache\.catalina\.filters\.CsrfPreventionFilter\$LruCache(?:\$1)?|java\.util\.(?:Linked)?HashMap"/>
</Context>
```

登录tomcat的管理界面 

注：镜像是基于Tomcat8的远程管理角色权限，管理账号和密码是：tomcat/password。 

执行maven命令：mvn clean tomcat7:deploy 。看是否部署成功。

#### 部署sprongBoot

方案1：`手动拷贝jar包到目录，手动执行build`

第一步：搭建springboot的web应用，可在CMD命令行中通过mvn install命令将应用打成jar包:如demo-0.0.1-SNAPSHOT.jar

第二步：将jar包copy到centos文件系统中，指定目录示例为：/usr/local/demo-0.0.1-SNAPSHOT.jar

第三步：构建docker镜像：此处以docker build方式构建

a./usr/local下创建dockerfile文件

```
# 指定一个基础镜像
FROM  java:8
#安装应用执行的环境java
RUN yum -y install java
#将指定的jar文件复制到容器中
COPY demo-0.0.1-SNAPSHOT.jar /usr/local/
#执行jar文件
ENTRYPOINT ["java" ,"-jar","/usr/local/demo-0.0.1-SNAPSHOT.jar"]
```

或

```
FROM java:8
VOLUME /tmp
ADD springboot4Docker-1.0-SNAPSHOT.jar app.jar
RUN sh -c 'touch /app.jar'
ENV JAVA_OPTS=""
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
```

>VOLUME 指定了临时文件目录为/tmp。其效果是在主机 /var/lib/docker 目录下创建了一个临时文件，并链接到容器的/tmp。改步骤是可选的，如果涉及到文件系统的应用就很有必要了。/tmp目录用来持久化到 Docker 数据文件夹，因为 Spring Boot 使用的内嵌 Tomcat 容器默认使用/tmp作为工作目录 
>项目的 jar 文件作为 “app.jar” 添加到容器的 
>ENTRYPOINT 执行项目 app.jar。为了缩短 Tomcat 启动时间，添加一个系统属性指向 “/dev/urandom” 作为 Entropy Source 

pom.xml

```json
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <artifactId>springboot4Docker</artifactId>
    <groupId>com.docker</groupId>
    <version>1.0-SNAPSHOT</version>
    <modelVersion>4.0.0</modelVersion>
    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <docker.image.prefix>13662241921</docker.image.prefix>
        <spring.boot.version>1.3.3.RELEASE</spring.boot.version>
    </properties>
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
            <plugin>
                <groupId>com.spotify</groupId>
                <artifactId>docker-maven-plugin</artifactId>
                <version>0.4.3</version>
                <configuration>
                    <imageName>${docker.image.prefix}/${project.artifactId}</imageName>
                    <dockerDirectory>src/main/docker</dockerDirectory>
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
</project>
```

>Spotify 的 docker-maven-plugin 插件是用于构建 Maven 的 Docker Image 
>1）imageName指定了镜像的名字，本例为 springio/lidong-spring-boot-demo 
>2）dockerDirectory指定 Dockerfile 的位置 
>3）resources是指那些需要和 Dockerfile 放在一起，在构建镜像时使用的文件，一般应用 jar 包需要纳入。

b.通过docker build方式构建镜像:docker build -t="springboot"  --no-cache.(备注：-t是为该镜像指定名称,不需要缓存)

>SECURITY WARNING: You are building a Docker image from Windows against a non-Windows Docker host. All files and directories added to build context will have '-rwxr-xr-x' permissions. It is recommended to double check and reset permissions for sensitive files and directories.

解决：

>//如果我们的虚拟名为default,进入之后用chmod命令对相应的目录进行权限操作
>docker-machine ssh default
>
>会进入docker服务器的目录

c.上述步骤执行完毕后，通过docker images命令查看生成的镜像id为：bfac85643697

d.运行此镜像：docker run  -p 8080:8080 --name webtest bfac85643697（备注：-p是指定端口的映射将应用端口8086映射到容器端口8082，用于对应用进行访问，bfac85643697为生成的镜像id）或通过docker run -i -t -d  -p 8082:8086 --name webtest bfac85643697  -g "daemon off;"运行，区别在于第二种方式是启动了一个后台的守护进程，

e:应用运行以后，通过以下链接访问：http://192.168.0.193:8082/test（备注：192.168.0.193为docker宿主机ip，8082为上述指定的docker映射端口，test为应用的映射url，根据自己的情况指定访问的url）



方案二：maven docker插件

pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <artifactId>springboot4Docker</artifactId>
    <groupId>com.docker</groupId>
    <version>1.0-SNAPSHOT</version>
    <modelVersion>4.0.0</modelVersion>
    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <docker.image.prefix>13662241921</docker.image.prefix>
        <spring.boot.version>1.3.3.RELEASE</spring.boot.version>
    </properties>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>1.3.3.RELEASE</version>
        <relativePath/>
    </parent>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
            
            <plugin>
                <groupId>com.spotify</groupId>
                <artifactId>docker-maven-plugin</artifactId>
                <version>0.4.12</version>
                <configuration>
                 <imageName>13662241921/aaa</imageName>
                  <!--   <imageTags>
                        docker的tag为项目版本号、latest
                        <imageTag>${git.commit.id.abbrev}</imageTag>
                        <imageTag>latest</imageTag>
                    </imageTags> -->

                     <!-- <baseImage>java</baseImage>  -->
                     <!-- <entryPoint>["java", "-jar", "/${project.build.finalName}.jar"]</entryPoint> -->
                   <dockerDirectory>src/main/docker</dockerDirectory>
                   <!--  <dockerHost>https://192.168.99.100:2376</dockerHost>
                    <dockerCertPath>C:\Users\789\.docker\machine\machines\default</dockerCertPath> -->
                    <pushImage>true</pushImage>
                     <!--mvn clean package docker:build -DpushImage -->
                     <!--  <registryUrl>registry.cn-hangzhou.aliyuncs.com</registryUrl> -->
                    <serverId>docker-hub</serverId>
                   <!--<serverId>docker-hub</serverId>--> 
                    <resources>
                        <resource>
                            <targetPath>/</targetPath>
                            <directory>${project.build.directory}</directory>
                            <include>${project.build.finalName}.jar</include>
                        </resource>
                    </resources>
                   <settingsFile>/settings.xml</settingsFile>
                </configuration>
            </plugin> 
        </plugins>
    </build>
</project>
```

pom的同级目录下settings.xml

```
<settings>
<server>
    <id>docker-hub</id>
    <username>13662241921</username>
    <password>{JY/vuiMyBDMHVI8mgiqB//YnzLZbAnul5JQRggLVwU8=}</password>
    <configuration>
      <email>47540266@qq.com</email>
    </configuration>
</server>
</settings>
```

上面的password需要maven加密，加密步骤如下：

1 随意定义一个种子

```
mvn --encrypt-master-password 123asdadfafdadf
{BHe/qKN8q30HBG3bAGbYLOVLnAqVRkzjb9/7yWs+Ks0=}
```

2 编辑~/.m2/settings-security.xml，写入

```
<?xml version="1.0" encoding="UTF-8"?>
<settingsSecurity>
   <master>{上一步生成的内容}</master>
</settingsSecurity>
```

3 最终生成docker加密后的密码填写入项目pom同级settings.xml（`.m2下和settings-security.xml统计目录也要有`）

```
mvn --encrypt-password 你的邮箱密码
{RxLx1asdfiafrjIHfXZDadfwveda23avsdv=}

vim ~/.m2/settings.xml
<server>
     <id>internal</id>
     <username>54chen</username>
     <password>{RxLx1asdfiafrjIHfXZDadfwveda23avsdv=}</password>
</server>
```

4 运行命令

```
mvn package docker:build
因为pom里配置了<pushImage>true</pushImage> 将会push到docker远程仓库
或者在运行maven命令时指定参数：
mvn clean package docker:build -DpushImage
```



```
sudo docker login --username=***pro@gmail.com registry.cn-hangzhou.aliyuncs.com
sudo docker pull registry.cn-hangzhou.aliyuncs.com/viiso/dockerdemo:[镜像版本号]
```

```
docker push kitesweet/pan-search-springboot
docker pull kitesweet/pan-search-springboot
```
```
[ERROR] Failed to execute goal com.spotify:docker-maven-plugin:1.0.0:build (default-cli) on project springboot4Docker: Exception caught: java.util.concurrent.ExecutionException: com.spotify.docker.cli
ent.shaded.javax.ws.rs.ProcessingException: org.apache.http.client.ClientProtocolException: Cannot retry request with a non-repeatable request entity: Connection reset by peer: socket write error -> [
Help 1]
```

mvn package && java -jar target/docker-app-0.1.0.jar

解决方案：

1：环境变量配置正确，在windows系统环境变量中新建DOCKER_HOST,值为tcp://ip:端口

DOCKER_HOST=tcp://192.168.99.100:2376

DOCKER_HOST=unix:///var/run/docker.sock mvn clean install （linux）

export DOCKET_HOST=unix:///private/var/tmp/docker.sock

2：编辑docker

vim /etc/sysconfig/docker

加入 other_args="-H tcp://0.0.0.0:2375 -H unix:///var/run/docker.sock"

:wq!

3：重启 service docker restart

4：mvn clean package docker:build -DskipTests

192.168.99.100

SET DOCKER_TLS_VERIFY=1
SET DOCKER_HOST=tcp://192.168.99.100:2376
SET DOCKER_CERT_PATH=C:\Users\789\.docker\machine\machines\default
SET DOCKER_MACHINE_NAME=default

访问docker服务

 docker -H tcp://192.168.99.101:2376 images

linux下：

 service docker start启动，需要先停止docker服务，用docker提供的命令行参数启动，例如docker daemon -H 0.0.0.0:2375。启动之后通过浏览器访问http://dockerhost:port/info查看你刚才的启动是否成功，如果正常返回一串json说明服务启动成功了。之后修改pom文件，在docker-maven-plugin插件的configuration节点下新增一个dockerHost节点，填入你的docker server的主机地址和端口，我这边填写的是http://10.100.120.22：2375，每一个模块都需要做这样的修改，之后在重新构建就没有问题了。

docker info

首先要查看docker daemon是否在运行。

ps aux | grep docker

```

com.spotify:docker-maven-plugin 报localhost:2375 Connection refused 错误正确解决方法

docker-machine env 查看docker host和证书信息
<dockerHost>https://192.168.99.100:2376</dockerHost>
<dockerCertPath>C:\Users\Administrator\.docker\machine\machines\default</dockerCertPath>
```

Canot connect to the Docker daemon. Is 'docker -d' running on this host?

以及类似的错误,就连docker version命令都报错了,楼主开始找啊找,找到了好多东西,结果发现没一个能行的,最后楼主使用这样的命令:

```
# vim /etc/default/docker
```

在该文件中添加如下内容:

```
DOCKER_OPTS="-H unix:///var/run/docker.sock -H 0.0.0.0:5555"
# service docker restart
```



**3、在Windows 中 利用 Maven 为该Docker服务端构筑docker 镜像（该Windows中无须安装Docker）：**

pom.xml配置插件：

```
<build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <executable>true</executable>
                </configuration>
            </plugin>

            <plugin>
                <groupId>com.spotify</groupId>
                <artifactId>docker-maven-plugin</artifactId>
                <version>0.4.13</version>
                <configuration>
                    <imageName>liuyx/test:0.1</imageName>
　　　　　　　　　　　　<!-- 本以为在此设置 前文服务端的地址和端口 可以生效，但是始终会报一个tcp protocol is not supported，鄙人才疏学浅，终究不知为什么，我们还是在别的地方设置吧 -->
                    <!--<dockerHost>tcp://xxx.xxx.xxx.xxx:2375</dockerHost>-->
                    <!-- 设置Dockerfile路径，设置了就会忽略baseImage和entryPoint等Dockerfile中应出现的东西，转而去执行Dockerfile-->
                    <!--<dockerDirectory>src/main/docker</dockerDirectory>-->

                    <skipDockerBuild>false</skipDockerBuild>
                    <baseImage>java8</baseImage>
                    <entryPoint>["java","-jar","/${project.build.finalName}.jar"]</entryPoint>
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

构建命令：

```
#在此，先设置当前的环境变量“DOCKER_HOST”为前文提到的docker服务端的ip和端口，当然也可以直接去系统环境变量设置
set DOCKER_HOST=tcp://xxx.xxx.xxx.xxx:2375
mvn clean package -X docker:build
```



#### Docker学习笔记 — 开启Docker远程访问

```
DOCKER_HOST=$host:2375 docker ps
默认情况下，Docker守护进程会生成一个socket（/var/run/docker.sock）文件来进行本地进程通信，而不会监听任何端口，因此只能在本地使用docker客户端或者使用Docker API进行操作。 
如果想在其他主机上操作Docker主机，就需要让Docker守护进程监听一个端口，这样才能实现远程通信。
修改Docker服务启动配置文件，添加一个未被占用的端口号，重启docker守护进程。
# vim /etc/default/docker
DOCKER_OPTS="-H 0.0.0.0:5555"
# service docker restart
此时发现docker守护进程已经在监听5555端口，在另一台主机上可以通过该端口访问Docker进程了。
# docker -H IP:5555 images
但是我们却发现在本地操作docker却出现问题。
# docker images
FATA[0000] Cannot connect to the Docker daemon. Is 'docker -d' running on this host
这是因为Docker进程只开启了远程访问，本地套接字访问未开启。我们修改/etc/default/docker，然后重启即可。

# vim /etc/default/docker
DOCKER_OPTS="-H unix:///var/run/docker.sock -H 0.0.0.0:5555"
# service docker restart
现在本地和远程均可访问docker进程了。
```

default)Looks like something went wrong in step ´Checking if machine default exists´...Press any key to continue... 

这是因为，启动时如果检测到没有 Boot2Docker，就会去下载，这个下载过程出现网络连接上的错误了，导致启动失败。

如果存在下载失败的临时文件，要将其删除。（我的机器上的路径是C:\Users\zheng\.docker\machine\cache\boot2docker.iso.tmp24517390）
自己用其他工具去下载对应的 boot2docker.iso 文件（下载链接：https://github.com/boot2docker/boot2docker/releases/download/v17.05.0-ce/boot2docker.iso）
然后放置到对应的目录（我的是：C:\Users\zheng\.docker\machine\cache\boot2docker.iso）就可以了。

本机安装一个docker ,远程主机安装一个docker ，这篇文章主要就是讲解如何用本地的docker client 访问远程主机的docker daemon

默认情况下，[Docker](http://lib.csdn.net/base/docker)守护进程会生成一个socket（/var/run/docker.sock）文件来进行本地进程通信，而不会监听任何端口，因此只能在本地使用docker客户端或者使用Docker API进行操作。 
如果想在其他主机上操作Docker主机，就需要让Docker守护进程监听一个端口，这样才能实现远程通信。

修改Docker服务启动配置文件，添加一个未被占用的端口号，重启docker守护进程。

```
# vim /etc/default/docker
DOCKER_OPTS="-H 0.0.0.0:5555"
# service docker restart
```

如果没有这个文件，应该考虑修改 /etc/sysconfig/docker这个文件。

此时发现docker守护进程已经在监听5555端口，在另一台主机上可以通过该端口访问Docker进程了。

实例说明：

我们可以从一台安装了docker的机器访问另一台安装了docker的机器。一般情况下我们使用当前机器的docker客户端访问当前机器的Server端。下面演示如何访问其他docker服务端。

- 第一台IP：192.168.12.3
- 第二台IP：192.168.12.4

使用第二台安装有docker的服务器做演示。为区分，设置label不同。

修改守护进程（Server）默认的启动配置：

默认是：`-H fd://`，可修改为：`-H tcp://0.0.0.0:2375 -H unix:///var/run/docker.sock -H fd:// --label name=server_1`

可设置多个连接方式。

tcp 是远程连接，unix 是本地socket 链接，fd不知道。

第一台访问第二台机器的docker服务：

- 通过http连接Server:

  ```
  curl http://192.168.12.4:2375/info
  ```

  访问的是服务器192.168.12.4:2375的info接口，返回服务器相关信息。

- 通过docker客户端访问Server：

  ```
  docker -H tcp://192.168.12.4:2375 info
  ```

如果是是第一台机器访问第一台机器Docker的服务端，则使用127.0.0.1:2375就行了。

和服务器端一样，客户端也支持三种连接方式，默认是 `-H unix:///var/run/docker.sock`：

- `-H unix:///path/to/sock`
- `tcp://host:port`
- `fd://socketfd`

docker客户端使用`docker info`默认访问的是本地Server。可以修改环境变量DOCKER_HOST改变默认连接。命令行直接输入：

```
export DOCKER_HOST="tcp://127.0.0.1:2375"
```

`127.0.0.1:237` 可以替换为实际的Server地址。

如果想恢复本地连接，将 DOCKER_HOST 置空即可：

```
export DOCKER_HOST=""
```


刚在新的Centos上安装Docker-CE,后运行`docker run hello-world`报错`Cannot connect to the Docker daemon at unix:///var/run/docker.sock. Is the docker daemon running?`

#### 访问远程机器docker

##### Ubuntu 15.04以后：

1 创建/etc/systemd/system/docker.service.d目录 【远程机器】

```
$ sudo mkdir /etc/systemd/system/docker.service.d
$ sudo vi /etc/systemd/system/docker.service.d/docker.conf  一说为：http-proxy.conf（经过验证都可以）
$ sudo vi /etc/systemd/system/multi-user.target.wants/docker.service(待验证)
```

2 创建文件内容 【远程机器】

```
[Service]

ExecStart=

ExecStart=/usr/bin/dockerd -H tcp://0.0.0.0:2376 -H unix:///var/run/docker.sock  (--insecure-registry=192.168.1.104:5000)
```

3 刷新docker守护进程 【远程机器】

```
sudo systemctl daemon-reload
```

4 重启docker服务 【远程机器】

```
sudo systemctl restart docker
```

5 确认是否成功 【远程机器】

```
ps -ef | grep docker
```

6 本地测试（本地不需要创建虚拟机，有docker客户端即可，但是cmd窗口关了就不生效了）

```
docker-machine env default （需要粘贴提示的信息）
docker info
http://$ip:2376/info

SET DOCKER_HOST=tcp://192.168.135.130:2376
docker images(可以，访问的远程DOCKER_HOST机器)
docker -H tcp://192.168.135.130:2376 images(可以，访问的参数指定的docker)

```

7 也可以临时启用远程访问

```
sudo dockerd -H tcp://0.0.0.0:2375 -H unix:///var/run/docker.sock &
```
8 查看docker使用的配置文件

```
//查看环境配置文件
$ systemctl show docker | grep EnvironmentFile
EnvironmentFile=-/etc/sysconfig/docker (ignore_errors=yes)
//查看服务启动文件位置：
$ systemctl show --property=FragmentPath docker
FragmentPath=/usr/lib/systemd/system/docker.service

$ grep EnvironmentFile /usr/lib/systemd/system/docker.service
EnvironmentFile=-/etc/sysconfig/docker
```

##### `在centos7.2下`

```
在：ExecStart=/usr/bin/dockerd-current 后面追加：-H tcp://0.0.0.0:2375 -H unix:///var/run/docker.sock

[root@wk02 system]# pwd
/usr/lib/systemd/system
[root@wk02 system]# vi /usr/lib/systemd/system/docker.service

# for containers run by docker
ExecStaer=/usr/bin/dcokerd    //保留
ExecStart=         //新增
ExecStart=/usr/bin/dockerd tcp://0.0.0.0:2376 -H unix:///var/run/docker.sock  //新增
```

```
systemctl daemon-reload 
systemctl restart docker.service
```

客户端：

```
docker -H tcp://192.168.48.128:2376 images
curl 192.168.48.128:2376/info

如果在服务器上可以执行，远程客户端ping通但是出错，则
关闭防火墙：systemctl stop firewalld.service

附客户端错误如下：
An error occurred trying to connect: Get http://192.168.48.128:2376/v1.22/images/json: dial tcp 192.168.48.128:2376: connectex: A connection atte                          mpt failed because the connected party did not properly respond after a period of time, or established connection failed because connected host has failed to respond.
```

防火墙：

```
#@root# : firewall-cmd --state  #查看状态
running

firewall-cmd --reload  #重启防火墙

启动一个服务：systemctl start firewalld.service
关闭一个服务：systemctl stop firewalld.service
重启一个服务：systemctl restart firewalld.service
显示一个服务的状态：systemctl status firewalld.service
在开机时启用一个服务：systemctl enable firewalld.service
在开机时禁用一个服务：systemctl disable firewalld.service
查看服务是否开机启动：systemctl is-enabled firewalld.service
查看已启动的服务列表：systemctl list-unit-files|grep enabled
查看启动失败的服务列表：systemctl --failed
```



#### 访问远程机器docker (tls)





#### 修改容器内容导致无法启动

我们可能会碰到这样的一个问题，在容器执行过程中，修改了容器的内容（如配置文件信息），但因为修改出了问题。导致容器关闭后，无法启动。

这事需要重新修改配置文件。 正常情况下可以通过 docker exec命令打开容器的一个shell终端进去修改。

但这时容器已经无法启动了。这时该如何处理呢？ 这有两种方案：

**方案一：创建新镜像**

把这个问题容器用docker commit提交到一个新的镜像，然后用docker run -it 基于新镜像运行一个新的容器进去改变（修复）配置文件。

再通过新的容器再提交一个新的镜像，然后在基于新的镜像重新启动容器（同最初的容器）。

这个方法是可行的，但问题是步骤多，而且提交了新的镜像，对于后续维护增加了复杂性。

**方案二：直接修改容器的文件**

所有的容器数据都存在/var/lib/docker/aufs/diff/路径下。比如：

```
root@ubuntu:~# ls /var/lib/docker/aufs/diff/ -l
total 176
drwxr-xr-x  2 root root 4096 Mar  6 05:13 040bf8e0842564e26e62f3e3a30785bd9651c82c52ed99115cd5360ce979e680
drwxr-xr-x  6 root root 4096 Mar  6 05:13 04f7e78a2c8ac9664503f4ea5a1d94bf27b94620987f241cfb9fd6631f761113
drwxr-xr-x  2 root root 4096 Mar 11 01:07 0c666375883f81ba0fc3962368766e93710f59c072a4b80cdf5640323018ccdb
drwxr-xr-x  4 root root 4096 Mar 11 07:53 0d7fc1722e459b242140ec45faec754d4967d72ea2ddf321f8606c837f8e8d4f
drwxr-xr-x  6 root root 4096 Mar 11 07:53 0d7fc1722e459b242140ec45faec754d4967d72ea2ddf321f8606c837f8e8d4f-init
drwxr-xr-x  3 root root 4096 Mar  6 05:13 0dc5e226a795507723362cc16046cf16650f8f70dc7bb721b799a5f2a40512ce
drwxr-xr-x  2 root root 4096 Mar  6 05:13 0fd3b6e125673affc1f348cdb0c071782bde7d7ab4748bea3e30bc2d1d7ea7ab
......................
```

一个容器的数据对应这其中的一个或多个目录 。其中目录名的前几位就是容器的ID，通过这知道容器和目录的对应关系。
注意这个目录需要用root用户执行。

具体的操作步骤如下：

1、设置当前目录  cd /var/lib/docker/aufs/diff/

2、查找要修改的配置文件所在容器中的位置

find ./ -name 'nginx.conf'

假设我们要修改的是 nginx.conf文件，可能的结果如：

./eb531927ba243b59f0db78848809423f7debe148a9ef972088ea41be73c2aa81/etc/nginx/nginx.conf
./4975acfb30f3f729ac08a9c1bd642f735298a47057fc7c414c7479696b80f36a/etc/nginx/nginx.conf
./6fce3cb01e3c9b8cc4e1fc270c012b1d0b666fe49ad8b6bededb99e295c5da4c/etc/nginx/nginx.conf

这时我们通过比较要修改容器的ID 与上面几个目录的前缀，就知道是要修改哪个配置文件了。

如果我们进入类似 4975acfb30f3f729ac08a9c1bd642f735298a47057fc7c414c7479696b80f36a 目录，会发现这个目录下的内容和linux跟目录下的目录结构非常类似。我们可以找到相关的配置文件直接修改。

说明：因为一个容器的文件系统包括不可修改的镜像层和可修改的读写层，这个目录下其实就是读写层的内容。

3、修改完毕后用 docker start 容器名/ID  即可重新启动容器。



#### Docker 要点记录

1：比如 我想设置 F:\config 为 挂载一个本地目录   写法是   docker run -d -v /F/config

2：容器内部访问问题

​      如果是通过  服务名称访问    注意服务名称 不能有  特殊字符  如 & _ 等  

3：boot2docker  创建的docker虚拟机 默认密码

​      用户名和密码是: docker/tcuser

4：关于 boot2docker  docker虚拟机 环境变量的问题

　 sudo vi /var/lib/boot2docker/profile   直接添加到最后就行 了

5：查看容器的root用户密码

因为docker容器启动时的root用户的密码是随机分配的。所以，通过这种方式就可以得到redmine容器的root用户的密码了。

```
docker logs <容器名orID> 2>&1 | grep '^User: ' | tail -n1
```

6：查看容器日志

```
docker logs -f <容器名orID>
```

7：一个容器连接到另一个容器

```
docker run -i -t --name sonar -d -link mmysql:db   tpires/sonar-server
```

8：运行一个新容器，同时为它命名、端口映射、文件夹映射。以redmine镜像为例

​      容器连接到mmysql容器，并将mmysql容器重命名为db。这样，sonar容器就可以使用db的相关的环境变量了。

```
docker run --name redmine -p 9003:80 -p 9023:22 -d -v /var/redmine/files:/redmine/files -v /var/redmine/mysql:/var/lib/mysql sameersbn/redmine
```

9：从container中拷贝文件出来

```
sudo docker cp 7bb0e258aefe:/etc/debian_version .
```

拷贝7bb0e258aefe中的/etc/debian_version到当前目录下。
`注意`：只要7bb0e258aefe没有被删除，文件命名空间就还在，可以放心的把exit状态的container的文件拷贝出来

10：CMD [“executable”,“param1”,”param2”] 使用exec 执行，推荐方式

11：`RUN`命令与`CMD`命令的区别

  简单说，`RUN`命令在 image 文件的构建阶段执行，执行结果都会打包进入 image 文件；`CMD`命令则是在容器启动后      执行。另外，一个 Dockerfile 可以包含多个`RUN`命令，但是只能有一个`CMD`命令。

注意，指定了`CMD`命令以后，`docker container run`命令就不能附加命令了（比如前面的`/bin/bash`），否则它会覆盖`CMD`命令。

12: `Are you trying to connect to a TLS-enabled daemon without TLS`

```
Are you trying to connect to a TLS-enabled daemon without TLS
默认安装完 docker 后，每次执行 docker 都需要运行 sudo 命令，非常浪费时间影响效率。如果不跟 sudo，直接执行 docker images 命令会有如下问题：
FATA[0000] Get http:///var/run/docker.sock/v1.18/images/json: dial unix /var/run/docker.sock: permission denied. Are you trying to connect to a TLS-enabled daemon without TLS?
于是考虑如何免 sudo 使用 docker，经过查找资料，发现只要把用户加入 docker 用户组即可，具体用法如下。
免 sudo 使用 docker
如果还没有 docker group 就添加一个：
sudo groupadd docker
将用户加入该 group 内。然后退出并重新登录就生效啦。
sudo gpasswd -a ${USER} docker
重启 docker 服务
sudo service docker restart
```



#### 基于 Docker 打造前端持续集成开发环境

本文将以一个标准的 Vue 项目为例，完全抛弃传统的前端项目开发部署方式，基于 Docker 容器技术打造一个精简的前端持续集成的开发环境。

**前置知识：**

1. CI（持续集成）：[阮一峰老师的关于 CI 的介绍](https://link.juejin.im/?target=http%3A%2F%2Fwww.ruanyifeng.com%2Fblog%2F2015%2F09%2Fcontinuous-integration.html)


2. Docker： [Docker 快速入门](https://link.juejin.im/?target=http%3A%2F%2Fguide.daocloud.io%2Fdcs%2Fdocker-9152673.html)

**目标：**

1. 代码无需在本地构建


2. 只需将代码推上 Github ，自动构建 -> 部署


3. 版本易管理，可轻松回退版本

现在开始进入主题

**第一步： 初始化 Vue 项目（使用vue官方脚手架 vue-cli）**

1. 初始化 vue 项目：vue init webpack vue-docker-demo
2. 在项目根目录下编 Dockerfile

[![01](http://blog.daocloud.io/wp-content/uploads/2017/11/01.jpg)](http://blog.daocloud.io/wp-content/uploads/2017/11/01.jpg)

Dockerfile 内容如下（如果是构建其他项目，比如 angular4，只需安装 angular-cli，将构建参数改成 ng build 即可，大同小异）

```
FROM node:6.10.3-slim
RUN apt-get update \    && apt-get install -y nginx
WORKDIR /app
COPY . /app/
EXPOSE 80
RUN  npm install \     && npm run build \     && cp -r dist/* /var/www/html \     && rm -rf /app
CMD ["nginx","-g","daemon off;"]
```

\3. 初始化 git， 连接并将代码推送到 Github 仓库，

[![02](http://blog.daocloud.io/wp-content/uploads/2017/11/02.jpg)](http://blog.daocloud.io/wp-content/uploads/2017/11/02.jpg)

**第二步：使用 DaoCloud 搭建 Devops 流程**

（也可以使用其他公有云服务，差别不大，本文将以简单易操作并且对个人开发者免费的 DaoCloud 为例）

\1. 注册一个 DaoCloud 账号

\2. 用户中心 -> 代码托管，授权可访问你的 Github 仓库

[![03](http://blog.daocloud.io/wp-content/uploads/2017/11/03.jpg)](http://blog.daocloud.io/wp-content/uploads/2017/11/03.jpg)

\2. 在 Devops 项目中新建一个**项目**，并选择 Github 中对应刚才新创建的项目

[![04](http://blog.daocloud.io/wp-content/uploads/2017/11/04.jpg)](http://blog.daocloud.io/wp-content/uploads/2017/11/04.jpg)

\3. 先手动构建一个镜像版本，便于下面用这个镜像版本创建一个**应用**

[![05](http://blog.daocloud.io/wp-content/uploads/2017/11/05.jpg)](http://blog.daocloud.io/wp-content/uploads/2017/11/05.jpg)

[![06](http://blog.daocloud.io/wp-content/uploads/2017/11/06.jpg)](http://blog.daocloud.io/wp-content/uploads/2017/11/06.jpg)

\4. 连接自有主机（没有自有主机的，也可以使用云端测试环境）

tips：可以去购买 vultr 等主机，按照指示流程完成主机接入，大概很简单的三四步操作，注意在完成主机连接后，需要手动在主机上启动 docker（service docker start）

[![07](http://blog.daocloud.io/wp-content/uploads/2017/11/07.jpg)](http://blog.daocloud.io/wp-content/uploads/2017/11/07.jpg)[![08](http://blog.daocloud.io/wp-content/uploads/2017/11/08.jpg)](http://blog.daocloud.io/wp-content/uploads/2017/11/08.jpg)

太酷了，我们已经将我们的主机接入了 DaoCloud，接下来就来完成最有意思的一步。

\5. 创建一个**应用**

进入【镜像仓库】选择刚才手动构建出来的镜像，并部署最新版本到自由主机或者云端测试环境

[![09](http://blog.daocloud.io/wp-content/uploads/2017/11/09.jpg)](http://blog.daocloud.io/wp-content/uploads/2017/11/09.jpg)[![10](http://blog.daocloud.io/wp-content/uploads/2017/11/10.jpg)](http://blog.daocloud.io/wp-content/uploads/2017/11/10.jpg)

稍等片刻，便可以点击“立即部署”

[![11](http://blog.daocloud.io/wp-content/uploads/2017/11/11.jpg)](http://blog.daocloud.io/wp-content/uploads/2017/11/11.jpg)

等待完成镜像拉取，待容器列表中的容器起来后，通过地址查看我们部署的 vue 应用

[![12](http://blog.daocloud.io/wp-content/uploads/2017/11/12.jpg)](http://blog.daocloud.io/wp-content/uploads/2017/11/12.jpg)[![13](http://blog.daocloud.io/wp-content/uploads/2017/11/13.jpg)](http://blog.daocloud.io/wp-content/uploads/2017/11/13.jpg)

 

太棒了，我们已经可以访问到我们刚才部署的 vue 应用了，也表示我们已经将我们的镜像部署到我们的自有主机上去了，此时进入主机查看容器运行情况，可以看到有一个正在运行中的容器，正是我们刚刚部署的，一切都是完美的。

[![14](http://blog.daocloud.io/wp-content/uploads/2017/11/14.jpg)](http://blog.daocloud.io/wp-content/uploads/2017/11/14.jpg)

完成到这里，我们可以说已经完成了 99% 的工作，但是还非常重要的最后 1%，那就是真正的自定义持续集成流程，让一切都自动化起来，现在让我们回到刚才 Devops 项目的【流程定义】中去

6.定义自动构建，自动发布任务

回到 Devops 项目里对自动化流程进行定义

[![15](http://blog.daocloud.io/wp-content/uploads/2017/11/15.jpg)](http://blog.daocloud.io/wp-content/uploads/2017/11/15.jpg)

首先我们来定义一下**自动构建任务**，我们设定规则是只有在有新 tag 时才执行构建任务，构建时查找根目录下的 Dockerfile，并以此构建镜像

[![16](http://blog.daocloud.io/wp-content/uploads/2017/11/16.jpg)](http://blog.daocloud.io/wp-content/uploads/2017/11/16.jpg)[![17](http://blog.daocloud.io/wp-content/uploads/2017/11/17.jpg)](http://blog.daocloud.io/wp-content/uploads/2017/11/17.jpg)

其次，我们再来定义**自动发布任务**，当构建任务完成时自动触发自动发布任务，并发布到自有主机的应用上去

[![18](http://blog.daocloud.io/wp-content/uploads/2017/11/18.jpg)](http://blog.daocloud.io/wp-content/uploads/2017/11/18.jpg)[![19](http://blog.daocloud.io/wp-content/uploads/2017/11/19.jpg)](http://blog.daocloud.io/wp-content/uploads/2017/11/19.jpg)

[![20](http://blog.daocloud.io/wp-content/uploads/2017/11/20.jpg)](http://blog.daocloud.io/wp-content/uploads/2017/11/20.jpg)

至此，我们已经完成了，所有流程控制工作，去测试一下整个流程是否能走通？

**第三步： 测试整个流程**

回到我们本地，修改一下文本内容，提交，并推送到远端，并且打下我们的第一个版本tag 1.0.0，并将 tag 推送到远端

![img](https://user-gold-cdn.xitu.io/2017/11/21/15fdf37bbbd53c46?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)

可以看到，与此同时，我们打 tag 的操作触发了我们定义的 CI 流程

![img](https://user-gold-cdn.xitu.io/2017/11/21/15fdf3b45cf43110?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)

稍等片刻，可以看到我们的应用更新了，对应的版本就是我们刚刚推上去的 1.0.0

![img](https://user-gold-cdn.xitu.io/2017/11/21/15fdf3d9fcb73808?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)

我们还可以对应用的版本进行切换，回退等操作

![img](https://user-gold-cdn.xitu.io/2017/11/21/15fdf3ea7bd79a0f?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)

至此，我们在只编写了一个 Dockerfile 配置文件，没有编写脚本的情况下，成功地搭建了一套前端的持续集成开发环境，之后我们只需要专心编写我们的业务代码就好了，打一个 Tag 便可以轻松地完成自动部署上线。

当然，这只是非常简单的一个部署在单节点的裸应用，它没有涉及到 nginx 配置，负载均衡，后端服务，日志收集，数据存储，灰度发布，集群管理等生产环境的所需要求，如果有兴趣，请持续关注我的 Docker 系列文章，以上这些都会在我接下来的文章中一一解答。 



#### 在Docker中运行SSH进程服务

以下是用`Dockerfile`设置sshd服务容器，您可以使用连接并检查其他容器的卷,或者可以快速访问测试容器。

```
    # sshd
    #
    # VERSION               0.0.1

    FROM     ubuntu:12.04
    MAINTAINER Thatcher R. Peskens "thatcher@dotcloud.com"

    # make sure the package repository is up to date
    RUN apt-get update

    RUN apt-get install -y openssh-server
    RUN mkdir /var/run/sshd
    RUN echo 'root:screencast' |chpasswd

    EXPOSE 22
    CMD    ["/usr/sbin/sshd", "-D"]
```

使用如下命令构建镜像：

```
    $ sudo docker build --rm -t eg_sshd .
```

然后运行它，你可以使用`docker port`来找出容器端口22映射到主机的端口

```
    $ sudo docker run -d -P --name test_sshd eg_sshd
    $ sudo docker port test_sshd 22
    0.0.0.0:49154
```

现在你可以使用ssh登陆Docker进程的主机IP地址，端口是49154(IP地址可以使用ifconfig获取)：

```
    $ ssh root@192.168.1.2 -p 49154
    # The password is ``screencast``.
    $$
```

最后，清理停止的容器，并且删除容器，然后删除镜像。

```
    $ sudo docker stop test_sshd
    $ sudo docker rm test_sshd
    $ sudo docker rmi eg_sshd
```


docker-machine create   --driver generic   --generic-ip-address=192.168.135.131   --generic-ssh-key id_rsa  --generic-ssh-user=root --generic-ssh-port 22 docker

docker三剑客：

https://www.cnblogs.com/qingzheng/category/1033826.html

https://www.thegeekstuff.com/2016/02/docker-machine-create-generic/