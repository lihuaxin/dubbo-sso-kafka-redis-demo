package tech.lihx.demo.core.web.filter.interceptor;

import java.io.IOException;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import tech.lihx.demo.core.common.web.ISessionCache;
import tech.lihx.demo.core.web.filter.FilterInvocation;
import tech.lihx.demo.core.web.wrap.CachePersistenceSession;
import tech.lihx.demo.core.web.wrap.RequestWrapper;

/**
 * 将session信息放到缓存
 * <p>
 *
 * @author LHX
 * @date 2014年11月21日
 * @version 1.0.0
 */
public class SessionCacheInterceptor implements FilterInterceptor, InitializingBean {

	/**
	 * 单点登录的cookie名称
	 */
	private String cookieName;

	private ISessionCache cache;


	@Override
	public void invoke( final FilterInvocation invocation ) throws IOException, ServletException {
		HttpServletRequest request = invocation.getRequest();
		// 初始化包装的session
		CachePersistenceSession session = new CachePersistenceSession(request, cookieName, cache);
		// 包装request
		RequestWrapper rw = new RequestWrapper(request, invocation.getServletContext(), session);
		invocation.setRequest(rw);
		// 执行其他过滤器
		invocation.doInterceptor();


	}


	public ISessionCache getCache() {
		return cache;
	}


	public void setCache( ISessionCache cache ) {
		this.cache = cache;
	}


	public String getCookieName() {
		return cookieName;
	}


	public void setCookieName( String cookieName ) {
		this.cookieName = cookieName;
	}


	@Override
	public void init( FilterConfig filterConfig ) {


	}


	@Override
	public void destroy() {


	}


	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(cookieName, "cookieName不能为null");
		Assert.notNull(cache, "cache不能为null");

	}

}
