# 通过sql实现无则插入有则修改(MySQL)

在实际工作中经常遇到这样的情况，如果传入的数据在数据库中没有记录，那么新增一条数据。如果在数据库中有相应的记录，那么则将对应的记录更新为最新数据。

​    通常情况下，我们会先通过条件去数据库查一次，根据返回的结果，如果为空则执行insert动作，如果不为空则执行update动作，这样的做法难免显得有些臃肿。其实我们可以通过 ON DUPLICATE KEY 关键字用一句相当简单的sql来实现相同的效果。下面就详细说一下用法。

前提条件：查询条件为主键或唯一索引

实现原理：insert时判断是否存在主键冲突或唯一索引冲突，如果存在则执行update。

示例：

首先创建一张user表：

```
create table user
(
	user_id int auto_increment comment '用户ID'
		primary key,
	user_name varchar(32) not null comment '用户姓名',
	user_code int not null comment '用户编号',
	user_age int not null comment '用户年龄',
	user_sex int not null comment '用户性别',
	constraint user_user_code_uindex
		unique (user_code)
)
comment '用户表'
;
```

然后随便插入一条数据：

```
INSERT INTO user (user_name, user_code, user_age, user_sex) VALUES ('用户1', 1, 13, 1);
```

查询表可以看到数据已经在表里，然后执行下面sql：

```
INSERT INTO user (user_name, user_code, user_age, user_sex) VALUES ('用户1', 1, 13, 1)
ON DUPLICATE KEY UPDATE user_age = 12, user_name = '用户2';
```

可以看出，如果只是单纯的执行insert操作该条语句会报错，因为user_code是唯一索引，这里产生了冲突。而执行上面语句发现并没有报错，因为在insert时，产生了键值冲突，所以执行了后面的update操作。再次查看user表发现，表中并没有新增一条数据，而是将愿来user_code=1的数据进行了相对应的更新操作。

注意事项：这种方法虽然可以减少部分代码量，但仅适用于表结构稳定的情况，因为如果后来表中再删除了唯一索引，那么这条sql就存在着失效的风险。