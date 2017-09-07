package tech.lihx.demo.core.common.util;

/**
 * 根据不同请求生成不同缓存key的前缀
 * <p>
 * 
 * @author lihx
 * @Date 2014-7-7
 */
public class CacheKeyPrefix {

	private static final ThreadLocal<String> KEY_HOLDER = new ThreadLocal<String>();


	public static void set( String key ) {
		KEY_HOLDER.set(key);
	}


	public static String get() {
		String prefix = KEY_HOLDER.get();
		if ( prefix == null ) { return ""; }
		return prefix;
	}


	public static void remove() {
		KEY_HOLDER.remove();
	}
}
