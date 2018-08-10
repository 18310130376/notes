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