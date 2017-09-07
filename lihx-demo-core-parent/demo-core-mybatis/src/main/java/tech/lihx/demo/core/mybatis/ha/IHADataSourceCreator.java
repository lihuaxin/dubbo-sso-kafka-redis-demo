package tech.lihx.demo.core.mybatis.ha;

import javax.sql.DataSource;

import tech.lihx.demo.core.mybatis.mybatis.rw.DataSourceDescriptor;

public interface IHADataSourceCreator {

	DataSource createHADataSource( DataSourceDescriptor descriptor ) throws Exception;
}
