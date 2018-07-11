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

