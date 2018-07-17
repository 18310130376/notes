# Tomcat 性能调优

http://www.importnew.com/27878.html

内存设置：

```
vi  /home/jeesml/jeesml/web/jeesml-tomcat/bin/catalina.sh  
JAVA_OPTS='-Xms128m -Xmx512m -XX:PermSize=128m' 
```



# 项目不放在webapp里的办法

server.xml Host节点里配置

```
<Context path="/" docBase="/web_branch_bo/Goldoffice_web-1.0.0-IX-TD-SIT" reloadable="true"></Context>
```

目录结构：

```
[root@localhost web_branch_bo]# ls
Goldoffice_web-1.0.0-IX-TD-SIT  Goldoffice_web-1.0.0-IX-TD-SIT.war  ROOT
[root@localhost web_branch_bo]#
[root@localhost web_branch_bo]# pwd
/web_branch_bo
[root@localhost web_branch_bo]#
```

