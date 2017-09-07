package tech.lihx.demo.core.web.interceptor;

import java.io.IOException;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tech.lihx.demo.core.common.environment.EnvironmentDetect;
import tech.lihx.demo.core.sso.SSOConfig;
import tech.lihx.demo.core.sso.SSOConstant;
import tech.lihx.demo.core.sso.SSOToken;
import tech.lihx.demo.core.sso.Token;
import tech.lihx.demo.core.sso.client.CacheType;
import tech.lihx.demo.core.sso.client.SSOHelper;
import tech.lihx.demo.core.sso.client.handler.SSOHandler;
import tech.lihx.demo.core.sso.common.CookieHelper;
import tech.lihx.demo.core.sso.common.util.HttpUtil;
import tech.lihx.demo.core.sso.common.util.RandomUtil;
import tech.lihx.demo.core.web.filter.FilterInvocation;
import tech.lihx.demo.core.web.filter.interceptor.FilterInterceptor;

/**
 * SSO 登录拦截器
 * 
 * @author LHX
 * @date 2016年9月6日
 * @version 1.0.0
 */
public class SSOInterceptor implements FilterInterceptor {

	private final static Logger LOGGER = LoggerFactory.getLogger(SSOInterceptor.class);

	private final static String HEARTBEAT_COOKIENAME = "hid";

	private static String OVERURL = null;

	protected SSOHandler handler;


	@Override
	public void invoke( FilterInvocation filterInvocation ) throws IOException, ServletException {
		HttpServletRequest req = filterInvocation.getRequest();
		HttpServletResponse res = filterInvocation.getResponse();
		boolean isOver = HttpUtil.inContainURL(req, OVERURL);
		/** 非拦截URL、安全校验Cookie */
		if ( !isOver ) {
			boolean logout = false;
			Token token = SSOHelper.getToken(req);
			if ( token == null ) {
				/**
				 * 重新登录
				 */
				LOGGER.info(" SSOInterceptor loginAgain. ");
				logout = true;
			} else {
				/**
				 * 检查超时
				 */
				SSOToken st = (SSOToken) token;
				if ( (CacheType.NORMAL.getType() == st.getCacheType()) && delayHeartBeat(req, res) ) {
					req.setAttribute(SSOConstant.SSO_TOKEN_ATTR, st);
				} else {
					LOGGER.info(" SSOInterceptor forceOut. ");

					// redirect logout page
					String retUrl = HttpUtil.getQueryString(req, SSOConfig.getEncoding());
					res.sendRedirect(HttpUtil.encodeRetURL(SSOConfig.getLogoutUrl(), "ReturnURL", retUrl) + "&tip=0");
					return;
				}
			}
			/**
			 * 判断是否退出当前登录
			 */
			if ( logout ) {
				SSOHelper.login(req, res);
				return;
			}
			/**
			 * 判定内网ip
			 * */
			if ( EnvironmentDetect.detectEnvironment().isProduct() && token != null ) {
				String ip = token.getUserIp();
				if ( ip != null && ip.startsWith("192.168.") ) {
					SSOHelper.login(req, res);
					return;
				}
			}
		}
		/**
		 * 正常执行
		 */
		filterInvocation.doInterceptor();
	}


	@Override
	public void init( FilterConfig filterConfig ) {
		/**
		 * 获取不需要拦截URL
		 */
		OVERURL = filterConfig.getServletContext().getInitParameter("over.url");
	}


	@Override
	public void destroy() {
		OVERURL = null;
	}


	public SSOHandler getHandler() {
		return handler;
	}


	public void setHandler( SSOHandler handler ) {
		this.handler = handler;
	}


	/**
	 * 延迟心跳
	 * 
	 * @param request
	 * @param response
	 * @return true 心跳正常， false 心跳停止
	 */
	private boolean delayHeartBeat( HttpServletRequest request, HttpServletResponse response ) {
		/**
		 * 心跳cookie存在不判断缓存心跳状态 减少缓存访问次数
		 */
		Cookie bc = CookieHelper.findCookieByName(request, HEARTBEAT_COOKIENAME);
		if ( bc != null ) { return true; }

		/**
		 * 判断是否停止心跳 未停止心跳设置心跳 Cookie
		 */
		if ( !handler.forceOut() ) {
			/**
			 * cookie 比心跳提前 2分钟失效
			 */
			int maxAge = SSOConfig.getLoginLoseTime() - 120;
			CookieHelper.addCookie(
				response, SSOConfig.getCookieDomain(), SSOConfig.getCookiePath(), HEARTBEAT_COOKIENAME,
				RandomUtil.getCharacterAndNumber(10), maxAge, true, false);
			return true;
		}

		/**
		 * 心跳停止
		 */
		return false;
	}
}
