package tech.lihx.demo.core.web.appversion.web;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;


/**
 * app 版本配置启动监听
 * <p>
 * 
 * @author LHX
 * @date 2015年4月28日
 * @version 1.0.0
 */
public class AppVersionConfigListener implements ServletContextListener {

	@Override
	public void contextInitialized( ServletContextEvent sce ) {
		AppVersionConfigurer.init(sce.getServletContext());
	}


	@Override
	public void contextDestroyed( ServletContextEvent sce ) {
		AppVersionConfigurer.shutdown(sce.getServletContext());
	}

}
