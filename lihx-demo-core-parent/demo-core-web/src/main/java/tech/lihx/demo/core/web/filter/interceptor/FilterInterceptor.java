package tech.lihx.demo.core.web.filter.interceptor;

import java.io.IOException;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;

import tech.lihx.demo.core.web.filter.FilterInvocation;

public interface FilterInterceptor {

	/**
	 * 返回false,不执行后面的过滤器
	 * 
	 * @param filterInvocation
	 * @return
	 */
	public void invoke( FilterInvocation filterInvocation ) throws IOException, ServletException;


	public void init( FilterConfig filterConfig );


	public void destroy();
}
