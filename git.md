#### 安装

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

#### 参考

https://www.cnblogs.com/xishuai/category/558994.html

http://www.cnblogs.com/wade-luffy/p/5760154.html