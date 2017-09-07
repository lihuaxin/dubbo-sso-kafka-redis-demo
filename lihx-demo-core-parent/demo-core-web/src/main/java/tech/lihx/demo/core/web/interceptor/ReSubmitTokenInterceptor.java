package tech.lihx.demo.core.web.interceptor;

import java.lang.reflect.Method;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;


/**
 * 防止重复提交的拦截器
 * <p>
 * 
 * @author LHX
 * @date 2016-4-8
 * @version 1.0.0
 */
public class ReSubmitTokenInterceptor extends HandlerInterceptorAdapter {

	private static final String RE_SUBMIT_CHECKTOKEN = "reSubmitChecktoken";


	@Override
	public boolean preHandle( HttpServletRequest request, HttpServletResponse response, Object handler )
		throws Exception {
		if ( handler instanceof HandlerMethod ) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			Method method = handlerMethod.getMethod();
			ReSubmitToken annotation = method.getAnnotation(ReSubmitToken.class);
			if ( annotation != null ) {
				boolean needSaveSession = annotation.save();
				if ( needSaveSession ) {
					request.getSession(false).setAttribute(RE_SUBMIT_CHECKTOKEN, UUID.randomUUID().toString());
				}
				boolean needRemoveSession = annotation.remove();
				if ( needRemoveSession ) {
					if ( isRepeatSubmit(request) ) {
						response.sendRedirect("/donotResubmit.html");
						return false;
					}
					request.getSession(false).removeAttribute(RE_SUBMIT_CHECKTOKEN);
				}
			}
			return true;
		} else {
			return super.preHandle(request, response, handler);
		}

	}


	private boolean isRepeatSubmit( HttpServletRequest request ) {
		String serverToken = (String) request.getSession(false).getAttribute(RE_SUBMIT_CHECKTOKEN);
		if ( serverToken == null ) { return true; }
		String clinetToken = request.getParameter(RE_SUBMIT_CHECKTOKEN);
		if ( clinetToken == null ) { return true; }
		if ( !serverToken.equals(clinetToken) ) { return true; }
		return false;
	}


}
