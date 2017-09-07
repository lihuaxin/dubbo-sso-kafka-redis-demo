package tech.lihx.demo.core.cache.support;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.cache.interceptor.KeyGenerator;

import tech.lihx.demo.core.cache.util.HashCodeKeyGenerator;
import tech.lihx.demo.core.common.util.CacheKeyPrefix;

public class CacheOperationContext {

	// 保存是否对当前线程操作进行缓存
	private static ThreadLocal<Boolean> threadCachehoder = new ThreadLocal<Boolean>();

	private final KeyGenerator keyGenerator = new HashCodeKeyGenerator();

	private final CacheOperation operation;

	private final Method method;

	private final Object[] args;


	public CacheOperationContext( CacheOperation operation, Method method, Object[] args ) {
		this.operation = operation;
		this.method = method;
		this.args = args;
	}


	public Object generateKey() {
		return keyGenerator.generate(method.getDeclaringClass(), this.method, this.args);
	}


	public static void setCache( boolean isCache ) {
		threadCachehoder.set(isCache);
	}


	public static boolean getCache() {
		Boolean isCahe = threadCachehoder.get();
		if ( isCahe == null ) { return true; }
		return isCahe;
	}


	public CacheOperation getOperation() {
		return operation;
	}


	public static void removeCache() {
		threadCachehoder.remove();
	}


	public String getTableName() {
		return CacheKeyPrefix.get() + this.operation.getTable();
	}


	public Method getMethod() {
		return method;
	}


	public String getKey() {
		return operation.getKey();
	}


	public int getExpireTime() {
		return this.operation.getExpire();
	}


	public boolean isCompress() {
		return this.operation.isCompress();
	}


	public Object[] getArgs() {
		return args;
	}


	public Map<String, Object> getParamMap() {
		if ( args == null ) { return Collections.emptyMap(); }
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
}
