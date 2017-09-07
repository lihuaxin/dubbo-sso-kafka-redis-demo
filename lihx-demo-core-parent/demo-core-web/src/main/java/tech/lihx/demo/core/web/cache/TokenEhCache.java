package tech.lihx.demo.core.web.cache;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;

import tech.lihx.demo.core.common.util.ApplicationUtil;
import tech.lihx.demo.core.sso.Token;
import tech.lihx.demo.core.sso.TokenCache;

/**
 * Token缓存到
 * <p>
 * 
 * @author LHX
 * @Date 2017年9月6日
 */
public class TokenEhCache extends TokenCache {

	private static final String CACHE_NAME = "sso_user_decrypt_cache";

	private Cache<String, Token> cache;


	private Cache<String, Token> getCache() {
		if ( cache == null ) {
			synchronized ( this ) {
				if ( cache != null ) { return cache; }
				CacheManager manager = ApplicationUtil.getBean(CacheManager.class);
				cache = manager.getCache(CACHE_NAME);
			}
		}
		return cache;
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
		return getCache().get(key);
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
		getCache().put(key, token);
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
		getCache().remove(key);
	}


}
