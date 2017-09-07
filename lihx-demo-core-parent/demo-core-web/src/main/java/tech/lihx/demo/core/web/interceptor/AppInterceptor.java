package tech.lihx.demo.core.web.interceptor;

import java.io.IOException;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tech.lihx.demo.core.common.code.Constants;
import tech.lihx.demo.core.sso.common.encrypt.AES;
import tech.lihx.demo.core.sso.common.encrypt.MD5;
import tech.lihx.demo.core.sso.common.util.HttpUtil;
import tech.lihx.demo.core.web.filter.FilterInvocation;
import tech.lihx.demo.core.web.filter.interceptor.FilterInterceptor;

/**
 * 应用登录拦截器
 * <p>
 *
 * @author LHX
 * @date 2016年3月12日
 * @version 1.0.0
 */
public class AppInterceptor implements FilterInterceptor {

	private final static Logger logger = LoggerFactory.getLogger(AppInterceptor.class);

	/**
	 * 初始化 AES 加密算法
	 */
	private static AES aes = new AES(Constants.SECRETKEY);

	private static String OVERURL = null;


	@Override
	public void invoke( FilterInvocation filterInvocation ) throws IOException, ServletException {
		HttpServletRequest req = filterInvocation.getRequest();
		boolean isOver = HttpUtil.inContainURL(req, OVERURL);
		if ( isOver ) {
			/**
			 * 正常执行
			 */
			filterInvocation.doInterceptor();
			return;
		}

		String token = req.getParameter(Constants.TOKEN);
		String sn = req.getParameter(Constants.SIGNATURE);
		if ( StringUtils.isNotBlank(token) && StringUtils.isNotBlank(sn) ) {
			String tk = aes.decryptAES(token);
			if ( tk != null ) {
				String[] tkArr = tk.split(Constants.CUT_SYMBOL);
				if ( tkArr.length >= 2 ) {
					String signatureUrl = HttpUtil.getRequestUrl(req);
					try {
						String signature = MD5.getSignature(
							signatureUrl.substring(0, signatureUrl.indexOf("&sn=")), tkArr[1]);
						if ( sn.equals(signature) ) {
							/**
							 * 正常执行
							 */
							req.setAttribute(Constants.TOKEN_ATRR, tk);
							req.setAttribute(Constants.USERID, tkArr[0]);
							filterInvocation.doInterceptor();
							return;
						}
					} catch ( IOException e ) {
						logger.error(e.getMessage(), e);
					}
				}
			}
		}

		/**
		 * 无访问权限
		 */
		HttpServletResponse res = filterInvocation.getResponse();
		res.sendRedirect("/api/m/noauth.html");
	}


	@Override
	public void init( FilterConfig filterConfig ) {
		logger.info("AppInterceptor init.");
		/**
		 * 获取不需要拦截URL
		 */
		OVERURL = filterConfig.getServletContext().getInitParameter("over.url");
	}


	@Override
	public void destroy() {
		logger.info("AppInterceptor destroy.");
	}
}
