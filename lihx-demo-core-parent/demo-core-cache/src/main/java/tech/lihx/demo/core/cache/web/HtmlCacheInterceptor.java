/**
 * ShiroInterceptor.java cn.vko.test.web.interceptor Copyright (c) 2014,
 * 北京微课创景教育科技有限公司版权所有.
 */

package tech.lihx.demo.core.cache.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import tech.lihx.demo.core.cache.annotation.Cacheable;

/**
 * html浏览器端缓存,通过设置header
 * <p>
 * 
 * @author lihx
 * @Date 2017年9月5日
 */
public class HtmlCacheInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle( HttpServletRequest request, HttpServletResponse response, Object handler )
		throws Exception {

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
		if ( ex == null && handler instanceof HandlerMethod ) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			Cacheable cache = handlerMethod.getMethod().getAnnotation(Cacheable.class);
			if ( cache != null ) {
				response.setHeader("Cache-Control", "max-age=" + cache.expire());
			}
		}
	}

}
