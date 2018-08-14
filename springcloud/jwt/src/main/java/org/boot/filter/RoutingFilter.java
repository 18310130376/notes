package org.boot.filter;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

@Component
public class RoutingFilter extends ZuulFilter {

	@Override
	public boolean shouldFilter() {
		final String serviceId = (String) RequestContext.getCurrentContext().get("proxy");
		System.out.println(serviceId);
		return true;
	}

	@Override
	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();
		String parameter = request.getParameter("accessToken");
		System.out.println("========"+parameter);
		System.out.println(request.getHeaderNames());
		return null;
	}

	@Override
	public String filterType() {
		return "route";
	}

	@Override
	public int filterOrder() {
		return 1;
	}
}
