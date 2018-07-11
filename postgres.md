# 安装

1.引入 PostgreSQL 9.4 Repository 官方的安装源 

rpm -Uvh <http://yum.postgresql.org/9.4/redhat/rhel-5-x86_64/pgdg-redhat94-9.4-1.noarch.rpm> 

2.安装

查看已安装版本

psql --version 

查找:rpm -qa| grep  包名

卸载:rpm -e --nodeps 包名 

yum install postgresql94 postgresql94-server postgresql94-contrib 

3.初始化 PostgreSQL集群Cluster initdb 

su - postgres -c /usr/pgsql-9.4/bin/initdb 

4.PostgreSQL 服务器 监听地址跟端口设置 

vi /var/lib/pgsql/9.4/data/postgresql.conf

 listen_addresses = '*'port = 5432

max_connction=1024 

5.PostgreSQL 访问权限(Permissions)设置,客户端连接设置 

打开vi /var/lib/pgsql/9.4/data/pg_hba.conf

添加:

host all all 172.30.10.0/24 md5 

host    all             all             127.0.0.1/32            trust
host    all             all             192.168.0.0/16          md5
host    all             all             192.168.32.106/24             md5
host    all             all             172.20.10.32/24             md5
host    all             all             172.20.0.0/16              md5
host    all             all             192.168.0.0/16              md5
host    all             all             172.30.0.0/16              md5
host    all             all             192.168.0.0/24             md5
host    all             all             192.168.98.0/24             md5
host    all             all             172.18.0.0/16             md5
host    all             all             172.18.0.0/24             md5

6.设置 PostgreSQL Server 自动开机启动 

chkconfig --levels 235 postgresql-9.4 on 

启动 PostgreSQL 9.4

service postgresql-9.4 start
service postgresql-9.4 restart

OR

/etc/init.d/postgresql-9.4 start

设置用户:

su - postgres

$ psql

ALTER USER postgres WITH PASSWORD 'postgres';

select * from pg_shadow ;

create database david;

create table test (id integer,name text);

防火墙的关闭:
防火墙:
1) 重启后生效
开启： chkconfig iptables on
关闭： chkconfig iptables off

2) 即时生效，重启后失效
开启： service iptables start
关闭： service iptables stop

3)如果以上命令不支持，试试这个：
启动： systemctl start firewalld
查看状态： systemctl status firewalld
停止： systemctl disable firewalld
禁用： systemctl stop firewalld


CREATE USER office WITH PASSWORD 'Dev1234{}';
CREATE USER officereader WITH PASSWORD 'Dev1234{}';

ALTER USER officereader WITH PASSWORD 'officereader123';
grant all on all tables in schema office to office;
grant all on all tables in schema trade_ix to office;


grant all on all tables in schema office to officereader;
grant all on all tables in schema trade_ix to officereader;

GRANT SELECT ON demo TO PUBLIC

7.测试连接

```
psql -h 172.30.1.108 -U  office  -W goldoffice_ix_remote
```

