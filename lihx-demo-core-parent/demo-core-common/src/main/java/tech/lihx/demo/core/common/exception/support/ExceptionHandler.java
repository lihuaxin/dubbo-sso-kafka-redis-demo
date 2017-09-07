package tech.lihx.demo.core.common.exception.support;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.HtmlUtils;

import tech.lihx.demo.core.common.code.CodeConstants;
import tech.lihx.demo.core.common.environment.EnvironmentDetect;
import tech.lihx.demo.core.common.exception.WebException;

public class ExceptionHandler extends MailSupport implements HandlerExceptionResolver {

	protected String viewName;

	protected String msgViewName;


	public String buildException( Exception ex ) {
		if ( EnvironmentDetect.detectEnvironment().isProduct() ) { return ex.getMessage(); }
		StringWriter writer = new StringWriter(500);
		ex.printStackTrace(new PrintWriter(writer));
		return HtmlUtils.htmlEscape(writer.toString());
	}


	@Override
	public ModelAndView resolveException(
			HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex ) {
		if ( ex != null ) {
			Map<String, Object> model = new HashMap<String, Object>();
			try {
				model.put("exception", buildException(ex));
				if ( ex instanceof WebException ) {
					WebException e = (WebException) ex;
					if ( !CodeConstants.EXCEPTION.equals(e.getCode()) ) {
						model.put("msg", ex.getMessage());
						if ( msgViewName != null ) { return new ModelAndView(msgViewName, model); }
					} else {
						log.error("您的程序出错了.", ex);
						send(ex, "web层异常", request.getAttribute("ssotoken_attr"), null);
					}
				} else {
					log.error("您的程序出错了.", ex);
					send(ex, "web层异常", request.getAttribute("ssotoken_attr"), null);
				}
				String currentIpAddress = InetAddress.getLocalHost().getHostAddress();
				model.put("currentIpAddress", currentIpAddress);
			} catch ( Exception e ) {
				log.error("", e);
			}
			if ( viewName != null ) { return new ModelAndView(viewName, model); }
		}
		return null;
	}


	public String getViewName() {
		return viewName;
	}


	public void setViewName( String viewName ) {
		this.viewName = viewName;
	}


	public String getMsgViewName() {
		return msgViewName;
	}


	public void setMsgViewName( String msgViewName ) {
		this.msgViewName = msgViewName;
	}


}
