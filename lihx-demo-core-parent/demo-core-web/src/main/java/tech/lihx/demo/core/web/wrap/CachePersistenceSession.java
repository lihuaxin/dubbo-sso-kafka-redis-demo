package tech.lihx.demo.core.web.wrap;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionListener;

import tech.lihx.demo.core.common.util.CacheKeyPrefix;
import tech.lihx.demo.core.common.util.MurMurHash;
import tech.lihx.demo.core.common.web.ISessionCache;
import tech.lihx.demo.core.sso.SSOToken;
import tech.lihx.demo.core.sso.client.SSOHelper;


/**
 * 缓存实现session存储
 * <p>
 * 
 * 
 * @author LHX
 * @date 2016年11月21日
 * @version 1.0.0
 */
public class CachePersistenceSession implements PersistenceSession {

	public static final String SESSION_KEY = "session:";

	protected final HttpServletRequest request;

	protected String sessionId = null;

	@SuppressWarnings( "unused" )
	private final String cookie_name;

	protected final ISessionCache cache;


	public CachePersistenceSession( HttpServletRequest request, String cookie_name, ISessionCache cache ) {
		this.request = request;
		this.cookie_name = cookie_name;
		this.cache = cache;
	}


	@Override
	public void setAttribute( String key, Object value ) {
		cache.set(getKey(key), value);
	}


	@Override
	public Object getAttribute( String key ) {
		return cache.get(getKey(key));

	}


	@Override
	public void removeArribute( String key ) {
		cache.remove(getKey(key));
	}


	@Override
	public Map<String, Object> getSessionMap() {

		throw new RuntimeException("not support");
	}


	@Override
	public String getSessionId() {


		return sessionId;

	}


	@Override
	public void invalidate() {
		throw new RuntimeException("not support");

	}


	@Override
	public boolean isInvalidate() {

		throw new RuntimeException("not support");

	}


	@Override
	public void setMaxInactiveInterval( int second ) {
		throw new RuntimeException("not support");

	}


	@Override
	public int getMaxInactiveInterval() {

		throw new RuntimeException("not support");
		// return 0;

	}


	@Override
	public long getLastAccessedTime() {

		throw new RuntimeException("not support");
		// return 0;

	}


	@Override
	public long getCreationTime() {

		throw new RuntimeException("not support");

	}


	private String getKey( String userKey ) {
		StringBuilder key = new StringBuilder(CacheKeyPrefix.get()).append(SESSION_KEY).append(this.sessionId)
				.append(":").append(MurMurHash.hash(userKey));
		return key.toString();
	}


	@Override
	public void init() {
		// //获取sessionId,根据单点登录的cookies计算sessionid
		String code = null;
		// Cookie[] cookie = request.getCookies();
		SSOToken token = (SSOToken) SSOHelper.getToken(request);
		// for ( int i = 0 ; i < cookie.length ; i++ ) {
		// if ( cookie[i].getName().equals(cookie_name) ) {
		// code = MurMurHash.hash(cookie[i].getValue()).toString();
		// break;
		// }
		// }
		// if ( code == null ) {
		// //自动处理sessionid,需要写cookies
		// throw new RuntimeException("无法计算sessionid");
		// }
		if ( token != null ) {
			code = String.valueOf(token.getUserId());
			this.sessionId = new StringBuilder(CacheKeyPrefix.get()).append(SESSION_KEY).append(code).toString();
		}
	}


	@Override
	public void setSessionListener( HttpSessionListener sessionListener ) {

		throw new RuntimeException("not support");
	}


	@Override
	public void setAttributeListener( HttpSessionAttributeListener attributeListener ) {

		throw new RuntimeException("not support");
	}


	@Override
	public void setWrappedSession( HttpSessionWrapper session ) {

	}


	@Override
	public void setAttribute( String key, Object value, int expireTime ) {
		cache.set(getKey(key), value, expireTime);
	}

}
