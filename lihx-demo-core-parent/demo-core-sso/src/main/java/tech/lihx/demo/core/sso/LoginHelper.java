package tech.lihx.demo.core.sso;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tech.lihx.demo.core.sso.common.CookieHelper;
import tech.lihx.demo.core.sso.common.encrypt.Encrypt;
import tech.lihx.demo.core.sso.common.util.RandomUtil;
import tech.lihx.demo.core.sso.common.util.ReflectUtil;
import tech.lihx.demo.core.sso.exception.KissoException;

/**
 * SSO登录帮助类
 * <p>
 * 
 * @author LHX
 * @Date 2016-5-9
 */
public class LoginHelper {

	private final static Logger logger = LoggerFactory.getLogger(LoginHelper.class);


	/**
	 * @Description 根据Token生成登录信息Cookie
	 * @param request
	 * @param token
	 *            SSO 登录信息票据
	 * @param encrypt
	 *            对称加密算法类
	 * @return Cookie 登录信息Cookie
	 */
	private static Cookie generateCookie( HttpServletRequest request, Token token, Encrypt encrypt ) {
		try {
			Cookie cookie = new Cookie(SSOConfig.getCookieName(), CoreHelper.encryptCookie(request, token, encrypt));
			cookie.setPath(SSOConfig.getCookiePath());
			cookie.setSecure(SSOConfig.getCookieSecure());
			cookie.setDomain(SSOConfig.getCookieDomain());
			/**
			 * 设置Cookie超时时间
			 */
			int maxAge = SSOConfig.getCookieMaxage();
			if ( maxAge >= 0 ) {
				cookie.setMaxAge(maxAge);
			}
			return cookie;
		} catch ( Exception e ) {
			logger.error("generateCookie is exception!", e.toString());
			return null;
		}
	}


	/**
	 * @Description 当前访问域下设置登录Cookie
	 * @param request
	 * @param response
	 * @param encrypt
	 *            对称加密算法类
	 */
	private static void setSSOCookie(
			HttpServletRequest request, HttpServletResponse response, Token token, Encrypt encrypt ) {
		if ( encrypt == null ) { throw new KissoException(" Encrypt not for null."); }
		try {
			Cookie ck = generateCookie(request, token, encrypt);
			if ( SSOConfig.getCookieHttponly() ) {
				/**
				 * Cookie设置HttpOnly
				 */
				CookieHelper.addHttpOnlyCookie(response, ck);
			} else {
				response.addCookie(ck);
			}
		} catch ( Exception e ) {
			logger.error("set HTTPOnly cookie createAUID is exception!", e.toString());
		}
	}


	/**
	 * @Description 当前访问域下设置登录Cookie
	 * @param request
	 * @param response
	 */
	public static void setSSOCookie( HttpServletRequest request, HttpServletResponse response, Token token ) {
		setSSOCookie(request, response, token, ReflectUtil.getConfigEncrypt());
	}


	/**
	 * @Description 当前访问域下设置登录Cookie 设置防止伪造SESSIONID攻击
	 * @param request
	 * @param response
	 */
	public static void authSSOCookie( HttpServletRequest request, HttpServletResponse response, Token token ) {
		CookieHelper.authJSESSIONID(request, RandomUtil.getCharacterAndNumber(8));
		setSSOCookie(request, response, token);
	}

}
