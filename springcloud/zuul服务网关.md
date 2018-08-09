# 一、Zuul简介

Zuul的主要功能是路由转发和过滤器。路由功能是微服务的一部分，比如／api/user转发到到user服务，/api/shop转发到到shop服务。zuul默认和Ribbon结合实现了负载均衡的功能。

zuul有以下功能

- Authentication   //权限
- Insights
- Stress Testing
- Canary Testing
- Dynamic Routing  //动态路由
- Service Migration
- Load Shedding
- Security      //安全
- Static Response handling
- Active/Active traffic management

# 二、创建service-zuul工程



```xml
<dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-zuul</artifactId>
        </dependency>
</dependencies>

<!--旧版本-->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-eureka</artifactId>
</dependency>
<dependency>
     <groupId>org.springframework.boot</groupId>
     <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
     <groupId>org.springframework.cloud</groupId>
     <artifactId>spring-cloud-starter-zuul</artifactId>
</dependency>
```

```java
package org.service.zuul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;


#@EnableZuulProxy
#@SpringCloudApplication
/**整合了@SpringBootApplication、@EnableDiscoveryClient、@EnableCircuitBreaker，主要目的还是简化配置**/

@SpringBootApplication
@EnableZuulProxy
@EnableEurekaClient
public class ZuulApplication {
	public static void main(String[] args) {
		SpringApplication.run(ZuulApplication.class, args);
	}
}
```

```properties
server.port=6001
spring.application.name=api-gateway

zuul.routes.api-a-url.path=/baidu/**
zuul.routes.api-a-url.url=http://www.baidu.com

zuul.routes.feignConsumer.path=/configClient/**
zuul.routes.feignConsumer.serviceId=configClient

#上面两行，如果feignConsumer就是服务名的话，可以不再指定serviceId，用下面一行就可以了。
#当然，也可以啥都不用配置，直接用默认规则服务名小写去访问
zuul.routes.feignConsumer.path=/feignConsumer/**

eureka.client.serviceUrl.defaultZone=http://localhost:9001/eureka/
```

访问http://localhost:6001/configClient/consumer01/4560实际请求的是configClient的consumer01

### 默认规则

如果上面不配置任何路由映射，直接访问http://localhost:6001/configclient/consumer01/4560?accessToken=a

发现也返回了正确的数据，这就是默认规则：注意是小写的，例如上面configclient

```
http://GATEWAY:GATEWAY_PORT/想要访问的Eureka服务id的小写/**
```

推荐使用serviceId的映射方式，除了对Zuul维护上更加友好之外，serviceId映射方式还支持了断路器，对于服务故障的情况下，可以有效的防止故障蔓延到服务网关上而影响整个系统的对外服务

# 三、服务过滤

在服务网关中定义过滤器只需要继承`ZuulFilter`抽象类实现其定义的四个抽象函数就可对请求进行拦截与过滤。

AccessFilter需要加@Component注解，否则需要在启动类里加Bean声明

```java
@Bean
public AccessFilter accessFilter() {
	return new AccessFilter();
}
```



```java
package org.boot.filter;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

@Component
public class AccessFilter extends ZuulFilter {

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public Object run() {// 过滤器的具体逻辑。可用很复杂，包括查sql，nosql去判断该请求到底有没有权限访问。
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();
		Object accessToken = request.getParameter("accessToken");
		if (accessToken == null) {
			ctx.setSendZuulResponse(false);//设置为false时就不会去访问真实的实例了，也就是不做路由处理，只有pre类型的过滤器支持终止转发
			ctx.setResponseStatusCode(401);
			 try {
				ctx.getResponse().getWriter().write("token is empty");
                //ctx.setResponseBody(body);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		return null;
	}

	@Override
	public String filterType() {
		return "pre";// 路由之前 routing：路由之时 post： 路由之后 error：发送错误调用
	}

	@Override
	public int filterOrder() {
		return 0;
	}
}
```

- 自定义过滤器的实现，需要继承`ZuulFilter`，需要重写实现下面四个方法：

  filterType：返回一个字符串代表过滤器的类型，在zuul中定义了四种不同生命周期的过滤器类型，具体如下：

  - `pre`：可以在请求被路由之前调用
  - `routing`：在路由请求时候被调用
  - `post`：在routing和error过滤器之后被调用
  - `error`：处理请求时发生错误时被调用


  - `filterOrder`：通过int值来定义过滤器的执行顺序
  - `shouldFilter`：返回一个boolean类型来判断该过滤器是否要执行，所以通过此函数可实现过滤器的开关。在上例中，我们直接返回true，所以该过滤器总是生效。
  - `run`：过滤器的具体逻辑。需要注意，这里我们通过`ctx.setSendZuulResponse(false)`令zuul过滤该请求，不对其进行路由，然后通过`ctx.setResponseStatusCode(401)`设置了其返回的错误码，当然我们也可以进一步优化我们的返回，比如，通过`ctx.setResponseBody(body)`对返回body内容进行编辑等。



过滤器执行的顺序是通过filterOrder方法进行排序，越小的值越优先处理，只有pre类型的过滤器支持终止转发



# 四、核心过滤器

路由功能在真正运行时，它的路由映射和请求转发都是由几个不同的过滤器完成的，每个类型的过滤器职责如下：

pre：路由映射，它将请求路径与配置的路由规则进行匹配，以找到需要转发的目标地址

routing：请求转发  对pre类型过滤器获得的路由地址进行转发到具体服务实例上去，当服务实例将请求结果都返回之后，      routing阶段完成，请求进入第三个阶段post

post： 此时请求将会被post类型的过滤器进行处理，这些过滤器在处理的时候不仅可以获取到请求信息，还能获取到服务实例的返回信息，所以在post类型的过滤器中，我们可以对处理结果进行一些加工或转换等内容

error： 该阶段只有在上述三个阶段中发生异常的时候才会触发，但是它的最后流向还是post类型的过滤器，因为它需要通过post过滤器将最终结果返回给请求客户端（实际实现上还有一些差别，后续介绍）



# 五、Zuul统一异常处理

## 一、严格try - catch

```java
package org.boot.filter;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

@Component
public class PreFilter extends ZuulFilter {

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();
		Object accessToken = request.getParameter("accessToken");
		if (accessToken == null) {
			ctx.setSendZuulResponse(false);
			ctx.setResponseStatusCode(401);
			 try {
				ctx.getResponse().getWriter().write("token is empty");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		try {
			doSomething();
		} catch (Exception e) {
			 ctx.set("error.status_code", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		     ctx.set("error.exception", e);
		}
		return null;
	}

	@Override
	public String filterType() {
		return "pre";
	}

	@Override
	public int filterOrder() {
		return 0;
	}
	
	 private void doSomething() {
	        throw new RuntimeException("Exist some errors...");
	}
}
```

以上下面代码是关键，SendErrorFilter（post阶段）判断上下文包含error.status_code才会执行，所以有异常的时候需要捕获，并且在上下文设置error.status_code值

```java
try {
	doSomething();
} catch (Exception e) {
	ctx.set("error.status_code", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	ctx.set("error.exception", e);
}
```

上面代码返回

```json
{
	"timestamp": 1533698033215,
	"status": 500,
	"error": "Internal Server Error",
	"exception": "java.lang.RuntimeException",
	"message": "Exist some errors..."
}
```

此时，我们的异常信息已经被`SendErrorFilter`过滤器正常处理并返回给客户端了，同时在网关的控制台中也输出了异常信息。从返回的响应信息中，我们可以看到几个我们之前设置在请求上下文中的内容，它们的对应关系如下：

- `status`：对应`error.status_code`参数的值
- `exception`：对应`error.exception`参数中`Exception`的类型
- `message`：对应`error.exception`参数中`Exception`的`message`信息。对于`message`的信息，我们在过滤器中还可以通过`ctx.set("error.message", "自定义异常消息");`来定义更友好的错误信息。`SendErrorFilter`会优先取`error.message`来作为返回的`message`内容，如果没有的话才会使用`Exception`中的`message`信息

例如上面代码改为

```java
try {
	doSomething();
} catch (Exception e) {
	ctx.set("error.status_code", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	ctx.set("error.exception", e);
	ctx.set("error.message","服务器出错了");
}
```

执行返回

```json
{
	"timestamp": 1533698511948,
	"status": 500,
	"error": "Internal Server Error",
	"exception": "java.lang.RuntimeException",
	"message": "服务器出错了"
}
```

## 二、ErrorFilter处理

上面的解决方案中我们不断强调要在过滤器中使用`try-catch`来处理业务逻辑并往请求上下文添加异常信息，但是不可控的人为因素、意料之外的程序因素等，依然会使得一些异常从过滤器中抛出，对于意外抛出的异常又会导致没有控制台输出也没有任何响应信息的情况出现，那么是否有什么好的方法来为这些异常做一个统一的处理呢？

这个时候，我们就可以用到`error`类型的过滤器了。由于在请求生命周期的`pre`、`route`、`post`三个阶段中有异常抛出的时候都会进入`error`阶段的处理，所以我们可以通过创建一个`error`类型的过滤器来捕获这些异常信息，并根据这些异常信息在请求上下文中注入需要返回给客户端的错误描述，这里我们可以直接沿用在`try-catch`处理异常信息时用的那些error参数，这样就可以让这些信息被`SendErrorFilter`捕获并组织成消息响应返回给客户端。比如，下面的代码就实现了这里所描述的一个过滤器：

```java
package org.boot.filter;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

@Component
public class PreFilter extends ZuulFilter {

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();
		Object accessToken = request.getParameter("accessToken");
		if (accessToken == null) {
			ctx.setSendZuulResponse(false);
			ctx.setResponseStatusCode(401);
			 try {
				ctx.getResponse().getWriter().write("token is empty");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		int i = 1/0;
		return null;
	}

	@Override
	public String filterType() {
		return "pre";
	}

	@Override
	public int filterOrder() {
		return 0;
	}
}
```

上面int i = 1/0 会出现异常，但是我们没有做任何处理

```java
package org.boot.filter;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

@Component
public class ErrorFilter extends ZuulFilter{

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public Object run() {
		 RequestContext ctx = RequestContext.getCurrentContext();
	     Throwable throwable = ctx.getThrowable();
	     ctx.set("error.status_code", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	     ctx.set("error.exception", throwable.getCause());
		return null;
	}

	@Override
	public String filterType() {
		return "error";
	}

	@Override
	public int filterOrder() {
		return 0;
	}
}
```

结果返回

```java
{
	"timestamp": 1533699142648,
	"status": 500,
	"error": "Internal Server Error",
	"exception": "java.lang.ArithmeticException",
	"message": "/ by zero"
}
```

## 三、进一步处理

问题：POST阶段的异常由error过滤器处理后并不会调用POST，所以不会被SendErrorFilter输出。

回想一下之前实现的两种异常处理方法，其中非常核心的一点，这两种处理方法都在异常处理时候往请求上下文中添加了一系列的`error.*`参数，而这些参数真正起作用的地方是在`post`阶段的`SendErrorFilter`，在该过滤器中会使用这些参数来组织内容返回给客户端。而对于`post`阶段抛出异常的情况下，由`error`过滤器处理之后并不会在调用`post`阶段的请求，自然这些`error.*`参数也就不会被`SendErrorFilter`消费输出。所以，如果我们在自定义`post`过滤器的时候，没有正确的处理异常，就依然有可能出现日志中没有异常并且请求响应内容为空的问题。我们可以通过修改之前`ThrowExceptionFilter`的`filterType`修改为`post`来验证这个问题的存在，注意去掉`try-catch`块的处理，让它能够抛出异常。

解决上述问题的方法有很多种，比如最直接的我们可以在实现`error`过滤器的时候，直接来组织结果返回就能实现效果，但是这样的缺点也很明显，对于错误信息组织和返回的代码实现就会存在多份，这样非常不易于我们日后的代码维护工作。所以为了保持对异常返回处理逻辑的一致，我们还是希望将`post`过滤器抛出的异常能够交给`SendErrorFilter`来处理。

在前文中，我们已经实现了一个`ErrorFilter`来捕获`pre`、`route`、`post`过滤器抛出的异常，并组织`error.*`参数保存到请求的上下文中。由于我们的目标是沿用`SendErrorFilter`，这些`error.*`参数依然对我们有用，所以我们可以继续沿用该过滤器，让它在`post`过滤器抛出异常的时候，继续组织`error.*`参数，只是这里我们已经无法将这些`error.*`参数再传递给`SendErrorFitler`过滤器来处理了。所以，我们需要在`ErrorFilter`过滤器之后再定义一个`error`类型的过滤器，让它来实现`SendErrorFilter`的功能，但是这个`error`过滤器并不需要处理所有出现异常的情况，它仅对`post`过滤器抛出的异常才有效。根据上面的思路，我们完全可以创建一个继承自`SendErrorFilter`的过滤器，就能复用它的`run`方法，然后重写它的类型、顺序以及执行条件，实现对原有逻辑的复用，具体实现如下：

```java
@Component
public class ErrorExtFilter extends SendErrorFilter {

    @Override
    public String filterType() {
        return "error";
    }

    @Override
    public int filterOrder() {
        return 30;	// 大于ErrorFilter的值
    }

    @Override
    public boolean shouldFilter() {
        // TODO 判断：仅处理来自post过滤器引起的异常
        return true;
    }
}
```

到这里，我们在过滤器调度上的实现思路已经很清晰了，但是又有一个问题出现在我们面前：怎么判断引起异常的过滤器是来自什么阶段呢？（`shouldFilter`方法该如何实现）对于这个问题，我们第一反应会寄希望于请求上下文`RequestContext`对象，可是在查阅文档和源码后发现其中并没有存储异常来源的内容，所以我们不得不扩展原来的过滤器处理逻辑，当有异常抛出的时候，记录下抛出异常的过滤器，这样我们就可以在`ErrorExtFilter`过滤器的`shouldFilter`方法中获取并以此判断异常是否来自`post`阶段的过滤器了。

为了扩展过滤器的处理逻辑，为请求上下文增加一些自定义属性，我们需要深入了解一下Zuul过滤器的核心处理器：`com.netflix.zuul.FilterProcessor`。该类中定义了下面过滤器调用和处理相关的核心方法：

- `getInstance()`：该方法用来获取当前处理器的实例
- `setProcessor(FilterProcessor processor)`：该方法用来设置处理器实例，可以使用此方法来设置自定义的处理器
- `processZuulFilter(ZuulFilter filter)`：该方法定义了用来执行`filter`的具体逻辑，包括对请求上下文的设置，判断是否应该执行，执行时一些异常处理等
- `getFiltersByType(String filterType)`：该方法用来根据传入的`filterType`获取API网关中对应类型的过滤器，并根据这些过滤器的`filterOrder`从小到大排序，组织成一个列表返回
- `runFilters(String sType)`：该方法会根据传入的`filterType`来调用`getFiltersByType(String filterType)`获取排序后的过滤器列表，然后轮询这些过滤器，并调用`processZuulFilter(ZuulFilter filter)`来依次执行它们
- `preRoute()`：调用`runFilters("pre")`来执行所有`pre`类型的过滤器
- `route()`：调用`runFilters("route")`来执行所有`route`类型的过滤器
- `postRoute()`：调用`runFilters("post")`来执行所有`post`类型的过滤器
- `error()`：调用`runFilters("error")`来执行所有`error`类型的过滤器

根据我们之前的设计，我们可以直接通过扩展`processZuulFilter(ZuulFilter filter)`方法，当过滤器执行抛出异常的时候，我们捕获它，并往请求上下中记录一些信息。比如下面的具体实现：

```java
public class DidiFilterProcessor extends FilterProcessor {

    @Override
    public Object processZuulFilter(ZuulFilter filter) throws ZuulException {
        try {
            return super.processZuulFilter(filter);
        } catch (ZuulException e) {
            RequestContext ctx = RequestContext.getCurrentContext();
            ctx.set("failed.filter", filter);
            throw e;
        }
    }
}
```

在上面代码的实现中，我们创建了一个`FilterProcessor`的子类，并重写了`processZuulFilter(ZuulFilter filter)`，虽然主逻辑依然使用了父类的实现，但是在最外层，我们为其增加了异常捕获，并在异常处理中为请求上下文添加了`failed.filter`属性，以存储抛出异常的过滤器实例。在实现了这个扩展之后，我们也就可以完善之前`ErrorExtFilter`中的`shouldFilter()`方法，通过从请求上下文中获取该信息作出正确的判断，具体实现如下：

```java
@Component
public class ErrorExtFilter extends SendErrorFilter {

    @Override
    public String filterType() {
        return "error";
    }

    @Override
    public int filterOrder() {
        return 30;	// 大于ErrorFilter的值
    }

    @Override
    public boolean shouldFilter() {
        // 判断：仅处理来自post过滤器引起的异常
        RequestContext ctx = RequestContext.getCurrentContext();
        ZuulFilter failedFilter = (ZuulFilter) ctx.get("failed.filter");
        if(failedFilter != null && failedFilter.filterType().equals("post")) {
            return true;
        }
        return false;
    }
}
```

到这里，我们的优化任务还没有完成，因为扩展的过滤器处理类并还没有生效。最后，我们需要在应用主类中，通过调用`FilterProcessor.setProcessor(new DidiFilterProcessor());`方法来启用自定义的核心处理器以完成我们的优化目标。



完整代码：

流程POST阶段出错----MyFilterProcessor异常记录出错的filter---ErrorFilter---EXErrorFilter

```java
@EnableZuulProxy
@SpringCloudApplication
public class ZuulApplication {
	public static void main(String[] args) {
		new SpringApplicationBuilder(ZuulApplication.class).web(true).run(args);
		System.out.println("====GatewayzuulApplication start successful==========");
		FilterProcessor.setProcessor(new MyFilterProcessor());
	}
}
```

```java
package org.boot.filter;

import org.springframework.cloud.netflix.zuul.filters.post.SendErrorFilter;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

@Component
public class EXErrorFilter extends SendErrorFilter {

	@Override
	public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        ZuulFilter failedFilter = (ZuulFilter) ctx.get("failed.filter");
        if(failedFilter != null && failedFilter.filterType().equals("post")) {
            return true;
        }
        return false;
	}

	@Override
	public String filterType() {
		return "error";
	}

	@Override
	public int filterOrder() {
		 return 30;	// 大于ErrorFilter的值
	}
}
```

```java
package org.boot.filter;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

@Component
public class PostFilter extends ZuulFilter {

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();
		String parameter = request.getParameter("accessToken");
		System.out.println("========"+parameter);
		
		String responseBody = ctx.getResponseBody();
		System.out.println("========responseBody:"+responseBody);
		int i = 1/0;
		return null;
	}

	@Override
	public String filterType() {
		return "post";
	}

	@Override
	public int filterOrder() {
		return 0;
	}
}
```

```java
package org.boot.filter;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

@Component
public class PreFilter extends ZuulFilter {

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();
		Object accessToken = request.getParameter("accessToken");
		if (accessToken == null) {
			ctx.setSendZuulResponse(false);
			ctx.setResponseStatusCode(401);
			 try {
				ctx.getResponse().getWriter().write("token is empty");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		//int i = 1/0;
		return null;
	}

	@Override
	public String filterType() {
		return "pre";
	}

	@Override
	public int filterOrder() {
		return 0;
	}
}
```

```java
package org.boot.filter;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

@Component
public class RoutingFilter extends ZuulFilter {

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();
		String parameter = request.getParameter("accessToken");
		System.out.println("========"+parameter);
		return null;
	}

	@Override
	public String filterType() {
		return "route";
	}

	@Override
	public int filterOrder() {
		return 0;
	}
}
```

```java
package org.boot.filter;

import org.springframework.stereotype.Component;

import com.netflix.zuul.FilterProcessor;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

@Component
public class MyFilterProcessor extends FilterProcessor{

	 @Override
	    public Object processZuulFilter(ZuulFilter filter) throws ZuulException {
	        try {
	            return super.processZuulFilter(filter);
	        } catch (ZuulException e) {
	            RequestContext ctx = RequestContext.getCurrentContext();
	            ctx.set("failed.filter", filter);
	            throw e;
	        }
	    }
}
```

```java
package org.boot.filter;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

@Component
public class ErrorFilter extends ZuulFilter{
	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public Object run() {
		 RequestContext ctx = RequestContext.getCurrentContext();
	     Throwable throwable = ctx.getThrowable();
	     ctx.set("error.status_code", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	     ctx.set("error.exception", throwable.getCause());
	     ctx.set("error.message", "出错了");
		return null;
	}

	@Override
	public String filterType() {
		return "error";
	}

	@Override
	public int filterOrder() {
		return 0;
	}
}
```

# 六、Zuul处理Cookie和重定向

由于我们在之前所有的入门教程中，对于HTTP请求都采用了简单的接口实现。而实际使用过程中，我们的HTTP请求要复杂的多，比如当我们将Spring Cloud Zuul作为API网关接入网站类应用时，往往都会碰到下面这两个非常常见的问题：

- 会话无法保持
- 重定向后的HOST错误

本文将帮助大家分析问题原因并给出解决这两个常见问题的方法。

### 会话保持问题

我们只需要通过设置sensitiveHeaders即可，设置方法分为两种：

- 全局设置：
  - `zuul.sensitive-headers=`
- 指定路由设置：
  - `zuul.routes.<routeName>.sensitive-headers=`
  - `zuul.routes.<routeName>.custom-sensitive-headers=true`



# 七、远程调用服务，添加headers解决拦截器拦截问题

application.properties加入：

hystrix.command.default.execution.isolation.strategy=SEMAPHORE

```java
package org.config;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import feign.RequestInterceptor;
import feign.RequestTemplate;

@Configuration
public class FeignConfig implements RequestInterceptor {

	@Override
	public void apply(RequestTemplate requestTemplate) {
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if (attributes == null) {
			return;
		}
		HttpServletRequest request = attributes.getRequest();
		Enumeration<String> headerNames = request.getHeaderNames();
		if (headerNames != null) {
			while (headerNames.hasMoreElements()) {
				String name = headerNames.nextElement();
				String values = request.getHeader(name);
				requestTemplate.header(name, values);
			}
		}
		Enumeration<String> bodyNames = request.getParameterNames();
		StringBuffer body = new StringBuffer();
		if (bodyNames != null) {
			while (bodyNames.hasMoreElements()) {
				String name = bodyNames.nextElement();
				String values = request.getParameter(name);
				body.append(name).append("=").append(values).append("&");
			}
		}
//		if (body.length() != 0) {
//			body.deleteCharAt(body.length() - 1);
//			requestTemplate.body(body.toString());
//		}
	}
}

```





# 八、Spring Cloud Hystrix Command属性

主要用来控制 HystrixCommand 命令的行为，主要有下面5种类型的属性配置：

- **execution配置**

  该配置前缀为 hystrix.command.default

  - execution.isolation.strategy ：该属性用来设置执行的隔离策略，有如下二个选项：
    - THREAD：通过线程池隔离的策略，在独立线程上执行，并且他的并发限制受线程池中线程数量的限制（默认）
    - SEMAPHONE：通过信号量隔离的策略，在调用线程上执行，并且他的并发限制受信号量计数的限制。
  - execution.isolation.thread.timeoutInMilliseconds：该属性用来配置 HystrixCommand 执行的超时时间，单位为毫秒，默认值 1000 ，超出此时间配置，Hystrix 会将该执行命令为 TIMEOUT 并进入服务降级处理逻辑
  - execution.timeout.enabled：该属性用来配置 HystrixCommand 执行是否启动超时时间，默认值 true，如果设置为 false，则 execution.isolation.thread.timeoutInMilliseconds 属性的配置将不起作用
  - execution.isolation.thread.interruptOnTimeout：该属性用来配置当 HystrixCommand 执行超时的时候，是否需要将他中断，默认值 true
  - execution.isolation.semaphore.maxConcurrentRequests：当隔离策略使用信号量时，该属性用来配置信号量的大小，当最大并发请求数达到该设置值，后续的请求将会被拒绝

- **fallback配置**

  该配置前缀为 hystrix.command.default

  - fallback.enabled：该属性用来设置服务降级策略是否启用，默认值 true ，如果设置为false，当请求失败或拒绝发生时，将不会调用 HystrixCommand.getFallback() 来执行服务降级逻辑

- **circuitBreaker 配置**

  该配置前缀为 hystrix.command.default

  - circuitBreaker.enabled：该属性用来确定当服务请求命令失败时，是否使用断路器来跟踪其健康指标和熔断请求，默认值 true
  - circuitBreaker.requestVolumeThreshold：该属性用来设置在滚动时间窗中，断路器的最小请求数。例如：默认值 20 的情况下，如果滚动时间窗（默认值 10秒）内仅收到19个请求，即使这19个请求都失败了，断路器也不会打开。
  - circuitBreaker.sleepWindowInMilliseconds：该属性用来设置当断路器打开之后的休眠时间窗。默认值 5000 毫秒，休眠时间窗结束之后，会将断路器设置为"半开"状态，尝试熔断的请求命令，如果依然失败就将断路器继续设置为"打开"状态，如果成功就设置为"关闭"状态。
  - circuitBreaker.errorThresholdPercentage：该属性用来设置断路器打开的错误百分比条件。例如，默认值为 50 的情况下，表示在滚动时间窗中，在请求数量超过 circuitBreaker.requestVolumeThreshold 阈值的请求下，如果错误请求数的百分比超过50，就把断路器设置为"打开"状态，否则就设置为"关闭"状态。
  - circuitBreaker.forceOpen：该属性用来设置断路器强制进入"打开"状态，会拒绝所有请求，该属性优先于 circuitBreaker.forceClosed
  - circuitBreaker.forceClosed：该属性用来设置断路器强制进入"关闭"状态，会接收所有请求。

- **metrics 配置**

  该配置属性与HystrixCommand 和 HystrixObservableCommand 执行中捕获指标信息有关，配置前缀为 hystrix.command.default

  - metrics.rollingStats.timeInMilliseconds：该属性用于设置滚动时间窗的长度，单位毫秒，该时间用于断路器判断健康度时需要收集信息的持续时间，默认值 10000 。断路器值啊收集指标信息时候会根据设置的时间窗长度拆分成多个"桶"来累计各度量值，每个"桶"记录了一段时间内的采集指标。

  - metrics.rollingStats.numBuckets：该属性用来设置滚动时间窗统计指标信息时，划分"桶"的数量，默认值 10 。 metrics.rollingStats.timeInMilliseconds 参数的设置必须能被该参数整除，否则将抛出异常。

    metrics.rollingPercentile.enabled：该属性用来设置对命令执行的延迟是否使用百分位数来跟踪和计算，默认值 true ，如果设置为 false 那么所有概要统计都将返回 -1

  - metrics.rollingPercentile.timeInMilliseconds：该属性用来设置百分位统计的滚动窗口的持续时间，单位：毫秒，默认值 60000

  - metrics.rollingPercentile.numBuckets：该属性用来设置百分位统计窗口中使用"桶"的数量，默认值 6

  - metrics.rollingPercentile.bucketSize：该属性用来设置在执行过程中每个"桶"中保留的最大执行次数，如果在滚动时间窗内发生超该设定值的执行次数，就从最初的位置开始重写，例如：设置为 100，滚动窗口为 10 秒，若在10秒内一个"桶"中发生了500次执行，那么该"桶"中只保留最后的100次执行的统计，默认值 100

  - metrics.healthSnapshot.intervalInMilliseconds：该属性用来设置采集影响断路器状态的健康快照（请求的成功、错误百分比）的间隔等待时间，默认值 500

- **requestContext 配置**

  该配置前缀为 hystrix.command.default

  - requestCache.enabled：该属性用来配置是否开启请求缓存
  - requestLog.enabledg：该属性用来设置 HystrixCommand 的执行和事件是否打印日志到 HystrixRequestLog 中，默认值 true

- **collapser 配置**

  该配置前缀为 hystrix.collapser.default

  - maxRequestsInBatch：该属性用来设置一次请求合并批处理允许的最大请求数量，默认值 Integer.MAX_VALUE
  - timerDelayInMilliseconds：该属性用来设置批处理过程中每个命令延迟的时间，单位毫秒，默认值 10
  - requestCache.enabled：该属性用来设置批处理过程中是否开启请求缓存，默认值 true

  **threadPool 配置**

  该配置前缀为 hystrix.threadpool.default

  - coreSize：该属性用来设置执行命令线程池的核心线程数，该值也就是命令执行的最大并发量，默认值 10
  - maxQueueSize：该属性用来设置线程池的最大队列大小，当设置为 -1 时，线程池将使用 SynchronousQueue 实现的队列，否则使用 LinkedBlockingQueue 实现的队列，默认值 -1
  - queueSizeRejectionThreshold ：该属性用来为队列设置拒绝阈值，即使队列没有到达最大值也能拒绝请求，该属性主要对 LinkedBlockingQueue 队列的补充，默认值 5，当 maxQueueSize 属性为 -1 时候，该属性无效
  - metrics.rollingPercentile.timeInMilliseconds：该属性用来设置线程池统计的滚动窗口的持续时间，单位：毫秒，默认值 10000
  - metrics.rollingPercentile.numBuckets：该属性用来设置线程池统计窗口中使用"桶"的数量，默认值 10



# 九、如何忽略某些服务

如果某些服务不想暴露，则只需如下配置即可

```
zuul.ignored-services=configclient
zuul.ignored-services=*
```



# 十、手动维护实例清单

```
ribbon.eureka.enabled=false #禁用eureka
zuul.routes.configClient.path=/configClient/**  //默认规则
configClient.ribbon.listOfServers=http://localhost:7001  #configClient必须等于上一行的configClient
```



# 十一、使用正则表达规则路由

```java
@Bean
public PatternServiceRouteMapper serviceRouteMapper() {
    return new PatternServiceRouteMapper(
        "(?<name>^.+)-(?<version>v.+$)",
        "${version}/${name}");
}
```

```tex
这个意思是说"myusers-v1"将会匹配路由"/v1/myusers/**". 任何正则表达式都可以, 但是所有组必须存在于servicePattern和routePattern之中. 
如果servicePattern不匹配服务ID，则使用默认行为. 在上面例子中，一个服务ID为“myusers”将被映射到路径“/ myusers/**”（没有版本被检测到）
，这个功能默认是关闭的，并且仅适用于服务注册的服务。
```

```yaml
zuul:
  ignoredPatterns: /**/admin/**
  routes:
    users: /myusers/**
```



# 十二、zuul.prefix

- 要向所有映射添加前缀，请将zuul.prefix设置为一个值，例如/ api。 默认情况下，在转发请求之前，从请求中删除代理前缀（使用zuul.stripPrefix = false关闭此行为）。
- ​

```
zuul.prefix=/api   # 必须带 /开头
```

访问时：http://localhost:6001/api/configClient/consumer01/4560?accessToken=a

如果设置

```
zuul.stripPrefix=false //表示不从请求中删除api前缀
```

此时请求http://localhost:6001/api/configClient/consumer01/4560?accessToken=a

```json
{
  "data": "{\"timestamp\":1533723266832,\"status\":404,\"error\":\"Not Found\",\"message\":\"No   message available\",\"path\":\"/api/consumer01/4560\"}"
}
```

截止https://www.cnblogs.com/520playboy/p/7234218.html



# 十三、查看路由规则

如果使用@EnableZuulProxy与Spring Boot Actuator，您将启用（默认情况下）一个额外的端点，通过HTTP作为/ routes可用。 到此端点的GET将返回映射路由的列表。 POST将强制刷新现有路由（例如，如果服务目录中有更改）

使用zuul已经自动引入了监控依赖，只需要在application.properties里配置：

```
management.security.enabled=false
```

即可访问：http://localhost:6001/routes

如果不让随便访问，则开启HTTP basic认证 

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

```properties
security.user.name=admin
security.user.password=123456
management.security.enabled=true
management.security.role=ADMIN
```

接着在浏览器访问上面的地址提示输入用户名和密码，输入正确后即可访问成功



# 十四、访问安全

访问zuul时，不是每个人都可以调用的，需要认证，此时开启HTTP basic认证

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

```properties
security.user.name=admin
security.user.password=123456
management.security.enabled=true
management.security.role=ADMIN
```

此时访问zuul的任何地址都需要输入用户名和密码



# 十五、本地转发



# 十六、URL重写

```java
package org.boot.filter;

import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

@Component
public class UrlPathFilter extends ZuulFilter{

	@Override
	public boolean shouldFilter() {
		
		final String serviceId = (String) RequestContext.getCurrentContext().get("proxy");
		//上一个filter设置该值
		boolean isOK = RequestContext.getCurrentContext().getBoolean("isOK");
		return "configClient".equals(serviceId) && isOK;
	}

	@Override
	public Object run() {
		
		  RequestContext context = RequestContext.getCurrentContext();
		  Object originalRequestPath = context.get("requestURI");
		  String modifiedRequestPath = "/api/prefix" + originalRequestPath;
		  context.put("requestURI", modifiedRequestPath);
		  return null;
	}

	@Override
	public String filterType() {
		return FilterConstants.PRE_TYPE;
	}

	@Override
	public int filterOrder() {
		return FilterConstants.PRE_DECORATION_FILTER_ORDER + 1;
	}
}
```



```java
package org.boot.filter;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

@Component
public class PreFilter extends ZuulFilter {

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();
		ctx.set("isOK",true);//可以把一些值放到ctx中，便于后面的filter获取使用
		HttpServletRequest request = ctx.getRequest();
		Object accessToken = request.getParameter("accessToken");
		if (accessToken == null) {
			ctx.setSendZuulResponse(false);
			ctx.setResponseStatusCode(401);
			 try {
				ctx.getResponse().getWriter().write("token is empty");
				ctx.set("isOK",false);//可以把一些值放到ctx中，便于后面的filter获取使用
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		System.out.println(request.getHeaderNames());
		return null;
	}

	@Override
	public String filterType() {
		return "pre";
	}

	@Override
	public int filterOrder() {
		return 1;
	}
}
```

上面是在路由之前，我们可以在路由获取真实的URL之后重写访问真正实例的URL



# 十七、filter执行顺序

| 顺序 | 过滤器                  | 功能                       |       |
| ---- | ----------------------- | -------------------------- | ----- |
| -3   | ServletDetectionFilter  | 标记处理servlet的类型      | Pre   |
| -2   | Servlet30WrapperFilter  | 包装httpServletRequest请求 | `     |
| -1   | FormBodyWrapperFilter   | 包装请求体                 | `     |
| 1    | DebugFilter             | 标记调试标志               | `     |
| 5    | PreDecorationFilter     | 处理请求上下文供后续使用   | `     |
|      |                         |                            |       |
| 10   | RibbonRoutingFilter     | ServiceId请求转发          | route |
| 100  | SimpleHostRoutingFilter | Url请求转发                | `     |
| 500  | SendForwardFilter       | Forward请求转发            | `     |
|      |                         |                            |       |
| 0    | SendErrorFilter         | 处理有错误的请求响应       | post  |
| 1000 | SendResponseFilter      | 处理正常处理的请求响应     | `     |



# 十八、zuul动态配置路由规则，从DB读取 

https://blog.csdn.net/hxpjava1/article/details/78304003

# 参考文档

https://segmentfault.com/a/1190000011635768



