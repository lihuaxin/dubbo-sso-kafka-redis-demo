package tech.lihx.demo.core.mybatis.ha;

import javax.sql.DataSource;

import tech.lihx.demo.core.mybatis.mybatis.rw.DataSourceDescriptor;

public class NonHADataSourceCreator implements IHADataSourceCreator {

	@Override
	public DataSource createHADataSource( DataSourceDescriptor descriptor ) throws Exception {
		return descriptor.getTarget();
	}

}
