## 介绍

[Docker Swarm](https://docs.docker.com/engine/swarm/) 和 Docker Compose 一样，都是 Docker 官方容器编排项目，但不同的是，Docker Compose 是一个在单个服务器或主机上创建多个容器的工具，而 Docker Swarm 则可以在多个服务器或主机上创建容器集群服务，对于微服务的部署，显然 Docker Swarm 会更加适合。

从 Docker 1.12.0 版本开始，Docker Swarm 已经包含在 Docker 引擎中（`docker swarm`），并且已经内置了服务发现工具，我们就不需要像之前一样，再配置 Etcd 或者 [Consul](https://www.ibm.com/developerworks/cn/opensource/os-cn-consul-docker-swarm/index.html) 来进行服务发现配置了

## Docker Machine 创建 Docker 主机

在进行 Docker Swarm 配置之前，我们还需要说下 Docker 另外一个官方工具 Docker Machine，其作用就是快速帮助我们搭建 Docker 主机环境，比如我们要使用 Docker Swarm，就必须有很多的 Docker 主机来进行操作，Docker Machine 就是最理想的工具。

因为我是在 windows 上进行操作的，并且 Docker forwindows 已经包含了 Docker Machine（`docker machine`），所以我不需要再额外进行安装了，如果使用 Linux 系统的话，安装也非常简单，命令：

```
$ sudo curl -L https://github.com/docker/machine/releases/download/v0.13.0/docker-machine-`uname -s`-`uname -m` > /usr/local/bin/docker-machine
$ sudo chmod +x /usr/local/bin/docker-machine
```

好了，我们先使用 Docker Machine 创建四个 Docker 主机，命令

```
$ docker-machine create -d virtualbox manager1 && 
docker-machine create -d virtualbox manager2 && 
docker-machine create -d virtualbox worker1 && 
docker-machine create -d virtualbox worker2
```

我们可以查看下创建的 Docker 主机信息，命令:(wk01,wk02是我创建的远程centos主机)

```
PS C:\Users\789> docker-machine ls
NAME       ACTIVE   DRIVER       STATE     URL                         SWARM   DOCKER
manager1   -        virtualbox   Running   tcp://192.168.99.100:2376           v18.05.0-ce
manager2   -        virtualbox   Running   tcp://192.168.99.101:2376           v18.05.0-ce
wk01       -        generic      Running   tcp://192.168.48.128:2376           v18.05.0-ce
wk02       *        generic      Running   tcp://192.168.48.129:2376           v18.05.0-ce
worker1    -        virtualbox   Running   tcp://192.168.99.102:2376           v18.05.0-ce
worker2    -        virtualbox   Running   tcp://192.168.99.103:2376           v18.05.0-ce
```

现在可以连接到任意一台主机

```
PS C:\Users\789> docker-machine ssh manager1
                        ##         .
                  ## ## ##        ==
               ## ## ## ## ##    ===
           /"""""""""""""""""\___/ ===
      ~~~ {~~ ~~~~ ~~~ ~~~~ ~~~ ~ /  ===- ~~~
           \______ o           __/
             \    \         __/
              \____\_______/
 _                 _   ____     _            _
| |__   ___   ___ | |_|___ \ __| | ___   ___| | _____ _ __
| '_ \ / _ \ / _ \| __| __) / _` |/ _ \ / __| |/ / _ \ '__|
| |_) | (_) | (_) | |_ / __/ (_| | (_) | (__|   <  __/ |
|_.__/ \___/ \___/ \__|_____\__,_|\___/ \___|_|\_\___|_|
Boot2Docker version 18.05.0-ce, build HEAD : b5d6989 - Thu May 10 16:35:28 UTC 2018
Docker version 18.05.0-ce, build f150324
```



## Docker Swarm 配置集群节点

在`manager1`主机上，创建一个 Docker Swarm 管理节点（初始化集群的时候，会自动把当前节点设置为管理节点）

```
PS C:\Users\789> docker-machine ssh manager1 "docker swarm init --advertise-addr 192.168.99.100"
Swarm initialized: current node (5gdy154zxsfgr13bo8qkqj81p) is now a manager.

To add a worker to this swarm, run the following command:

    docker swarm join --token SWMTKN-1-44vqx042ok1m3gqg24vaeb717ftl37i2tey910v1sptm806t4w-4xwtu34y0mh5msacou8nofl8x 192.168.99.100:2377

To add a manager to this swarm, run 'docker swarm join-token manager' and follow the instructions.
```

接着，我们在`worker1`和`worker2`主机上，创建两个工作节点，并加入到集群中，命令：

```
$ docker-machine ssh worker1 "docker swarm join --token SWMTKN-1-44vqx042ok1m3gqg24vaeb717ftl37i2tey910v1sptm806t4w-4xwtu34y0mh5msacou8nofl8x 192.168.99.100:2377"
This node joined a swarm as a worker.

$ docker-machine ssh worker2 "docker swarm join --token SWMTKN-1-44vqx042ok1m3gqg24vaeb717ftl37i2tey910v1sptm806t4w-4xwtu34y0mh5msacou8nofl8x 192.168.99.100:2377"
This node joined a swarm as a worker.
```

还有另外一个`manager2`主机，需要配置为管理节点，我们需要先在`manager1`主机上，获取管理节点对应的`token`，然后再配置为管理节点，命令：

```
PS C:\Users\789> docker-machine ssh manager1 "docker swarm join-token manager"
To add a manager to this swarm, run the following command:

    docker swarm join --token SWMTKN-1-44vqx042ok1m3gqg24vaeb717ftl37i2tey910v1sptm806t4w-c088cowsuwn0mjijqrewm6ske 192.168.99.100:2377

```

```
$ docker-machine ssh manager2 "docker swarm join --token SWMTKN-1-44vqx042ok1m3gqg24vaeb717ftl37i2tey910v1sptm806t4w-c088cowsuwn0mjijqrewm6ske 192.168.99.100:2377"
This node joined a swarm as a manager.
```

配置好之后，我们进入`manager1`主机内，或者` docker-machine ssh manager1 docker node ls`（上面的命令也可以在主机内执行），然后查看集群节点的信息，命令：

```
$ docker node ls
ID                            HOSTNAME            STATUS              AVAILABILITY        MANAGER STATUS
n0ub7dpn90rxjq97dr0g8we0w *   manager1            Ready               Active              Leader
t4cy67qp0bf2spgabsutwxnzt     manager2            Ready               Active              Reachable
if0kmzp4ww3oy57y7cha7v36t     worker1             Ready               Active              
jgg61cujzaeb3du5796fm0x2g     worker2             Ready               Active      
```

`Leader`表示当前集群的头，`Reachable`可以理解为头的候选人，头一挂掉它就顶上去了。

需要注意的是，我当天配置好之后，把所有的 Docker 主机都`stop`了，然后隔天重新`start`之后，出现了下面问题：在manager上`docker-machine ssh manager1 docker node ls`

```
docker node ls
Error response from daemon: rpc error: code = Unknown desc = The swarm does not have a leader. It's possible that too few managers are online. Make sure more than half of the managers are online.
```

好像是集群节点丢失了头，相关问题：[如何处理 docker swarm 集群"The swarm does not have a leader"问题](https://q.cnblogs.com/q/96996/)，按照文章进行解决：

```
$ docker swarm init --force-new-cluster
Error response from daemon: could not choose an IP address to advertise since this system has multiple addresses on different interfaces (10.0.2.15 on eth0 and 192.168.99.102 on eth1) - specify one with --advertise-addr
$ docker swarm init --force-new-cluster --advertise-addr 192.168.99.102
Error response from daemon: This node is not a swarm manager. Worker nodes can't be used to view or modify cluster state. Please run this command on a manager node or promote the current node to a manager.
$ docker node ls
卡死
$ docker-machine restart manager1 
重启不了，一直转圈
```

没办法，后来我只能删掉四个 Docker 主机，重新进行创建了。

## Docker Service 部署单个集群服务

在部署集群服务之前，我们需要做些准备工作，因为 Docker 主机中没有配置 Docker 镜像加速地址，所以在拉取官方镜像的时候，肯定会非常慢，除了配置 Docker 镜像加速地址之外，我们还可以使用 Docker 私有镜像仓库，来解决这个问题。

```
$ docker run -d -v /Users/xishuai/Documents/Docker:/var/lib/registry -p 5000:5000 --restart=always --name registry registry

$ docker tag nginx 192.168.99.1:5000/nginx:latest && 
docker push 192.168.99.1:5000/nginx:latest && 
docker pull 192.168.99.1:5000/nginx:latest

$ curl http://192.168.99.1:5000/v2/_catalog
{"repositories":["nginx"]}
```

我们在 Mac OS 上创建了一个私有仓库容器，并把`nginx`镜像放到私有仓库中，因为没有使用 Https，所以在拉取和推送镜像的时候，会报如下错误（Mac OS 和 Docker 主机都会报错）：

```
$ docker pull 192.168.99.1:5000/nginx:latest
The push refers to a repository [192.168.99.1:5000/nginx]
Get https://192.168.99.1:5000/v1/_ping: http: server gave HTTP response to HTTPS client
```

解决方式，我们需要分别在四个 Docker 主机中添加配置（Docker for Mac 在管理界面配置即可），命令：

```
$ sudo touch /etc/docker/daemon.json && 
sudo chmod 777 /etc/docker/daemon.json && 
sudo echo '{ "insecure-registries":    ["192.168.99.1:5000"] }' > /etc/docker/daemon.json
```

然后重启四个 Docker 主机（Docker for Mac 也需要重启），命令：

```
$ docker-machine restart manager1 && 
docker-machine restart manager2 && 
docker-machine restart worker1 && 
docker-machine restart worker2
```

上面比较啰嗦，我们接下来正式部署集群服务，还是拿`nginx`镜像做为示例，命令（`docker service create`命令[详细说明](http://www.yiibai.com/docker/service_create.html)）：` docker-machine ssh manager1 docker service create --replicas 4 -p 8088:80 --name nginx nginx:latest`

```
$ docker service create --replicas 4 -p 8088:80 --name nginx 192.168.99.1:5000/nginx:latest
ap8h8srb8yh3mni0h2nz61njz
overall progress: 4 out of 4 tasks 
1/4: running   [==================================================>] 
2/4: running   [==================================================>] 
3/4: running   [==================================================>] 
4/4: running   [==================================================>] 
verify: Service converged 
```

需要注意的是，`--replicas 4`表示创建服务的实例个数（默认是一个），啥意思？比如4，就是在四个 Docker 主机上，分别创建一个`nginx`服务，如果是3，那就是三个 Docker 主机，或者你可以理解为 Docker 主机的个数，另外，`REPLICAS`会有进度显示，并且执行是异步的。

我们也可以手动设置实例个数，命令：

```
$ docker service scale nginx=4
```

部署好服务后，我们就可以进行查看了，命令：

下面的示例中期望的副本数是4，不过副本运行的任务数是3:

一旦创建了所有的任务且为RUNNING状态，任务的实际数量就等于期望的数量：

```
PS C:\Users\789> docker-machine ssh manager1 docker service ls
ID                  NAME                MODE                REPLICAS            IMAGE               PORTS
jorbaww6zd5k        nginx               replicated          3/4                 nginx:latest        *:8088->80/tcp

PS C:\Users\789> docker-machine ssh manager1 docker service ps nginx
ID                  NAME                IMAGE               NODE                DESIRED STATE       CURRENT STATE              ERROR               PORTS
9l783ncbw037        nginx.1             nginx:latest        worker1             Running             Running 22 minutes ago
qjepzpsf98ru        nginx.2             nginx:latest        worker2             Running             Preparing 26 minutes ago
nmqkktlxgi9e        nginx.3             nginx:latest        manager2            Running             Running 22 minutes ago
wvnobh8c7siv        nginx.4             nginx:latest        manager1            Running             Running 23 minutes ago
```

我们任意使用四个 Docker 主机中的一个 IP 地址，浏览器打开：<http://192.168.99.100:8088/>



## Docker Stack 部署多个集群服务，以及 GUI 管理页面

`docker service`部署的是单个服务，我们可以使用`docker stack`进行多服务编排部署，使用的同样是`docker-compose.yml`配置文件，示例：

```
version: "3"

services:
  nginx:
    image: 192.168.99.1:5000/nginx:latest
    ports:
      - 8088:80
    deploy:
      mode: replicated
      replicas: 4

  visualizer:
    image: 192.168.99.1:5000/dockersamples/visualizer:latest
    ports:
      - "8080:8080"
    volumes:
      - "/var/run/docker.sock:/var/run/docker.sock"
    deploy:
      replicas: 1
      placement:
        constraints: [node.role == manager]

  portainer:
    image: 192.168.99.1:5000/portainer/portainer:latest
    ports:
      - "9000:9000"
    volumes:
      - "/var/run/docker.sock:/var/run/docker.sock"
    deploy:
      replicas: 1
      placement:
        constraints: [node.role == manager]
```

如上所示，我们总共需要部署三个服务，出了`nginx`服务作为示例之外，`visualizer`（[官方地址](https://github.com/dockersamples/docker-swarm-visualizer)）和`portainer`（[官方地址](https://portainer.io/)）都是集群 GUI 管理服务。

部署命令：

```
$ docker stack deploy -c docker-compose.yml deploy-demo
Creating service deploy-demo_nginx
Creating service deploy-demo_visualizer
Creating service deploy-demo_portainer
```

部署成功之后，我们可以查看具体详情，命令：

```
$ docker stack ls
NAME                SERVICES
deploy-demo         3
```

查看`visualizer`GUI 集群管理，浏览器打开：<http://192.168.99.100:8080/>

查看`portainer`GUI 集群管理，需要先配置账号信息，浏览器打开：<http://192.168.99.100:9000/>

可以看到，`portainer`比`visualizer`强大太多了，甚至我们所有的操作都可以在`portainer`上完成。



## 添加节点

在manager上执行

```
PS C:\Users\789> docker-machine ssh manager1 docker swarm join-token worker
To add a worker to this swarm, run the following command:

    docker swarm join --token SWMTKN-1-44vqx042ok1m3gqg24vaeb717ftl37i2tey910v1sptm806t4w-4xwtu34y0mh5msacou8nofl8x 192.168.99.100:237

PS C:\Users\789>
```

在需要加入的主机或者ssh登陆主机docker-machine ssh wk01 执行上一步返回的token

```
PS C:\Users\789> docker-machine ssh wk01 "docker swarm join --token SWMTKN-1-44vqx042ok1m3gqg24vaeb717ftl37i2tey910v1sptm806t4w-4xwtu34y0mh5msacou8nofl8x 192.168.99.100:2377"
Error response from daemon: error while validating Root CA Certificate: x509: certificate has expired or is not yet valid
exit status 1
```

出现上面的错误：wk01上：

```
rm -rf /etc/localtime
ln -s /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

ntpdate time.nuri.net 或
ntpdate -u 211.115.194.21
```

上面加入到集群后执行

```
docker service scale nginx=4
```





## 5.  常用命令

### docker-machine 常用命令

| 命令                                           | 说明                                        |
| ---------------------------------------------- | ------------------------------------------- |
| docker-machine create 参数  --update-delay 10s | 创建一个 Docker 主机（常用`-d virtualbox`） |
| docker-machine ls                              | 查看所有的 Docker 主机                      |
| docker-machine ssh                             | SSH 到主机上执行命令                        |
| docker-machine env                             | 显示连接到某个主机需要的环境变量            |
| docker-machine inspect                         | 输出主机更多信息                            |
| docker-machine kill                            | 停止某个主机                                |
| docker-machine restart                         | 重启某台主机                                |
| docker-machine rm                              | 删除某台主机                                |
| docker-machine scp                             | 在主机之间复制文件                          |
| docker-machine start                           | 启动一个主机                                |
| docker-machine status                          | 查看主机状态                                |
| docker-machine stop                            | 停止一个主机                                |

确认publish 参数？

docker service create --name nginx  --replicas 2 --publish 80:80 hub.test.com:5000/almi/nginx:0.1



### docker swarm 常用命令

| 命令                            | 说明                 |
| ------------------------------- | -------------------- |
| docker swarm init               | 初始化集群           |
| docker swarm join-token worker  | 查看工作节点的 token |
| docker swarm join-token manager | 查看管理节点的 token |
| docker swarm join               | 加入集群中           |

docker swarm init

docker swarm join-token manager …… #添加manager节点

多ip需要指定ip地址

docker swarm init --listen-addr 172.16.50.21:2377 --advertise-addr 172.16.50.21

### docker node 常用命令

| 命令                                      | 说明                               |
| ----------------------------------------- | ---------------------------------- |
| docker node ls                            | 查看所有集群节点                   |
| docker node rm                            | 删除某个节点（`-f`强制删除）       |
| docker node inspect                       | 查看节点详情                       |
| docker node demote                        | 节点降级，由管理节点降级为工作节点 |
| docker node promote                       | 节点升级，由工作节点升级为管理节点 |
| docker node update                        | 更新节点                           |
| docker node ps                            | 查看节点中的 Task 任务             |
| docker node update --role manager worker1 | 工作节点变为manager                |
| docker node update --role worker manager1 | manager点变为worker                |

### docker service 常用命令

| 命令                                                  | 说明                         |
| ----------------------------------------------------- | ---------------------------- |
| docker service create                                 | 部署服务                     |
| docker service inspect tomcat  --pretty               | 查看服务详情                 |
| docker service logs                                   | 产看某个服务日志             |
| docker service ls                                     | 查看所有服务详情             |
| docker service rm tomcat                              | 删除某个服务（`-f`强制删除） |
| docker service scale tomcat=3                         | 设置某个服务个数             |
| docker service update                                 | 更新某个服务                 |
| docker-machine ssh manager1  docker service ps tomcat | 查看哪个节点在运行服务       |

docker service update –publish-add 80 my_web

> publish-add参数指添加或者更新一个对外端口 
> image参数指更新镜像 
> hostname 更新或指定容器名称 
> force 指强制更新，即使本次更新没有任何改变



### docker stack 常用命令

| 命令                  | 说明                       |
| --------------------- | -------------------------- |
| docker stack deploy   | 部署新的堆栈或更新现有堆栈 |
| docker stack ls       | 列出现有堆栈               |
| docker stack ps       | 列出堆栈中的任务           |
| docker stack rm       | 删除堆栈                   |
| docker stack services | 列出堆栈中的服务           |
| docker stack down     | 移除某个堆栈（不会删除数据 |

## 开启Docker API服务

如要使用swarm，则必须让Docker开放其HTTP的API。默认情况下这个API没有开启，而开启此API需要在启动时加入-H参数。修改lib/systemd/system/docker.service文件中的参数，并且用systemctl来管理启动docker服务。

上述这个文件的ExecStart很明显是指出了docker的启动参数，在第一行的后面直接加上：

```
-H tcp://0.0.0.0:2376
```

```
systemctl daemon-reload
```

​    然后再重启/启动Docker服务，此时通过netstat -ntlp可以看到一个新开的2376端口，此乃默认的 DockerHTTPAPI的端口。如果是一个集群则注意集群中所有相关的主机都记得要启动带这个端口的Docker服务。

从 Docker 1.12.0 版本开始，Docker Swarm 已经包含在 Docker 引擎中（`docker swarm`），并且已经内置了服务发现工具，我们就不需要像之前一样，再配置 Etcd 或者 [Consul](https://www.ibm.com/developerworks/cn/opensource/os-cn-consul-docker-swarm/index.html) 来进行服务发现配置了。 

## 命令

|                    |                                                              |
| ------------------ | ------------------------------------------------------------ |
| create             | docker service create –replicas 5 –name myhelloworld alpine ping docker.com |
| 查看创建出来的服务 | docker service ls                                            |
|                    | docker service update                                        |
|                    | docker service inspect                                       |
|                    | docker service ps  my_web                                    |
|                    | docker service rm my_web                                     |
| 扩展一个或多个服务 | docker service scale webtier_nginx=5                         |

### docker service logs

针对docker swarm模式，获取容器日志的命令。

一般，依次执行下列命令，得到某服务的容器名

```
docker service ls
docker service ps [服务名]12
```

然后就可以通过容器名，获取其日志了

```
docker service logs [容器名]1
```

### docker service logs显示日志为空

要让 docker service logs 正常工作，需要设置docker一些配置

```
vi /etc/docker/daemon.json1
```

给该文件添加：

```
{
    "log-driver": "json-file",
    "log-opts": {
        "labels": "production_status,geo",
        "env": "os,customer"
    }
}
```

然后重启docker



## 参考文档

https://www.cnblogs.com/franknihao/p/8490416.html

http://www.cnblogs.com/xishuai/p/docker-swarm.html

https://www.cnblogs.com/drawnkid/p/8487337.html

https://www.jianshu.com/p/9eb9995884a5

https://www.cnblogs.com/adolfmc/category/1027938.html

汇总

https://www.centos.bz/tag/docker/page/45/



https://www.cnblogs.com/liuyansheng/p/8178341.html