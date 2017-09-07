package tech.lihx.demo.core.web.filter.interceptor;

import java.io.IOException;

import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import tech.lihx.demo.core.common.environment.EnvironmentDetect;
import tech.lihx.demo.core.common.environment.RunConfig;
import tech.lihx.demo.core.common.util.DateUtil;
import tech.lihx.demo.core.web.filter.FilterInvocation;
import tech.lihx.demo.core.web.velocity.VelocityUtil;

/**
 * 加载一些公用的对象到servletcontext
 * <p>
 * 
 * @author LHX
 * @Date 2017-9-6
 */
public class ServletContextInterceptor implements FilterInterceptor {

	protected final Logger logger = LoggerFactory.getLogger(ServletContextInterceptor.class);

	// 用于拼接绝对路径
	public final static String SERVER_CONTEXT_PATH = "serverContextPath";


	@Override
	public void invoke( FilterInvocation filterInvocation ) throws IOException, ServletException {
		HttpServletRequest request = filterInvocation.getRequest();
		String prefix = getPrefix(request);
		request.setAttribute(SERVER_CONTEXT_PATH, prefix);
		// logger.debug(prefix);
		filterInvocation.doInterceptor();
	}


	protected String getPrefix( HttpServletRequest request ) {
		String path = request.getContextPath();
		String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path;
		return basePath;
	}


	@Override
	public void init( FilterConfig filterConfig ) {
		ServletContext context = filterConfig.getServletContext();
		// 加载velocity工具类
		context.setAttribute("vkoUtil", new VelocityUtil());
		context.setAttribute("dateUtil", new DateUtil());
		context.setAttribute("environment", EnvironmentDetect.detectEnvironment());
		WebApplicationContext webApplicationContext = WebApplicationContextUtils
				.getRequiredWebApplicationContext(context);
		RunConfig config = webApplicationContext.getBean(RunConfig.class);
		// CollectionUtils.mergePropertiesIntoMap(config.getProperties(),
		// config.getProperties());
		// 系统配置文件
		context.setAttribute("properties", config.getProperties());
	}


	@Override
	public void destroy() {

	}

}
