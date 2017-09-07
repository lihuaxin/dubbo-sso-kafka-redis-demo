package tech.lihx.demo.core.common.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.ConfigurableWebApplicationContext;

/**
 * 静态调用applicationContext
 * <p>
 * 
 * @author lihx
 * @Date 2017-9-4 17:59:13
 */
public class ApplicationUtil implements ApplicationContextAware {

	private static ApplicationContext context;


	public static void setApplication( ApplicationContext context ) {
		ApplicationUtil.context = context;
	}


	public static <T> T getBean( Class<T> cls ) {
		return context.getBean(cls);
	}


	public static Object getBean( String name ) {
		return context.getBean(name);
	}


	public static <T> T getBean( String name, Class<T> cls ) {
		return context.getBean(name, cls);
	}


	public static boolean containsBean( String name ) {
		return context.containsBean(name);
	}


	public static ConfigurableWebApplicationContext getContext() {
		return (ConfigurableWebApplicationContext) context;
	}


	@Override
	public void setApplicationContext( ApplicationContext applicationContext ) throws BeansException {
		context = applicationContext;
	}
}
