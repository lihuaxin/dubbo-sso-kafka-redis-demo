package tech.lihx.demo.core.web.wrap.cookie;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionListener;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import tech.lihx.demo.core.web.wrap.PersistenceSession;

public class CookieSessionFactory implements SessionFactory, InitializingBean {

	private CookieConfig cookieConfig;

	private HttpSessionAttributeListener attributeListener;

	private HttpSessionListener sessionListener;


	@Override
	public PersistenceSession buildSession( HttpServletRequest request, HttpServletResponse response ) {
		CookiePersistenceSession session = new CookiePersistenceSession(request, response, cookieConfig);
		return session;
	}


	@Override
	public CookieConfig getCookieConfig() {
		return cookieConfig;
	}


	public void setCookieConfig( CookieConfig cookieConfig ) {
		this.cookieConfig = cookieConfig;
	}


	@Override
	public HttpSessionAttributeListener getAttributeListener() {
		return attributeListener;
	}


	public void setAttributeListener( HttpSessionAttributeListener attributeListener ) {
		this.attributeListener = attributeListener;
	}


	@Override
	public HttpSessionListener getSessionListener() {
		return sessionListener;
	}


	public void setSessionListener( HttpSessionListener sessionListener ) {
		this.sessionListener = sessionListener;
	}


	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(cookieConfig, "cookie配置不能为null");

	}

}
