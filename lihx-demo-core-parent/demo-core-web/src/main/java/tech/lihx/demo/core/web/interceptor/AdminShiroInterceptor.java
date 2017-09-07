/**
 * ShiroInterceptor.java cn.vko.support.test.web.interceptor Copyright (c) 2014,
 * 北京微课创景教育科技有限公司版权所有.
 */

package tech.lihx.demo.core.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import tech.lihx.demo.core.sso.SSOToken;
import tech.lihx.demo.core.sso.client.SSOHelper;
import tech.lihx.demo.core.support.privilege.Privilege;
import tech.lihx.demo.core.support.privilege.shiro.SSOAuthRealm;
import tech.lihx.demo.core.support.privilege.shiro.SSOAuthToken;

/**
 * shiro权限控制
 * <p>
 *
 * @author LHX
 * 
 * @Date 2015年9月22日
 */
public class AdminShiroInterceptor implements HandlerInterceptor {

	private static final ThreadLocal<Subject> currentSubject = new ThreadLocal<Subject>();

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	private org.apache.shiro.mgt.SecurityManager manager;


	@Override
	public boolean preHandle( HttpServletRequest request, HttpServletResponse response, Object handler )
		throws Exception {
		if ( handler instanceof HandlerMethod ) {
			SSOToken token = (SSOToken) SSOHelper.getToken(request);
			if ( token == null ) { return true; }
			SSOAuthRealm.currentToken.set(token.toString());

			HandlerMethod handlerMethod = (HandlerMethod) handler;
			Privilege privilege = handlerMethod.getMethod().getAnnotation(Privilege.class);
			if ( privilege == null ) { return true; }


			// 每次需要获取用户id
			Subject subject = new Subject.Builder().buildSubject();
			// logger.debug("是否验证登录:{}", subject.isAuthenticated());
			Session session = subject.getSession(false);
			if ( session != null ) {
				session.touch();
			}
			if ( !subject.isAuthenticated() ) {
				subject.login(new SSOAuthToken(token.getUserId(), token.toString()));
				// logger.debug("登录成功");
			}
			boolean isSuccess = false;
			for ( String code : privilege.value() ) {
				if ( subject.isPermitted(code) ) {
					isSuccess = true;
					break;
				}
			}
			if ( isSuccess ) {
				currentSubject.set(subject);
				return isSuccess;
			}
			logger.info("验证失败");
			response.sendError(403, "You have no right to enter!");
			return isSuccess;
		}
		return true;
	}


	@Override
	public void postHandle(
			HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView )
		throws Exception {

	}


	@Override
	public void
			afterCompletion( HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex )
				throws Exception {
		// 移除
		currentSubject.remove();

		SSOAuthRealm.currentToken.remove();
	}


	public org.apache.shiro.mgt.SecurityManager getManager() {
		return manager;
	}


	public void setManager( org.apache.shiro.mgt.SecurityManager manager ) {
		SecurityUtils.setSecurityManager(manager);
		this.manager = manager;
	}

}
