# **docker machine概要**

Docker Machine发布之前，你可能会遇到以下问题：

①你需要登录主机，按照主机及操作系统特有的安装以及配置步骤安装Docker，使其能运行Docker容器。
②你需要研发一套工具管理多个Docker主机并监控其状态。
③你在本地开发，产品部署在公有云平台，你希望能尽可能的减小两个环境的差异性

Docker Machine的出现解决了以上问题。

①Docker Machine简化了部署的复杂度，无论是在本机的虚拟机上还是在公有云平台，只需要一条命令便可搭建好Docker主机
②Docker Machine提供了多平台多Docker主机的集中管理
③Docker Machine 使应用由本地迁移到云端变得简单，只需要修改一下环境变量即可和任意Docker主机通信部署应用。

# docker machine在远程主机部署docker

环境:

1. docker-machine主机：192.168.1.9 docker版本：docker-ce
2. docker主机：192.168.1.10

步骤：

①更新curl

```
yum update curl -y
```

②安装docker-machine

```
curl -L https://github.com/docker/mac...uname -s-uname -m >/tmp/docker-machine && install /tmp/docker-machine /usr/local/bin/docker-machine
```

```
[root@node1 ~]# docker-machine -v
docker-machine version 0.13.0, build 9ba6da9
```

③创建ssh密钥对，实现两主机无密登录

```
[root@node1 ~]# ssh-keygen
[root@node1 ~]# ssh-copy-id 192.168.1.10
```

④开启machine通讯端口

```
[root@node1 ~]# firewall-cmd --add-port=2376/tcp --permanent
[root@node1 ~]# firewall-cmd --reload
```

⑤创建docker主机,命名host1

```
[root@node1 ~]# docker-machine create -d generic --generic-ip-address=192.168.1.10 host1
Running pre-create checks...
Creating machine...
(swarm-master) No SSH key specified. Assuming an existing key at the default location.
Waiting for machine to be running, this may take a few minutes...
Detecting operating system of created instance...
Waiting for SSH to be available...
Detecting the provisioner...
Provisioning with centos...
Running...
[root@node1 ~]# docker-machine ls
NAME ACTIVE DRIVER STATE URL SWARM DOCKER ERRORS
host1 - generic Running tcp://192.168.1.10:2376 v17.12.1-ce
```

⑥变更docker环境变量

```
[root@node1 ~]# docker-machine env host1
export DOCKER_TLS_VERIFY="1"
export DOCKER_HOST="tcp://192.168.1.10:2376"
export DOCKER_CERT_PATH="/root/.docker/machine/machines/host1"
export DOCKER_MACHINE_NAME="host1"
# Run this command to configure your shell: 
# eval $(docker-machine env host1)

[root@node1 ~]# eval $(docker-machine env host1)

```

⑦运行容器查看两端是否同步

```
[root@node1 ~]# docker run -d busybox
[root@node1 ~]# docker-machine ssh host1
[root@host1 ~]# docker image ls
REPOSITORY TAG IMAGE ID CREATED SIZE
busybox latest f6e427c148a7 6 days ago 1.15MB
[root@host1 ~]# docker ps 
CONTAINER ID IMAGE COMMAND CREATED STATUS PORTS NAMES
8d9b5005a264 busybox "sh" 17 seconds ago Up 16 seconds ago modest_joliot
[root@host1 ~]# exit
[root@node1 ~]# docker ps
CONTAINER ID IMAGE COMMAND CREATED STATUS PORTS NAMES
8d9b5005a264 busybox "sh" 57seconds ago Up 16 seconds ago modest_joliot
```









