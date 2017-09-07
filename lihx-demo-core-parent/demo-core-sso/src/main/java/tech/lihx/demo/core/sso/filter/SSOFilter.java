package tech.lihx.demo.core.sso.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tech.lihx.demo.core.sso.SSOConstant;
import tech.lihx.demo.core.sso.Token;
import tech.lihx.demo.core.sso.client.SSOHelper;
import tech.lihx.demo.core.sso.common.util.HttpUtil;

/**
 * SSO 过滤器验证登录状态
 * <p>
 * 
 * @author LHX
 * @Date 2016-5-8
 */
public class SSOFilter implements Filter {

	private final static Logger logger = LoggerFactory.getLogger(SSOFilter.class);

	private static String OVERURL = null;


	@Override
	public void init( FilterConfig config ) throws ServletException {
		/**
		 * 从应用 web.xml 配置参数中 获取不需要拦截URL
		 */
		OVERURL = config.getInitParameter("over.url");
	}


	@Override
	public void doFilter( ServletRequest request, ServletResponse response, FilterChain chain ) throws IOException,
		ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		boolean isOver = HttpUtil.inContainURL(req, OVERURL);
		/** 非拦截URL、安全校验Cookie */
		if ( !isOver ) {
			if ( !(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse) ) {
				logger.error("SSOFilter not http resource..");
				throw new ServletException("此过滤器只保护HTTP资源");
			}

			Token token = SSOHelper.getToken(req);
			if ( token == null ) {
				/**
				 * 重新登录
				 */
				SSOHelper.login(req, res);
			} else {
				req.setAttribute(SSOConstant.SSO_TOKEN_ATTR, token);
			}
		}
		chain.doFilter(request, response);
	}


	@Override
	public void destroy() {
		OVERURL = null;
	}
}
