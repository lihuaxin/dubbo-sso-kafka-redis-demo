package tech.lihx.demo.core.mybatis.mybatis.interceptor;

import java.lang.reflect.Method;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.ibatis.binding.BindingException;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.defaults.DefaultSqlSession.StrictMap;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SqlPerformanceInterceptor implements MyBatisInterceptor {

	private int warnTime = 50;

	private static Logger logger = LoggerFactory.getLogger(SqlPerformanceInterceptor.class);


	public MappedStatement getMappedStatement( Configuration configuration, Class<?> mapperInterface, Method method ) {
		String statementName = mapperInterface.getName() + "." + method.getName();
		MappedStatement ms = null;
		if ( configuration.hasStatement(statementName) ) {
			ms = configuration.getMappedStatement(statementName);
		} else if ( !mapperInterface.equals(method.getDeclaringClass().getName()) ) {
			String parentStatementName = method.getDeclaringClass().getName() + "." + method.getName();
			if ( configuration.hasStatement(parentStatementName) ) {
				ms = configuration.getMappedStatement(parentStatementName);
			}
		}
		if ( ms == null ) { throw new BindingException("Invalid bound statement (not found): " + statementName); }
		return ms;
	}


	public static String getSql( Configuration configuration, BoundSql boundSql, String sqlId, long time ) {
		String sql = showSql(configuration, boundSql);
		StringBuilder str = new StringBuilder(100);
		str.append(sqlId.substring(sqlId.lastIndexOf(".", sqlId.lastIndexOf(".") - 1) + 1));
		str.append(":");
		str.append(sql);
		str.append(":");
		str.append(time);
		str.append("ms");
		return str.toString();
	}


	public static String getParameterValue( Object obj ) {
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
		if ( parameterObject != null && parameterObject.getClass().isArray() ) {
			Object[] objArray = (Object[]) parameterObject;
			if ( objArray.length == 1 ) {
				parameterObject = objArray[0];
			}
		}

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


	public Object getParameters( MyBatisInvocation handler ) {
		Object[] args = handler.getArgs();
		if ( args != null ) {
			Map<String, Object> paramsMap = handler.getParamsMap();
			if ( args.length == 1 ) {
				if ( paramsMap.isEmpty() ) {
					if ( args[0] instanceof List ) {
						StrictMap<Object> map = new StrictMap<Object>();
						map.put("list", args[0]);
						return map;
					} else if ( args[0] != null && args[0].getClass().isArray() ) {
						StrictMap<Object> map = new StrictMap<Object>();
						map.put("array", args[0]);
						return map;
					}
				}
			}
			return paramsMap;
		}
		return null;
	}


	// private Map<String, Object> getParamMap(MyBatisInvocation handler) {
	// Object[] args = handler.getArgs();
	// Annotation[][] parameterAnnotations =
	// handler.getMethod().getParameterAnnotations();
	// Map<String, Object> paramsMap = new HashMap<String, Object>();
	// for (int i = 0; i < parameterAnnotations.length; i++) {
	// for (Annotation annotation : parameterAnnotations[i]) {
	// if (annotation instanceof Param) {
	// Param myAnnotation = (Param) annotation;
	// paramsMap.put(myAnnotation.value(), args[i]);
	// }
	// }
	// }
	// return paramsMap;
	// }

	@Override
	public Object invoke( MyBatisInvocation handler ) throws Throwable {
		long start = System.currentTimeMillis();
		Object result = handler.execute();
		long end = System.currentTimeMillis();
		if ( (end - start) >= warnTime ) {
			try {
				MappedStatement ms = getMappedStatement(
					handler.getConfiguration(), handler.getMapperInterface(), handler.getMethod());
				String sql = getSql(handler.getConfiguration(), ms.getBoundSql(getParameters(handler)), ms.getId(), end
						- start);
				logger.warn(sql);
			} catch ( Exception e ) {
				logger.error("统计查询时间错误", e);
			}
		}

		return result;
	}


	public int getWarnTime() {
		return warnTime;
	}


	public void setWarnTime( int warnTime ) {
		this.warnTime = warnTime;
	}

}
