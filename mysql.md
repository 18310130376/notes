https://www.cnblogs.com/wade-luffy/category/904814.html 



# 查看进程命令

|                  |          |
| ---------------- | -------- |
| show processlist | 查看进程 |
|                  |          |
|                  |          |

# mysql高效创建索引

## 方式一 

有[一个](http://blog.uouo123.com/tags-1368.html)问题，一张表有3百万条记录，随着[时间](http://blog.uouo123.com/tags-65.html)的增加，记录量会更多，此时[查询](http://blog.uouo123.com/tags-335.html)速度很慢。在创建此表前[没有](http://blog.uouo123.com/tags-948.html)未相应字段添加索引，所以此时需要为表添加索引。但是因为[数据](http://blog.uouo123.com/tags-603.html)量大的[原因](http://blog.uouo123.com/tags-921.html)，索引添加不成功，想了很多办法，终于在短时间内[解决](http://blog.uouo123.com/tags-480.html)了。

```sql
1、进入mysql界面。mysql -uroot -hlocalhost -plovelive gm;

2、导出相应表的数据。select * from tab into outfile 'tab.txt'; 此处tab.txt文件在mysql的data目录里

3、删除相应表的数据，并置第一条记录为0。truncate tab;

4、创建索引。create index IDX_NAME using BTREE on tab (col);
　索引的方式有：BTREE、RTREE、HASH、FULLTEXT、SPATIAL
　
5、导入文件到相应表。load data infile '/mysql/data/tab.txt' into table tab;
```

## 方式二

普通的添加字段sql

ALTER TABLE `table_name` ADD COLUMN `num`  int(10) NOT NULL DEFAULT 0 AFTER `addtime`;

普通的添加索引sql

ALTER TABLE `table_name` ADD INDEX `num` (`num`) ;

但是线上的一张表如果数据量很大呢，执行加字段操作就会锁表，这个过程可能需要很长时间甚至导致服务崩溃，那么这样操作就很有风险了。

大表加字段的思路如下：

① 创建一个临时的新表，首先复制旧表的结构（包含索引）

② 给新表加上新增的字段

③ 把旧表的数据复制过来

④ 删除旧表，重命名新表的名字为旧表的名字

实现过程大概就是这样，下面我会附带我实现的sql:

① 创建一个临时的新表，首先复制旧表的结构（包含索引）

create table new_table like old_table;

② 给新表加上新增的字段 增加索引

ALTER TABLE `table_name` ADD COLUMN `num`  int(10) NOT NULL DEFAULT 0 AFTER `addtime`;


ALTER TABLE `table_name` ADD INDEX `num` (`num`) ;

③ 把旧表的数据复制过来

insert into new_table(id,name,content,addtime) select id,name,content,addtime from old_table;

注意：执行这步的时候，可能这个过程也需要时间，这个时候有新的数据进来，所以原来的表如果有字段记录了数据的写入时间就最好了，可以找到执行这一步操作之后的数据，并重复导入到新表，直到数据差异很小。不过还是会可能损失极少量的数据。所以，如果表的数据特别大，同时又要保证数据完整，最好停机操作。

我操作的时候是选取的一个低峰期时间操作的，减少数据差距。

④ 旧表的名字修改为别的名，重命名新表的名字为旧表的名字

留一个备用表，可以等新表完全没问题再删除



# 开启mysql定时任务

```
查看event是否开启 : SHOW VARIABLES LIKE '%event_sche%';
将事件计划开启 : SET GLOBAL event_scheduler = 1;
将事件计划关闭 : SET GLOBAL event_scheduler = 0;
关闭事件任务 : ALTER EVENT eventName ON COMPLETION PRESERVE DISABLE;
开启事件任务 : ALTER EVENT eventName ON COMPLETION PRESERVE ENABLE;
查看事件任务 : SHOW EVENTS ;
查看存储过程：select `name` from mysql.proc where db = 'smart' and `type` = 'PROCEDURE' 

```

