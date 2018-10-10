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

或者

```
1.在计算机的安装盘下找到 '用户' 这个文件夹打开。
2.找到'用户' 文件夹下面有个和你计算机的名字一样的文件夹。
3.新建'.gitconfig' 文件
4.用编辑器打开新建文件，输入:
[user]
name = ‘你的git用户名’
email = ‘你的git邮箱’
[credential]
helper = store

5.保存后，随意打开一个项目，pull 或者 push 一次，下次就不需要输入密码了。
```





# 编译安装

卸载自带的git

```
yum remove git
```

下载最新版git

```
wget https://github.com/git/git/archive/v2.9.2.tar.gz
```

解压

```
# tar zxvf v2.9.2.tar.gz
# cd git-2.9.2
```

编译安装

```
# make configure
# ./configure --prefix=/usr/local/git --with-iconv=/usr/local/libiconv
# make all doc
# sudo make install install-doc install-html
```

修改环境变量

```
# sudo vim /etc/profile
```

在最后一行添加

```
export PATH=/usr/local/git/bin:$PATH
```

保存后使其立即生效

```
# source /etc/profile
```

查看是否安装成功

```
#git --version
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



# 配置

Git 提供了一个叫做 git config 的工具，专门用来配置或读取相应的工作环境变量。

这些环境变量，决定了 Git 在各个环节的具体工作方式和行为。这些变量可以存放在以下三个不同的地方：

- `/etc/gitconfig` 文件：系统中对`所有用户`都普遍适用的配置。若使用 `git config` 时用 `--system` 选项，读写的就是这个文件。
- `~/.gitconfig` 文件：用户目录下的配置文件只适用于`当前用户`。若使用 `git config` 时用 `--global` 选项，读写的就是这个文件。
- 当前项目的 Git 目录中的配置文件（也就是工作目录中的 `.git/config` 文件）：这里的配置仅仅针对`当前项目`有效。每一个级别的配置都会覆盖上层的相同配置，所以 `.git/config` 里的配置会覆盖 `/etc/gitconfig` 中的同名变量。

在 Windows 系统上，Git 会找寻用户主目录下的 .gitconfig 文件。主目录即 $HOME 变量指定的目录，一般都是 C:\Documents and Settings\$USER。

此外，Git 还会尝试找寻 /etc/gitconfig 文件，只不过看当初 Git 装在什么目录，就以此作为根目录来定位。



## 配置用户信息

配置个人的用户名称和电子邮件地址：

```
$ git config --global user.name "Louis"
$ git config --global user.email louis@qq.com
```

如果用了 **--global** 选项，那么更改的配置文件就是位于你用户主目录下的那个，以后你所有的项目都会默认使用这里配置的用户信息。

如果要在某个特定的项目中使用其他名字或者电邮，只要去掉 --global 选项重新配置即可，新的设定保存在当前项目的 .git/config 文件里。

### 查看配置信息

要检查已有的配置信息，可以使用 git config --list 命令：

```
$ git config --list
http.postbuffer=2M
user.name=Louis
user.email=louis@qq.com
```

有时候会看到重复的变量名，那就说明它们来自不同的配置文件（比如 /etc/gitconfig 和 ~/.gitconfig），不过最终 Git 实际采用的是最后一个。

也可以直接查阅某个环境变量的设定，只要把特定的名字跟在后面即可，像这样：

```
$ git config user.name
Louis
```



# 参考

https://www.cnblogs.com/xifengxiaoma/p/9510778.html

https://www.cnblogs.com/xishuai/category/558994.html

http://www.cnblogs.com/wade-luffy/p/5760154.html