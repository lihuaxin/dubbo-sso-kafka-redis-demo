package tech.lihx.demo.core.common.util;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Spring Context 工具类
 * 
 * @author LHX
 *
 */
public class SpringUtil {

	/**
	 * 获取项目的上下文路径
	 * 
	 * @param request
	 * @return
	 */
	public static String getRealPath( HttpServletRequest request ) {
		WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(request.getSession()
				.getServletContext());
		return appContext.getServletContext().getRealPath("/");
	}
}
