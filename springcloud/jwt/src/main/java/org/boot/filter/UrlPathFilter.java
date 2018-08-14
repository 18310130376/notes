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
		return "configClient".equals(serviceId);
	}

	@Override
	public Object run() {
//		
//		  RequestContext context = RequestContext.getCurrentContext();
//		  Object originalRequestPath = context.get("requestURI");
//		  String modifiedRequestPath = "/api/prefix" + originalRequestPath;
//		  context.put("requestURI", modifiedRequestPath);
		  return null;
	}

	@Override
	public String filterType() {
		return FilterConstants.ROUTE_TYPE;
	}

	@Override
	public int filterOrder() {
		return FilterConstants.RIBBON_ROUTING_FILTER_ORDER + 1;
	}
}