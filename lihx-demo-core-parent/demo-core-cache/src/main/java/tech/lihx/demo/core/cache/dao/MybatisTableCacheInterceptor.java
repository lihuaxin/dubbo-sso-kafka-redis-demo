package tech.lihx.demo.core.cache.dao;

import java.util.HashSet;
import java.util.List;
import java.util.Properties;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import tech.lihx.demo.core.cache.interfaces.TableCache;
import tech.lihx.demo.core.cache.support.CacheOperationContext;
import tech.lihx.demo.core.cache.util.Invoker;
import tech.lihx.demo.core.cache.util.Md5KeyGenerator;
import tech.lihx.demo.core.cache.util.TableNamesUtil;
import tech.lihx.demo.core.cache.util.ThrowableWrapper;
import tech.lihx.demo.core.common.util.ApplicationUtil;

/**
 * @author lihx
 * 
 */
@Intercepts( {
		@Signature( type = Executor.class, method = "update", args = { MappedStatement.class, Object.class } ),
		@Signature( type = Executor.class, method = "query", args = { MappedStatement.class, Object.class,
				RowBounds.class, ResultHandler.class } ) } )
public class MybatisTableCacheInterceptor implements Interceptor {

	private Properties properties;

	private static HashSet<String> cachedTables = new HashSet<String>();


	@Override
	public Object intercept( final Invocation invocation ) throws Throwable {
		String methodName = invocation.getMethod().getName();
		if ( "query".equals(methodName) ) {
			// 禁缓存不执行查询,但是执行删除
			if ( !CacheOperationContext.getCache() ) { return invocation.proceed(); }
			MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
			Executor exe = (Executor) invocation.getTarget();
			Object parameter = invocation.getArgs()[1];
			RowBounds rowBounds = (RowBounds) invocation.getArgs()[2];
			BoundSql boundSql = mappedStatement.getBoundSql(parameter);
			CacheKey cacheKey = exe.createCacheKey(mappedStatement, parameter, rowBounds, boundSql);
			String sql = boundSql.getSql();
			sql = sql.replaceAll("[\\s]+", " ").toUpperCase();
			if ( !sql.contains(" FROM ") ) { return invocation.proceed(); }
			List<String> tableList = TableNamesUtil.getTableNames(sql);
			if ( tableList.size() == 1 ) {
				String tableName = tableList.get(0).toUpperCase();
				if ( !cachedTables.contains(tableName) ) { return invocation.proceed(); }
				String key = Md5KeyGenerator.MD5(cacheKey.toString());
				TableCache cache = ApplicationUtil.getBean(TableCache.class);
				Invoker aopAllianceInvoker = new Invoker() {

					@Override
					public Object invoke() {
						try {
							return invocation.proceed();
						} catch ( Throwable ex ) {
							throw new ThrowableWrapper(ex);
						}
					}
				};
				return cache.get(tableName, key, aopAllianceInvoker, true);
			}
			return invocation.proceed();

		} else if ( "update".equals(methodName) ) {
			Object obj = invocation.proceed();
			MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
			Object parameter = invocation.getArgs()[1];
			BoundSql boundSql = mappedStatement.getBoundSql(parameter);
			String sql = boundSql.getSql();
			sql = sql.replaceAll("[\\s]+", " ").toUpperCase();
			List<String> tableList = TableNamesUtil.getTableNames(sql);
			if ( tableList.size() == 1 ) {
				String tableName = tableList.get(0).toUpperCase();
				if ( !cachedTables.contains(tableName) ) { return obj; }
				TableCache cache = ApplicationUtil.getBean(TableCache.class);
				cache.removeAll(tableName);
			}

			return obj;
		}
		return invocation.proceed();
	}


	@Override
	public Object plugin( Object target ) {
		return Plugin.wrap(target, this);
	}


	@Override
	public void setProperties( Properties properties ) {
		// 表名称请大写
		this.properties = properties;
		String cached = this.properties.getProperty("cachedTables");
		if ( cached != null ) {
			String[] mapper = cached.split(",");
			for ( int i = 0 ; i < mapper.length ; i++ ) {
				cachedTables.add(mapper[i].trim().toUpperCase());
			}
		}
	}
}
