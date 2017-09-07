
package tech.lihx.demo.core.web.velocity;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.view.velocity.VelocityConfigurer;

import tech.lihx.demo.core.common.environment.EnvironmentDetect;
import tech.lihx.demo.core.common.util.VelocityTemplateUtil;


/**
 * 实现velocity配置根据环境变换
 * <p>
 * 
 * @author LTU
 * @date 2014年11月15日
 * @version 1.0.0
 */
public class VelocityConfigurerExt extends VelocityConfigurer {

	private Resource configLocation;


	@Override
	public VelocityEngine createVelocityEngine() throws IOException, VelocityException {
		if ( configLocation != null ) {
			Properties properties = new Properties();
			Map<Object, Object> context = new HashMap<Object, Object>();
			context.put("environment", EnvironmentDetect.detectEnvironment());
			context.putAll(System.getProperties());
			String config = VelocityTemplateUtil.getConfiguration(context, configLocation.getURL().getFile());
			StringReader reader = new StringReader(config);
			properties.load(reader);
			setVelocityProperties(properties);
		}
		return super.createVelocityEngine();

	}


	/**
	 * 拦截配置设置
	 * 
	 */
	@Override
	public void setConfigLocation( Resource configLocation ) {
		this.configLocation = configLocation;
		super.setConfigLocation(null);

	}
}
