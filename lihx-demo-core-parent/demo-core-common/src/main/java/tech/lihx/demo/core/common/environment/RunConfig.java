package tech.lihx.demo.core.common.environment;

import java.util.Properties;

/**
 * @author bo 根据环境不同,配置不同
 */
public interface RunConfig {

	public String getProperty( String key );


	public Properties getProperties();


	public String getProperty( String key, String defaultValue );

}
