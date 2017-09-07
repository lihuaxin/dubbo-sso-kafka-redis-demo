package tech.lihx.demo.core.common.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CookieUtil to simplify cookie operation.
 * 
 * @author LHX
 */
public class CookieUtil {

	@SuppressWarnings( "unused" )
	private static final int MAX_AGE = (-1); // session scope!

	@SuppressWarnings( "unused" )
	private static final Logger logger = LoggerFactory.getLogger(CookieUtil.class);


	public static String encode( String s ) {
		try {
			return URLEncoder.encode(s, "UTF-8");
		} catch ( UnsupportedEncodingException e ) {
			throw new RuntimeException(e);
		}
	}


	public static String decode( String s ) {
		try {
			return URLDecoder.decode(s, "UTF-8");
		} catch ( UnsupportedEncodingException e ) {
			throw new RuntimeException(e);
		}
	}


	public static Cookie getCookie( HttpServletRequest request, String name ) {
		Cookie[] cookies = request.getCookies();
		if ( (cookies == null) || (name == null) || (name.length() == 0) ) { return null; }
		for ( int i = 0 ; i < cookies.length ; ++i ) {
			if ( name.equals(cookies[i].getName()) ) { return cookies[i]; }
		}
		return null;
	}


	public static void deleteCookie( HttpServletRequest request, HttpServletResponse response, Cookie cookie ) {
		if ( cookie != null ) {
			cookie.setPath(getPath(request));
			cookie.setValue("");
			cookie.setMaxAge(0);
			response.addCookie(cookie);
		}
	}


	public static void setCookie( HttpServletRequest request, HttpServletResponse response, String name, String value ) {
		setCookie(request, response, name, value, 31536000);
	}


	/**
	 * 添加cookie
	 * 
	 * @param request
	 * @param response
	 * @param name
	 *            cookie的名称
	 * @param value
	 *            cookie的值
	 * @param maxAge
	 *            cookie存放的时间(以秒为单位,假如存放三天,即3*24*60*60; 如果值为0,cookie将随浏览器关闭而清除)
	 */
	public static void setCookie(
			HttpServletRequest request, HttpServletResponse response, String name, String value, int maxAge ) {
		Cookie cookie = new Cookie(name, (value == null) ? "" : value);
		cookie.setMaxAge(maxAge);
		cookie.setPath(getPath(request));
		response.addCookie(cookie);
	}


	private static String getPath( HttpServletRequest request ) {
		String path = request.getContextPath();
		return (((path == null) || (path.length() == 0)) ? "/" : path);
	}


	public static void main( String[] args ) {
		System.out.println(2592000);
	}
}
