package tech.lihx.demo.core.cache.util;

import java.lang.reflect.Method;

import org.springframework.cache.interceptor.KeyGenerator;

import tech.lihx.demo.core.common.util.CacheKeyPrefix;
import tech.lihx.demo.core.common.util.MurMurHash;

import com.alibaba.fastjson.JSON;

public class HashCodeKeyGenerator implements KeyGenerator {

	@Override
	public Object generate( Object target, Method method, Object... params ) {
		return toString(method, params);
	}


	private String toString( Method m, Object... params ) {
		StringBuilder sb = getString(m.getDeclaringClass().getName(), m.toString(), params);
		return new StringBuilder(CacheKeyPrefix.get()).append(MurMurHash.hash(sb.toString())).toString();
	}


	public static String getKey( String className, String methodName, Object... objs ) {
		StringBuilder sb = getString(className, methodName, objs);
		return new StringBuilder(CacheKeyPrefix.get()).append(MurMurHash.hash(sb.toString())).toString();
	}


	public static StringBuilder getString( String className, String methodName, Object... objs ) {
		StringBuilder sb = new StringBuilder();
		sb.append(className);
		sb.append(".");
		sb.append(methodName);
		for ( Object object : objs ) {
			if ( object == null ) {
				sb.append("null,");
			} else {
				sb.append(JSON.toJSONString(object));
				sb.append(",");
			}
		}
		return sb;
	}

}
