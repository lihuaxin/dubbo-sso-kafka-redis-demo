package tech.lihx.demo.core.web.wrap;

import java.util.Map;

import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionListener;

/**
 * 
 * 定义session中数据的持久化操作
 * @author LHX 
 */
public interface PersistenceSession {

	/**
	 * 保存sessionid,包含session创建时间
	 */
	public static final String KEY_ID = "_js_id";

	/**
	 * 存放时间,包含最后访问时间和session过期时间
	 */
	public static final String KEY_TIME = "_jt_id";

	/**
	 * 计数从0开始
	 */
	public static final String KEY_COOKIE_NAME_PREFIX = "_s_";

	public static final String KEY_AES = "/[]+_-=~!#$%^&*(),.this is key@<>':'|\\";


	/**
	 * 添加数据
	 * 
	 * @param key
	 * @param value
	 */
	public void setAttribute( String key, Object value );


	/**
	 * 获取数据
	 * 
	 * @param key
	 * @return
	 */
	public Object getAttribute( String key );


	/**
	 * 移除数据
	 * 
	 * @param key
	 */
	public void removeArribute( String key );


	public void setAttribute( String key, Object value, int expireTime );


	/**
	 * 获取session中所有的数据
	 * 
	 * @param sessionid
	 * @return
	 */
	public Map<String, Object> getSessionMap();


	public String getSessionId();


	public void invalidate();


	public boolean isInvalidate();


	public void setMaxInactiveInterval( int second );


	public int getMaxInactiveInterval();


	public long getLastAccessedTime();


	public long getCreationTime();


	// 初始化
	public void init();


	public void setSessionListener( HttpSessionListener sessionListener );


	public void setAttributeListener( HttpSessionAttributeListener attributeListener );


	public void setWrappedSession( HttpSessionWrapper session );

}
