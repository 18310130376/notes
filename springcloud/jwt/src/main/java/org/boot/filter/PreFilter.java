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
