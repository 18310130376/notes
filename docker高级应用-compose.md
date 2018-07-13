#### Docker之Compose服务编排

https://www.cnblogs.com/52fhy/p/5991344.html

https://www.cnblogs.com/linjiqin/p/8849432.html

目的：docker-compose 你可以把所有繁复的 docker 操作全都一条命令，自动化的完成。

###### 安装Compose 

```
# 方法一：
$ curl -L https://github.com/docker/compose/releases/download/1.8.1/docker-compose-`uname -s`-`uname -m` > /usr/local/bin/docker-compose
$ chmod +x /usr/local/bin/docker-compose

# Linux下等效于
$ curl -L https://github.com/docker/compose/releases/download/1.8.1/docker-compose-Linux-x86_64 > /usr/local/bin/docker-compose; chmod +x /usr/local/bin/docker-compose

# 方法二：使用pip安装，版本可能比较旧
$ yum install python-pip python-dev
$ pip install docker-compose

# 方法三：作为容器安装
$ curl -L https://github.com/docker/compose/releases/download/1.8.0/run.sh > /usr/local/bin/docker-compose
$ chmod +x /usr/local/bin/docker-compose

# 方法四：离线安装
# 下载[docker-compose-Linux-x86_64](https://github.com/docker/compose/releases/download/1.8.1/docker-compose-Linux-x86_64)，然后重新命名添加可执行权限即可：
$ mv docker-compose-Linux-x86_64 /usr/local/bin/docker-compose;
$ chmod +x /usr/local/bin/docker-compose
# 百度云地址： http://pan.baidu.com/s/1slEOIC1 密码: qmca
# docker官方离线地址：https://dl.bintray.com/docker-compose/master/
```

安装完成后可以查看版本： 

```
# docker-compose --version
docker-compose version 1.8.1, build 878cff1

提示说执行被拒绝。 所以要给文件添加执行权限。
$ sudo chmod +x /usr/local/bin/docker-compose
```

升级

如果你使用的是 Compose 1.2或者早期版本，当你升级完成后，你需要删除或者迁移你现有的容器。这是因为，1.3版本， Composer 使用 Docker 标签来对容器进行检测，所以它们需要重新创建索引标记。

卸载

```
$ rm /usr/local/bin/docker-compose

# 卸载使用pip安装的compose
$ pip uninstall docker-compose
```

Compose区分Version 1和Version 2（Compose 1.6.0+，Docker Engine 1.10.0+）。Version 2支持更多的指令。Version 1没有声明版本默认是"version 1"。Version 1将来会被弃用。

版本1指的是忽略`version`关键字的版本；版本2必须在行首添加`version: '2'`。

###### bash 补全命令

```
$ curl -L https://raw.githubusercontent.com/docker/compose/1.8.0/contrib/completion/bash/docker-compose > /etc/bash_completion.d/docker-compose
chmod +x /etc/bash_completion.d/docker-compose

卸载
sudo rm /etc/bash_completion.d/docker-compose
```



##### 常见问题：

1：application.yml文件键值之间要用冒号：隔开，而且冒号和值之间有一个空格，否则就报上面的错误

​     错误写法：

​        enableWebDubug:true

​    正确写法：

​       enableWebDubug: true







##### 入门示例

一般步骤

1、定义Dockerfile，方便迁移到任何地方；
2、编写docker-compose.yml文件；
3、运行`docker-compose up`启动服务

示例

准备工作：提前下载好镜像：

```
docker pull mysql
docker pull wordpress
```

一个docker-compose.yml包含image或者build，image会使用存在的镜像，不存在直接会pull镜像，build则指定Dockerfile进行构建镜像

需要新建一个空白目录，例如wptest。新建一个docker-compose.yml

```
version: '2'
services:
    web: 
      image: wordpress:latest 
    //build: .    //web会使用当前目录中的Dockerfile文件构建镜像
   // build:
      // context: ./dir   //webapp服务将会通过./dir目录下的Dockerfile-alternate文件构建容器镜像。
      // dockerfile: Dockerfile-alternate
      links: 
        - db
      ports: 
        - "8002:80"
      environment:
        WORDPRESS_DB_HOST: db:3306
        WORDPRESS_DB_PASSWORD: 123456
    db: 
      image: mysql 
      environment: 
        - MYSQL_ROOT_PASSWORD=123456
```

以上命令的意思是新建db和wordpress容器。等同于：

```
$ docker run --name db -e MYSQL_ROOT_PASSWORD=123456 -d mysql
$ docker run --name some-wordpress --link db:mysql -p 8002:80 -d wordpress
```

```
注意，如果你是直接从fig迁移过来的，且web里links是- db:mysql，这里会提示没有给wordpress设置环境变量，这里需要添加环境变量WORDPRESS_DB_HOST和WORDPRESS_DB_PASSWORD。
```

好，我们启动应用：

```
# docker-compose up
Creating wptest_db_1...
Creating wptest_wordpress_1...
Attaching to wptest_db_1, wptest_wordpress_1
wordpress_1 | Complete! WordPress has been successfully copied to /var/www/html
```

就成功了。浏览器访问 [http://localhost:8002（或](http://localhost:8002%EF%BC%88%E6%88%96/) [http://host-ip:8002）即可](http://host-ip:8002%EF%BC%89%E5%8D%B3%E5%8F%AF/)。

默认是前台运行并打印日志到控制台。如果想后台运行，可以：

```
docker-compose up -d
```

创建并启动：docker-compose up -d  ：默认执行当前路径下的docker-compose.yml，可以使用-f filename.yml

docker-compose  -f  docker-compose.yml up  -d 

服务后台后，可以使用下列命令查看状态：

```
# docker-compose ps
       Name                      Command               State          Ports         
-----------------------------------------------------------------------------------
figtest_db_1          docker-entrypoint.sh mysqld      Up      3306/tcp             
figtest_wordpress_1   docker-entrypoint.sh apach ...   Up      0.0.0.0:8002->80/tcp 

# docker-compose logs
Attaching to wptest_wordpress_1, wptest_db_1
db_1        | 2016-10-4T14:38:46.98030Z 0 [Warning] TIMESTAMP with implicit DEFAULT value is deprecated. Please use --explicit_defaults_for_timestamp server option (see documentation for more details).
db_1        | 2016-10-4T14:38:46.99974Z 0 [Note] mysqld (mysqld 5.7.15) starting as process 1 ...
db_1        | 2016-10-4T14:38:46.27191Z 0 [Note] InnoDB: PUNCH HOLE support available
```

停止服务：

```
# docker-compose stop
Stopping wptest_wordpress_1...
Stopping wptest_db_1...
```

重新启动服务：

```
docker-compose restart
```



##### 示例二

```
version: '2'
services:
  webapp:
    image: <your_web_app_here>:latest
    ports:
      - "80:80"
    depends_on:
      - postgres
      - memcached
    volumes:
     - /path/to/my/code/on/my/dev/machine:/path/to/the/code/in/the/container
    env_file: env_vars/webapp_env_vars.env

  memcached:
    image: memcached:latest

  postgres:
    image: postgres:9.3
```

**env_vars/webapp_env_vars.env**

```
DB_HOST=postgres
DB_PORT=5432
DB_USER=some_cool_dev
DB_PASSWORD=OMG_PLS_K33P_4_S3CRET
MEMCACHED_HOST=memcached
MEMCACHED_PORT=11211
SOME_VARIABLE_MY_WEBAPP_READS=a_value_i_use_to_configure_it
```

##### 示例三mysql

**1:run方式启动mysql容器**

```
sudo docker run -d -p 3306:3306 --name dbmysql -e MYSQL_ROOT_PASSWORD=password -v /mysql/datadir:/var/lib/mysql -v /mysql/conf:/etc/mysql/conf.d  docker.io/mysql:latest  

-e MYSQL_ROOT_PASSWORD=password ：指定root密码  
-v /mysql/datadir:/var/lib/mysql ：指定数据库本地存储路径，如果系统没有关闭SELinux，会启动失败，原因是本地目录不允许挂载到容器，需要先执行chcon -Rt svirt_sandbox_file_t /mysql/datadir  
-v /mysql/conf:/etc/mysql/conf.d ：指定使用自定义的mysql配置文件启动数据库，比如在该路径下创建一个my-config.cnf 
  
vi my-config.cnf  
[mysqld]  
port=3306  
character-set-server=utf8  
wait_timeout=288000  # 链接超时，默认为8小时，单位为秒  
lower_case_table_names=1 # 不去分大小写
```

**2:docker-compose方式**

```
mkdir -p /docker-compose-dir/mysql
cd /docker-compose-dir/mysql
vi docker-compose.yml
```

```
dbmysql:  
  image: docker.io/mysql:latest  
  ports:  
    - 3306:3306  
  environment:  
    MYSQL_ROOT_PASSWORD: password 
    MYSQL_DATABASE: 
    MYSQL_USER: 
    MYSQL_PASSWORD: 
  volumes:  
    - /mysql/datadir:/var/lib/mysql  
    - /mysql/conf:/etc/mysql/conf.d:ro
```

```
[root@centos-linux-agent mysql]# docker-compose up -d  
Creating mysql_dbmysql_1  
[root@centos-linux-agent mysql]# docker ps -a  
CONTAINER ID        IMAGE                    COMMAND                  CREATED             STATUS              PORTS                    NAMES  
087f4e32cd29        docker.io/mysql:latest   "docker-entrypoint.sh"   4 seconds ago       Up 3 seconds        0.0.0.0:3306->3306/tcp   mysql_dbmysql_1  
```

**数据库脚本初始化**

有时候，需要先执行数据库初始化脚本才能运行项目容器，可以采用如下方式：

脚本路径：/mysql_sql/mysql.sql

```
sudo docker run -it --rm --link=mysql_dbmysql_1:db  -v /mysql_sql:/mysql_sql mysql:latest mysql -hdb -uroot -ppassword -Ddbname  
```

说明：如果脚本中包含建库语句，则可以去掉-D参数

进入容器后执行：

mysql> source /mysql_sql/mysql.sql;

mysql> exit

退出容器后，容器将自动删除。



##### 示例四**tomcat**

**下载镜像：**

sudo docker pull tomcat

目前最新版为8.0.X，可以通过上面的官方地址，找到需要的jre+tomcat的tag版本

**run方式启动容器：**

sudo docker run -d -p 8080:8080 -v /usr/local/tomcat  tomcat:latest

查看容器中的tomcat目录被挂载到本机的路径：

```
sudo docker ps 

CONTAINER ID IMAGE COMMAND CREATED STATUS PORTS NAMES  
ef993d0f48c1 tomcat:latest "catalina.sh run"22 minutes ago Up9 minutes 0.0.0.0:8080->8080/tcp goofy_blackwell  
```

```
sudo docker inspect -f "{{.Mounts}}" ef9
```

可以看到：

/var/lib/docker/volumes/bbc41e0bbc97ae32839e876f291eb60173b6b1fc2867dd11a5381f85a85b8a59/_data

然后可以修改conf下的配置文件，将war包拷贝到webapps下，等等，修改后要重启容器：sudo docker restart ef9

也可以直接映射宿主机上的war包路径：

**sudo docker run -d -p 8080:8080 -v /tomcat/webapps:/usr/local/tomcat/webapps  tomcat:latest**

这样，只需要将war包拷贝到宿主机的/tomcat/webapps下即可。

这样做和在本机使用tomcat并没有实际的区别，它的优势就是宿主机不需要安装jdk和tomcat。



*docker-compose方式*

可以使用docker-compose一次启动多个tomcat容器，并建立起他们之间的关联关系。

```
mkdir -p /docker-compose-dir/tomcat
cd /docker-compose-dir/tomcat
vi docker-compose.yml
```

```
version: '2'  
services:  
   db:  
     container_name: mysql01 
     image: docker.io/mysql:latest  
     volumes:  
       - /mysql/datadir_tomcat:/var/lib/mysql  
       - /mysql/conf:/etc/mysql/conf.d:ro  
     restart: always  
     environment:  
       MYSQL_ROOT_PASSWORD: password  
         
   tomcat01:  
     depends_on:  
       - db  
     container_name: tomcat01  
     image: docker.io/tomcat:latest 
     environment:
       TZ: Asia/Shanghai
       JAVA_OPTS: -Xmx512m
     volumes:  
       - /tomcat01/webapps:/usr/local/tomcat/webapps  
     links:  
       - db:db  
     ports:  
       - "8081:8080"  
     restart: always  
  
   tomcat02:  
     depends_on:  
       - db  
     container_name: tomcat01  
     image: docker.io/tomcat:latest 
     environment:
       TZ: Asia/Shanghai
       JAVA_OPTS: -Xmx512m
     volumes:  
       - /tomcat02/webapps:/usr/local/tomcat/webapps  
     links:  
       - db:db  
       - tomcat01:tomcat01  
     ports:  
       - "8082:8080"  
     restart: always 
```

```
docker-compose up -d
```

上面这种方式，随便进入任何一个容器，执行ping命令，都可以互相ping通

ping tomcat01

ping db

但是查看各自的/etc/hosts，却看不到相应的配置

所以war里使用上面的链接别名配置好互相要访问的地址，然后拷贝到对应的部署路径下，并重启。

```
docker-compose restart
```



也可以使用Dockerfile，将war包等直接封装为一个新的镜像。

```
sudo mkdir /dockerfile
sudo vi Dockerfile
```

```
FROM tomcat:latest  
MAINTAINER "hanqunfeng <hanqf2008@163.com>"  
ADD web.war /usr/local/tomcat/webapps/  
```

将web.war拷贝到当前路径下

生成镜像：sudo docker build -t web/tomcat8 .  ：注意最后面那个点，代表当前路径

启动：sudo run -p 8080:8080 -d web/tomcat8 

**docker-compose**

```
tomcat01:  
     depends_on:  
       - db  
     build: /dockerfile  
     links:  
       - db:db  
     ports:  
       - "8081:8080"  
     restart: always  
```

docker-compose up -d ：第一次执行会自动创建一个镜像，并启动容器

如果该镜像已经被创建了，再次执行时要加上--build：docker-compose up --build -d，此时会重新创建该镜像



#### mysql、tomcat、nginx部署

https://blog.csdn.net/xiaoxiangzi520/article/details/78775503



##### docker-compose.yml参考

每个docker-compose.yml必须定义`image`或者`build`中的一个，其它的是可选的。

###### image

指定为镜像名称或镜像 ID。如果镜像在本地不存在，Compose 将会尝试拉去这个镜像。

指定镜像tag或者ID。示例：

```
image: redis
image: ubuntu:14.04
image: tutum/influxdb
image: example-registry.com:4000/postgresql
image: a4bc65fd
```

注意，在`version 1`里同时使用`image`和`build`是不允许的，`version 2`则可以，如果同时指定了两者，会将`build`出来的镜像打上名为`image`标签。

###### build

如果使用 build 指令，在 Dockerfile 中设置的选项(例如：CMD, EXPOSE, VOLUME, ENV 等) 将会自动被获取，无需在 docker-compose.yml 中再次设置。

用来指定一个包含`Dockerfile`文件的路径。一般是当前目录`.`。Fig将build并生成一个随机命名的镜像。

注意，在`version 1`里`bulid`仅支持值为字符串。`version 2`里支持对象格式。

```
build: ./dir

build:
  context: ./dir
  dockerfile: Dockerfile-alternate
  args:
    buildno: 1
```

`context`为路径，`dockerfile`为需要替换默认`docker-compose`的文件名，`args`为构建(build)过程中的环境变量，用于替换Dockerfile里定义的`ARG`参数，容器中不可用。示例：
Dockerfile:

```
ARG buildno
ARG password

RUN echo "Build number: $buildno"
RUN script-requiring-password.sh "$password"
```

docker-compose.yml:

```
build:
  context: .
  args:
    buildno: 1
    password: secret

build:
  context: .
  args:
    - buildno=1
    - password=secret
```

###### command

用来覆盖缺省命令。示例：

```
command: bundle exec thin -p 3000
```

`command`也支持数组形式：

```
command: [bundle, exec, thin, -p, 3000]
```

###### links

用于链接另一容器服务，如需要使用到另一容器的mysql服务。可以给出服务名和别名；也可以仅给出服务名，这样别名将和服务名相同。同`docker run --link`。示例：

```
links:
 - db
 - db:mysql
 - redis
```

使用了别名将自动会在容器的`/etc/hosts`文件里创建相应记录：

```
172.17.2.186  db
172.17.2.186  mysql
172.17.2.187  redis
```

所以我们在容器里就可以直接使用别名作为服务的主机名。

###### ports

用于暴露端口。同`docker run -p`。示例：

```
ports:
 - "3000"
 - "8000:8000"
 - "49100:22"
 - "127.0.0.1:8001:8001"
```

###### expose

expose提供container之间的端口访问，不会暴露给主机使用。同`docker run --expose`。

```
expose:
 - "3000"
 - "8000"
```

###### volumes

挂载数据卷。同`docker run -v`。示例：

```
volumes:
 - /var/lib/mysql
 - cache/:/tmp/cache
 - ~/configs:/etc/configs/:ro
```

###### volumes_from

挂载数据卷容器，挂载是容器。同`docker run --volumes-from`。示例：

```
volumes_from:
 - service_name
 - service_name:ro
 - container:container_name
 - container:container_name:rw
```

`container:container_name`格式仅支持`version 2`。

###### environment

添加环境变量。同`docker run -e`。可以是数组或者字典格式：

```
environment:
  RACK_ENV: development
  SESSION_SECRET:

environment:
  - RACK_ENV=development
  - SESSION_SECRET
```

###### depends_on

用于指定服务依赖，一般是mysql、redis等。
指定了依赖，将会优先于服务创建并启动依赖。

`links`也可以指定依赖。



###### external_links

链接搭配`docker-compose.yml`文件或者`Compose`之外定义的服务，通常是提供共享或公共服务。格式与`links`相似：

```
external_links:
 - redis_1
 - project_db_1:mysql
 - project_db_1:postgresql
```

注意，`external_links`链接的服务与当前服务必须是同一个网络环境。

###### extra_hosts

添加主机名映射。

```
extra_hosts:
 - "somehost:162.242.195.82"
 - "otherhost:50.31.209.229"
```

将会在`/etc/hosts`创建记录：

```
162.242.195.82  somehost
50.31.209.229   otherhost
```

###### extends

继承自当前yml文件或者其它文件中定义的服务，可以选择性的覆盖原有配置。

```
extends:
  file: common.yml
  service: webapp
```

`service`必须有，`file`可选。`service`是需要继承的服务，例如`web`、`database`。

###### net

设置网络模式。同docker的`--net`参数。

```
net: "bridge"
net: "none"
net: "container:[name or id]"
net: "host"
```

###### dns

自定义dns服务器。

```
dns: 8.8.8.8
dns:
  - 8.8.8.8
  - 9.9.9.9
```

cpu_shares, cpu_quota, cpuset, domainname, hostname, ipc, mac_address, mem_limit, memswap_limit, privileged, read_only, restart, shm_size, stdin_open, tty, user, working_dir

这些命令都是单个值，含义请参考[docker run](https://www.cnblogs.com/52fhy/p/Docker%20run%20reference%20-%20Docker%20https://docs.docker.com/engine/reference/run/)。

```
cpu_shares: 73
cpu_quota: 50000
cpuset: 0,1

user: postgresql
working_dir: /code

domainname: foo.com
hostname: foo
ipc: host
mac_address: 02:42:ac:11:65:43

mem_limit: 1000000000
mem_limit: 128M
memswap_limit: 2000000000
privileged: true

restart: always

read_only: true
shm_size: 64M
stdin_open: true
tty: true
```

###### 命令行参考

|                                                              |                                                              |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| docker-compose stop wordpress                                | 停止容器运行                                                 |
| docker-compose rm wordpress                                  | 删除老旧的容器                                               |
| docker-compose start wordpress                               | 启动新容器                                                   |
| docker-compose restart nginx                                 |                                                              |
| docker-compose up  【 -d nginx】                             | 构建建启动nignx容器                                          |
| docker-compose logs nginx                                    |                                                              |
| docker-compose logs -f --tail 10 nginx                       |                                                              |
| docker-compose build                                         | build 构建或重建服务                                         |
| docker-compose kill                                          | kill 杀掉容器                                                |
| docker-compose logs                                          | logs 显示容器的输出内容                                      |
| docker-compose port                                          | port 打印绑定的开放端口                                      |
| docker-compose ps                                            | ps 显示容器                                                  |
| docker-compose -f docker-compose-tomcat.yml ps               |                                                              |
| docker-compose pull                                          | pull 拉取服务镜像                                            |
| docker-compose restart   \|\|     docker-compose -f docker-compose-tomcat-only.yml restart | restart 重启服务                                             |
| docker-compose rm  \|\| docker-compose -f docker-compose-tomcat-only.yml rm | rm 删除停止的容器                                            |
| docker-compose run container_name bash                       | run 运行一个一次性命令                                       |
| docker-compose  scale                                        | scale 设置服务的容器数目                                     |
| docker-compose start                                         | start 开启服务                                               |
| docker-compose stop                                          | stop 停止服务                                                |
| docker-compose exec nginx bash                               | 登录到nginx容器中                                            |
| docker-compose down                                          | 删除所有nginx容器,镜像                                       |
| docker-compose config  -q                                    | 验证（docker-compose.yml）文件配置，当配置正确时，不输出任何内容，当文件配置错误，输出错误信息。 |
| docker-compose events --json nginx                           | 以json的形式输出nginx的docker日志                            |
| docker-compose pause nginx                                   | 暂停nignx容器                                                |
| docker-compose unpause nginx                                 | 恢复ningx容器                                                |









```
$ docker-compose
Define and run multi-container applications with Docker.

Usage:
  docker-compose [-f <arg>...] [options] [COMMAND] [ARGS...]
  docker-compose -h|--help

Options:
  -f, --file FILE             Specify an alternate compose file (default: docker-compose.yml)
  -p, --project-name NAME     Specify an alternate project name (default: directory name)
  --verbose                   Show more output
  -v, --version               Print version and exit
  -H, --host HOST             Daemon socket to connect to

  --tls                       Use TLS; implied by --tlsverify
  --tlscacert CA_PATH         Trust certs signed only by this CA
  --tlscert CLIENT_CERT_PATH  Path to TLS certificate file
  --tlskey TLS_KEY_PATH       Path to TLS key file
  --tlsverify                 Use TLS and verify the remote
  --skip-hostname-check       Don't check the daemon's hostname against the name specified
                              in the client certificate (for example if your docker host
                              is an IP address)

Commands:
  build              Build or rebuild services
  bundle             Generate a Docker bundle from the Compose file
  config             Validate and view the compose file
  create             Create services
  down               Stop and remove containers, networks, images, and volumes
  events             Receive real time events from containers
  exec               Execute a command in a running container
  help               Get help on a command
  kill               Kill containers
  logs               View output from containers
  pause              Pause services
  port               Print the public port for a port binding
  ps                 List containers
  pull               Pulls service images
  push               Push service images
  restart            Restart services
  rm                 Remove stopped containers
  run                Run a one-off command
  scale              Set number of containers for a service
  start              Start services
  stop               Stop services
  unpause            Unpause services
  up                 Create and start containers
  version            Show the Docker-Compose version information
```

###### 批处理脚本

```
# 关闭所有正在运行容器
docker ps | awk  '{print $1}' | xargs docker stop

# 删除所有容器应用
docker ps -a | awk  '{print $1}' | xargs docker rm
# 或者
docker rm $(docker ps -a -q)
```



##### Docker Compose命令详解

1.Docker compose的使用非常类似于docker命令的使用，**但是需要注意的是大部分的compose命令都需要到docker-compose.yml文件所在的目录下才能执行。**

2.compose以守护进程模式运行加-d选项
$ docker-compose up -d

3.查看有哪些服务，使用docker-compose ps命令，非常类似于 docker 的ps命令
![img](https://images2018.cnblogs.com/blog/270324/201804/270324-20180415205606207-540673302.png)

4.查看compose日志
$ docker-compose logs web
$ docker-compose logs redis

5.停止compose服务
$ docker-compose stop
$ docker-compose ps
![img](https://images2018.cnblogs.com/blog/270324/201804/270324-20180415205623207-2111865747.png)
看到服务的状态为Exit退出状态

6.重启compose服务
$ docker-compose restart
$ docker-compose ps
![img](https://images2018.cnblogs.com/blog/270324/201804/270324-20180415205633966-1901827040.png)

7.kill compose服务
$ docker-compose kill
$ docker-compose ps
![img](https://images2018.cnblogs.com/blog/270324/201804/270324-20180415205646050-800416755.png)
状态码为137

8.删除compose服务
$ docker-compose rm
![img](https://images2018.cnblogs.com/blog/270324/201804/270324-20180415205700598-234241997.png)



##### Docker Compose 配置文件详解

```
version: '2'
services:
  web:
    image: dockercloud/hello-world
    restart: always
    ports:
      - 8080
    networks:
      - front-tier
      - back-tier

  redis:
    image: redis
    links:
      - web
    networks:
      - back-tier

  lb:
    image: dockercloud/haproxy
    ports:
      - 80:80
    links:
      - web
    networks:
      - front-tier
      - back-tier
    volumes:
      - /var/run/docker.sock:/var/run/docker.sock 

networks:
  front-tier:
    driver: bridge
  back-tier:
driver: bridge
```

可以看到一份标准配置文件应该包含 version、services、networks 三大部分，其中最关键的就是 services 和 networks 两个部分，下面先来看 services 的书写规则。



###### 1. image

指定为镜像名称或镜像 ID。如果镜像在本地不存在，Compose 将会尝试拉去这个镜像。

```
services:
  web:
    image: hello-world
```

在 services 标签下的第二级标签是 web，这个名字是用户自己自定义，它就是服务名称。
image 则是指定服务的镜像名称或镜像 ID。如果镜像在本地不存在，Compose 将会尝试拉取这个镜像。
例如下面这些格式都是可以的：

```
image: redis
image: ubuntu:14.04
image: tutum/influxdb
image: example-registry.com:4000/postgresql
image: a4bc65fd
```

###### 2. build

如果使用 build 指令，在 Dockerfile 中设置的选项(例如：CMD, EXPOSE, VOLUME, ENV 等) 将会自动被获取，无需在 docker-compose.yml 中再次设置。

服务除了可以基于指定的镜像，还可以基于一份 Dockerfile，在使用 up 启动之时执行构建任务，这个构建标签就是 build，它可以指定 Dockerfile 所在文件夹的路径。Compose 将会利用它自动构建这个镜像，然后使用这个镜像启动服务容器。

```
build: /path/to/build/dir
```

也可以是相对路径，只要上下文确定就可以读取到 Dockerfile。

```
build: ./dir
```

设定上下文根目录，然后以该目录为准指定 Dockerfile。

```
build:
  context: ../
  dockerfile: path/of/Dockerfile
```

```
注意 build 都是一个目录，如果你要指定 Dockerfile 文件需要在 build 标签的子级标签中使用 dockerfile 标签指定，如上面的例子。
如果你同时指定了 image 和 build 两个标签，那么 Compose 会构建镜像并且把镜像命名为 image 后面的那个名字。
```

```
build: ./dir
image: webapp:tag
```

既然可以在 docker-compose.yml 中定义构建任务，那么一定少不了 arg 这个标签，就像 Dockerfile 中的 ARG 指令，它可以在构建过程中指定环境变量，但是在构建成功后取消，在 docker-compose.yml 文件中也支持这样的写法：

```
build:
  context: .
  args:
    buildno: 1
    password: secret
```

下面这种写法也是支持的，一般来说下面的写法更适合阅读。

```
build:
  context: .
  args:
    - buildno=1
    - password=secret
```

与 ENV 不同的是，ARG 是允许空值的。例如：

```
args:
  - buildno
  - password
```

这样构建过程可以向它们赋值

注意：YAML 的布尔值（true, false, yes, no, on, off）必须要使用引号引起来（单引号、双引号均可），否则会当成字符串解析。

###### 3. command

使用 command 可以覆盖容器启动后默认执行的命令。

```
command: bundle exec thin -p 3000
```

也可以写成类似 Dockerfile 中的格式

```
command: [bundle, exec, thin, -p, 3000]
```

###### 4.container_name

前面说过 Compose 的容器名称格式是：<项目名称>*<服务名称>*<序号>
虽然可以自定义项目名称、服务名称，但是如果你想完全控制容器的命名，可以使用这个标签指定：

```
container_name: app
```

###### 5.depends_on

在使用 Compose 时，最大的好处就是少打启动命令，但是一般项目容器启动的顺序是有要求的，如果直接从上到下启动容器，必然会因为容器依赖问题而启动失败。

例如在没启动数据库容器的时候启动了应用容器，这时候应用容器会因为找不到数据库而退出，为了避免这种情况我们需要加入一个标签，就是 depends_on，这个标签解决了容器的依赖、启动先后的问题。

```
version: '2'
services:
  web:
    build: .
    depends_on:
      - db
      - redis
  redis:
    image: redis
  db:
    image: postgres
```

注意的是，默认情况下使用 docker-compose up web 这样的方式启动 web 服务时，也会启动 redis 和 db 两个服务，因为在配置文件中定义了依赖关系。

###### 6.dns

和 --dns 参数一样用途，格式如下：

```
dns: 8.8.8.8
```

也可以是一个列表：

```
dns:
  - 8.8.8.8
  - 9.9.9.9
```

此外 dns_search 的配置也类似：

```
dns_search: example.com
dns_search:
  - dc1.example.com
  - dc2.example.com
```

###### 7. tmpfs

挂载临时目录到容器内部，与 run 的参数一样效果：

```
tmpfs: /run
tmpfs:
  - /run
  - /tmp
```

###### 8. entrypoint

在 Dockerfile 中有一个指令叫做 ENTRYPOINT 指令，用于指定接入点，第四章有对比过与 CMD 的区别。
在 docker-compose.yml 中可以定义接入点，覆盖 Dockerfile 中的定义：

```
entrypoint: /code/entrypoint.sh
```

格式和 Docker 类似，不过还可以写成这样：

```
entrypoint:
    - php
    - -d
    - zend_extension=/usr/local/lib/php/extensions/no-debug-non-zts-20100525/xdebug.so
    - -d
    - memory_limit=-1
    - vendor/bin/phpunit
```

###### 9.env_file

还记得前面提到的 .env 文件吧，这个文件可以设置 Compose 的变量。而在 docker-compose.yml 中可以定义一个专门存放变量的文件。
如果通过 docker-compose -f FILE 指定了配置文件，则 env_file 中路径会使用配置文件路径。

如果有变量名称与 environment 指令冲突，则以后者为准。格式如下：

```
env_file: .env
```

或者根据 docker-compose.yml 设置多个：

```
env_file:
  - ./common.env
  - ./apps/web.env
  - /opt/secrets.env
```

注意的是这里所说的环境变量是对宿主机的 Compose 而言的，如果在配置文件中有 build 操作，这些变量并不会进入构建过程中，如果要在构建中使用变量还是首选前面刚讲的 arg 标签。

###### 10. environment

与上面的 env_file 标签完全不同，反而和 arg 有几分类似，这个标签的作用是设置镜像变量，它可以保存变量到镜像里面，也就是说启动的容器也会包含这些变量设置，这是与 arg 最大的不同。
一般 arg 标签的变量仅用在构建过程中。而 environment 和 Dockerfile 中的 ENV 指令一样会把变量一直保存在镜像、容器中，类似 docker run -e 的效果。

```
environment:
  RACK_ENV: development
  SHOW: 'true'
  SESSION_SECRET:

environment:
  - RACK_ENV=development
  - SHOW=true
  - SESSION_SECRET
```

###### 11. expose

这个标签与Dockerfile中的EXPOSE指令一样，用于指定暴露的端口，但是只是作为一种参考，实际上docker-compose.yml的端口映射还得ports这样的标签。

```
expose:
 - "3000"
 - "8000"
```

###### 12. external_links

在使用Docker过程中，我们会有许多单独使用docker run启动的容器，为了使Compose能够连接这些不在docker-compose.yml中定义的容器，我们需要一个特殊的标签，就是external_links，它可以让Compose项目里面的容器连接到那些项目配置外部的容器（前提是外部容器中必须至少有一个容器是连接到与项目内的服务的同一个网络里面）。
格式如下：

```
external_links:
 - redis_1
 - project_db_1:mysql
 - project_db_1:postgresql
```

###### 13. extra_hosts

添加主机名的标签，就是往/etc/hosts文件中添加一些记录，与Docker client的--add-host类似：

```
extra_hosts:
 - "somehost:162.242.195.82"
 - "otherhost:50.31.209.229"
```

启动之后查看容器内部hosts：

```
162.242.195.82  somehost
50.31.209.229   otherhost
```

###### 14. labels

向容器添加元数据，和Dockerfile的LABEL指令一个意思，格式如下：

```
labels:
  com.example.description: "Accounting webapp"
  com.example.department: "Finance"
  com.example.label-with-empty-value: ""
labels:
  - "com.example.description=Accounting webapp"
  - "com.example.department=Finance"
  - "com.example.label-with-empty-value"
```

###### 15. links

还记得上面的depends_on吧，那个标签解决的是启动顺序问题，这个标签解决的是容器连接问题，与Docker client的--link一样效果，会连接到其它服务中的容器。
格式如下：

```
links:
 - db
 - db:database
 - redis
```

使用的别名将会自动在服务容器中的/etc/hosts里创建。例如：

```
172.12.2.186  db
172.12.2.186  database
172.12.2.187  redis
```

相应的环境变量也将被创建。

###### 16. logging

这个标签用于配置日志服务。格式如下：

```
logging:
  driver: syslog
  options:
    syslog-address: "tcp://192.168.0.42:123"
```

默认的driver是json-file。只有json-file和journald可以通过docker-compose logs显示日志，其他方式有其他日志查看方式，但目前Compose不支持。对于可选值可以使用options指定。
有关更多这方面的信息可以阅读官方文档：
[https://docs.docker.com/engine/admin/logging/overview/](https://link.jianshu.com?t=https://docs.docker.com/engine/admin/logging/overview/)

###### 17. pid

```
pid: "host"
```

将PID模式设置为主机PID模式，跟主机系统共享进程命名空间。容器使用这个标签将能够访问和操纵其他容器和宿主机的名称空间。

###### 18. ports

映射端口的标签。
使用HOST:CONTAINER格式或者只是指定容器的端口，宿主机会随机映射端口。

```
ports:
 - "3000"
 - "8000:8000"
 - "49100:22"
 - "127.0.0.1:8001:8001"
```

> 注意：当使用HOST:CONTAINER格式来映射端口时，如果你使用的容器端口小于60你可能会得到错误得结果，因为YAML将会解析xx:yy这种数字格式为60进制。所以建议采用字符串格式。

###### 19. security_opt

为每个容器覆盖默认的标签。简单说来就是管理全部服务的标签。比如设置全部服务的user标签值为USER。

```
security_opt:
  - label:user:USER
  - label:role:ROLE
```

###### 20. stop_signal

设置另一个信号来停止容器。在默认情况下使用的是SIGTERM停止容器。设置另一个信号可以使用stop_signal标签。

```
stop_signal: SIGUSR1
```

###### 21. volumes

挂载一个目录或者一个已存在的数据卷容器，可以直接使用 [HOST:CONTAINER] 这样的格式，或者使用 [HOST:CONTAINER:ro] 这样的格式，后者对于容器来说，数据卷是只读的，这样可以有效保护宿主机的文件系统。
Compose的数据卷指定路径可以是相对路径，使用 . 或者 .. 来指定相对目录。
数据卷的格式可以是下面多种形式：

```
volumes:
  // 只是指定一个路径，Docker 会自动在创建一个数据卷（这个路径是容器内部的）。
  - /var/lib/mysql

  // 使用绝对路径挂载数据卷
  - /opt/data:/var/lib/mysql

  // 以 Compose 配置文件为中心的相对路径作为数据卷挂载到容器。
  - ./cache:/tmp/cache

  // 使用用户的相对路径（~/ 表示的目录是 /home/<用户目录>/ 或者 /root/）。
  - ~/configs:/etc/configs/:ro

  // 已经存在的命名的数据卷。
  - datavolume:/var/lib/mysql
```

如果你不使用宿主机的路径，你可以指定一个volume_driver。

```
volume_driver: mydriver
```

###### 22. volumes_from

从其它容器或者服务挂载数据卷，可选的参数是 :ro或者 :rw，前者表示容器只读，后者表示容器对数据卷是可读可写的。默认情况下是可读可写的。

```
volumes_from:
  - service_name
  - service_name:ro
  - container:container_name
  - container:container_name:rw
```

###### 23. cap_add, cap_drop

添加或删除容器的内核功能。详细信息在前面容器章节有讲解，此处不再赘述。

```
cap_add:
  - ALL

cap_drop:
  - NET_ADMIN
  - SYS_ADMIN
```

###### 24. cgroup_parent

指定一个容器的父级cgroup。

```
cgroup_parent: m-executor-abcd
```

###### 25. devices

设备映射列表。与Docker client的--device参数类似。

```
devices:
  - "/dev/ttyUSB0:/dev/ttyUSB0"
```

###### 26. extends

这个标签可以扩展另一个服务，扩展内容可以是来自在当前文件，也可以是来自其他文件，相同服务的情况下，后来者会有选择地覆盖原有配置。

```
extends:
  file: common.yml
  service: webapp
```

用户可以在任何地方使用这个标签，只要标签内容包含file和service两个值就可以了。file的值可以是相对或者绝对路径，如果不指定file的值，那么Compose会读取当前YML文件的信息。
更多的操作细节在后面的12.3.4小节有介绍。

###### 27. network_mode

网络模式，与Docker client的--net参数类似，只是相对多了一个service:[service name] 的格式。
例如：

```
network_mode: "bridge"
network_mode: "host"
network_mode: "none"
network_mode: "service:[service name]"
network_mode: "container:[container name/id]"
```

可以指定使用服务或者容器的网络。

###### 28. networks

加入指定网络，格式如下：

```
services:
  some-service:
    networks:
     - some-network
     - other-network
```

关于这个标签还有一个特别的子标签aliases，这是一个用来设置服务别名的标签，例如：

```
services:
  some-service:
    networks:
      some-network:
        aliases:
         - alias1
         - alias3
      other-network:
        aliases:
         - alias2
```

相同的服务可以在不同的网络有不同的别名。

###### 29. 其它

还有这些标签：cpu_shares, cpu_quota, cpuset, domainname, hostname, ipc, mac_address, mem_limit, memswap_limit, privileged, read_only, restart, shm_size, stdin_open, tty, user, working_dir
上面这些都是一个单值的标签，类似于使用docker run的效果。

```
cpu_shares: 73
cpu_quota: 50000
cpuset: 0,1

user: postgresql
working_dir: /code

domainname: foo.com
hostname: foo
ipc: host
mac_address: 02:42:ac:11:65:43

mem_limit: 1000000000
memswap_limit: 2000000000
privileged: true

restart: always

read_only: true
shm_size: 64M
stdin_open: true
tty: true
```
