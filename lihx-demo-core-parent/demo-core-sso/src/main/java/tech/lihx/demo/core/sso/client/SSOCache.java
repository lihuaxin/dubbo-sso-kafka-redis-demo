package tech.lihx.demo.core.sso.client;


/**
 * SSO 缓存工具类
 * 
 * @author LHX
 * @date 2017年9月6日
 * @version 1.0.0
 */
public class SSOCache {

	/**
	 * 登录用户基本信息缓存key
	 */
	private static final String SSO_CACHE_KEY = "ssocache_";


	/**
	 * SSO 缓存用户信息 Key
	 * <p>
	 * 
	 * @param key
	 *            关键字
	 * @return String 缓存 Key
	 */
	public static String key( long key ) {
		return key(String.valueOf(key));
	}


	/**
	 * SSO 缓存用户信息 Key
	 * <p>
	 * 
	 * @param key
	 *            关键字
	 * @return String 缓存 Key
	 */
	public static String key( String key ) {
		StringBuffer ck = new StringBuffer(SSO_CACHE_KEY);
		ck.append(key);
		return ck.toString();
	}


	/**
	 * SSO 缓存状态
	 * <p>
	 *
	 * @param cacheStatus
	 *            缓存状态
	 * @return CacheType 缓存类型
	 */
	public static CacheType status( boolean status ) {
		if ( status ) {
			return CacheType.NORMAL;
		} else {
			return CacheType.SHUTDOWN;
		}
	}
}
