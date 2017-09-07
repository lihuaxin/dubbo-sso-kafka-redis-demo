package tech.lihx.demo.core.sso.common.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tech.lihx.demo.core.sso.SSOConfig;

/**
 * HTTP工具类
 * <p>
 * 
 * @author LHX
 * @Date 2016-5-8
 */
public class HttpUtil {

	private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);


	/**
	 * 获取当前完整请求地址
	 * <p>
	 * 
	 * @param request
	 * @return 请求地址
	 */
	public static String getRequestUrl( HttpServletRequest request ) {
		StringBuffer url = new StringBuffer(request.getScheme());
		// 请求协议 http,https
		url.append("://");
		url.append(request.getHeader("host"));// 请求服务器
		url.append(request.getRequestURI());// 工程名
		if ( request.getQueryString() != null ) {
			// 请求参数
			url.append("?").append(request.getQueryString());
		}
		return url.toString();
	}


	/**
	 * @Description 获取URL查询条件
	 * @param request
	 * @param encode
	 *            URLEncoder编码格式
	 * @return
	 * @throws IOException
	 */
	public static String getQueryString( HttpServletRequest request, String encode ) throws IOException {
		StringBuffer sb = request.getRequestURL();
		String query = request.getQueryString();
		if ( query != null && query.length() > 0 ) {
			sb.append("?").append(query);
		}
		return URLEncoder.encode(sb.toString(), encode);
	}


	/**
	 * @Description getRequestURL是否包含在URL之内
	 * @param request
	 * @param url
	 *            参数为以';'分割的URL字符串
	 * @return
	 */
	public static boolean inContainURL( HttpServletRequest request, String url ) {
		boolean result = false;
		if ( url != null && !"".equals(url.trim()) ) {
			String[] urlArr = url.split(";");
			StringBuffer reqUrl = request.getRequestURL();
			for ( String element : urlArr ) {
				if ( reqUrl.indexOf(element) > 1 ) {
					result = true;
					break;
				}
			}
		}
		return result;
	}


	public static boolean inContainURL( HttpServletRequest request, String[] urlArr ) {
		boolean result = false;
		if ( urlArr != null ) {
			StringBuffer reqUrl = request.getRequestURL();
			for ( String element : urlArr ) {
				if ( reqUrl.indexOf(element) > 1 ) {
					result = true;
					break;
				}
			}
		}
		return result;
	}


	/**
	 * 检测是否包含相应的url
	 * <p>
	 * 
	 * @param request
	 * @param url
	 * @return
	 */
	public static boolean inContainURL( HttpServletRequest request, List<String> url ) {
		boolean result = false;

		StringBuffer reqUrl = request.getRequestURL();
		logger.debug("checkLogin  url : " + reqUrl);
		if ( url != null ) {
			for ( String ignoreUrl : url ) {
				if ( reqUrl.indexOf(ignoreUrl) > 1 ) {
					result = true;
					break;
				}
			}
		}
		return result;
	}


	/**
	 * @Description URLEncoder返回地址
	 * @param url
	 *            跳转地址
	 * @param retParam
	 *            返回地址参数名
	 * @param retUrl
	 *            返回地址
	 * @return
	 */
	public static String encodeRetURL( String url, String retParam, String retUrl ) {
		if ( url == null ) { return null; }
		StringBuffer retStr = new StringBuffer(url);
		retStr.append("?");
		retStr.append(retParam);
		retStr.append("=");
		try {
			retStr.append(URLEncoder.encode(retUrl, SSOConfig.getEncoding()));
		} catch ( UnsupportedEncodingException e ) {
			logger.error(e.getMessage(), e);
		}
		return retStr.toString();
	}


	/**
	 * GET 请求
	 * <p>
	 * 
	 * @param request
	 * @return boolean
	 */
	public static boolean isGet( HttpServletRequest request ) {
		if ( "GET".equalsIgnoreCase(request.getMethod()) ) { return true; }
		return false;
	}


	/**
	 * POST 请求
	 * <p>
	 * 
	 * @param request
	 * @return boolean
	 */
	public static boolean isPost( HttpServletRequest request ) {
		if ( "POST".equalsIgnoreCase(request.getMethod()) ) { return true; }
		return false;
	}
}
