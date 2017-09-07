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

import javax.sql.DataSource;

/**
 * {@link CobarDataSourceDescriptor} describe a data base deployment structure
 * with 2 databases as HA group.<br>
 * it looks like:<br>
 * 
 * <pre>
 *                  Client
 *                    /\
 *                  /    \
 *         Active DB <-> Standby DB
 * </pre>
 * 
 * {@link #targetDataSource} should be the reference to the current active
 * database, while {@link #standbyDataSource} should be the standby database.<br>
 * for both {@link #targetDataSource} and {@link #standbyDataSource}, each one
 * should have a sibling data source that connect to the same target database.<br>
 * as to the reason why do so, that's :
 * <ol>
 * <li>these sibling DataSources will be used when do
 * database-status-detecting.(if we fetch connection from target data source,
 * when it's full, the deteting behavior can't be performed.)</li>
 * <li>if the {@link #targetDataSource} and {@link #standbyDataSource} are
 * DataSource implementations configured in local application container, we can
 * fetch necessary information via reflection to create connection to target
 * database independently, but if they are fetched from JNDI, we can't, so
 * explicitly declaring sibling data sources is necessary in this situation.</li>
 * </ol>
 * 
 * @author fujohnwang
 * @since 1.0
 */
public class DataSourceDescriptor {

	/**
	 * 标识
	 */
	private String identity;

	/**
	 * 是读库还是写库，write，read
	 */
	private String type;

	/**
	 * 当前使用
	 */
	private DataSource target;

	/**
	 * 热备
	 */
	private DataSource standby;

	/**
	 * we will initialize proper thread pools which stand in front of data
	 * sources as per connection pool size. <br>
	 * usually, they will have same number of objects.<br>
	 * you have to set a proper size for this attribute as per your data source
	 * attributes. In case you forget it, we set a default value with
	 * "number of CPU" * 5.
	 */
	private int poolSize = Runtime.getRuntime().availableProcessors() * 5;


	public String getIdentity() {
		return identity;
	}


	public void setIdentity( String identity ) {
		this.identity = identity;
	}


	public void setPoolSize( int poolSize ) {
		this.poolSize = poolSize;
	}


	public int getPoolSize() {
		return poolSize;
	}


	public String getType() {
		return type;
	}


	public void setType( String type ) {
		this.type = type;
	}


	public DataSource getTarget() {
		return target;
	}


	public void setTarget( DataSource target ) {
		this.target = target;
	}


	public DataSource getStandby() {
		return standby;
	}


	public void setStandby( DataSource standby ) {
		this.standby = standby;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((identity == null) ? 0 : identity.hashCode());
		return result;
	}


	@Override
	public boolean equals( Object obj ) {
		if ( this == obj ) { return true; }
		if ( obj == null ) { return false; }
		if ( getClass() != obj.getClass() ) { return false; }
		DataSourceDescriptor other = (DataSourceDescriptor) obj;
		if ( identity == null ) {
			if ( other.identity != null ) { return false; }
		} else if ( !identity.equals(other.identity) ) { return false; }
		return true;
	}

}
