package tech.lihx.demo.core.mybatis.mybatis.ds;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import com.alibaba.druid.pool.DruidDataSource;

public class DruidDataSourceFactory implements InitializingBean {

	private DruidDataSource basicDruidDataSource;


	public DruidDataSource makeDataSource() {

		return basicDruidDataSource.cloneDruidDataSource();
	}


	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(basicDruidDataSource, "basicDruidDataSource不能为null");
	}


	public DruidDataSource getBasicDruidDataSource() {
		return basicDruidDataSource;
	}


	public void setBasicDruidDataSource( DruidDataSource basicDruidDataSource ) {
		this.basicDruidDataSource = basicDruidDataSource;
	}


}
