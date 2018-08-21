https://www.cnblogs.com/linjiqin/category/808798.html

https://www.cnblogs.com/kevingrace/category/839102.html



# 安装

检查是否安装nginx 

```
nginx -v
```

下载并且解压

```
wget http://nginx.org/download/nginx-1.10.3.tar.gz
tar zxvf nginx-1.10.3.tar.gz
cd nginx-1.10.3
```

启动server状态页和https模块: 

```
./configure --with-http_stub_status_module --with-http_ssl_module
```

如果报错，提示缺少pcre模块，那么就下载pcre: 

```
wget https://ftp.pcre.org/pub/pcre/pcre-8.40.tar.gz
tar zxvf pcre-8.40.tar.gz
```

安装pcre： 

```
cd pcre-8.40
./configure
make
make install
```

如果报错缺少zlib，那么就安装zlib: 

```
yum install -y zlib-devel
```

继续安装nginx: 

```
cd nginx-1.10.3 
./configure
make
make install
```

nginx安装成功后路径为/usr/local/nginx,创建proxy.conf 

```
#!nginx (-)
# proxy.conf
proxy_redirect          off;
proxy_set_header        Host $host;
proxy_set_header        X-Real-IP $remote_addr;  #获取真实ip
#proxy_set_header       X-Forwarded-For   $proxy_add_x_forwarded_for; #获取代理者的真实ip
client_max_body_size    10m;
client_body_buffer_size 128k;
proxy_connect_timeout   90;
proxy_send_timeout      90;
proxy_read_timeout      90;
proxy_buffer_size       4k;
proxy_buffers           4 32k;
proxy_busy_buffers_size 64k;
proxy_temp_file_write_size 64k;

编辑conf文件夹中的nginx.conf，修改主要配置：
    upstream admin{
        #两个服务所在tomcat的路径，要配置server.xml中根路径
        server localhost:18501;
        server localhost:28501;
        #根据ip地址hash来负载，解决了session共享的问题
        ip_hash;
    }

    server {
        listen       80;
        server_name  localhost;

        location / {
        //对应upstream
           proxy_pass http://admin;
        }
    }

重启nginx:./nginx -s reload
启动nginx:./nginx
停止nginx:./nginx -s stop
```

# nginx负载均衡的5种策略

nginx可以根据客户端IP进行负载均衡，在upstream里设置ip_hash，就可以针对同一个C类地址段中的客户端选择同一个后端服务器，除非那个后端服务器宕了才会换一个。

nginx的upstream目前支持的5种方式的分配

**1、轮询（默认）**
每个请求按时间顺序逐一分配到不同的后端服务器，如果后端服务器down掉，能自动剔除。 
upstream backserver { 
server 192.168.0.14; 
server 192.168.0.15; 
} 
**2、指定权重**
指定轮询几率，weight和访问比率成正比，用于后端服务器性能不均的情况。 
upstream backserver { 
server 192.168.0.14 weight=10; 
server 192.168.0.15 weight=10; 
} 
**3、IP绑定 ip_hash**
每个请求按访问ip的hash结果分配，这样每个访客固定访问一个后端服务器，可以解决session的问题。 
upstream backserver { 
ip_hash; 
server 192.168.0.14:88; 
server 192.168.0.15:80; 
} 
**4、fair（第三方）**
按后端服务器的响应时间来分配请求，响应时间短的优先分配。 
upstream backserver { 
server server1; 
server server2; 
fair; 
} 
**5、url_hash（第三方）**
按访问url的hash结果来分配请求，使每个url定向到同一个后端服务器，后端服务器为缓存时比较有效。 
upstream backserver { 
server squid1:3128; 
server squid2:3128; 
hash $request_uri; 
hash_method crc32; 
} 
在需要使用负载均衡的server中增加 
proxy_pass http://backserver/; 
upstream backserver{ 
ip_hash; 
server 127.0.0.1:9090 down; (down 表示单前的server暂时不参与负载) 
server 127.0.0.1:8080 weight=2; (weight 默认为1.weight越大，负载的权重就越大) 
server 127.0.0.1:6060; 
server 127.0.0.1:7070 backup; (其它所有的非backup机器down或者忙的时候，请求backup机器) 
} 
max_fails ：允许请求失败的次数默认为1.当超过最大次数时，返回proxy_next_upstream 模块定义的错误 

fail_timeout:max_fails次失败后，暂停的时间