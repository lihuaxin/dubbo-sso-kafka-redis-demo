package tech.lihx.demo.core.mybatis.mybatis;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tech.lihx.demo.core.common.environment.EnvironmentDetect;
import tech.lihx.demo.core.mybatis.mybatis.tx.TxServiceHelper;

@Intercepts( {
		@Signature( type = Executor.class, method = "update", args = { MappedStatement.class, Object.class } ),
		@Signature( type = Executor.class, method = "query", args = { MappedStatement.class, Object.class,
				RowBounds.class, ResultHandler.class } ) } )
public class MybatisExeTimeInterceptor implements Interceptor {

	// Logger logger = LoggerFactory.getLogger(MybatisExeTimeInterceptor.class);


	@Override
	public Object intercept( Invocation invocation ) throws Throwable {
		if ( EnvironmentDetect.detectEnvironment().isProduct() ) {
			return invocation.proceed();
		} else {
			MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
			Object parameter = null;
			if ( invocation.getArgs().length > 1 ) {
				parameter = invocation.getArgs()[1];
			}
			TxServiceHelper.add(mappedStatement.getSqlCommandType());
			String sqlId = mappedStatement.getId();
			BoundSql boundSql = mappedStatement.getBoundSql(parameter);
			Configuration configuration = mappedStatement.getConfiguration();
			Object returnValue = null;
			long start = System.currentTimeMillis();
			try {
				returnValue = invocation.proceed();
			} finally {
				long end = System.currentTimeMillis();
				long time = (end - start);
				// 打印查询时间大于50ms的sql语句
				String sql = getSql(configuration, boundSql, sqlId, time);
				Logger logger = LoggerFactory.getLogger(sqlId);
				if ( time > 100 ) {
					logger.warn(sql);
					System.err.println(sql);
				} else {
					logger.info(sql);
				}
			}

			return returnValue;
		}

	}


	@SuppressWarnings( "unused" )
	public static String getSql( Configuration configuration, BoundSql boundSql, String sqlId, long time ) {
		StringBuilder str;
		try {
			String sql = showSql(configuration, boundSql);
			str = new StringBuilder(100);
			// str.append(sqlId);
			// str.append(":");
			str.append(sql);
			str.append(":");
			str.append(time);
			str.append("ms");
			return str.toString();
		} catch ( Exception e ) {

		}
		return "";
	}


	private static String getParameterValue( Object obj ) {
		String value = "null";
		if ( obj instanceof String ) {
			value = "'" + obj.toString() + "'";
		} else if ( obj instanceof Date ) {
			DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);
			value = "'" + formatter.format((Date) obj) + "'";
		} else {
			if ( obj != null ) {
				value = obj.toString();
			}
		}
		return value;
	}


	public static String showSql( Configuration configuration, BoundSql boundSql ) {
		Object parameterObject = boundSql.getParameterObject();
		List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
		String sql = boundSql.getSql().replaceAll("[\\s]+", " ");
		if ( parameterMappings.size() > 0 && parameterObject != null ) {
			TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
			if ( typeHandlerRegistry.hasTypeHandler(parameterObject.getClass()) ) {
				sql = sql.replaceFirst("\\?", getParameterValue(parameterObject));

			} else {
				MetaObject metaObject = configuration.newMetaObject(parameterObject);
				for ( ParameterMapping parameterMapping : parameterMappings ) {
					String propertyName = parameterMapping.getProperty();
					if ( metaObject.hasGetter(propertyName) ) {
						Object obj = metaObject.getValue(propertyName);
						sql = sql.replaceFirst("\\?", getParameterValue(obj));
					} else if ( boundSql.hasAdditionalParameter(propertyName) ) {
						Object obj = boundSql.getAdditionalParameter(propertyName);
						sql = sql.replaceFirst("\\?", getParameterValue(obj));
					}
				}
			}
		}
		return sql;
	}


	@Override
	public Object plugin( Object target ) {
		return Plugin.wrap(target, this);
	}


	@Override
	public void setProperties( Properties properties0 ) {
	}
}
