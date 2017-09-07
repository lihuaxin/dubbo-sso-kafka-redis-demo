package tech.lihx.demo.core.cache.dao;

import java.util.Properties;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import tech.lihx.demo.core.cache.util.Invoker;
import tech.lihx.demo.core.mybatis.mybatis.ext.MapperProxyExt;
import tech.lihx.demo.core.mybatis.mybatis.interceptor.MyBatisInvocation;

/**
 * 缓存处理,实体缓存和过期缓存
 * 
 */
@Intercepts( {
		@Signature( type = Executor.class, method = "update", args = { MappedStatement.class, Object.class } ),
		@Signature( type = Executor.class, method = "query", args = { MappedStatement.class, Object.class,
				RowBounds.class, ResultHandler.class } ) } )
public class MapperCacheInterceptor extends MapperCacheAspectSupport implements Interceptor {

	@Override
	public Object intercept( final Invocation invocation ) throws Throwable {
		Invoker aopAllianceInvoker = new Invoker() {

			@Override
			public Object invoke() {
				try {
					return invocation.proceed();
				} catch ( Throwable ex ) {
					throw new RuntimeException(ex);
				}
			}
		};
		MyBatisInvocation inv = MapperProxyExt.getMyBatisInvocation();
		final Object result = execute(aopAllianceInvoker, inv.getMethod(), inv.getArgs());
		return result;
	}


	@Override
	public Object plugin( Object target ) {
		return Plugin.wrap(target, this);
	}


	@Override
	public void setProperties( Properties properties ) {
	}
}
