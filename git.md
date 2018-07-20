# 安装

用git --version命令检查是否已经安装

```
git --version
```

删除旧版本的git

```
yum remove git
```

安装git

```
yum -y install git
```

Git配置

```
git config --global user.name  "your name"  
git config --global user.email "your email"   
git config --global push.default simple        # 每次push仅push当前分支
git config --global core.autocrlf false        # 忽略window/unix换行符
git config --global gui.encoding utf-8         # 避免乱码
git config --global core.quotepath off         # 避免git status显示的中文文件名乱码
```

设置SSH

```
ssh-keygen -t rsa -C "your email"
```

然后一路回车，不需要输入任何密码。在当前用户目录的.ssh文件夹下(~/.ssh/id_rsa.pub)会生成id_rsa.pub文件，其内容就是ssh key pair。
对于Linux还需执行以下命令将ssh key告诉系统：

```
$ ssh-add ~/.ssh/id_rsa
```

将生成的ssh key添加到github（[账户创建和配置](https://link.jianshu.com/?t=https://git-scm.com/book/zh/v2/GitHub-%E8%B4%A6%E6%88%B7%E7%9A%84%E5%88%9B%E5%BB%BA%E5%92%8C%E9%85%8D%E7%BD%AE)），git@osc或者任何提供git服务的网站，以后使用git提交到远端服务器就不需要密码了。

保存git账号：(上面的git config命令就是修改这里的)

```
cd
vi .git-credentials //输入git地址及用户名密码
git config --global user.name [username]
git config --global user.email [email]
vi .gitconfig
[credential] 
     helper = store
保存后git pull首次还是需要输入用户名和密码，以后就不用输入了
```



# Tortoisegit使用教程

1.安装Git及Tortoisegit先上图，首先需要把123按顺序安装了

Git下载地址：https://git-for-windows.github.io/

Tortoisegit及语言包下载地址：http://tortoisegit.org/download/ 

```
Git-2.12.0-64-bit.exe
TortoiseGit-2.4.0.2-64bit.msi
TortoiseGit-LanguagePack-2.4.0.0-64bit-zh_CN.msi
```



# gitLab



# 命令

https://www.yiibai.com/git/git_checkout.html

|                   |                      |
| ----------------- | -------------------- |
| git checkout  dev | 切换到dev分支        |
| git checkout .    | 忽略更改拉取最新代码 |
|                   |                      |
|                   |                      |
|                   |                      |
|                   |                      |
|                   |                      |
|                   |                      |
|                   |                      |



# github访问慢解决

配置host

windows： C:\Windows\System32\drivers\etc

linux：vi  /etc/hosts

```
151.101.44.249 github.global.ssl.fastly.net 
192.30.253.113 github.com
```

刷新DNS： linux下： sudo /etc/init.d/networking restart

​             windows下： ipconfig /flushdns



# github大文件上传

git  bash

```
git config --global http.postBuffer 524288000
git config --global http.sslVerify false
```

小乌龟

```
右键TortoiseGit--settings--Git--Edit systemwide gitconfig--把postBuffer的值修改为524288000
部分内容如下：
[http]
sslCAInfo = D:/programFiles/Git/mingw64/ssl/certs/ca-bundle.crt
postBuffer=524288000
```

# 参考

https://www.cnblogs.com/xishuai/category/558994.html

http://www.cnblogs.com/wade-luffy/p/5760154.html