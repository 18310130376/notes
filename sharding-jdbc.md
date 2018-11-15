# sharding-jdbc结合mybatis实现分库分表功能

https://www.cnblogs.com/zwt1990/p/6762135.html

```xml
<dependency>
    <groupId>com.dangdang</groupId>
    <artifactId>sharding-jdbc-core</artifactId>
    <version>1.4.2</version>
</dependency>
<dependency>
       <groupId>com.dangdang</groupId>
       <artifactId>sharding-jdbc-config-spring</artifactId>
       <version>1.4.0</version>
</dependency>
```

sharding-jdbc.xml：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:context="http://www.springframework.org/schema/context" 
    xmlns:tx="http://www.springframework.org/schema/tx"
    xmlns:rdb="http://www.dangdang.com/schema/ddframe/rdb"
    xsi:schemaLocation="http://www.springframework.org/schema/beans
                        http://www.springframework.org/schema/beans/spring-beans.xsd 
                        http://www.springframework.org/schema/tx 
                        http://www.springframework.org/schema/tx/spring-tx.xsd
                        http://www.springframework.org/schema/context 
                        http://www.springframework.org/schema/context/spring-context.xsd
                        http://www.dangdang.com/schema/ddframe/rdb 
                        http://www.dangdang.com/schema/ddframe/rdb/rdb.xsd">
                
    <rdb:strategy id="tableShardingStrategy" sharding-columns="user_id" algorithm-class="com.meiren.member.common.sharding.MemberSingleKeyTableShardingAlgorithm"/>
    
    <rdb:data-source id="shardingDataSource">
        <rdb:sharding-rule data-sources="dataSource">
            <rdb:table-rules>
                <rdb:table-rule logic-table="member_index" actual-tables="member_index_tbl_${[0,1,2,3,4,5,6,7,8,9]}${0..9}"  table-strategy="tableShardingStrategy"/>
                <rdb:table-rule logic-table="member_details" actual-tables="member_details_tbl_${[0,1,2,3,4,5,6,7,8,9]}${0..9}"  table-strategy="tableShardingStrategy"/>
            </rdb:table-rules>
        </rdb:sharding-rule>
    </rdb:data-source>
    
    <bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <property name="dataSource" ref="shardingDataSource" />
    </bean>
</beans>
```

