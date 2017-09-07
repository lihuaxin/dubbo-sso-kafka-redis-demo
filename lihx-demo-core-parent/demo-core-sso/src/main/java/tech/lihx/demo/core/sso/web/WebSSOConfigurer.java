package tech.lihx.demo.core.sso.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tech.lihx.demo.core.sso.SSOConfig;
import tech.lihx.demo.core.sso.exception.KissoException;

/**
 * SSO 配置
 * <p>
 * 
 * @author LHX
 * @date 2016年3月11日
 * @version 1.0.0
 */
public class WebSSOConfigurer {

	private static final Logger logger = LoggerFactory.getLogger(WebSSOConfigurer.class);

	/**
	 * Parameter specifying the location of the sso config file
	 */
	public static final String CONFIG_LOCATION_PARAM = "ssoConfigLocation";


	private WebSSOConfigurer() {
	}


	public static void init( ServletContext servletContext ) {
		String location = servletContext.getInitParameter(CONFIG_LOCATION_PARAM);
		if ( location != null ) {
			if ( location.indexOf("classpath") >= 0 ) {
				String[] cfg = location.split(":");
				if ( cfg.length == 2 ) {
					InputStream in = WebSSOConfigurer.class.getClassLoader().getResourceAsStream(cfg[1]);
					// 初始化配置
					SSOConfig.init(getInputStream(servletContext, in));
				}
			} else {
				File file = new File(location);
				if ( file.isFile() ) {
					try {
						SSOConfig.init(getInputStream(servletContext, new FileInputStream(file)));
					} catch ( FileNotFoundException e ) {
						logger.error(e.getMessage(), e);
						throw new KissoException(location);
					}
				} else {
					throw new KissoException(location);
				}
			}
		} else {
			servletContext.log("Initializing is not available ssoConfigLocation on the classpath");
		}
	}


	public static void shutdown( ServletContext servletContext ) {
		servletContext.log("Uninstalling sso ");
	}


	private static Properties getInputStream( ServletContext servletContext, InputStream in ) {
		Properties p = null;
		try {
			p = new Properties();
			p.load(in);
		} catch ( Exception e ) {
			servletContext.log(" sso read config file error. ", e);
		}
		return p;
	}
}
