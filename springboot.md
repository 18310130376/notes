see:https://github.com/vector4wang/spring-boot-quick

see:https://www.cnblogs.com/sunny3096/category/1034222.html



# Jar方式运行

后台启动并打印日志：

nohup java  -Xms128m -Xmx512m -jar boot.jar Djava.security.egd=file:/dev/./urandom > 日志.log 2>&1 &

解决tomcat启动慢的问题: Djava.security.egd=file:/dev/./urandom

nohup java -Xms128m -Xmx512m -jar boot.jar  1>/dev/null 2>&1 &

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]

为了缩短 Tomcat 的启动时间，添加 java.security.egd 的系统属性指向 /dev/urandom 

