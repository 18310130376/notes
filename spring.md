# 前言

Spring 5 新增许多特性



# init-method 

用于在bean初始化时指定执行方法

```xml
<bean id="RedisTopicListener"
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

