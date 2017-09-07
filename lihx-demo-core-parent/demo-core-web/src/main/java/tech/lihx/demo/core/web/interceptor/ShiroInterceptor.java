package tech.lihx.demo.core.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import tech.lihx.demo.core.common.enums.EnumConstants.UserType;
import tech.lihx.demo.core.sso.SSOToken;
import tech.lihx.demo.core.sso.client.SSOHelper;
import tech.lihx.demo.core.support.privilege.shiro.SSOAuthRealm;
import tech.lihx.demo.core.web.filter.InvocationUtils;

/**
 * shiro权限控制
 * <p>
 *
 * @author LHX
 * @Date 2016年9月22日
 */
public class ShiroInterceptor implements HandlerInterceptor {

	private static final ThreadLocal<Subject> currentSubject = new ThreadLocal<Subject>();

	protected static Logger logger = LoggerFactory.getLogger(ShiroInterceptor.class);

	private SecurityManager manager;


	@Override
	public boolean preHandle( HttpServletRequest request, HttpServletResponse response, Object handler )
		throws Exception {
		// if ( handler instanceof HandlerMethod ) {
		// SSOToken token = (SSOToken) SSOHelper.getToken(request);
		// if ( token == null ) { return true; }
		// SSOAuthRealm.currentToken.set(token.toString());
		// // 学生和家长只判定角色
		// int loginType = Integer.parseInt(token.getLoginType());
		// if ( UserType.ADMIN.key() != loginType ) {
		// String uri = request.getRequestURI();
		// if ( uri.startsWith("/manage/") ) {
		// response.sendError(403, "Forbidden");
		// return false;
		// }
		// }
		// HandlerMethod handlerMethod = (HandlerMethod) handler;
		// Privilege privilege =
		// handlerMethod.getMethod().getAnnotation(Privilege.class);
		// if ( privilege == null ) { return true; }
		//
		// if ( UserType.STUDENT.key() == loginType
		// || UserType.PARENT.key() == loginType || UserType.ADMIN.key() ==
		// loginType ) {
		// // 判定角色
		// for ( String code : privilege.value() ) {
		// if ( code.length() == 9 ) {
		// int int_code = Integer.parseInt(code.substring(3, 6));
		// if ( int_code == loginType ) { return true; }
		// }
		// }
		// }
		// // 记住登录信息
		// // WebSubject subject = new WebSubject.Builder(manager, request,
		// // response).buildWebSubject();
		// // 每次需要获取用户id
		// Subject subject = new Subject.Builder().buildSubject();
		// // logger.debug("是否验证登录:{}", subject.isAuthenticated());
		// Session session = subject.getSession(false);
		// if ( session != null ) {
		// session.touch();
		// }
		// if ( !subject.isAuthenticated() ) {
		// subject.login(new SSOAuthToken(token.getUserId(), token.toString()));
		// // logger.debug("登录成功");
		// }
		// boolean isSuccess = false;
		// for ( String code : privilege.value() ) {
		// if ( subject.isPermitted(code) ) {
		// isSuccess = true;
		// break;
		// }
		// }
		// if ( isSuccess ) {
		// currentSubject.set(subject);
		// return isSuccess;
		// }
		// // 验证特殊
		// if ( validateSpecial(token) ) { return true; }
		// logger.info("验证失败");
		// response.sendError(403, "Forbidden");
		// return isSuccess;
		// }
		return true;
	}


	// private static boolean validateSpecial( SSOToken token ) {
	// try {
	// RedisCache cache = ApplicationUtil.getBean(RedisCache.class);
	// String key = SSOCache.key(token.toString());
	// User user = (User) cache.get(key);
	// RunConfig config = ApplicationUtil.getBean(RunConfig.class);
	// if (
	// config.getProperty("school.special").contains(user.getSchoolId().toString())
	// ) { return true; }
	// } catch ( Exception e ) {
	// logger.error("验证特殊学校失败", e);
	//
	// }
	// return false;
	// }


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


	@SuppressWarnings( "null" )
	public static boolean isPermitted( String code ) {
		SSOToken token = (SSOToken) SSOHelper.getToken(InvocationUtils.getCurrentThreadRequest());
		int loginType = Integer.parseInt(token.getLoginType());
		
		/*
		 * 区分角色
		 * if ( UserType.STUDENT.key() == loginType
				|| UserType.PARENT.key() == loginType || UserType.ADMIN.key() == loginType ) {
			// 判定角色
			if ( code.length() == 9 ) {
				int int_code = Integer.parseInt(code.substring(3, 6));
				if ( int_code == loginType ) { return true; }
			}
		}*/
		Subject subject = currentSubject.get();
		if ( subject == null || code == null ) {
			// // 验证特殊
			// if ( validateSpecial(token) ) { return true; }
			// return false;
		}
		return subject.isPermitted(code);
	}


	public SecurityManager getManager() {
		return manager;
	}


	public void setManager( SecurityManager manager ) {
		SecurityUtils.setSecurityManager(manager);
		this.manager = manager;
	}

}
