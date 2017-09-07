package tech.lihx.demo.core.web.appversion.web;

import javax.servlet.ServletContext;

import org.apache.commons.lang.StringUtils;


/**
 * app 版本配置
 * <p>
 * 
 * @author LHX
 * @date 2015年4月28日
 * @version 1.0.0
 */
public class AppVersionConfigurer {

	/**
	 * Parameter specifying the location of the app version config file
	 */
	private static final String CONFIG_LOCATION_PARAM = "appVersionConfigLocation";

	public static String CONFIG_LOCATION = null;


	private AppVersionConfigurer() {

	}


	public static void init( ServletContext servletContext ) {
		String _location = servletContext.getInitParameter(CONFIG_LOCATION_PARAM);
		if ( StringUtils.isEmpty(_location) ) {
			servletContext.log("Initializing is not available ssoConfigLocation on the classpath");
		} else {
			CONFIG_LOCATION = _location;
		}
	}


	public static void shutdown( ServletContext servletContext ) {
		servletContext.log(" Uninstalling app version configurer. ");
	}
}
