package tech.lihx.demo.core.sso.web;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;


/**
 * 配置启动监听
 * <p>
 * 
 * @author LHX
 * @date 2016年3月11日
 * @version 1.0.0
 */
public class SSOConfigListener implements ServletContextListener {

	@Override
	public void contextInitialized( ServletContextEvent sce ) {
		WebSSOConfigurer.init(sce.getServletContext());
	}


	@Override
	public void contextDestroyed( ServletContextEvent sce ) {
		WebSSOConfigurer.shutdown(sce.getServletContext());
	}

}
