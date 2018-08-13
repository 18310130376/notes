# 一、configClient调用Server安全

## ConfigServer方式一

```xml
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

application.properties里

```properties
security.basic.enabled=true    #启用SpringSecurity的安全配置项
security.user.name=jmxjava    #认证用户名
security.user.password=123456 #认证密码
security.user.role=USER # #授权角色
```

这样的话服务端就配置好了，访问服务端提示输入用户名和密码

查看请求头：

```
Authorization: Basic am14amF2YToxMjM0NTY=
```



## ConfigServer方式二

认证方式独立为一个模块，其他Server引入该模块

单独模块的代码如下：

```java
package com.boot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{

	 @Override
     protected void configure(HttpSecurity http) throws Exception {
         //表示所有的访问都必须进行认证请求处理后才能正常进行
         http.httpBasic().and().authorizeRequests().anyRequest().fullyAuthenticated();
         //设置session为无状态,提升操作效率
         http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
     }
     
     @Autowired
     public void configGlobal(AuthenticationManagerBuilder auth) throws Exception{
         auth.inMemoryAuthentication().withUser("jmxjava").password("123456").roles("USER").and().withUser("admin").password("admin").roles("adminstrator");   
     }
}
```

## configClient

```java
//定义一个Bean修改头信息进行客户端认证
	@Bean
	public HttpHeaders getHeader() {
	    HttpHeaders headers=new HttpHeaders();
	    String auth="jmxjava:123456";//认证的原始信息
	    byte[] encodeAuth=Base64.getEncoder().encode(auth.getBytes(Charset.forName("US-ASCII")));//将原始认证信息进行Base64加密
	    String authHeader="Basic "+new String(encodeAuth);//加密后的认证信息要与Basic有个空格
	    headers.set("Authorization", authHeader);
	    return headers;
	}
```

在controller上注入

```java
@Autowired
private HttpHeaders headers;
	
@RequestMapping(value = "/consumer/dept/add")
public boolean addDept(Dept dept) {
Boolean flag = restTemplate.exchange(DEPT_ADD_URL,HttpMethod.POST,new HttpEntity<Object>(dept,headers),Boolean.class).getBody();
	return flag;
}
```

如果用feign可以写个RequestInterceptor在里面写入header信息

注意,配置文件需要设置`hystrix.command.default.execution.isolation.strategy=SEMAPHORE`

```java
package org.config;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import feign.RequestInterceptor;
import feign.RequestTemplate;

@Configuration
public class FeignConfig implements RequestInterceptor {
	
	@Autowired
	private HttpHeaders headers;

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
		requestTemplate.header("Authorization", headers.toSingleValueMap().get("Authorization"));
		Enumeration<String> bodyNames = request.getParameterNames();
		StringBuffer body = new StringBuffer();
		if (bodyNames != null) {
			while (bodyNames.hasMoreElements()) {
				String name = bodyNames.nextElement();
				String values = request.getParameter(name);
				body.append(name).append("=").append(values).append("&");
			}
		}
	}
}
```

## Session的无状态配置

由于ConfigServer加入了security，在Server打印request.getSession().getId()，发现每次都不一样

说明我们的SpringSecurity默认配置下是不保存用户状态的,如果我们有需求可以修改application.yml的security.session值来设置保存

在Server端配置

```
#always设置保存用户状态(内存可能会被占满)  stateless设置不保存用户状态
security.sessions=always
```

直接访问Server，看到Session保持不变

```
@RequestMapping(value = "/provider/{name}", method = RequestMethod.GET)
 public String provider02(HttpServletRequest request,@PathVariable("name") String name) {
	return request.getSession().getId();
}
```



# 二、Spring Cloud Security（GitHub认证）



**Oauth2的4种模式：**

1、授权码模式(获取code、code换取access_token)

2、简化模式(直接换取access_token，基本不用)

3、密码模式(客户端像用户索取账号密码，然后客户端向服务端索取授权，基本不用)

4、客户端模式（客户端以自己的名义要求"服务提供商"提供服务；场景：提供接口服务)



本文用的是授权码模式。

架构流程：

**1、先注册应用，每个系统都有**

client_id 应用id

client_secret 应用secret

**2、请求授权码code（换取的code其实已经标志了是哪一个用户）**

http://localhost:8080/oauth2-server/authorize?client_id=client_id&client_secret=client_secret&response_type=code&redirect_uri=redirect_uri

参数说明

client_id 应用id

client_secret 应用secret（secret肯定是不能放在url上面的，一般都会把secret当成一个秘钥对传输的参数做一个电子签名，然后到服务端验证这个签名是否合法）

response_type 返回授权码的标识

redirect_uri 回调地址

**3、服务端跳转到登录页面（安全：可以做redirect_uri的域名认证，像微信登录，注册应用时保存域名），用户输入账号密码登录（或者已经登录的用户做授权操作）**

**4、服务端返回授权码，重定向到redirect_uri**

redirect_uri?code=63910432da9186b22b1ad888d55ae8ae

**5、以POST方式提交到http://localhost:8080/oauth2-server/accessToken,最终返回accessToken**

client_id 应用id

client_secret 应用secret

grant_type 用于传递授权码的参数名authorization_code

code 用户登录授权后的授权码

redirect_uri 回调地址

**服务端最终返回如下数据**

{"expires_in":3600,"access_token":"access_token"}

**6、access_token过期校验**

**7、access_token刷新接口**

**8、获取用户信息，见过很多网站，第三方登录后还要注册一次，因为他们拿到了access_token后并没有去获取用户信息，或者获取不到需要的信息，比如说手机、邮箱等信息**

获取用户信息，服务端一般会返回一个唯一标志UUID、用户名、用户头像等用户信息，然后子系统录入这些信息，并把该用户信息和access_token做绑定，子系统也可以对该用户做授权等操作

**为什么授权码模式需要这个授权码**

因为直接返回access_token有可能被黑客拦截到，而拿到access_token就可以获得用户信息了，非常不安全。 

那最容易想到的是，微博通过https返回access_token啊，https会对access_token加密，那不就可以了吗？ 为什么这个方法不可行呢？ 

因为有可能我们的网站不支持https！站在微博的角度来看，千千万万的第三方网站，你要求每个网站都支持https，是不太现实的。那为什么返回code再去获取access_token就安全了呢？ 

**1、用clientId换取code，这一步是跳转到了保存用户信息的应用的，比如说qq、微信、新浪等，为什么需要跳转到这个应用，因为需要获取登录用户的信息（可能保存在浏览器cookie里面），换取code后，需要重定向回用户的应用，这时候，并不是每个应用都支持https的，所以信息并不是加密的。**

但是，另一个问题来了，黑客拿到了code，它也向微博发起请求去获取access_token是不是就可以了呢？ 

**2、这是获取不到accessToken，因为获取accessToken还需要用到secret，而且，这时候请求获取accessToken是https（像QQ、微信、新浪，人家肯定是支持https的），这时候是加密的，就安全了。**



流程说明：

访问本地http://localhost:6001/configClient/consumer01/4560?accessToken=a

发现没登录，则跳转到http://localhost:6001/login ？如何判断

本地login跳转到https://github.com/login/oauth/authorize?client_id=9c984bce16df0cc8679f&redirect_uri=http://localhost:6001/login&response_type=code&state=oekh2a

转到github登录页面https://github.com/login?client_id=9c984bce16df0cc8679f&return_to=%2Flogin%2Foauth%2Fauthorize%3Fclient_id%3D9c984bce16df0cc8679f%26redirect_uri%3Dhttp%253A%252F%252Flocalhost%253A6001%252Flogin%26response_type%3Dcode%26state%3Doekh2a

输入用户名密码后：

Github登陆成功调转到http://localhost:6001/login?code=e24fac25417760ed7a00&state=oekh2a

本都登陆成功后跳转到http://localhost:6001/configClient/consumer01/4560?accessToken=a



搭建

访问https://github.com/settings/developers  

点击Register a new OAuth application

两个URL写入Zuul应用的地址

记下：clientId和clientSecret

在pom引入

```
<dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-starter-oauth2</artifactId>
</dependency>
<dependency>
     <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-starter-security</artifactId>
</dependency>
```

```java
package org.boot;

import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.stereotype.Component;

@Component
@EnableOAuth2Sso // 实现基于OAuth2的单点登录，建议跟踪进代码阅读以下该注解的注释，很有用
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.antMatcher("/**")
				// 所有请求都得经过认证和授权
				.authorizeRequests().anyRequest().authenticated().and().authorizeRequests().antMatchers("/", "/anon")
				.permitAll().and()
				// 这里之所以要禁用csrf，是为了方便。
				// 否则，退出链接必须要发送一个post请求，请求还得带csrf token
				// 那样我还得写一个界面，发送post请求
				.csrf().disable()
				// 退出的URL是/logout
				.logout().logoutUrl("/logout").permitAll()
				// 退出成功后，跳转到/路径。
				.logoutSuccessUrl("/login");
	}
}
```

```
security.user.password=user
security.ignored=/
security.sessions=never

security.oauth2.sso.loginPath=/login
security.oauth2.client.clientId=9c984bce16df0cc8679f
security.oauth2.client.clientSecret=4b1d55aaf32a66818e5a3c7afcff04b355f1e6f2

security.oauth2.client.accessTokenUri=https://github.com/login/oauth/access_token
security.oauth2.client.userAuthorizationUri=https://github.com/login/oauth/authorize

security.oauth2.resource.userInfoUri=https://api.github.com/user
security.oauth2.resource.preferTokenInfo=false
```

此时访问：http://localhost:6001/configClient/consumer01/4560?accessToken=a

会转到github登录页面，登陆成功后输出正确内容

编写controller

```java
@GetMapping("/")
public String welcome() {
		return "welcome";
}

@RequestMapping("/user")
	public Principal user(Principal user) {
		return user;
}
```

访问：http://localhost:6001/user

输出了Github用户信息





# 三、API网关统一认证

只能保证入口的权限，不能保证内部服务之间的调用安全



# 四、服务之间的安全

如同方式一也是服务之前的安全



用户输入账号密码成功后----返回JWT生成的Token，放到请求头Authorization里，每次经过网关Pre，先看Authorization有没有token，没有则返回，有的话则获取最新token，传递到下游，下游根据filter再次校验token。

```java
//放在zuul就可以了
@Override
  public Object run() {
  RequestContext ctx = RequestContext.getCurrentContext();
  ctx.addZuulRequestHeader("Authorization", TokenScheduledTask.token);
  return null;
}
```

```java
/**
   放在zuul就可以了
 * 定时刷新token
 **/
@Component
public class TokenScheduledTask {
    private static Logger logger = LoggerFactory.getLogger(TokenScheduledTask.class);

    public final static long ONE_Minute = 60 * 1000 * 60 * 20;

    public static String token = "";

    @Autowired
    private AuthService authService;

    /**
     * 刷新Token
     */
    @Scheduled(fixedDelay = ONE_Minute)
    public void reloadApiToken() {
        token = authService.getToken();
        while (StringUtils.isBlank(token)) {
            try {
                Thread.sleep(1000);
                token = authService.getToken();
            } catch (InterruptedException e) {
                logger.error("", e);
            }
        }
    }
}
```



# 五、JWT认证

编程微服务模式了，cookie和session机制已经不能很好的满足保护API的需求了，更多的情况下采用token的验证机制，JWT的本质也是一种token。

JWT：JSON Web Token，是JSON风格的轻量级授权和认证规范，可以实现无状态，分布式的web应用授权。JWT的内容由三部分组成，分别是Header,Payload,Signature，三个部分之间通过.分割,举例

xxxxx.yyyyyy.zzzzz

Header

头部Header一般由2个部分组成alg和typ,alg是加密算法，如HMAC或SHA256,typ是token类型，取值为jwt,一个Header的例子

{

"alg": "HS256",

"typ": "JWT"

}

然后对Header部分进行Base64编码，得到第一部分的值

eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.{PAYLOAD}.{SIGNATURE}

Payload

内容部分Payload是JWT存储信息的主体，包含三类内容

- 标准中注册的声明
- 公共的声明
- 私有的声明

标准中注册的声明

- iss:jwt签发者
- sub:jwt所面向的用户
- aud:接收jwt的一方
- exp:jwt的过期时间
- nbf:定义在什么时间之前该jwt是不可用的
- iat:jwt的签发时间
- jti:jwt的唯一标识，主要用作一次性token，避免重放攻击

公共的声明：

可以存放任何信息，根据业务实际需要添加，如用户id，名称等，但不要存放敏感信息

私有的声明：

私有声是提供者和消费者所共同定义的声明，不建议存放敏感信息

举例：定义一个payload:

{ "sub": "1234567890", "name": "John Doe", "admin": true }对其进行Base64编码，得到第二部分

eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWV9.{SIGNATURE}

Signature

token的签名部分由三部分组成256签名

var encodedString = base64UrlEncode(header) + '.' + base64UrlEncode(payload);

var signature = HMACSHA256(encodedString, 'secret');

得到最终的token串

eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWV9.TJVA95OrM7E2cBab30RMHrHDcEfxjoYZgeFONFh7HgQ

```
header.payload.signature
```



###  普通方法

第一部分`{"alg":"HS512"}`是签名算法

第二部分 `{"exp":1495176357,"username":"admin"}`是一些数据(你想放什么都可以), 这里有过期日期和用户名

第三部分`')4'7�6-DM�(�H6fJ::$c���a4�~tI2%Xd-�$nL(l`非常重要,是签名Signiture, 服务器会验证这个以防伪造. 因为JWT其实是明文传送, 任何人都能篡改里面的内容. 服务端通过验证签名, 从而确定这个JWT是自己生成的.

原理也不是很复杂, 我用一行代码就能表示出来

首先我们将JWT第一第二部分的内容, 加上你的秘钥(key或者叫secret), 然后用某个算法(比如hash算法)求一下, 求得的内容就是你的签名. 验证的时候只需要验证你用JWT算出来的值是否等于JWT里面的签名.

因为别人没有你的key, 所以也就没法伪造签名

简单粗暴一行代码解释什么是签名:

```
 int signiture = ("{alg:HS512}{exp:1495176357,username:admin}" + key).hashCode();
```

最后附上签名,得到完整的JWT:

```
{"alg":"HS512"}{"exp":1495176357,"username":"admin"}    ')4'7�6-DM�(�H6fJ::$c���a4�~tI2%Xd-�$nL(l
```

为了方便复制和使用, 通常我们都是把JWT用base64编码之后放在http的header里面, 并且每一次呼叫api都附上这个JWT, 并且服务器每次也验证JWT是否过期

通常我们用到的JWT:

```perl
Base64编码后:
eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjE0OTUxNzYzNTcsInVzZXJuYW1lIjoiYWRtaW4ifQ.mQtCfLKfI0J7c3HTYt7kRN4AcoixiUSDaZv2ZKOjq2JMZjBhf1DmE0Fn6PdEkyJZhYZJTMLaIPwyR-uu6BMKGw
```

```java
package org.boot.controller;
import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtAuthenticationFilter extends OncePerRequestFilter{
	
	  private static final PathMatcher pathMatcher = new AntPathMatcher();

	    @Override
	    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
	        try {
	            if(isProtectedUrl(request)) {
//	                String token = request.getHeader("Authorization");
//	                //检查jwt令牌, 如果令牌不合法或者过期, 里面会直接抛出异常, 下面的catch部分会直接返回
//	                JwtUtil.validateToken(token);
	            	 request = JwtUtil.validateTokenAndAddUserIdToHeader(request);
	            }
	        } catch (Exception e) {
	            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
	            return;
	        }
	        //如果jwt令牌通过了检测, 那么就把request传递给后面的RESTful api
	        filterChain.doFilter(request, response);
	    }
	    //我们只对地址 /api 开头的api检查jwt. 不然的话登录/login也需要jwt
	    private boolean isProtectedUrl(HttpServletRequest request) {
	        return pathMatcher.match("/api/**", request.getServletPath());
	    }
}
```

```java
package org.boot.controller;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtUtil {

	private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

	static final String SECRET = "ThisIsASecret";

	static final Long Expiration_TimeMillis = 3 * 60 * 1000L;

	static final String TOKEN_PREFIX = "Bearer ";

	static final String HEADER_STRING = "Authorization";

	public static String generateToken(String username) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("username", username);
		map.put("userId", 100);
		map.put("role", "admin");
		String jwt = Jwts.builder().setClaims(map)
				.setExpiration(new Date(System.currentTimeMillis() + Expiration_TimeMillis))

				.signWith(SignatureAlgorithm.HS512, SECRET).compact();
		return TOKEN_PREFIX + jwt; // jwt前面一般都会加Bearer
	}

	public static void validateToken(String token) {
		try {
			Map<String, Object> body = Jwts.parser().setSigningKey(SECRET)
					.parseClaimsJws(token.replace(TOKEN_PREFIX, "")).getBody();
			System.out.println(body);
		} catch (Exception e) {
			throw new IllegalStateException("Invalid Token. " + e.getMessage());
		}
	}

	public static HttpServletRequest validateTokenAndAddUserIdToHeader(HttpServletRequest request) {
		String token = request.getHeader(HEADER_STRING);
		if (token != null) {
			try {
				Map<String, Object> body = Jwts.parser().setSigningKey(SECRET)
						.parseClaimsJws(token.replace(TOKEN_PREFIX, "")).getBody();
				return new CustomHttpServletRequest(request, body);
			} catch (Exception e) {
				logger.info(e.getMessage());
				throw new TokenValidationException(e.getMessage());
			}
		} else {
			throw new TokenValidationException("Missing token");
		}
	}

	public static class CustomHttpServletRequest extends HttpServletRequestWrapper {

		private Map<String, String> claims;

		public CustomHttpServletRequest(HttpServletRequest request, Map<String, ?> claims) {
			super(request);
			this.claims = new HashMap<>();
			claims.forEach((k, v) -> this.claims.put(k, String.valueOf(v)));
		}

		@Override
		public Enumeration<String> getHeaders(String name) {
			if (claims != null && claims.containsKey(name)) {
				return Collections.enumeration(Arrays.asList(claims.get(name)));
			}
			return super.getHeaders(name);
		}

		public Map<String, String> getClaims() {
			return claims;
		}
	}

	static class TokenValidationException extends RuntimeException {
		public TokenValidationException(String msg) {
			super(msg);
		}
	}
}
```

```java
package org.boot.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@SuppressWarnings("all")
@RestController
public class UserController {

	@GetMapping("/api/protected")
	public @ResponseBody Object hellWorld(@RequestHeader(value = "userId") String userId) {
		return "Hello World! This is a protected api,your userId="+userId;
	}
	
	@PostMapping("/login")
    public Object login(HttpServletResponse response, @RequestBody final Map<String,Object> account) throws IOException {
        if(isValidPassword(account)) {
            final String jwt = JwtUtil.generateToken((String)(account.get("username")));
            return new HashMap<String,String>(){{
                put("token", jwt);
            }};
        }else {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
    }
	
	
	/**
	 *模拟登陆查询数据库
	 **/
	private boolean isValidPassword(Map<String,Object> account) {
	        return "admin".equals(account.get("username")) && "admin".equals(account.get("password"));
	 }
}
```

```java
	@Bean
	public FilterRegistrationBean jwtFilter() {
		final FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		JwtAuthenticationFilter filter = new JwtAuthenticationFilter();
		registrationBean.setFilter(filter);
		return registrationBean;
	}
```

```xml
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt</artifactId>
    <version>0.7.0</version>
</dependency>
```

### 整合Security

#### 方式一、简单的集成

利用上面的代码，在过滤器中从token解析出role，放进security的上下文，因为这里登录用上面的，security只做鉴权

```xml
<dependency>
	<groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

```java
package org.boot.controller;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers(HttpMethod.POST, "/login").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(),
                        UsernamePasswordAuthenticationFilter.class);
    }
}
```

关键代码setAuthentication，用户名密码因为没用框架的，这里只是做鉴权操作，用到角色信息

```java
package org.boot.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtAuthenticationFilter extends OncePerRequestFilter{
	
	  private static final PathMatcher pathMatcher = new AntPathMatcher();

	    @Override
	    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
	        try {
	            if(isProtectedUrl(request)) {
	            	Map<String, Object> claims = JwtUtil.validateTokenAndGetClaims(request);
	                String role = String.valueOf(claims.get("role"));

	            	 SecurityContextHolder.getContext().setAuthentication(
	                         new UsernamePasswordAuthenticationToken(
	                                 null, null, Arrays.asList(() -> role)));
	            }
	        } catch (Exception e) {
	            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
	            return;
	        }
	        //如果jwt令牌通过了检测, 那么就把request传递给后面的RESTful api
	        filterChain.doFilter(request, response);
	    }
	    //我们只对地址 /api 开头的api检查jwt. 不然的话登录/login也需要jwt
	    private boolean isProtectedUrl(HttpServletRequest request) {
	        return pathMatcher.match("/api/**", request.getServletPath());
	    }
}
```

```java
  public static Map<String, Object> validateTokenAndGetClaims(HttpServletRequest request) {
	        String token = request.getHeader(HEADER_STRING);
	        if (token == null)
	            throw new TokenValidationException("Missing token");
	        // parse the token. exception when token is invalid
	        Map<String, Object> body = Jwts.parser()
	                .setSigningKey(SECRET)
	                .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
	                .getBody();
	        return body;
	    }
```

```java
	package org.boot.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@SuppressWarnings("all")
@RestController
public class UserController {

	@PreAuthorize("hasAuthority('admin')")
	@GetMapping("/api/protected")
	public @ResponseBody Object hellWorldAdmin() {
		return "Hello World! This is a protected api,you are admin";
	}
	
	@PreAuthorize("hasAuthority('user')")
	@GetMapping("/api/protected/user")
	public @ResponseBody Object hellWorldUser() {
		return "Hello World! This is a protected api,your are user";
	}
	
	static Map<String,Object> userMap = null;
	
	static {
		userMap = new HashMap();
		userMap.put("user", new User("user", "user", "user"));
		userMap.put("admin",new User("admin", "admin", "admin"));
	}
	
	static class User implements Serializable{
		
		private String username;
		private String password;
		private String role;
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		public String getRole() {
			return role;
		}
		public void setRole(String role) {
			this.role = role;
		}
		
		User(String username,String password,String role){
			this.username = username;
			this.password = password;
			this.role = role;
		}
	}
	
	@PostMapping("/login")
    public Object login(HttpServletResponse response, @RequestBody final Map<String,Object> account) throws IOException {
        if(isValidPassword(account)) {
            final String jwt = JwtUtil.generateToken(account);
            return new HashMap<String,String>(){{
                put("token", jwt);
            }};
        }else {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
    }
	
	/**
	 *模拟登陆查询数据库
	 **/
	private boolean isValidPassword(Map<String,Object> account) {
		return userMap.containsKey(account.get("username"));
	 }
}
```

#### 方式二、整合security

##### 引入依赖

```xml
<dependency>
   <groupId>io.jsonwebtoken</groupId>
   <artifactId>jjwt</artifactId>
   <version>0.7.0</version>
</dependency>

<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-security</artifactId>
</dependency>
```







参考：

https://www.jianshu.com/p/e62af4a217eb



https://www.jianshu.com/p/6307c89fe3fa



https://github.com/shuaicj/zuul-auth-example