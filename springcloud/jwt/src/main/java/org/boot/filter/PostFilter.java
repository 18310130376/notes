package org.boot.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.netflix.client.http.HttpResponse;
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
		try {
			Object zuulResponse  = ctx.get("ribbonResponse");
			// Object zuulResponse = RequestContext.getCurrentContext().get("zuulResponse");
			if (zuulResponse != null) {
				 HttpResponse resp = (HttpResponse) zuulResponse;
				 // RibbonHttpResponse resp = (RibbonHttpResponse) zuulResponse;
				 //InputStream stream = RequestContext.getCurrentContext().getResponseDataStream();
			     String body = IOUtils.toString(resp.getInputStream());
			     System.err.println(body);
			     String firstValue = resp.getHttpHeaders().getFirstValue("Content-Type");
			     System.out.println(firstValue);
			     resp.close();
			     RequestContext.getCurrentContext().setResponseBody(formatResponseBody(body));
			 }
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String filterType() {
		return "post";
	}

	@Override
	public int filterOrder() {
		return 2;
	}
	
	
	public String formatResponseBody(Object data) {
		
		Gson gson = new Gson();
		Map<String,Object> map = new HashMap();
		map.put("data", data);
		return gson.toJson(map);
	}
}
