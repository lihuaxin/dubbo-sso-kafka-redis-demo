package tech.lihx.demo.core.common.web;


/**
 * 存取session值的接口
 * <p>
 * 
 * @author lihx
 * @date 2014年11月21日
 * @version 1.0.0
 */
public interface ISessionCache {

	/**
	 * 获取缓存的对象
	 * <p>
	 *
	 *
	 * @return 反序列化后的对象
	 */
	public Object get( String key );


	/**
	 * 向缓存中发送数据
	 * <p>
	 *
	 * @param data
	 *            数据
	 * @param expireTime
	 *            时常
	 */
	public void set( String key, Object value, int expireTime );


	/**
	 * 过期时间走默认
	 * <p>
	 *
	 * @param key
	 * @param value
	 */
	public void set( String key, Object value );


	/**
	 * 移除
	 * <p>
	 *
	 * @param sessionId
	 *            session id
	 */
	public void remove( String key );

}
