package tech.lihx.demo.core.common.exception.support;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import tech.lihx.demo.core.common.code.CodeConstants;
import tech.lihx.demo.core.common.exception.WebException;
import tech.lihx.demo.core.common.response.AppResponse;
import tech.lihx.demo.core.common.util.JsonUtil;

public class AppOrImExceptionHandler extends MailSupport implements HandlerExceptionResolver {

	@Override
	public ModelAndView resolveException(
			HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex ) {
		if ( ex != null ) {
			try {
				String result = "";
				if ( ex instanceof WebException ) {
					WebException e = (WebException) ex;
					if ( CodeConstants.EXCEPTION.equals(e.getCode()) ) {
						log.error("您的程序出错了.", ex);
						send(ex, "app端异常", request.getParameter("token"), null);
					}
					result = JsonUtil.toCompatibleJSON(new AppResponse(e.getCode(), e.getMessage()), null);
				} else {
					log.error("您的程序出错了.", ex);
					send(ex, "app端异常", request.getParameter("token"), null);
					result = JsonUtil.toCompatibleJSON(new AppResponse(CodeConstants.EXCEPTION, "系统异常"), null);
				}

				// response.setCharacterEncoding("UTF-8");
				response.setContentType("text/plain;charset=UTF-8");
				response.getWriter().write(result);
			} catch ( Exception e ) {
				log.error("", e);
			}
		}
		return new ModelAndView();
	}

}
