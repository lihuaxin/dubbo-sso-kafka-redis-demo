package tech.lihx.demo.core.web.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import tech.lihx.demo.core.common.environment.EnvironmentDetect;
import tech.lihx.demo.core.common.environment.RunConfig;
import tech.lihx.demo.core.common.util.DateUtil;
import tech.lihx.demo.core.web.velocity.VelocityUtil;

/**
 * 容器启动时加载一些通用的常量
 * <p>
 * 
 * @author LHx
 * @date 2016年11月23日
 * @version 1.0.0
 */
public class RequestContextListener implements ServletContextListener {

	@Override
	public void contextInitialized( ServletContextEvent sce ) {
		ServletContext context = sce.getServletContext();
		context.setAttribute("vkoUtil", new VelocityUtil());
		context.setAttribute("dateUtil", new DateUtil());
		context.setAttribute("environment", EnvironmentDetect.detectEnvironment());
		WebApplicationContext webApplicationContext = WebApplicationContextUtils
				.getRequiredWebApplicationContext(context);
		RunConfig config = webApplicationContext.getBean(RunConfig.class);
		context.setAttribute("properties", config.getProperties());
	}


	@Override
	public void contextDestroyed( ServletContextEvent sce ) {

	}

}
