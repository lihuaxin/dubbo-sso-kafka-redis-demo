package tech.lihx.demo.core.cache.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 锁实现
 * <p>
 * 
 * @author lihx
 * @Date 2014-8-18
 */
public abstract class LockSupport {

	protected static ConsistentHashImpl<Object> lock;

	static {
		Map<String, Object> lockMap = new HashMap<String, Object>();
		for ( int i = 0 ; i < 1000 ; i++ ) {
			Object obj = new Object();
			lockMap.put(obj.toString(), obj);
		}
		lock = new ConsistentHashImpl<Object>(lockMap);
	}


	public static Object getLock( String key ) {
		return lock.get(key);
	}
}
