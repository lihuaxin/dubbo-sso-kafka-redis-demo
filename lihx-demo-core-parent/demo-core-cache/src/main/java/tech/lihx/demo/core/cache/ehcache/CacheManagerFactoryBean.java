package tech.lihx.demo.core.cache.ehcache;

import net.sf.ehcache.CacheManager;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

/**
 * 生成CacheManager
 * <p>
 * 
 * @author lihx
 * @Date 2017-9-5 10:40:04
 */
public class CacheManagerFactoryBean implements FactoryBean<CacheManager>, InitializingBean {

	private Resource configFile;

	private CacheManager manager;


	@Override
	public CacheManager getObject() throws Exception {

		return manager;

	}


	@Override
	public Class<?> getObjectType() {
		return CacheManager.class;

	}


	@Override
	public boolean isSingleton() {

		return true;

	}


	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(configFile, "ehcache配置文件不能为null");
		manager = CacheManager.create(configFile.getURL());
	}


	public Resource getConfigFile() {
		return configFile;
	}


	public void setConfigFile( Resource configFile ) {
		this.configFile = configFile;
	}

}
