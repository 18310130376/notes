# Tomcat 性能调优

http://www.importnew.com/27878.html

内存设置：

```
vi  /home/jeesml/jeesml/web/jeesml-tomcat/bin/catalina.sh  
JAVA_OPTS='-Xms128m -Xmx512m -XX:PermSize=128m' 
```

