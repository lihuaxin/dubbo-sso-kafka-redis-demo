package tech.lihx.demo.core.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * 拦截器,预处理或在页面渲染前填充某些数据
 * <p>
 *
 * @author LHX
 * @date 2016年12月3日
 * @version 1.0.0
 */
public class PreparableInterceptor implements HandlerInterceptor {


	@Override
	public void
			afterCompletion( HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex )
				throws Exception {
		if ( handler instanceof HandlerMethod ) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			if ( handlerMethod.getBean() instanceof HandlerInterceptor ) {
				((HandlerInterceptor) handlerMethod.getBean()).afterCompletion(request, response, handler, ex);
			}

		}
	}


	@Override
	public void postHandle(
			HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView )
		throws Exception {
		if ( handler instanceof HandlerMethod ) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			if ( handlerMethod.getBean() instanceof HandlerInterceptor ) {
				((HandlerInterceptor) handlerMethod.getBean()).postHandle(request, response, handler, modelAndView);
			}

		}
	}


	@Override
	public boolean preHandle( HttpServletRequest request, HttpServletResponse response, Object handler )
		throws Exception {
		if ( handler instanceof HandlerMethod ) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			if ( handlerMethod.getBean() instanceof HandlerInterceptor ) { return ((HandlerInterceptor) handlerMethod
					.getBean()).preHandle(request, response, handler); }
		}
		return true;

	}
}
