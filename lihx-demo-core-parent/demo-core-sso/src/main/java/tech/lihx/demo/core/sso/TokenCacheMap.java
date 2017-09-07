package tech.lihx.demo.core.sso;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Token缓存到HashMap
 * <p>
 * 
 * @author LHX
 * @Date 2016-6-17
 */
public class TokenCacheMap extends TokenCache {

	/**
	 * Token Map
	 */
	private static ConcurrentHashMap<String, Token> tokenMap = null;

	static {
		if ( tokenMap == null ) {
			tokenMap = new ConcurrentHashMap<String, Token>();
		}
	}


	/**
	 * 根据key获取SSO票据
	 * <p>
	 * 
	 * @param key
	 *            关键词
	 * @return Token SSO票据
	 */
	@Override
	public Token get( String key ) {
		return tokenMap.get(key);
	}


	/**
	 * 设置SSO票据
	 * <p>
	 * 
	 * @param key
	 *            关键词
	 */
	@Override
	public void set( String key, Token token ) {
		tokenMap.put(key, token);
	}


	/**
	 * 删除SSO票据
	 * <p>
	 * 
	 * @param key
	 *            关键词
	 */
	@Override
	public void delete( String key ) {
		tokenMap.remove(key);
	}

}
