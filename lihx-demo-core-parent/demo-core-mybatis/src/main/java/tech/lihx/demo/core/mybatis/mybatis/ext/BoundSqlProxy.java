package tech.lihx.demo.core.mybatis.mybatis.ext;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.session.Configuration;

import tech.lihx.demo.core.common.util.Page;


/**
 * BoundSql代理
 * <p>
 * 
 * @author lihx
 * @date 2017-9-5
 * @version 1.0.0
 */
public class BoundSqlProxy extends BoundSql {

	private final Page<?> page;

	private final BoundSql proxy;


	public BoundSqlProxy( Configuration configuration, Page<?> page, BoundSql proxy ) {

		super(configuration, proxy.getSql(), proxy.getParameterMappings(), proxy.getParameterObject());
		this.page = page;
		this.proxy = proxy;
	}


	@Override
	public String getSql() {
		StringBuilder sb = new StringBuilder(proxy.getSql());
		sb.append(" LIMIT ").append(page.getStartNo() - 1).append(",").append(page.getPageSize());
		return sb.toString();

	}


	@Override
	public boolean hasAdditionalParameter( String name ) {

		return proxy.hasAdditionalParameter(name);

	}


	@Override
	public Object getAdditionalParameter( String name ) {

		return proxy.getAdditionalParameter(name);

	}


	@Override
	public void setAdditionalParameter( String name, Object value ) {

		proxy.setAdditionalParameter(name, value);

	}
}
