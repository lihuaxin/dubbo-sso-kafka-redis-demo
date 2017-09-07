package tech.lihx.demo.core.web.wrap;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSessionContext;

@SuppressWarnings( "deprecation" )
public class HttpSessionWrapper implements CacheSession {

	private final PersistenceSession session;

	private final ServletContext servletContext;


	public HttpSessionWrapper( ServletContext servletContext, PersistenceSession session ) {
		this.session = session;
		this.servletContext = servletContext;
	}

	private class EmptyEnumerator implements Enumeration<String> {

		private final Iterator<String> iterator;


		EmptyEnumerator( Set<String> keys ) {
			iterator = keys.iterator();
		}


		@Override
		public boolean hasMoreElements() {
			return iterator.hasNext();
		}


		@Override
		public String nextElement() {
			return iterator.next();
		}
	}


	@Override
	public Enumeration<String> getAttributeNames() {
		return new EmptyEnumerator(session.getSessionMap().keySet());
	}


	@Override
	public long getCreationTime() {
		return session.getCreationTime();
	}


	@Override
	public String getId() {
		return session.getSessionId();
	}


	@Override
	public long getLastAccessedTime() {
		return session.getLastAccessedTime();
	}


	@Override
	public int getMaxInactiveInterval() {
		return session.getMaxInactiveInterval();
	}


	@Override
	public Object getAttribute( String s ) {
		return session.getAttribute(s);
	}


	@Override
	public ServletContext getServletContext() {
		return servletContext;
	}


	@Override
	@Deprecated
	public HttpSessionContext getSessionContext() {
		return null;
	}


	@Override
	@Deprecated
	public Object getValue( String s ) {
		return session.getAttribute(s);
	}


	@Override
	@Deprecated
	public String[] getValueNames() {
		Set<String> set_key = session.getSessionMap().keySet();
		String[] keys = new String[set_key.size()];
		return set_key.toArray(keys);
	}


	@Override
	public void invalidate() {
		// 使之无效 0
		session.invalidate();
	}


	@Override
	public boolean isNew() {
		return true;
	}


	@Override
	@Deprecated
	public void putValue( String s, Object obj ) {
		session.setAttribute(s, obj);
	}


	@Override
	public void removeAttribute( String s ) {
		session.removeArribute(s);
	}


	@Override
	@Deprecated
	public void removeValue( String s ) {
		session.removeArribute(s);
	}


	@Override
	public void setAttribute( String s, Object obj ) {
		session.setAttribute(s, obj);
	}


	@Override
	public void setMaxInactiveInterval( int i ) {
		// 会话结束时
		session.setMaxInactiveInterval(i);
	}


	@Override
	public void setAttribute( String key, Object value, int expireTime ) {
		session.setAttribute(key, value, expireTime);

	}

}
