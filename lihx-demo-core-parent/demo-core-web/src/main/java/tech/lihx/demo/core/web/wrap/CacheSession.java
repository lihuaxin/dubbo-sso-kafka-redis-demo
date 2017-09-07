package tech.lihx.demo.core.web.wrap;

import javax.servlet.http.HttpSession;


/**
 * 对session的进一步封装,提供更多操作缓存数据的方法
 * <p>
 * 
 * @author LHX
 * @date 2016年11月24日
 * @version 1.0.0
 */
public interface CacheSession extends HttpSession {

	public void setAttribute( String key, Object value, int expireTime );


	// getAttribute();
}
