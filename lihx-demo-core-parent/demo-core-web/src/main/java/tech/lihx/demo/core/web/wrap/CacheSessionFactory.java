package tech.lihx.demo.core.web.wrap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionListener;

import tech.lihx.demo.core.web.wrap.cookie.CookieConfig;
import tech.lihx.demo.core.web.wrap.cookie.SessionFactory;

/**
 * TODO(这里用一句话描述这个类的作用)
 * <p>
 * TODO(这里描述这个类补充说明 – 可选)
 * 
 * @author LHX
 * @date 2014年11月21日
 * @version 1.0.0
 */
public class CacheSessionFactory implements SessionFactory {

	@Override
	public PersistenceSession buildSession( HttpServletRequest request, HttpServletResponse response ) {

		return null;

	}


	@Override
	public HttpSessionListener getSessionListener() {

		return null;

	}


	@Override
	public HttpSessionAttributeListener getAttributeListener() {

		return null;

	}


	@Override
	public CookieConfig getCookieConfig() {

		return null;

	}

}
