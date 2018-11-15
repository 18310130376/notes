# 前言

Spring 5 新增许多特性



# xml-init-method 

用于在bean初始化时指定执行方法

```xml
<bean id="redisTopicListener"
		class="com.gwghk.gts2.api.service.RedisTopicListener"
		init-method="init" scope="singleton" destroy-method="destroy">
     <property name="topic" value="test" />
</bean>
```

```java
package com.gwghk.gts2.api.service;

import java.util.Set;

import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.redisson.api.listener.MessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwghk.gts2.client.models.autogen.ViewFoTradeReportParam;
import com.gwghk.gts2.client.models.autogen.ViewFundingRecordParam;
import com.gwghk.gts2.client.models.autogen.ViewInterestsRecordParam;
import com.gwghk.gts2.client.models.autogen.ViewSettlementRecordParam;
import com.gwghk.gts2.common.util.RedisTopic;
import com.gwghk.gts2.common.util.RedisUtil;
import com.gwghk.gts2.core.service.RedisDataService;


public class RedisTopicListener {
	final static Logger logger = LoggerFactory.getLogger(RedisTopicListener.class);
	final static RedissonClient redissonClient = RedisUtil.getInstance().getRedissonClient();
	
	@Autowired
	private RedisDataService redisService;
	
	public void init() {
	
	}
}
```

测试：

```java
public class ApplicationTest {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("beans.xml");
        // 根据bean id获取bean对象
        RedisTopicListener redisTopicListener = (User) applicationContext.getBean("redisTopicListener");
        System.out.println(redisTopicListener);
    }
}
```

# **@Bean-init-method **

```java
/**
 * 定义一个注解配置文件 必须要加上@Configuration注解
 *
 * @author zhangqh
 * @date 2018年4月30日
 */
@Configuration
public class MainConfig {
   
    @Bean(initMethod="initUser",destroyMethod="destroyUser")
    public User getUser(){
        return new User("zhangsan",26);
    }
    
    public void initUser(){
        System.out.println("初始化用户bean之前执行");
    }
    public void destroyUser(){
        System.out.println("bean销毁之后执行");
    }
}
```

测试

```java
//使用AnnotationConfigApplicationContext获取spring容器ApplicationContext2
ApplicationContext applicationContext2 = new AnnotationConfigApplicationContext(MainConfig.class);
User bean2 = applicationContext2.getBean(User.class);
System.out.println(bean2);
```

```

```

