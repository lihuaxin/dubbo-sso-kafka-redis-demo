package tech.lihx.demo.core.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import tech.lihx.demo.core.web.filter.interceptor.FilterInterceptor;
import tech.lihx.demo.core.web.filter.interceptor.FilterInterceptorConfig;

public final class CoreFilter implements Filter {

	private static final Logger logger = LoggerFactory.getLogger(CoreFilter.class);

	private static final String KEY_ENCODING = "encoding";

	// 字符编码
	private String encoding = null;

	// 过滤接口
	private FilterInterceptor[] interceptor = null;

	// Servlet上下文
	private ServletContext context = null;

	public static final String INVOCATION_KEY = "$$spring-mvc.invocation";


	@Override
	public void doFilter( ServletRequest req, ServletResponse res, FilterChain chain ) throws IOException,
		ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		// 设置编码
		if ( encoding != null ) {
			request.setCharacterEncoding(encoding);
		}
		try {
			// String domain = getDomain(request);
			// 缓存的域名
			// CacheKeyPrefix.set(domain);
			DefaultFilterInvocation invocation = null;
			invocation = new DefaultFilterInvocation(request, response, chain, interceptor, context);
			request.setAttribute(INVOCATION_KEY, invocation);
			InvocationUtils.bindRequestToCurrentThread(request);
			// 调用拦截器
			invocation.doInterceptor();
		} finally {
			// CacheKeyPrefix.remove();
			InvocationUtils.unindRequestFromCurrentThread();
		}

	}


	protected String getDomain( HttpServletRequest request ) {
		String url = request.getRequestURL().toString();
		if ( !url.endsWith("/") ) {
			url += "/";
		}
		String scheme = request.getScheme() + "://";
		url = url.substring(scheme.length(), url.length());
		String domain = "";
		if ( url.contains(":") ) {
			domain = url.substring(0, url.indexOf(":"));
		} else {
			domain = url.substring(0, url.indexOf("/"));
		}
		return domain + ":";
	}


	/**
	 * 此方法实现如下如下逻辑：
	 * <p>
	 * 1.动态注入Invocation实现类
	 * <p>
	 * 2.动态初始化过滤器
	 */
	@Override
	public void init( FilterConfig filterConfig ) throws ServletException {
		context = filterConfig.getServletContext();
		encoding = filterConfig.getInitParameter(KEY_ENCODING);
		ConfigurableApplicationContext applicationContext = (ConfigurableApplicationContext) WebApplicationContextUtils
				.getWebApplicationContext(context);
		DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) applicationContext.getBeanFactory();
		BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(InvocationLocalImpl.class);
		beanFactory.registerBeanDefinition("InvocationLocalImpl", builder.getBeanDefinition());
		try {
			interceptor = applicationContext.getBean(FilterInterceptorConfig.class).getFilterInterceptors();
			for ( int i = 0 ; i < interceptor.length ; i++ ) {
				interceptor[i].init(filterConfig);
			}
		} catch ( Exception e ) {
			logger.warn("无拦截器配置");
		}
	}


	@Override
	public void destroy() {
		if ( interceptor != null ) {
			for ( int i = 0 ; i < interceptor.length ; i++ ) {
				interceptor[i].destroy();
			}
		}

	}

}
