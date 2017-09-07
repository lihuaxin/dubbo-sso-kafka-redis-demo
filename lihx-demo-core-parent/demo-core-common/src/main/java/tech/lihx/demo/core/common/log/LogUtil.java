package tech.lihx.demo.core.common.log;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;

@SuppressWarnings( "unchecked" )
public class LogUtil {

	public static String getHeaderInfo( HttpServletRequest request ) {
		// 获得所有头信息 key值集合
		StringBuilder str = new StringBuilder();
		Enumeration<String> enumeration1 = request.getHeaderNames();
		while ( enumeration1.hasMoreElements() ) {
			String key = enumeration1.nextElement();
			if ( "Cookie".equals(key) || "Accept".equals(key) || "Accept-Language".equals(key) ) {
				continue;
			}
			if ( "Connection".equals(key) || "Cache-Control".equals(key) || "Accept-Encoding".equals(key) ) {
				continue;
			}
			Enumeration<String> enumeration2 = request.getHeaders(key);
			while ( enumeration2.hasMoreElements() ) {
				String value = enumeration2.nextElement();
				str.append(key);
				str.append(":");
				str.append(value);
				str.append(";");
			}
		}
		return str.toString();
	}


	public static String getParameterInfo( HttpServletRequest request ) {
		return JSON.toJSONString(request.getParameterMap());
	}


	public static String getRequestAttributeInfo( HttpServletRequest request ) {
		StringBuilder str = new StringBuilder();
		Enumeration<String> attrs = request.getAttributeNames();
		while ( attrs != null && attrs.hasMoreElements() ) {
			String attr = attrs.nextElement();
			String value = null;
			if ( attr.startsWith("org.springframework.") || attr.startsWith("$$") || attr.startsWith("javax.servlet.") ) {
				continue;
			}
			if ( "roseInvocation".equals(attr) || "ctxpath".equals(attr) ) {
				continue;
			}
			if ( attr.startsWith("__jsp") ) {
				continue;
			}
			try {
				value = JSON.toJSONString(request.getAttribute(attr));
			} catch ( Exception e ) {
				continue;
			}
			str.append(attr);
			str.append(":");
			str.append(value);
			str.append(";");
		}
		return str.toString();
	}


	public static String getSessionAttributeInfo( HttpServletRequest request ) {
		StringBuilder str = new StringBuilder();
		Enumeration<String> attrs = request.getSession().getAttributeNames();
		while ( attrs != null && attrs.hasMoreElements() ) {
			String attr = attrs.nextElement();
			str.append(attr);
			str.append(":");
			str.append(JSON.toJSONString(request.getSession().getAttribute(attr)));
			str.append(";");
		}
		return str.toString();
	}


	public static String getExceptionInfo( HttpServletRequest request ) {
		Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
		String message = (String) request.getAttribute("javax.servlet.error.message");
		// String servletName = (String)
		// request.getAttribute("javax.servlet.error.servlet_name");
		// String uri = (String)
		// request.getAttribute("javax.servlet.error.request_uri");
		// Throwable t = (Throwable)
		// request.getAttribute("javax.servlet.error.exception");
		// Class exception = (Class)
		// request.getAttribute("javax.servlet.error.exception_type");
		if ( statusCode != null ) {
			StringBuilder str = new StringBuilder();
			str.append("status_code:");
			str.append(statusCode);
			str.append(";");
			str.append("message:");
			str.append(message);
			str.append(";");
			return str.toString();
		}
		return null;
	}


	public static String getInfo( HttpServletRequest request ) {
		String remoteAddr = request.getRemoteHost();// request.getRemoteAddr()
		StringBuilder str = new StringBuilder("\r\nremoteAddr:[");
		str.append(remoteAddr);
		str.append("]\r\n");
		str.append("method:[");
		str.append(request.getMethod());
		str.append("]");
		str.append("\r\nURI:[");
		str.append(request.getRequestURI());
		String query = request.getQueryString();
		if ( query != null ) {
			str.append("?");
			str.append(query);
		}
		str.append("]\r\n");
		str.append("Headers:[");
		str.append(getHeaderInfo(request));
		str.append("]\r\n");
		str.append("Parameters:[");
		str.append(getParameterInfo(request));
		str.append("]\r\n");
		// if (request.getQueryString() != null) {
		// str.append("QueryString:[");
		// str.append(request.getQueryString());
		// str.append("]\r\n");
		// }
		String refferer = request.getHeader("Refferer");
		if ( refferer != null ) {
			str.append("Refferer:[");
			str.append(refferer);
			str.append("]\r\n");
		}
		// str.append("RequestAttrs:[");
		// str.append(getRequestAttributeInfo(request));
		// str.append("]\r\n");
		// str.append("SessionAttrs:[");
		// str.append(getSessionAttributeInfo(request));
		// str.append("]\r\n");
		String exceptionInfo = getExceptionInfo(request);
		if ( exceptionInfo != null ) {
			str.append("Error:[");
			str.append(exceptionInfo);
			str.append("]\r\n");
		}
		return str.toString();
	}
}
