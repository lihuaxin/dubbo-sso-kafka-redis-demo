package tech.lihx.demo.core.common.util;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.support.PropertiesLoaderUtils;

/**
 *
 * 属性文件操作 工具类
 *
 * 我们知道可以通过 java.util.Properties的load(InputStream inStream)
 * 方法从一个输入流中加载属性资源。Spring 提供的 PropertiesLoaderUtils 允许您直接通过基于类路径的文件地址加载属性资源
 *
 * @author LHX
 *
 */
public class PropertiesUtil extends PropertiesLoaderUtils {

	private static final Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);


	/**
	 * 根据属性文件中的键获取值
	 *
	 * @param key
	 * @return
	 */
	public static String getSiteEnvValue( String key ) {
		Properties props;
		String value = "";
		try {
			props = PropertiesLoaderUtils.loadAllProperties("properties/env.properties");
			value = props.getProperty(key);
		} catch ( IOException e ) {
			value = "";
			logger.error(e.getMessage(), e);
		}
		return value;
	}


	public static String getJdbcValue( String key ) {
		String value = "";
		if ( Util.isEmpty(key) ) { return value; }
		Properties props;
		try {
			props = PropertiesLoaderUtils.loadAllProperties("properties/jdbc.properties");
			value = props.getProperty(key);
		} catch ( IOException e ) {
			value = "";
			logger.error(e.getMessage(), e);
		}
		return value;
	}


	public static void main( String[] args ) throws Throwable {
		logger.debug(PropertiesUtil.getSiteEnvValue("site.server.url"));
	}
}
