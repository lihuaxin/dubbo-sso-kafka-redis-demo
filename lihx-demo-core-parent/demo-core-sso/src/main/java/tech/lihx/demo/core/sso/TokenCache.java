package tech.lihx.demo.core.sso;

/**
 * Token 缓存父类
 * <p>
 * 
 * @author LHX
 * @Date 2016-6-17
 */
public abstract class TokenCache {

	/**
	 * 根据key获取SSO票据
	 * <p>
	 * 
	 * @param key
	 *            关键词
	 * @return Token SSO票据
	 */
	public abstract Token get( String key );


	/**
	 * 设置SSO票据
	 * <p>
	 * 
	 * @param key
	 *            关键词
	 */
	public abstract void set( String key, Token token );


	/**
	 * 删除SSO票据
	 * <p>
	 * 
	 * @param key
	 *            关键词
	 */
	public abstract void delete( String key );
}
