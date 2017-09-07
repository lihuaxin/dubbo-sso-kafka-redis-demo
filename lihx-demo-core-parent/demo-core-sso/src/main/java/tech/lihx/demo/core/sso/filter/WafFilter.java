package tech.lihx.demo.core.sso.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tech.lihx.demo.core.sso.common.util.HttpUtil;
import tech.lihx.demo.core.sso.waf.request.WafRequestWrapper;

/**
 * Waf防火墙过滤器
 * <p>
 * 
 * @author LHX
 * @Date 2016-5-8
 */
public class WafFilter implements Filter {

	private final static Logger logger = LoggerFactory.getLogger(WafFilter.class);

	private static String OVER_URL = null;// 非过滤地址

	private static boolean FILTER_XSS = true;// 开启XSS脚本过滤

	private static boolean FILTER_SQL = true;// 开启SQL注入过滤


	@Override
	public void init( FilterConfig config ) throws ServletException {
		// 读取Web.xml配置地址
		OVER_URL = config.getInitParameter("over.url");

		FILTER_XSS = getParamConfig(config.getInitParameter("filter_xss"));
		FILTER_SQL = getParamConfig(config.getInitParameter("filter_sql_injection"));
		logger.info(" WafFilter init . filter_xss: {} , filter_sql_injection: {} , FilterUrl:{}", new Object[ ] {
				FILTER_XSS, FILTER_SQL, OVER_URL });
	}


	@Override
	public void doFilter( ServletRequest request, ServletResponse response, FilterChain chain ) throws IOException,
		ServletException {
		logger.debug(" WafFilter doFilter .");
		HttpServletRequest req = (HttpServletRequest) request;
		// HttpServletResponse res = (HttpServletResponse) response;

		boolean isOver = HttpUtil.inContainURL(req, OVER_URL);

		/** 非拦截URL、直接通过. */
		if ( !isOver ) {
			logger.debug(" Yes doFilter .");
			try {
				// Request请求XSS过滤
				chain.doFilter(new WafRequestWrapper(req, FILTER_XSS, FILTER_SQL), response);
			} catch ( Exception e ) {
				logger.error(" wafxx.jar WafFilter exception , requestURL: {}", req.getRequestURL());
				logger.error(e.getMessage(), e);
			}
			return;
		}

		chain.doFilter(request, response);
	}


	@Override
	public void destroy() {
		logger.debug(" WafFilter destroy .");
	}


	/**
	 * @Description 获取参数配置
	 * @param value
	 *            配置参数
	 * @return 未配置返回 True
	 */
	private boolean getParamConfig( String value ) {
		if ( value == null || "".equals(value.trim()) ) {
			// 未配置默认 True
			return true;
		}
		return new Boolean(value);
	}
}
