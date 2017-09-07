package tech.lihx.demo.core.mybatis.mybatis.interceptor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;

public class MyBatisInvocation {

	private final SqlSession sqlSession;

	private final Object[] args;

	private final Class<?> mapperInterface;

	private final Method method;

	private final MyBatisInterceptor[] interceptors;

	private int index = 0;

	private int batchIndex = -1;

	private final Map<String, Object> paramsMap;

	private final Map<Method, MapperMethod> methodCache;


	public MyBatisInvocation(
			Class<?> mapperInterface,
			SqlSession sqlSession,
			Method method,
			Object[] args,
			MyBatisInterceptor[] interceptors,
			Map<Method, MapperMethod> methodCache ) {
		this.sqlSession = sqlSession;
		this.args = args;
		this.mapperInterface = mapperInterface;
		this.method = method;
		this.interceptors = interceptors;
		this.methodCache = methodCache;
		paramsMap = getParamMap(method, args);
	}


	public Object execute() throws Throwable {
		if ( interceptors == null ) {
			final MapperMethod mapperMethod = cachedMapperMethod();
			return mapperMethod.execute(sqlSession, args);
		}
		if ( index < interceptors.length ) {
			return interceptors[index++].invoke(this);
		} else {
			final MapperMethod mapperMethod = cachedMapperMethod();
			return mapperMethod.execute(sqlSession, args);
		}

	}


	private MapperMethod cachedMapperMethod() {
		MapperMethod mapperMethod = methodCache.get(method);
		if ( mapperMethod == null ) {
			mapperMethod = new MapperMethod(mapperInterface, method, sqlSession.getConfiguration());
			methodCache.put(method, mapperMethod);
		}
		return mapperMethod;
	}


	@SuppressWarnings( "hiding" )
	private Map<String, Object> getParamMap( Method method, Object[] args ) {
		Annotation[][] parameterAnnotations = method.getParameterAnnotations();
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		for ( int i = 0 ; i < parameterAnnotations.length ; i++ ) {
			for ( Annotation annotation : parameterAnnotations[i] ) {
				if ( annotation instanceof Param ) {
					Param myAnnotation = (Param) annotation;
					paramsMap.put(myAnnotation.value(), args[i]);
				}
			}
		}
		return paramsMap;
	}


	public Map<String, Object> getParamsMap() {
		return paramsMap;
	}


	public Configuration getConfiguration() {
		return sqlSession.getConfiguration();
	}


	public Object[] getArgs() {
		return args;
	}


	public Class<?> getMapperInterface() {
		return mapperInterface;
	}


	public Method getMethod() {
		return method;
	}


	public int getBatchIndex() {
		return batchIndex;
	}


	public void batchIndexIncrease() {
		batchIndex++;
	}

}
