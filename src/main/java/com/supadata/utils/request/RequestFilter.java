
package com.supadata.utils.request;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 类说明: 
 * @ClassName: RequestFilter
 * @author: pengxiuxiao
 * @date: 2017年1月10日 下午5:20:39
*/
public class RequestFilter implements Filter {

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		HttpServletRequest request=(HttpServletRequest) arg0;
		HttpServletResponse response=(HttpServletResponse) arg1;
		String uri = request.getRequestURI();
//		String queryString = request.getQueryString();
		System.out.println("请求地址"+uri);
//		System.out.println("请求参数"+queryString);
//		System.out.println(request.getServletPath());
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods","POST, GET");
		response.setHeader("Access-Control-Allow-Headers","x-requested-with,content-type");
		arg2.doFilter(arg0, arg1);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}

}
