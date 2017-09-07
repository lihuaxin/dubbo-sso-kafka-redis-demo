package tech.lihx.demo.core.web.filter.interceptor;

import java.io.IOException;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tech.lihx.demo.core.common.exception.WebException;
import tech.lihx.demo.core.common.response.AppResponse;
import tech.lihx.demo.core.common.util.JsonUtil;
import tech.lihx.demo.core.sso.common.util.HttpUtil;
import tech.lihx.demo.core.web.filter.FilterInvocation;
import tech.lihx.demo.core.web.waf.WafRequestWrapper;


/**
 * 防止注入
 * <p>
 *
 * @author LHX
 * @date 2015年6月5日
 * @version 1.0.0
 */
public class WafInterceptor implements FilterInterceptor {


	protected static Logger logger = LoggerFactory.getLogger(WafInterceptor.class);

	private String[] overUrl = null;// 非过滤地址

	private boolean filterXss = true;// 开启XSS脚本过滤

	private boolean filterSql = true;// 开启SQL注入过滤

	// 返回结果是否是json格式
	private boolean json = false;


	@Override
	public void invoke( FilterInvocation filterInvocation ) throws IOException, ServletException {
		// logger.debug(" WafFilter doFilter .");
		HttpServletRequest req = filterInvocation.getRequest();
		// HttpServletResponse res = (HttpServletResponse) response;

		boolean isOver = HttpUtil.inContainURL(req, overUrl);

		/** 非拦截URL、直接通过. */
		if ( !isOver ) {
			// logger.debug(" Yes doFilter .");
			filterInvocation.setRequest(new WafRequestWrapper(req, filterXss, filterSql, json));
		}
		// 拦截异常
		try {
			filterInvocation.doInterceptor();
		} catch ( WebException e ) {
			if ( json ) {
				HttpServletResponse response = filterInvocation.getResponse();
				String result = JsonUtil.toCompatibleJSON(new AppResponse(e.getCode(), e.getMessage()), null);
				response.setContentType("text/plain;charset=UTF-8");
				response.getWriter().write(result);
			} else {
				throw e;
			}

		}

	}


	@Override
	public void init( FilterConfig filterConfig ) {


	}


	@Override
	public void destroy() {


	}


	public void setOverUrl( String[] overUrl ) {
		this.overUrl = overUrl;
	}


	public String[] getOverUrl() {
		return overUrl;
	}


	public boolean isFilterXss() {
		return filterXss;
	}


	public void setFilterXss( boolean filterXss ) {
		this.filterXss = filterXss;
	}


	public boolean isFilterSql() {
		return filterSql;
	}


	public void setFilterSql( boolean filterSql ) {
		this.filterSql = filterSql;
	}


	public boolean isJson() {
		return json;
	}


	public void setJson( boolean json ) {
		this.json = json;
	}


}
