package tech.lihx.demo.core.web.wrap.cookie;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionListener;

import tech.lihx.demo.core.web.wrap.PersistenceSession;

public interface SessionFactory {

	public PersistenceSession buildSession( HttpServletRequest request, HttpServletResponse response );


	public HttpSessionListener getSessionListener();


	public HttpSessionAttributeListener getAttributeListener();


	public CookieConfig getCookieConfig();
}
