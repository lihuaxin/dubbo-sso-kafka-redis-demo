package tech.lihx.demo.core.mybatis.mybatis.ext;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.SqlSource;


/**
 * 扩展兼容分页
 * <p>
 * 
 * @author lihx
 * @date 2017-9-5
 * @version 1.0.0
 */
public class SqlSourceProxyBoundSql implements SqlSource {


	private final BoundSql boundSql;


	public SqlSourceProxyBoundSql( BoundSql boundSql ) {
		this.boundSql = boundSql;
	}


	@Override
	public BoundSql getBoundSql( Object parameterObject ) {
		return boundSql;
	}

}
