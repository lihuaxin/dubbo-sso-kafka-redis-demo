package tech.lihx.demo.core.sso.common;

import javax.servlet.http.HttpServletRequest;

import nl.bitwalker.useragentutils.UserAgent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tech.lihx.demo.core.sso.common.encrypt.MD5;

/**
 * 验证浏览器基本信息
 * <p>
 * 
 * @author LHX
 * @Date 2016-5-8
 */
public class Browser {

	private final static Logger logger = LoggerFactory.getLogger(Browser.class);


	/**
	 * @Description 获取浏览器客户端信息签名值
	 * @param request
	 * @return
	 */
	public static String getUserAgent( HttpServletRequest request, String value ) {
		StringBuffer sf = new StringBuffer();
		sf.append(value);
		sf.append("-");
		/**
		 * 混淆浏览器版本信息
		 */
		UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("user-agent"));
		sf.append(userAgent.getBrowser());
		sf.append("-");
		sf.append(userAgent.getBrowserVersion());

		/**
		 * MD5 浏览器版本信息
		 */
		return MD5.toMD5(sf.toString());
	}


	/**
	 * @Description 请求浏览器是否合法 (只校验客户端信息不校验domain)
	 * @param request
	 * @param userAgent
	 *            浏览器客户端信息
	 * @return
	 */
	public static boolean isLegalUserAgent( HttpServletRequest request, String value, String userAgent ) {
		String rlt = getUserAgent(request, value);
		if ( rlt.equalsIgnoreCase(userAgent) ) {
			return true;
		} else {
			logger.debug("Browser getUserAgent:{}", rlt);
			logger.debug("Browser isLegalUserAgent is illegal.");
		}
		return false;
	}
}
