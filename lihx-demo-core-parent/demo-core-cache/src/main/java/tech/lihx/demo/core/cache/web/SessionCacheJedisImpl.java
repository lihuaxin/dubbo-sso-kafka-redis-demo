package tech.lihx.demo.core.cache.web;

import tech.lihx.demo.core.cache.jedis.JedisBase;
import tech.lihx.demo.core.common.web.ISessionCache;


/**
 * session缓存实现
 * <p>
 * 
 * @author lihx
 * @date 2017年9月5日
 * @version 1.0.0
 */
public class SessionCacheJedisImpl extends JedisBase implements ISessionCache {

	/**
	 * 过期时间一天
	 */
	int expireTime = 24 * 3600;


	@Override
	public Object get( String sessionId ) {
		if ( sessionId == null ) { return null; }
		byte[] encodeKey = encode(sessionId);
		return this.uncompressDeserialize(commands.get(encodeKey));

	}


	@Override
	public void remove( String sessionId ) {
		if ( sessionId != null ) {
			byte[] encodeKey = encode(sessionId);
			commands.del(encodeKey);
		}
	}


	public int getExpireTime() {
		return expireTime;
	}


	public void setExpireTime( int expireTime ) {
		this.expireTime = expireTime;
	}


	@Override
	public void set( String key, Object value, int expireTime ) {
		if ( value != null ) {
			byte[] encodeKey = encode(key);
			commands.setex(encodeKey, expireTime, serializeCompress(value));
		}

	}


	@Override
	public void set( String key, Object value ) {
		set(key, value, expireTime);

	}


}
