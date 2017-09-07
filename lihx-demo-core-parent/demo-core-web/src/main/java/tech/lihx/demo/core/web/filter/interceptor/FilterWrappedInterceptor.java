package tech.lihx.demo.core.web.filter.interceptor;

import java.io.IOException;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import tech.lihx.demo.core.web.filter.FilterInvocation;

public class FilterWrappedInterceptor implements FilterInterceptor {

	@Override
	public void invoke( FilterInvocation filterInvocation ) throws IOException, ServletException {
		// 原始
		HttpServletRequest request = filterInvocation.getRequest();
		// 自动注入支持
		try {
			ServletRequestAttributes attributes = new ServletRequestAttributes(request);
			LocaleContextHolder.setLocale(request.getLocale());
			RequestContextHolder.setRequestAttributes(attributes);
			filterInvocation.doInterceptor();
		} finally {
			ServletRequestAttributes threadAttributes = (ServletRequestAttributes) RequestContextHolder
					.getRequestAttributes();
			if ( threadAttributes != null ) {
				LocaleContextHolder.resetLocaleContext();
				RequestContextHolder.resetRequestAttributes();
			}
		}

	}


	@Override
	public void init( FilterConfig filterConfig ) {

		// TODO Auto-generated method stub

	}


	@Override
	public void destroy() {

		// TODO Auto-generated method stub

	}

}
