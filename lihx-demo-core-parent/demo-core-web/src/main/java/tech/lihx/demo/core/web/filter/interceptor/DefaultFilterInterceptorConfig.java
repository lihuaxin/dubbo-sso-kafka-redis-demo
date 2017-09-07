package tech.lihx.demo.core.web.filter.interceptor;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

public class DefaultFilterInterceptorConfig implements InitializingBean, FilterInterceptorConfig {

	private FilterInterceptor[] filterInterceptors;


	@Override
	public FilterInterceptor[] getFilterInterceptors() {
		return filterInterceptors;
	}


	public void setFilterInterceptors( FilterInterceptor[] filterInterceptors ) {
		this.filterInterceptors = filterInterceptors;
	}


	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(filterInterceptors, "请添加filter拦截器!");
		Assert.notEmpty(filterInterceptors, "拦截器不能为空");
	}

}
