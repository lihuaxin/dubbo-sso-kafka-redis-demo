/**
 * Copyright 1999-2011 Alibaba Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package tech.lihx.demo.core.mybatis.mybatis.rw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;

import tech.lihx.demo.core.mybatis.ha.IHADataSourceCreator;
import tech.lihx.demo.core.mybatis.ha.NonHADataSourceCreator;


/**
 * StrongRefDataSourceLocator is mainly responsible for assembling data sources
 * mapping relationship as per data source definitions in spring container.
 * 
 * @author fujohnwang
 */
public class DefaultDataSourceService implements IDataSourceService, InitializingBean {

	private Set<DataSourceDescriptor> dataSourceDescriptors = new HashSet<DataSourceDescriptor>();

	private IHADataSourceCreator haDataSourceCreator;

	private final Map<String, DataSource> dataSources = new HashMap<String, DataSource>();

	private final List<DataSource> readDataSources = new ArrayList<DataSource>();

	private final List<DataSource> writeDataSources = new ArrayList<DataSource>();


	@Override
	public Map<String, DataSource> getDataSources() {
		return dataSources;
	}


	@Override
	public void afterPropertiesSet() throws Exception {
		if ( haDataSourceCreator == null ) {
			setHaDataSourceCreator(new NonHADataSourceCreator());
		}
		for ( DataSourceDescriptor descriptor : dataSourceDescriptors ) {
			Validate.notEmpty(descriptor.getIdentity());
			Validate.notNull(descriptor.getTarget());
			DataSource dataSourceToUse = descriptor.getTarget();
			if ( descriptor.getStandby() != null ) {
				dataSourceToUse = haDataSourceCreator.createHADataSource(descriptor);
			}
			dataSourceToUse = new LazyConnectionDataSourceProxy(dataSourceToUse);
			dataSources.put(descriptor.getIdentity(), dataSourceToUse);
			// 读写分开
			if ( "read".equalsIgnoreCase(descriptor.getType()) ) {
				readDataSources.add(dataSourceToUse);
			} else if ( "write".equalsIgnoreCase(descriptor.getType()) ) {
				writeDataSources.add(dataSourceToUse);
			}
		}
	}


	public void setDataSourceDescriptors( Set<DataSourceDescriptor> dataSourceDescriptors ) {
		this.dataSourceDescriptors = dataSourceDescriptors;
	}


	@Override
	public Set<DataSourceDescriptor> getDataSourceDescriptors() {
		return dataSourceDescriptors;
	}


	public void setHaDataSourceCreator( IHADataSourceCreator haDataSourceCreator ) {
		this.haDataSourceCreator = haDataSourceCreator;
	}


	public IHADataSourceCreator getHaDataSourceCreator() {
		return haDataSourceCreator;
	}


	@Override
	public List<DataSource> getReadDataSources() {
		return readDataSources;
	}


	@Override
	public List<DataSource> getWriteDataSources() {
		return writeDataSources;
	}

}
